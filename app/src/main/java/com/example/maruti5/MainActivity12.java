package com.example.maruti5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.maruti5.databinding.ActivityMain12Binding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity12 extends AppCompatActivity {

    ActivityMain12Binding binding;
    FirebaseDatabase database;
    ArrayList<MessageModel> massagemodel;
    String senderUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMain12Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        setContentView(binding.getRoot());
        database=FirebaseDatabase.getInstance();
        massagemodel=new ArrayList<>();
        senderUid= FirebaseAuth.getInstance().getUid();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(MainActivity12.this);
        binding.rv.setLayoutManager(linearLayoutManager);
        ChatAdapter adapter=new ChatAdapter(MainActivity12.this,massagemodel);
        binding.rv.setAdapter(adapter);


        database.getReference().child("Group Chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                massagemodel.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    MessageModel model=dataSnapshot.getValue(MessageModel.class);
                    massagemodel.add(model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.imageView16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String massage=binding.editTextTextPersonName9.getText().toString();
                MessageModel model=new MessageModel(senderUid,massage);
                model.setTimestamp(new Date().getTime());
                binding.editTextTextPersonName9.setText("");

                database.getReference().child("Group Chat")
                        .push()
                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
            }
        });


    }
}