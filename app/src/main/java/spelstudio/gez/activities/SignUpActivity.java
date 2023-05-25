package spelstudio.gez.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import spelstudio.gez.Helpers.SharedPrefrenceHelper;
import spelstudio.gez.Helpers.UserProfileHelper;
import spelstudio.gez.Helpers.imagepicker.imagePicker.ImagePicker;
import spelstudio.gez.R;
import spelstudio.gez.webapi.Api;
import spelstudio.gez.webapi.Controller;
import spelstudio.gez.webapi.MultiPartHelper;
import spelstudio.gez.webapi.Responses.Country;
import spelstudio.gez.webapi.Responses.GetCountriesResponse;
import spelstudio.gez.webapi.Responses.SetUserInfoResponse;
import spelstudio.gez.webapi.Responses.UserInfo;

public class SignUpActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener {
    Calendar mCalendar;
    int month, year, day;
    Bitmap bitmap;
    Button saveBtn;
    ImageView userProfImg;
    EditText userNameEdt, emailEdt, passwordEdt;
    Spinner countryEdt;
    TextView dateText, alreadyHvAccTxt, tvTitle;
    View llEmail;
    UserInfo mInfo;
    boolean isUpdate;

    ProgressDialog mProgressDialog;

    List<Country> countries;
    private File userImage;
    private ImagePicker imagePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mCalendar = Calendar.getInstance();

        isUpdate = getIntent().getBooleanExtra("isUpdate", false);

        userProfImg = (ImageView) findViewById(R.id.usr_signup_img);
        saveBtn = findViewById(R.id.SaveBtn);
        llEmail = findViewById(R.id.llEmail);
        tvTitle = findViewById(R.id.tvTitle);

