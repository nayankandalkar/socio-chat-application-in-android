package com.example.maruti5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.maruti5.databinding.ActivityMain7Binding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity7 extends AppCompatActivity {
ActivityMain7Binding binding;
Intent intent;
String postId;
String postedBy;
FirebaseDatabase database;
FirebaseAuth auth;
ArrayList<Comment> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMain7Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        intent=getIntent();
        postId=intent.getStringExtra("postId");
        postedBy=intent.getStringExtra("postedBy");

       list=new ArrayList<>();

        database.getReference().child("post")
                               .child(postId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Post post=snapshot.getValue(Post.class);
                        Picasso.get()
                                .load(post.getPostImage())
                                .placeholder(R.drawable.mayur)
                                .into(binding.imageView8);
                        binding.textView12.setText(post.getPostlike()+"");
                        binding.textView14.setText(post.getCommentCount()+"");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        database.getReference().child("user")
                .child(postedBy).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);

                //we are setting profile here

//                Picasso.get()
//                        .load(user.getProfilePhoto())
//                        .placeholder(R.drawable.mayur)
//                        .into(binding.imageView10);
//                binding.textView11.setText(user.getName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.imageView11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Comment comment=new Comment();
               comment.setCommentBody(binding.editTextTextPersonName8.getText().toString());
               comment.setCommentedAt(new Date().getTime());
               comment.setCommentedBy(FirebaseAuth.getInstance().getUid());
                database.getReference()
                        .child("post")
                        .child(postId)
                        .child("comments")
                        .push()
                        .setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                               database.getReference()
                                       .child("post")
                                       .child(postId)
                                       .child("commentCount").addListenerForSingleValueEvent(new ValueEventListener() {
                                           @Override
                                           public void onDataChange(@NonNull DataSnapshot snapshot) {
                                               int  commentcount=0;

                                               if (snapshot.exists()){
                                                   commentcount=snapshot.getValue(Integer.class);
                                               }
                                                   database.getReference()
                                                           .child("post")
                                                           .child(postId)
                                                           .child("commentCount")
                                                           .setValue(commentcount+1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                               @Override
                                                               public void onSuccess(Void unused) {

                                                                   binding.editTextTextPersonName8.setText("");
                                                                   Toast.makeText(MainActivity7.this, "commented", Toast.LENGTH_SHORT).show();


                                                            Notification notification=new Notification();
                                                            notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                            notification.setNotificationAt(new Date().getTime());
                                                            notification.setPostId(postId);
                                                            notification.setNotificationBy(postedBy);
                                                            notification.setType("comment");

                                                            FirebaseDatabase.getInstance().getReference()
                                                                    .child("notification")
                                                                    .child(postedBy)
                                                                    .push()
                                                                    .setValue(notification);

                                                               }
                                                           });

                                           }

                                           @Override
                                           public void onCancelled(@NonNull DatabaseError error) {

                                           }
                                       });
                            }
                        });
            }
        });


        CommentAdapter adapter=new CommentAdapter(MainActivity7.this,list);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(MainActivity7.this);
        binding.recycler.setLayoutManager(linearLayoutManager);
        binding.recycler.setAdapter(adapter);
        database.getReference()
                .child("post")
                .child(postId)
                .child("comments").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                            Comment comment=dataSnapshot.getValue(Comment.class);
                            list.add(comment);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}