
package spelstudio.gez.webapi.Responses;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class GetFriendsListResponse {

    @SerializedName("arrayFriends")
    private List<ArrayFriend> mArrayFriends;
    @SerializedName("errMsg")
    private String mErrMsg;
    @SerializedName("errNum")
    private String mErrNum;
    @SerializedName("inviteCode")
    private String mInviteCode;

    public List<ArrayFriend> getArrayFriends() {
        return mArrayFriends;
    }

    public void setArrayFriends(List<ArrayFriend> arrayFriends) {
        mArrayFriends = arrayFriends;
    }

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

    public String getInviteCode() {
        return mInviteCode;
    }

    public void setInviteCode(String inviteCode) {
        mInviteCode = inviteCode;
    }

}
