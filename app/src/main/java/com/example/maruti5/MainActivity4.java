package com.example.maruti5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.maruti5.databinding.ActivityMain4Binding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity4 extends AppCompatActivity {
ActivityMain4Binding binding;
ArrayList<User> list;
FirebaseDatabase firebaseDatabase;
FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMain4Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
          list=new ArrayList<>();
          firebaseDatabase=FirebaseDatabase.getInstance();
          auth=FirebaseAuth.getInstance();

          UserAdapter userAdapter=new UserAdapter(MainActivity4.this,list);
        LinearLayoutManager layoutManager=new LinearLayoutManager(MainActivity4.this);
        binding.rv.setLayoutManager(layoutManager);
        binding.rv.setAdapter(userAdapter);

        firebaseDatabase.getReference().child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for ( DataSnapshot dataSnapshot:snapshot.getChildren()){
                    User user=dataSnapshot.getValue(com.example.maruti5.User.class);
                    user.setUserID(dataSnapshot.getKey());
                    if (!dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getUid())){
                    list.add(user);}
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}