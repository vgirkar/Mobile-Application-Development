package com.example.hw07;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.Timestamp;
import java.util.ArrayList;


public class HistoryFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    ArrayList<Track> tracks = new ArrayList<>();
    RecyclerView recyclerView;
    sortAdapter adapter;
    FirebaseFirestore db;
    String userId;
    Button button5;

    public HistoryFragment() {
        
    }

    public static HistoryFragment newInstance(String param1) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_history, container, false);
        getActivity().setTitle(getString(R.string.history));
        recyclerView = view.findViewById(R.id.recyclerView);
        db = FirebaseFirestore.getInstance();

        db.collection("jogging")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                tracks.clear();
                int i=0;
                for(QueryDocumentSnapshot doc: value) {
                    if(userId.equalsIgnoreCase((String)doc.getData().get("userid"))) {
                        i = i + 1;
                        GeoPoint geoPoint = doc.getGeoPoint("geopoint");
                        double lat = geoPoint.getLatitude();
                        double lng = geoPoint.getLongitude();
                        LatLng latLng = new LatLng(lat, lng);
                        GeoPoint end = doc.getGeoPoint("end");
                        double lat1 = end.getLatitude();
                        double lng1 = end.getLongitude();
                        LatLng latLng1 = new LatLng(lat1, lng1);
                        Track m = new Track(userId, i,latLng,latLng1, (Timestamp) doc.getData().get("time"));
                        tracks.add(m);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
        adapter = new sortAdapter(tracks);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemPosition = recyclerView.getChildLayoutPosition(view);
            }
        });
        button5 = view.findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.home(userId);
            }
        });

        return view;
    }
    public class sortAdapter extends RecyclerView.Adapter<sortAdapter.sortHolder>{
        ArrayList<Track> arraySort = new ArrayList<Track>();
        public class sortHolder extends  RecyclerView.ViewHolder{
            TextView timestamp;
            Button track;
            int position;
            public sortHolder(@NonNull View itemView) {
                super(itemView);
                timestamp = itemView.findViewById(R.id.timeStamp);
                track = itemView.findViewById(R.id.button4);
                track.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       mListener.trackActivity(userId, tracks.get(position).getGeopoint(),tracks.get(position).getEnd(),tracks.get(position).getTime());
                    }
                });
            }
        }
        public  sortAdapter(ArrayList<Track> arraySort){
            this.arraySort = arraySort;
        }

        @NonNull
        @Override
        public sortHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.historylayout,parent,false);
            sortHolder sortHolder = new sortHolder(view);
            return sortHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull sortHolder holder, int position) {
            holder.timestamp.setText(tracks.get(position).getId()+" "+tracks.get(position).getTime().toDate().toString()  + "  "+getString(R.string.view) );
            holder.position = holder.getAdapterPosition();
        }

        @Override
        public int getItemCount() {
            if(arraySort == null) {
                return 0;
            }
            else{
                return this.arraySort.size();
            }
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