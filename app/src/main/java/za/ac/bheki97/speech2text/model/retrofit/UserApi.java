package za.ac.bheki97.speech2text.model.retrofit;

import kotlin.ParameterName;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import za.ac.bheki97.speech2text.model.user.AuthRequest;
import za.ac.bheki97.speech2text.model.user.AuthUserInfo;
import za.ac.bheki97.speech2text.model.user.User;

public interface UserApi {

    @POST("/account")
    Call<User> registerAcc(@Body User user);

    @PUT("/account/update")
    Call<User> updateProfile(@Header("Authorization") String jwtToken, @Body User user);

    @DELETE("/account/{id}")
    Call<String> deleteAcc(@Header("Authorization") String jwtToken, @Path("id")String id);

    @POST("/authenticate")
    Call<AuthUserInfo> loginUser(@Body AuthRequest request);

    @Multipart
    @POST("/speech/transcribe")
    Call<String> trascribeAudio(@Part MultipartBody.Part file,@Part("language") String language);


}
