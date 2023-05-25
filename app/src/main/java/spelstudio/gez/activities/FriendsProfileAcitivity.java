package spelstudio.gez.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import spelstudio.gez.Helpers.SharedPrefrenceHelper;
import spelstudio.gez.R;
import spelstudio.gez.webapi.Api;
import spelstudio.gez.webapi.Controller;
import spelstudio.gez.webapi.Responses.GetFriendRsponse;
import spelstudio.gez.webapi.Responses.StatusResponse;
import spelstudio.gez.webapi.Responses.UserInfo;

public class FriendsProfileAcitivity extends AppCompatActivity {
    ProgressDialog mDialog;
    UserInfo mInfo;
    AlertDialog mAlertDialog;
    AlertDialog.Builder mBuilder;
    ImageButton mDelFrndBtn;
    String friendId;
    ImageView mProfileImg, mUserRankImage, mFrndRankImage;
    TextView wonTxt, loseTxt, playTxt, frndsTxt, addGezTxt, emoti1Txt, emoti2Txt, emoti3Txt, emoti4Txt, emoti5Txt, emoti6Txt, userFrndWonTxt, userFrndPercentTxt, userFrndPlayTxt, frndUsrPlayTxt, frndUsrGoodTxt, frndUsrPercentTxt, frndName, profCompFrndName, frndLastSeenTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_profile_acitivity);
        onInitialize();
        setApiCall();
    }

    private void setApiCall() {
        RequestBody mRequest = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("UserGuid", mInfo.userGuid)
                .addFormDataPart("FriendID", friendId)
                .build();
        Api mApi = Controller.getApi();
        Call<GetFriendRsponse> mCall = mApi.getFriend(mRequest);
        mCall.enqueue(new Callback<GetFriendRsponse>() {
            @Override
            public void onResponse(Call<GetFriendRsponse> call, Response<GetFriendRsponse> response) {
                if (response != null) {
                    GetFriendRsponse mResp = new GetFriendRsponse();
                    mResp = response.body();
                    wonTxt.setText(mResp.getTotalGezGood());
                    loseTxt.setText(mResp.getTotalGezWrong());
                    addGezTxt.setText(mResp.getAddedGez());
                    playTxt.setText(mResp.getOpenGez());
                    frndsTxt.setText(mResp.getTotalFriends());
                    emoti1Txt.setText(Html.fromHtml(mResp.getEmo1() + "<small>%</small>"));
                    emoti2Txt.setText(Html.fromHtml(mResp.getEmo2() + "<small>%</small>"));
                    emoti3Txt.setText(Html.fromHtml(mResp.getEmo3() + "<small>%</small>"));
                    emoti4Txt.setText(Html.fromHtml(mResp.getEmo4() + "<small>%</small>"));
                    emoti5Txt.setText(Html.fromHtml(mResp.getEmo5() + "<small>%</small>"));
                    emoti6Txt.setText(Html.fromHtml(mResp.getEmo6() + "<small>%</small>"));

                    Glide.with(FriendsProfileAcitivity.this).load(mResp.getFriendPicUrl()).into(mProfileImg);

                    mDialog.cancel();
                    frndLastSeenTxt.setText(mResp.getLastActive().toString());
                    frndUsrPercentTxt.setText(mResp.getFriendUserPercentage());
                    frndUsrGoodTxt.setText(String.valueOf(mResp.getFriendUserGood()));
                    frndUsrPlayTxt.setText(Html.fromHtml(mResp.getFriendUserGezOpen() + "<small>/" + mResp.getTotalGez() + "</small>"));
                    userFrndPercentTxt.setText(mResp.getUserFriendPercentage());
                    userFrndWonTxt.setText(mResp.getUserFriendGood());
                    userFrndPlayTxt.setText(Html.fromHtml(mResp.getUserFriendGezOpen() + "<small>/" + mResp.getTotalGez() + "</small>"));
                    frndName.setText(mResp.getFriendName());
                    profCompFrndName.setText(mResp.getFriendName());
                    if (Integer.parseInt(mResp.getFriendUserGood()) > Integer.parseInt(mResp.getUserFriendGood())) {
                        frndUsrGoodTxt.setTextColor(getResources().getColor(R.color.colorTextGreen));
                        frndUsrGoodTxt.setTypeface(frndUsrGoodTxt.getTypeface(), Typeface.BOLD);
                        userFrndWonTxt.setTypeface(userFrndWonTxt.getTypeface(), Typeface.NORMAL);
                        userFrndWonTxt.setTextColor(getResources().getColor(R.color.colorTextRed));
                        mFrndRankImage.setVisibility(View.VISIBLE);
                        mUserRankImage.setVisibility(View.GONE);
                    } else {
                        userFrndWonTxt.setTextColor(getResources().getColor(R.color.colorTextGreen));
                        frndUsrGoodTxt.setTextColor(getResources().getColor(R.color.colorTextRed));
                        userFrndWonTxt.setTypeface(userFrndWonTxt.getTypeface(), Typeface.BOLD);
                        frndUsrGoodTxt.setTypeface(frndUsrGoodTxt.getTypeface(), Typeface.NORMAL);

                        mUserRankImage.setVisibility(View.VISIBLE);
                        mFrndRankImage.setVisibility(View.GONE);
                    }
                    try {
                        if (Float.parseFloat(mResp.getFriendUserPercentage().replace(",", ".")) > Float.parseFloat(mResp.getUserFriendPercentage().replace(",", "."))) {
                            frndUsrPercentTxt.setTextColor(getResources().getColor(R.color.colorTextGreen));
                            userFrndPercentTxt.setTextColor(getResources().getColor(R.color.colorTextRed));
                            frndUsrPercentTxt.setTypeface(frndUsrPercentTxt.getTypeface(), Typeface.BOLD);
                            userFrndPercentTxt.setTypeface(userFrndPercentTxt.getTypeface(), Typeface.NORMAL);

                        } else {
                            userFrndPercentTxt.setTextColor(getResources().getColor(R.color.colorTextGreen));
                            frndUsrPercentTxt.setTextColor(getResources().getColor(R.color.colorTextRed));
                            userFrndPercentTxt.setTypeface(userFrndPercentTxt.getTypeface(), Typeface.BOLD);
                            frndUsrPercentTxt.setTypeface(frndUsrPercentTxt.getTypeface(), Typeface.NORMAL);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<GetFriendRsponse> call, Throwable t) {
                t.printStackTrace();
                mDialog.cancel();
            }
        });
        ImageButton mRankAddBtn = (ImageButton) findViewById(R.id.frnd_prof_add_gez_img);
        Glide.with(this).load(R.drawable.btn_add_small).into(mRankAddBtn);

    }

    private void onInitialize() {
        mDelFrndBtn = (ImageButton) findViewById(R.id.frnd_prof_delete_btn);
        frndsTxt = (TextView) findViewById(R.id.frnd_prof_frnd_txt);
        loseTxt = (TextView) findViewById(R.id.frnd_prof_lose_txt);
        playTxt = (TextView) findViewById(R.id.frnd_prof_play_txt);
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Loading");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        wonTxt = (TextView) findViewById(R.id.frnd_prof_won_txt);
        addGezTxt = (TextView) findViewById(R.id.frnd_prof_add_gez_txt);
        mProfileImg = (ImageView) findViewById(R.id.frnd_prof_img);
        mUserRankImage = (ImageView) findViewById(R.id.prof2_usr_rank);
        mFrndRankImage = (ImageView) findViewById(R.id.prof2_frnd_rank_img);
        frndName = (TextView) findViewById(R.id.frnd_name_txt);
        profCompFrndName = (TextView) findViewById(R.id.prof_rank_frnd_name);
        emoti1Txt = (TextView) findViewById(R.id.prof_frnd_emoji_1_txt);
        emoti2Txt = (TextView) findViewById(R.id.prof_frnd_emoji_2_txt);
        emoti3Txt = (TextView) findViewById(R.id.prof_frnd_emoji_3_txt);
        emoti4Txt = (TextView) findViewById(R.id.prof_frnd_emoji_4_txt);
        emoti5Txt = (TextView) findViewById(R.id.prof_frnd_emoji_5_txt);
        emoti6Txt = (TextView) findViewById(R.id.prof_frnd_emoji_6_txt);
        frndLastSeenTxt = (TextView) findViewById(R.id.frnd_last_seen_txt);
        frndUsrPercentTxt = (TextView) findViewById(R.id.prof_rnk_frnd_prcnt_txt);
        frndUsrGoodTxt = (TextView) findViewById(R.id.prof_rnk_frnd_won_txt);
        frndUsrPlayTxt = (TextView) findViewById(R.id.prof_rnk_frnd_play_txt);
        userFrndPercentTxt = (TextView) findViewById(R.id.prof_rnk_usr_prcnt_txt);
        userFrndPlayTxt = (TextView) findViewById(R.id.prof_rnk_usr_play_txt);
        userFrndWonTxt = (TextView) findViewById(R.id.prof_rnk_usr_won_txt);
        SharedPrefrenceHelper mHelper = new SharedPrefrenceHelper(getApplicationContext());
        mInfo = new UserInfo();
        mInfo = mHelper.getSavedUser();
        friendId = getIntent().getExtras().getString("friendId");
        mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Warning");
        mBuilder.setMessage("Do really want to delete your friend");
        mBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                mDialog.show();
                deleteFriend(friendId, mInfo.userGuid);
            }
        });
        mBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        mAlertDialog = mBuilder.create();
        mDelFrndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlertDialog.show();
            }
        });
    }

    private void deleteFriend(String friendId, String userGuid) {
        RequestBody mBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("UserGuid", userGuid)
                .addFormDataPart("FriendID", friendId)
                .build();
        Api mApi = Controller.getApi();
        Call<StatusResponse> mCall = mApi.delFriend(mBody);
        mCall.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                mDialog.cancel();
                if (response.body().getErrNum().trim().equalsIgnoreCase("200")) {
                    Toast.makeText(FriendsProfileAcitivity.this, "Successfully Deleted", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(FriendsProfileAcitivity.this, "Failed to delete", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                Toast.makeText(FriendsProfileAcitivity.this, "Failed to delete", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

    public void onFrndProfBackBtnClicked(View mView) {
        finish();
    }
}
