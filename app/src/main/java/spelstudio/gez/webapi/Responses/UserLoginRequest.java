package spelstudio.gez.webapi.Responses;

import com.google.gson.annotations.SerializedName;

public class UserLoginRequest {

    @SerializedName("UserEmail")
    public String UserEmail;

    @SerializedName("UserPassword")
    public String UserPassword;
}
