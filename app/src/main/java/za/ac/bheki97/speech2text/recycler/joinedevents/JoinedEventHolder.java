package za.ac.bheki97.speech2text.recycler.joinedevents;


import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import za.ac.bheki97.speech2text.HomeActivity;
import za.ac.bheki97.speech2text.databinding.RowJoinedEventBinding;
import za.ac.bheki97.speech2text.model.event.GuestEvent;
import za.ac.bheki97.speech2text.model.retrofit.RetrofitService;
import za.ac.bheki97.speech2text.model.retrofit.UserApi;

//this holder is for the view Joined Events
public class JoinedEventHolder extends RecyclerView.ViewHolder{

    private RowJoinedEventBinding binding;
    private int position;
    private JoinedEventAdapter adapter;
    private UserApi retrofitApi;
    private RetrofitService retrofitService;
    private GuestEvent event;


    public JoinedEventHolder(RowJoinedEventBinding binding) {

        super(binding.getRoot());
        this.binding = binding;

        retrofitService = new RetrofitService();
        retrofitApi = retrofitService.getRetrofit().create(UserApi.class);
    }

    public void bind(int position, JoinedEventAdapter adapter){
        this.position = position;
        this.adapter = adapter;

        //setting the GuestEvent Obj for the Holder
        this.event = this.adapter.getEventAtPos(this.position);
        
        //assign Occasion to the view
        binding.txtOccasion.setText(event.getOccasion());

        //assign Host name
        binding.txtHostname.setText(new StringBuilder()
                .append(event.getHost().getAccount().getFirstname()).append(" ")
                .append(event.getHost().getAccount().getLastname()).toString());

        //assign Event Date
        LocalDateTime date = LocalDateTime.parse(event.getDate());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd'th' EEEE MMM yyyy, HH:mm");
        binding.txtEventDate.setText(date.format(formatter));
        event.setDate(event.getDate().substring(0,event.getDate().length()-3));
        //Set Onclick for the leave Button
        leaveEvent();

        binding.getRoot().setOnClickListener( v->{

            if(event.getLocalDateTime().isBefore(LocalDateTime.now())){
                Toast.makeText(binding.getRoot().getContext() ,"Event Started", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(binding.getRoot().getContext(), "Event has not yet Started", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void leaveEvent(){
        binding.btnLeave.setOnClickListener(v ->{
            retrofitApi.leaveJoinedEvent(adapter.getEventAtPos(position).getEventKey(),
                    HomeActivity.getUserInfo().getUser().getIdNumber()).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if(response.code()==200){
                        adapter.removeItem(position);
                        Toast.makeText(binding.getRoot().getContext(), "Successfully Left the event", Toast.LENGTH_SHORT).show();

                        return;
                    }

                    try {
                        Toast.makeText(binding.getRoot().getContext(), "Server Error"+response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Toast.makeText(binding.getRoot().getContext(), "No Connection!!!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
