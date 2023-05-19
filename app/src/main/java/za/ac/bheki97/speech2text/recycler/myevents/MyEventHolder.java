package za.ac.bheki97.speech2text.recycler.myevents;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import za.ac.bheki97.speech2text.R;
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


            dialog.setContentView(R.layout.dialog_image);

            ImageView imageView = dialog.findViewById(R.id.image_view);


            try {
                BitMatrix matrix = writer.encode(event.getEventKey()+"##"+event.getEventKey()+"##"+event.getEventKey(), BarcodeFormat.QR_CODE, 500, 500);
                BarcodeEncoder encoder = new BarcodeEncoder();
                Bitmap bitmap = encoder.createBitmap(matrix);




                imageView.setImageBitmap(bitmap);

                Button closeButton = dialog.findViewById(R.id.close_button);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });



                dialog.setCanceledOnTouchOutside(true);

                // Show the dialog
                dialog.show();

            } catch (WriterException e) {
                e.printStackTrace();
            }
        });
    }

}
