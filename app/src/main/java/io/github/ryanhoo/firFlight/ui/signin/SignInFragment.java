package io.github.ryanhoo.firFlight.ui.signin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.ryanhoo.firFlight.R;
import io.github.ryanhoo.firFlight.data.UserSession;
import io.github.ryanhoo.firFlight.data.model.Token;
import io.github.ryanhoo.firFlight.data.service.RetrofitService;
import io.github.ryanhoo.firFlight.network.NetworkError;
import io.github.ryanhoo.firFlight.network.RetrofitCallback;
import io.github.ryanhoo.firFlight.network.RetrofitClient;
import io.github.ryanhoo.firFlight.ui.base.BaseFragment;
import io.github.ryanhoo.firFlight.ui.main.MainActivity;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created with Android Studio.
 * User: ryan@whitedew.me
 * Date: 3/16/16
 * Time: 12:15 AM
 * Desc: SignInFragment
 */
public class SignInFragment extends BaseFragment {

    final long ANIMATION_DURATION = 1000;
    final long SHOW_KEYBOARD_DELAY = 500;

    @Bind(R.id.edit_text_email)
    EditText editTextEmail;
    @Bind(R.id.edit_text_password)
    EditText editTextPassword;
    @Bind(R.id.button_sign_in)
    Button buttonSignIn;

    RetrofitService mRetrofitService;

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRetrofitService = RetrofitClient.defaultInstance().create(RetrofitService.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signin, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        performAnimationIn(editTextEmail);
        performAnimationIn(editTextPassword);
        performAnimationIn(buttonSignIn);
        showSoftKeyboard(SHOW_KEYBOARD_DELAY);
    }

    @OnClick({R.id.button_sign_in})
    public void onClick(View view) {
        if (view.getId() == R.id.button_sign_in) {
            String email = "tps@whitedew.me";
            String password = "";
            if (editTextEmail.length() > 0 && editTextPassword.length() > 0) {
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
            }
            UserSession.getInstance().signIn(email, password, new RetrofitCallback<Token>() {
                @Override
                public void onSuccess(Call<Token> call, Response httpResponse, Token token) {
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    getActivity().finish();
                }

                @Override
                public void onFailure(Call<Token> call, NetworkError error) {
                    Toast.makeText(getActivity(), error.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void performAnimationIn(View view) {
        final int TRANSLATE_Y = 100;
        view.setAlpha(0f);
        view.setTranslationY(TRANSLATE_Y);
        ViewCompat.animate(view)
                .alpha(1f)
                .setDuration(ANIMATION_DURATION)
                .translationYBy(-TRANSLATE_Y)
                .setInterpolator(new OvershootInterpolator(1.1f))
                .start();

    }

    private void showSoftKeyboard(long delayMillis) {
        editTextEmail.postDelayed(new Runnable() {
            @Override
            public void run() {
                editTextEmail.requestFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editTextEmail, InputMethodManager.SHOW_IMPLICIT);
            }
        }, delayMillis);
    }
}
