package io.github.ryanhoo.firFlight.ui.signin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.ryanhoo.firFlight.R;
import io.github.ryanhoo.firFlight.account.SignInCallback;
import io.github.ryanhoo.firFlight.account.UserSession;
import io.github.ryanhoo.firFlight.analytics.FlightAnalytics;
import io.github.ryanhoo.firFlight.analytics.FlightEvent;
import io.github.ryanhoo.firFlight.data.service.RetrofitService;
import io.github.ryanhoo.firFlight.network.RetrofitClient;
import io.github.ryanhoo.firFlight.ui.base.BaseActivity;
import io.github.ryanhoo.firFlight.ui.helper.OnTextChangedListener;

/**
 * Created with Android Studio.
 * User: ryan@whitedew.me
 * Date: 4/3/16
 * Time: 12:49 AM
 * Desc: SignInActivity
 */
public class SignInActivity extends BaseActivity {

    private static final String REG_EMAIL = ".+@.+\\..+";

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.edit_text_email)
    EditText editTextEmail;
    @Bind(R.id.edit_text_password)
    EditText editTextPassword;

    MenuItem menuItemSignIn;

    RetrofitService mRetrofitService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        ActionBar actionBar = supportActionBar(toolbar);
        if (actionBar != null) {
            actionBar.setTitle(null);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_nav_close);
        }
        mRetrofitService = RetrofitClient.defaultInstance().create(RetrofitService.class);

        OnTextChangedListener onTextChangedListener = new OnTextChangedListener() {
            @Override
            public void onTextChanged(Editable s) {
                menuItemSignIn.setEnabled(isFormValid());
            }
        };
        editTextEmail.addTextChangedListener(onTextChangedListener);
        editTextPassword.addTextChangedListener(onTextChangedListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sign_in, menu);
        menuItemSignIn = menu.findItem(R.id.menu_item_sign_in).setEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_sign_in) {
            signIn();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isFormValid() {
        if (editTextEmail.length() == 0)
            return false;
        if (editTextPassword.length() == 0)
            return false;
        if (!editTextEmail.getText().toString().matches(REG_EMAIL)) {
            return false;
        }
        return true;
    }

    private void signIn() {
        menuItemSignIn.setEnabled(false);
        final String email = editTextEmail.getText().toString();
        final String password = editTextPassword.getText().toString();
        UserSession.getInstance().signIn(email, password, new SignInCallback() {
            @Override
            public void success() {
                // SignIn Event Success
                FlightAnalytics.onEvent(new FlightEvent(FlightEvent.EVENT_SIGN_IN)
                        .putCustomAttribute(FlightEvent.KEY_EMAIL, email)
                        .putSuccess(true)
                );
                menuItemSignIn.setEnabled(false);
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void fail() {
                // SignIn Event Fail
                FlightAnalytics.onEvent(new FlightEvent(FlightEvent.EVENT_SIGN_IN)
                        .putCustomAttribute(FlightEvent.KEY_EMAIL, email)
                        .putSuccess(false)
                );
                Toast.makeText(SignInActivity.this, R.string.ff_signin_failed, Toast.LENGTH_SHORT).show();
                menuItemSignIn.setEnabled(false);
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_from_top);
    }
}
