package io.github.ryanhoo.firFlight.ui.main;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import io.github.ryanhoo.firFlight.R;
import io.github.ryanhoo.firFlight.data.UserSession;
import io.github.ryanhoo.firFlight.data.model.User;
import io.github.ryanhoo.firFlight.data.service.RetrofitService;
import io.github.ryanhoo.firFlight.network.NetworkError;
import io.github.ryanhoo.firFlight.network.RetrofitCallback;
import io.github.ryanhoo.firFlight.network.RetrofitClient;
import io.github.ryanhoo.firFlight.ui.app.AppListFragment;
import io.github.ryanhoo.firFlight.ui.base.BaseActivity;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created with Android Studio.
 * User: ryan@whitedew.me
 * Date: 3/17/16
 * Time: 1:36 AM
 * Desc: MainActivity
 */
public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.navigation_view)
    NavigationView navigationView;

    ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        supportActionBar(toolbar);

        requestUser();
        setUpDrawerToggle();
        setUpDrawerContent();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_fragment_container, new AppListFragment(), "apps")
                .commit();
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
        RetrofitService retrofitService = RetrofitClient.defaultInstance().create(RetrofitService.class);
        Call<User> call = retrofitService.user(UserSession.getInstance().getToken().getAccessToken());
        call.enqueue(new RetrofitCallback<User>() {
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
