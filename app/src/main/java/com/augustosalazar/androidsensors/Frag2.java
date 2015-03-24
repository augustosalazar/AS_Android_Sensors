package com.augustosalazar.androidsensors;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Frag2 extends Fragment implements LocationListener {

    private EditText mEditTextLat;
    private EditText mEditTextLong;
    private Button mButton;
    private boolean mStarted;
    private LocationManager mLocationManager;

    private static final String ARG_SECTION_NUMBER = "section_number";


    public static Frag2 newInstance(int sectionNumber) {
        Frag2 fragment = new Frag2();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_frag2, container, false);

        mEditTextLat = (EditText) rootView.findViewById(R.id.editText4);
        mEditTextLong = (EditText) rootView.findViewById(R.id.editText5);
        mButton = (Button) rootView.findViewById(R.id.buttonGps);

        mEditTextLat.setText("");
        mEditTextLong.setText("");
        mStarted = false;
        mButton.setText("Iniciar Sensor");

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mStarted) {
                    startCapture(getActivity());
                }else {
                    stopCapturing();
                }
            }
        });

        return rootView;
    }


    public void startCapture(Context context){
        mStarted = true;
        mButton.setText("Detener Sensor");

        mLocationManager = (LocationManager) getActivity().getSystemService(
                Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                0, 0, this);

    }

    public void stopCapturing() {
        mStarted = false;
        mButton.setText("Iniciar Sensor");

        if (mLocationManager != null) {
            mLocationManager.removeUpdates(this);
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        mEditTextLat.setText(location.getLatitude()+"");
        mEditTextLong.setText(location.getLongitude()+"");
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
}
