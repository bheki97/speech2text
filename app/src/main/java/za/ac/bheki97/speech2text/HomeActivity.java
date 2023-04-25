package za.ac.bheki97.speech2text;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import za.ac.bheki97.speech2text.databinding.ActivityHomeBinding;
import za.ac.bheki97.speech2text.model.user.AuthUserInfo;
import za.ac.bheki97.speech2text.model.user.User;

public class HomeActivity extends AppCompatActivity {

    private static AuthUserInfo userInfo;
    private static boolean audioRecordingPermissionGranted;

    private AppBarConfiguration mAppBarConfiguration;
    //private User user;
    private static ActivityHomeBinding binding;

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final String[] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfo = (AuthUserInfo) getIntent().getSerializableExtra("userInfo");

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);


        setSupportActionBar(binding.appBarHome.toolbar);
        binding.appBarHome.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home,
                 R.id.nav_my_events, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //configure Logout Button
        setLogoutListener(navigationView.getMenu().findItem(R.id.nav_logout));


        //Configure ViewProfile
        startViewProfileIntent((ImageView) binding.navView.getHeaderView(0).findViewById(
                R.id.profileImageView));

        TextView usernameView = (TextView)binding.navView.getHeaderView(0).findViewById(R.id.username);
        TextView emailView = (TextView)binding.navView.getHeaderView(0).findViewById(R.id.emailView);
        usernameView.setText(userInfo.getUser().getFirstname()+" "+ userInfo.getUser().getLastname());
        emailView.setText(userInfo.getUser().getEmail());


    }

    private void startViewProfileIntent(ImageView viewById) {
        viewById.setOnClickListener(view ->
        {

            Intent intent = new Intent(HomeActivity.this,ProfileActivity.class);
            intent.putExtra("userInfo",userInfo);
            startActivity(intent);
        });
    }

    private void setLogoutListener(MenuItem item) {
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                finish();

                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public static void updateUserInfo(AuthUserInfo userInfo){
        HomeActivity.userInfo = userInfo;
        TextView usernameView = (TextView)binding.navView.getHeaderView(0).findViewById(R.id.username);
        TextView emailView = (TextView)binding.navView.getHeaderView(0).findViewById(R.id.emailView);
        usernameView.setText(userInfo.getUser().getFirstname()+" "+ HomeActivity.userInfo.getUser().getLastname());
        emailView.setText(HomeActivity.userInfo.getUser().getEmail());
        binding.navView.invalidate();
    }
    public static boolean isAudioRecordingPermissionGranted() {
        return audioRecordingPermissionGranted;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                audioRecordingPermissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }

        if (!audioRecordingPermissionGranted) {

        }
    }

    public static AuthUserInfo getUserInfo(){
        return userInfo;
    }

}