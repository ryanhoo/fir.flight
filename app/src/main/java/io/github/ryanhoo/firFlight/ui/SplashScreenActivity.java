package io.github.ryanhoo.firFlight.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.view.ViewPropertyAnimatorCompatSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.ryanhoo.firFlight.R;

/**
 * Created with Android Studio.
 * User: ryan@whitedew.me
 * Date: 3/15/16
 * Time: 8:27 PM
 * Desc: SplashScreenActivity
 */
public class SplashScreenActivity extends BaseActivity {

    final long ANIMATION_DURATION = 1000;

    @Bind(R.id.text_view_app_name)
    TextView textViewAppName;
    @Bind(R.id.text_view_slogan)
    TextView textViewSlogan;

    @Bind(R.id.image_view_propeller)
    ImageView imageViewPropeller;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);

        // Update User Token

        // Animate UI in
        animateTextViews();
        animatePropeller();
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
