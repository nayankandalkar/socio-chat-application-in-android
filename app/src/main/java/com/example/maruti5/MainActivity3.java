package com.example.maruti5;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.maruti5.databinding.ActivityMain3Binding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MainActivity3 extends AppCompatActivity {
ActivityMain3Binding binding;
FirebaseAuth auth;
FirebaseStorage storage;
FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMain3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


       // UserAdapter userAdapter=new UserAdapter(getContex)


        auth=FirebaseAuth.getInstance();
        storage=FirebaseStorage.getInstance();
        database=FirebaseDatabase.getInstance();
        database.getReference().child("user").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    binding.progressBar.setVisibility(View.GONE);
                    User user=snapshot.getValue(User.class);
                    Picasso.get()
                            .load(user.getCoverPhoto())
                            .placeholder(R.drawable.mayur)
                            .into(binding.imageView);

                    Picasso.get()
                            .load(user.getProfilePhoto())
                            .placeholder(R.drawable.mayur)
                            .into(binding.imageView2);

                          binding.textView4.setText(user.getName());
                          binding.textView5.setText(user.getProfession());
                          binding.textView15.append("  "+user.getFollowerCount()+"");
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        ArrayList<Follow> list=new ArrayList<>();
        FollowAdapter followAdapter=new FollowAdapter(MainActivity3.this,list);
      //  LinearLayoutManager layoutManager=new LinearLayoutManager(MainActivity3.this);
        binding.recyclerphoto.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false));
        binding.recyclerphoto.setAdapter(followAdapter);

        database.getReference().child("user").child(auth.getUid()).child("follower").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Follow follow=dataSnapshot.getValue(Follow.class);
                    list.add(follow);
                }
                followAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,11);
            }
        });

        binding.button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,22);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 11) {


        if (data.getData() != null) {
            Uri uri = data.getData();
            binding.imageView.setImageURI(uri);
            // Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();
            final StorageReference reference = storage.getReference().child("cover_photo")
                    .child(FirebaseAuth.getInstance().getUid());
            reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(MainActivity3.this, "cover photo saved ", Toast.LENGTH_SHORT).show();
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference().child("user").child(auth.getUid()).child("coverPhoto").setValue(uri.toString());
                        }
                    });
                }
            });
        }

    }else{


            if (data.getData() != null) {
                Uri uri = data.getData();
                binding.imageView2.setImageURI(uri);
                // Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();
                final StorageReference reference = storage.getReference().child("profile_image")
                        .child(FirebaseAuth.getInstance().getUid());
                reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(MainActivity3.this, "cover photo saved ", Toast.LENGTH_SHORT).show();
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                database.getReference().child("user").child(auth.getUid()).child("profilePhoto").setValue(uri.toString());
                            }
                        });
                    }
                });
            }
        }

    }
}