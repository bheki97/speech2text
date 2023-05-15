package za.ac.bheki97.speech2text.recycler.guest;


import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import za.ac.bheki97.speech2text.databinding.RowEditGuestBinding;
import za.ac.bheki97.speech2text.recycler.guest.model.Guest;

//this guest holder is for the Edit Event
public class GuestHolder extends RecyclerView.ViewHolder{

    private RowEditGuestBinding binding;

    public GuestHolder(RowEditGuestBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Guest guest){

    }
}
