package za.ac.bheki97.speech2text.model.recycler.speakers;



import androidx.recyclerview.widget.RecyclerView;

import za.ac.bheki97.speech2text.databinding.RowSpeakerBinding;
import za.ac.bheki97.speech2text.model.recycler.guest.model.Speaker;
import za.ac.bheki97.speech2text.model.retrofit.RetrofitService;
import za.ac.bheki97.speech2text.model.retrofit.UserApi;

//Partake into the Event
public class SpeakerHolder extends RecyclerView.ViewHolder {

    private RowSpeakerBinding binding;
    private UserApi retrofitApi;
    private RetrofitService retrofitService;
    private Speaker speaker;
    SpeakerAdapter adapter;


    public SpeakerHolder(RowSpeakerBinding binding) {

        super(binding.getRoot());
        this.binding= binding;

        retrofitService = new RetrofitService();
        retrofitApi = retrofitService.getRetrofit().create(UserApi.class);



    }

    public void bind(SpeakerAdapter adapter,int position){
        this.adapter = adapter;
        speaker = adapter.getSpeakers().get(position);
        String names = speaker.getAccount().getFirstname()+" "+speaker.getAccount().getLastname();
        binding.txtSpeakername.setText(names);
        binding.txtspeech.setText(speaker.getSpeech());



    }
}
