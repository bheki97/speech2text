package za.ac.bheki97.speech2text.model.recycler.speakers;



import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import za.ac.bheki97.speech2text.HomeActivity;
import za.ac.bheki97.speech2text.databinding.RowSpeakerBinding;
import za.ac.bheki97.speech2text.exception.SpeakTextException;
import za.ac.bheki97.speech2text.model.recycler.guest.model.Speaker;
import za.ac.bheki97.speech2text.model.retrofit.RetrofitService;
import za.ac.bheki97.speech2text.model.retrofit.UserApi;

//Partake into the Event
public class SpeakerHolder extends RecyclerView.ViewHolder {

    private RowSpeakerBinding binding;
    private UserApi retrofitApi;
    private RetrofitService retrofitService;
    private Speaker speaker;
    SpeakerAdapter adapter;


    public SpeakerHolder(RowSpeakerBinding binding) {

        super(binding.getRoot());
        this.binding= binding;
        setSpeakBtnOnClickListener();

        retrofitService = new RetrofitService();
        retrofitApi = retrofitService.getRetrofit().create(UserApi.class);

    }

    public void bind(SpeakerAdapter adapter,int position){
        this.adapter = adapter;
        speaker = adapter.getSpeakers().get(position);
        String names = speaker.getAccount().getFirstname()+" "+speaker.getAccount().getLastname();
        binding.txtSpeakername.setText(names);
        binding.txtspeech.setText(speaker.getSpeech());


    }

    private void setSpeakBtnOnClickListener() {
        binding.btnLeave.setOnClickListener(v ->{

        String transLang = HomeActivity.getUserInfo().getUser().getLanguage();
        if(transLang.equalsIgnoreCase("af-ZA")||
                transLang.equalsIgnoreCase("en-US")){

            getAudioFromServer(transLang,speaker.getSpeech());

        }else{
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(binding.getRoot().getContext());
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



        });

    }


    private void getAudioFromServer(String transLang,String msg) {
        retrofitApi.getSpeechFromText(transLang,msg).enqueue(
                new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.code()==200){
                            try {
                                speak(response.body().bytes());
                                Toast.makeText(binding.getRoot().getContext(),"Listen.....",Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                Toast.makeText(binding.getRoot().getContext(),"Bad Response",Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            try {
                                Toast.makeText(binding.getRoot().getContext(),response.errorBody().string(),Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(binding.getRoot().getContext(),"Audio Request Failed",Toast.LENGTH_SHORT).show();
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

}
