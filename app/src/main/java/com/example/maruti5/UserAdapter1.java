package com.example.maruti5;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maruti5.databinding.UserBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAdapter1  extends RecyclerView.Adapter<UserAdapter1.viewHolder> {

    Context context;
    ArrayList<User> list;

    public UserAdapter1(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public UserAdapter1.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.user,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter1.viewHolder holder, int position) {



        User user=list.get(position);

        Picasso.get()
                .load(user.getProfilePhoto())
                .placeholder(R.drawable.mayur)
                .into(holder.binding.profileImage);
        holder.binding.textView.setText(user.getName());
        holder.binding.textView2.setText(user.getEmail());

        FirebaseDatabase.getInstance().getReference()
                .child("chat")
                .child(FirebaseAuth.getInstance().getUid()+user.getUserID())
                .orderByChild("timestamp")
                .limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChildren()){
                            for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                                holder.binding.textView2.setText(dataSnapshot.child("message").getValue(String.class));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        // Toast.makeText(context, user.getUserId() , Toast.LENGTH_SHORT).show();


        FirebaseDatabase.getInstance().getReference().child("chatcount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    ChatCount count=dataSnapshot.getValue(ChatCount.class);


                    //  Toast.makeText(context, count.getReceiverid(), Toast.LENGTH_SHORT).show();

                    if (   FirebaseAuth.getInstance().getUid().equals(count.getReceiverid()) ) {
                        if (user.getUserID().equals(count.getSenderId())) {
                            holder.binding.itemview.setBackgroundColor(Color.parseColor("#9DEAA0"));

                            //  Toast.makeText(context, "ram ram", Toast.LENGTH_SHORT).show();
                        }
                    }

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,MainActivity11.class);
                intent.putExtra("userID",user.getUserID()+"");
                intent.putExtra("userName",user.getName()+"");
                context.startActivity(intent);
                ((Activity) v.getContext()).finish();

                //   Toast.makeText(context, user.getUserId(), Toast.LENGTH_SHORT).show();


//                if (FirebaseAuth.getInstance().getUid().equals())
//                FirebaseDatabase.getInstance().getReference().child("chat")
//                        .removeValue();










                FirebaseDatabase.getInstance().getReference().child("chatcount").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            ChatCount count=dataSnapshot.getValue(ChatCount.class);


                            //  Toast.makeText(context, count.getReceiverid(), Toast.LENGTH_SHORT).show();

                            if (   FirebaseAuth.getInstance().getUid().equals(count.getReceiverid()) ) {
                                FirebaseDatabase.getInstance().getReference()
                                        .child("chatcount")
                                        .child(count.receiverid).setValue(null);
                                //  holder.binding.itemview.setBackgroundColor(Color.parseColor("#FFFFFFFF"));

                            }


                            //     holder.binding.itemview.setBackgroundColor(Color.parseColor("#FFFFFFFF"));


                            // Toast.makeText(context, "ok o9", Toast.LENGTH_SHORT).show();



                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, "no noino ", Toast.LENGTH_SHORT).show();
                    }
                });











            }

        });





    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        UserBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding=UserBinding.bind(itemView);
        }
    }
}
