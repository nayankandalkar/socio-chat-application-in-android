package com.example.maruti5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maruti5.databinding.ReceiverBinding;
import com.example.maruti5.databinding.SenderBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<MessageModel> list;

    int SENDER_VIEW_TYPE=1;
    int RECEIVER_VIEW_TYPE=2;


    public ChatAdapter(Context context, ArrayList<MessageModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType ==SENDER_VIEW_TYPE){
            View view= LayoutInflater.from(context).inflate(R.layout.sender,parent,false);
            return  new SenderViewHolder(view);
        }else {
            View view= LayoutInflater.from(context).inflate(R.layout.receiver,parent,false);
            return  new ReceiverViewHolder(view);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MessageModel messageModel=list.get(position);
        if (holder.getClass() == SenderViewHolder.class){
            ((SenderViewHolder)holder).binding.textView6.setText(messageModel.getMessage());
        }else {
            ((ReceiverViewHolder)holder).binding.textView4.setText(messageModel.getMessage());

        }
    }

    @Override
    public int getItemViewType(int position) {

        if (list.get(position).getUid().equals(FirebaseAuth.getInstance().getUid())){
            return  SENDER_VIEW_TYPE;
        }else {
            return  RECEIVER_VIEW_TYPE;
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ReceiverViewHolder extends RecyclerView.ViewHolder{

        ReceiverBinding binding;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=ReceiverBinding.bind(itemView);
        }
    }

    class  SenderViewHolder extends RecyclerView.ViewHolder{

        SenderBinding binding;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=SenderBinding.bind(itemView);
        }
    }
}
