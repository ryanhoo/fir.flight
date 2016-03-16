package io.github.ryanhoo.firFlight.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.view.ViewPropertyAnimatorCompatSet;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.ryanhoo.firFlight.R;
import io.github.ryanhoo.firFlight.data.UserSession;
import io.github.ryanhoo.firFlight.ui.base.BaseActivity;
import io.github.ryanhoo.firFlight.ui.main.MainActivity;
import io.github.ryanhoo.firFlight.ui.signin.SignInFragment;

/**
 * Created with Android Studio.
 * User: ryan@whitedew.me
 * Date: 3/15/16
 * Time: 8:27 PM
 * Desc: SplashScreenActivity
 */
public class SplashScreenActivity extends BaseActivity {

    final long ANIMATION_DURATION = 1000;
    final long SHOW_SIGN_IN_DELAY = 1000;
    final long SIGNED_IN_DELAY = 2000;

    @Bind(R.id.text_view_app_name)
    TextView textViewAppName;
    @Bind(R.id.text_view_slogan)
    TextView textViewSlogan;

    @Bind(R.id.image_view_propeller)
    ImageView imageViewPropeller;

    @Bind(R.id.layout_fragment_container)
    FrameLayout layoutFragmentContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);

        // Update User Token
        if (UserSession.getInstance().isSignedIn()) {
            // Main Activity
            layoutFragmentContainer.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                    finish();
                }
            }, SIGNED_IN_DELAY);
        } else {
            // Sign In
            layoutFragmentContainer.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showSignIn();
                }
            }, SHOW_SIGN_IN_DELAY);
        }

        resetSloganMarginTop(!UserSession.getInstance().isSignedIn());

        // Animate UI in
        animateTextViews();
        animatePropeller();
    }

    // Make room for sign in fragment if necessary
    private void resetSloganMarginTop(boolean needToMakeRoomForSignIn) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) textViewAppName.getLayoutParams();
        params.topMargin = needToMakeRoomForSignIn
                ? getResources().getDimensionPixelSize(R.dimen.ff_splash_slogan_top_with_sign_in)
                : getResources().getDimensionPixelSize(R.dimen.ff_splash_slogan_top);
    }

    private void showSignIn() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.layout_fragment_container, SignInFragment.newInstance(), "SignInFragment")
                .commit();
    }

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
