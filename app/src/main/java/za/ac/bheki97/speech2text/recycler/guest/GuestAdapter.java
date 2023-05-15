package za.ac.bheki97.speech2text.recycler.guest;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GuestAdapter extends RecyclerView.Adapter<GuestHolder> {



    @NonNull
    @Override
    public GuestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GuestHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull GuestHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
