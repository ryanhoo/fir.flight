package io.github.ryanhoo.firFlight.ui.main;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import io.github.ryanhoo.firFlight.R;
import io.github.ryanhoo.firFlight.data.UserSession;
import io.github.ryanhoo.firFlight.data.model.User;
import io.github.ryanhoo.firFlight.network.NetworkError;
import io.github.ryanhoo.firFlight.network.RetrofitCallback;
import io.github.ryanhoo.firFlight.ui.about.AboutFragment;
import io.github.ryanhoo.firFlight.ui.app.AppsFragment;
import io.github.ryanhoo.firFlight.ui.base.BaseActivity;
import io.github.ryanhoo.firFlight.ui.message.MessagesFragment;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created with Android Studio.
 * User: ryan@whitedew.me
 * Date: 3/17/16
 * Time: 1:36 AM
 * Desc: MainActivity
 * - https://guides.codepath.com/android/Fragment-Navigation-Drawer#setup-drawer-resources
 */
public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private interface Tab {
        String APPS = "apps";
        String MESSAGES = "messages";
        String ABOUT = "about";
    }

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.navigation_view)
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    String mCurrentFragmentTab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        supportActionBar(toolbar);

        requestUser();
        setUpDrawerToggle();
        setUpDrawerContent();

        switchScene(null, Tab.APPS);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE! Make sure to override the method with only a single `Bundle` argument
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void setUpDrawerToggle() {
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.ff_main_drawer_open,
                R.string.ff_main_drawer_close
        );
        drawerLayout.addDrawerListener(drawerToggle);
    }

    private void setUpDrawerContent() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                onDrawerItemChecked(item);
                return true;
            }
        });
    }

    private void onDrawerItemChecked(MenuItem item) {
        if (item.getItemId() == R.id.item_sign_out) {
            confirmSignOut();
            return;
        }
        for (int i = 0; i < navigationView.getMenu().size(); i++) {
            MenuItem menuItem = navigationView.getMenu().getItem(i);
            if (menuItem.getItemId() == item.getItemId()) {
                menuItem.setChecked(true);
            } else {
                menuItem.setChecked(false);
            }
        }

        drawerLayout.closeDrawers();

        String toTab = null;
        switch (item.getItemId()) {
            case R.id.item_apps:
                toTab = Tab.APPS;
                break;
            case R.id.item_account:
                break;
            case R.id.item_messages:
                toTab = Tab.MESSAGES;
                break;
            case R.id.item_settings:
                break;
            case R.id.item_about:
                toTab = Tab.ABOUT;
                break;
        }
        if (toTab != null && !toTab.equals(mCurrentFragmentTab)) {
            switchScene(mCurrentFragmentTab, toTab);
        }
    }

    private void switchScene(String fromTab, String toTab) {
        Fragment from = getSupportFragmentManager().findFragmentByTag(fromTab);
        Fragment to = getSupportFragmentManager().findFragmentByTag(toTab);
        boolean addToStack = false;
        if (to == null) {
            switch (toTab) {
                case Tab.APPS:
                    to = new AppsFragment();
                    addToStack = true;
                    break;
                case Tab.MESSAGES:
                    to = new MessagesFragment();
                    addToStack = true;
                    break;
                case Tab.ABOUT:
                    to = new AboutFragment();
                    addToStack = true;
                    break;
            }
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        try {
            if (from != null) {
                transaction.hide(from);
            }
            if (to != null) {
                if (addToStack) {
                    Log.d(TAG, "switchScene: create new tab " + toTab);
                    transaction.add(R.id.layout_fragment_container, to, toTab);
                } else {
                    transaction.show(to);
                }
                mCurrentFragmentTab = toTab;
            }
        } finally {
            transaction.commit();
        }
    }

    private void confirmSignOut() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.ff_dialog_sign_out_title))
                .setMessage(getString(R.string.ff_dialog_sign_out_message))
                .setNegativeButton(getString(R.string.ff_dialog_cancel), null)
                .setPositiveButton(getString(R.string.ff_dialog_confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // SignOut Event
                        final String name = UserSession.getInstance().getUser().getName();
                        final String email = UserSession.getInstance().getUser().getEmail();
                        CustomEvent signOutEvent = new CustomEvent("signOut")
                                .putCustomAttribute("email", email)
                                .putCustomAttribute("name", name);
                        Answers.getInstance().logCustom(signOutEvent);

                        // Sign out
                        UserSession.getInstance().signOut();
                        MainActivity.this.finish();
                    }
                })
                .show();
    }

    private void onLoadUserInfo(User user) {
        final ImageView imageViewIcon = ButterKnife.findById(drawerLayout, R.id.image_view_icon);
        final TextView textViewName = ButterKnife.findById(drawerLayout, R.id.text_view_name);
        final TextView textViewEmail = ButterKnife.findById(drawerLayout, R.id.text_view_email);

        textViewName.setText(user.getName());
        textViewEmail.setText(user.getEmail());
        Glide.with(this)
                .load(user.getGravatar())
                .asBitmap()
                .placeholder(R.color.ff_apps_icon_placeholder)
                .centerCrop()
                .into(new BitmapImageViewTarget(imageViewIcon) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageViewIcon.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    // Request

    private void requestUser() {
        User user = UserSession.getInstance().getUser();
        if (user != null) {
            onLoadUserInfo(user);
        }
        UserSession.getInstance().updateUser(new RetrofitCallback<User>() {
            @Override
            public void onSuccess(Call<User> call, Response httpResponse, User user) {
                Log.d(TAG, String.format("User: %s\n%s\n%s", user.getName(), user.getEmail(), user.getGravatar()));
                onLoadUserInfo(user);
            }

            @Override
            public void onFailure(Call<User> call, NetworkError error) {
                Toast.makeText(MainActivity.this, error.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
