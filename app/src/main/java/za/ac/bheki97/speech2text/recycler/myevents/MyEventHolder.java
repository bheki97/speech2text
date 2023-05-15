package za.ac.bheki97.speech2text.recycler.myevents;


import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import za.ac.bheki97.speech2text.databinding.RowJoinedEventBinding;
import za.ac.bheki97.speech2text.databinding.RowMyEventBinding;
import za.ac.bheki97.speech2text.model.event.Event;

//This holder is for the view Created/Hosted Events
public class MyEventHolder extends RecyclerView.ViewHolder{

    private RowMyEventBinding binding;



    public MyEventHolder(RowMyEventBinding binding) {
        super(binding.getRoot());
    }

    public void bind(Event event){

    }

}
