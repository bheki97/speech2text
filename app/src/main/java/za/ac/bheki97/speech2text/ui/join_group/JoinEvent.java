package za.ac.bheki97.speech2text.ui.join_group;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import za.ac.bheki97.speech2text.R;
import za.ac.bheki97.speech2text.databinding.FragmentJoinEventBinding;


public class JoinEvent extends Fragment {

    private FragmentJoinEventBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_join_event, container, false);
    }
}