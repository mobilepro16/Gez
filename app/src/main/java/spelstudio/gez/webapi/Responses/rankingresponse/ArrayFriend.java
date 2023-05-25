
package spelstudio.gez.webapi.Responses.rankingresponse;

import com.google.gson.annotations.SerializedName;

public class ArrayFriend {

    @SerializedName("FriendID")
    private String mFriendID;
    @SerializedName("FriendName")
    private String mFriendName;
    @SerializedName("FriendPicUrl")
    private String mFriendPicUrl;
    @SerializedName("FriendUserGezOpen")
    private String mFriendUserGezOpen;
    @SerializedName("FriendUserGood")
    private String mFriendUserGood;
    @SerializedName("FriendUserPercentage")
    private String mFriendUserPercentage;
    @SerializedName("UserFriendGezOpen")
    private String mUserFriendGezOpen;
    @SerializedName("UserFriendGood")
    private String mUserFriendGood;
    @SerializedName("UserFriendPercentage")
    private String mUserFriendPercentage;
    @SerializedName("TotalGez")
    private String totalGez;

    public int getTotalGez() {
        try {
            return Integer.parseInt(totalGez);
        } catch (NumberFormatException e) {

        }
        return 0;
    }

    public String getFriendID() {
        return mFriendID;
    }

    public void setFriendID(String friendID) {
        mFriendID = friendID;
    }

    public String getFriendName() {
        return mFriendName;
    }

    public void setFriendName(String friendName) {
        mFriendName = friendName;
    }

    public String getFriendPicUrl() {
        return mFriendPicUrl;
    }

    public void setFriendPicUrl(String friendPicUrl) {
        mFriendPicUrl = friendPicUrl;
    }

    public String getFriendUserGezOpen() {
        return mFriendUserGezOpen;
    }

    public void setFriendUserGezOpen(String friendUserGezOpen) {
        mFriendUserGezOpen = friendUserGezOpen;
    }

    public String getFriendUserGood() {
        return mFriendUserGood;
    }

    public void setFriendUserGood(String friendUserGood) {
        mFriendUserGood = friendUserGood;
    }

    public String getFriendUserPercentage() {
        return mFriendUserPercentage.replace(",", ".");
    }

    public void setFriendUserPercentage(String friendUserPercentage) {
        mFriendUserPercentage = friendUserPercentage;
    }

    public String getUserFriendGezOpen() {
        return mUserFriendGezOpen;
    }

    public void setUserFriendGezOpen(String userFriendGezOpen) {
        mUserFriendGezOpen = userFriendGezOpen;
    }

    public String getUserFriendGood() {
        return mUserFriendGood;
    }

    public void setUserFriendGood(String userFriendGood) {
        mUserFriendGood = userFriendGood;
    }

    public String getUserFriendPercentage() {
        return mUserFriendPercentage.replace(",", ".");
    }

    public void setUserFriendPercentage(String userFriendPercentage) {
        mUserFriendPercentage = userFriendPercentage;
    }

}
