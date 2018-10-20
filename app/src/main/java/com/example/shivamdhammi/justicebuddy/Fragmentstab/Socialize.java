package com.example.shivamdhammi.justicebuddy.Fragmentstab;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.shivamdhammi.justicebuddy.R;
import com.example.shivamdhammi.justicebuddy.uploadSocialize;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Socialize extends Fragment {

    //uploasing



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.socialize, container, false);

        return view;


    }


    public List<uploadSocialize> setdata(String imageuril){
        List<uploadSocialize> mock = new ArrayList<>();
        uploadSocialize test = new uploadSocialize();

        test.setStatus(tag.getText().toString().trim());
        test.setUserid(mAuth.getUid());
        test.setUsername(user.getDisplayName());
        test.setImagurl(imageurl);
        test.setProfilepic(String.valueOf(user.getPhotoUrl()));


        mock.add(test);
        return mock;
    }
}