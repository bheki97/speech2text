package za.ac.bheki97.speech2text;

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

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import za.ac.bheki97.speech2text.databinding.ActivityCreateEventBinding;
import za.ac.bheki97.speech2text.exception.EventException;
import za.ac.bheki97.speech2text.model.event.Event;
import za.ac.bheki97.speech2text.model.retrofit.RetrofitService;
import za.ac.bheki97.speech2text.model.retrofit.UserApi;
import za.ac.bheki97.speech2text.model.user.Host;

public class CreateEventActivity extends AppCompatActivity {

    private ActivityCreateEventBinding binding;
    private LocalDateTime date;
    private boolean isTimeValid = false;


    private UserApi retrofitApi;
    private RetrofitService retrofitService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreateEventBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        retrofitService = new RetrofitService();
        retrofitApi = retrofitService.getRetrofit().create(UserApi.class);
        date =null;

        //setOnclickListener For the Create Event Button
        OnClickListenerForCreateEventBtn();
    }

    private void OnClickListenerForCreateEventBtn() {
        binding.createEventBtn.setOnClickListener(v ->{

            try {

                validateEventValues();

            } catch (EventException e) {
                Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
                return;
            }
            Event event = new Event();
            String name = binding.nameOfEvent.getText().toString().trim();
            String description = binding.descriptionOfEvent.getText().toString().trim();

            event.setEventKey(name+"##"+description+"##"+name+"##"+description+"##"+name+"##"+description);
            event.setDescription(description);
            event.setOccasion(name);
            event.setHost(new Host(HomeActivity.getUserInfo().getUser(),binding.nameOfEvent.getText().toString()));
            event.setDate(date.toString());
            System.out.println(date.toString());

            retrofitApi.hostEvent(HomeActivity.getUserInfo().getJwtToken(),event).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.code()==200){
                        Toast.makeText(CreateEventActivity.this,"Event Created",Toast.LENGTH_SHORT).show();
                    }else{
                        try {
                            Toast.makeText(CreateEventActivity.this,response.errorBody().string(),Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Toast.makeText(CreateEventActivity.this,"Error Occurred",Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });


        });
    }

    private void validateEventValues() throws EventException {
        if(binding.nameOfEvent.getText().toString().isEmpty())
            throw new EventException("Event name cannot be empty!!!");

        if(binding.descriptionOfEvent.getText().toString().isEmpty())
            throw new EventException("Event Description cannot be empty cannot be empty!!!");

        if(binding.brand.getText().toString().isEmpty())
            throw new EventException("Your Host cannot be empty cannot be empty!!!");

        if(binding.descriptionOfEvent.getText().toString()
                .equalsIgnoreCase(binding.nameOfEvent.getText().toString()));

        //System.out.println("Date valid: "+isTimeValid);
        if(!isTimeValid){
            throw new EventException("That's too soon, choose date from Tomorrow!!!!!");
        }


//        if(binding.descriptionOfEvent.getText().toString().split(".").length<3 ?
//            binding.descriptionOfEvent.getText().toString().split("\n").length<3:
//            binding.descriptionOfEvent.getText().toString().split(" ").length<20)
//            throw new EventException("Please give a more descriptive \nDescription with at least 3 sentences");


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
                TimePickerDialog timePickerDialog = new TimePickerDialog(CreateEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
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

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                        ((TextInputEditText) view).setText(sdf.format(calendar.getTime()));
                    }
                }, hourOfDay, minute, false);
                timePickerDialog.show();
            }
        }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

}