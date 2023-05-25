package spelstudio.gez.webapi.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Gez {

    @SerializedName("GezID")
    @Expose
    public String gezID;
    @SerializedName("VideoUrl")
    @Expose
    public String videoUrl;
    @SerializedName("VideoName")
    @Expose
    public String videoName;
    @SerializedName("GezAddedBy")
    @Expose
    public String gezAddedBy;
    @SerializedName("TotalGood")
    @Expose
    public String totalGood;
    @SerializedName("TotalWrong")
    @Expose
    public String totalWrong;
    @SerializedName("TotalFriendsDoneRated")
    @Expose
    public String totalFriendsDoneRated;
    @SerializedName("TotalFriendsDone")
    @Expose
    public String totalFriendsDone;
    @SerializedName("TotalFriendsOpenRated")
    @Expose
    public String totalFriendsOpenRated;
    @SerializedName("TotalFriendsOpen")
    @Expose
    public String totalFriendsOpen;

}
