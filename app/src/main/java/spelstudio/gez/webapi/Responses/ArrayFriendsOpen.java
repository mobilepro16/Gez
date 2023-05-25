
package spelstudio.gez.webapi.Responses;

import com.google.gson.annotations.SerializedName;

public class ArrayFriendsOpen {

    @SerializedName("FriendID")
    private String mFriendID;
    @SerializedName("FriendPicUrl")
    private String mFriendPicUrl;
    @SerializedName("UserSelected")
    private Integer mUserSelected;

    public String getFriendID() {
        return mFriendID;
    }

    public String getFriendPicUrl() {
        return mFriendPicUrl;
    }

    public Integer getUserSelected() {
        return mUserSelected;
    }

    public void setUserSelected(int selected) {
        mUserSelected = selected;
    }


}
