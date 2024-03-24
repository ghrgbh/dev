package com.example.technodrive.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.technodrive.R;

public class AdminHome extends AppCompatActivity {
    private TextView service,addProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        service=(TextView)findViewById(R.id.textViewService);
        addProduct=(TextView)findViewById(R.id.textViewAddProduct);
        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminHome.this, Admin.class);
                intent.putExtra("category","service");
                startActivity(intent);
            }
        });
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminHome.this, Admin.class);
                intent.putExtra("category","service");
                startActivity(intent);
            }
        });
    }
}