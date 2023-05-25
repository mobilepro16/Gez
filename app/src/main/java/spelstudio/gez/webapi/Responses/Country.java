package spelstudio.gez.webapi.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Country {

    public Country() {
        id = "-1";
        name = "";
    }

    @SerializedName("ID")
    @Expose
    public String id;
    @SerializedName("Name")
    @Expose
    public String name;

    @Override
    public String toString() {
        return name;
    }
}
