package za.ac.bheki97.speech2text.model.recycler.joinedevents;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import za.ac.bheki97.speech2text.databinding.RowJoinedEventBinding;
import za.ac.bheki97.speech2text.model.event.GuestEvent;

public class JoinedEventAdapter extends RecyclerView.Adapter<JoinedEventHolder>{

    private List<GuestEvent> events;


    public JoinedEventAdapter(List<GuestEvent> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public JoinedEventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowJoinedEventBinding binding = RowJoinedEventBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);

        return new JoinedEventHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull JoinedEventHolder holder, int position) {
        GuestEvent event = events.get(position);
        holder.bind(position,this);
    }

    public GuestEvent getEventAtPos(int position){
        return events.get(position);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }


    public void removeItem(int position) {
        events.remove(position);
       notifyItemRemoved(position);
    }
}
