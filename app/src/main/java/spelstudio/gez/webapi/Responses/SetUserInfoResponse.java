
package spelstudio.gez.webapi.Responses;

import com.google.gson.annotations.SerializedName;

public class SetUserInfoResponse {

    @SerializedName("errMsg")
    private String mErrMsg;
    @SerializedName("errNum")
    private String mErrNum;
    @SerializedName("OpenFriends")
    private String mOpenFriends;
    @SerializedName("OpenGez")
    private String mOpenGez;
    @SerializedName("UserBirthDate")
    private String mUserBirthDate;
    @SerializedName("UserCountry")
    private String mUserCountry;
    @SerializedName("UserGuid")
    private String mUserGuid;
    @SerializedName("UserName")
    private String mUserName;
    @SerializedName("UserPicUrl")
    private String mUserPicUrl;

    public String getErrMsg() {
        return mErrMsg;
    }

    public void setErrMsg(String errMsg) {
        mErrMsg = errMsg;
    }

    public String getErrNum() {
        return mErrNum;
    }

    public void setErrNum(String errNum) {
        mErrNum = errNum;
    }

    public String getOpenFriends() {
        return mOpenFriends;
    }

    public void setOpenFriends(String openFriends) {
        mOpenFriends = openFriends;
    }

    public String getOpenGez() {
        return mOpenGez;
    }

    public void setOpenGez(String openGez) {
        mOpenGez = openGez;
    }

    public String getUserBirthDate() {
        return mUserBirthDate;
    }

    public void setUserBirthDate(String userBirthDate) {
        mUserBirthDate = userBirthDate;
    }

    public String getUserCountry() {
        return mUserCountry;
    }

    public void setUserCountry(String userCountry) {
        mUserCountry = userCountry;
    }

    public String getUserGuid() {
        return mUserGuid;
    }

    public void setUserGuid(String userGuid) {
        mUserGuid = userGuid;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getUserPicUrl() {
        return mUserPicUrl;
    }

    public void setUserPicUrl(String userPicUrl) {
        mUserPicUrl = userPicUrl;
    }

}
