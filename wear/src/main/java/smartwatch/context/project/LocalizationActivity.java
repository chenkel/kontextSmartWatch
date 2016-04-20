package smartwatch.context.project;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

import smartwatch.context.common.superclasses.Localization;

public class LocalizationActivity extends Activity implements BeaconConsumer {
    private static final String TAG = LocalizationActivity.class.getSimpleName();

    private BeaconManager beaconManager;


    private final String uuidBlue = "CE:BA:BE:97:DB:0C";
    private final String uuidRed = "DD:3F:50:F2:76:74";
    private final String uuidYellow = "FB:39:E6:2D:82:EF";

    private Localization mLocalization;
    private Vibrator v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processing);

        final TextView descriptionTextView = (TextView) findViewById(R.id.description);
        final TextView processingTextView = (TextView) findViewById(R.id.processing);
        processingTextView.setText("Lokalisierung läuft");

        mLocalization = new Localization(this) {
            @Override
            protected void notifyLocationChange(String priorPlaceId, String foundPlaceId) {
                v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 500 milliseconds
                v.vibrate(500);
            }

            @Override
            protected void showLocalizationProgressOutput() {
            }

            @Override
            protected void updateLocalizationProgressUI(String foundPlaceId, String locationDescription) {
                descriptionTextView.setText(locationDescription);
            }
        };
        mLocalization.startLocalization();

        initializeBeaconManager();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocalization.stopLocalization();
        beaconManager.unbind(this);
    }

    private void initializeBeaconManager() {
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(mLocalization.rangeNotifier);

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingWatchId", null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}