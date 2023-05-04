package za.ac.bheki97.speech2text.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import za.ac.bheki97.speech2text.databinding.FragmentJoinedEventsBinding;

public class JoinedEventsFragment extends Fragment {

    private FragmentJoinedEventsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        JoinedEventsViewModel joinedEventsViewModel =
                new ViewModelProvider(this).get(JoinedEventsViewModel.class);

        binding = FragmentJoinedEventsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSlideshow;
        joinedEventsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}