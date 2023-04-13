package za.ac.bheki97.speech2text.service;

import java.util.ArrayList;
import java.util.List;

import za.ac.bheki97.speech2text.model.retrofit.RetrofitService;
import za.ac.bheki97.speech2text.model.retrofit.UserApi;

public class GoogleService {

    private RetrofitService retrofitService;
    private UserApi api;
    private final ArrayList<String> supportedLanguages;

    public GoogleService(ArrayList<String> supportedLanguages) {
        this.supportedLanguages = supportedLanguages;

        retrofitService = new RetrofitService();
        api = retrofitService.getRetrofit().create(UserApi.class);
    }

    public String transcribeAudio(String fileUri,String language){


        return null;
    }


}
