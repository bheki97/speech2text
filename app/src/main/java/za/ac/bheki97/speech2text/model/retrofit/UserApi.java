package za.ac.bheki97.speech2text.model.retrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import za.ac.bheki97.speech2text.model.user.AuthRequest;
import za.ac.bheki97.speech2text.model.user.AuthUserInfo;
import za.ac.bheki97.speech2text.model.user.User;

public interface UserApi {

    @POST("/new-account")
    Call<User> registerAcc(@Body User user);

    @POST("/authenticate")
    Call<AuthUserInfo> loginUser(@Body AuthRequest request);

}