        userNameEdt = (EditText) findViewById(R.id.usr_name_edt);
        emailEdt = (EditText) findViewById(R.id.usr_email_edt);
        countryEdt = findViewById(R.id.usr_country_edt);
        passwordEdt = (EditText) findViewById(R.id.usr_password_edt);
        dateText = (TextView) findViewById(R.id.usr_dob_txt2);
        Glide.with(this).load(R.drawable.no_user_logo).apply(RequestOptions.circleCropTransform()).into(userProfImg);
        userProfImg.setOnClickListener(this);
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDatePickerClicked();
            }
        });
        day = mCalendar.get(Calendar.DAY_OF_MONTH);
        month = mCalendar.get(Calendar.MONTH);
        year = mCalendar.get(Calendar.YEAR);
        alreadyHvAccTxt = (TextView) findViewById(R.id.already_hv_acc_txt);
        alreadyHvAccTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
        month++;
        if (isUpdate) {
            saveBtn.setText("Update");
            tvTitle.setText("Update profile");
            alreadyHvAccTxt.setVisibility(View.GONE);
            mProgressDialog = ProgressDialog.show(SignUpActivity.this, getString(R.string.app_name), "Please Wait");
            getUserInfo();
        }

        getCountries();
    }

    private void getUserInfo() {
        /**/

        SharedPrefrenceHelper mHelper = new SharedPrefrenceHelper(getApplicationContext());
        mInfo = mHelper.getSavedUser();
        Api mApi = Controller.getApi();
        RequestBody mBody = new MultipartBody.Builder().
                setType(MultipartBody.FORM).
                addFormDataPart("UserGuid", mInfo.userGuid).
                build();
        final Call<UserInfo> mResp = mApi.getUserInfo(mBody);
        mResp.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                mProgressDialog.dismiss();

                if (response.isSuccessful() && response.body() != null) {
                    SharedPrefrenceHelper mHelper = new SharedPrefrenceHelper(getApplicationContext());
                    mHelper.addUserInfo(response.body());
                    UserProfileHelper mUserProf = mHelper.getUserProfHelper();
                    alreadyHvAccTxt.setVisibility(View.GONE);
                    populateUserData(response.body(), mUserProf);
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(SignUpActivity.this, "error", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });

    }

    private void populateUserData(UserInfo mInfo, UserProfileHelper mUserProf) {
        userNameEdt.setText(mInfo.userName);
        dateText.setText(mInfo.userBirthDate);
        emailEdt.setText(mUserProf.getmUserEmail());
        passwordEdt.setText(mUserProf.getmUserPassword());

        Glide.with(SignUpActivity.this).load(mInfo.userPicUrl).into(userProfImg);
    }

    public void saveBtnClicked(View mView) {
        validateForm();
    }

    //Todo set email validity properly
    void validateForm() {

        if (userNameEdt.getText().toString().length() > 0 && emailEdt.getText().toString().contains("@") && countryEdt.getSelectedItemPosition() > 0 && passwordEdt.getText().toString().length() > 0) {

            String encoded = "";
            if (isUpdate)
                updateCall(encoded);
            else
                signupCall(encoded);
        } else {
            //todo ask sir validation
            if (userNameEdt.getText().toString().isEmpty()) {
                userNameEdt.setError("Invalid User Name");
            } else {
                userNameEdt.setError(null);
            }
            if (emailEdt.getText().toString().isEmpty()) {
                emailEdt.setError("Invalid Email");
            } else {
                emailEdt.setError(null);
            }
            if (dateText.getText().toString().isEmpty()) {
                dateText.setError("Invalid Date");
            } else {
                dateText.setError(null);
            }
            if (passwordEdt.getText().toString().isEmpty()) {
                passwordEdt.setError("Invalid Password");
            } else {
                passwordEdt.setError(null);
            }
        }
    }

    private void updateCall(String encoded) {

        mProgressDialog = ProgressDialog.show(SignUpActivity.this, "", "Please wait");

        RequestBody body;
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("UserGuid", mInfo.userGuid)
                .addFormDataPart("UserEmail", emailEdt.getText().toString().trim())
                .addFormDataPart("UserPassword", passwordEdt.getText().toString().trim())
                .addFormDataPart("UserName", userNameEdt.getText().toString().trim())
                .addFormDataPart("UserBirthDate", dateText.getText().toString().trim())
                .addFormDataPart("UserCountry", countries.get(countryEdt.getSelectedItemPosition()).id);
        if (userImage != null) {
            builder.addPart(MultiPartHelper.prepareImageFilePart("UserPic", userImage, SignUpActivity.this));
        }
        body = builder.build();

        Api mApi = Controller.getApi();
        Call<SetUserInfoResponse> mCall = mApi.updateUserInfo(body);
        mCall.enqueue(new Callback<SetUserInfoResponse>() {
            @Override
            public void onResponse(Call<SetUserInfoResponse> call, Response<SetUserInfoResponse> response) {
                mProgressDialog.dismiss();
                if (response != null) {
                    SetUserInfoResponse mResp;
                    mResp = response.body();
                    if (mResp.getErrNum().equalsIgnoreCase("200")) {
                        finish();
                        Toast.makeText(SignUpActivity.this, "Updated Successfully", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(SignUpActivity.this, mResp.getErrMsg() + "", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SetUserInfoResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                t.printStackTrace();
                if (isUpdate) {
                    Toast.makeText(SignUpActivity.this, "Update Failed", Toast.LENGTH_LONG).show();
                } else {

                    Toast.makeText(SignUpActivity.this, "Signup Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void signupCall(String encoded) {
        RequestBody body;
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("UserEmail", emailEdt.getText().toString().trim())
                .addFormDataPart("UserPassword", passwordEdt.getText().toString().trim())
                .addFormDataPart("UserName", userNameEdt.getText().toString().trim())
                .addFormDataPart("UserBirthDate", dateText.getText().toString().trim())
                .addFormDataPart("UserCountry", countries.get(countryEdt.getSelectedItemPosition()).id);
        if (userImage != null) {
            builder.addPart(MultiPartHelper.prepareImageFilePart("UserPic", userImage, SignUpActivity.this));
        }
        body = builder.build();

        Api mApi = Controller.getApi();
        Call<SetUserInfoResponse> mCall = mApi.setUserInfo(body);
        mCall.enqueue(new Callback<SetUserInfoResponse>() {
            @Override
            public void onResponse(Call<SetUserInfoResponse> call, Response<SetUserInfoResponse> response) {
                if (response != null) {
                    SetUserInfoResponse mResp = new SetUserInfoResponse();
                    mResp = response.body();
                    String respp = response.body().toString();
                    if (mResp.getErrNum().equalsIgnoreCase("200")) {
                        finish();
                        Toast.makeText(SignUpActivity.this, "Signup Successful", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SetUserInfoResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(SignUpActivity.this, "Signup Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onDatePickerClicked() {
        DatePickerDialog mDateDialog = new DatePickerDialog(this, this, year, month, day);
        mDateDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        month++;
        dateText.setText(year + "-" + (month > 9 ? month : "0" + month) + "-" + (day > 9 ? day : "0" + day));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.usr_signup_img: {

                Dexter.withActivity(SignUpActivity.this)
                        .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                        .withListener(new CompositeMultiplePermissionsListener(DialogOnAnyDeniedMultiplePermissionsListener.Builder
                                .withContext(SignUpActivity.this)
                                .withTitle("Camera & Photos permission")
                                .withMessage("Both camera and Photos permission are needed to take your Picture")
                                .withButtonText(android.R.string.ok)
                                .withIcon(R.mipmap.ic_launcher)
                                .build(),
                                new MultiplePermissionsListener() {
                                    @Override
                                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                                        if (report.areAllPermissionsGranted()) {
                                            imagePicker = new ImagePicker();
                                            imagePicker.withActivity(SignUpActivity.this)
                                                    .withCompression(false)
                                                    .start();
                                        }
                                    }

                                    @Override
                                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
                                }))
                        .check();

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ImagePicker.SELECT_IMAGE && resultCode == Activity.RESULT_OK) {

            String filePath = imagePicker.getImageFilePath(data);

            if (filePath != null) {

                Bitmap selectedImage = BitmapFactory.decodeFile(filePath);

                userProfImg.setImageBitmap(selectedImage);
                userImage = new File(filePath);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getCountries() {
        Api mApi = Controller.getApi();
        Call<GetCountriesResponse> mCall = mApi.getCountryList();
        mCall.enqueue(new Callback<GetCountriesResponse>() {
            @Override
            public void onResponse(Call<GetCountriesResponse> call, Response<GetCountriesResponse> response) {
                if (response != null) {
                    GetCountriesResponse mResp = response.body();
                    if (mResp.errNum.equalsIgnoreCase("200")) {
                        countries = mResp.arrayCountry;
                        countries.add(0, new Country());
                        ArrayAdapter<Country> spinnerArrayAdapter = new ArrayAdapter<Country>
                                (SignUpActivity.this, android.R.layout.simple_spinner_item,
                                        countries);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                                .simple_spinner_dropdown_item);
                        countryEdt.setAdapter(spinnerArrayAdapter);

                        if (isUpdate) {
                            countryEdt.setSelection(Integer.parseInt(mInfo.userCountry));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GetCountriesResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
}
