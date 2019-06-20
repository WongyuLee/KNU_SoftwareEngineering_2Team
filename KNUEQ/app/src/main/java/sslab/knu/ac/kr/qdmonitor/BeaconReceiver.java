package sslab.knu.ac.kr.qdmonitor;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;


public class BeaconReceiver extends Service {
    public static final String TAG = BeaconReceiver.class.getSimpleName();
    NotificationCompat.Builder mBuilder;
    BeaconManager mBeaconManager;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothLeScanner mBluetoothLeScanner;
    BluetoothLeAdvertiser mBluetoothLeAdvertiser;
    private static final String BEACON_PARSER = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25";
    Vector<Beacon> beacon;
    Vector<ScanFilter> scanFilters;
    ScanSettings.Builder mScanSettings;
    String EQLevel1 = "01 01 02 03 04 ";
    String EQLevel2 = "02 01 02 03 04 ";
    String EQLevel3 = "03 01 02 03 04 ";
    Timer ScanningTimer = null;
    TimerTask ScanningTask;
    final Handler handler = new Handler();
    private boolean isScanning = false;
    private static final ParcelUuid UID_SERVICE =
            ParcelUuid.fromString("00001234-0000-1000-8000-00805f9b34fb");
    public BeaconReceiver() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        mBeaconManager = BeaconManager.getInstanceForApplication(this);
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BEACON_PARSER));

        Bitmap mLargeIconForNoti = BitmapFactory.decodeResource(getResources(),R.drawable.knu_mainlogo);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel("SSLab","KNUEQService",NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(notificationChannel);
            mBuilder = new NotificationCompat.Builder(BeaconReceiver.this,notificationChannel.getId());
        }else{
            mBuilder = new NotificationCompat.Builder(BeaconReceiver.this);
        }
        mBuilder.setAutoCancel(true)
                .setSmallIcon(R.drawable.knu_mainlogo)
                .setContentTitle("경북대학교 SoftwareSystems Lab")
                .setContentText("지진감지신호를 수신중입니다")
                .setLargeIcon(mLargeIconForNoti)
                .setAutoCancel(false)
                .setOngoing(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        startForeground(1,mBuilder.build());
        Log.i(TAG, "Start BLE Scanning...");
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        mBluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
        beacon = new Vector<>();
        mScanSettings = new ScanSettings.Builder();
        mScanSettings.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY);
        ScanSettings scanSettings = mScanSettings.build();

        scanFilters = new Vector<>();
        ScanFilter.Builder scanFilter = new ScanFilter.Builder();
        scanFilter.setDeviceAddress("B8:27:EB:CA:53:A6"); //ex) 00:00:00:00:00:00
        ScanFilter scan = scanFilter.build();
        scanFilters.add(scan);
        if(isScanning == false){
            mBluetoothLeScanner.startScan(scanFilters, scanSettings, mScanCallback);
            isScanning = true;
        }
        if(ScanningTimer != null){
            ScanningTimer.cancel();
        }
        ScanningTimer = new Timer();
    }

    ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            try {
                String EqLevel;
                //ScanRecord scanRecord = result.getScanRecord();
                //Log.d("getTxPowerLevel()",scanRecord.getTxPowerLevel()+"");
                EqLevel = byteArrayToHex(result.getScanRecord().getServiceData(UID_SERVICE));
                //Log.d("onScanResult()", result.getDevice().getAddress() + "\n" + result.getRssi() + "\n" + result.getDevice().getName()
                        //+ "\n" + result.getDevice().getBondState() + "\n" + result.getDevice().getType() +"\n" + test +"\n" + EQLevel1);

                if(EqLevel.equals(EQLevel1)){
                    Log.d("detect eq()","level 1");
                    Intent intent1 = new Intent(getApplicationContext(), PopupService.class);
                    intent1.putExtra("Level", String.valueOf(1));
                    startActivity(intent1);
                    mBluetoothLeScanner.stopScan(mScanCallback);
                    isScanning = false;
                    startScanning();
                } else if(EqLevel.equals(EQLevel2)){
                    Log.d("detect eq()","level 2");
                    Intent intent1 = new Intent(getApplicationContext(), PopupService.class);
                    intent1.putExtra("Level", String.valueOf(2));
                    startActivity(intent1);
                    mBluetoothLeScanner.stopScan(mScanCallback);
                    isScanning = false;
                    startScanning();
                } else if(EqLevel.equals(EQLevel3)){
                    Log.d("detect eq()","level 3");
                    Intent intent1 = new Intent(getApplicationContext(), PopupService.class);
                    intent1.putExtra("Level", String.valueOf(3));
                    startActivity(intent1);
                    mBluetoothLeScanner.stopScan(mScanCallback);
                    isScanning = false;
                    startScanning();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            Log.d("onBatchScanResults", results.size() + "");
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.d("onScanFailed()", errorCode+"");
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder();
        for(final byte b: a)
            sb.append(String.format("%02x ", b&0xff));
        return sb.toString();
    }

    private void startScanning(){
        ScanningTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(!isScanning ){
                            ScanSettings scanSettings = mScanSettings.build();
                            mBluetoothLeScanner.startScan(scanFilters, scanSettings, mScanCallback);
                            isScanning = true;
                        }
                    }
                });
            }
        };
        //5second
        ScanningTimer.schedule(ScanningTask,5000);
    }
}
