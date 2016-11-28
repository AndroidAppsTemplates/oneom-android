package com.iam.oneom.core.network;

public interface DownloadProgressListener {
    void onProgressUpdate(long downloaded, long total);
}
