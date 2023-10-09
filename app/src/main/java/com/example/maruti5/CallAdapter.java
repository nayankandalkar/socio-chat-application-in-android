package com.example.maruti5;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maruti5.databinding.CallBinding;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;

import java.util.ArrayList;
import java.util.Collections;

public class CallAdapter extends RecyclerView.Adapter<CallAdapter.viewHolder> {

    Context context;
    ArrayList<User> list;

    public CallAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CallAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.call,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallAdapter.viewHolder holder, int position) {
        User user=list.get(position);

        holder.binding.textView.setText(user.getName());
        holder.binding.textView2.setText(user.getEmail());

        holder.binding.callBtn.setIsVideoCall(true);


//        holder.binding.callBtn.setResourceID("zego_uikit_call");



        // holder.binding.callBtn.setInvitees(Collections.singletonList(new ZegoUIKitUser(targetuserid,targetuserid)));


        holder.binding.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.binding.callBtn.setInvitees(Collections.singletonList(new ZegoUIKitUser(user.getEmail(),user.getEmail())));
            }
        });

        holder.binding.imageView17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                 int PERMISSION_CODE= 100;

                if (ContextCompat.checkSelfPermission( context,android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions((Activity) context,new String[]{android.Manifest.permission.CALL_PHONE},PERMISSION_CODE);

                }

                        String phoneno =user.getMobile();
                        Intent i = new Intent(Intent.ACTION_CALL);
                        i.setData(Uri.parse("tel:"+phoneno));
                        context.startActivity(i);







            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        CallBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding=CallBinding.bind(itemView);
        }
    }





}
