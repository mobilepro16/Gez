package spelstudio.gez.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import spelstudio.gez.Helpers.SharedPrefrenceHelper;
import spelstudio.gez.R;
import spelstudio.gez.webapi.Api;
import spelstudio.gez.webapi.Controller;
import spelstudio.gez.webapi.Responses.DelUserResponse;
import spelstudio.gez.webapi.Responses.UserInfo;

public class SettingsActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait");
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    public void onBackBtnfClicked(View view) {
        finish();
    }

    public void onUpdateProfClicked(View view) {

        Intent intent = new Intent(SettingsActivity.this, SignUpActivity.class);
        intent.putExtra("isUpdate", true);
        startActivity(intent);

    }

    public void onDelAccClicked(View view) {
        new AlertDialog.Builder(SettingsActivity.this)
                .setTitle(getString(R.string.app_name))
                .setMessage("Do you want to Delete your account?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteAccountCall();
                    }
                })
                .setNegativeButton("No", null)
                .create().show();


    }

    private void deleteAccountCall() {
        mProgressDialog.show();
        SharedPrefrenceHelper mHelper = new SharedPrefrenceHelper(getApplicationContext());
        UserInfo mInfo = mHelper.getSavedUser();
        Api mApi = Controller.getApi();
        RequestBody mBody = new MultipartBody.Builder().
                setType(MultipartBody.FORM).
                addFormDataPart("UserGuid", mInfo.userGuid).
                build();
        final Call<DelUserResponse> mResp = mApi.delUser(mBody);
        mResp.enqueue(new Callback<DelUserResponse>() {
            @Override
            public void onResponse(Call<DelUserResponse> call, Response<DelUserResponse> response) {
                mProgressDialog.cancel();
                if (response.body() != null) {
                    logOut();
                }
            }

            @Override
            public void onFailure(Call<DelUserResponse> call, Throwable t) {
                mProgressDialog.cancel();
                Toast.makeText(SettingsActivity.this, "error", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });

    }

    private void logOut() {
        new SharedPrefrenceHelper(this).removeUserInfo();
        Intent intent = new Intent(this, SplashScreenActivity.class);
        //TODO: remove all from backstack
        startActivity(intent);
    }

    public void onLogoutClicked(View view) {
        new AlertDialog.Builder(SettingsActivity.this)
                .setTitle(getString(R.string.app_name))
                .setMessage("Do you want to Logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        logOut();
                    }
                })
                .setNegativeButton("No", null)
                .create().show();
    }

    public void onAboutUsClicked(View view) {
        Intent intent = new Intent(SettingsActivity.this, AboutGezActivity.class);
        startActivity(intent);
    }
}
