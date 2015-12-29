package by.fpmi.lab5.geolocation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    TextView tvOut;
    TextView tvLat;
    TextView tvLon;
    TextView tvAlt;
    TextView tvStatus;
    ImageView ic_done;
    ImageView ic_close;
    Button start;
    Button stop;
    LocationManager mlocManager;
    LocationListener mlocListener;
    String provider;
    long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvOut = (TextView) findViewById(R.id.elapsedTime);
        tvLat = (TextView) findViewById(R.id.latitude);
        tvLon = (TextView) findViewById(R.id.longitude);
        tvAlt = (TextView) findViewById(R.id.altitude);
        tvStatus = (TextView)findViewById(R.id.status);
        ic_done = (ImageView)findViewById(R.id.statusIcon);
        ic_close = (ImageView)findViewById(R.id.closeIcon);

        start = (Button)findViewById(R.id.start);
        stop = (Button)findViewById(R.id.stop);

        startTime = new Date().getTime();
        //tvOut.setText(" " + startTime);

        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Date currentTime = new Date();
                tvLat.setText(" " + location.getLatitude());
                tvLon.setText(" " + location.getLongitude());
                tvAlt.setText(" " + location.getAltitude());
                tvOut.setText(" " + (currentTime.getTime() - startTime));
                tvOut.append(getString(R.string.timeUnit));
                startTime = currentTime.getTime();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        provider = mlocManager.getBestProvider(criteria, true);
        mlocManager.requestLocationUpdates(provider, 0, 0, mlocListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mlocManager.removeUpdates(mlocListener);
    }



    public void onStart(View view) {
        tvStatus.setText(R.string.isStarted);
        ic_close.setVisibility(View.INVISIBLE);
        ic_done.setVisibility(View.VISIBLE);
        mlocManager.requestLocationUpdates(provider, 0, 0, mlocListener);

    }

    public void onStop(View view) {
        if(view.equals(stop)) {
            tvStatus.setText(R.string.isStopped);
            ic_close.setVisibility(View.VISIBLE);
            ic_done.setVisibility(View.INVISIBLE);
            mlocManager.removeUpdates(mlocListener);
        }
    }

    private boolean checkGPSPermission() {
        return ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
    }
}
