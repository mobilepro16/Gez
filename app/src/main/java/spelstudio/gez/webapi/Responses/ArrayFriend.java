
package spelstudio.gez.webapi.Responses;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ArrayFriend {

    @SerializedName("FriendID")
    private String mFriendID;
    @SerializedName("FriendLastActive")
    private String mFriendLastActive;
    @SerializedName("FriendName")
    private String mFriendName;
    @SerializedName("FriendPicUrl")
    private String mFriendPicUrl;

    public String getFriendID() {
        return mFriendID;
    }

    public void setFriendID(String friendID) {
        mFriendID = friendID;
    }

    public String getFriendLastActive() {
        return mFriendLastActive;
    }

    public void setFriendLastActive(String friendLastActive) {
        mFriendLastActive = friendLastActive;
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

}
