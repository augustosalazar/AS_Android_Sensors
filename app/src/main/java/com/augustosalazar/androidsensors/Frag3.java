package com.augustosalazar.androidsensors;


import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class Frag3 extends Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    protected Boolean mRequestingLocationUpdates;

    protected Location mCurrentLocation;
    protected Button mStartUpdatesButton;
    protected Button mStopUpdatesButton;
    protected TextView mLastUpdateTimeTextView;
    protected TextView mLatitudeTextView;
    protected TextView mLongitudeTextView;
    protected TextView mSpeedTextView;

    protected String mLastUpdateTime;

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static Frag3 newInstance(int sectionNumber) {
        Frag3 fragment = new Frag3();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frag3, container, false);

        Log.d("AndroidSensors", "Frag3");
        mStartUpdatesButton = (Button) view.findViewById(R.id.buttonStart);
        mStopUpdatesButton = (Button) view.findViewById(R.id.buttonStop);
        mLatitudeTextView = (TextView) view.findViewById(R.id.editTextLat);
        mLongitudeTextView = (TextView) view.findViewById(R.id.editTextLong);
        mLastUpdateTimeTextView = (TextView) view.findViewById(R.id.editTextDate);
        mSpeedTextView = (TextView) view.findViewById(R.id.editTextSpeed);

        mStartUpdatesButton.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       startUpdatesButtonHandler();
                                                   }
                                               });

        mStopUpdatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopUpdatesButtonHandler();
            }
        });

        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";


        buildGoogleApiClient();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        Log.d("AndroidSensors","Connecting");

    }

    @Override
    public void onResume() {
        super.onResume();

        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("AndroidSensors", "onConnected");
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            updateUI();
        }

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(this.getClass().getSimpleName(), "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
        Log.d("AndroidSensors", "onLocationChanged");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(this.getClass().getSimpleName(), "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    private void updateUI() {
        if (mCurrentLocation != null) {
            mLatitudeTextView.setText(String.valueOf(mCurrentLocation.getLatitude()));
            mLongitudeTextView.setText(String.valueOf(mCurrentLocation.getLongitude()));
            mLastUpdateTimeTextView.setText(mLastUpdateTime);
            mSpeedTextView.setText(((mCurrentLocation.getSpeed()*3600)/1000)+"");
        }
    }

    protected void startLocationUpdates() {
        if (!mGoogleApiClient.isConnected()) {
            Log.d("AndroidSensors", "startLocationUpdates not connected");
            mGoogleApiClient.connect();
            mRequestingLocationUpdates = false;
            setButtonsEnabledState();
        } else {
            Log.d("AndroidSensors","startLocationUpdates connected");
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }

    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    private void setButtonsEnabledState() {
        if (mRequestingLocationUpdates) {
            mStartUpdatesButton.setEnabled(false);
            mStopUpdatesButton.setEnabled(true);
        } else {
            mStartUpdatesButton.setEnabled(true);
            mStopUpdatesButton.setEnabled(false);
        }
    }

    public void stopUpdatesButtonHandler() {
        if (mRequestingLocationUpdates) {
            mRequestingLocationUpdates = false;
            setButtonsEnabledState();
            stopLocationUpdates();
        }
    }

    public void startUpdatesButtonHandler() {
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
            setButtonsEnabledState();
            startLocationUpdates();
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
        Log.d("AndroidSensors", "buildGoogleApiClient");
    }

}
