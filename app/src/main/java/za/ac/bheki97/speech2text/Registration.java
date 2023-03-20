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
import za.ac.bheki97.speech2text.exception.UserInputFieldException;
import za.ac.bheki97.speech2text.model.user.User;
import za.ac.bheki97.speech2text.model.retrofit.RetrofitService;
import za.ac.bheki97.speech2text.model.retrofit.UserApi;

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
           try {
               User user = createUserFromUserInput();
               sendRegisterRequest(user);
           }catch (NumberFormatException numExc){
               Toast.makeText(this,"Mobile Number Invalid",Toast.LENGTH_SHORT).show();
           }catch (UserInputFieldException exc){
               Toast.makeText(this,exc.getMessage(),Toast.LENGTH_SHORT).show();

           }

        });
    }

    private User createUserFromUserInput() throws NumberFormatException, UserInputFieldException {
        User user = new User();
        user.setFirstname(binding.firstnameInput.getText().toString());
        if(user.getFirstname().isEmpty()){
            System.out.println("field empty");
            throw new UserInputFieldException("First Name Field is Empty");
        }
        user.setLastname(binding.lastnameInput.getText().toString());
        if(user.getLastname().isEmpty()){
            throw new UserInputFieldException("Last Name Field is Empty");
        }
        user.setEmail(binding.emailInput.getText().toString());
        if(user.getEmail().isEmpty()){
            throw new UserInputFieldException("Email Field is Empty");
        }


        String id = binding.idInput.getText().toString();
        if(id.isEmpty()){
            throw new UserInputFieldException("The Id number Field is Empty");
        }else{
            if(id.length()==13){
               // id.contains(['a','b']);
                if(id.matches("^[0-9]+$")){
                    user.setIdNumber(id);
                }else{
                    throw new UserInputFieldException("Id must Contain Digits Only");
                }

            }else{
                throw new UserInputFieldException("Id must 13 characters Long");
            }
        }

        user.setPassword(binding.passwrdInput.getText().toString());
        if(user.getPassword().isEmpty()){
            throw new UserInputFieldException("Password Field is Empty");
        }
        user.setPassword(binding.idInput.getText().toString());
        if(user.getPassword().isEmpty()){
            throw new UserInputFieldException("Password Field is Empty");
        }




        if(binding.contactsInput.getText().toString().isEmpty()){
            throw new UserInputFieldException("Email Field is Empty");
        }else{
            user.setContactNo(new Long(binding.contactsInput.getText().toString()));
        }

        return user;
    }

    private void sendRegisterRequest(User user) {
        userApi.registerAcc(user).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(Registration.this,response.body(),Toast.LENGTH_SHORT).show();
//                Intent homeIntent = new Intent(Registration.this,HomeActivity.class);
//                homeIntent.putExtra("user",user);
//                startActivity(homeIntent);

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(Registration.this,"Server Offline",Toast.LENGTH_LONG).show();
            }
        });

    }


    private void openHomeActivity(User user) {
        Toast.makeText(this,"Successfully Registered!!!!",Toast.LENGTH_LONG).show();
        Intent homeIntent = new Intent(this,HomeActivity.class);
        homeIntent.putExtra("user",user);
    }
}