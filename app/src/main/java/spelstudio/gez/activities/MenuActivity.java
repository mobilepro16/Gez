package spelstudio.gez.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import spelstudio.gez.Helpers.SharedPrefrenceHelper;
import spelstudio.gez.Helpers.UserProfileHelper;
import spelstudio.gez.R;
import spelstudio.gez.constants.Constants;
import spelstudio.gez.webapi.Api;
import spelstudio.gez.webapi.Controller;
import spelstudio.gez.webapi.Responses.StatusResponse;
import spelstudio.gez.webapi.Responses.UserInfo;

public class MenuActivity extends AppCompatActivity {
    ImageButton mSettingsBtn;
    SharedPrefrenceHelper mHelper;
    TextView mBadgeOpenGez, mBadgeFrnd;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if (!TextUtils.isEmpty(Constants.inviteCode)) {
            addFriendCall();
        }

        mBadgeFrnd = findViewById(R.id.menu_badge_frnd);
        mBadgeOpenGez = findViewById(R.id.menu_badge_gez);
        mBadgeOpenGez.setVisibility(View.GONE);
        mBadgeFrnd.setVisibility(View.GONE);
        mHelper = new SharedPrefrenceHelper(getApplicationContext());
        mSettingsBtn = findViewById(R.id.menu_settings_btn);

        mSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, SettingsActivity.class));

            }
        });
    }

    private void addFriendCall() {
        progressDialog = ProgressDialog.show(MenuActivity.this, "", "Please wait");
        Api mApi = Controller.getApi();
        SharedPrefrenceHelper mHelper = new SharedPrefrenceHelper(getApplicationContext());
        UserInfo mInfo = mHelper.getSavedUser();
        RequestBody mBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("UserGuid", mInfo.userGuid)
                .addFormDataPart("InviteCode", Constants.inviteCode)
                .build();
        Call<StatusResponse> mCall = mApi.setFriend(mBody);
        mCall.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                progressDialog.dismiss();
                StatusResponse resp = response.body();
                if (resp != null && resp.getErrNum().equals("200")) {
                    Constants.inviteCode = "";
                    Intent intent = new Intent(MenuActivity.this, FriendsActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                progressDialog.dismiss();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        UserProfileHelper mUserProf = new UserProfileHelper();
        if (mHelper.getUserProfHelper() != null) {
            mUserProf = mHelper.getUserProfHelper();
            getBadgesData(mUserProf);
        }
    }

    private void getBadgesData(UserProfileHelper mUserProf) {
        Api mApi = Controller.getApi();
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("UserEmail", mUserProf.getmUserEmail())
                .addFormDataPart("UserPassword", mUserProf.getmUserPassword())
                .build();


        Call<UserInfo> mCall = mApi.getUserLogin(body);
        mCall.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response.body().errNum.equalsIgnoreCase("200")) {
                    UserInfo mInfo = response.body();
                    SharedPrefrenceHelper mHelper = new SharedPrefrenceHelper(getApplicationContext());
                    mHelper.addUserInfo(mInfo);

                    if (mInfo.openGez != null && !mInfo.openGez.equals("0")) {
                        mBadgeOpenGez.setVisibility(View.VISIBLE);
                        mBadgeOpenGez.setText(mInfo.openGez);
                    } else {
                        mBadgeOpenGez.setVisibility(View.GONE);
                    }
                    if (mInfo.openFriends != null && !mInfo.openFriends.equals("0")) {
                        mBadgeFrnd.setVisibility(View.VISIBLE);
                        mBadgeFrnd.setText(mInfo.openFriends);
                    } else {
                        mBadgeFrnd.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void playBtnClicked(View mView) {
        startActivity(new Intent(MenuActivity.this, PlayActivity.class));
    }

    public void friendsBtnClicked(View mView) {
        startActivity(new Intent(this, FriendsActivity.class));
    }

    public void aboutBtnClicked(View mView) {
        startActivity(new Intent(this, AboutGezActivity.class));
    }

    public void addGezBtnClicked(View mView) {
        startActivity(new Intent(this, SearchGezActivity.class));
    }

    public void RankClicked(View mView) {
        startActivity(new Intent(this, RankActivity.class));
    }


}
