package com.example.maruti5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maruti5.databinding.FollowerPhotoBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.viewHolder> {
    Context context;
    ArrayList<Follow> list;

    public FollowAdapter(Context context, ArrayList<Follow> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FollowAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view= LayoutInflater.from(context).inflate(R.layout.follower_photo,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowAdapter.viewHolder holder, int position) {

        Follow follow=list.get(position);

        FirebaseDatabase.getInstance().getReference()
                        .child("user")
                                .child(follow.getFollowedBy()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user=snapshot.getValue(User.class);
                        Picasso.get()
                                .load(user.getProfilePhoto())
                                .placeholder(R.drawable.mayur)
                                .into(holder.binding.imageView4);
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
        FollowerPhotoBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding=FollowerPhotoBinding.bind(itemView);
        }
    }
}
