package io.github.ryanhoo.firFlight.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 3/15/16
 * Time: 10:07 PM
 * Desc: Token
 */
/*
// 1. /signIn
{
  "access_token": "0d1b816b9f69103cd11ef2b3dfd1ca37"
}
// 2. /user/api_token
{
  "api_token": "0d1b816b9f69103cd11ef2b3dfd1ca37"
}
*/
public class Token implements Parcelable {

    public Token() {
        // Empty Constructor
    }

    protected Token(Parcel in) {
        readFromParcel(in);
    }

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("api_token")
    private String apiToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accessToken);
        dest.writeString(this.apiToken);
    }

    private void readFromParcel(Parcel in) {
        this.accessToken = in.readString();
        this.apiToken = in.readString();
    }

    public static final Parcelable.Creator<Token> CREATOR = new Parcelable.Creator<Token>() {
        public Token createFromParcel(Parcel source) {
            return new Token(source);
        }

        public Token[] newArray(int size) {
            return new Token[size];
        }
    };
}
