package za.ac.bheki97.speech2text;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import za.ac.bheki97.speech2text.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private  ActivityMainBinding binding;
    private TextView registerLink,fgrtPassword;
    private Button loginBtn;

    private TextInputEditText passwordEditTxt,emailEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Configure Register Link;
        registerLink = binding.registerLink;
        addOnClickForRegLink();

        //Configure Login Button
        loginBtn = binding.loginBtn;
        addOnClickForLoginBtn();

        //Configure Forgot Password Link
        fgrtPassword = binding.fgrtPasswordLink;
        addOnClickForFgtPaaswordLink();

        //Get Email and Password Edit Text
        emailEditText = binding.emailEditText;
        passwordEditTxt = binding.passwordEditText;




    }

    private void addOnClickForFgtPaaswordLink() {
        Toast.makeText(MainActivity.this,"Function Not Yet Available",Toast.LENGTH_SHORT);
    }

    private void addOnClickForLoginBtn() {
        loginBtn.setOnClickListener(view -> {



//            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
//            startActivity(intent);
        });
    }

    private void addOnClickForRegLink() {
        registerLink.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,Registration.class);
            startActivity(intent);
        });

    }
}