package za.ac.bheki97.speech2text;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import za.ac.bheki97.speech2text.databinding.ActivityMainBinding;
import za.ac.bheki97.speech2text.exception.UserInputFieldException;
import za.ac.bheki97.speech2text.model.user.AuthRequest;
import za.ac.bheki97.speech2text.model.retrofit.RetrofitService;
import za.ac.bheki97.speech2text.model.retrofit.UserApi;
import za.ac.bheki97.speech2text.model.user.AuthUserInfo;

public class MainActivity extends AppCompatActivity {

    private  ActivityMainBinding binding;
    private TextView registerLink,fgrtPassword;
    private RetrofitService retrofitService;
    private Button loginBtn;
    private UserApi userApi;

    private TextInputEditText passwordEditTxt,emailEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        retrofitService = new RetrofitService();
        userApi = retrofitService.getRetrofit().create(UserApi.class);

        //Get Email and Password Edit Text
        emailEditText = binding.emailEditText;
        passwordEditTxt = binding.passwordEditText;

        //Configure Register Link;
        registerLink = binding.registerLink;
        addOnClickForRegLink();

        try {
            //Configure Login Button
            loginBtn = binding.loginBtn;
            addOnClickForLoginBtn();

        }catch (UserInputFieldException exc){

        }

        //Configure Forgot Password Link
        fgrtPassword = binding.fgrtPasswordLink;
        addOnClickForFgtPasswordLink();






    }

    private void addOnClickForFgtPasswordLink() {
        binding.fgrtPasswordLink.setOnClickListener(view ->{
            Toast.makeText(MainActivity.this,"Function Not Yet Available",Toast.LENGTH_SHORT).show();
        });
    }

    private void addOnClickForLoginBtn() throws UserInputFieldException{
        loginBtn.setOnClickListener(view -> {
            String email = emailEditText.getText().toString();
            if(!email.isEmpty()){
                String password = passwordEditTxt.getText().toString();
                if(!password.isEmpty()){
                    AuthRequest auth = new AuthRequest(email,password);
                    authenticateUser(auth);
                }else{

                    Toast.makeText(this,"Password Field is Empty",Toast.LENGTH_SHORT).show();
                }
            }else{
                System.out.println("Email: "+email);
                Toast.makeText(this,"Email Field is Empty",Toast.LENGTH_SHORT).show();
            }



        });
    }

    private void authenticateUser(AuthRequest auth) {

        userApi.loginUser(auth).enqueue(new Callback<AuthUserInfo>() {
            @Override
            public void onResponse(Call<AuthUserInfo> call, Response<AuthUserInfo> response) {
                if(response.code()==200){
                    startHomeActivity(response.body());
                    System.out.println(response.code());
                }else{
                    Toast.makeText(MainActivity.this,"Invalid Login Details",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthUserInfo> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Server Offline",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void startHomeActivity(AuthUserInfo userInfo) {
            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
            intent.putExtra("userInfo",userInfo);

            MainActivity.this.startActivity(intent);


    }

    private void addOnClickForRegLink() {
        registerLink.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,Registration.class);
            startActivity(intent);
        });

    }
}