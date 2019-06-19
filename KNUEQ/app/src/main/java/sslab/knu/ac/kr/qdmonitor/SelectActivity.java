package sslab.knu.ac.kr.qdmonitor;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class SelectActivity extends AppCompatActivity {
    BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 100;
    private static final int PERMISSIONS = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS);

    }

    public void callBeaconReceiverActivity(View view) {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        Intent intent = new Intent(this, BeaconReceiver.class);
        startService(intent);
    }

    public void callDeviceSetActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
