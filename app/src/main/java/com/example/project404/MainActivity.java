package com.example.project404;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    String name = "", branch = "", usn="", role = ""; //search query fields
    RecyclerView recview;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    List<Datum> s_data= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Search Results:");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("FLC-members");

        mAdapt adapt = new mAdapt(s_data, getApplicationContext());
        recview = (RecyclerView) findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));
        recview.setAdapter(adapt);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int c = 0;
                s_data.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Datum datum = snapshot.getValue(Datum.class);
                    if (usn.length() == 10) {
                        if (datum.getUsn().equalsIgnoreCase(usn)) {
                            c++;
                            profile p = new profile();
                            p.next(datum, getApplicationContext());
                        }
                        if (c == 0) {
                            Toast.makeText(getApplicationContext(), "FLC member with that USN does not exist!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (datum.getName().toLowerCase().contains(name.toLowerCase()) && datum.getBranch().toLowerCase().contains(branch.toLowerCase()) && datum.getRole().toLowerCase().contains(role.toLowerCase())) {
                            c++;
                            s_data.add(datum);
                        }
                        if (c == 0) {
                            Toast.makeText(getApplicationContext(), "No search results found.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                adapt.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

        /*databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                Datum data = dataSnapshot.getValue(Datum.class);
                s_data.add(data);
                //Log.d("TAG",dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
               //mAdapt.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

}