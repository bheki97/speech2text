package za.ac.bheki97.speech2text.recycler.guest;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import za.ac.bheki97.speech2text.databinding.RowEditGuestBinding;
import za.ac.bheki97.speech2text.model.event.Event;
import za.ac.bheki97.speech2text.recycler.guest.model.Guest;

public class GuestAdapter extends RecyclerView.Adapter<GuestHolder> {

    private List<Guest> guests;
    private Event event;



    public GuestAdapter(Event event){
        this.guests = event.getGuests();
        this.event = event;
    }

    public List<Guest> getGuests() {
        return guests;
    }

    @NonNull
    @Override
    public GuestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowEditGuestBinding binding = RowEditGuestBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);

        return new GuestHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GuestHolder holder, int position) {
        holder.bind(position,this);
    }

    public Event getEvent() {
        return event;
    }

    @Override
    public int getItemCount() {
        return guests.size();
    }
}
