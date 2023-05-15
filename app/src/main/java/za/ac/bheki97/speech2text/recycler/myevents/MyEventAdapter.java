package za.ac.bheki97.speech2text.recycler.myevents;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import za.ac.bheki97.speech2text.databinding.RowMyEventBinding;
import za.ac.bheki97.speech2text.model.event.Event;

public class MyEventAdapter extends RecyclerView.Adapter<MyEventHolder>{

    private List<Event> events;

    public MyEventAdapter(List<Event> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public MyEventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowMyEventBinding binding = RowMyEventBinding.inflate(LayoutInflater.from(parent.getContext()));

        return new MyEventHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyEventHolder holder, int position) {
        Event event = events.get(position);
        holder.bind(event);
    }





    @Override
    public int getItemCount() {
        return events.size();
    }
}
