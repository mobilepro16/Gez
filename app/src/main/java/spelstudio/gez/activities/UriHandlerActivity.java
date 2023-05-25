package spelstudio.gez.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import spelstudio.gez.Helpers.SharedPrefrenceHelper;
import spelstudio.gez.R;
import spelstudio.gez.constants.Constants;

public class UriHandlerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = getIntent();
                if (Intent.ACTION_VIEW.equals(intent.getAction())) {
                    Uri uri = intent.getData();
                    Constants.inviteCode = uri.getQueryParameter("invitecode");
                } else {
                    Constants.inviteCode = "";
                }
                SharedPrefrenceHelper mHelper = new SharedPrefrenceHelper(getApplicationContext());
                if (mHelper.getSavedUser() != null) {
                    finish();
                    startActivity(new Intent(UriHandlerActivity.this, MenuActivity.class));
                } else {
                    finish();
                    startActivity(new Intent(UriHandlerActivity.this, LoginActivity.class));
                }
            }
        }, 2000);
    }
}
