package io.github.ryanhoo.firFlight.ui.profile;

import android.content.*;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.view.*;
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
import io.github.ryanhoo.firFlight.ui.about.AboutActivity;
import io.github.ryanhoo.firFlight.ui.base.BaseFragment;
import io.github.ryanhoo.firFlight.ui.common.alert.FlightDialog;
import io.github.ryanhoo.firFlight.ui.common.alert.FlightToast;
import io.github.ryanhoo.firFlight.ui.common.widget.CharacterDrawable;
import io.github.ryanhoo.firFlight.ui.setting.SettingsActivity;

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
        setHasOptionsMenu(true);
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
    public void onDestroyView() {
        mPresenter.unsubscribe();
        super.onDestroyView();
    }

    @Override
    public void setPresenter(ProfileContract.Presenter presenter) {
        mPresenter = presenter;
    }

    // OnClick Events

    @OnClick(R.id.text_view_api_token)
    public void onApiTokenClick(TextView textView) {
        final CharSequence token = textView.getText();

        ClipboardManager manager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        manager.setPrimaryClip(ClipData.newPlainText("Api Token", token));

        new FlightToast.Builder(getActivity())
                .message(getString(R.string.ff_profile_copy_token, token))
                .show();
    }

    // Menu

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_settings:
                onOpenSettings();
                break;
            case R.id.menu_item_about:
                onOpenAbout();
                break;
            case R.id.menu_item_sign_out:
                onSignOut();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onOpenSettings() {
        startActivity(new Intent(getActivity(), SettingsActivity.class));
    }

    private void onOpenAbout() {
        startActivity(new Intent(getActivity(), AboutActivity.class));
    }

    private void onSignOut() {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.ff_dialog_sign_out_title)
                .setMessage(R.string.ff_dialog_sign_out_message)
                .setNegativeButton(R.string.ff_no, null)
                .setPositiveButton(R.string.ff_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UserSession.getInstance().signOut();
                    }
                })
                .show();
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
                .placeholder(CharacterDrawable.create(getActivity(), user.getName().charAt(0), true, R.dimen.ff_padding_large))
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
