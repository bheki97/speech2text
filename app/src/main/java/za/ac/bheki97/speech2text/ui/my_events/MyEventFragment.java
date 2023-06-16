package za.ac.bheki97.speech2text.ui.my_events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import za.ac.bheki97.speech2text.model.recycler.myevents.MyEventAdapter;

public class MyEventFragment extends Fragment {

    private FragmentMyEventsBinding binding;
    private MyEventAdapter adapter;
    List<Event> events;

    private UserApi retrofitApi;
    private RetrofitService retrofitService;

    @Override
    public void onStart() {
        super.onStart();

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MyEventViewModel myEventViewModel =
                new ViewModelProvider(this).get(MyEventViewModel.class);

        binding = FragmentMyEventsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        events = new ArrayList<>();

        retrofitService = new RetrofitService();
        retrofitApi = retrofitService.getRetrofit().create(UserApi.class);
        adapter = new MyEventAdapter();
        binding.myEventWrapper.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));


//        if(events==null || events.isEmpty()){
//
//            displayNoEvents();
//            System.out.println("There are not events\n\n");
//
//            System.out.println("\n\n");
//        }else{
//
//            displayRecyclerView();
//        }

        updateMyEventList();


        return root;
    }

    private void updateMyEventList() {
        retrofitApi.getAllHostedEvent(HomeActivity.getUserInfo().getUser().getIdNumber()).enqueue(new Callback<Event[]>() {
            @Override
            public void onResponse(Call<Event[]> call, Response<Event[]> response) {
                if(response.code()==200){
                    events = new ArrayList<>(Arrays.asList(response.body()));
                    events.forEach(System.out::println);
                    adapter.setEvents(events);
                    System.out.println("got all events");
                    //adapter.getEvents().forEach(System.out::println);
                    binding.myEventWrapper.setAdapter(adapter);

                    //binding.errMsg.setText("");
                }else{
                    try {
                        System.out.println("Error occurred: "+ response.errorBody().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }


            }

            @Override
            public void onFailure(Call<Event[]> call, Throwable t) {
                System.out.println("Request Failed");

                //binding.errMsg.setText("You currently don't have events");
            }
        });

    }

    private void displayNoEvents() {

       // binding.myEventWrapper.setAdapter(adapter = new MyEventAdapter(new ArrayList<>()));
        //binding.errMsg.setText("You currently Don't Events");

    }

    private void displayRecyclerView() {
       // adapter = new MyEventAdapter(events);
        binding.myEventWrapper.setAdapter(adapter);
        //binding.errMsg.setText("");

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