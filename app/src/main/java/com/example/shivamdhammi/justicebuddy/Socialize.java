package com.example.shivamdhammi.justicebuddy.Fragmentstab;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shivamdhammi.justicebuddy.R;
import com.example.shivamdhammi.justicebuddy.posts;
import com.example.shivamdhammi.justicebuddy.uploadSocialize;
import com.github.rubensousa.raiflatbutton.RaiflatButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import me.grantland.widget.AutofitTextView;

public class Socialize extends Fragment {




    //uploasing
    String pmobile = " ";
    String pemail = " ";
    String desc;
    RecyclerView recyclerview;
    public static final String ARG_PAGE = "ARG_PAGE";
    protected ImageView imgViewCamera, share;
    protected int LOAD_IMAGE_CAMERA = 0, CROP_IMAGE = 1, LOAD_IMAGE_GALLARY = 2;
    private Uri picUri, crop;
    private File pic;
    private boolean mlike = false, checklike;
    private DatabaseReference mdatabaselike;
    long likeno;
    ArrayList<posts> data = new ArrayList<>();


    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    Bitmap photo;


    //FIREBASE DATABASE FIELDS
    DatabaseReference mUserDatabse;
    DatabaseReference post;
    EditText tag;
    FirebaseUser user;
    EditText describe;
    SwipeRefreshLayout swipeLayout;


    StorageReference mStorageRef;
    ProgressDialog mProgress;


    private int mPage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.socialize, container, false);
        imgViewCamera = (ImageView) view.findViewById(R.id.profile_image);
        share = (ImageView) view.findViewById(R.id.sharebtn);
        describe = (EditText) view.findViewById(R.id.tag);
        mUserDatabse = FirebaseDatabase.getInstance().getReference();

        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerview.setHasFixedSize(true);
        recyclerview.setAdapter(new Socialize.RecUsersAdapter(getActivity(), data));
        recyclerview.getAdapter().notifyDataSetChanged();

        post = mUserDatabse.child("Posts");


        mStorageRef = FirebaseStorage.getInstance().getReference();

        imgViewCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), LOAD_IMAGE_GALLARY);


            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contact();
            }
        });
        getData();


        return view;


    }

    private void contact() {

        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.contact_details);


        final MaterialEditText email = (MaterialEditText) dialog.findViewById(R.id.email);
        final MaterialEditText mobile = (MaterialEditText) dialog.findViewById(R.id.mobile);
        final RaiflatButton btn = (RaiflatButton) dialog.findViewById(R.id.interested);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                pemail = String.valueOf(email.getText());
                pmobile = String.valueOf(mobile.getText());

                saveUserProfile(pemail, pmobile);
                dialog.dismiss();
            }
        });

        dialog.show();


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOAD_IMAGE_GALLARY) {
            if (data != null) {

                picUri = data.getData();
                imgViewCamera.setImageURI(picUri);


                //CropImage();
            }
        } else if (requestCode == CROP_IMAGE) {
            if (data != null) {
                // get the returned data
                Bundle extras = data.getExtras();


                if (extras.getParcelable("data") != null) {
                    photo = extras.getParcelable("data");

                    imgViewCamera.setImageBitmap(photo);

                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), LOAD_IMAGE_GALLARY);

                }


            }
        }

    }

    private void saveUserProfile(final String pemail, final String pmobile) {

        if (picUri != null) {

            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = mStorageRef.child("images/" + String.valueOf(UUID.randomUUID().toString()));


            ref.putFile(picUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //Toast.makeText(getActivity(), uri.toString(), Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    addInitialDataToFirebase(uri.toString(), pemail, pmobile);
                                }
                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });


        }

    }


    private void addInitialDataToFirebase(String imageurl, String pemail, String pmobile) {


        List<uploadSocialize> sampleEntries = setdata(imageurl, pemail, pmobile);


        for (uploadSocialize Entry : sampleEntries) {
            String key = post.push().getKey();
            Entry.setPostId(key);
            post.child(key).setValue(Entry);

            describe.setText("");
            imgViewCamera.setImageDrawable(getResources().getDrawable(R.drawable.baseline_add_a_photo_black_48));


        }


    }


    public List<uploadSocialize> setdata(String imageuril, String email, String mobile) {
        List<uploadSocialize> mock = new ArrayList<>();
        uploadSocialize test = new uploadSocialize();

        test.setImagurl(imageuril);
        test.setEmail(email);
        test.setContactno(mobile);
        test.setStatus(String.valueOf(describe.getText()).trim());

        mock.add(test);
        return mock;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void getData() {

        mUserDatabse.child("Posts").orderByChild("postid").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> items = dataSnapshot.getChildren().iterator();

                        data.clear();
                        while (items.hasNext()) {

                            DataSnapshot item = items.next();

                            String imageUrl, gmail, status, gmobile, postid;
                            imageUrl = String.valueOf(item.child("imagurl").getValue());
                            status = String.valueOf(item.child("status").getValue());
                            postid = String.valueOf(item.child("postId").getValue());
                            gmail = String.valueOf(item.child("email").getValue());
                            gmobile = String.valueOf(item.child("contactno").getValue());

                            posts entry = new posts(imageUrl, status, gmobile, gmail, postid);
                            data.add(entry);


                        }
                        recyclerview.setAdapter(new Socialize.RecUsersAdapter(getActivity(), data));
                        recyclerview.getAdapter().notifyDataSetChanged();
                        mUserDatabse.child("Posts").removeEventListener(this);
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });


    }
    public class RecUsersAdapter extends RecyclerView.Adapter<Socialize.RecUsersAdapter.RecViewHolder> {


        private Context context;
        private List<posts> data;

        public RecUsersAdapter(Context context, List<posts> data) {
            this.context = context;
            this.data = data;
        }

        @Override
        public Socialize.RecUsersAdapter.RecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.socialget, parent, false);
            return new Socialize.RecUsersAdapter.RecViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final Socialize.RecUsersAdapter.RecViewHolder holder, final int position) {

            final posts event = data.get(position);
            //RequestOptions options = new RequestOptions();
            //options.centerCrop();

            Random rnd = new Random();
            int color = Color.argb(80, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));


            holder.lay.setBackgroundColor(color);




            Glide.with(context).load(event.getImagurl())
                    //.apply(options)
                    .into(holder.img);


            holder.status.setText(event.getStatus());


















        }


        @Override
        public int getItemCount() {
            return data.size();
        }

        public class RecViewHolder extends RecyclerView.ViewHolder {


            public ImageView img;
            public AutofitTextView status;
            public LinearLayout lay;


            public RecViewHolder(View itemView) {
                super(itemView);


                status = (AutofitTextView) itemView.findViewById(R.id.status);
                img = (ImageView) itemView.findViewById(R.id.imgpost);
                lay=(LinearLayout)itemView.findViewById(R.id.lay);



            }
        }


    }
}






