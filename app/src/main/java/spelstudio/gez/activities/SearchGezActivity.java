package spelstudio.gez.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeBaseActivity;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import spelstudio.gez.Helpers.SharedPrefrenceHelper;
import spelstudio.gez.Helpers.YoutubeConnector;
import spelstudio.gez.Helpers.YoutubeVideoItem;
import spelstudio.gez.R;
import spelstudio.gez.adapters.AddGezRecyclerAdapter;
import spelstudio.gez.adapters.YoutubeSearchVidAdapter;
import spelstudio.gez.interfaces.SearchAddGezClickListener;
import spelstudio.gez.webapi.Api;
import spelstudio.gez.webapi.Controller;
import spelstudio.gez.webapi.Responses.GetPromoVidzResponse;
import spelstudio.gez.webapi.Responses.UserInfo;


public class SearchGezActivity extends YouTubeBaseActivity implements SearchAddGezClickListener, AddGezRecyclerAdapter.AddGezRecItemClickListener {
    RecyclerView mRecyclerView;
    ImageButton mAddGezBtn, mSearchBtn, mResetSearchBtn;
    EditText searchViewTxt;
    View ivCross;
    ProgressDialog mProgressDialog;
    private List<YoutubeVideoItem> searchResults;
    UserInfo mInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_gez);
        mRecyclerView = (RecyclerView) findViewById(R.id.add_gez_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchViewTxt = (EditText) findViewById(R.id.search_view_gez);
        mSearchBtn = (ImageButton) findViewById(R.id.search_gez_img);
        ivCross = findViewById(R.id.ivCross);
        ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchViewTxt.setText("");
            }
        });
        mInfo = new UserInfo();
        mResetSearchBtn = (ImageButton) findViewById(R.id.add_gez_reset_btn);
        searchViewTxt.setFocusable(false);
        searchViewTxt.setFocusableInTouchMode(true);
        mResetSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchViewTxt.setText("");
                //todo ask sir and change focus
//                searchViewTxt.setFocus
            }
        });
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchViewTxt.getText().toString() != null)
                    mProgressDialog.show();
                searchOnYoutube(searchViewTxt.getText().toString());
            }
        });
        searchViewTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b == true) {
                    mResetSearchBtn.setVisibility(View.VISIBLE);
                } else {
                    mResetSearchBtn.setVisibility(View.GONE);
                }
            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        SharedPrefrenceHelper mHelper = new SharedPrefrenceHelper(getApplicationContext());
        mInfo = mHelper.getSavedUser();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading");
        mProgressDialog.show();
        mProgressDialog.setCanceledOnTouchOutside(false);
        //TODO get user guid from prefrences
        RequestBody mRequest = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("UserGuid", mInfo.userGuid)
                .build();
        Api mApi = Controller.getApi();
        Call<GetPromoVidzResponse> mCall = mApi.getPromoVids(mRequest);
        mCall.enqueue(new Callback<GetPromoVidzResponse>() {
            @Override
            public void onResponse(Call<GetPromoVidzResponse> call, Response<GetPromoVidzResponse> response) {
                mProgressDialog.cancel();
                if (response != null && response.isSuccessful()) {
                    GetPromoVidzResponse mResp = new GetPromoVidzResponse();
                    mResp = response.body();
                    mRecyclerView.setAdapter(new AddGezRecyclerAdapter(mResp.arrayVideos, SearchGezActivity.this, SearchGezActivity.this, SearchGezActivity.this));
                }
            }

            @Override
            public void onFailure(Call<GetPromoVidzResponse> call, Throwable t) {
                mProgressDialog.cancel();
                t.printStackTrace();
            }
        });
        searchViewTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                searchViewTxt.setFocusable(false);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                searchViewTxt.setFocusableInTouchMode(true);
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    searchOnYoutube(searchViewTxt.getText().toString());
                    mProgressDialog.show();
                    return true;
                }
                return false;
            }
        });

    }

    private void searchOnYoutube(final String string) {
        hideKeyboard();
        final Handler handler = new Handler();
        new Thread() {
            public void run() {
                YoutubeConnector yc = new YoutubeConnector(SearchGezActivity.this);
                searchResults = yc.search(string);
                handler.post(new Runnable() {
                    public void run() {
                        mRecyclerView.setAdapter(new YoutubeSearchVidAdapter(searchResults, SearchGezActivity.this, SearchGezActivity.this, SearchGezActivity.this));
                        mProgressDialog.cancel();
                    }
                });
            }
        }.start();
    }

    public void AddGezHomeButtonClicked(View mView) {
        finish();
    }

    public void AddGezBtnClicked(View mView) {
        startActivity(new Intent(this, AddGezActivity.class));
    }

    @Override
    public void onClicked() {
        startActivity(new Intent(this, AddGezActivity.class));
    }

    public void onSearchGezBackBtnClicked(View mView) {
        finish();
        startActivity(new Intent(this, MenuActivity.class));
    }

    @Override
    public void onVideoClicked(String mVideoName, String mVideoOwner, String mVideoUrl) {
        Intent mIntent = new Intent(SearchGezActivity.this, AddGezActivity.class);
        mIntent.putExtra("videoName", mVideoName);
        mIntent.putExtra("videoOwner", mVideoOwner);
        mIntent.putExtra("videoUrl", mVideoUrl);
        startActivity(mIntent);

    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
