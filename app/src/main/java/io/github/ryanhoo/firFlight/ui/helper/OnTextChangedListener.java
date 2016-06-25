package io.github.ryanhoo.firFlight.ui.helper;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 4/3/16
 * Time: 1:52 AM
 * Desc: OnTextChangedListener
 */
public abstract class OnTextChangedListener implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        onTextChanged(s);
    }

    public abstract void onTextChanged(Editable s);
}
