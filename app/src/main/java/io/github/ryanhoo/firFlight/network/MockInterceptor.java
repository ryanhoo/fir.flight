package io.github.ryanhoo.firFlight.network;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import okhttp3.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 7/20/16
 * Time: 3:53 PM
 * Desc: MockInterceptor
 * Support url query parameters
 * \@GET("/messages?mock=true&mock_data=messages.json")
 * - mock[boolean]
 * - mock_data[assets/network/mock/:filename.json]
 * - mock_delay[long]: delay the request from [0, delay]
 * TODO
 * - mock_code
 * - mock_message
 * - mock_error[assets/network/mock/:error.json]
 */
public class MockInterceptor implements Interceptor {

    private static final String TAG = "MockInterceptor";

    public static final String MEDIA_TYPE = "application/json";

    public static final String FIELD_MOCK = "mock";
    public static final String FIELD_MOCK_DATA = "mock_data";

    public static final String MOCK_FILE_PATH = "network/mock/";

    private Context mContext;

    public MockInterceptor(Context context) {
        mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        final HttpUrl url = chain.request().url();
        boolean mock = false;
        final String queryMock = url.queryParameter(FIELD_MOCK);
        if (!TextUtils.isEmpty(queryMock)) {
            mock = Boolean.parseBoolean(queryMock);
        }
        if (mock) {
            final String queryDelay = url.queryParameter("mock_delay");
            if (!TextUtils.isEmpty(queryDelay)) {
                int delay = Integer.parseInt(queryDelay);
                try {
                    Thread.sleep(new Random().nextInt(delay));
                } catch (InterruptedException e) {
                    Log.e(TAG, "Someone interrupted my sleeping...: ", e);
                }
            }
            // Skip Gzip in case error occurs
            final Request request = chain.request().newBuilder()
                    .header("Accept-Encoding", "None")
                    .build();
            Response response = chain.proceed(request);
            String mockData = url.queryParameter(FIELD_MOCK_DATA);
            InputStream in = mContext.getAssets().open(MOCK_FILE_PATH + mockData);
            final int available = in.available();
            byte[] bytes = new byte[available];
            int read = in.read(bytes);
            Log.d(TAG, "read: " + read + " available: " + available);
            ResponseBody body = ResponseBody.create(MediaType.parse(MEDIA_TYPE), bytes);
            return response.newBuilder()
                    .code(200)
                    .body(body)
                    .build();
        }
        return chain.proceed(chain.request());
    }
}
