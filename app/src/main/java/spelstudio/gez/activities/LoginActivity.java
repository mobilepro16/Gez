package spelstudio.gez.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import spelstudio.gez.Helpers.SharedPrefrenceHelper;
import spelstudio.gez.Helpers.UserProfileHelper;
import spelstudio.gez.R;
import spelstudio.gez.webapi.Api;
import spelstudio.gez.webapi.Controller;
import spelstudio.gez.webapi.Responses.UserInfo;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText emailEdt, passwordEdt;
    ProgressDialog mProgessDialog;
    Boolean btnClicked = false;
    TextView mFrgtPswrdTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_gez);
        TextView signUpTxt = (TextView) findViewById(R.id.sign_up_txt);
        signUpTxt.setOnClickListener(this);
        mProgessDialog = new ProgressDialog(this);
        mProgessDialog.setMessage("Loading");
        mProgessDialog.setCanceledOnTouchOutside(false);
        mFrgtPswrdTxt = (TextView) findViewById(R.id.forgetPasswordTxt);
        mFrgtPswrdTxt.setOnClickListener(this);
        emailEdt = (EditText) findViewById(R.id.login_email_edt);
        passwordEdt = (EditText) findViewById(R.id.login_pswrd_edt);
    }

    void checkValidity() {

        if (isValidEmailId(emailEdt.getText().toString().trim()) && passwordEdt.getText().toString().trim().length() > 4) {
            mProgessDialog.show();
            requestLogin();
        } else {
            if (!isValidEmailId(emailEdt.getText().toString().trim())) {
                emailEdt.setError("Invalid Email");
            }
            if (passwordEdt.getText().toString().length() < 4) {
                passwordEdt.setError("Invalid Password");
            }
        }

    }

    private boolean isValidEmailId(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnClicked = false;
    }

    private void requestLogin() {
        Api mApi = Controller.getApi();
        /*UserLoginRequest mReq = new UserLoginRequest();
        mReq.UserEmail = emailEdt.getText().toString();
        mReq.UserPassword = passwordEdt.getText().toString();*/

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("UserEmail", emailEdt.getText().toString().trim())
                .addFormDataPart("UserPassword", passwordEdt.getText().toString().trim())
                .build();


        Call<UserInfo> mCall = mApi.getUserLogin(body);
        mCall.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                mProgessDialog.cancel();
                if (response.body().errNum.equalsIgnoreCase("200".toString().trim())) {
                    UserInfo mInfo = response.body();
                    SharedPrefrenceHelper mHelper = new SharedPrefrenceHelper(getApplicationContext());
                    mHelper.addUserInfo(mInfo);
                    mHelper.addUserProf(new UserProfileHelper(emailEdt.getText().toString(), passwordEdt.getText().toString()));
                    finish();
                    startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                } else {
                    emailEdt.setError("Invalid");
                    passwordEdt.setError("Invalid");
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                mProgessDialog.cancel();
                Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

    public void signUpBtnCicked() {
        btnClicked = true;
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.putExtra("isUpdate", false);
        startActivity(intent);

    }

    public void loginBtnClicked(View mView) {
        //todo uncheck rest code
        checkValidity();
       /* finish();
        startActivity(new Intent(LoginActivity.this, MenuActivity.class));*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_up_txt: {
                signUpBtnCicked();
                break;
            }
            case R.id.forgetPasswordTxt: {
                btnClicked = true;
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (btnClicked == false) {
            finish();
        }
    }
}
