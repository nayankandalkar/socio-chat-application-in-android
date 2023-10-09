package com.example.maruti5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.example.maruti5.databinding.AnyBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.viewHolder> {

    Context context;
    ArrayList<Story> list;

    public StoryAdapter(Context context, ArrayList<Story> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public StoryAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.any,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryAdapter.viewHolder holder, int position) {

        Story story=list.get(position);
        FirebaseDatabase.getInstance().getReference().child("user").child(story.getStoryBy()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);


                Picasso.get()
                        .load(user.getProfilePhoto())
                        .placeholder(R.drawable.mayur)
                        .into(holder.binding.imageView14);


        holder.binding.imageView13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<MyStory> myStories = new ArrayList<>();

                for(UserStories stories: story.getStories()){
                    myStories.add(new MyStory(
                            stories.getImage()
                           // story.getDate()
                    ));
                }


                new StoryView.Builder(((AppCompatActivity)context).getSupportFragmentManager())
                        .setStoriesList(myStories) // Required
                        .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                        .setTitleText(user.getName()) // Default is Hidden
                        .setSubtitleText("") // Default is Hidden
                        .setTitleLogoUrl(user.getProfilePhoto()) // Default is Hidden
                        .setStoryClickListeners(new StoryClickListeners() {
                            @Override
                            public void onDescriptionClickListener(int position) {
                                //your action
                            }

                            @Override
                            public void onTitleIconClickListener(int position) {
                                //your action
                            }
                        }) // Optional Listeners
                        .build() // Must be called before calling show method
                        .show();


            }
        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

        UserStories lastStory=story.getStories().get(story.getStories().size()-1);

        Picasso.get().load(lastStory.getImage())
                .placeholder(R.drawable.mayur)
                .into(holder.binding.imageView13);
        holder.binding.profilephoto.setPortionsCount(story.getStories().size());

        Toast.makeText(context, list.size()+" ", Toast.LENGTH_SHORT).show();
       // UserStories laststories=story.getStories().get();


//        holder.binding.imageView13.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ArrayList<MyStory> myStories = new ArrayList<>();
//
//                for(UserStories stories: story.getStories()){
//                    myStories.add(new MyStory(
//                            stories.getImage()
//                           // story.getDate()
//                    ));
//                }
//
//
//                new StoryView.Builder(((AppCompatActivity)context).getSupportFragmentManager())
//                        .setStoriesList(myStories) // Required
//                        .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
//                        .setTitleText("") // Default is Hidden
//                        .setSubtitleText("") // Default is Hidden
//                        .setTitleLogoUrl("some-link") // Default is Hidden
//                        .setStoryClickListeners(new StoryClickListeners() {
//                            @Override
//                            public void onDescriptionClickListener(int position) {
//                                //your action
//                            }
//
//                            @Override
//                            public void onTitleIconClickListener(int position) {
//                                //your action
//                            }
//                        }) // Optional Listeners
//                        .build() // Must be called before calling show method
//                        .show();
//
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        AnyBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding=AnyBinding.bind(itemView);
        }
    }
}
