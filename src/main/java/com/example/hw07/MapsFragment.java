package com.example.hw07;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class MapsFragment extends Fragment {
    private GoogleMap gMap;
    private static final String ARG_PARAM1 = "param1";
    int ACCESS_LOCATION_REQUEST_CODE = 10001;
    String userId;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    FirebaseFirestore db;
    LatLng loc;
    Button stop;
    LocationResult result;
    Timestamp timestamp;

    public static MapsFragment newInstance(String userId) {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, userId);
        fragment.setArguments(args);
        return fragment;
    }


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            gMap = googleMap;
            gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation();
                zoomToUserLocation();

            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);
                }
            }
            /*LatLng home = new LatLng(-11, 151);
            MarkerOptions markerOptions = new MarkerOptions().position(home).title("Activity");
            gMap.addMarker(markerOptions);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(home, 15);
            gMap.animateCamera(cameraUpdate);*/
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ACCESS_LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation();
                zoomToUserLocation();
            }
        }
    }

    private void enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            
            return;
        }
        gMap.setMyLocationEnabled(true);

    }

    private void zoomToUserLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                loc = latLng;
                if(loc == null){
                    enableUserLocation();
                    loc = new LatLng(37.4219762,-122.0840116);
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 14));
                    gMap.addMarker(new MarkerOptions().position(loc).title("start"));
                }
                else {
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 14));
                    gMap.addMarker(new MarkerOptions().position(loc).title("start"));
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        getActivity().setTitle(getString(R.string.TrackLocation));
        stop = view.findViewById(R.id.button3);
        LatLng end;
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationResult finalResult = result;
                db = FirebaseFirestore.getInstance();
                Map<String, Object> user = new HashMap<>();
                if(loc == null){
                    enableUserLocation();
                    loc = new LatLng(35.81021,-78.8686);
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 14));
                    gMap.addMarker(new MarkerOptions().position(loc).title("start"));
                }
                GeoPoint g = new GeoPoint(loc.latitude,loc.longitude);
                user.put("geopoint", g);
                user.put("userid", userId);

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    
                    return;
                }
                Task<Location> task = fusedLocationProviderClient.getLastLocation();
                task.addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        LatLng latLng = null;
                        if(location == null){
                            latLng = new LatLng(35.8107933,-78.8685333);
                        }
                        else {
                             latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        }
                        gMap.addMarker(new MarkerOptions().position(latLng).title("end"));
                        GeoPoint gEnd = new GeoPoint(latLng.latitude,latLng.longitude);
                        user.put("end",gEnd);
                        user.put("time", new Timestamp(System.currentTimeMillis())  );
                        db.collection("jogging").add(user);
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.saved)
                        .setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mListener.home(userId);
                            }
                        });
                builder.create().show();
            }
        });
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString(ARG_PARAM1);
        }
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
    }
    IListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof IListener){
            mListener = (IListener) context;
        }else{
            throw new RuntimeException(context.toString() + getString(R.string.InterfaceMsg));
        }

    }
}