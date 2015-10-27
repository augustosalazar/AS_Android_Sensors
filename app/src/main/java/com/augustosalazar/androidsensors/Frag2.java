package com.augustosalazar.androidsensors;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.location.LocationRequest;


public class Frag2 extends Fragment implements LocationListener {

    private EditText mEditTextLat;
    private EditText mEditTextLong;
    private EditText mEditTextSpeed;
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
        Log.d("AndroidSensors", "Frag2");
        mEditTextLat = (EditText) rootView.findViewById(R.id.editText4);
        mEditTextLong = (EditText) rootView.findViewById(R.id.editText5);
        mEditTextSpeed = (EditText) rootView.findViewById(R.id.editText6);
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

        boolean enabled = mLocationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!enabled) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            // Set other dialog properties
            builder.setTitle("GPS no esta habilitado");
            builder.setMessage("Habilitar ahora?");
            builder.setCancelable(false);
            // Add the buttons
            builder.setNegativeButton("Ahora no", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    return;
                }
            });

            builder = builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                    return;
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        }




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
        mEditTextSpeed.setText(((location.getSpeed()*3600)/1000)+"");
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
