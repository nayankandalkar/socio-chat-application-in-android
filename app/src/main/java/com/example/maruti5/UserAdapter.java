package com.example.maruti5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.maruti5.databinding.SampleUserBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewHolder> {

    Context context;
    ArrayList<User>  list;

    public UserAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public UserAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

      View view= LayoutInflater.from(context).inflate(R.layout.sample_user,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.viewHolder holder, int position) {
            User user=list.get(position);

        Picasso.get()
                .load(user.getProfilePhoto())
                .placeholder(R.drawable.mayur)
                .into(holder.binding.imageView3);

        holder.binding.textView6.setText(user.getName());
        holder.binding.textView7.setText(user.getProfession());

        FirebaseDatabase.getInstance().getReference().child("user").child(user.getUserID()).child("follower").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    holder.binding.button6.setText("folloeing");
                    holder.binding.button6.setTextColor(context.getResources().getColor(R.color.black));
                    holder.binding.button6.setEnabled(false);
                }
                else {

                    holder.binding.button6.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Follow follow=new Follow();
                            follow.setFollowedBy(FirebaseAuth.getInstance().getUid());
                            follow.setFollowedAt(new Date().getTime());

                            FirebaseDatabase.getInstance().getReference()
                                    .child("user")
                                    .child(user.getUserID())
                                    .child("follower")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .setValue(follow).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("user")
                                                    .child(user.getUserID())
                                                    .child("followerCount").setValue(user.getFollowerCount() +1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(context, "ok ok ok", Toast.LENGTH_SHORT).show();
                                                            holder.binding.button6.setText("folloeing");
                                                            holder.binding.button6.setTextColor(context.getResources().getColor(R.color.black));
                                                            holder.binding.button6.setEnabled(false);
                                                            Notification notification=new Notification();

                                                            notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                            notification.setNotificationAt(new Date().getTime());
                                                            notification.setType("follow");

                                                            FirebaseDatabase.getInstance().getReference()
                                                                    .child("notification")
                                                                    .child(user.getUserID())
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

//        holder.binding.button6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Follow follow=new Follow();
//                follow.setFollowedBy(FirebaseAuth.getInstance().getUid());
//                follow.setFollowedAt(new Date().getTime());
//
//                FirebaseDatabase.getInstance().getReference()
//                        .child("user")
//                        .child(user.getUserID())
//                        .child("follower")
//                        .child(FirebaseAuth.getInstance().getUid())
//                        .setValue(follow).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                FirebaseDatabase.getInstance().getReference()
//                                        .child("user")
//                                        .child(user.getUserID())
//                                        .child("followerCount").setValue(user.getFollowerCount() +1).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void unused) {
//                                                Toast.makeText(context, "ok ok ok", Toast.LENGTH_SHORT).show();
//                                                holder.binding.button6.setText("folloeing");
//                                                holder.binding.button6.setTextColor(context.getResources().getColor(R.color.black));
//                                                holder.binding.button6.setEnabled(false);
//                                            }
//                                        });
//                            }
//                        });
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        SampleUserBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding=SampleUserBinding.bind(itemView);
        }
    }
}
