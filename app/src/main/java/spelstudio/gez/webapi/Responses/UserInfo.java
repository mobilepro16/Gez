package spelstudio.gez.webapi.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInfo {

    @SerializedName("errNum")
    @Expose
    public String errNum;
    @SerializedName("errMsg")
    @Expose
    public String errMsg;
    @SerializedName("UserGuid")
    @Expose
    public String userGuid;
    @SerializedName("UserName")
    @Expose
    public String userName;
    @SerializedName("UserPicUrl")
    @Expose
    public String userPicUrl;
    @SerializedName("UserBirthDate")
    @Expose
    public String userBirthDate;
    @SerializedName("UserCountry")
    @Expose
    public String userCountry;
    @SerializedName("OpenGez")
    @Expose
    public String openGez;
    @SerializedName("OpenFriends")
    @Expose
    public String openFriends;

}
