package io.github.ryanhoo.firFlight.data.model.impl;

import android.os.Parcel;
import com.google.gson.annotations.SerializedName;
import io.github.ryanhoo.firFlight.data.model.App;
import io.github.ryanhoo.firFlight.data.model.IMessageContent;
import io.github.ryanhoo.firFlight.data.model.Release;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 3/19/16
 * Time: 8:12 PM
 * Desc: ReleaseMessageContent
 */
/*
"content": {
    "user": "Whitedew",
    "app": {
      "id": "564d8604e75e2d71e5000008",
      "name": "牙医经理人",
      "short": "bailu",
      "icon_url": "http://firicon.fir.im/d9627ebd8959ac03222a962f4c21a8837498277b?t=1458137043.4128423"
    },
    "release": {
      "version": "2.6.6",
      "build": "16031601",
      "changelog": "- 新的 Profile 编辑（未完成）"
    }
}
*/
public class ReleaseMessageContent implements IMessageContent {

    public ReleaseMessageContent() {
        // Empty Constructor
    }

    protected ReleaseMessageContent(Parcel in) {
        readFromParcel(in);
    }

    @SerializedName("user")
    private String user;

    @SerializedName("app")
    private App app;

    @SerializedName("release")
    private Release release;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public Release getRelease() {
        return release;
    }

    public void setRelease(Release release) {
        this.release = release;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.user);
        dest.writeParcelable(this.app, flags);
        dest.writeParcelable(this.release, flags);
    }

    private void readFromParcel(Parcel in) {
        this.user = in.readString();
        this.app = in.readParcelable(App.class.getClassLoader());
        this.release = in.readParcelable(Release.class.getClassLoader());
    }

    public static final Creator<ReleaseMessageContent> CREATOR = new Creator<ReleaseMessageContent>() {
        @Override
        public ReleaseMessageContent createFromParcel(Parcel source) {
            return new ReleaseMessageContent(source);
        }

        @Override
        public ReleaseMessageContent[] newArray(int size) {
            return new ReleaseMessageContent[size];
        }
    };
}
