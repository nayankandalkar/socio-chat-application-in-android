package com.example.maruti5;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.maruti5.databinding.ActivityMain5Binding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zegocloud.uikit.prebuilt.call.config.ZegoNotificationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity5 extends AppCompatActivity {

    ArrayList<Post> list;
    ActivityMain5Binding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ActivityResultLauncher<String> gallryLauncher;
    FirebaseStorage storage;
    ArrayList<Story> storyList;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMain5Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database=FirebaseDatabase.getInstance();
        dialog=new ProgressDialog(MainActivity5.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("story upolading");
        dialog.setMessage("please wait");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        auth=FirebaseAuth.getInstance();

        storage=FirebaseStorage.getInstance();
        list=new ArrayList<>();
       PostAdapter Adapter=new PostAdapter(MainActivity5.this,list);
       LinearLayoutManager layoutManager=new LinearLayoutManager(MainActivity5.this);
       binding.recycler.setLayoutManager(layoutManager);
        binding.recycler.setAdapter(Adapter);

        storyList=new ArrayList<>();

        StoryAdapter adapter=new StoryAdapter(MainActivity5.this,storyList);
      //  LinearLayoutManager layoutManager1=new LinearLayoutManager(MainActivity5.this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false));
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setNestedScrollingEnabled(false);
        adapter.notifyDataSetChanged();
        database.getReference().child("storie").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                storyList.clear();
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Story story=new Story();
                        story.setStoryBy(dataSnapshot.getKey());
                        story.setStoryAt(dataSnapshot.child("postedby" +
                                "").getValue(Long.class));
                        ArrayList<UserStories> stories=new ArrayList<>();
                        for (DataSnapshot snapshot1:dataSnapshot.child("userstories").getChildren()){

                            UserStories  userStories=snapshot1.getValue(UserStories.class);
                            stories.add(userStories);

                        }
                          story.setStories(stories);
                         storyList.add(story);
                    }
                    adapter.notifyDataSetChanged();
                }
              //  adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        database.getReference().child("post").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Post post=dataSnapshot.getValue(Post.class);
                    post.setPostId(dataSnapshot.getKey());
                    list.add(post);
                }
                Adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




//        binding.storyadd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                gallryLauncher.launch("image/*");
//
//
//            }
//        });




        gallryLauncher=registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {





                      //  binding.storyadd.setImageURI(result);



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


                                                        Toast.makeText(MainActivity5.this, "ok ok ok ", Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();
                                                    }
                                                });
                                    }
                                });
                            }
                        });
                    }
                });



        binding.bottomnavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();

                if (id ==R.id.home){
                    Toast.makeText(MainActivity5.this, "home", Toast.LENGTH_SHORT).show();
                }else  if (id == R.id.search){
                    Toast.makeText(MainActivity5.this, "search", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(MainActivity5.this,MainActivity4.class);
                    startActivity(intent);
                }else if (id ==R.id.addstory){
                    Toast.makeText(MainActivity5.this, "add story", Toast.LENGTH_SHORT).show();
                    gallryLauncher.launch("image/*");
                    dialog.show();
                } else  if (id == R.id.addpost){
                    Toast.makeText(MainActivity5.this, "add post", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(MainActivity5.this,MainActivity6.class);
                    startActivity(intent);
                }else  if (id == R.id.account){
                    Toast.makeText(MainActivity5.this, "account", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(MainActivity5.this,MainActivity3.class);
                    startActivity(intent);
                }


                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.notify,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.notify) {

            Intent intent=new Intent(MainActivity5.this,MainActivity9.class);
            startActivity(intent);
           // Toast.makeText(this, "noifyinggggggg", Toast.LENGTH_SHORT).show();
        }
        if (id==R.id.chat) {

            Intent intent=new Intent(MainActivity5.this,MainActivity10.class);
            startActivity(intent);
            // Toast.makeText(this, "noifyinggggggg", Toast.LENGTH_SHORT).show();
        }
        if (id==R.id.groupChat) {

            Intent intent=new Intent(MainActivity5.this,MainActivity12.class);
            startActivity(intent);
            // Toast.makeText(this, "noifyinggggggg", Toast.LENGTH_SHORT).show();
        }







        if (id == R.id.call){

            FirebaseDatabase.getInstance()
                    .getReference()
                    .child("user")
                    .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user=snapshot.getValue(User.class);

                            startmyservice(user.getEmail());

                            Intent intent=new Intent(MainActivity5.this,MainActivity13.class);

                            intent.putExtra("caller",user.getEmail());
                            startActivity(intent);
                            Toast.makeText(MainActivity5.this, user.getEmail(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            Toast.makeText(this, "call ", Toast.LENGTH_SHORT).show();
        }








        return super.onOptionsItemSelected(item);
    }




    public  void  startmyservice(String userId){
        Application application =getApplication() ; // Android's application context
        long appID = 290347300;   // yourAppID
        String appSign ="830e792ea509bcd081f449f71cd6942dc7b2a93dc81e3dfb52d88a99444b345a";  // yourAppSign
        String userID =userId; // yourUserID, userID should only contain numbers, English characters, and '_'.
        String userName =userID;   // yourUserName

        ZegoUIKitPrebuiltCallInvitationConfig callInvitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();
        callInvitationConfig.notifyWhenAppRunningInBackgroundOrQuit = true;
        ZegoNotificationConfig notificationConfig = new ZegoNotificationConfig();
        notificationConfig.sound = "zego_uikit_sound_call";
        notificationConfig.channelID = "CallInvitation";
        notificationConfig.channelName = "CallInvitation";
        ZegoUIKitPrebuiltCallInvitationService.init(getApplication(), appID, appSign, userID, userName,callInvitationConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ZegoUIKitPrebuiltCallInvitationService.unInit();
    }


}