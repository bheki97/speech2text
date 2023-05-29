package za.ac.bheki97.speech2text.recycler.myevents;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.textfield.TextInputEditText;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import za.ac.bheki97.speech2text.CreateEventActivity;
import za.ac.bheki97.speech2text.databinding.ActivityEditMyEventBinding;
import za.ac.bheki97.speech2text.exception.UserInputFieldException;
import za.ac.bheki97.speech2text.model.event.Event;
import za.ac.bheki97.speech2text.model.retrofit.RetrofitService;
import za.ac.bheki97.speech2text.model.retrofit.UserApi;
import za.ac.bheki97.speech2text.recycler.guest.GuestAdapter;

public class EditMyEventActivity extends AppCompatActivity {

    private ActivityEditMyEventBinding binding;
    private LocalDateTime date;
    private boolean isTimeValid = false;
    private Event event,eventChanges;
    private UserApi retrofitApi;
    private RetrofitService retrofitService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditMyEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
         eventChanges = event = (Event) getIntent().getSerializableExtra("event");

        if(event==null){
            Toast.makeText(this, "Event Does not Exist!!!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        binding.guestsRecycler.setLayoutManager(new LinearLayoutManager(this));
        retrofitService = new RetrofitService();
        retrofitApi = retrofitService.getRetrofit().create(UserApi.class);

        //prefill event data
        binding.idOccasion.setText(event.getOccasion());
        binding.editDescption.setText(event.getDescription());
        binding.dateTimeEditText.setText(event.getDate());

        setOnClickListenerForUpdateButton();
    }

    private void setOnClickListenerForUpdateButton() {
        binding.btnUpdate.setOnClickListener(v ->{

            try {
                validateInputDate();

                makeARetrofitCallForChanges();

            } catch (UserInputFieldException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }


        });


    }

    private void makeARetrofitCallForChanges() {
        retrofitApi.updateHostedEvent(eventChanges).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.code()==200 && response.body()!=null && response.body()){

                    event = new Event(eventChanges);


                    Toast.makeText(EditMyEventActivity.this, "Successfully Edited!!!", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }

    private void validateInputDate() throws UserInputFieldException {
        String txtOcc = binding.idOccasion.getText().toString();
        String txtDesc = binding.editDescption.getText().toString();

        eventChanges.setOccasion(txtOcc);
        eventChanges.setDescription(txtDesc);

        if(txtOcc.isEmpty())
            throw new UserInputFieldException("Please enter occasion");
        if(txtDesc.isEmpty())
            throw new UserInputFieldException("Please enter occasion");


        if(!isTimeValid)
            throw new UserInputFieldException("That's too soon please change date");



    }


    public void showDateTimePicker(View view) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(EditMyEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);



                        try {
                            date = LocalDateTime.of(year,month,day,hour,minute);
                        }catch (DateTimeException dte){
                            System.out.println("Error Occurred");
                            date = LocalDateTime.of(year,month+1,day,hour,minute);
                        }
                        System.out.println("Date Valid now: "+ date.plusMonths(1).isAfter(LocalDateTime.now()));
                        isTimeValid = date.plusMonths(1).isAfter(LocalDateTime.now());
                        //isTimeValid &= date.equals(LocalDateTime.parse(event.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                        ((TextInputEditText) view).setText(sdf.format(calendar.getTime()));
                    }
                }, hourOfDay, minute, false);
                timePickerDialog.show();
            }
        }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadGuest();
    }

    private void loadGuest() {
        GuestAdapter adapter = new GuestAdapter(event);
        binding.guestsRecycler.setAdapter(adapter);
    }


}