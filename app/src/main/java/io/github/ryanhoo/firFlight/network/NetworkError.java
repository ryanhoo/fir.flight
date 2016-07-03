package io.github.ryanhoo.firFlight.network;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 3/15/16
 * Time: 10:21 PM
 * Desc: NetworkError
 */
/*
{
  "errors": "app is not found",
  "code": 100701
}
*/
public class NetworkError implements Parcelable {

    public static final int ERROR_CODE_UNKNOWN = -1;

    public NetworkError() {
        // Empty Constructor
    }

    public NetworkError(int code, String message) {
        this.errorCode = code;
        this.errorMessage = message;
    }

    public NetworkError(Parcel in) {
        readFromParcel(in);
    }

    @SerializedName("errors")
    private String errorMessage;

    @SerializedName("code")
    private int errorCode;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.errorMessage);
        dest.writeInt(this.errorCode);
    }

    private void readFromParcel(Parcel in) {
        this.errorMessage = in.readString();
        this.errorCode = in.readInt();
    }

    public static final Parcelable.Creator<NetworkError> CREATOR = new Parcelable.Creator<NetworkError>() {
        public NetworkError createFromParcel(Parcel source) {
            return new NetworkError(source);
        }

        public NetworkError[] newArray(int size) {
            return new NetworkError[size];
        }
    };
}
