package io.github.ryanhoo.firFlight.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 3/16/16
 * Time: 11:10 PM
 * Desc: Release
 */
/*
{
  "version": "2.6.5",
  "build": "16031502",
  "release_type": "inhouse",
  "distribution_name": "",
  "supported_platform": null,
  "created_at": 1458030884
}
*/
public class Release implements Parcelable, Serializable {

    public Release() {
        // Empty Constructor
    }

    protected Release(Parcel in) {
        readFromParcel(in);
    }

    private long id;

    @SerializedName("version")
    private String version;

    @SerializedName("build")
    private String build;

    @SerializedName("changelog")
    private String changelog;

    @SerializedName("release_type")
    private String releaseType;

    @SerializedName("distribution_name")
    private String distributionName;

    @SerializedName("supported_platform")
    private String supportedPlatform;

    @SerializedName("created_at")
    private long createdAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getReleaseType() {
        return releaseType;
    }

    public void setReleaseType(String releaseType) {
        this.releaseType = releaseType;
    }

    public String getDistributionName() {
        return distributionName;
    }

    public void setDistributionName(String distributionName) {
        this.distributionName = distributionName;
    }

    public String getSupportedPlatform() {
        return supportedPlatform;
    }

    public void setSupportedPlatform(String supportedPlatform) {
        this.supportedPlatform = supportedPlatform;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.version);
        dest.writeString(this.build);
        dest.writeString(this.changelog);
        dest.writeString(this.releaseType);
        dest.writeString(this.distributionName);
        dest.writeString(this.supportedPlatform);
        dest.writeLong(this.createdAt);
    }

    private void readFromParcel(Parcel in) {
        this.id = in.readLong();
        this.version = in.readString();
        this.build = in.readString();
        this.changelog = in.readString();
        this.releaseType = in.readString();
        this.distributionName = in.readString();
        this.supportedPlatform = in.readString();
        this.createdAt = in.readLong();
    }

    public static final Parcelable.Creator<Release> CREATOR = new Parcelable.Creator<Release>() {
        public Release createFromParcel(Parcel source) {
            return new Release(source);
        }

        public Release[] newArray(int size) {
            return new Release[size];
        }
    };
}
