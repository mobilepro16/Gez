
package spelstudio.gez.webapi.Responses;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class GetGezResponse {

    @SerializedName("ArrayFriendsDone")
    private List<ArrayFriendsDone> mArrayFriendsDone;
    @SerializedName("ArrayFriendsOpen")
    private List<ArrayFriendsOpen> mArrayFriendsOpen;
    @SerializedName("errMsg")
    private String mErrMsg;
    @SerializedName("errNum")
    private String mErrNum;
    @SerializedName("GezAddedBy")
    private String mGezAddedBy;
    @SerializedName("GezID")
    private String mGezID;
    @SerializedName("GezPlayed")
    private String mGezPlayed;
    @SerializedName("GezSelected")
    private String mGezSelected;
    @SerializedName("TotalFriendsDone")
    private String mTotalFriendsDone;
    @SerializedName("TotalFriendsDoneRated")
    private String mTotalFriendsDoneRated;
    @SerializedName("TotalFriendsOpen")
    private String mTotalFriendsOpen;
    @SerializedName("TotalFriendsOpenRated")
    private String mTotalFriendsOpenRated;
    @SerializedName("VideoName")
    private String mVideoName;
    @SerializedName("VideoUrl")
    private String mVideoUrl;

    public List<ArrayFriendsDone> getArrayFriendsDone() {
        return mArrayFriendsDone;
    }

    public void setArrayFriendsDone(List<ArrayFriendsDone> arrayFriendsDone) {
        mArrayFriendsDone = arrayFriendsDone;
    }

    public List<ArrayFriendsOpen> getArrayFriendsOpen() {
        return mArrayFriendsOpen;
    }

    public void setArrayFriendsOpen(List<ArrayFriendsOpen> arrayFriendsOpen) {
        mArrayFriendsOpen = arrayFriendsOpen;
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

    public String getGezAddedBy() {
        return mGezAddedBy;
    }

    public void setGezAddedBy(String gezAddedBy) {
        mGezAddedBy = gezAddedBy;
    }

    public String getGezID() {
        return mGezID;
    }

    public void setGezID(String gezID) {
        mGezID = gezID;
    }

    public String getGezPlayed() {
        return mGezPlayed;
    }

    public void setGezPlayed(String gezPlayed) {
        mGezPlayed = gezPlayed;
    }

    public String getGezSelected() {
        return mGezSelected;
    }

    public void setGezSelected(String gezSelected) {
        mGezSelected = gezSelected;
    }

    public String getTotalFriendsDone() {
        return mTotalFriendsDone;
    }

    public void setTotalFriendsDone(String totalFriendsDone) {
        mTotalFriendsDone = totalFriendsDone;
    }

    public String getTotalFriendsDoneRated() {
        return mTotalFriendsDoneRated;
    }

    public void setTotalFriendsDoneRated(String totalFriendsDoneRated) {
        mTotalFriendsDoneRated = totalFriendsDoneRated;
    }

    public String getTotalFriendsOpen() {
        return mTotalFriendsOpen;
    }

    public void setTotalFriendsOpen(String totalFriendsOpen) {
        mTotalFriendsOpen = totalFriendsOpen;
    }

    public String getTotalFriendsOpenRated() {
        return mTotalFriendsOpenRated;
    }

    public void setTotalFriendsOpenRated(String totalFriendsOpenRated) {
        mTotalFriendsOpenRated = totalFriendsOpenRated;
    }

    public String getVideoName() {
        return mVideoName;
    }

    public void setVideoName(String videoName) {
        mVideoName = videoName;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        mVideoUrl = videoUrl;
    }

}
