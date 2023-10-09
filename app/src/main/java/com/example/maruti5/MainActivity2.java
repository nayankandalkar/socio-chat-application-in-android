package com.example.maruti5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity2 extends AppCompatActivity {
EditText editText,editText2,editText3,editText4,editText5;
Button button;
    FirebaseAuth auth;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        editText=findViewById(R.id.editTextTextPersonName);
        editText2=findViewById(R.id.editTextTextPersonName2);
        editText3=findViewById(R.id.editTextTextPersonName3);
        editText4=findViewById(R.id.editTextTextPersonName4);
        editText5=findViewById(R.id.editTextTextPersonName10);
         button=findViewById(R.id.button);

         button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String email=editText.getText().toString();
                 String password=editText2.getText().toString();

                 auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         if (task.isSuccessful()) {
                             User user=new User( editText3.getText().toString(), editText4.getText().toString(),email,password);
                             user.setMobile(editText5.getText().toString());
                             String id=task.getResult().getUser().getUid();
                             database.getReference().child("user").child(id).setValue(user);
                             Toast.makeText(MainActivity2.this, " ram ram", Toast.LENGTH_SHORT).show();
                         } else {
                             Toast.makeText(MainActivity2.this, "no no no", Toast.LENGTH_SHORT).show();
                         }
                     }
                 });
             }
         });
    }
}