package com.example.maruti5;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maruti5.databinding.PostuserBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import okhttp3.internal.cache.DiskLruCache;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.viewHolder> {
    Context context;
    ArrayList<Post> list;

    public PostAdapter(Context context, ArrayList<Post> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PostAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.postuser,parent,false);
       return new  viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.viewHolder holder, int position) {

        Post model=list.get(position);
        holder.binding.likecount.setText(model.getPostlike() +" ");
        holder.binding.textView13.setText(model.getCommentCount()+"");
        Picasso.get()
                .load(model.getPostImage())
                .placeholder(R.drawable.mayur)
                .into(holder.binding.imageView7);
        String description=model.getPostDescription();
        if (description.isEmpty()){
            holder.binding.description.setVisibility(View.GONE);
        }
        holder.binding.description.setText(model.getPostDescription());
        FirebaseDatabase.getInstance().getReference().child("user").child(model.getPostedBy()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);

                Picasso.get()
                        .load(user.getProfilePhoto())
                        .placeholder(R.drawable.mayur)
                        .into(holder.binding.profile);
                holder.binding.name.setText(user.getName());
                holder.binding.profession.setText(user.getProfession());

                FirebaseDatabase.getInstance().getReference().child("post")
                        .child(model.getPostId())
                        .child("like")
                        .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    holder.binding.like.setImageResource(R.drawable.img);
                                }else {
                                    holder.binding.like.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("post")
                                                    .child(model.getPostId())
                                                    .child("like")
                                                    .child(FirebaseAuth.getInstance().getUid())
                                                    .setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            FirebaseDatabase.getInstance().getReference()
                                                                    .child("post")
                                                                    .child(model.getPostId())
                                                                    .child("postlike")
                                                                    .setValue(model.getPostlike() +1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                            //  holder.binding.like.re
                                                                            holder.binding.like.setImageResource(R.drawable.img);


                                                                            Notification notification=new Notification();
                                                                            notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                                            notification.setNotificationAt(new Date().getTime());
                                                                            notification.setPostId(model.getPostId());
                                                                            notification.setPostedBy(model.getPostedBy());
                                                                            notification.setType("like");

                                                                            FirebaseDatabase.getInstance().getReference()
                                                                                    .child("notification")
                                                                                    .child(model.getPostedBy())
                                                                                    .push()
                                                                                    .setValue(notification);



                                                                        }
                                                                    });

                                                        }
                                                    });
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                holder.binding.imageView15.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context,MainActivity7.class);
                        intent.putExtra("postId",model.getPostId());
                     //   intent.putExtra("postedBy",model.getPostedBy());
                        intent.putExtra("postedBy",model.getPostedBy());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });

//                holder.binding.like.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        FirebaseDatabase.getInstance().getReference()
//                                .child("post")
//                                .child(model.getPostId())
//                                .child("like")
//                                .child(FirebaseAuth.getInstance().getUid())
//                                .setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void unused) {
//                                        FirebaseDatabase.getInstance().getReference()
//                                                .child("post")
//                                                .child(model.getPostId())
//                                                .child("postlike")
//                                                .setValue(model.getPostlike() +1).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                    @Override
//                                                    public void onSuccess(Void unused) {
//                                                      //  holder.binding.like.re
//                                                        holder.binding.like.setImageResource(R.drawable.img);
//
//
//                                                    }
//                                                });
//
//                                    }
//                                });
//                    }
//                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        PostuserBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding=PostuserBinding.bind(itemView);

        }
    }
}
