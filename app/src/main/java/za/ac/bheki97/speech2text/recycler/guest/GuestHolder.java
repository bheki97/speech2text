package za.ac.bheki97.speech2text.recycler.guest;


import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import za.ac.bheki97.speech2text.databinding.RowEditGuestBinding;
import za.ac.bheki97.speech2text.model.MakeSpeakerDto;
import za.ac.bheki97.speech2text.model.retrofit.RetrofitService;
import za.ac.bheki97.speech2text.model.retrofit.UserApi;
import za.ac.bheki97.speech2text.recycler.guest.model.Guest;
import za.ac.bheki97.speech2text.recycler.guest.model.Speaker;

//this guest holder is for the Edit Event
public class GuestHolder extends RecyclerView.ViewHolder{

    private final RowEditGuestBinding binding;
    private Guest guest;
    private GuestAdapter adapter;
    private UserApi retrofitApi;
    private RetrofitService retrofitService;

    public GuestHolder(RowEditGuestBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        retrofitService = new RetrofitService();
        retrofitApi = retrofitService.getRetrofit().create(UserApi.class);

    }

    public void bind(int position, GuestAdapter adapter){
        this.guest = adapter.getGuests().get(position);
        this.adapter = adapter;
        if(guest instanceof Speaker){
            binding.makeSpeakerBtn.setEnabled(false);
        }
        binding.txtxGuestId.setText(" "+guest.getGuestId());
        binding.txtGuestName.setText(new StringBuilder().append(guest.getAccount().getFirstname())
                .append(" ").append(guest.getAccount().getLastname()).toString());

        setOnClickListenerForMakeSpeakerBtn();

    }

    private void setOnClickListenerForMakeSpeakerBtn() {
        binding.makeSpeakerBtn.setOnClickListener(v ->{
            MakeSpeakerDto dto = new MakeSpeakerDto();
            dto.setGuestId(guest.getGuestId());
            dto.setEventKey(adapter.getEvent().getEventKey());
            dto.setHostId(adapter.getEvent().getHost().getHostId());

            makeARetrofitCallToMakeChange(dto);


        });

    }

    private void makeARetrofitCallToMakeChange(MakeSpeakerDto dto) {
        retrofitApi.changeGuestTitle(dto).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.code()==200){
                    Toast.makeText(binding.getRoot().getContext(), "Successfully Changed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(binding.getRoot().getContext(), "No connection!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
