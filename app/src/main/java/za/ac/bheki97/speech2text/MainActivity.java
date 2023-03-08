package za.ac.bheki97.speech2text;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import za.ac.bheki97.speech2text.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}