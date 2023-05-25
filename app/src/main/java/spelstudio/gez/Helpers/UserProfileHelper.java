package spelstudio.gez.Helpers;

public class UserProfileHelper {
    public UserProfileHelper(String mUserEmail, String mUserPassword) {
        this.mUserEmail = mUserEmail;
        this.mUserPassword = mUserPassword;
    }

    String mUserEmail;
    String mUserPassword;

    public UserProfileHelper() {

    }

    public String getmUserEmail() {
        return mUserEmail;
    }

    public String getmUserPassword() {
        return mUserPassword;
    }
}
