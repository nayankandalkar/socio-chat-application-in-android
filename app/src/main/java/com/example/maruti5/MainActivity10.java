package com.example.maruti5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.maruti5.databinding.ActivityMain10Binding;
import com.example.maruti5.databinding.ActivityMain3Binding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity10 extends AppCompatActivity {

    FirebaseDatabase database;
    ArrayList<User> list;
    ActivityMain10Binding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMain10Binding.inflate(getLayoutInflater());
        setContentView( binding.getRoot());

        database=FirebaseDatabase.getInstance();
        list=new ArrayList<>();
        UserAdapter1 userAdapter=new UserAdapter1(MainActivity10.this,list);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(MainActivity10.this);


        binding.rv.setLayoutManager(linearLayoutManager);
        binding.rv.setAdapter(userAdapter);

        database.getReference().child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    User user=dataSnapshot.getValue(User.class);

                    user.setUserID(dataSnapshot.getKey());
                    list.add(user);
                    // Toast.makeText(MainActivity3.this, user.getUserId(), Toast.LENGTH_SHORT).show();
                }


                userAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}