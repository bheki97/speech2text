package za.ac.bheki97.speech2text;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import za.ac.bheki97.speech2text.databinding.ActivityHomeBinding;
import za.ac.bheki97.speech2text.exception.EventException;
import za.ac.bheki97.speech2text.model.JoinEventDto;
import za.ac.bheki97.speech2text.model.event.Event;
import za.ac.bheki97.speech2text.model.retrofit.RetrofitService;
import za.ac.bheki97.speech2text.model.retrofit.UserApi;
import za.ac.bheki97.speech2text.model.user.AuthUserInfo;

public class HomeActivity extends AppCompatActivity {


    private static AuthUserInfo userInfo;
    private static boolean audioRecordingPermissionGranted;

    private AppBarConfiguration mAppBarConfiguration;
    //private User user;
    private static ActivityHomeBinding binding;

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final String[] permissions = {Manifest.permission.RECORD_AUDIO};

    private static List<Event> myEvents = null;
    private static List<Event> joinedEvents = null;


    private UserApi retrofitApi;
    private RetrofitService retrofitService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfo = (AuthUserInfo) getIntent().getSerializableExtra("userInfo");

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        retrofitService = new RetrofitService();
        retrofitApi = retrofitService.getRetrofit().create(UserApi.class);

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);


        setSupportActionBar(binding.appBarHome.toolbar);
        binding.appBarHome.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openJoinCreateEventDialog();
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

    private void openJoinCreateEventDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(HomeActivity.this);

        builder.setTitle("Confirm");
        builder.setMessage("Do you want to Join or Create New Event?");
        builder.setPositiveButton("Join Event", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startJoinEventActivty();
            }
        });

        builder.setNegativeButton("Create Event", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startCreateEventActivty();
            }
        });

          builder.create().show();
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                String code = result.getContents();
                if(code!=null){
                    String arr[] = code.split("##");
                    System.out.println("Length of arr: "+arr.length);
                    try {
                        if(arr.length!=3)
                            throw new EventException("Invalid QR code: "+ Arrays.asList(arr));

                        joinEventCal(arr[0]);




                    } catch (EventException e) {
                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(HomeActivity.this);

                        builder.setTitle("Error");
                        builder.setMessage(e.getMessage());
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                    }

                }

//                if(result.getContents() !=null) {
//                    //openMainActivity(result.getContents());
//
//                    AlertDialog.Builder builder = new  AlertDialog.Builder( HomeActivity.this);
//                    builder.setTitle("Results");
//                    builder.setMessage("Invalid Event Code.\nReason might be that the Functionality " +
//                            "is not yet implemented");
//                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//
//                    builder.create().show();
//                }
            });

    private void joinEventCal(String s) {


        retrofitApi.joinEvent(new JoinEventDto(s,userInfo.getUser().getIdNumber())).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                if(response.code()==200){
                    if(response.body())
                        Toast.makeText(HomeActivity.this,"Successfully Joined!!!",Toast.LENGTH_SHORT).show();
                    else Toast.makeText(HomeActivity.this,"Failed to Join",Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        Toast.makeText(HomeActivity.this,response.errorBody().string(),Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(HomeActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(HomeActivity.this,"Server Error, connection Failed",Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void startCreateEventActivty() {
        startActivity(new Intent(HomeActivity.this,CreateEventActivity.class));

    }

    private void startJoinEventActivty() {
        ScanOptions options = new ScanOptions();

        options.setPrompt("Volume up to flash");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);

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

    public static List<Event> getMyEvents() {
        return myEvents;
    }

    public static void setMyEvents(List<Event> myEvents) {
        HomeActivity.myEvents = myEvents;
    }

    public static List<Event> getJoinedEvents() {
        return joinedEvents;
    }

    public static void setJoinedEvents(List<Event> joinedEvents) {
        HomeActivity.joinedEvents = joinedEvents;
    }

    public static AuthUserInfo getUserInfo(){
        return userInfo;
    }

}