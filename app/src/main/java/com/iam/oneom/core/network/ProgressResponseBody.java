package com.iam.oneom.core.network;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public class ProgressResponseBody extends ResponseBody {

    private String TAG = ProgressResponseBody.class.getSimpleName();

    private final ResponseBody responseBody;
    private final DownloadProgressListener progressListener;
    private BufferedSource bufferedSource;
    private Handler handler;

    public ProgressResponseBody(ResponseBody responseBody, DownloadProgressListener progressListener) {
        this.responseBody = responseBody;
        this.progressListener = progressListener;
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                handler.post(() -> {
                    if (progressListener != null) {
                        progressListener.onProgressUpdate(totalBytesRead, responseBody.contentLength());
                    }
                });
                return bytesRead;
            }
        };
    }

    /**
     *
     * @param path - Full path for saving directory
     * @param filename - Saving filename
     * @return
     */
    public boolean saveOnDevice(String path, String filename) {
        File outputDirectory = new File(path);
        if (!outputDirectory.exists()) outputDirectory.mkdir();

        String filenameWthtExt = filename.split("\\.")[0];

        Log.d(TAG, "saveOnDevice: " + outputDirectory.getAbsolutePath());
        Log.d(TAG, "saveOnDevice: " + outputDirectory.exists());

        try {
            for (String file : outputDirectory.list()) {
                if (file.contains(filenameWthtExt)) {
                    new File(outputDirectory, file).delete();
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }

        try {
            // todo change the file location/filename according to your needs
            File futureStudioIconFile = new File(outputDirectory, filename);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];


                inputStream = this.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }



        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
}