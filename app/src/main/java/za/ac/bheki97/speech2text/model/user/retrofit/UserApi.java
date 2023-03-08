package za.ac.bheki97.speech2text.model.user.retrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import za.ac.bheki97.speech2text.model.user.User;

public interface UserApi {

    @POST
    Call<User> registerAcc(@Body User user);
}
