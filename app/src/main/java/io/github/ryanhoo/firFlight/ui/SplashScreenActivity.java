package io.github.ryanhoo.firFlight.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.view.ViewPropertyAnimatorCompatSet;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.ryanhoo.firFlight.R;
import io.github.ryanhoo.firFlight.account.UserSession;
import io.github.ryanhoo.firFlight.ui.base.BaseActivity;
import io.github.ryanhoo.firFlight.ui.main.MainActivity;
import io.github.ryanhoo.firFlight.ui.signin.SignInActivity;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 3/15/16
 * Time: 8:27 PM
 * Desc: SplashScreenActivity
 */
public class SplashScreenActivity extends BaseActivity {

    private static final int REQUEST_SIGN_IN = 1;

    final long ANIMATION_DURATION = 1000;
    final long SHOW_SIGN_IN_DELAY = 1000;
    final long SIGNED_IN_DELAY = 2500;

    @Bind(R.id.text_view_app_name)
    TextView textViewAppName;
    @Bind(R.id.text_view_slogan)
    TextView textViewSlogan;

    @Bind(R.id.image_view_propeller)
    ImageView imageViewPropeller;

    Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);

        mHandler = new Handler();

        // Update User Token
        if (UserSession.getInstance().isSignedIn()) {
            // Main Activity
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    openMainActivity();
                }
            }, SIGNED_IN_DELAY);
        } else {
            // Sign In
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    openSignInActivity();
                }
            }, SHOW_SIGN_IN_DELAY);
        }

        // Animate UI in
        animateTextViews();
        animatePropeller();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_SIGN_IN) {
            openMainActivity();
        }
    }

    private void openMainActivity() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
        finish();
    }

    private void openSignInActivity() {
        startActivityForResult(new Intent(SplashScreenActivity.this, SignInActivity.class), REQUEST_SIGN_IN);
        SplashScreenActivity.this.overridePendingTransition(R.anim.slide_in_from_bottom, android.R.anim.fade_out);
    }

    // Animations

    private void animateTextViews() {
        final int TRANSLATE_Y = 100;
        textViewAppName.setAlpha(0f);
        textViewAppName.setTranslationY(TRANSLATE_Y);
        textViewSlogan.setAlpha(0f);
        ViewPropertyAnimatorCompat appNameAnimator = ViewCompat.animate(textViewAppName)
                .alpha(1)
                .setDuration(ANIMATION_DURATION)
                .translationYBy(-TRANSLATE_Y);
        ViewPropertyAnimatorCompat appSloganAnimator = ViewCompat.animate(textViewSlogan)
                .alpha(1)
                .setDuration(ANIMATION_DURATION);
        ViewPropertyAnimatorCompatSet animatorSet = new ViewPropertyAnimatorCompatSet();
        animatorSet.playSequentially(appNameAnimator, appSloganAnimator);
    }

    private void animatePropeller() {
        imageViewPropeller.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_rotate_propeller));
    }
}
