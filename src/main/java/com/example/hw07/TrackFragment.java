package com.example.hw07;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class TrackFragment extends Fragment {
    public static String userid;
    public static  LatLng startLat;
    public static LatLng endLat;
    public static Timestamp timestamp;
    private GoogleMap mMap;
    private Marker mStart;
    private Marker mEnd;
    private Marker point;
    TextView start;
    TextView end;
    TextView time;
    Button backBtn;

    public static TrackFragment newInstance(String userId, LatLng start, LatLng end, Timestamp time ) {
        TrackFragment fragment = new TrackFragment();
        Bundle args = new Bundle();
        userid = userId;
        startLat = start;
        endLat = end;
        timestamp = time;
        fragment.setArguments(args);
        return fragment;
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            final ArrayList<LatLng> latLngArrayList = new ArrayList<>();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(startLat));
            latLngArrayList.add(startLat);
            latLngArrayList.add(endLat);
            Polyline line =googleMap.addPolyline(new PolylineOptions()
                    .addAll(latLngArrayList)
                    .width(6)
                    .color(Color.RED));

            line.setPoints(latLngArrayList);
            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    mStart = mMap.addMarker(new MarkerOptions().position(startLat).title("start"));
                    mStart.showInfoWindow();
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(startLat);
                    builder.include(endLat);
                    LatLngBounds bo = builder.build();
                    CameraUpdate cu2 = CameraUpdateFactory.newLatLngBounds(bo,50);

                    mEnd = mMap.addMarker(new MarkerOptions().position(endLat).title("end"));
                    mEnd.showInfoWindow();

                    mMap.moveCamera(cu2);
                }
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_track, container, false);
        start = view.findViewById(R.id.start);
        end = view.findViewById(R.id.end);
        time = view.findViewById(R.id.time);
        backBtn = view.findViewById(R.id.backBtn);
        start.setText("Start latitude: "+startLat.latitude+" longitude: "+startLat.longitude );
        end.setText("End latitude: "+endLat.latitude+" longitude: "+endLat.longitude );
        time.setText(timestamp.toDate().toString());
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.history(userid);
            }
        });
        getActivity().setTitle(getString(R.string.track));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
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