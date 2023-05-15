package za.ac.bheki97.speech2text.recycler.joinedevents;


import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import za.ac.bheki97.speech2text.databinding.RowJoinedEventBinding;
import za.ac.bheki97.speech2text.model.event.Event;

//this holder is for the view Joined Events
public class JoinedEventHolder extends RecyclerView.ViewHolder{

    private RowJoinedEventBinding binding;
    public JoinedEventHolder(RowJoinedEventBinding binding) {

        super(binding.getRoot());
    }

    public void bind(Event event){

    }
}
