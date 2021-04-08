package com.cpen391.flappybluetooth.activity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.cpen391.flappyUI.GameSettings;
import com.cpen391.flappyUI.LoggedInUser;
import com.cpen391.flappyUI.TappingActivity;
import com.cpen391.flappyVoiceRecording.VoiceControlActivity;
import com.cpen391.flappyaccount.R;
import com.cpen391.flappybluetooth.util.BluetoothConnectionUtil;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import timber.log.Timber;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    BluetoothAdapter mBluetoothAdapter;
    Button btnEnableDisable_Discoverable;


    Button btnStartConnection;
    Button btnSend;

    EditText etSend;

    Button btnONOFF;
    Button btnDiscover;
    TextView available_devices_txt;
    TextView paired_devices_txt;

    public static boolean ended = false;
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString(UUIDs.HC05UNIVERSALUUID);

    BluetoothDevice mBTDevice;

    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();
    public ArrayList<BluetoothDevice> mPairedBTDevices = new ArrayList<>();

    public DeviceListAdapter mDeviceListAdapter;
    public DeviceListAdapter mPairedDevicesAdapter;

    public Boolean control_type = true;

    ListView lvNewDevices;
    ListView lvPairedDevices;


    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceive: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE ON");
                        populatePairedDevices();
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING ON");
                        break;
                }
            }
        }
    };

    /**
     * Broadcast Receiver for changes made to bluetooth states such as:
     * 1) Discoverability mode on/off or expire.
     */
    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {

                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                switch (mode) {
                    //Device is in Discoverable Mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Toast.makeText(getApplicationContext(), "Your device is discoverable!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Enabled.");
                        break;
                    //Device not in discoverable mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Able to receive connections.");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Not able to receive connections.");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG, "mBroadcastReceiver2: Connecting....");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG, "mBroadcastReceiver2: Connected.");
                        break;
                }

            }
        }
    };


    /**
     * Broadcast Receiver for listing devices that are not yet paired
     * -Executed by btnDiscover() method.
     */
    private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "mBroadcastReceiver3 onReceive: ACTION FOUND.");

            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
                if (!mBTDevices.contains(device) && device.getName() != null && !device.getName().equals("") && !mPairedBTDevices.isEmpty() && !mPairedBTDevices.contains(device)) {
                    mBTDevices.add(device);

                    mDeviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, mBTDevices);
                    lvNewDevices.setAdapter(mDeviceListAdapter);
                }
            }
        }
    };

    /**
     * Broadcast Receiver that detects bond state changes (Pairing status changes)
     */
    private final BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //3 cases:
                //case1: bonded already
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDED.");
                    Toast.makeText(getApplicationContext(), "paired with " + mDevice.getName(), Toast.LENGTH_LONG).show();

                    //inside BroadcastReceiver4
                    mBTDevice = mDevice;
                }
                //case2: creating a bone
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDING.");
                }
                //case3: breaking a bond
                if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                    Log.d(TAG, "BroadcastReceiver: BOND_NONE.");
                }
            }
        }
    };


    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: called.");
        super.onDestroy();
        try {
            unregisterReceiver(mBroadcastReceiver1);
            unregisterReceiver(mBroadcastReceiver2);
            unregisterReceiver(mBroadcastReceiver3);
            unregisterReceiver(mBroadcastReceiver4);
        } catch (Exception e) {

        }
        //mBluetoothAdapter.cancelDiscovery();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        control_type = getIntent().getBooleanExtra("control_method", true);

        btnONOFF = (Button) findViewById(R.id.btnONOFF);
        btnEnableDisable_Discoverable = (Button) findViewById(R.id.btnDiscoverable_on_off);
        lvNewDevices = (ListView) findViewById(R.id.lvNewDevices);
        lvPairedDevices = (ListView) findViewById(R.id.paired_devices_view);
        mBTDevices = new ArrayList<>();

        btnStartConnection = (Button) findViewById(R.id.btnStartConnection);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnDiscover = (Button) findViewById(R.id.btnFindUnpairedDevices);
        etSend = (EditText) findViewById(R.id.editText);

        available_devices_txt = (TextView) findViewById(R.id.available_devices_txt);
        paired_devices_txt = (TextView) findViewById(R.id.paired_devices_txt);

        //Broadcasts when bond state changes (ie:pairing)
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);

        registerReceiver(mBroadcastReceiver4, filter);
        registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);

        initObserver();

        lvNewDevices.setOnItemClickListener(mNewDevicesClickListener);
        lvPairedDevices.setOnItemClickListener(mPairedDevicesClickListener);

        btnONOFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: enabling/disabling bluetooth.");
                enableDisableBT();
            }
        });

        btnStartConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startConnection();
            }
        });

        populatePairedDevices();
    }

    private void initObserver() {

        BluetoothConnectionService.connected.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (BluetoothConnectionService.connected.getValue() == true)
                    Toast.makeText(getApplicationContext(), "connection established!", Toast.LENGTH_SHORT).show();
                else Toast.makeText(getApplicationContext(), "connection failed!", Toast.LENGTH_SHORT).show();
            }
        });

        BluetoothConnectionService.readyToSend.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                BluetoothConnectionUtil.getInstance().sendSettingInfo(MainActivity.this,
                        getIntent().getStringExtra("color0"),
                        getIntent().getStringExtra("color1"),
                        getIntent().getStringExtra("difficult_level"),
                        getIntent().getStringExtra("login_mode")
                );


                if (getIntent().getBooleanExtra("control_mode", true)) {
                    Intent tapping = new Intent(getApplicationContext(), TappingActivity.class);
                    tapping.putExtra("bird_color", GameSettings.getInstance().getBirdColor());
                    startActivity(tapping);
                } else {
                    Intent record = new Intent(getApplicationContext(), VoiceControlActivity.class);
                    startActivity(record);
                }
            }
        });

        BluetoothConnectionService.getReadyToStart.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Timber.d("You are good to start the game!");
                if (LoggedInUser.getInstance().isLogin()) {
                    for (int i = 0; i <= 10; ++i) {
                        BluetoothConnectionUtil.getInstance().sendMessage(MainActivity.this, LoggedInUser.getInstance().getUser().getUserName());
                    }
                }
            }
        });

        BluetoothConnectionService.ended.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                finish();
            }
        });
    }

    private void populatePairedDevices() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        pairedDevices.forEach((device) -> {
            String deviceName = device.getName();
            String deviceHardwareAddress = device.getAddress(); // MAC address
            Timber.d("bonded device: " + deviceName + " :: " + deviceHardwareAddress);
            if (!mPairedBTDevices.contains(device)) {
                mPairedBTDevices.add(device);
            }
            Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
            mPairedDevicesAdapter = new DeviceListAdapter(this, R.layout.device_adapter_view, mPairedBTDevices);
            lvPairedDevices.setAdapter(mPairedDevicesAdapter);
        });
    }


    //create method for starting connection
    //***remember the connection will fail and app will crash if you haven't paired first
    public void startConnection() {
        startBTConnection(mBTDevice, MY_UUID_INSECURE);
    }

    /**
     * starting chat service method
     */
    public void startBTConnection(BluetoothDevice device, UUID uuid) {
        Log.d(TAG, "startBTConnection: Initializing RFCOM Bluetooth Connection.");
        try {
            BluetoothConnectionUtil.getInstance().getBluetoothConnection().startClient(device, uuid);
        } catch (NullPointerException e) {
            Timber.d("check your damn connection");
            Toast.makeText(MainActivity.this, "Please check your bluetooth connection", Toast.LENGTH_SHORT).show();
        }
    }


    public void enableDisableBT() {
        if (mBluetoothAdapter == null) {
            Log.d(TAG, "enableDisableBT: Does not have BT capabilities.");
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Log.d(TAG, "enableDisableBT: enabling BT.");
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }
        if (mBluetoothAdapter.isEnabled()) {
            Log.d(TAG, "enableDisableBT: disabling BT.");
            mBluetoothAdapter.disable();

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }

    }


    public void btnEnableDisable_Discoverable(View view) {
        Log.d(TAG, "btnEnableDisable_Discoverable: Making device discoverable for 300 seconds.");

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);

        IntentFilter intentFilter = new IntentFilter(mBluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(mBroadcastReceiver2, intentFilter);

    }

    public void btnDiscover(View view) {
        Log.d(TAG, "btnDiscover: Looking for unpaired devices.");

        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
            Log.d(TAG, "btnDiscover: Canceling discovery.");

            //check BT permissions in manifest
            checkBTPermissions();
            mBTDevices.clear();

            mBluetoothAdapter.startDiscovery();
        }
        if (!mBluetoothAdapter.isDiscovering()) {

            //check BT permissions in manifest
            checkBTPermissions();

            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
    }

    /**
     * This method is required for all devices running API23+
     * Android must programmatically check the permissions for bluetooth. Putting the proper permissions
     * in the manifest is not enough.
     * <p>
     * NOTE: This will only execute on versions > LOLLIPOP because it is not needed otherwise.
     */
    private void checkBTPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        } else {
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }


    /**
     * The on-click listener for all devices in the ListViews
     */
    private AdapterView.OnItemClickListener mNewDevicesClickListener
            = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int i, long l) {
            //first cancel discovery because its very memory intensive.
            mBluetoothAdapter.cancelDiscovery();

            Log.d(TAG, "onItemClick: You Clicked on a device.");
            String deviceName = mBTDevices.get(i).getName();
            String deviceAddress = mBTDevices.get(i).getAddress();

            Log.d(TAG, "onItemClick: deviceName = " + deviceName);
            Log.d(TAG, "onItemClick: deviceAddress = " + deviceAddress);

            //create the bond.
            //NOTE: Requires API 17+? I think this is JellyBean
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
                Log.d(TAG, "Trying to pair with " + deviceName);
                Timber.d("------------------------" + mBTDevices.get(i).createBond());

                mBTDevice = mBTDevices.get(i);
                BluetoothConnectionUtil.getInstance().setBluetoothConnection(new BluetoothConnectionService(MainActivity.this));
            }
        }
    };


    /**
     * The on-click listener for all devices in the ListViews
     */
    private AdapterView.OnItemClickListener mPairedDevicesClickListener
            = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int i, long l) {
            //first cancel discovery because its very memory intensive.
            mBluetoothAdapter.cancelDiscovery();

            Log.d(TAG, "onItemClick: You Clicked on a device.");
            String deviceName = mPairedBTDevices.get(i).getName();
            String deviceAddress = mPairedBTDevices.get(i).getAddress();

            Log.d(TAG, "onItemClick: deviceName = " + deviceName);
            Log.d(TAG, "onItemClick: deviceAddress = " + deviceAddress);

            //create the bond.
            //NOTE: Requires API 17+? I think this is JellyBean
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
                Log.d(TAG, "Trying to pair with " + deviceName);
                Timber.d("&&&&&&&&&&&&&&&&&&&&&&&&&&" + mPairedBTDevices.get(i).createBond());


                mBTDevice = mPairedBTDevices.get(i);
                Log.d(TAG, "LINE 406::: " + String.valueOf(mBTDevice.getBondState()));
                if (mBTDevice.getBondState() == 12)
                    Toast.makeText(MainActivity.this, "paired with " + deviceName, Toast.LENGTH_SHORT).show();
                BluetoothConnectionUtil.getInstance().setBluetoothConnection(new BluetoothConnectionService(MainActivity.this));
            }
        }
    };

    // TODO: probably need more user related info in order to send messages, define the parameter list as needed
    public static void actionStart(Context context,
                                   String color0,
                                   String color1,
                                   String diffLevel,
                                   String loginMode,
                                   Boolean controlMode,
                                   String birdColor) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("color0", color0);
        intent.putExtra("color1", color1);
        intent.putExtra("difficult_level", diffLevel);
        intent.putExtra("login_mode", loginMode);
        intent.putExtra("control_mode", controlMode);
        intent.putExtra("bird_color", birdColor);
        context.startActivity(intent);
    }
}
