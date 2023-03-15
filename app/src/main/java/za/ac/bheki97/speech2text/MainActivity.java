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
import za.ac.bheki97.speech2text.model.user.retrofit.RetrofitService;
import za.ac.bheki97.speech2text.model.user.retrofit.UserApi;

public class MainActivity extends AppCompatActivity {

    private  ActivityMainBinding binding;
    private TextView registerLink,fgrtPassword;
    private Button loginBtn;
    private UserApi userApi;

    private TextInputEditText passwordEditTxt,emailEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
        Toast.makeText(MainActivity.this,"Function Not Yet Available",Toast.LENGTH_SHORT);
    }

    private void addOnClickForLoginBtn() throws UserInputFieldException{
        loginBtn.setOnClickListener(view -> {
            String email = emailEditText.getText().toString();
            if(!email.isEmpty()){
                String password = passwordEditTxt.getText().toString();
                if(!password.isEmpty()){
                    Toast.makeText(this,"Email Field is Empty",Toast.LENGTH_SHORT).show();
                }else{
                    AuthRequest auth = new AuthRequest(email,password);
                    authenticateUser(auth);
                }
            }else{
                Toast.makeText(this,"Email Field is Empty",Toast.LENGTH_SHORT).show();
            }



        });
    }

    private void authenticateUser(AuthRequest auth) {
        userApi.loginUser(auth).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                startHomeActivity(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startHomeActivity(String token) {
            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
            startActivity(intent);


    }

    private void addOnClickForRegLink() {
        registerLink.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,Registration.class);
            startActivity(intent);
        });

    }
}