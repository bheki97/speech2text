package za.ac.bheki97.speech2text.recycler.myevents;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import za.ac.bheki97.speech2text.databinding.ActivityEditMyEventBinding;

public class EditMyEventActivity extends AppCompatActivity {

    private ActivityEditMyEventBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditMyEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}