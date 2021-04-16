// Generated by view binder compiler. Do not edit!
package com.cpen391.flappybluetooth.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.cpen391.flappybluetooth.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityHomeBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TextView availableDevicesTxt;

  @NonNull
  public final ImageView bluetoothIcon;

  @NonNull
  public final Button btnDiscoverableOnOff;

  @NonNull
  public final Button btnFindUnpairedDevices;

  @NonNull
  public final Button btnONOFF;

  @NonNull
  public final Button btnSend;

  @NonNull
  public final Button btnStartConnection;

  @NonNull
  public final EditText editText;

  @NonNull
  public final ListView lvNewDevices;

  @NonNull
  public final TextView pairedDevicesTxt;

  @NonNull
  public final ListView pairedDevicesView;

  private ActivityHomeBinding(@NonNull LinearLayout rootView, @NonNull TextView availableDevicesTxt,
      @NonNull ImageView bluetoothIcon, @NonNull Button btnDiscoverableOnOff,
      @NonNull Button btnFindUnpairedDevices, @NonNull Button btnONOFF, @NonNull Button btnSend,
      @NonNull Button btnStartConnection, @NonNull EditText editText,
      @NonNull ListView lvNewDevices, @NonNull TextView pairedDevicesTxt,
      @NonNull ListView pairedDevicesView) {
    this.rootView = rootView;
    this.availableDevicesTxt = availableDevicesTxt;
    this.bluetoothIcon = bluetoothIcon;
    this.btnDiscoverableOnOff = btnDiscoverableOnOff;
    this.btnFindUnpairedDevices = btnFindUnpairedDevices;
    this.btnONOFF = btnONOFF;
    this.btnSend = btnSend;
    this.btnStartConnection = btnStartConnection;
    this.editText = editText;
    this.lvNewDevices = lvNewDevices;
    this.pairedDevicesTxt = pairedDevicesTxt;
    this.pairedDevicesView = pairedDevicesView;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityHomeBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityHomeBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_home, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityHomeBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.available_devices_txt;
      TextView availableDevicesTxt = rootView.findViewById(id);
      if (availableDevicesTxt == null) {
        break missingId;
      }

      id = R.id.bluetooth_icon;
      ImageView bluetoothIcon = rootView.findViewById(id);
      if (bluetoothIcon == null) {
        break missingId;
      }

      id = R.id.btnDiscoverable_on_off;
      Button btnDiscoverableOnOff = rootView.findViewById(id);
      if (btnDiscoverableOnOff == null) {
        break missingId;
      }

      id = R.id.btnFindUnpairedDevices;
      Button btnFindUnpairedDevices = rootView.findViewById(id);
      if (btnFindUnpairedDevices == null) {
        break missingId;
      }

      id = R.id.btnONOFF;
      Button btnONOFF = rootView.findViewById(id);
      if (btnONOFF == null) {
        break missingId;
      }

      id = R.id.btnSend;
      Button btnSend = rootView.findViewById(id);
      if (btnSend == null) {
        break missingId;
      }

      id = R.id.btnStartConnection;
      Button btnStartConnection = rootView.findViewById(id);
      if (btnStartConnection == null) {
        break missingId;
      }

      id = R.id.editText;
      EditText editText = rootView.findViewById(id);
      if (editText == null) {
        break missingId;
      }

      id = R.id.lvNewDevices;
      ListView lvNewDevices = rootView.findViewById(id);
      if (lvNewDevices == null) {
        break missingId;
      }

      id = R.id.paired_devices_txt;
      TextView pairedDevicesTxt = rootView.findViewById(id);
      if (pairedDevicesTxt == null) {
        break missingId;
      }

      id = R.id.paired_devices_view;
      ListView pairedDevicesView = rootView.findViewById(id);
      if (pairedDevicesView == null) {
        break missingId;
      }

      return new ActivityHomeBinding((LinearLayout) rootView, availableDevicesTxt, bluetoothIcon,
          btnDiscoverableOnOff, btnFindUnpairedDevices, btnONOFF, btnSend, btnStartConnection,
          editText, lvNewDevices, pairedDevicesTxt, pairedDevicesView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}