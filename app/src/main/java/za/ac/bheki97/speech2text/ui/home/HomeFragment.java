package za.ac.bheki97.speech2text.ui.home;

import android.Manifest;
import android.graphics.drawable.Icon;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.ByteString;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import za.ac.bheki97.speech2text.HomeActivity;
import za.ac.bheki97.speech2text.MainActivity;
import za.ac.bheki97.speech2text.R;
import za.ac.bheki97.speech2text.databinding.FragmentHomeBinding;
import za.ac.bheki97.speech2text.model.retrofit.RetrofitService;
import za.ac.bheki97.speech2text.model.retrofit.UserApi;
import za.ac.bheki97.speech2text.service.GoogleService;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RetrofitService retrofitService;
    private UserApi api;

    private String transLang;
    private String originLang ="English";


    private MediaRecorder mediaRecorder;
    private Handler mainHandler;
    private boolean isRecording;
    private String recordedFileName;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        retrofitService = new RetrofitService();
        api = retrofitService.getRetrofit().create(UserApi.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mainHandler = new Handler();
        isRecording = false;
        mediaRecorder = new MediaRecorder();


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.languages, android.R.layout.simple_spinner_item);

        binding.translationSelectLang.setAdapter(adapter);
        binding.originSelectLang.setAdapter(adapter);

        setOnclickListenerForOriginSelectLang();
        setRecordBtnClickListener();

//
//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void setRecordBtnClickListener() {
        binding.recordBtn.setOnClickListener(view ->{
            try{
                if(!isRecording){
                    if(HomeActivity.isAudioRecordingPermissionGranted()){
                        //toggleRecording();
                        System.out.println("recording");
                        startAudioRecording();
                    }else{
                        getActivity().finish();
                    }
                }else{
                    //stopAudioRecording();
                    toggleRecording();


                    System.out.println("stopping recording file: "+recordedFileName);
                    convertSpeechToText();
                }



            }catch(IOException ex){
                ex.printStackTrace();
            }
        });
    }

    private void convertSpeechToText() throws IOException {
        Path path = Paths.get(recordedFileName);
        byte[] data = Files.readAllBytes(path);
        RequestBody audioRequestBody = RequestBody.create(
                MediaType.parse("audio/*"),
                data
        );
        MultipartBody.Part audioPart = MultipartBody.Part.createFormData(
                "audio", // field name
                path.getFileName().toString(),
                audioRequestBody
        );
        String langCode;
        if(originLang.equalsIgnoreCase("Zulu")){
            langCode = "zu-ZA";
        }else if(originLang.equalsIgnoreCase("Sotho")){
            langCode = "st-ZA";
        }else if(originLang.equalsIgnoreCase("Tsonga")){
            langCode = "ts-ZA";
        }else if(originLang.equalsIgnoreCase("English")){
            langCode = "en-US";
        }else{
            langCode = "af-ZA";
        }


        api.trascribeAudio(audioPart,langCode).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                binding.editTextInitial.setText(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getActivity(),"Request Failed",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setOnClickListenerForTranslationSelectLang() {
        binding.translationSelectLang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                transLang = adapterView.getItemAtPosition(position).toString();

            }
        });
    }
    private void setOnclickListenerForOriginSelectLang(){
        binding.originSelectLang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                originLang = adapterView.getItemAtPosition(position).toString();

            }
        });
    }



    private void startAudioRecording() throws IOException {
        toggleRecording();
        String uuid = UUID.randomUUID().toString();
        recordedFileName = getActivity().getFilesDir().getPath() + "/" + uuid + ".mp3";

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setAudioSamplingRate(44100);
        mediaRecorder.setAudioChannels(2);
        mediaRecorder.setAudioEncodingBitRate(320000);
        mediaRecorder.setOutputFile(recordedFileName);
        mediaRecorder.prepare();
        mediaRecorder.start();
    }

    private void stopAudioRecording(){
        toggleRecording();
        mediaRecorder.stop();
        mediaRecorder.reset();
        mediaRecorder.release();

    }

    private void toggleRecording() {
        isRecording = !isRecording;
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (isRecording) {
                    binding.recordBtn.setImageIcon(Icon.createWithResource(getActivity(),R.drawable.ic_stop));
                } else {
                    binding.recordBtn.setImageIcon(Icon.createWithResource(getActivity(),R.drawable.ic_microphone));
                }
            }
        });
    }
}