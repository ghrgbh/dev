package com.example.technodrive.Admin;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.technodrive.Login;
import com.example.technodrive.Regestration;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.technodrive.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Admin extends AppCompatActivity {
    private String categoryName,pDesk,pName,pMark,pCount,pPrice,saveData,saveTime,randomKey,downloadeImage;
    private ImageView productImage;
    private EditText name,mark,count,price,desk;
    private Button addProduct;
    private Uri imageUri;
    private StorageReference imageRef;
    private static final int GALLERYPICK = 1;
    private DatabaseReference productsRef;
    private ProgressDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        categoryName=getIntent().getExtras().get("category").toString();
        productImage=(ImageView)findViewById(R.id.imageViewProduct);
        name=(EditText) findViewById(R.id.editTextName);
        mark=(EditText)findViewById(R.id.editTextMark);
        count=(EditText) findViewById(R.id.editTextCount);
        price=(EditText) findViewById(R.id.editTextPrice);
        desk=(EditText) findViewById(R.id.editTextDesc);
        addProduct=(Button) findViewById(R.id.btnAddProduct);
        loading=new ProgressDialog(this);
        imageRef= FirebaseStorage.getInstance().getReference().child("ProductImage");
        productsRef=FirebaseDatabase.getInstance().getReference().child("Products");
        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GalleryInit();
            }
        });

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pName=name.getText().toString();
                pMark=mark.getText().toString();
                pCount=count.getText().toString();
                pPrice=price.getText().toString();
                pDesk=desk.getText().toString();
                if(imageUri==null|| TextUtils.isEmpty(pName)||TextUtils.isEmpty(pMark)|| TextUtils.isEmpty(pCount)|| TextUtils.isEmpty(pPrice)|| TextUtils.isEmpty(pDesk)){
                    Toast.makeText(Admin.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                }
                else {
                    loading.setTitle("Это займет немного времени");
                    loading.setMessage("Пожалуйста подождите");
                    loading.setCanceledOnTouchOutside(false);
                    loading.show();
                    Calendar calendar=Calendar.getInstance();
                    SimpleDateFormat currentData=new SimpleDateFormat("ddMMyyyy");
                    saveData= currentData.format(calendar.getTime());
                    SimpleDateFormat currentTime=new SimpleDateFormat("HHmmss");
                    saveTime= currentTime.format(calendar.getTime());
                    randomKey=saveData+saveTime;
                    StorageReference filePath=imageRef.child(imageUri.getLastPathSegment()+randomKey+".webm");
                    final UploadTask uploadTask=filePath.putFile(imageUri);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String message=e.toString();
                            Toast.makeText(Admin.this,"Ошибка: "+message,Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(Admin.this,"Изображение загружено",Toast.LENGTH_SHORT).show();
                            Task<Uri>uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if(!task.isSuccessful()){
                                        throw task.getException();
                                    }
                                    downloadeImage=filePath.getDownloadUrl().toString();
                                    return filePath.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(Admin.this,"Изображение сохранено",Toast.LENGTH_SHORT).show();
                                        HashMap<String,Object> productMap=new HashMap<>();
                                        productMap.put("id",randomKey);
                                        productMap.put("data",saveData);
                                        productMap.put("time",saveTime);
                                        productMap.put("description",pDesk);
                                        productMap.put("price",pPrice);
                                        productMap.put("name",pName);
                                        productMap.put("count",pCount);
                                        productMap.put("mark",pMark);
                                        productMap.put("image",downloadeImage);
                                        productMap.put("category",categoryName);
                                        productsRef.child(randomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    loading.dismiss();
                                                    Toast.makeText(Admin.this, "Товар добавлен", Toast.LENGTH_SHORT).show();
                                                    Intent logIntent = new Intent(Admin.this, AdminHome.class);
                                                    startActivity(logIntent);
                                                }
                                                else {
                                                    String message=task.getException().toString();
                                                    Toast.makeText(Admin.this, "Ошибка: "+message, Toast.LENGTH_SHORT).show();
                                                    loading.dismiss();
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
    }
    private void GalleryInit() {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,GALLERYPICK);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERYPICK&&resultCode==RESULT_OK&&data!=null){
            imageUri=data.getData();
            productImage.setImageURI(imageUri);
        }
    }
}
