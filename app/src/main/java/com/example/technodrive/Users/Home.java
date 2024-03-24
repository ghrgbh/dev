package com.example.technodrive.Users;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.esotericsoftware.kryo.NotNull;
import com.example.technodrive.General.General;
import com.example.technodrive.Login;
import com.example.technodrive.Pattern.Users;
import com.example.technodrive.Products;
import com.example.technodrive.R;
import com.example.technodrive.Regestration;
import com.example.technodrive.Settings;
import com.example.technodrive.ViewHolder.ProductViewHolder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.technodrive.databinding.ActivityHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Меню");
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Переход в корзину", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }

        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.textViewUserName);
        TextView userSurnameTextView = headerView.findViewById(R.id.textViewUserSurname);
        CircleImageView profileImageView = headerView.findViewById(R.id.imageViewUserPhoto);

        userNameTextView.setText(General.onlineUser.getName());
        userSurnameTextView.setText(General.onlineUser.getSurname());
        Picasso.get().load(General.onlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);

        recyclerView = findViewById(R.id.recyclerViewMenu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Product>().setQuery(ProductsRef, Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull @NotNull ProductViewHolder holder, int i, @NonNull @NotNull Products model) {
                holder.txtProductName.setText(model.getTitle());
                holder.txtProductDescription.setText(model.getDesck());
                holder.txtProductPrice.setText(model.getPrice());
                holder.txtProductCount.setText(model.getCount()+" штук");
                holder.txtProductMark.setText(model.getMark());
                Picasso.get().load(model.getImageProduct()).into(holder.imageProduct);
            }

            @NonNull
            @NotNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
        public void onBackPressed() {
            DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
            if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                drawerLayout.closeDrawer(GravityCompat.START);
            }else{
                super.onBackPressed();
            }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.nav_basket){

        } else if(id == R.id.nav_orders){

        } else if(id == R.id.nav_category){

        } else if(id == R.id.nav_setings){
            Intent loginIntent = new Intent(Home.this, Settings.class);
            startActivity(loginIntent);

        } else if(id == R.id.nav_exit){
            Paper.book().destroy();
            Intent loginIntent = new Intent(Home.this, Login.class);
            startActivity(loginIntent);
        }
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    public static class MainActivity extends AppCompatActivity {
        private Button joinButton, loginButton;
        private ProgressDialog loadingBar;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            joinButton = (Button) findViewById(R.id.btnAuth);
            loginButton = (Button) findViewById(R.id.btnReg);
            loadingBar = new ProgressDialog(this);


            Paper.init(this);

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent loginIntent = new Intent(MainActivity.this, Login.class);
                    startActivity(loginIntent);
                }
            });

            joinButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent registerIntent = new Intent(MainActivity.this, Regestration.class);
                    startActivity(registerIntent);
                }
            });

            String UserPhoneKey = Paper.book().read(General.userRememberEmail);
            String UserPasswordKey = Paper.book().read(General.userRememberEmail);

            if(UserPhoneKey != "" && UserPasswordKey != ""){
                if(!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey) ){
                    ValidateUser(UserPhoneKey,UserPasswordKey);

                    loadingBar.setTitle("Это займет немного времени");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                }
            }
        }

        private void ValidateUser(final String phone, final String password) {
            final DatabaseReference RootRef;
            RootRef = FirebaseDatabase.getInstance().getReference();

            RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child("Users").child(phone).exists())
                    {
                        Users usersData = dataSnapshot.child("Users").child(phone).getValue(Users.class);

                        if(usersData.getEmail().equals(phone))
                        {
                            if(usersData.getPassword().equals(password))
                            {
                                loadingBar.dismiss();
                                Intent homeIntent = new Intent(MainActivity.this, Home.class);
                                General.onlineUser = usersData;
                                startActivity(homeIntent);
                            }
                            else {
                                loadingBar.dismiss();
                            }
                        }
                    }
                    else {
                        loadingBar.dismiss();
                        Toast.makeText(MainActivity.this, "Аккаунта с таким номером не существует", Toast.LENGTH_SHORT).show();

                        Intent registerIntent = new Intent(MainActivity.this, Regestration.class);
                        startActivity(registerIntent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}