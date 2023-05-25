package spelstudio.gez.activities;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import spelstudio.gez.R;

public class AboutGezActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_gez);

        View fb_btn = findViewById(R.id.fb_btn);
        View privacy_btn = findViewById(R.id.privacy_btn);
        View website_btn = findViewById(R.id.website_btn);

        fb_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facebook_like();
            }
        });
        privacy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBrowser("http://www.gez.app/privacy");
            }
        });
        website_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBrowser("http://www.gez.app");
            }
        });
    }

    private void openBrowser(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    public void onMenuBackClicked(View mView) {
        finish();
    }

    public void facebook_like() {
        try {
            Intent intent = newFacebookIntent("https://www.facebook.com/Gez.now");
            startActivity(intent);
        } catch (Exception e) {
        }
    }

    public Intent newFacebookIntent(String url) {
        Uri uri = Uri.parse(url);
        try {
            ApplicationInfo applicationInfo = this.getPackageManager().getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                uri = Uri.parse("fb://facewebmodal/f?href=" + url);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return new Intent(Intent.ACTION_VIEW, uri);
    }
}
