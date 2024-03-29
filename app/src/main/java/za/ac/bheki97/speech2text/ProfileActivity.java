package za.ac.bheki97.speech2text;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import za.ac.bheki97.speech2text.databinding.ActivityProfileBinding;
import za.ac.bheki97.speech2text.exception.UserInputFieldException;
import za.ac.bheki97.speech2text.model.MessageDto;
import za.ac.bheki97.speech2text.model.retrofit.RetrofitService;
import za.ac.bheki97.speech2text.model.retrofit.UserApi;
import za.ac.bheki97.speech2text.model.user.AuthUserInfo;
import za.ac.bheki97.speech2text.model.user.User;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;
    private Button updateAccBtn, makeChangesBtn,delAccBtn;
    private AuthUserInfo userInfo;
    private TextView passwrdReset;
    private User user;
    private String transLang;

    private UserApi retrofitApi;
    private RetrofitService retrofitService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfo = (AuthUserInfo) getIntent().getSerializableExtra("userInfo");
        user = userInfo.getUser();


        //initialize Binding
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        retrofitService = new RetrofitService();
        retrofitApi = retrofitService.getRetrofit().create(UserApi.class);

        //Configure Language select
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_item);
        binding.translationSelectLang.setAdapter(adapter);
        setOnClickListenerForTranslationSelectLang();


        //initial User to the Edit Text
        updateEditText();

        //Configure Edit Text for changes
        makeChangesBtn = binding.makeChangeBtn;
        enableEditTextForChanges();

        //Configure Password Reset Text View
        //passwrdReset = binding.passwrdReset;
        //setOnclickListenerForPasswrdRest();

        //Configure Delete Account Button
        delAccBtn = binding.delAccBtn;
        setOnclickListenerForDelAccBtn();

        //Set Onclick Listener for update acc button
        updateAccBtn = binding.updateAccBtn;
        setOnclickListenerForUpdateAccBtn();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    private void setOnClickListenerForTranslationSelectLang() {
        binding.translationSelectLang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                transLang = adapterView.getItemAtPosition(position).toString();


            }
        });
    }


    private void setOnclickListenerForDelAccBtn() {
        delAccBtn.setOnClickListener(view ->{
            retrofitApi.deleteAcc(HomeActivity.getUserInfo().getJwtToken(),
                    HomeActivity.getUserInfo().getUser().getIdNumber()).enqueue(new Callback<MessageDto>() {
                @Override
                public void onResponse(Call<MessageDto> call, Response<MessageDto> response) {
                    if(response.code()==200){
                        Toast.makeText(ProfileActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                        //HomeActivity.updateUserInfo(null);

                        finishAffinity();
                        startActivity(new Intent(ProfileActivity.this,MainActivity.class));
                        return;
                    }
                    Toast.makeText(ProfileActivity.this,"Server error",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<MessageDto> call, Throwable t) {
                    Toast.makeText(ProfileActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
                }
            });


            //Toast.makeText(this,"Functionality Not Yet Implemented!!!",Toast.LENGTH_LONG).show();
        });
    }

//    private void setOnclickListenerForPasswrdRest() {
//        passwrdReset.setOnClickListener(view ->{
//            Toast.makeText(this,"Functionality Not Yet Implemented!!!",Toast.LENGTH_LONG).show();
//        });
//
//    }

    private void enableEditTextForChanges() {
        makeChangesBtn.setOnClickListener(view ->{
            if(binding.firstnameInput.isEnabled()){
                binding.firstnameInput.setEnabled(false);
                binding.lastnameInput.setEnabled(false);
                binding.emailEditText.setEnabled(false);
                binding.mobileNumEditTxt.setEnabled(false);
                binding.updateAccBtn.setEnabled(false);
                updateEditText();

            }else{
                binding.firstnameInput.setEnabled(true);
                binding.lastnameInput.setEnabled(true);
                binding.emailEditText.setEnabled(true);
                binding.mobileNumEditTxt.setEnabled(true);
                binding.updateAccBtn.setEnabled(true);
            }
        });

    }

    private void updateEditText() {
        binding.firstnameInput.setText(user.getFirstname());
        binding.lastnameInput.setText(user.getLastname());
        binding.idInput.setText(user.getIdNumber());
        binding.emailEditText.setText(user.getEmail());
        binding.mobileNumEditTxt.setText(user.getContactNo());

    }

    private void setOnclickListenerForUpdateAccBtn() {
        updateAccBtn.setOnClickListener( btn ->{

            try{
                editUserObjUsingInputFields();
                setConfirmUpdate();


            }catch (NumberFormatException numExc){
                Toast.makeText(this,"Mobile Number Invalid",Toast.LENGTH_SHORT).show();
            }catch (UserInputFieldException exc){
                Toast.makeText(this,exc.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void setConfirmUpdate() {

        MaterialAlertDialogBuilder builder =  new MaterialAlertDialogBuilder(ProfileActivity.this)
                .setTitle("Confirm")
                .setMessage("Are you sure you want to update the account?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateAcc();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateAcc() {
        retrofitApi.updateProfile("Bearer "+userInfo.getJwtToken(),user).enqueue(new Callback<AuthUserInfo>() {
            @Override
            public void onResponse(Call<AuthUserInfo> call, Response<AuthUserInfo> response) {
                String msg;
                if(response.code()==200){

                    userInfo = response.body();
                    user = userInfo.getUser();
                    updateEditText();
                    HomeActivity.updateUserInfo(userInfo);
                    msg = user.getFirstname()+" your account has been Successfully";

                }else {
                    try{
                        msg = response.errorBody().string();
                    }catch (IOException exc){
                        msg = "Error Occurred";
                    }
                }

                displayUpdateMessage(msg);
            }

            @Override
            public void onFailure(Call<AuthUserInfo> call, Throwable t) {
                Toast.makeText(ProfileActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayUpdateMessage(String msg) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(ProfileActivity.this);
        builder.setTitle("Success Message")
                .setMessage(msg)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();

    }


    private void editUserObjUsingInputFields() throws UserInputFieldException,NumberFormatException{

        //update firstname
        user.setFirstname(binding.firstnameInput.getText().toString());
        if(user.getFirstname().isEmpty()){
            System.out.println("field empty");
            throw new UserInputFieldException("First Name Field is Empty");
        }

        //update Lastname
        user.setLastname(binding.lastnameInput.getText().toString());
        if(user.getLastname().isEmpty()){
            throw new UserInputFieldException("Last Name Field is Empty");
        }

        //update Email
        String data = binding.emailEditText.getText().toString();
        if(data.isEmpty()){
            throw new UserInputFieldException("Email Field is Empty");
        }else{
            if(data.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")){
                user.setEmail(data);
            }else{
                throw new UserInputFieldException("Use Correct Email Format");
            }
        }

        if(transLang==null || transLang.isEmpty()){
            throw new UserInputFieldException("Email Field is Empty");
        }else{
             user.setLanguage(changeToLangCode(transLang));
        }



        //update Mobile Number
        data = binding.mobileNumEditTxt.getText().toString();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}