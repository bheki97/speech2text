package za.ac.bheki97.speech2text.ui.my_events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import za.ac.bheki97.speech2text.HomeActivity;
import za.ac.bheki97.speech2text.databinding.FragmentMyEventsBinding;
import za.ac.bheki97.speech2text.model.event.Event;
import za.ac.bheki97.speech2text.model.retrofit.RetrofitService;
import za.ac.bheki97.speech2text.model.retrofit.UserApi;
import za.ac.bheki97.speech2text.recycler.myevents.MyEventAdapter;

public class MyEventFragment extends Fragment {

    private FragmentMyEventsBinding binding;
    List<Event> events;

    private UserApi retrofitApi;
    private RetrofitService retrofitService;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MyEventViewModel myEventViewModel =
                new ViewModelProvider(this).get(MyEventViewModel.class);

        binding = FragmentMyEventsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        retrofitService = new RetrofitService();
        retrofitApi = retrofitService.getRetrofit().create(UserApi.class);



        retrofitApi.getAllHostedEvent(HomeActivity.getUserInfo().getUser().getIdNumber()).enqueue(new Callback<Event[]>() {
            @Override
            public void onResponse(Call<Event[]> call, Response<Event[]> response) {
                if(response.code()==200){
                    events = new ArrayList<>(Arrays.asList(response.body()));
                }else{
                    events = new ArrayList<>();
                }
            }

            @Override
            public void onFailure(Call<Event[]> call, Throwable t) {
                events = new ArrayList<>();
            }
        });

        if(events==null || events.isEmpty()){
            displayNoEvents();
        }else{

            displayRecyclerView();
        }


        return root;
    }

    private void displayNoEvents() {

    }

    private void displayRecyclerView() {
        MyEventAdapter adapter = new MyEventAdapter(events);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}