package io.github.ryanhoo.firFlight.ui.profile;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import io.github.ryanhoo.firFlight.R;
import io.github.ryanhoo.firFlight.account.UserSession;
import io.github.ryanhoo.firFlight.data.model.Token;
import io.github.ryanhoo.firFlight.data.model.User;
import io.github.ryanhoo.firFlight.ui.base.BaseFragment;
import io.github.ryanhoo.firFlight.ui.common.alert.FlightDialog;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 7/9/16
 * Time: 12:21 AM
 * Desc: ProfileFragment
 */
public class ProfileFragment extends BaseFragment implements ProfileContract.View {

    @Bind(R.id.image_view_avatar)
    ImageView imageView;
    @Bind(R.id.text_view_user)
    TextView textViewUser;
    @Bind(R.id.text_view_email)
    TextView textViewEmail;
    @Bind(R.id.text_view_api_token)
    TextView textViewApiToken;
    @Bind(R.id.button_refresh)
    Button buttonRefresh;

    ProfileContract.Presenter mPresenter;
    FlightDialog mProgressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mProgressDialog = FlightDialog.defaultLoadingDialog(getActivity());

        // Init
        User user = UserSession.getInstance().getUser();
        Token token = UserSession.getInstance().getToken();
        updateUserProfile(user);
        updateApiToken(token);

        ProfilePresenter presenter = new ProfilePresenter(this);
        presenter.subscribe();
    }

    @Override
    public void setPresenter(ProfileContract.Presenter presenter) {
        mPresenter = presenter;
    }

    // OnClick Events

    @OnClick(R.id.button_refresh)
    public void onRefreshApiToken() {
        mPresenter.refreshApiToken();
    }

    // MVP View

    @Override
    public void updateUserProfile(User user) {
        Glide.with(getActivity())
                .load(user.getGravatar())
                .asBitmap()
                .placeholder(R.drawable.default_avatar)
                .centerCrop()
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
        textViewUser.setText(user.getName());
        textViewEmail.setText(user.getEmail());
    }

    @Override
    public void updateApiToken(Token token) {
        textViewApiToken.setText(token.getApiToken());
    }

    @Override
    public void onRefreshApiTokenStart() {
        mProgressDialog.show();
        buttonRefresh.setEnabled(false);
    }

    @Override
    public void onRefreshApiTokenCompleted() {
        mProgressDialog.dismiss();
        buttonRefresh.setEnabled(true);
    }
}
