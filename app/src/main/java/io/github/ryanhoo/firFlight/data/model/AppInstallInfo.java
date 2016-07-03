package io.github.ryanhoo.firFlight.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 3/20/16
 * Time: 9:32 PM
 * Desc: AppInstallation
 */
/*
{
  "name": "牙医经理人",
  "version": "16031502",
  "changelog": "- 测试版 bailu.me 主页 Tab 颜色调整",
  "updated_at": 1458030884,
  "versionShort": "2.6.5",
  "build": "16031502",
  "installUrl": "http://download.fir.im/v2/app/install/564d8604e75e2d71e5000008?download_token=e6ebd5ee78fa9ea78516a01eca850f69",
  "install_url": "http://download.fir.im/v2/app/install/564d8604e75e2d71e5000008?download_token=e6ebd5ee78fa9ea78516a01eca850f69",
  "direct_install_url": "http://download.fir.im/v2/app/install/564d8604e75e2d71e5000008?download_token=e6ebd5ee78fa9ea78516a01eca850f69",
  "update_url": "http://fir.im/bailu",
  "binary": {
    "fsize": 7729371
  }
}
*/
//TODO Repeat with Release
public class AppInstallInfo implements Parcelable {

    public AppInstallInfo() {
        // Empty Constructor
    }

    protected AppInstallInfo(Parcel in) {
        readFromParcel(in);
    }

    @SerializedName("name")
    private String name;

    @SerializedName("versionShort")
    private String version;

    @SerializedName("build")
    private String build;

    @SerializedName("changelog")
    private String changelog;

    @SerializedName("installUrl")
    private String installUrl;

    @SerializedName("direct_install_url")
    private String directInstallUrl;

    @SerializedName("update_url")
    private String updateUrl;

    @SerializedName("binary")
    private Binary binary;

    @SerializedName("updated_at")
    private Date updatedAt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public String getChangelog() {
        return changelog;
    }

    public void setChangelog(String changelog) {
        this.changelog = changelog;
    }

    public String getInstallUrl() {
        return installUrl;
    }

    public void setInstallUrl(String installUrl) {
        this.installUrl = installUrl;
    }

    public String getDirectInstallUrl() {
        return directInstallUrl;
    }

    public void setDirectInstallUrl(String directInstallUrl) {
        this.directInstallUrl = directInstallUrl;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    public Binary getBinary() {
        return binary;
    }

    public void setBinary(Binary binary) {
        this.binary = binary;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    class Binary implements Serializable {
        @SerializedName("fsize")
        private int fileSize;

        public int getFileSize() {
            return fileSize;
        }

        public void setFileSize(int fileSize) {
            this.fileSize = fileSize;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.version);
        dest.writeString(this.build);
        dest.writeString(this.changelog);
        dest.writeString(this.installUrl);
        dest.writeString(this.directInstallUrl);
        dest.writeString(this.updateUrl);
        dest.writeSerializable(this.binary);
        dest.writeLong(updatedAt != null ? updatedAt.getTime() : -1);
    }

    private void readFromParcel(Parcel in) {
        this.name = in.readString();
        this.version = in.readString();
        this.build = in.readString();
        this.changelog = in.readString();
        this.installUrl = in.readString();
        this.directInstallUrl = in.readString();
        this.updateUrl = in.readString();
        this.binary = (Binary) in.readSerializable();
        long tmpUpdatedAt = in.readLong();
        this.updatedAt = tmpUpdatedAt == -1 ? null : new Date(tmpUpdatedAt);
    }

    public static final Parcelable.Creator<AppInstallInfo> CREATOR = new Parcelable.Creator<AppInstallInfo>() {
        @Override
        public AppInstallInfo createFromParcel(Parcel source) {
            return new AppInstallInfo(source);
        }

        @Override
        public AppInstallInfo[] newArray(int size) {
            return new AppInstallInfo[size];
        }
    };
}
