package spelstudio.gez.activities;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import spelstudio.gez.Helpers.SharedPrefrenceHelper;
import spelstudio.gez.R;
import spelstudio.gez.adapters.FriendsListAdapter;
import spelstudio.gez.interfaces.FriendListItemClickListener;
import spelstudio.gez.interfaces.RecyclerFooterItemClickListener;
import spelstudio.gez.webapi.Api;
import spelstudio.gez.webapi.Controller;
import spelstudio.gez.webapi.Responses.GetFriendsListResponse;
import spelstudio.gez.webapi.Responses.UserInfo;

public class FriendsActivity extends AppCompatActivity implements FriendListItemClickListener, RecyclerFooterItemClickListener {
    RecyclerView mRecyclerView;
    ProgressDialog mDialog;
    String mInviteCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        setViews();
    }

    private void setViews() {
        mDialog = new ProgressDialog(this);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setMessage("Loading");
        mDialog.show();
        mRecyclerView = (RecyclerView) findViewById(R.id.friends_recycler_view);
        RecyclerView.LayoutManager mManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mManager);
        SharedPrefrenceHelper mHelper = new SharedPrefrenceHelper(getApplicationContext());
        UserInfo mInfo = new UserInfo();
        mInfo = mHelper.getSavedUser();
        List<String> demo = new ArrayList<String>();
        Api mApi = Controller.getApi();
        RequestBody mBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("UserGuid", mInfo.userGuid)
                .addFormDataPart("InviteCode", "0")
                .build();
        retrofit2.Call<GetFriendsListResponse> mCall = mApi.getFriendsList(mBody);
        mCall.enqueue(new retrofit2.Callback<GetFriendsListResponse>() {
            @Override
            public void onResponse(retrofit2.Call<GetFriendsListResponse> call, retrofit2.Response<GetFriendsListResponse> response) {
                mDialog.cancel();
                if (response.body().getErrNum().equalsIgnoreCase("200")) {
                    if (response.body().getArrayFriends() != null && response.body().getArrayFriends().size() > 0) {
                        mRecyclerView.setAdapter(new FriendsListAdapter(response.body().getArrayFriends(), FriendsActivity.this, FriendsActivity.this, FriendsActivity.this));
                        if (response.body().getInviteCode() != null) {
                            mInviteCode = response.body().getInviteCode();
                        }
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<GetFriendsListResponse> call, Throwable t) {
                mDialog.cancel();

            }
        });
    }

    public void menuBackBtnFriends(View mView) {
        finish();
    }

    private void initiatePopupWindow() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidth = displayMetrics.widthPixels;
        int deviceHeight = displayMetrics.heightPixels;
// We need to get the instance of the LayoutInflater
        LayoutInflater inflater = (LayoutInflater) FriendsActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.add_friend_popup_layout,
                (ViewGroup) findViewById(R.id.popup_element));
        final PopupWindow pwindo;
        pwindo = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        pwindo.setFocusable(true);
        pwindo.setOutsideTouchable(false);
        pwindo.showAtLocation(layout, Gravity.BOTTOM, 0, 15);
        View container = (View) pwindo.getContentView().getParent();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.7f;
        wm.updateViewLayout(container, p);
        TextView mCancelTxt = (TextView) layout.findViewById(R.id.popup_cancel_txt);
        final TextView mSendEmail = (TextView) layout.findViewById(R.id.popup_email_txt);
        TextView mSendSms = (TextView) layout.findViewById(R.id.popup_sms_txt);
        TextView mCopyLink = (TextView) layout.findViewById(R.id.popup_copylnk_txt);
        mCancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pwindo.dismiss();
            }
        });
        mSendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInviteCode != null) {
                    Uri SMS_URI = Uri.parse("smsto:"); //Replace the phone number
                    Intent sms = new Intent(Intent.ACTION_VIEW, SMS_URI);
                    sms.putExtra("sms_body", "This is test message"); //Replace the message witha a vairable
                    startActivity(sms);
                }
            }
        });
        mSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInviteCode != null) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/html");
                    intent.putExtra(Intent.EXTRA_EMAIL, "");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Invite Code Gez");
                    intent.putExtra(Intent.EXTRA_TEXT, mInviteCode);
                    startActivity(Intent.createChooser(intent, "Send Email"));
                }
            }
        });
        mCopyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInviteCode != null) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label", mInviteCode);
                    if (clipboard == null) return;
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(FriendsActivity.this, "Copied", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    public void onFrndBackBtnClicked(View mView) {
        finish();
    }


    @Override
    public void onFriendItemClicked(String s) {
        startActivity(new Intent(this, FriendsProfileAcitivity.class).putExtra("friendId", s));
    }

    @Override
    public void onFooterClicked() {
        //initiatePopupWindow();

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey, I want to invite you to be my friend in the Gez app. Click on this link to start.\nhttps://gez.app/friend/?invitecode=" + mInviteCode);
        sendIntent.setType("*/*");
        startActivity(sendIntent);
    }
}
