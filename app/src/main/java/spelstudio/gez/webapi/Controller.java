package spelstudio.gez.webapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Controller {
    static String API_BASE_URL="https://gez.onsfotoboek.nl/GezAPI.php/";
    Controller(){}
    public static Api getApi(){
        HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor();
        //Set your desired log level
        Gson gson = new GsonBuilder().setLenient().create();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder mOkHttpClient=new OkHttpClient.Builder();
        mOkHttpClient.connectTimeout(4, TimeUnit.MINUTES).writeTimeout(4, TimeUnit.MINUTES).readTimeout(10, TimeUnit.MINUTES);
        Retrofit.Builder mBuilder=new Retrofit.Builder().baseUrl(API_BASE_URL).addConverterFactory(GsonConverterFactory.create(gson));
        Retrofit mRetrofit=mBuilder.client(mOkHttpClient.addInterceptor(httpLoggingInterceptor).build()).build();
        //Create a very simple rest adapter which points the github api endpoints
        Api mClient=mRetrofit.create(Api.class);
        return mClient;
    }
}
