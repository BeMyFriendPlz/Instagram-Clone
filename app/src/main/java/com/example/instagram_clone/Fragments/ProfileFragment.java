package com.example.instagram_clone.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.instagram_clone.Model.Post;
import com.example.instagram_clone.Model.User;
import com.example.instagram_clone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private CircleImageView imageProfile;
    private ImageView options;
    private ImageButton myPictures, savedPictures;
    private Button editProfile;
    private TextView posts, followers, following, fullname, bio, username;
    private FirebaseUser fUser;
    private String profileId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        String data = getContext().getSharedPreferences("PROFILE", Context.MODE_PRIVATE)
                .getString("profileId", "none");
        if (data.equals("none")) {
            profileId = fUser.getUid();
        } else {
            profileId = data;
        }

        imageProfile = view.findViewById(R.id.image_profile);
        options = view.findViewById(R.id.options);
        followers = view.findViewById(R.id.followers);
        following = view.findViewById(R.id.following);
        posts = view.findViewById(R.id.posts);
        fullname = view.findViewById(R.id.fullname);
        bio = view.findViewById(R.id.bio);
        username = view.findViewById(R.id.username);
        myPictures = view.findViewById(R.id.my_pictures);
        savedPictures = view.findViewById(R.id.saved_pictures);
        editProfile = view.findViewById(R.id.edit_profile);

        userInfo();
        getFollowersAndFollowingCount();
        getPostCount();

        // Kiểm tra xem có phải profile của mình không
        if (profileId.equals(fUser.getUid())) {
            editProfile.setText("Edit profile");
        } else {
            checkFollowingStatus();
        }

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btnText = editProfile.getText().toString();
                if (btnText.equals("Edit profile")) {
                    // GOTO edit activity
                } else if (btnText.equals("follow")) {
                    FirebaseDatabase.getInstance().getReference().child("Follow")
                            .child(fUser.getUid()).child("following").child(profileId).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow")
                            .child(profileId).child("followers").child(fUser.getUid()).setValue(true);
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Follow")
                            .child(fUser.getUid()).child("following").child(profileId).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow")
                            .child(profileId).child("followers").child(fUser.getUid()).removeValue();
                }
            }
        });

        return view;
    }

    private void checkFollowingStatus() {
        FirebaseDatabase.getInstance().getReference().child("Follow")
                .child(fUser.getUid()).child("following").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child(profileId).exists()) {
                            editProfile.setText("following");
                        } else {
                            editProfile.setText("follow");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getPostCount() {
        FirebaseDatabase.getInstance().getReference().child("Posts")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int counter = 0;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Post post = dataSnapshot.getValue(Post.class);

                            if (post.getPublisher().equals(profileId)) {
                                counter++;
                            }
                        }

                        posts.setText(String.valueOf(counter));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getFollowersAndFollowingCount() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(profileId);
        ref.child("followers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followers.setText("" + snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ref.child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                following.setText("" + snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void userInfo() {
        FirebaseDatabase.getInstance().getReference().child("Users").child(profileId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);

                        if (user.getImageurl().equals("default")) {
                            imageProfile.setImageResource(R.mipmap.ic_launcher);
                        } else {
                            Picasso.get().load(user.getImageurl()).into(imageProfile);
                        }

                        username.setText(user.getUsername());
                        fullname.setText(user.getName());
                        bio.setText(user.getBio());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}