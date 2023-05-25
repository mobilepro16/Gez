package spelstudio.gez.webapi.Responses;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetPromoVidzResponse {

    @SerializedName("errNum")
    @Expose
    public String errNum;
    @SerializedName("errMsg")
    @Expose
    public String errMsg;
    @SerializedName("arrayVideos")
    @Expose
    public List<Video> arrayVideos = null;

    public class Video {

        @SerializedName("VideoUrl")
        @Expose
        public String videoUrl;
        @SerializedName("VideoName")
        @Expose
        public String videoName;
        @SerializedName("VideoOwner")
        @Expose
        public String videoOwner;
        @SerializedName("VideoLabelUrl")
        @Expose
        public String videoLabelUrl;

    }


}