package za.ac.bheki97.speech2text.model.retrofit;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import za.ac.bheki97.speech2text.model.JoinEventDto;
import za.ac.bheki97.speech2text.model.MakeSpeakerDto;
import za.ac.bheki97.speech2text.model.MessageDto;
import za.ac.bheki97.speech2text.model.event.Event;
import za.ac.bheki97.speech2text.model.event.GuestEvent;
import za.ac.bheki97.speech2text.model.recycler.guest.model.Speaker;
import za.ac.bheki97.speech2text.model.translation.TranslationDto;
import za.ac.bheki97.speech2text.model.user.AuthRequest;
import za.ac.bheki97.speech2text.model.user.AuthUserInfo;
import za.ac.bheki97.speech2text.model.user.User;
import za.ac.bheki97.speech2text.ui.join_group.JoinEvent;

public interface UserApi {

    @POST("/account")
    Call<User> registerAcc(@Body User user);

    @PUT("/account/update")
    Call<AuthUserInfo> updateProfile(@Header("Authorization") String jwtToken, @Body User user);

    @DELETE("/account/{id}")
    Call<MessageDto> deleteAcc(@Header("Authorization") String jwtToken, @Path("id")String id);

    @POST("/authenticate")
    Call<AuthUserInfo> loginUser(@Body AuthRequest request);

    @POST("/speech/translate")
    Call<ResponseBody> translate(@Body TranslationDto dto);

    @GET("/speech/{lang}/{text}")
    Call<ResponseBody> getSpeechFromText(@Path("lang") String lang,@Path("text")String text);

    @Multipart
    @POST("/speech/transcribe")
    Call<ResponseBody> trascribeAudio(@Part MultipartBody.Part file, @Part("language") String language);

    @Multipart
    @POST("/speech/event")
    Call<ResponseBody> trascribeSpeakerSpeech(@Part MultipartBody.Part file
            ,@Part("languageOri") String languageOri, @Part("languageOri") String languageTrans);

    @POST("/event")
    Call<ResponseBody> hostEvent(@Header("Authorization")String jwtToken,@Body Event event);

    @POST("/event/host")
    Call<Event[]> getAllHostedEvent(@Body String id);

    @DELETE("/event/{id}")
    Call<Boolean> deleteEvent(@Path("id") String id);

    @Multipart
    @PUT("/event/{guestId}/{language}")
    Call<ResponseBody> addSpeech(@Part MultipartBody.Part file,
                                 @Path("guestId")Integer guestId,@Path("language")String language);

    @POST("/event/joined")
    Call<GuestEvent[]> getAllJoinedEvents(@Body String id);

    @PUT("/event")
    Call<Boolean> updateHostedEvent(@Body Event event);

    @POST("/event/change-title")
    Call<Boolean> changeGuestTitle(@Body MakeSpeakerDto dto);

    @DELETE("/event/leave/{eventKey}/{accId}")
    Call<Boolean> leaveJoinedEvent(@Path("eventKey")String eventKey,@Path("accId")String accId);

    @POST("/event/join")
    Call<Boolean> joinEvent(@Body JoinEventDto joinEventDto);

    @GET("/event/{eventKey}/speaker")
    Call<List<Speaker>> getAllSpeakersOfTheEvent(@Path("eventKey") String eventKey);

    @GET("/event/{eventKey}/speaker/{language}")
    Call<List<Speaker>> getAllSpeakersOfTheEventFor(@Path("eventKey") String eventKey,
            @Path("language")String language);

}
