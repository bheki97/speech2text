package za.ac.bheki97.speech2text;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import za.ac.bheki97.speech2text.databinding.ActivityRegistrationBinding;
import za.ac.bheki97.speech2text.exception.SpeakTextException;
import za.ac.bheki97.speech2text.exception.UserInputFieldException;
import za.ac.bheki97.speech2text.model.user.User;
import za.ac.bheki97.speech2text.model.retrofit.RetrofitService;
import za.ac.bheki97.speech2text.model.retrofit.UserApi;

public class Registration extends AppCompatActivity {

    private ActivityRegistrationBinding binding;
    private RetrofitService retrofitService;
    private boolean isPasswordValid = false;
    private UserApi userApi;
    private Button registerButton;
    private TextView loginLink,valSpecChar,valLowercase,valUppercase,valNumeric,valCharLength;
    private TextInputEditText passwordEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_item);

        binding.originSelectLang.setAdapter(adapter);

        //configure Register Button
        registerButton = binding.registerbtn;
        addOnClickListenerForRegisterBtn();

        //Configure Login Link
        loginLink  = binding.loginLink;
        addOnClickListenerForLoginLink();

        //Configure Password Edit Text
        passwordEditText = binding.passwrdInput;
        valLowercase = binding.lowercaseTxtView;
        valSpecChar = binding.specCharTxtView;
        valUppercase = binding.uppercaseTxtView;
        valNumeric = binding.numericTxtView;
        valCharLength = binding.txtLengthTxtView;
        addOnChangeListenerForPasswordEditText();


        retrofitService = new RetrofitService();
        userApi = retrofitService.getRetrofit().create(UserApi.class);


    }

    private void addOnChangeListenerForPasswordEditText() {
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               String data = s.toString();
                if(data.matches(".*[0-9].*")){
                  valNumeric.setTextColor(Color.GREEN);
                  isPasswordValid = true;
               }else{
                   valNumeric.setTextColor(Color.RED);
                    isPasswordValid = false;
               }

                //check if at least contains one Lowercase
                if(data.matches(".*[a-z].*")){
                    valLowercase.setTextColor(Color.GREEN);
                    isPasswordValid = true;
                }else{
                    valLowercase.setTextColor(Color.RED);
                    isPasswordValid = false;
                }

                //check if at least contains one Uppercase
                if(data.matches(".*[A-Z].*")){
                    System.out.println("Uppercase Added");
                    valUppercase.setTextColor(Color.GREEN);
                    isPasswordValid = true;
                }else{
                    System.out.println("Uppercase Removed");
                    valUppercase.setTextColor(Color.RED);
                    isPasswordValid = false;
                }

                //check if at least contains one Special Character
                if(data.matches(".*[^a-zA-Z0-9].*")){
                    valSpecChar.setTextColor(Color.GREEN);
                    isPasswordValid = true;
                }else{
                    valSpecChar.setTextColor(Color.RED);
                    isPasswordValid = false;
                }

                //check if at least contains one Special Character
                if(data.length()>8){
                    valCharLength.setTextColor(Color.GREEN);
                    isPasswordValid = true;
                }else{
                    valCharLength.setTextColor(Color.RED);
                    isPasswordValid = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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



        String data = binding.idInput.getText().toString();
        if(data.isEmpty()){
            throw new UserInputFieldException("The Id number Field is Empty");
        }else{
            if(data.length()==13){
               // data.contains(['a','b']);
                if(data.matches("^[0-9]+$")){
                    user.setIdNumber(data);
                    data = data.substring(6,10);

                    if(Integer.parseInt(data)<5000){
                        user.setGender('F');
                    }else{
                        user.setGender('M');
                    }

                }else{
                    throw new UserInputFieldException("Id must Contain Digits Only");
                }

            }else{
                throw new UserInputFieldException("Id must 13 characters Long");
            }
        }

        data = binding.emailInput.getText().toString();
        if(data.isEmpty()){
            throw new UserInputFieldException("Email Field is Empty");
        }else{

            if(data.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")){
                user.setEmail(data);
            }else{
                throw new UserInputFieldException("Invalid email Format");
            }
        }

        data = binding.contactsInput.getText().toString();
        if(data.isEmpty()){
            throw new UserInputFieldException("Mobile Number Field is Empty");
        }else{
            if(data.matches("^[0-9]+$")){
               //data.matches("^[0-9]+$")
                if(data.length()==10){
                    user.setContactNo(data);

                }else{
                    throw new UserInputFieldException("Mobile Number Must Be 10 digits");
                }
            }else{
                throw new UserInputFieldException("Mobile Number Must Contain digits only");
            }
        }

        //set Language
        if(binding.originSelectLang.getText().toString()==null||
                binding.originSelectLang.getText().toString().isEmpty()){
            throw new UserInputFieldException("Choose Your Preferred language of translation\n" +
                    "N.B only English&Afrikaans are Supported for Audio conversion");
        }
        user.setLanguage(changeToLangCode(binding.originSelectLang.getText().toString()));


        if(isPasswordValid){
            user.setPassword(binding.passwrdInput.getText().toString());
        }else{
            throw new UserInputFieldException("Enter Valid Password");
        }




        return user;
    }

    private void sendRegisterRequest(User user) {
        userApi.registerAcc(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if(response.code()==200){
                    Toast.makeText(Registration.this,"User Registered",Toast.LENGTH_SHORT).show();
                    String msg = "Congratulations " +response.body().getFirstname()+"!!!You Have " +
                            "Successfully Registered on the Prita App";
                    MaterialAlertDialogBuilder builder =  setBuilder("Registration Success",msg)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Registration.this,MainActivity.class);
                                    startActivity(intent);
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    try {
                        String msg = response.errorBody().string();
                        MaterialAlertDialogBuilder builder = setBuilder("Registration Success",msg)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }catch (IOException e){
                        Toast.makeText(Registration.this,"Unkown Error",Toast.LENGTH_SHORT).show();
                    }
                }
//

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Logger.getLogger(Registration.class.getName()).log(Level.SEVERE,t.toString(), t);
                //Toast.makeText(Registration.this,"Server Offline",Toast.LENGTH_LONG).show();
            }
        });

    }

    private MaterialAlertDialogBuilder setBuilder(String title, String msg) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Registration.this);
        builder.setTitle(title)
                .setMessage(msg);


        return builder;

    }

    private String changeToLangCode(String lang){

        if(lang.equalsIgnoreCase("Zulu")){
            return "zu-ZA";
        }else if(lang.equalsIgnoreCase("Sotho")){
            return "st-ZA";
        }else if(lang.equalsIgnoreCase("Tsonga")){
            return "ts-ZA";
        }else if(lang.equalsIgnoreCase("English")){
            return "en-US";
        }else if(lang.equalsIgnoreCase("Afrikaans")){
            return "af-ZA";
        }else{
            return lang;
        }

    }


    private void openHomeActivity(User user) {
        Toast.makeText(this,"Successfully Registered!!!!",Toast.LENGTH_LONG).show();
        Intent homeIntent = new Intent(this,HomeActivity.class);
        homeIntent.putExtra("user",user);
    }
}