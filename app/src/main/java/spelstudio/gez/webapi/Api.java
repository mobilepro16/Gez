package spelstudio.gez.webapi;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import spelstudio.gez.webapi.Responses.AddGezResponse;
import spelstudio.gez.webapi.Responses.DelUserResponse;
import spelstudio.gez.webapi.Responses.StatusResponse;
import spelstudio.gez.webapi.Responses.GUIDRequest;
import spelstudio.gez.webapi.Responses.GetCountriesResponse;
import spelstudio.gez.webapi.Responses.GetFriendRsponse;
import spelstudio.gez.webapi.Responses.GetFriendsListResponse;
import spelstudio.gez.webapi.Responses.GetGezListResponse;
import spelstudio.gez.webapi.Responses.GetGezResponse;
import spelstudio.gez.webapi.Responses.GetPromoVidzResponse;
import spelstudio.gez.webapi.Responses.ResetPasswordResponse;
import spelstudio.gez.webapi.Responses.SetGezFriendResponse;
import spelstudio.gez.webapi.Responses.SetUserInfoResponse;
import spelstudio.gez.webapi.Responses.UserInfo;
import spelstudio.gez.webapi.Responses.rankingresponse.GetRankingResponse;

public interface Api {

    @POST("getRanking")
    Call<GetRankingResponse> getRanking(@Body RequestBody request);

    @POST("getFriendsList")
    Call<GetFriendsListResponse> getFriendsList(@Body RequestBody request);

    @POST("addGez")
    Call<AddGezResponse> addGez(@Body RequestBody userId);

    @POST("getUserLogin")
    Call<UserInfo> getUserLogin(@Body RequestBody userId);

    @POST("getGezList")
    Call<GetGezListResponse> getGezList(@Body RequestBody userId);

    @POST("getGez")
    Call<GetGezResponse> getGez(@Body RequestBody request);

    @POST("getGezListArchive")
    Call<GetGezListResponse> getGezListArchive(@Body RequestBody requestBody);

    @POST("getPromoVids")
    Call<GetPromoVidzResponse> getPromoVids(@Body RequestBody userId);

    @POST("getUserInfo")
    Call<UserInfo> getUserInfo(@Body RequestBody userId);

    @POST("SetGezFriend")
    Call<SetGezFriendResponse> setGezFriend(@Body RequestBody request);

    @POST("resetPassword")
    Call<ResetPasswordResponse> resetPassword(@Body RequestBody mBody);

    @POST("delUser")
    Call<DelUserResponse> delUser(@Body RequestBody userId);

    @POST("setUserInfo")
    Call<SetUserInfoResponse> setUserInfo(@Body RequestBody request);

    @POST("updateUserInfo")
    Call<SetUserInfoResponse> updateUserInfo(@Body RequestBody request);

    @POST("getFriend")
    Call<GetFriendRsponse> getFriend(@Body RequestBody request);

    @POST("delFriend")
    Call<StatusResponse> delFriend(@Body RequestBody request);

    @POST("setGezUser")
    Call<StatusResponse> setGezUser(@Body RequestBody request);

    @POST("setFriend")
    Call<StatusResponse> setFriend(@Body RequestBody request);

    @GET("getCountryList")
    Call<GetCountriesResponse> getCountryList();


}
