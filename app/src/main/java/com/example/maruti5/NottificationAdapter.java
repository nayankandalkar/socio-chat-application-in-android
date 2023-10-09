package com.example.maruti5;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.maruti5.databinding.NotificationRvBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NottificationAdapter extends RecyclerView.Adapter<NottificationAdapter.viewHolder> {

    Context context;
    ArrayList<Notification> list;

    public NottificationAdapter(Context context, ArrayList<Notification> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NottificationAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.notification_rv,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NottificationAdapter.viewHolder holder, int position) {
        Notification model=list.get(position);
        String type=model.getType();
        FirebaseDatabase.getInstance().getReference()
                .child("user")
                .child(model.getNotificationBy())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user=snapshot.getValue(User.class);
                        Picasso.get().load(user.getProfilePhoto())
                                .placeholder(R.drawable.mayur)
                                .into(holder.binding.imageView4);

                     //   holder.binding.textView11.setText(user.getName());
                      //  holder.binding.textView16.setText(user.getProfession());
                        if (type .equals("like")){
                            holder.binding.textView11.setText(Html.fromHtml( "<b>"+user.getName()+"</b>"+"         "+"liked the post"));
                        }else if (type.equals("follow")){
                            holder.binding.textView11.setText(Html.fromHtml( "<b>"+user.getName()+"</b>"+"             "+"follow the post"));
                        }else  if (type.equals("comment")){
                            holder.binding.textView11.setText(Html.fromHtml(  "<b>"+user.getName()+"</b>"+"          "+"comment the post"));
                        }

                        holder.binding.textView16.setText(user.getProfession());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        holder.binding.opennotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!type.equals("follow")){
                    FirebaseDatabase.getInstance().getReference()
                            .child("notification")
                            .child(model.getPostedBy())
                            .child(model.getNotificationId())
                            .child("checkOpen")
                            .setValue(true);
                    holder.binding.opennotification.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    Intent intent=new Intent(context,MainActivity7.class);
                    intent.putExtra("postId",model.getPostId());
                    //   intent.putExtra("postedBy",model.getPostedBy());
                    intent.putExtra("postedBy",model.getNotificationBy());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
        boolean checknote=model.isCheckOpen();
        if (checknote == true){
            holder.binding.opennotification.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }else {

        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        NotificationRvBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding=NotificationRvBinding.bind(itemView);
        }
    }
}
