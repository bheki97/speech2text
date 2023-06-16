package za.ac.bheki97.speech2text;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.graphics.drawable.Icon;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import za.ac.bheki97.speech2text.databinding.ActivityAttendEventBinding;
import za.ac.bheki97.speech2text.model.recycler.guest.model.Speaker;
import za.ac.bheki97.speech2text.model.recycler.speakers.SpeakerAdapter;
import za.ac.bheki97.speech2text.model.retrofit.RetrofitService;
import za.ac.bheki97.speech2text.model.retrofit.UserApi;
import za.ac.bheki97.speech2text.thread.FetchSpeakers;
import za.ac.bheki97.speech2text.thread.FetchSpeakersCallback;

public class AttendEventActivity extends AppCompatActivity {

    private ActivityAttendEventBinding binding;
    boolean isSpeaker = false;
    private Speaker speaker;

    private String eventKey;
   // private int id;
    private UserApi retrofitApi;
    private RetrofitService retrofitService;
    private SpeakerAdapter adapter;


    private MediaRecorder mediaRecorder;
    private Handler mainHandler;
    private boolean isRecording;
    private String recordedFileName;
    FetchSpeakers fetch;


    private FetchSpeakersCallback callback = () ->{
      runOnUiThread( ()->{
          updateTheSpeaker();
      });
    };

    private void updateTheSpeaker() {
        addSpeakers();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setup view binding
        binding = ActivityAttendEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //id = getIntent().getIntExtra("id",-1);
        eventKey = getIntent().getStringExtra("eventKey");

        if(eventKey==null || eventKey.isEmpty()){
            finish();
            Toast.makeText(this, "Failed to Event Key", Toast.LENGTH_SHORT).show();
            return;
        }
        mainHandler = new Handler();
        isRecording = false;
        mediaRecorder = new MediaRecorder();

        fetch = new FetchSpeakers(callback);

//        if(id==-1){
//            finish();
//            Toast.makeText(this, "Failed to Get Gust/Host Id", Toast.LENGTH_SHORT).show();
//            return;
//        }


        retrofitService = new RetrofitService();
        retrofitApi = retrofitService.getRetrofit().create(UserApi.class);

        binding.speakerRecycler.setLayoutManager(new LinearLayoutManager(this));


        setRecordBtnClickListener();



    }



    private void startAudioRecording() throws IOException {
        toggleRecording();
        String uuid = UUID.randomUUID().toString();
        recordedFileName = this.getFilesDir().getPath() + "/" + uuid + ".mp3";

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioSamplingRate(44100);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setAudioEncodingBitRate(192000);
        mediaRecorder.setOutputFile(recordedFileName);
        mediaRecorder.prepare();
        mediaRecorder.start();
    }

    private void stopAudioRecording() throws IOException {
        toggleRecording();
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;


    }

    private void toggleRecording() {
        isRecording = !isRecording;
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (isRecording) {
                    binding.recordBtn.setImageIcon(Icon.createWithResource(AttendEventActivity.this,R.drawable.ic_stop));
                } else {
                    binding.recordBtn.setImageIcon(Icon.createWithResource(AttendEventActivity.this,R.drawable.ic_microphone));
                }
            }
        });
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
                        this.finish();
                    }
                }else{
                    stopAudioRecording();


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



        retrofitApi.addSpeech(audioPart,speaker.getGuestId(),
                HomeActivity.getUserInfo().getUser().getLanguage()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    if(response.code()==200){
                        binding.editTextInitial.setText(response.body().string());
                    }else if(response.code()==403){
                        Toast.makeText(AttendEventActivity.this,"Access Denied",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(AttendEventActivity.this,"Bad Request",Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(AttendEventActivity.this,"Request Failed",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        if(fetch!=null){
            fetch.stopCall();
            fetch=null;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        fetch.start();

    }

    @Override
    public void onPause(){
        super.onPause();

        if(fetch!=null){
            fetch.stopCall();
            fetch=null;
        }
    }



    private void addSpeakers() {
        retrofitApi.getAllSpeakersOfTheEventFor(eventKey,HomeActivity.getUserInfo().getUser().getLanguage()).enqueue(new Callback<List<Speaker>>() {
            @Override
            public void onResponse(Call<List<Speaker>> call, Response<List<Speaker>> response) {

                if(response.code()==200){
                    setupAdapter(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Speaker>> call, Throwable t) {

            }
        });

    }

    private void setupAdapter(List<Speaker> speakers) {
        adapter =  new SpeakerAdapter(speakers);
        isSpeaker = false;
        int cnt=0;
        while(!isSpeaker && cnt< speakers.size()){
            isSpeaker = (speakers.get(cnt).getAccount().getIdNumber()
                    .equals(HomeActivity.getUserInfo().getUser().getIdNumber()));
            if(isSpeaker){
                speaker = speakers.get(cnt);
                speakers.remove(cnt);
            }
            cnt++;

        }

        if(isSpeaker){
            binding.topPanel.setVisibility(View.VISIBLE);
        }else{
            binding.topPanel.setVisibility(View.GONE);
        }

        binding.speakerRecycler.setAdapter(adapter);

    }
}