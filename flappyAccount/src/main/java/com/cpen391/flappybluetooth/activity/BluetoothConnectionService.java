package com.cpen391.flappybluetooth.activity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.UUID;

import timber.log.Timber;

public class BluetoothConnectionService {

    private static final String appName = "BluetoothConnection";

    private static final UUID MY_UUID_INSECURE = UUID.fromString(UUIDs.HC05UNIVERSALUUID);

    // Finite state representations to signify the current state of the game
    public static MutableLiveData<Boolean> connected = new MutableLiveData<>();
    public static MutableLiveData<Boolean> readyToSend = new MutableLiveData<>();
    public static MutableLiveData<Boolean> getReadyToStart = new MutableLiveData<>();
    public static MutableLiveData<Integer> ended = new MutableLiveData<>();

    private final BluetoothAdapter mBluetoothAdapter;
    Context mContext;

    // threads for device working as server, client, and a connection manager thread
    private AcceptThread mInsecureAcceptThread;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;

    private BluetoothDevice mmDevice;
    private UUID deviceUUID;

    ProgressDialog mProgressDialog;

    public BluetoothConnectionService(Context context) {
        mContext = context;
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    private class AcceptThread extends Thread {

        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;

            //create a new listening server socket
            try {
                tmp = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(appName, MY_UUID_INSECURE);

                Timber.d("AcceptThread: Setting up server using: %s", MY_UUID_INSECURE);
            } catch (IOException e) {
                e.printStackTrace();
            }

            mmServerSocket = tmp;
        }

        public void run() {
            Timber.d("run: AcceptThread Running.");

            BluetoothSocket socket = null;

            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                Timber.d("run: RFCOM server socket start.....");

                socket = mmServerSocket.accept();

                Timber.d("run: RFCOM server socket accepted connection.");

            } catch (IOException e) {
                Timber.e("AcceptThread: IOException: %s", e.getMessage());
            }

            // push the current connection to the connection manager thread
            if (socket != null) {
                connected(socket, mmDevice);
            }

            Timber.i("END mAcceptThread ");
        }

        public void cancel() {
            Timber.d("cancel: Cancelling AcceptThread.");
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Timber.e("cancel: Close of AcceptThread ServerSocket failed. %s", e.getMessage());
            }
        }
    }

    private class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice device, UUID uuid) {
            Timber.d("ConnectThread: started");
            mmDevice = device;
            deviceUUID = uuid;
        }

        public void run() {
            BluetoothSocket tmp = null;
            Timber.i("RUN mConnectThread ");

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                Timber.d("ConnectThread: Trying to create InsecureRfcommSocket using UUID: %s", MY_UUID_INSECURE);
                tmp = mmDevice.createRfcommSocketToServiceRecord(deviceUUID);
            } catch (IOException e) {
                Timber.e("ConnectThread: Could not create InsecureRfcommSocket %s", e.getMessage());
            }

            mmSocket = tmp;

            // Always cancel discovery because it will slow down a connection
            mBluetoothAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket

            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();

                Timber.d("run: ConnectThread connected.");
                connected.postValue(true);

            } catch (IOException e) {

                connected.postValue(false);
                // Close the socket
                e.printStackTrace();
                try {
                    mmSocket.close();
                    Timber.d("run: Closed Socket.");
                } catch (IOException e1) {
                    Timber.e("mConnectThread: run: Unable to close connection in socket %s", e1.getMessage());
                }
                Timber.d("run: ConnectThread: Could not connect to UUID: %s", MY_UUID_INSECURE);

            }

            connected(mmSocket, mmDevice);
        }

        public void cancel() {
            try {
                Timber.d("cancel: Closing Client Socket.");
                mmSocket.close();
            } catch (IOException e) {
                Timber.e("cancel: close() of mmSocket in Connectthread failed. %s", e.getMessage());
            }
        }
    }


    /**
     * Start the chat service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume()
     */
    public synchronized void start() {
        Timber.d("start");

        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if (mInsecureAcceptThread == null) {
            mInsecureAcceptThread = new AcceptThread();
            mInsecureAcceptThread.start();
        }
    }


    /**
     * AcceptThread starts and sits waiting for a connection.
     * Then ConnectThread starts and attempts to make a connection with the other devices AcceptThread.
     **/

    public void startClient(BluetoothDevice device, UUID uuid) {
        Timber.d("startClient: Started.");

        //initialize progress dialog
        mProgressDialog = ProgressDialog.show(mContext, "Connecting Bluetooth"
                , "Please Wait...", true);

        mConnectThread = new ConnectThread(device, uuid);
        mConnectThread.start();
    }


    /**
     * Finally the ConnectedThread which is responsible for maintaining the BTConnection, Sending the data, and
     * receiving incoming data through input/output streams respectively.
     **/
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Timber.d("ConnectedThread: Starting.");

            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            //dismiss the progressdialog when connection is established
            try {
                mProgressDialog.dismiss();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }


            try {
                tmpIn = mmSocket.getInputStream();
                tmpOut = mmSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream

            int bytes; // bytes returned from read()
            Handler handler = new Handler(Looper.getMainLooper());
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                // Read from the InputStream
                try {
                    bytes = mmInStream.read(buffer);
                    String incomingMessage = new String(buffer, 0, bytes);

                    // test against the incoming messages to signify state changes of the game
                    if (incomingMessage.contains("hello"))
                        readyToSend.postValue(true);
                    if (incomingMessage.contains("OK"))
                        getReadyToStart.postValue(true);

                    try {

                        // when a message ends "end", it signifies a complete message is sent
                        if (incomingMessage.charAt(0) != 'e' ||
                                incomingMessage.charAt(1) != 'n' ||
                                incomingMessage.charAt(2) != 'd') continue;
                        int score = incomingMessage.charAt(3);

                        handler.post(() -> {

                            Timber.d("GAME END: your score is %s", incomingMessage);
                            ended.postValue(score);

                        });
                        break;

                    } catch (Exception ignored) {
                    }

                    Timber.d("InputStream: %s", incomingMessage);

                } catch (IOException e) {
                    Timber.e("write: Error reading Input Stream. %s", e.getMessage());
                    break;
                }
            }
        }

        //Call this from the main activity to send data to the remote device
        public void write(byte[] bytes) {
            String text = new String(bytes, Charset.defaultCharset());
            Timber.d("write: Writing to outputstream: %s", text);
            try {
                mmOutStream.write(bytes);
                Toast.makeText(mContext, "Jump!", Toast.LENGTH_SHORT).show();
                Timber.d("write: mmOutStream is " + mmOutStream.toString() + ", wrote: " + Arrays.toString(bytes));
            } catch (IOException e) {
                Timber.e("write: Error writing to output stream. %s", e.getMessage());
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException ignored) {
            }
        }
    }


    private void connected(BluetoothSocket mmSocket, BluetoothDevice mmDevice) {
        Timber.d("connected: Starting.");

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(mmSocket);
        mConnectedThread.start();
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     *
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;

        // Synchronize a copy of the ConnectedThread
        Timber.d("write: Write Called.");
        //perform the write
        try {
            mConnectedThread.write(out);
        } catch (Exception e) {
            Toast.makeText(mContext, "please check your connection!", Toast.LENGTH_LONG).show();
        }
    }

}
