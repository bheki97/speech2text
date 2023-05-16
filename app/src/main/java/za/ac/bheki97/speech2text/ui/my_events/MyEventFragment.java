package za.ac.bheki97.speech2text.ui.my_events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.io.IOException;
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
    private MyEventAdapter adapter;
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

        updateMyEventList();


        if(events==null || events.isEmpty()){
            displayNoEvents();
        }else{

            displayRecyclerView();
        }


        return root;
    }

    private void updateMyEventList() {
        retrofitApi.getAllHostedEvent(HomeActivity.getUserInfo().getUser().getIdNumber()).enqueue(new Callback<Event[]>() {
            @Override
            public void onResponse(Call<Event[]> call, Response<Event[]> response) {
                if(response.code()==200){
                    events = new ArrayList<>(Arrays.asList(response.body()));

                    System.out.println("I got All the Event ");

                    events.forEach(System.out::println);
                }else{
                    try {
                        System.out.println("Error occurred: "+ response.errorBody().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    events = new ArrayList<>();
                }
            }

            @Override
            public void onFailure(Call<Event[]> call, Throwable t) {
                System.out.println("Request Failed");
                events = new ArrayList<>();
            }
        });

    }

    private void displayNoEvents() {

    }

    private void displayRecyclerView() {
        adapter = new MyEventAdapter(events);


    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}