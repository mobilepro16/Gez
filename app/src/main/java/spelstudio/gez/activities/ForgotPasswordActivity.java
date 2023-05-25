package spelstudio.gez.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import spelstudio.gez.R;
import spelstudio.gez.webapi.Api;
import spelstudio.gez.webapi.Controller;
import spelstudio.gez.webapi.Responses.GetPromoVidzResponse;
import spelstudio.gez.webapi.Responses.ResetPasswordResponse;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText mEmailEdt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mEmailEdt=(EditText)findViewById(R.id.frgt_pswrd_email);
    }

    public void mCancelBtnClicked(View mView){
        finish();
    }

    public void onSendPasswordBtnClicked(View mView){
        if(mEmailEdt.getText().toString().trim()!=null&&!mEmailEdt.getText().toString().trim().isEmpty()){
            mEmailEdt.setError(null);

            String email=mEmailEdt.getText().toString().trim();
            RequestBody mBody=new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("UserEmail",email)
                    .build();
            Api mApi= Controller.getApi();
            Call<ResetPasswordResponse> mCall=mApi.resetPassword(mBody);
            mCall.enqueue(new Callback<ResetPasswordResponse>() {
                @Override
                public void onResponse(Call<ResetPasswordResponse> call, Response<ResetPasswordResponse> response) {
                    ResetPasswordResponse mResponse=response.body();
                    if(mResponse.getErrNum().trim().equalsIgnoreCase("200")){
                        Toast.makeText(ForgotPasswordActivity.this,"Password Sent Successfully",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(ForgotPasswordActivity.this,"Failed to sent email",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResetPasswordResponse> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }else {
            mEmailEdt.setError("Invalid Email");
        }
    }

}
