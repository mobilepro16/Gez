package spelstudio.gez.Helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import spelstudio.gez.webapi.Responses.UserInfo;

import static spelstudio.gez.constants.Constants.USER_INFO_KEY;
import static spelstudio.gez.constants.Constants.USER_PROFILE_KEY;

public class SharedPrefrenceHelper {
    SharedPreferences.Editor prefsEditor;
    SharedPreferences mPrefs;

    public SharedPrefrenceHelper(Context mContext) {
        mPrefs = PreferenceManager
                .getDefaultSharedPreferences(mContext);

//        mPrefs = mContext.getPreferences(MODE_PRIVATE);
//       prefsEditor = mPrefs.edit();
    }

    public void addUserInfo(UserInfo mInfo) {
        prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mInfo);
        prefsEditor.putString(USER_INFO_KEY, json);
        prefsEditor.commit();
    }

    public void removeUserInfo() {
        prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        prefsEditor.putString(USER_INFO_KEY, "");
        prefsEditor.commit();
    }

    public UserInfo getSavedUser() {
        Gson mGson = new Gson();
        String json = mPrefs.getString(USER_INFO_KEY, "");
        UserInfo mInfo = mGson.fromJson(json, UserInfo.class);
        return mInfo;
    }

    public void addUserProf(UserProfileHelper mProfHelper) {
        prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mProfHelper);
        prefsEditor.putString(USER_PROFILE_KEY, json);
        prefsEditor.commit();
    }

    public UserProfileHelper getUserProfHelper() {
        Gson mGson = new Gson();
        String json = mPrefs.getString(USER_PROFILE_KEY, "");
        UserProfileHelper mInfo = mGson.fromJson(json, UserProfileHelper.class);
        return mInfo;
    }
}
