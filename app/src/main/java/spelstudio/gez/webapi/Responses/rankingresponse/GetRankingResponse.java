
package spelstudio.gez.webapi.Responses.rankingresponse;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class GetRankingResponse {

    @SerializedName("AddedGez")
    private String mAddedGez;
    @SerializedName("arrayFriends")
    private List<ArrayFriend> mArrayFriends;
    @SerializedName("Emo1")
    private String mEmo1;
    @SerializedName("Emo2")
    private String mEmo2;
    @SerializedName("Emo3")
    private String mEmo3;
    @SerializedName("Emo4")
    private String mEmo4;
    @SerializedName("Emo5")
    private String mEmo5;
    @SerializedName("Emo6")
    private String mEmo6;
    @SerializedName("errMsg")
    private String mErrMsg;
    @SerializedName("errNum")
    private String mErrNum;
    @SerializedName("OpenGez")
    private String mOpenGez;
    @SerializedName("TotalFriends")
    private String mTotalFriends;
    @SerializedName("TotalGezGood")
    private String mTotalGezGood;
    @SerializedName("TotalGezWrong")
    private String mTotalGezWrong;
    @SerializedName("UserName")
    private String mUserName;
    @SerializedName("UserPicUrl")
    private String mUserPicUrl;

    public String getAddedGez() {
        return mAddedGez;
    }

    public void setAddedGez(String addedGez) {
        mAddedGez = addedGez;
    }

    public List<ArrayFriend> getArrayFriends() {
        return mArrayFriends;
    }

    public void setArrayFriends(List<ArrayFriend> arrayFriends) {
        mArrayFriends = arrayFriends;
    }

    public String getEmo1() {
        return mEmo1;
    }

    public void setEmo1(String emo1) {
        mEmo1 = emo1;
    }

    public String getEmo2() {
        return mEmo2;
    }

    public void setEmo2(String emo2) {
        mEmo2 = emo2;
    }

    public String getEmo3() {
        return mEmo3;
    }

    public void setEmo3(String emo3) {
        mEmo3 = emo3;
    }

    public String getEmo4() {
        return mEmo4;
    }

    public void setEmo4(String emo4) {
        mEmo4 = emo4;
    }

    public String getEmo5() {
        return mEmo5;
    }

    public void setEmo5(String emo5) {
        mEmo5 = emo5;
    }

    public String getEmo6() {
        return mEmo6;
    }

    public void setEmo6(String emo6) {
        mEmo6 = emo6;
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

    public String getOpenGez() {
        return mOpenGez;
    }

    public void setOpenGez(String openGez) {
        mOpenGez = openGez;
    }

    public String getTotalFriends() {
        return mTotalFriends;
    }

    public void setTotalFriends(String totalFriends) {
        mTotalFriends = totalFriends;
    }

    public String getTotalGezGood() {
        return mTotalGezGood;
    }

    public void setTotalGezGood(String totalGezGood) {
        mTotalGezGood = totalGezGood;
    }

    public String getTotalGezWrong() {
        return mTotalGezWrong;
    }

    public void setTotalGezWrong(String totalGezWrong) {
        mTotalGezWrong = totalGezWrong;
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
