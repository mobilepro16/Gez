package spelstudio.gez.webapi.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetGezListResponse {

    @SerializedName("errNum")
    @Expose
    public String errNum;
    @SerializedName("errMsg")
    @Expose
    public String errMsg;
    @SerializedName("ArrayGez")
    @Expose
    public List<Gez> arrayGez = null;



}
