package za.ac.bheki97.speech2text.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import za.ac.bheki97.speech2text.HomeActivity;
import za.ac.bheki97.speech2text.databinding.FragmentJoinedEventsBinding;
import za.ac.bheki97.speech2text.model.event.GuestEvent;
import za.ac.bheki97.speech2text.model.retrofit.RetrofitService;
import za.ac.bheki97.speech2text.model.retrofit.UserApi;
import za.ac.bheki97.speech2text.recycler.joinedevents.JoinedEventAdapter;

public class JoinedEventsFragment extends Fragment {

    private FragmentJoinedEventsBinding binding;
    private UserApi retrofitApi;
    private RetrofitService retrofitService;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        JoinedEventsViewModel joinedEventsViewModel =
                new ViewModelProvider(this).get(JoinedEventsViewModel.class);

        binding = FragmentJoinedEventsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        retrofitService = new RetrofitService();
        retrofitApi = retrofitService.getRetrofit().create(UserApi.class);

        binding.recycler.setLayoutManager(new LinearLayoutManager(getContext()));



        return root;
    }

    private void loadEvents(){
        getEventFromServer();

    }
    private void populateEventsToAdapter(List<GuestEvent> events){
        JoinedEventAdapter adapter = new JoinedEventAdapter(events);
        binding.recycler.setAdapter(adapter);
    }

    private void getEventFromServer() {
        retrofitApi.getAllJoinedEvents(HomeActivity.getUserInfo().getUser().getIdNumber())
                .enqueue(new Callback<GuestEvent[]>() {
                    @Override
                    public void onResponse(Call<GuestEvent[]> call, Response<GuestEvent[]> response) {
                        if(response.code()==200 && response.body()!=null){
                            populateEventsToAdapter(new ArrayList<>(Arrays.asList(response.body())));
                            Toast.makeText(getContext(), "You Have Joined: "
                                    +response.body().length+" events", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            System.out.println(response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        Toast.makeText(getContext(), "Could not get your Events", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Call<GuestEvent[]> call, Throwable t) {
                        Toast.makeText(getContext(), "Cannot reach server", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void onResume() {
        super.onResume();
        loadEvents();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}