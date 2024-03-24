package com.example.technodrive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.technodrive.Admin.AdminHome;
import com.example.technodrive.General.General;
import com.example.technodrive.Pattern.Users;
import com.example.technodrive.Users.Home;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class Login extends AppCompatActivity {
    private Button loginButton;
    private EditText emailLogin,passwordLogin,adminPasswordLogin;
    private String adminUser="Users";
    private CheckBox checkBoxRememberUser;
    private ProgressDialog loadingWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton=(Button)findViewById(R.id.buttonLogin);
        emailLogin=(EditText) findViewById(R.id.editTextEmail);
        passwordLogin=(EditText) findViewById(R.id.editTextPassword);
        adminPasswordLogin=(EditText) findViewById(R.id.adminCode);
        checkBoxRememberUser=(CheckBox)findViewById(R.id.checkboxRemember);
        Paper.init(this);
        loadingWindow=new ProgressDialog(this);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });
    }
    private void userLogin() {
        String email = emailLogin.getText().toString();
        String password = passwordLogin.getText().toString();
        String adminCode = adminPasswordLogin.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
        } else {
            loadingWindow.setTitle("Выполняется вход");
            loadingWindow.setMessage("Пожалуйста подождите");
            loadingWindow.setCanceledOnTouchOutside(false);
            loadingWindow.show();

            CheckInfoLogin(email, password);
        }
        if(!(TextUtils.isEmpty(adminCode))){
            loadingWindow.setTitle("Выполняется вход");
            loadingWindow.setMessage("Пожалуйста подождите");
            loadingWindow.setCanceledOnTouchOutside(false);
            loadingWindow.show();
            adminUser="Admin";
            CheckAdminLogin(email, password,adminCode);
        }
    }
    private void CheckAdminLogin(String email, String password, String adminCode) {
        adminUser="Admin";
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(adminUser).child(email).exists()) {
                    Users userInfoAdmin = snapshot.child(adminUser).child(email).getValue(Users.class);
                    if(userInfoAdmin.getEmail().equals(email)){
                        if (userInfoAdmin.getEmail().equals(email) && userInfoAdmin.getPassword().equals(password) && userInfoAdmin.getEmail().equals(adminCode)) {
                           if(adminUser.equals("Admin")){
                                loadingWindow.dismiss();
                                Toast.makeText(Login.this, "Вход прошел успешно", Toast.LENGTH_SHORT).show();
                                Intent homeIntentAdmin=new Intent(Login.this, AdminHome.class);
                                startActivity(homeIntentAdmin);
                            }
                        } else {
                            loadingWindow.dismiss();
                            Toast.makeText(Login.this, "Неверный пароль", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    loadingWindow.dismiss();
                    Toast.makeText(Login.this, "Этот номер не зарегистрирован", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingWindow.dismiss();
                Toast.makeText(Login.this, "Ошибка: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void CheckInfoLogin(String email, String password) {
        if(checkBoxRememberUser.isChecked()){
            Paper.book().write(General.userRememberEmail,email);
            Paper.book().write(General.userRememberPassword,password);
        }
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(adminUser).child(email).exists()) {
                    Users userInfo = snapshot.child(adminUser).child(email).getValue(Users.class);
                    if(userInfo.getEmail().equals(email)){
                        if (userInfo.getEmail().equals(email) && userInfo.getPassword().equals(password)) {
                            if(adminUser.equals("Users")){
                                loadingWindow.dismiss();
                                Toast.makeText(Login.this, "Вход прошел успешно", Toast.LENGTH_SHORT).show();
                                Intent homeIntent=new Intent(Login.this, Home.class);
                                startActivity(homeIntent);
                            }
                        } else {
                            loadingWindow.dismiss();
                            Toast.makeText(Login.this, "Неверный пароль", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    loadingWindow.dismiss();
                    Toast.makeText(Login.this, "Этот номер не зарегистрирован", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingWindow.dismiss();
                Toast.makeText(Login.this, "Ошибка: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}