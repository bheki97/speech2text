package za.ac.bheki97.speech2text.recycler.joinedevents;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import za.ac.bheki97.speech2text.databinding.RowJoinedEventBinding;
import za.ac.bheki97.speech2text.model.event.Event;

public class JoinedEventAdapter extends RecyclerView.Adapter<JoinedEventHolder>{

    private List<Event> events;


    public JoinedEventAdapter(List<Event> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public JoinedEventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowJoinedEventBinding binding = RowJoinedEventBinding.inflate(LayoutInflater.from(parent.getContext()));

        return new JoinedEventHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull JoinedEventHolder holder, int position) {
        Event event = events.get(position);
        holder.bind(event);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
