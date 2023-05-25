package spelstudio.gez.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import spelstudio.gez.Helpers.SharedPrefrenceHelper;
import spelstudio.gez.R;
import spelstudio.gez.adapters.RankRecyclerAdapter;
import spelstudio.gez.webapi.Api;
import spelstudio.gez.webapi.Controller;
import spelstudio.gez.webapi.Responses.UserInfo;
import spelstudio.gez.webapi.Responses.rankingresponse.GetRankingResponse;

public class RankActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    ProgressDialog mProgressDialog;
    TextView friendsTxt, playTxt, wonTxt, lostTxt, addGezTxt, emoti1Txt, emoti2Txt, emoti3Txt, emoti4Txt, emoti5Txt, emoti6Txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading");
        mProgressDialog.setCanceledOnTouchOutside(false);
        friendsTxt = (TextView) findViewById(R.id.rank_frnd_txt);
        wonTxt = (TextView) findViewById(R.id.rank_won_txt);
        lostTxt = (TextView) findViewById(R.id.rank_lose_txt);
        playTxt = (TextView) findViewById(R.id.rank_play_txt);
        addGezTxt = (TextView) findViewById(R.id.rank_add_gez_txt);
        emoti1Txt = (TextView) findViewById(R.id.rank_emoji_1_txt);
        emoti2Txt = (TextView) findViewById(R.id.rank_emoji_2_txt);
        emoti3Txt = (TextView) findViewById(R.id.rank_emoji_3_txt);
        emoti4Txt = (TextView) findViewById(R.id.rank_emoji_4_txt);
        emoti5Txt = (TextView) findViewById(R.id.rank_emoji_5_txt);
        emoti6Txt = (TextView) findViewById(R.id.rank_emoji_6_txt);
        mRecyclerView = (RecyclerView) findViewById(R.id.rank_recycler_view);
        Api mApi = Controller.getApi();
        SharedPrefrenceHelper mHelper = new SharedPrefrenceHelper(getApplicationContext());
        UserInfo mInfo = new UserInfo();
        mInfo = mHelper.getSavedUser();
        RequestBody mBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("UserGuid", mInfo.userGuid.toString().trim())
                .build();
        mProgressDialog.show();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Call<GetRankingResponse> mCall = mApi.getRanking(mBody);
        mCall.enqueue(new Callback<GetRankingResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<GetRankingResponse> call, Response<GetRankingResponse> response) {
                mProgressDialog.cancel();
                GetRankingResponse obj = response.body();
                if (obj != null && obj.getErrNum().equalsIgnoreCase("200")) {
                    friendsTxt.setText(obj.getTotalFriends());
                    wonTxt.setText(obj.getTotalGezGood());
                    lostTxt.setText(obj.getTotalGezWrong());
                    playTxt.setText(obj.getOpenGez());
                    emoti1Txt.setText(Html.fromHtml(obj.getEmo1() + "<small>%</small>"));
                    emoti2Txt.setText(Html.fromHtml(obj.getEmo2() + "<small>%</small>"));
                    emoti3Txt.setText(Html.fromHtml(obj.getEmo3() + "<small>%</small>"));
                    emoti4Txt.setText(Html.fromHtml(obj.getEmo4() + "<small>%</small>"));
                    emoti5Txt.setText(Html.fromHtml(obj.getEmo5() + "<small>%</small>"));
                    emoti6Txt.setText(Html.fromHtml(obj.getEmo6() + "<small>%</small>"));
                    addGezTxt.setText(obj.getAddedGez());
                    mRecyclerView.setAdapter(new RankRecyclerAdapter(obj.getArrayFriends(), RankActivity.this));

                }
            }

            @Override
            public void onFailure(Call<GetRankingResponse> call, Throwable t) {
                t.printStackTrace();
                mProgressDialog.cancel();
            }
        });

        ImageButton mRankAddBtn = (ImageButton) findViewById(R.id.rank_add_gez_img);
        Glide.with(this).load(R.drawable.btn_add_small).into(mRankAddBtn);
    }

    public void onRankBackBtnClicked(View mView) {
        finish();
        startActivity(new Intent(this, MenuActivity.class));
    }
}
