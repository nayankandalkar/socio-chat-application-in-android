package com.example.maruti5;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.maruti5.databinding.ActivityMain6Binding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

public class MainActivity6 extends AppCompatActivity {
ActivityMain6Binding binding;
Uri uri;
FirebaseAuth auth;
FirebaseDatabase database;
FirebaseStorage storage;
ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMain6Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        dialog=new ProgressDialog(MainActivity6.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("post upolading");
        dialog.setMessage("please wait");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        binding.editTextTextPersonName7.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String description=binding.editTextTextPersonName7.getText().toString();
                if (!description.isEmpty()){
                    binding.button7.setEnabled(true);
                }
                else {
                   // String description=binding.editTextTextPersonName7.getText().toString();
                   // if (!description.isEmpty()){
                        binding.button7.setEnabled(false);
                   // }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        binding.imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,10);
            }
        });

        binding.button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                StorageReference reference=storage.getReference().child("post")
                        .child(FirebaseAuth.getInstance().getUid())
                        .child(new Date().getTime()+"");
                reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Post post=new Post();
                                post.setPostImage(uri.toString());
                                post.setPostedBy(FirebaseAuth.getInstance().getUid());
                                post.setPostDescription(binding.editTextTextPersonName7.getText().toString());
                                post.setPostedAt(new Date().getTime());
                                database.getReference().child("post")
                                        .push()
                                        .setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(MainActivity6.this, "ram ram  ", Toast.LENGTH_SHORT).show();
                                           dialog.dismiss();
                                            }
                                        });
                                Toast.makeText(MainActivity6.this, "ram ram", Toast.LENGTH_SHORT).show();
                            }
                           //  Toast.makeText(MainActivity6.this, "ram ram  ", Toast.LENGTH_SHORT).show();
                        });
                    }
                });

            }
        });







        binding.bottomnavigationadd.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();

                if (id ==R.id.home){
                    Toast.makeText(MainActivity6.this, "home", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(MainActivity6.this,MainActivity5.class);
                    startActivity(intent);

                }else  if (id == R.id.search){
                    Toast.makeText(MainActivity6.this, "search", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(MainActivity6.this,MainActivity4.class);
                    startActivity(intent);
                }else  if (id == R.id.addpost){
                    Toast.makeText(MainActivity6.this, "add post", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(MainActivity6.this,MainActivity6.class);
                    startActivity(intent);
                }else  if (id == R.id.account){
                    Toast.makeText(MainActivity6.this, "account", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(MainActivity6.this,MainActivity3.class);
                    startActivity(intent);
                }


                return false;
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getData() !=null){
            uri=data.getData();


            binding.imageView5.setImageURI(uri);

                binding.button7.setEnabled(true);

        }
    }
}