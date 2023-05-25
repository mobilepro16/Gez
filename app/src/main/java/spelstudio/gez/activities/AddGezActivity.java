package spelstudio.gez.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import spelstudio.gez.Helpers.SharedPrefrenceHelper;
import spelstudio.gez.Helpers.UrlToVideoIdConverter;
import spelstudio.gez.R;
import spelstudio.gez.constants.Constants;
import spelstudio.gez.webapi.Api;
import spelstudio.gez.webapi.Controller;
import spelstudio.gez.webapi.Responses.AddGezResponse;
import spelstudio.gez.webapi.Responses.UserInfo;

public class AddGezActivity extends YouTubeBaseActivity implements RadioGroup.OnCheckedChangeListener {
    RadioGroup mRadioGroup;
    Button mAddGezBtn;
    YouTubePlayer.OnInitializedListener mInitializeListener;
    YouTubePlayerView mPlayer;
    ProgressDialog mProgressDialog;
    String videoName, videoOwner, videoUrl;
    String gezSelected = null;
    UserInfo mInfo;
    TextView mVideoNameTxt, mVideoOwnerTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gez);
        mRadioGroup = (RadioGroup) findViewById(R.id.radiogroup_add_gez);
        mRadioGroup.setOnCheckedChangeListener(this);
        mAddGezBtn = (Button) findViewById(R.id.add_gez_add_btn);
        mVideoNameTxt = (TextView) findViewById(R.id.add_gez_video_desc);
        mVideoOwnerTxt = (TextView) findViewById(R.id.add_gez_desc_2);
        mInfo = new UserInfo();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage("Loading");
        SharedPrefrenceHelper mHelper = new SharedPrefrenceHelper(getApplicationContext());
        mInfo = mHelper.getSavedUser();

        try {
            videoName = getIntent().getExtras().getString("videoName").toString();
            videoOwner = getIntent().getExtras().getString("videoOwner").toString();
            videoUrl = getIntent().getExtras().getString("videoUrl").toString();
            mVideoNameTxt.setText(videoName);
            mVideoOwnerTxt.setText(videoOwner);
        } catch (Exception e) {
            e.printStackTrace();
        }

        FrameLayout fl_player = findViewById(R.id.videoView);
        mPlayer = new YouTubePlayerView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        fl_player.addView(mPlayer, params); //todo remove this function call after demo testing
        UrlToVideoIdConverter mConv = new UrlToVideoIdConverter();
        String videoId = mConv.videoId(videoUrl);
        onInitializePlayer(videoId);

        //todo get guid from user shared prefrences

    }

    private void callAddGez() {
        String completeURL = "";
        if (videoUrl.startsWith("http:")) {
            completeURL = videoUrl;
        } else {
            completeURL = "https://www.youtube.com/watch?v=" + videoUrl;
        }
        mProgressDialog.show();
        RequestBody mRequest = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("UserGuid", mInfo.userGuid)
                .addFormDataPart("VideoUrl", completeURL.trim())
                .addFormDataPart("VideoName", videoName)
                .addFormDataPart("VideoOwner", videoOwner.trim())
                .addFormDataPart("GezSelected", gezSelected.trim())
                .build();
        Api mApi = Controller.getApi();
        Call<AddGezResponse> mCall = mApi.addGez(mRequest);
        mCall.enqueue(new Callback<AddGezResponse>() {
            @Override
            public void onResponse(Call<AddGezResponse> call, Response<AddGezResponse> response) {
                mProgressDialog.cancel();
                if (response != null && response.isSuccessful() && response.body() != null) {
                    AddGezResponse mResp = new AddGezResponse();
                    mResp = response.body();
                    if (mResp.getErrNum() == null || mResp.getErrNum().toString().equalsIgnoreCase("200")) {
                        Toast.makeText(AddGezActivity.this, "Successfully Added", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(AddGezActivity.this, "Operation failed: " + mResp.getErrMsg(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AddGezResponse> call, Throwable t) {
                mProgressDialog.cancel();
                t.printStackTrace();
                Toast.makeText(AddGezActivity.this, "Resp not found ", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.add_rate_emoji_1: {
                setSelected(radioGroup, 0);
                mAddGezBtn.setEnabled(true);
                gezSelected = "1";
                break;
            }
            case R.id.add_rate_emoji_2: {
                setSelected(radioGroup, 1);
                mAddGezBtn.setEnabled(true);
                gezSelected = "2";
                break;
            }

            case R.id.add_rate_emoji_3: {
                setSelected(radioGroup, 2);
                mAddGezBtn.setEnabled(true);
                gezSelected = "3";
                break;
            }
            case R.id.add_rate_emoji_4: {
                setSelected(radioGroup, 3);
                mAddGezBtn.setEnabled(true);
                gezSelected = "4";
                break;
            }
            case R.id.add_rate_emoji_5: {
                setSelected(radioGroup, 4);
                mAddGezBtn.setEnabled(true);
                gezSelected = "5";
                break;
            }
            case R.id.add_rate_emoji_6: {
                setSelected(radioGroup, 5);
                mAddGezBtn.setEnabled(true);
                gezSelected = "6";
                break;
            }

        }
    }


    public void onInitializePlayer(final String url) {
        //todo delete after demo testing
        mInitializeListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//                youTubePlayer.setPlaybackEventListener(AddGezActivity.this);
//                youTubePlayer.setPlayerStateChangeListener(AddGezActivity.this);
                youTubePlayer.loadVideo(url);
//                youTubePlayer.cueVideo("RUwkB_2inPg");
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//                if (youTubeInitializationResult.isUserRecoverableError()) {
//                    youTubeInitializationResult.getErrorDialog(PlayActivity.this, RECOVERY_DIALOG_REQUEST_YOUTUBE_PLAYER).show();
//                } else {
//                    String errorMessage = String.format(
//                            getString(R.string.error_player).toString());
//                    Toast.makeText(PlayActivity.this, errorMessage, Toast.LENGTH_LONG).show();
//                }
            }
        };
        mPlayer.initialize(Constants.YOUTUBE_DEVELOPER_KEY, mInitializeListener);
    }


    public void addGezClicked(View mView) {
        callAddGez();
    }

    private void setSelected(RadioGroup radioGroup, int selectedChild) {
        for (int i = 0; i < 6; i++) {
            if (i != selectedChild) {
                radioGroup.getChildAt(i).setEnabled(false);
            }
        }
    }

    public void goHome(View view) {
        finish();
    }
}
