package za.ac.bheki97.speech2text;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import za.ac.bheki97.speech2text.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private  ActivityMainBinding binding;
    private TextView registerLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        registerLink = binding.registerLink;
        addOnClickForRegLink();




    }

    private void addOnClickForRegLink() {
        registerLink.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,Registration.class);
            startActivity(intent);
        });

    }
}