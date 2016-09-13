package com.augustosalazar.androidsensors;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;

import java.util.List;


public class Frag2 extends Fragment implements LocationListener {

    private EditText mEditTextLat;
    private EditText mEditTextLong;
    private EditText mEditTextSpeed;
    private Button mButton;
    private boolean mStarted;
    private LocationManager mLocationManager;
    private final int MY_PERMISSIONS_REQUEST = 1;

    private static final String ARG_SECTION_NUMBER = "section_number";

    private String TAG = "Flag2";

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
                } else {
                    stopCapturing();
                }
            }
        });

        return rootView;
    }

    public String getBestProvider(LocationManager lm) {
        List<String> provs = lm.getAllProviders();
        for (String prov : provs) {
            Log.d(TAG, "Provider: " + prov);
        }
        Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_COARSE);
        c.setAltitudeRequired(false);
        c.setBearingRequired(false);
        c.setCostAllowed(false);
        c.setPowerRequirement(Criteria.NO_REQUIREMENT);
        c.setSpeedRequired(false);
        String provider = lm.getBestProvider(c, true);
        Log.d(TAG, "Best Provider: " + provider);
        return provider;
    }



    public void startCapture(Context context) {
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


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "SIN PERMISOS");

            mStarted = false;
            mButton.setText("Iniciar Sensor");
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST);
            return;
        }
        mLocationManager.requestLocationUpdates(getBestProvider(mLocationManager),0, 0, this);


    }

    public void stopCapturing() {
        mStarted = false;
        mButton.setText("Iniciar Sensor");
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d(TAG, "MainActivity onRequestPermissionsResult requestCode" +requestCode);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getActivity(),"Perfecto, volver a internar",Toast.LENGTH_LONG).show();


                } else {

                    Toast.makeText(getActivity(),"???",Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }
}
