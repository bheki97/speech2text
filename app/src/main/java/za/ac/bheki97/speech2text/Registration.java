package za.ac.bheki97.speech2text;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import za.ac.bheki97.speech2text.databinding.ActivityHomeBinding;
import za.ac.bheki97.speech2text.databinding.ActivityRegistrationBinding;
import za.ac.bheki97.speech2text.model.user.User;
import za.ac.bheki97.speech2text.model.user.retrofit.RetrofitService;
import za.ac.bheki97.speech2text.model.user.retrofit.UserApi;

public class Registration extends AppCompatActivity {

    private ActivityRegistrationBinding binding;
    private RetrofitService retrofitService;
    private UserApi userApi;
    private Button registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        registerButton = binding.registerbtn;
        retrofitService = new RetrofitService();
        userApi = retrofitService.getRetrofit().create(UserApi.class);

        registerButton.setOnClickListener(view ->{
            User user = new User();
            user.setFirstname("Bheki");
            user.setLastname("Mautjana");
            user.setEmail("bfmokoena7@gmail.com");
            user.setContactNo(27760794703L);
            user.setGender(new Character('M'));

            userApi.registerAcc(user).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                    openHomeActivity(user);

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });

        });

    }

    private void openHomeActivity(User user) {
        Toast.makeText(this,"Successfully Registered!!!!",Toast.LENGTH_LONG);
        Intent homeIntent = new Intent(this,HomeActivity.class);
        homeIntent.putExtra("user",user);
    }
}