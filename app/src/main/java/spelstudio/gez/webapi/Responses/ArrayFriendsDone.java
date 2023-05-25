
package spelstudio.gez.webapi.Responses;

import com.google.gson.annotations.SerializedName;

public class ArrayFriendsDone {

    @SerializedName("FriendID")
    private String mFriendID;
    @SerializedName("FriendPicUrl")
    private String mFriendPicUrl;
    @SerializedName("FriendSelected")
    private String mFriendSelected;
    @SerializedName("UserPoint")
    private String mUserPoint;
    @SerializedName("UserSelected")
    private String mUserSelected;
    public int mUserWonStatus = -1;

    public String getFriendID() {
        return mFriendID;
    }

    public void setFriendID(String friendID) {
        mFriendID = friendID;
    }

    public String getFriendPicUrl() {
        return mFriendPicUrl;
    }

    public void setFriendPicUrl(String friendPicUrl) {
        mFriendPicUrl = friendPicUrl;
    }

    public int getFriendSelected() {
        try {
            return Integer.parseInt(mFriendSelected) - 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public int getUserSelected() {
        try {
            return Integer.parseInt(mUserSelected) - 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public void setFriendSelected(String friendSelected) {
        mFriendSelected = friendSelected;
    }

    public String getUserPoint() {
        return mUserPoint;
    }

    public void setUserPoint(String userPoint) {
        mUserPoint = userPoint;
    }

    public void setUserSelected(String userSelected) {
        mUserSelected = userSelected;
    }

}
