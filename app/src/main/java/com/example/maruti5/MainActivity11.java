package com.example.maruti5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.maruti5.databinding.ActivityMain10Binding;
import com.example.maruti5.databinding.ActivityMain11Binding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity11 extends AppCompatActivity {
   ActivityMain11Binding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    ArrayList<MessageModel> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain11Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());









        setContentView( binding.getRoot());
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        list=new ArrayList<>();

        final String senderId=  auth.getUid();
        String receiverId=getIntent().getStringExtra("userID");
        String userName=getIntent().getStringExtra("userName");

        Toast.makeText(this,   receiverId+"", Toast.LENGTH_SHORT).show();

        LinearLayoutManager layoutManager=new LinearLayoutManager(MainActivity11.this);
        binding.rv.setLayoutManager(layoutManager);
        ChatAdapter adapter=new ChatAdapter(MainActivity11.this,list);
        binding.rv.setAdapter(adapter);
        final  String senderRoom=senderId+receiverId;
        final  String receiverRoom=receiverId+senderId;

        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=binding.editTextTextPersonName6.getText().toString();
                final  MessageModel model=new MessageModel(senderId,message);
                //  model.setCheck(true);
                ///     model.setCount();

                ChatCount count=new ChatCount();
                count.setReceiverid(receiverId);
                count.setSenderId(FirebaseAuth.getInstance().getUid());
                database.getReference()
                        .child("chatcount")
                        .child(receiverId)
                        .setValue(count);




                model.setTimestamp(new Date().getTime());
                binding.editTextTextPersonName6.setText("");
                database.getReference().child("chat").child(senderRoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        database.getReference().child("chat")
                                .child(receiverRoom)
                                .push()
                                .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                    }
                                });

                    }
                });



            }
        });

        database.getReference().child("chat")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();

                        for (DataSnapshot snapshot1:snapshot.getChildren()){
                            MessageModel model=snapshot1.getValue(MessageModel.class);
                            list.add(model);

                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }



                });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent=new Intent(MainActivity11.this,MainActivity10.class);
        startActivity(intent);
    }
}