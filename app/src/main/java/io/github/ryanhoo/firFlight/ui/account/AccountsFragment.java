package io.github.ryanhoo.firFlight.ui.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import io.github.ryanhoo.firFlight.R;
import io.github.ryanhoo.firFlight.account.Account;
import io.github.ryanhoo.firFlight.account.AccountManager;
import io.github.ryanhoo.firFlight.data.model.User;
import io.github.ryanhoo.firFlight.ui.base.BaseFragment;

import java.util.List;

/**
 * Created with Android Studio.
 * User: ryan@whitedew.me
 * Date: 4/2/16
 * Time: 11:17 PM
 * Desc: AccountsFragment
 */
public class AccountsFragment extends BaseFragment {

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

    private void updateUI() {
        Account currentAccount = AccountManager.getCurrentAccount(getActivity());
        List<Account> accounts = AccountManager.getAccounts(getActivity());
        for (int i = 0; i < accounts.size(); i++) {
            final Account account = accounts.get(i);
            final View itemView = getActivity().getLayoutInflater()
                    .inflate(R.layout.item_account, layoutContainer, false);
            final ImageView imageView = ButterKnife.findById(itemView, R.id.image_view);
            final TextView textViewName = ButterKnife.findById(itemView, R.id.text_view_name);
            final TextView textViewEmail = ButterKnife.findById(itemView, R.id.text_view_email);
            final View dividerTop = ButterKnife.findById(itemView, R.id.divider_top);
            final View dividerTopWithPadding = ButterKnife.findById(itemView, R.id.divider_top_with_padding);
            final View dividerBottom = ButterKnife.findById(itemView, R.id.divider_bottom);
            final View selected = ButterKnife.findById(itemView, R.id.view_selected);
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
                    .placeholder(R.color.ff_apps_icon_placeholder)
                    .into(imageView);
            textViewName.setText(user.getName());
            textViewEmail.setText(user.getEmail());
            layoutContainer.addView(itemView);

            final int position = i;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(account, position, isCurrentAccount);
                }
            });
        }
    }

    private void onItemClick(Account account, int position, boolean isCurrentAccount) {

    }
}
