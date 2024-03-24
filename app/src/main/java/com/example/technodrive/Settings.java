package com.example.technodrive;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ActivityNavigatorDestinationBuilderKt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;


import com.example.technodrive.General.General;
import com.example.technodrive.Users.Home;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Settings extends AppCompatActivity {
    private CircleImageView profilePhoto;
    private EditText name,surname,city,email,password;
    private TextView exit,save;
    private String checker="";
    private Uri imageUri;
    private Button exitBtn,saveBtn;
    private StorageReference storageProfilePictureRef;
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        profilePhoto=(CircleImageView) findViewById(R.id.imageSettingsProfile);
        name=(EditText)findViewById(R.id.editTextSetingsName);
        surname=(EditText)findViewById(R.id.editTextSetingsSurname);
        city=(EditText) findViewById(R.id.editTextSetingsCity);
        email=(EditText) findViewById(R.id.editTextSetingsEmail);
        password=(EditText) findViewById(R.id.editTextSetingsPassword);
        exit=(TextView) findViewById(R.id.textViewClose);
        save=(TextView) findViewById(R.id.textViewSave);
        storageProfilePictureRef = FirebaseStorage.getInstance().getReference().child("Profile image");

        userInfoDisplay(profilePhoto, name, surname, city, email, password);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(Settings.this, Home.class);
                startActivity(loginIntent);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (checker.equals("clicked"))
                {
                    userInfoSaved();
                }
                else
                {
                    updateOnlyUserInfo();
                }
            }
        });

        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker = "clicked";

                /*CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .start(SettingsActivity.this);*/
            }
        });
    }

    private void userInfoDisplay(final CircleImageView profilePhoto, final EditText name, final EditText surname, final EditText city, final EditText email, final EditText password) {
        String phone = General.onlineUser.getEmail();
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    if (dataSnapshot.child("image").exists())
                    {
                        String pImage = dataSnapshot.child("image").getValue().toString();
                        String pName = dataSnapshot.child("name").getValue().toString();
                        String pSurname = dataSnapshot.child("surname").getValue().toString();
                        String pEmail = dataSnapshot.child("email").getValue().toString();
                        String pCity = dataSnapshot.child("city").getValue().toString();
                        String pPassword = dataSnapshot.child("password").getValue().toString();

                        Picasso.get().load(pImage).into(profilePhoto);
                        name.setText(pName);
                        surname.setText(pSurname);
                        email.setText(pEmail);
                        city.setText(pCity);
                        password.setText(pPassword);
                    }

                    if (dataSnapshot.child("email").exists())
                    {
                        String pName = dataSnapshot.child("name").getValue().toString();
                        String pSurname = dataSnapshot.child("surname").getValue().toString();
                        String pEmail = dataSnapshot.child("email").getValue().toString();
                        String pCity = dataSnapshot.child("city").getValue().toString();
                        String pPassword = dataSnapshot.child("password").getValue().toString();
                        name.setText(pName);
                        surname.setText(pSurname);
                        email.setText(pEmail);
                        city.setText(pCity);
                        password.setText(pPassword);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK  &&  data!=null)
        {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                imageUri = selectedImageUri;
            }
            /*ActivityResult result = getActivityResult(data);
            imageUri = result.getUri();*/
            profilePhoto.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(Settings.this, Settings.class));
            finish();
        }
    }


    private void userInfoSaved() {
        if (TextUtils.isEmpty(name.getText().toString()) || TextUtils.isEmpty(surname.getText().toString()) || TextUtils.isEmpty(city.getText().toString()) || TextUtils.isEmpty(email.getText().toString()) || TextUtils.isEmpty(password.getText().toString())) {
            Toast.makeText(Settings.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
        }
        else if(checker.equals("clicked"))
        {
            uploadImage();
        }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Это займет немного времени");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null)
        {
            final StorageReference fileRef = storageProfilePictureRef
                    .child(General.onlineUser.getEmail() + ".WebP");

            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                        @Override
                        public Object then(@NonNull Task task) throws Exception
                        {
                            if (!task.isSuccessful())
                            {
                                throw task.getException();
                            }

                            return fileRef.getDownloadUrl();
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task)
                        {
                            if (task.isSuccessful())
                            {
                                Uri downloadUrl = task.getResult();
                                String myUrl = downloadUrl.toString();

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                                HashMap<String, Object> userMap = new HashMap<>();
                                userMap. put("name", name.getText().toString());
                                userMap. put("surname", surname.getText().toString());
                                userMap. put("city", city.getText().toString());
                                userMap. put("email", email.getText().toString());
                                userMap. put("password", password.getText().toString());
                                userMap. put("image", myUrl);
                                ref.child(General.onlineUser.getEmail()).updateChildren(userMap);

                                progressDialog.dismiss();

                                startActivity(new Intent(Settings.this, Home.class));
                                finish();
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(Settings.this, "Ошибка", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(this, "Изображение не выбрано.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateOnlyUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap. put("name", name.getText().toString());
        userMap. put("surname", surname.getText().toString());
        userMap. put("city", city.getText().toString());
        userMap. put("email", email.getText().toString());
        userMap. put("password", password.getText().toString());
        ref.child(General.onlineUser.getEmail()).updateChildren(userMap);

        startActivity(new Intent(Settings.this, Home.class));
        finish();
    }
}