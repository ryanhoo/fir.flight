package io.github.ryanhoo.firFlight.network.download;

import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import io.github.ryanhoo.firFlight.util.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 3/20/16
 * Time: 5:36 PM
 * Desc: AsyncDownloadTask
 */
@Deprecated
public class AsyncDownloadTask extends AsyncTask<Void, Integer, Uri> {

    private static final String TAG = "AsyncDownloadTask";

    private String fileDir;
    private String fileName;
    private String downloadUrl;

    private DownloadListener downloadListener;

    private float progress;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (downloadListener != null) {
            downloadListener.onStart();
        }
    }

    @Override
    protected Uri doInBackground(Void... params) {
        publishProgress(0, 0);
        HttpURLConnection urlConnection = null;
        BufferedOutputStream outputStream = null;
        File downloadFile = null;
        try {
            URL url = new URL(downloadUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setInstanceFollowRedirects(true);
            urlConnection.connect();
            int contentLength = urlConnection.getContentLength();

            Log.d(TAG, "Start downloading " + urlConnection.getURL());
            Log.d(TAG, String.format("File size %.2f kb", (float) contentLength / 1024));

            if (fileName == null) {
                fileName = getFileName(urlConnection);
            }
            downloadFile = new File(fileDir, fileName);
            outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile));
            byte[] buffer = new byte[16 * 1024];
            int length;
            int totalLength = 0;
            InputStream in = urlConnection.getInputStream();
            while ((length = in.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
                totalLength += length;
                publishProgress(totalLength, contentLength);
            }
            // Log
            Map<String, List<String>> headers = urlConnection.getHeaderFields();
            for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                String headerName = entry.getKey();
                List<String> headerValue = entry.getValue();
                Log.d(TAG, String.format("%s: %s", headerName, StringUtils.join(headerValue, ", ")));
            }
            Log.d(TAG, "doInBackground: " + urlConnection.getContentEncoding());
            publishProgress(contentLength, contentLength);
        } catch (IOException e) {
            Log.e(TAG, String.format("Download: %s, %s", fileName, downloadUrl), e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ignore) {
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return Uri.fromFile(downloadFile);
    }

    @Override
    protected void onPostExecute(Uri uri) {
        super.onPostExecute(uri);
        if (downloadListener == null) return;
        if (uri == null) {
            downloadListener.onFail();
        } else {
            downloadListener.onFinish(uri);
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        int current = values[0];
        int total = values[1];
        progress = (total == 0) ? 0 : (float) current / (float) total;
        if (downloadListener != null) {
            downloadListener.onProgress(current, total, progress);
        }
    }

    public AsyncDownloadTask setFileDir(String dir) {
        fileDir = dir;
        return this;
    }

    public AsyncDownloadTask setFileDir(File dir) {
        fileDir = dir.getPath();
        return this;
    }

    public AsyncDownloadTask setFileName(String name) {
        fileName = name;
        return this;
    }

    public AsyncDownloadTask setUrl(String url) {
        downloadUrl = url;
        return this;
    }

    public AsyncDownloadTask setListener(DownloadListener listener) {
        downloadListener = listener;
        return this;
    }

    public float getProgress() {
        return progress;
    }

    private String getFileName(HttpURLConnection urlConnection) {
        Uri uri = Uri.parse(urlConnection.getURL().toString());
        // fir.im url: http://pkg.fir.im/27c81a3398038551ab67aa9335a4418f009c0655.apk?attname=bailu-2.6.6-16032001-160320142728-release.apk_2.6.6.apk&e=1458475559&token=LOvmia8oXF4xnLh0IdH05XMYpH6ENHNpARlmPc-T:6N-VyCkN2bcr68ykQzhnDj3OkUE=
        String fileName = uri.getQueryParameter("attname");
        if (TextUtils.isEmpty(fileName)) {
            // attachment; filename="bailu-2.6.6-16032001-160320142728-release.apk_2.6.6.apk"
            String attachment = urlConnection.getHeaderField("Content-Disposition");
            if (attachment != null) {
                String delimiter = "filename=\"";
                int index = attachment.indexOf(delimiter);
                if (index != -1) {
                    fileName = attachment.substring(index + delimiter.length(), attachment.length() - 1);
                }
                if (TextUtils.isEmpty(fileName)) {
                    fileName = uri.getLastPathSegment();
                }
            }
        }
        return fileName;
    }
}
