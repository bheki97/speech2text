package za.ac.bheki97.speech2text.recycler.guest;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import za.ac.bheki97.speech2text.databinding.RowEditGuestBinding;
import za.ac.bheki97.speech2text.recycler.guest.model.Guest;

public class GuestAdapter extends RecyclerView.Adapter<GuestHolder> {

    List<Guest> guests;


    public GuestAdapter(List<Guest> guests){
        this.guests = guests;

    }

    @NonNull
    @Override
    public GuestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowEditGuestBinding binding = RowEditGuestBinding.inflate(LayoutInflater.from(parent.getContext()));

        return new GuestHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GuestHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return guests.size();
    }
}
