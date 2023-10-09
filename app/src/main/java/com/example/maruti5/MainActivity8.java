package com.example.maruti5;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.maruti5.databinding.ActivityMain8Binding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity8 extends AppCompatActivity {

    ActivityMain8Binding binding;
    ArrayList<Story> storylist;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ActivityResultLauncher<String> gallryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMain8Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
         database=FirebaseDatabase.getInstance();
         storage=FirebaseStorage.getInstance();
         storylist=new ArrayList<>();
        StoryAdapter adapter=new StoryAdapter(MainActivity8.this,storylist);
     //   LinearLayoutManager linearLayoutManager=new LinearLayoutManager(MainActivity8.this);
        binding.rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        binding.rv.setAdapter(adapter);

        database.getReference()
                .child("storie").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        storylist.clear();
                        if (snapshot.exists()){
                          //  storylist.clear();
                            for (DataSnapshot storysnapshot:snapshot.getChildren()){
                                Story story=new Story();
                                story.setStoryBy(storysnapshot.getKey());
                                story.setStoryAt(storysnapshot.child("postedby").getValue(Long.class));
                                ArrayList<UserStories> stories=new ArrayList<>();
                                for (DataSnapshot snapshot1:storysnapshot.child("userstories").getChildren()){
                                    UserStories userStories=snapshot1.getValue(UserStories.class);
                                    stories.add(userStories);
                                }
                                story.setStories(stories);
                                storylist.add(story);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




        binding.imageView12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gallryLauncher.launch("image/*");


            }
        });
        gallryLauncher=registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        binding.imageView12.setImageURI(result);
                        StorageReference reference=storage.getReference()
                                .child("stories")
                                .child(FirebaseAuth.getInstance().getUid())
                                .child(new Date().getTime()+"");
                        reference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Story story=new Story();
                                        story.setStoryAt(new Date().getTime());

                                        database.getReference().child("storie")
                                                .child(FirebaseAuth.getInstance().getUid())
                                                .child("postedby")
                                                .setValue(story.getStoryAt()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        UserStories userStories=new UserStories(uri.toString(),story.getStoryAt());
                                                        database.getReference().child("storie")
                                                                .child(FirebaseAuth.getInstance().getUid())
                                                                .child("userstories")
                                                                .push()
                                                                .setValue(userStories);


                                                    }
                                                });
                                    }
                                });
                            }
                        });
                    }
                });



    }
}