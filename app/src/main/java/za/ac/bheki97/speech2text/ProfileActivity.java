package za.ac.bheki97.speech2text;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import za.ac.bheki97.speech2text.databinding.ActivityProfileBinding;
import za.ac.bheki97.speech2text.exception.UserInputFieldException;
import za.ac.bheki97.speech2text.model.user.AuthUserInfo;
import za.ac.bheki97.speech2text.model.user.User;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;
    private Button updateAccBtn, makeChangesBtn,delAccBtn;
    private AuthUserInfo userInfo;
    private TextView passwrdReset;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfo = (AuthUserInfo) getIntent().getSerializableExtra("userInfo");
        user = userInfo.getUser();


        //initialize Binding
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        //initial User to the Edit Text
        initialsEditText();

        //Configure Edit Text for changes
        makeChangesBtn = binding.makeChangeBtn;
        enableEditTextForChanges();

        //Configure Password Reset Text View
        passwrdReset = binding.passwrdReset;
        setOnclickListenerForPasswrdRest();

        //Configure Delete Account Button
        delAccBtn = binding.delAccBtn;
        setOnclickListenerForDelAccBtn();

        //Set Onclick Listener for update acc button
        updateAccBtn = binding.updateAccBtn;
        setOnclickListenerForUpdateAccBtn();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }



    private void setOnclickListenerForDelAccBtn() {
        delAccBtn.setOnClickListener(view ->{
            Toast.makeText(this,"Functionality Not Yet Implemented!!!",Toast.LENGTH_LONG).show();
        });
    }

    private void setOnclickListenerForPasswrdRest() {
        passwrdReset.setOnClickListener(view ->{
            Toast.makeText(this,"Functionality Not Yet Implemented!!!",Toast.LENGTH_LONG).show();
        });

    }

    private void enableEditTextForChanges() {
        makeChangesBtn.setOnClickListener(view ->{
            if(binding.firstnameInput.isEnabled()){
                binding.firstnameInput.setEnabled(false);
                binding.lastnameInput.setEnabled(false);
                binding.emailEditText.setEnabled(false);
                binding.mobileNumEditTxt.setEnabled(false);
                binding.updateAccBtn.setEnabled(false);
                initialsEditText();

            }else{
                binding.firstnameInput.setEnabled(true);
                binding.lastnameInput.setEnabled(true);
                binding.emailEditText.setEnabled(true);
                binding.mobileNumEditTxt.setEnabled(true);
                binding.updateAccBtn.setEnabled(true);
            }
        });

    }

    private void initialsEditText() {
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

            }catch (NumberFormatException numExc){
                Toast.makeText(this,"Mobile Number Invalid",Toast.LENGTH_SHORT).show();
            }catch (UserInputFieldException exc){
                Toast.makeText(this,exc.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

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