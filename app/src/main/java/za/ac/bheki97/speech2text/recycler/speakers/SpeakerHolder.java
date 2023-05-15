package za.ac.bheki97.speech2text.recycler.speakers;


import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import za.ac.bheki97.speech2text.databinding.RowSpeakerBinding;
import za.ac.bheki97.speech2text.recycler.speakers.model.Speaker;

//Partake into the Event
public class SpeakerHolder extends RecyclerView.ViewHolder {

    private RowSpeakerBinding binding;


    public SpeakerHolder(RowSpeakerBinding binding) {
        super(binding.getRoot());
    }

    public void bind(Speaker speaker){

    }
}
