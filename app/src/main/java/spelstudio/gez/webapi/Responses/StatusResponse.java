
package spelstudio.gez.webapi.Responses;

import com.google.gson.annotations.SerializedName;

public class StatusResponse {

    @SerializedName("errMsg")
    private String mErrMsg;
    @SerializedName("errNum")
    private String mErrNum;

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

}
