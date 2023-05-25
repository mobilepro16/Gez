package spelstudio.gez.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import spelstudio.gez.Helpers.SharedPrefrenceHelper;
import spelstudio.gez.Helpers.UrlToVideoIdConverter;
import spelstudio.gez.R;
import spelstudio.gez.adapters.GezDoneRecyclerAdapter;
import spelstudio.gez.adapters.GezOpenRecyclerAdapter;
import spelstudio.gez.constants.Constants;
import spelstudio.gez.interfaces.FriendListItemClickListener;
import spelstudio.gez.interfaces.RecyclerFooterItemClickListener;
import spelstudio.gez.interfaces.RecyclerViewPositionChangeListener;
import spelstudio.gez.webapi.Api;
import spelstudio.gez.webapi.Controller;
import spelstudio.gez.webapi.Responses.ArrayFriendsDone;
import spelstudio.gez.webapi.Responses.ArrayFriendsOpen;
import spelstudio.gez.webapi.Responses.GetGezResponse;
import spelstudio.gez.webapi.Responses.SetGezFriendResponse;
import spelstudio.gez.webapi.Responses.StatusResponse;
import spelstudio.gez.webapi.Responses.UserInfo;

import static spelstudio.gez.constants.Constants.RECOVERY_DIALOG_REQUEST_YOUTUBE_PLAYER;
import static spelstudio.gez.constants.Constants.VIDEO_ID;
import static spelstudio.gez.constants.Constants.YOUTUBE_DEVELOPER_KEY;

