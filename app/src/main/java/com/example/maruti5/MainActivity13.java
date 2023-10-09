package com.example.maruti5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.maruti5.databinding.ActivityMain13Binding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity13 extends AppCompatActivity {

    ArrayList<User>  list;
    ActivityMain13Binding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMain13Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
         list=new ArrayList<>();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(MainActivity13.this);
        binding.rv.setLayoutManager(linearLayoutManager);
        CallAdapter adapter=new CallAdapter(MainActivity13.this,list);
        binding.rv.setAdapter(adapter);

        FirebaseDatabase.getInstance().getReference().child("user")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            User user=dataSnapshot.getValue(User.class);
                            user.setUserID(dataSnapshot.getKey());
                            list.add(user);

                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}