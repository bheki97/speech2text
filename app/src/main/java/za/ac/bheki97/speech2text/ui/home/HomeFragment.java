package za.ac.bheki97.speech2text.ui.home;

import android.content.DialogInterface;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import za.ac.bheki97.speech2text.HomeActivity;
import za.ac.bheki97.speech2text.R;
import za.ac.bheki97.speech2text.databinding.FragmentHomeBinding;
import za.ac.bheki97.speech2text.exception.SpeakTextException;
import za.ac.bheki97.speech2text.exception.TranslationException;
import za.ac.bheki97.speech2text.model.retrofit.RetrofitService;
import za.ac.bheki97.speech2text.model.retrofit.UserApi;
import za.ac.bheki97.speech2text.model.translation.TranslationDto;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RetrofitService retrofitService;
    private UserApi api;
    private byte[] audioData;

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
        setOnClickListenerForTranslationSelectLang();
        setRecordBtnClickListener();


        setTranslateBtnOnclickListener();
        setSpeakBtnOnClickListener();

        //TranslateToAudio();


//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    private void validateSpeakerData() throws SpeakTextException {
        if(binding.tanslatedTextView.getText().toString().isEmpty()){
            throw new SpeakTextException("Please Convert Text before Translating");
        }

        if(transLang==null){
            throw new SpeakTextException("Choose Your Preferred language of translation\n" +
                    "N.B only English are Supported for Audio conversion");
        }
    }

    private void setSpeakBtnOnClickListener() {
        binding.transSpeakerBtn.setOnClickListener(v ->{

            try{

                validateSpeakerData();
                System.out.println(transLang);
                transLang = changeToLangCode(transLang);
                if(transLang.equalsIgnoreCase("af-ZA")||
                        transLang.equalsIgnoreCase("en-US")){

                    getAudioFromServer();

                }else{
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
                    builder.setTitle("Info Message")
                            .setMessage("Sorry to inform you Now that Prita only Support Afrikaans and English." +
                                    "Updates will be made soon. Thank you, Enjoy using the App")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.create().show();
                }


            }catch (SpeakTextException e){
                Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
            }



        });

    }

    public void TranslateToAudio(){
        binding.transSpeakerBtn.setOnClickListener(v ->{
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
            builder.setTitle("Info Message")
                    .setMessage("Sorry to inform you Now that Prita only Support Afrikaans and English." +
                            "Updates will be made soon. Thank you, Enjoy using the App")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.create().show();

        });
    }

    private void getAudioFromServer() {
        api.getSpeechFromText(transLang,binding.tanslatedTextView.getText().toString()).enqueue(
                new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.code()==200){
                        try {
                            speak(response.body().bytes());
                            Toast.makeText(getActivity(),"Listen.....",Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            Toast.makeText(getActivity(),"Bad Response",Toast.LENGTH_SHORT).show();
                        }

                    }else if(response.code()==403){
                        Toast.makeText(getActivity(),"Access Denied",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity(),"Bad Request",Toast.LENGTH_SHORT).show();
                    }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(),"Audio Request Failed",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void speak(byte[] bytes) throws IOException {
        File tempFile = File.createTempFile("audio", "temp");
        tempFile.deleteOnExit();
        System.out.println(Arrays.toString(bytes));
        // Write the byte array to the temporary file
        FileOutputStream fos = new FileOutputStream(tempFile);
        fos.write(bytes);
        fos.close();

        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(tempFile.getAbsolutePath());
        mediaPlayer.prepare();
        mediaPlayer.start();
    }


    private void setTranslateBtnOnclickListener() {
        binding.translateBtn.setOnClickListener(v->{

            try {
                if(isTranslationDataValid()){
                    translate();
                }else{
                    Toast.makeText(getActivity(),"Confirm All data if It's Valid",Toast.LENGTH_LONG).show();
                }
            } catch (TranslationException e) {
                Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
            }

        });
    }

    private String changeToLangCode(String lang){

        if(lang.equalsIgnoreCase("Zulu")){
            return "zu-ZA";
        }else if(lang.equalsIgnoreCase("Sotho")){
            return "st-ZA";
        }else if(lang.equalsIgnoreCase("Tsonga")){
            return "ts-ZA";
        }else if(lang.equalsIgnoreCase("English")){
            return "en-US";
        }else if(lang.equalsIgnoreCase("Afrikaans")){
            return "af-ZA";
        }else{
            return lang;
        }

    }

    private void translate() {

        api.translate(new TranslationDto(binding.editTextInitial.getText().toString(),changeToLangCode(originLang),changeToLangCode(transLang))).enqueue(new Callback<ResponseBody>() {
            @Override public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code()==200){
                    try {
                        if(response.body()!=null){
                            binding.tanslatedTextView.setText(response.body().string());
                        }else{

                            Toast.makeText(getActivity(),"Server Responded With a Null Value",Toast.LENGTH_SHORT).show();

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getActivity(),"Bad Request,Server Error",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity(),"Request Failed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isTranslationDataValid() throws TranslationException {
        //validate Translation Language
        if(transLang==null || transLang.isEmpty()){
            throw new TranslationException("Please Translation Choose Language!!!");
        }

        //validate text from audio
        if(binding.editTextInitial.getText().toString().isEmpty()){
            throw new TranslationException("Record or Type Text you want to Translate");
        }

        //validate original Language
        if(transLang==null || transLang.isEmpty()){
            throw new TranslationException("Please choose the correct Language of the " +
                    "text you wan to translate");
        }


        //check if Origin is the same with the Translation Language
        if(transLang.equalsIgnoreCase(originLang)){
            throw new TranslationException("You cannot Translate to the same Language\n" +
                    "Please choose a Different Language");
        }


        return true;
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
        StringBuilder langCode = new StringBuilder();
        if(originLang.equalsIgnoreCase("Zulu")){
            langCode.append("zu-ZA");
        }else if(originLang.equalsIgnoreCase("Sotho")){
            langCode.append( "st-ZA");
        }else if(originLang.equalsIgnoreCase("Tsonga")){
            langCode.append("ts-ZA");
        }else if(originLang.equalsIgnoreCase("English")){
            langCode.append("en-US");
        }else{
            langCode.append("af-ZA");
        }


        api.trascribeAudio(audioPart,langCode.toString()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    if(response.code()==200){
                        binding.editTextInitial.setText(response.body().string());
                    }else if(response.code()==403){
                        Toast.makeText(getActivity(),"Access Denied",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity(),"Bad Request",Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
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
                System.out.println("Chosen Lang: ");
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
                    binding.recordBtn.setImageIcon(Icon.createWithResource(getActivity(),R.drawable.ic_stop));
                } else {
                    binding.recordBtn.setImageIcon(Icon.createWithResource(getActivity(),R.drawable.ic_microphone));
                }
            }
        });
    }
}