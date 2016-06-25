package io.github.ryanhoo.firFlight.data.model.impl;

import android.os.Parcel;
import com.google.gson.annotations.SerializedName;
import io.github.ryanhoo.firFlight.data.model.IMessageContent;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 3/19/16
 * Time: 8:11 PM
 * Desc: SystemMessageContent
 */
/*
"content": {
    "title": "<p>【公告】\n截止2月29号，fir.im 将取消“微信登录”方式，请前往用户中心绑定邮箱和密码，感谢支持！ \n</p>\n",
    "link": null
}
*/
public class SystemMessageContent implements IMessageContent {

    public SystemMessageContent() {
        // Empty Constructor
    }

    protected SystemMessageContent(Parcel in) {
        readFromParcel(in);
    }

    @SerializedName("title")
    private String title;

    @SerializedName("link")
    private String link;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.link);
    }

    private void readFromParcel(Parcel in) {
        this.title = in.readString();
        this.link = in.readString();
    }

    public static final Creator<SystemMessageContent> CREATOR = new Creator<SystemMessageContent>() {
        @Override
        public SystemMessageContent createFromParcel(Parcel source) {
            return new SystemMessageContent(source);
        }

        @Override
        public SystemMessageContent[] newArray(int size) {
            return new SystemMessageContent[size];
        }
    };
}
