package za.ac.bheki97.speech2text.recycler.myevents;


import android.content.DialogInterface;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import za.ac.bheki97.speech2text.databinding.RowMyEventBinding;
import za.ac.bheki97.speech2text.model.event.Event;
import za.ac.bheki97.speech2text.model.retrofit.RetrofitService;
import za.ac.bheki97.speech2text.model.retrofit.UserApi;

//This holder is for the view Created/Hosted Events
public class MyEventHolder extends RecyclerView.ViewHolder{

    private RowMyEventBinding binding;
    private UserApi retrofitApi;
    private RetrofitService retrofitService;
    private Event event;
    private MyEventAdapter adapter;
    private int position;


    public MyEventHolder(RowMyEventBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        retrofitService = new RetrofitService();
        retrofitApi = retrofitService.getRetrofit().create(UserApi.class);

    }

    public void bind(Event event,int position,MyEventAdapter adapter){
        this.event = event;
        this.adapter = adapter;
        this.position  = position;
        binding.occasionTxtView.setText(event.getOccasion());
        binding.descriptionTxtView.setText(event.getDescription());
        setOnclickListenerRmvBtn();

    }

    public void setOnclickListenerEditBtn(){
        binding.edtBtn.setOnClickListener( v ->{

        });
    }
    public void setOnclickListenerRmvBtn(){
        binding.rmvBtn.setOnClickListener( v ->{
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(binding.getRoot().getContext());
            builder.setTitle("confirm? ")
                    .setMessage("Are you sure want to delete "+ event.getOccasion()+" occasion? ")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteEventCall();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            builder.create().show();

        });
    }

    private void deleteEventCall() {
        retrofitApi.deleteEvent(event.getEventKey()).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                if(response.code()==200){
                    adapter.CancelEvent(position);
                    Toast.makeText(binding.getRoot().getContext(), "Event Successfully Cancelled", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }

    public void setOnclickListenerInvBtn(){
        binding.invBtn.setOnClickListener( v ->{

        });
    }

}
