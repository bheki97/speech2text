package za.ac.bheki97.speech2text.recycler.speakers;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import za.ac.bheki97.speech2text.databinding.RowSpeakerBinding;
import za.ac.bheki97.speech2text.recycler.speakers.model.Speaker;

public class SpeakerAdapter extends RecyclerView.Adapter<SpeakerHolder>{

    private List<Speaker> speakers;

    public SpeakerAdapter(List<Speaker> speakers) {
        this.speakers = speakers;
    }

    @NonNull
    @Override
    public SpeakerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowSpeakerBinding binding = RowSpeakerBinding.inflate(LayoutInflater.from(parent.getContext()));

        return new SpeakerHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SpeakerHolder holder, int position) {
        Speaker speaker = speakers.get(position);
        holder.bind(speaker);
    }

    @Override
    public int getItemCount() {
        return speakers.size();
    }
}
