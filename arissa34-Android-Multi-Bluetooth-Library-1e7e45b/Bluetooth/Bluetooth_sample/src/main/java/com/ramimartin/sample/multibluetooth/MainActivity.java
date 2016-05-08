package com.ramimartin.sample.multibluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ramimartin.multibluetooth.activity.BluetoothFragmentActivity;
import com.ramimartin.multibluetooth.bluetooth.mananger.BluetoothManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends BluetoothFragmentActivity implements DiscoveredDialogFragment.DiscoveredDialogListener {
    @InjectView(R.id.mwebView)
    WebView mwebview;

    @InjectView(R.id.listview)
    ListView mListView;
    ArrayAdapter<String> mAdapter;
    List<String> mListLog;

    @InjectView(R.id.communication)
    EditText mEditText;

    @InjectView(R.id.send)
    ImageButton mSendBtn;

    @InjectView(R.id.client)
    ToggleButton mClientToggleBtn;
    @InjectView(R.id.serveur)
    ToggleButton mServerToggleBtn;

    @InjectView(R.id.connect)
    Button mConnectBtn;
    @InjectView(R.id.disconnect)
    Button mDisconnect;
    String macAdress = null;
    private android.os.Handler handler = new android.os.Handler();
    /*
    *
Server API Key help
AIzaSyBPxOVdAzXoo9UrsuDURmenOqIQG310PLs

Sender ID help
855804541252*/
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean sentToken = com.ramimartin.sample.multibluetooth.PreferenceManager.instance(getApplicationContext()).sentToken();

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        mListLog = new ArrayList<String>();
        mAdapter = new ArrayAdapter<String>(this, R.layout.item_console, mListLog);
        mListView.setAdapter(mAdapter);
        WebSettings webSettings = mwebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mwebview.setWebChromeClient(new WebChromeClient());
        mwebview.setWebViewClient(new WebViewClient());
        mwebview.addJavascriptInterface(new AndroidtoJava(),"callAndroidValue");
        mwebview.loadUrl("http://117.16.244.19/");
        if (checkPlayServices()) {
            startService(new Intent(this, RegistrationIntentService.class));
        }
    }

    @Override
    public int myNbrClientMax() {
        return 7;
    }

    @OnClick(R.id.serveur)
    public void serverType() {
        setLogText("===> Start Server ! Your mac address : " + mBluetoothManager.getYourBtMacAddress());
        setTimeDiscoverable(BluetoothManager.BLUETOOTH_TIME_DICOVERY_3600_SEC);
        selectServerMode();
        mServerToggleBtn.setChecked(true);
        mClientToggleBtn.setChecked(false);
        mConnectBtn.setEnabled(true);
        mConnectBtn.setText("Scan Devices");
    }

    @OnClick(R.id.client)
    public void clientType() {
        setLogText("===> Start Client ! Your mac address : " + mBluetoothManager.getYourBtMacAddress());
        setTimeDiscoverable(BluetoothManager.BLUETOOTH_TIME_DICOVERY_120_SEC);
        selectClientMode();
        mServerToggleBtn.setChecked(false);
        mClientToggleBtn.setChecked(true);
        mConnectBtn.setEnabled(true);
    }

    @OnClick(R.id.connect)
    public void connect() {
        setLogText("===> Start Scanning devices ...");
        if (getTypeBluetooth() == BluetoothManager.TypeBluetooth.Client) {
            showDiscoveredDevicesDialog();
        }
        scanAllBluetoothDevice();
    }

    @OnClick(R.id.disconnect)
    public void disconnect() {
        setLogText("===> Disconnect");
        disconnectClient();
        disconnectServer();
    }

    @OnClick(R.id.send)
    public void send() {
        if (isConnected()) {
            sendMessage(mEditText.getText().toString(), 0);
            setLogText("===> Send : " + mEditText.getText().toString());
        }
    }
    public class AndroidtoJava {
        @JavascriptInterface//?
        public void callAndroidname(final String arg){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this,arg,Toast.LENGTH_SHORT).show();
                    if(arg.equals("clearner")){
                        sendMessage(arg,0);
                    } else if (arg.equals("powerpoint")){
                        sendMessage(arg,1);
                    }

                }
            });
        }
    }
    @Override
    public void onBluetoothStartDiscovery() {
        setLogText("===> Start discovering ! Your mac address : " + mBluetoothManager.getYourBtMacAddress());
    }

    @Override
    public void onBluetoothDeviceFound(BluetoothDevice device) {
        if(getTypeBluetooth() == BluetoothManager.TypeBluetooth.Server) {
            setLogText("===> Device detected and Thread Server created for this address : " + device.getAddress());
            macAdress = device.getAddress();
        }else{
            setLogText("===> Device detected : "+ device.getAddress());
        }
    }

    @Override
    public void onClientConnectionSuccess() {
        setLogText("===> Client Connexion success !");
        mEditText.setText("Client");
        mSendBtn.setEnabled(true);
        mConnectBtn.setEnabled(false);
        mDisconnect.setEnabled(true);
    }

    @Override
    public void onClientConnectionFail() {
        setLogText("===> Client Connexion fail !");
        mServerToggleBtn.setChecked(false);
        mClientToggleBtn.setChecked(false);
        mDisconnect.setEnabled(false);
        mConnectBtn.setEnabled(false);
        mConnectBtn.setText("Connect");
        mEditText.setText("");
    }

    @Override
    public void onServeurConnectionSuccess() {
        setLogText("===> Serveur Connexion success !");
        mEditText.setText("Server");
        mDisconnect.setEnabled(true);
    }

    @Override
    public void onServeurConnectionFail() {
        setLogText("===> Serveur Connexion fail !");
    }

    @Override
    public void onBluetoothCommunicator(String messageReceive) {
        setLogText("===> receive msg : " + messageReceive);
    }

    @Override
    public void onBluetoothNotAviable() {
        setLogText("===> Bluetooth not aviable on this device");
        mSendBtn.setEnabled(false);
        mClientToggleBtn.setEnabled(false);
        mServerToggleBtn.setEnabled(false);
        mConnectBtn.setEnabled(false);
        mDisconnect.setEnabled(false);
    }

    public void setLogText(String text) {
        mListLog.add(text);
        mAdapter.notifyDataSetChanged();
        mListView.setSelection(mListView.getCount() - 1);
    }

    private void showDiscoveredDevicesDialog() {
        String tag = DiscoveredDialogFragment.class.getSimpleName();
        DiscoveredDialogFragment fragment = DiscoveredDialogFragment.newInstance();
        fragment.setListener(this);
        showDialogFragment(fragment, tag);
    }

    private void showDialogFragment(DialogFragment dialogFragment, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(dialogFragment, tag);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onDeviceSelectedForConnection(String addressMac) {
        setLogText("===> Connect to " + addressMac);
        createClient(addressMac);
    }

    @Override
    public void onScanClicked() {
        scanAllBluetoothDevice();
    }
    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction(getString(R.string.action_registration_complete));
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("MainActivity", "This device is not supported.|");
                finish();
            }
            return false;
        }
        return true;
    }
}