package io.github.ryanhoo.firFlight.ui.account;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import io.github.ryanhoo.firFlight.R;
import io.github.ryanhoo.firFlight.account.Account;
import io.github.ryanhoo.firFlight.account.AccountManager;
import io.github.ryanhoo.firFlight.data.model.User;
import io.github.ryanhoo.firFlight.ui.base.BaseFragment;
import io.github.ryanhoo.firFlight.ui.main.MainActivity;
import io.github.ryanhoo.firFlight.ui.signin.SignInActivity;

import java.util.List;

/**
 * Created with Android Studio.
 * User: ryan@whitedew.me
 * Date: 4/2/16
 * Time: 11:17 PM
 * Desc: AccountsFragment
 */
public class AccountsFragment extends BaseFragment {

    private static final int REQUEST_SIGN_INTO_NEW_ACCOUNT = 1;

    @Bind(R.id.layout_container)
    LinearLayout layoutContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_accounts, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        updateUI();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_SIGN_INTO_NEW_ACCOUNT) {
            onAccountChanged();
        }
    }

    @OnClick({R.id.button_add})
    public void onClick(View view) {
        if (view.getId() == R.id.button_add) {
            startActivityForResult(new Intent(getActivity(), SignInActivity.class), REQUEST_SIGN_INTO_NEW_ACCOUNT);
            getActivity().overridePendingTransition(R.anim.slide_in_from_bottom, android.R.anim.fade_out);
        }
    }

    private void updateUI() {
        Account currentAccount = AccountManager.getCurrentAccount(getActivity());
        List<Account> accounts = AccountManager.getAccounts(getActivity());
        if (accounts.size() == 0) {
            layoutContainer.removeAllViews();
            return;
        }
        if (layoutContainer.getChildCount() > accounts.size()) {
            layoutContainer.removeViews(accounts.size(), layoutContainer.getChildCount() - accounts.size());
        }
        for (int i = 0; i < accounts.size(); i++) {
            final Account account = accounts.get(i);
            final View itemView;
            if (layoutContainer.getChildCount() > i) {
                itemView = layoutContainer.getChildAt(i);
            } else {
                itemView = getActivity().getLayoutInflater()
                        .inflate(R.layout.item_account, layoutContainer, false);
                layoutContainer.addView(itemView);
            }
            final ImageView imageView = ButterKnife.findById(itemView, R.id.image_view);
            final TextView textViewName = ButterKnife.findById(itemView, R.id.text_view_name);
            final TextView textViewEmail = ButterKnife.findById(itemView, R.id.text_view_email);
            final View dividerTop = ButterKnife.findById(itemView, R.id.divider_top);
            final View dividerTopWithPadding = ButterKnife.findById(itemView, R.id.divider_top_with_padding);
            final View dividerBottom = ButterKnife.findById(itemView, R.id.divider_bottom);
            final View selected = ButterKnife.findById(itemView, R.id.view_selected);
            dividerTop.setVisibility(View.GONE);
            dividerTopWithPadding.setVisibility(View.GONE);
            dividerBottom.setVisibility(View.GONE);
            final boolean isCurrentAccount = currentAccount.getName().equals(account.getName());
            selected.setVisibility(isCurrentAccount ? View.VISIBLE : View.GONE);
            if (accounts.size() == 1) {
                dividerTop.setVisibility(View.VISIBLE);
                dividerBottom.setVisibility(View.VISIBLE);
            } else if (i == 0) {
                dividerTop.setVisibility(View.VISIBLE);
            } else if (i == accounts.size() - 1) {
                dividerBottom.setVisibility(View.VISIBLE);
                dividerTopWithPadding.setVisibility(View.VISIBLE);
            } else {
                dividerTopWithPadding.setVisibility(View.VISIBLE);
            }
            User user = account.getUser();
            Glide.with(getActivity())
                    .load(user.getGravatar())
                    .asBitmap()
                    .placeholder(R.color.ff_apps_icon_placeholder)
                    .into(new BitmapImageViewTarget(imageView) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            imageView.setImageDrawable(circularBitmapDrawable);
                        }
                    });
            textViewName.setText(user.getName());
            textViewEmail.setText(user.getEmail());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(account, isCurrentAccount);
                }
            });
        }
    }

    private void onItemClick(Account account, boolean isCurrentAccount) {
        if (isCurrentAccount) return;
        AccountManager.switchAccount(getActivity(), account);
        updateAccount(account);
        onAccountChanged();
    }

    @Override
    protected void onAccountChanged() {
        super.onAccountChanged();
        updateUI();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).onAccountChanged();
        }
    }
}
