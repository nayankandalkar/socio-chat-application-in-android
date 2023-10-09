package com.example.maruti5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.example.maruti5.databinding.ActivityMain9Binding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity9 extends AppCompatActivity {


    ActivityMain9Binding binding;
    ArrayList<Notification> list;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMain9Binding.inflate(getLayoutInflater()) ;
        setContentView(binding.getRoot());
        list=new ArrayList<>();
        database=FirebaseDatabase.getInstance();
        NottificationAdapter adapter=new NottificationAdapter(MainActivity9.this,list);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(MainActivity9.this);
        binding.rv.setLayoutManager(linearLayoutManager);
        binding.rv.setAdapter(adapter);

        database.getReference()
                .child("notification")
                .child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            Notification notification=dataSnapshot.getValue(Notification.class);
                            notification.setNotificationId(dataSnapshot.getKey());
                            //notification.getPostedBy();
                            notification.setNotificationBy(notification.getNotificationBy());

                            list.add(notification);
                          //  Toast.makeText(MainActivity9.this, notification.getNotificationBy(), Toast.LENGTH_SHORT).show();
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




    }
}