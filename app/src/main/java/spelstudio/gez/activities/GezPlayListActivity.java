package spelstudio.gez.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.api.services.youtube.YouTube;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import spelstudio.gez.Helpers.SharedPrefrenceHelper;
import spelstudio.gez.R;
import spelstudio.gez.adapters.PlayListRecyclerAdapter;
import spelstudio.gez.adapters.RankRecyclerAdapter;
import spelstudio.gez.interfaces.PlayListItemClickListener;
import spelstudio.gez.webapi.Api;
import spelstudio.gez.webapi.Controller;
import spelstudio.gez.webapi.Responses.GUIDRequest;
import spelstudio.gez.webapi.Responses.GetGezListResponse;
import spelstudio.gez.webapi.Responses.UserInfo;

public class GezPlayListActivity extends YouTubeBaseActivity implements PlayListItemClickListener {
    RecyclerView mRecyclerView;
    EditText editText;
    //if archive list is shown then mIsArchvLst is true else false
    boolean mIsArchvLst = false;
    UserInfo mInfo;
    TextView mTitleTxt;
    ImageButton mPlBackBtn, mPlArchvLstBtn;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gez_playlist);
        onInitialize();
        callApi();
    }

    private void callApi() {
        mProgressDialog.show();
        Api mApi = Controller.getApi();
        RequestBody mRequest = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("UserGuid", mInfo.userGuid)
                .build();
        Call<GetGezListResponse> respCall = mApi.getGezList(mRequest);
        respCall.enqueue(new Callback<GetGezListResponse>() {
            @Override
            public void onResponse(Call<GetGezListResponse> call, Response<GetGezListResponse> response) {
                mProgressDialog.dismiss();
                mIsArchvLst = false;
                if (response != null) {
                    GetGezListResponse mResp = response.body();
                    if (!mResp.errNum.trim().equalsIgnoreCase("99")) {
                        mRecyclerView.setAdapter(new PlayListRecyclerAdapter(mResp.arrayGez, GezPlayListActivity.this, GezPlayListActivity.this, mIsArchvLst));
                    } else {
                        Toast.makeText(GezPlayListActivity.this, "msg-99", Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<GetGezListResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                t.printStackTrace();
                Toast.makeText(GezPlayListActivity.this, "failed", Toast.LENGTH_LONG);
            }
        });
    }

    private void onInitialize() {
        mTitleTxt = (TextView) findViewById(R.id.playlist_title_txt);
        mPlBackBtn = (ImageButton) findViewById(R.id.playlist_home_btn);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading");
        mPlArchvLstBtn = (ImageButton) findViewById(R.id.playlist_archv_btn);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mRecyclerView = (RecyclerView) findViewById(R.id.playlist_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPlBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mInfo = new UserInfo();
        SharedPrefrenceHelper mHelper = new SharedPrefrenceHelper(getApplicationContext());
        mInfo = mHelper.getSavedUser();
        ImageButton mRankAddBtn = (ImageButton) findViewById(R.id.rank_add_gez_img);
        mProgressDialog.show();
        mPlArchvLstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mIsArchvLst) {
                    mIsArchvLst = true;
                    getArchiveList();
                    mPlArchvLstBtn.setVisibility(View.GONE);
                } else {
                    mIsArchvLst = false;
                    mPlArchvLstBtn.setVisibility(View.VISIBLE);
                    callApi();
                }
            }
        });
    }

    @Override
    public void onFooterClicked() {
        finish();
        startActivity(new Intent(this, SearchGezActivity.class));
    }

    @Override
    public void onItemClicked(String gezId) {
        startActivity(new Intent(GezPlayListActivity.this, PlayActivity.class).putExtra("gezId", gezId));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void getArchiveList() {
        mProgressDialog.show();
        Api mApi = Controller.getApi();
        RequestBody mRequest = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("UserGuid", mInfo.userGuid)
                .build();
        Call<GetGezListResponse> respCall = mApi.getGezListArchive(mRequest);
        respCall.enqueue(new Callback<GetGezListResponse>() {
            @Override
            public void onResponse(Call<GetGezListResponse> call, Response<GetGezListResponse> response) {
                mProgressDialog.cancel();
                if (response != null) {
                    GetGezListResponse mResp = response.body();
                    if (!mResp.errNum.trim().equalsIgnoreCase("99")) {
                        mRecyclerView.setAdapter(new PlayListRecyclerAdapter(mResp.arrayGez, GezPlayListActivity.this, GezPlayListActivity.this, mIsArchvLst));
                        mTitleTxt.setText("Gez Archive");
                    } else {
                        Toast.makeText(GezPlayListActivity.this, "msg-99", Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<GetGezListResponse> call, Throwable t) {
                mProgressDialog.cancel();
                t.printStackTrace();
                Toast.makeText(GezPlayListActivity.this, "failed", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (mPlArchvLstBtn.getVisibility() == View.VISIBLE)
            super.onBackPressed();
        else {
            mRecyclerView.setAdapter(null);
            callApi();
            mTitleTxt.setText("Gez Playlist");
            mPlArchvLstBtn.setVisibility(View.VISIBLE);
        }

    }
}
