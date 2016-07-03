package io.github.ryanhoo.firFlight.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 3/19/16
 * Time: 7:56 PM
 * Desc: Message
 */
/*
{
  "id": "56c6a8566280dd3e91029b79",
  "type": "sys",
  "is_read": false,
  // Sys
  "content": {
    "title": "<p>【公告】\n截止2月29号，fir.im 将取消“微信登录”方式，请前往用户中心绑定邮箱和密码，感谢支持！ \n</p>\n",
    "link": null
  },
  // Release
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
  },
  "template": "sys",
  "created_at": 1455859798.276
}
*/
public class Message implements Parcelable {

    /**
     * Message type:
     * - sys: system notifications
     * - release: new release distributed
     */
    public interface Type {
        String SYSTEM = "sys";
        String NEW_RELEASE = "release";
    }

    public Message() {
        // Empty Constructor
    }

    protected Message(Parcel in) {
        readFromParcel(in);
    }

    @SerializedName("id")
    private String id;

    @SerializedName("type")
    private String type;

    @SerializedName("is_read")
    private boolean read;

    @SerializedName("content")
    private IMessageContent content;

    @SerializedName("template")
    private String template;

    @SerializedName("created_at")
    private Date createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public IMessageContent getContent() {
        return content;
    }

    public void setContent(IMessageContent content) {
        this.content = content;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.type);
        dest.writeByte(read ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.content, flags);
        dest.writeString(this.template);
        dest.writeLong(createdAt != null ? createdAt.getTime() : -1);
    }

    private void readFromParcel(Parcel in) {
        this.id = in.readString();
        this.type = in.readString();
        this.read = in.readByte() != 0;
        this.content = in.readParcelable(IMessageContent.class.getClassLoader());
        this.template = in.readString();
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
    }

    public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel source) {
            return new Message(source);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };
}
