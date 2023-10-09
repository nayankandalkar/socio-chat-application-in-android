package com.example.maruti5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.maruti5.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
       FirebaseAuth auth;
       FirebaseDatabase database;
       FirebaseUser currentuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        currentuser= auth.getCurrentUser();

        binding.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=binding.editTextTextPersonName5.getText().toString();
                String password=binding.editTextTextPersonName6.getText().toString();
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "ram ram", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(MainActivity.this, "no no", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

//
//        binding.button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String email=binding.editTextTextPersonName.getText().toString();
//                String password=binding.editTextTextPersonName2.getText().toString();
//
////                if (TextUtils.isEmpty(email)){
////                    binding.editTextTextPersonName.setText("email cannot be apply");
////                    binding.editTextTextPersonName.requestFocus();
////                }
////                else  if (TextUtils.isEmpty(password)){
////                    binding.editTextTextPersonName.setText("password cannot be apply");
////                    binding.editTextTextPersonName.requestFocus();
////                }
//              //  else {
//                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                            User user=new User(binding.editTextTextPersonName3.getText().toString(),binding.editTextTextPersonName4.getText().toString(),email,password);
//                            String id=task.getResult().getUser().getUid();
//                              database.getReference().child("user").child(id).setValue(user);
//                            Toast.makeText(MainActivity.this, " ram ram", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(MainActivity.this, "no no no", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//
//               // }
//            }
//        });






        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,MainActivity2.class);
                startActivity(intent);
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentuser != null){
            Intent intent=new Intent(MainActivity.this,MainActivity5.class);
            startActivity(intent);
            finish();
        }
    }
}