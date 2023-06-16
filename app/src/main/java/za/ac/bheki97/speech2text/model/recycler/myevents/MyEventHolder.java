package za.ac.bheki97.speech2text.model.recycler.myevents;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import za.ac.bheki97.speech2text.AttendEventActivity;
import za.ac.bheki97.speech2text.databinding.DialogImageBinding;
import za.ac.bheki97.speech2text.databinding.RowMyEventBinding;
import za.ac.bheki97.speech2text.model.event.Event;
import za.ac.bheki97.speech2text.model.retrofit.RetrofitService;
import za.ac.bheki97.speech2text.model.retrofit.UserApi;

//This holder is for the view Created/Hosted Events
public class MyEventHolder extends RecyclerView.ViewHolder{

    private RowMyEventBinding binding;
    private UserApi retrofitApi;
    private RetrofitService retrofitService;
    private Event event;
    private MyEventAdapter adapter;
    private int position;


    public MyEventHolder(RowMyEventBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        retrofitService = new RetrofitService();
        retrofitApi = retrofitService.getRetrofit().create(UserApi.class);
    }

    public void bind(Event event,int position,MyEventAdapter adapter){
        this.event = event;
        this.adapter = adapter;
        this.position  = position;
        binding.occasionTxtView.setText(event.getOccasion());
        binding.descriptionTxtView.setText(event.getDescription());
        setOnclickListenerRmvBtn();
        setOnclickListenerInvBtn();
        setOnclickListenerEditBtn();
        event.setDate(event.getDate().substring(0,event.getDate().length()-3));

        binding.getRoot().setOnClickListener( v->{

            if(event.getLocalDateTime().isBefore(LocalDateTime.now())){
                Intent intent = new Intent(binding.getRoot().getContext(),
                        AttendEventActivity.class);
                intent.putExtra("eventKey",event.getEventKey());
                Toast.makeText(binding.getRoot().getContext() ,"Event Started", Toast.LENGTH_SHORT).show();
                binding.getRoot().getContext().startActivity(intent);
            }else{
                Toast.makeText(binding.getRoot().getContext(), "Event has not yet Started", Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void setOnclickListenerEditBtn(){
        binding.edtBtn.setOnClickListener( v ->{
            Intent intent = new Intent(binding.getRoot().getContext(),EditMyEventActivity.class);
            intent.putExtra("event",event);
            binding.getRoot().getContext().startActivity(intent);
        });
    }
    public void setOnclickListenerRmvBtn(){
        binding.rmvBtn.setOnClickListener( v ->{
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(binding.getRoot().getContext());
            builder.setTitle("confirm? ")
                    .setMessage("Are you sure want to delete "+ event.getOccasion()+" occasion? ")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteEventCall();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            builder.create().show();

        });
    }

    private void deleteEventCall() {
        retrofitApi.deleteEvent(event.getEventKey()).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                if(response.code()==200){
                    adapter.CancelEvent(position);
                    Toast.makeText(binding.getRoot().getContext(), "Event Successfully Cancelled", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }

    public void setOnclickListenerInvBtn(){
        binding.invBtn.setOnClickListener( v ->{

            System.out.println("Event Key: "+event.getEventKey());
            MultiFormatWriter writer = new MultiFormatWriter();
            final Dialog dialog = new Dialog(binding.getRoot().getContext());


            DialogImageBinding dialogBinding = DialogImageBinding.inflate(dialog.getLayoutInflater());
            dialog.setContentView(dialogBinding.getRoot());

            try {
                BitMatrix matrix = writer.encode(event.getEventKey()+"##"+event.getEventKey()+"##"+event.getEventKey(), BarcodeFormat.QR_CODE, 500, 500);
                BarcodeEncoder encoder = new BarcodeEncoder();
                Bitmap bitmap = encoder.createBitmap(matrix);




                dialogBinding.imageView.setImageBitmap(bitmap);
                dialogBinding.closeButton.setOnClickListener( vi -> dialog.dismiss());
                dialogBinding.shareButton.setOnClickListener( sh ->{
                        shareImage(bitmap);
                });




                dialog.setCanceledOnTouchOutside(true);

                // Show the dialog
                dialog.show();

            } catch (WriterException e) {
                e.printStackTrace();
            }
        });
    }


    private void shareImage(Bitmap bitmap){
        Uri imageUri = getImageUri(bitmap);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);

        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Shared Image");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this image I created!");

        // Start the share activity
        binding.getRoot().getContext().startActivity(Intent.createChooser(shareIntent, "Share Image"));
    }


    private Uri getImageUri(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(binding.getRoot().getContext().getContentResolver(), bitmap, "Shared Image", null);
        return Uri.parse(path);
    }

}