public class PlayActivity extends YouTubeBaseActivity implements FriendListItemClickListener, GezDoneRecyclerAdapter.GezDoneEmojiSelectedListener,
        RecyclerFooterItemClickListener, RadioGroup.OnCheckedChangeListener, RecyclerViewPositionChangeListener,
        YouTubePlayer.OnInitializedListener, YouTubePlayer.PlaybackEventListener, YouTubePlayer.PlayerStateChangeListener, GezOpenRecyclerAdapter.GezOpenEmojiSelectedListener {
    YouTubePlayerView mPlayer;
    RecyclerView mRecyclerView;
    String gezId;

    List<ArrayFriendsDone> doneFriends;
    List<ArrayFriendsOpen> openFriends;

    Task mTask;
    public static int GezOpenEmojiCounter = 0;
    public static int GezDoneEmojiCounter = 0;
    GezOpenRecyclerAdapter mOpenAdapter;
    GezDoneRecyclerAdapter mDoneAdapter;
    UserInfo mInfo;
    RadioGroup gezTabs;
    RadioButton gezDoneTab, gezOpenTab;
    FrameLayout fl_player;
    GezDoneRecyclerAdapter mRecyclerAdapter;
    RadioGroup mRadioGroup;
    YouTubePlayer.OnInitializedListener mInitializeListener;
    SharedPrefrenceHelper mHelper;
    YouTubePlayer mYoutubePlayer;
    GetGezResponse mGezResponse;
    TextView mVideNameTxt, mVideoAddedByTxt;
    ProgressDialog mProgressDialog;
    TabLayout tabLayout;
    ViewPager viewPager;
    int friendsCount = 0;
    ImageButton mListBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        onInitialize();
        setApiCall();
        mListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mYoutubePlayer != null) {
                    mYoutubePlayer.pause();
                }
                startActivity(new Intent(PlayActivity.this, GezPlayListActivity.class));
            }
        });
    }

    private void setApiCall() {
        mProgressDialog.show();
        //todo set call with user from shared preferences
        Api mApi = Controller.getApi();
        RequestBody mBody = new MultipartBody.Builder().
                setType(MultipartBody.FORM).
                addFormDataPart("UserGuid", mInfo.userGuid).
                addFormDataPart("GezID", gezId).
                build();
        final Call<GetGezResponse> mResp = mApi.getGez(mBody);
        mResp.enqueue(new Callback<GetGezResponse>() {
            @Override
            public void onResponse(Call<GetGezResponse> call, Response<GetGezResponse> response) {
                mProgressDialog.cancel();
                if (response.body() != null) {
                    mTask = new Task();
                    mTask.execute();

                    GetGezResponse mGezResponse = response.body();
                    PlayActivity.this.mGezResponse = mGezResponse;

                    if (mGezResponse != null && mGezResponse.getVideoName() != null) {
                        mVideNameTxt.setText(mGezResponse.getVideoName());
                    } else {
                        startActivity(new Intent(PlayActivity.this, GezPlayListActivity.class));
                        return;
                    }
                    if (mYoutubePlayer != null) {
                        mYoutubePlayer.pause();
                    }

                    mVideoAddedByTxt.setText("Added by " + mGezResponse.getGezAddedBy());
                    int videoPlayedFlag = Integer.parseInt(mGezResponse.getGezPlayed().trim());
                    if (videoPlayedFlag == 1) {
                        setSelected(mRadioGroup, Integer.valueOf(mGezResponse.getGezSelected()) - 1, false);
                        mRadioGroup.getChildAt(Integer.valueOf(mGezResponse.getGezSelected())).setSelected(true);
                        mRadioGroup.setOnCheckedChangeListener(null);
                    } else {
                        mRadioGroup.setOnCheckedChangeListener(PlayActivity.this);
                    }
                    UrlToVideoIdConverter mConv = new UrlToVideoIdConverter();
                    String videoId = mConv.videoId(mGezResponse.getVideoUrl());
                    onInitializePlayer(videoId);
                    doneFriends = mGezResponse.getArrayFriendsDone();
                    mDoneAdapter = new GezDoneRecyclerAdapter(PlayActivity.this, doneFriends, PlayActivity.this, PlayActivity.this);
                    mDoneAdapter.setEmojiSelectedListener(PlayActivity.this);
                    mDoneAdapter.setFrndItemClickListener(PlayActivity.this);

                    openFriends = mGezResponse.getArrayFriendsOpen();

                    mOpenAdapter = new GezOpenRecyclerAdapter(PlayActivity.this, openFriends, PlayActivity.this, PlayActivity.this, PlayActivity.this);
                    if (mGezResponse.getArrayFriendsDone().size() > 0) {
                        mRecyclerView.setAdapter(mDoneAdapter);
                        gezDoneTab.setChecked(true);
                    } else {
                        mRecyclerView.setAdapter(mOpenAdapter);
                        gezOpenTab.setChecked(true);
                    }
                    mOpenAdapter.setmEmojiSelectedListener(PlayActivity.this);
                    gezDoneTab.setText(mGezResponse.getTotalFriendsDoneRated() + "/" + mGezResponse.getTotalFriendsDone());
                    gezOpenTab.setText(mGezResponse.getTotalFriendsOpenRated() + "/" + mGezResponse.getTotalFriendsOpen());


                }
//                mGezResponse.
            }

            @Override
            public void onFailure(Call<GetGezResponse> call, Throwable t) {
                mProgressDialog.cancel();
                Toast.makeText(PlayActivity.this, "error", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });

    }

    private void onInitialize() {
        mInfo = new UserInfo();
        gezTabs = (RadioGroup) findViewById(R.id.tabs_play);
        gezDoneTab = (RadioButton) findViewById(R.id.radio_gez_done);
        gezOpenTab = (RadioButton) findViewById(R.id.radio_gez_open);
        mListBtn = (ImageButton) findViewById(R.id.pl_list_btn);
        gezTabs.setOnCheckedChangeListener(this);
        SharedPrefrenceHelper mHelper = new SharedPrefrenceHelper(getApplicationContext());
        mInfo = mHelper.getSavedUser();
        if (getIntent().getExtras() == null || getIntent().getExtras().getString("gezId") == null) {
            gezId = "0";
        } else {
            gezId = getIntent().getExtras().getString("gezId");
        }
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading");
        mProgressDialog.setCanceledOnTouchOutside(false);
//            mInfo = mHelper.getSavedUser();
//            gezId = mInfo.userGuid;
        mVideNameTxt = (TextView) findViewById(R.id.video_name_txt);
        mVideoAddedByTxt = (TextView) findViewById(R.id.video_added_by_txt);
        mRadioGroup = (RadioGroup) findViewById(R.id.radiogroup_user_rating);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_green);
        RecyclerView.LayoutManager mManager = new LinearLayoutManager(PlayActivity.this);
        mRecyclerView.setLayoutManager(mManager);
        fl_player = findViewById(R.id.fl_player);
        mPlayer = new YouTubePlayerView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        fl_player.addView(mPlayer, params); //todo remove this function call after demo testing
    }

    public void onInitializePlayer(final String url) {
        //todo delete after demo testing
        mInitializeListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.setPlaybackEventListener(PlayActivity.this);
                youTubePlayer.setPlayerStateChangeListener(PlayActivity.this);
                youTubePlayer.loadVideo(url);
                mYoutubePlayer = youTubePlayer;
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
            }
        };
        mPlayer.initialize(Constants.YOUTUBE_DEVELOPER_KEY, mInitializeListener);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.rate_emoji_1: {
                setSelected(radioGroup, 0);

                break;
            }
            case R.id.rate_emoji_2: {
                setSelected(radioGroup, 1);
                break;
            }

            case R.id.rate_emoji_3: {
                setSelected(radioGroup, 2);
                break;
            }
            case R.id.rate_emoji_4: {
                setSelected(radioGroup, 3);
                break;
            }
            case R.id.rate_emoji_5: {
                setSelected(radioGroup, 4);
                break;
            }
            case R.id.rate_emoji_6: {
                setSelected(radioGroup, 5);
                break;
            }
            case R.id.radio_gez_done: {
                mRecyclerView.setAdapter(mDoneAdapter);
                break;
            }
            case R.id.radio_gez_open: {
                mRecyclerView.setAdapter(mOpenAdapter);
                break;
            }

        }
    }

    private void setSelected(RadioGroup radioGroup, int selectedChild, boolean... shouldCall) {
        for (int i = 0; i < 6; i++) {
            if (i != selectedChild) {
                radioGroup.getChildAt(i).setEnabled(false);
            }
        }

        if (shouldCall.length == 0)
            callSetGezUser(selectedChild);
    }

    public void onPlayBackBtnClicked(View mView) {
        finish();

        startActivity(new Intent(PlayActivity.this, MenuActivity.class));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RECOVERY_DIALOG_REQUEST_YOUTUBE_PLAYER) {
            getYoutubePlayerProvider().initialize(YOUTUBE_DEVELOPER_KEY, this);

        }
    }

    private YouTubePlayer.Provider getYoutubePlayerProvider() {
        return mPlayer;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            youTubePlayer.cueVideo(VIDEO_ID);
            youTubePlayer.pause();
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_DIALOG_REQUEST_YOUTUBE_PLAYER).show();
        } else {
            String errorMessage = String.format("Error initializing youtube", youTubeInitializationResult.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPlaying() {

    }

    @Override
    public void onPaused() {

    }

    @Override
    public void onStopped() {
    }

    @Override
    public void onBuffering(boolean b) {

    }

    @Override
    public void onSeekTo(int i) {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onLoaded(String s) {

    }

    @Override
    public void onAdStarted() {

    }

    @Override
    public void onVideoStarted() {
    }

    @Override
    public void onVideoEnded() {
        mRadioGroup.setOnCheckedChangeListener(PlayActivity.this);
    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {

    }

    @Override
    public void onPositionFound(int position) {
        mRecyclerView.scrollToPosition(position + 1);
    }

    @Override
    public void onFooterClicked() {
        startActivity(new Intent(PlayActivity.this, GezPlayListActivity.class));
    }


    @Override
    public void setGezDoneFriend(int userpoint, String friendId, int emojiSelected, int position) {
        callSetGez(userpoint, friendId, emojiSelected);
        gezDoneTab.setText((position + 1) + "/" + (mDoneAdapter.getItemCount() - 1));
        doneFriends.get(position).setUserSelected(String.valueOf(emojiSelected));
        GezDoneEmojiCounter++;
        if (position < mDoneAdapter.getItemCount() - 1) {
            mRecyclerView.scrollToPosition(position + 1);
            if (position == mOpenAdapter.getItemCount() - 2) {
                mRecyclerView.setAdapter(mOpenAdapter);
                gezOpenTab.setChecked(true);
            }
        } else {
            mRecyclerView.setAdapter(mOpenAdapter);
            gezOpenTab.setChecked(true);

        }
        if (GezOpenEmojiCounter == mOpenAdapter.getItemsSize() && GezDoneEmojiCounter == mDoneAdapter.getItemSize()) {
            gezId = "0";
            setApiCall();
        }
    }

    class Task extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
//            if(mDoneAdapter!=null&&mOpenAdapter!=null&&mDoneAdapter.getItemSize()==GezDoneEmojiCounter&&mOpenAdapter.getItemsSize()==GezOpenEmojiCounter){
            return GezDoneEmojiCounter + GezOpenEmojiCounter;
//            }

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Integer Flag) {
            super.onPostExecute(Flag);
            if (mDoneAdapter != null && mOpenAdapter != null && mDoneAdapter.getItemSize() + mOpenAdapter.getItemsSize() == Flag) {
                gezId = "0";
                GezOpenEmojiCounter = 0;
                GezDoneEmojiCounter = 0;
                setApiCall();
            }
        }
    }

    private void callSetGez(int userpoint, String friendId, int emojiSelected, final int position) {
        Api mApi = Controller.getApi();
        RequestBody mBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("GezID", mGezResponse.getGezID().toString())
                .addFormDataPart("UserGuid", mInfo.userGuid)
                .addFormDataPart("FriendID", friendId)
                .addFormDataPart("UserSelected", String.valueOf(emojiSelected))
                .addFormDataPart("UserPoint", String.valueOf(userpoint))
                .build();
        Call<SetGezFriendResponse> mRequest = mApi.setGezFriend(mBody);
        mRequest.enqueue(new Callback<SetGezFriendResponse>() {
            @Override
            public void onResponse(Call<SetGezFriendResponse> call, Response<SetGezFriendResponse> response) {
                if (response != null) {
                    if (friendsCount == mOpenAdapter.getItemsSize() + mDoneAdapter.getItemSize()) {
                        gezId = "0";
                        setApiCall();
                    }
                    SetGezFriendResponse mResp = new SetGezFriendResponse();
                    mResp = response.body();
                    if (mResp.getErrNum().trim().equalsIgnoreCase("99")) {
                        Toast.makeText(PlayActivity.this, "Error Uploading Your Selection", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SetGezFriendResponse> call, Throwable t) {
                Toast.makeText(PlayActivity.this, "Exception", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void callSetGez(int userpoint, String friendId, int emojiSelected) {
        mProgressDialog.show();
        Api mApi = Controller.getApi();
        RequestBody mBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("GezID", mGezResponse.getGezID().toString())
                .addFormDataPart("UserGuid", mInfo.userGuid)
                .addFormDataPart("FriendID", friendId)
                .addFormDataPart("UserSelected", String.valueOf(emojiSelected))
                .addFormDataPart("UserPoint", String.valueOf(userpoint))
                .build();
        Call<SetGezFriendResponse> mRequest = mApi.setGezFriend(mBody);
        mRequest.enqueue(new Callback<SetGezFriendResponse>() {
            @Override
            public void onResponse(Call<SetGezFriendResponse> call, Response<SetGezFriendResponse> response) {
                if (response != null) {
                    mProgressDialog.cancel();
                    SetGezFriendResponse mResp = new SetGezFriendResponse();
                    mResp = response.body();
                    if (mResp.getErrNum().trim().equalsIgnoreCase("99")) {
                        Toast.makeText(PlayActivity.this, "Error Uploading Your Selection", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SetGezFriendResponse> call, Throwable t) {
                mProgressDialog.cancel();
                Toast.makeText(PlayActivity.this, "Exception", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void callSetGezUser(int emojiSelected) {
        mProgressDialog.show();
        Api mApi = Controller.getApi();
        RequestBody mBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("GezID", mGezResponse.getGezID())
                .addFormDataPart("UserGuid", mInfo.userGuid)
                .addFormDataPart("UserSelected", String.valueOf(emojiSelected + 1))
                .build();
        Call<StatusResponse> mRequest = mApi.setGezUser(mBody);
        mRequest.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                if (response != null) {
                    mProgressDialog.dismiss();
                    StatusResponse mResp = new StatusResponse();
                    mResp = response.body();
                    if (mResp.getErrNum().trim().equalsIgnoreCase("99")) {
                        Toast.makeText(PlayActivity.this, "Error Uploading Your Selection", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(PlayActivity.this, "Exception", Toast.LENGTH_LONG).show();
            }
        });

    }


    @Override
    public void onFriendItemClicked(String friendId) {
        Intent intent = new Intent(PlayActivity.this, FriendsProfileAcitivity.class);
        intent.putExtra("friendId", friendId);
        finish();
        startActivity(intent);
    }

    @Override
    public void setGezOpenFriend(String friendId, int emojiSelected, int position) {
        gezOpenTab.setText((position + 1) + "/" + (mOpenAdapter.getItemCount() - 1));
        callSetGez(0, friendId, emojiSelected, position);

        openFriends.get(position).setUserSelected(emojiSelected);

        GezOpenEmojiCounter++;
        if (position < mOpenAdapter.getItemCount() - 1) {
            mRecyclerView.scrollToPosition(position + 1);

        }
        if (GezOpenEmojiCounter == mOpenAdapter.getItemsSize() && GezDoneEmojiCounter == mDoneAdapter.getItemSize()) {
            gezId = "0";
            setApiCall();
        }
    }
}
