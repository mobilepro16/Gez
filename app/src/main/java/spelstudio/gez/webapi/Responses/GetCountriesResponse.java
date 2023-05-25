package spelstudio.gez.webapi.Responses;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetCountriesResponse {

    @SerializedName("errNum")
    @Expose
    public String errNum;
    @SerializedName("errMsg")
    @Expose
    public String errMsg;
    @SerializedName("arrayCountry")
    @Expose
    public List<Country> arrayCountry = null;



}
