package com.example.technodrive;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.HashMap;
public class Regestration extends AppCompatActivity {
    private Button regBtn;
    private EditText regName,regSurname,regCity,regEmail,regPassword,regAdminCode;
    private ProgressDialog loadingWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regestration);
        regBtn=(Button) findViewById(R.id.buttonReg);
        regName=(EditText) findViewById(R.id.editTextNameReg);
        regSurname=(EditText) findViewById(R.id.editTextSurenameReg);
        regCity=(EditText) findViewById(R.id.editTextCityReg);
        regEmail=(EditText) findViewById(R.id.editTextEmailReg);
        regPassword=(EditText) findViewById(R.id.editTextPasswordReg);
        loadingWindow=new ProgressDialog(this);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();
            }
        });
    }
    private void CreateAccount() {
        String userName=regName.getText().toString();
        String userSurname=regSurname.getText().toString();
        String userCity=regCity.getText().toString();
        String userEmail=regEmail.getText().toString();
        String userPassword=regPassword.getText().toString();
        if (TextUtils.isEmpty(regName.getText().toString()) ||
                TextUtils.isEmpty(regSurname.getText().toString()) ||
                TextUtils.isEmpty(regCity.getText().toString()) ||
                TextUtils.isEmpty(regEmail.getText().toString()) ||
                TextUtils.isEmpty(regPassword.getText().toString())) {
            Toast.makeText(this,"Заполните все поля",Toast.LENGTH_SHORT).show();
        }
        else {
            loadingWindow.setTitle("Это займет немного времени");
            loadingWindow.setMessage("Пожалуйста подождите");
            loadingWindow.setCanceledOnTouchOutside(false);
            loadingWindow.show();

            CheckUserInfo(userName, userSurname, userEmail, userCity, userPassword);
        }

    }
    private void CheckUserInfo(String userName, String userSurname, String userEmail, String userCity, String userPassword) {
        DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(userEmail).exists())) {
                    HashMap<String, Object> userInfoMap = new HashMap<>();
                    userInfoMap.put("email", userEmail);
                    userInfoMap.put("name", userName);
                    userInfoMap.put("surname", userSurname);
                    userInfoMap.put("city", userCity);
                    userInfoMap.put("password", userPassword);

                    RootRef.child("Users").child(userEmail).updateChildren(userInfoMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                loadingWindow.dismiss();
                                Toast.makeText(Regestration.this, "Регистрация прошла успешно!", Toast.LENGTH_SHORT).show();
                                Intent logIntent = new Intent(Regestration.this, Login.class);
                                startActivity(logIntent);
                            } else {
                                loadingWindow.dismiss();
                                Toast.makeText(Regestration.this, "Произошла ошибка", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    loadingWindow.dismiss();
                    Toast.makeText(Regestration.this, "Этот номер уже зарегистрирован", Toast.LENGTH_SHORT).show();
                    Intent logIntent = new Intent(Regestration.this, Login.class);
                    startActivity(logIntent);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loadingWindow.dismiss();
                Toast.makeText(Regestration.this, "Ошибка: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}