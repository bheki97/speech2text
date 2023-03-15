package za.ac.bheki97.speech2text;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import za.ac.bheki97.speech2text.databinding.ActivityRegistrationBinding;
import za.ac.bheki97.speech2text.model.user.User;
import za.ac.bheki97.speech2text.model.user.retrofit.RetrofitService;
import za.ac.bheki97.speech2text.model.user.retrofit.UserApi;

public class Registration extends AppCompatActivity {

    private ActivityRegistrationBinding binding;
    private RetrofitService retrofitService;
    private UserApi userApi;
    private Button registerButton;
    private TextView loginLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //configure Register Button
        registerButton = binding.registerbtn;
        addOnClickListenerForRegisterBtn();

        //Configure Login Link
        loginLink  = binding.loginLink;
        addOnClickListenerForLoginLink();

        retrofitService = new RetrofitService();
        userApi = retrofitService.getRetrofit().create(UserApi.class);


    }

    private void addOnClickListenerForLoginLink() {
        loginLink.setOnClickListener(view -> {
            Intent intent = new Intent(Registration.this,MainActivity.class);
            startActivity(intent);
        });

    }

    private void addOnClickListenerForRegisterBtn() {
        registerButton.setOnClickListener(view ->{
            User user = new User();
            user.setFirstname(binding.firstnameInput.getText().toString());
            user.setLastname(binding.lastnameInput.getText().toString());
            user.setEmail(binding.emailInput.getText().toString());
            user.setContactNo(new Long(binding.contactsInput.getText().toString()));
            if(binding.femaleRadio.isChecked()){
                user.setGender(new Character('F'));
            }else{
                user.setGender(new Character('M'));
            }

            userApi.registerAcc(user).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Toast.makeText(Registration.this,"Successfully Registered!!!!",Toast.LENGTH_LONG);
                    Intent homeIntent = new Intent(Registration.this,HomeActivity.class);
                    homeIntent.putExtra("user",user);
                    startActivity(homeIntent);

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(Registration.this,"Failed To Register",Toast.LENGTH_LONG);
                }
            });

        });
    }

    private void failedToRegister() {
        Toast.makeText(this,"Failed To Register",Toast.LENGTH_LONG);
    }

    private void openHomeActivity(User user) {
        Toast.makeText(this,"Successfully Registered!!!!",Toast.LENGTH_LONG);
        Intent homeIntent = new Intent(this,HomeActivity.class);
        homeIntent.putExtra("user",user);
    }
}