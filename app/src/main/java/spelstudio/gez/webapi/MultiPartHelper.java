package spelstudio.gez.webapi;

import android.content.Context;

import java.io.File;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MultiPartHelper {

    public static MultipartBody.Part prepareImageFilePart(String parameter,File file, Context context) {
        try {
            RequestBody requestFile =
                    RequestBody.create(
                            MediaType.parse("image/*"),
                            new Compressor(context).compressToFile(file)
                    );

            // MultipartBody.Part is used to send also the actual file name
            return MultipartBody.Part.createFormData(parameter, file.getName(), requestFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static RequestBody createPartFromString(String value) {
        return RequestBody.create(MultipartBody.FORM, value);
    }
}
