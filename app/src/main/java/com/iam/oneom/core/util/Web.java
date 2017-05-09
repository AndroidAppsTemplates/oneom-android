package com.iam.oneom.core.util;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.iam.oneom.R;
import com.iam.oneom.pages.main.WebViewActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class Web {
//    public static ConcurrentHashMap<String, SocketAddress> proxies = new ConcurrentHashMap<>();
    public class url {
        public static final String proxies = "http://www.freeproxy-list.ru/api/proxy?anonymity=false&token=demo";
        public static final String domain = "https://oneom.tk";
        public static final String poster_prefix = "https://fileom.s3.amazonaws.com/";
        public static final String episodes = "/ep";
        public static final String error = "/errors/java";
        public static final String serial = "/serial";
        public static final String search = "/search";
        public static final String startupData = "/data/config";
        public static final String torrent = "/torrent";
        public static final String videoIndex = "/video/index";
    }
    public class header {
        public static final String appjson = "application/json";
    }

    public static String httpPost(String domain, String postData) {
        StringBuilder builder = new StringBuilder();
        try {
            URL url = new URL(Web.url.domain + domain);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //            httpURLConnection.addRequestProperty("token", LavkaLavka.User.token);
            conn.setRequestProperty("Content-Type", header.appjson);
            conn.setRequestProperty("Accept", header.appjson);
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(postData);

            writer.flush();
            writer.close();
            os.close();
            int responseCode;
            try {
                responseCode = conn.getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
                responseCode = conn.getResponseCode();
            }

            String line;
            BufferedReader reader;
            try {
                reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));
            } catch (IOException e) {
                reader = new BufferedReader(new
                        InputStreamReader(conn.getErrorStream()));
            }
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();
            writer.close();
            Log.i("http post response", responseCode + "");
            Log.i("http post response", url.getPath());
            Log.i("http post response", builder.toString());
            Log.i("http post response", conn.getResponseMessage());
            Log.i("http post response", conn.getHeaderFields().toString());
            Log.i("http post response", postData);
        } catch (Exception e) {
//            OneOm.handleError(Thread.currentThread(), e, "Web.httpPost(String,String)");
            e.printStackTrace();
        }
        return builder.toString();
    }

//    public static String httpPatch(String domain, HashMap<String, String> postData) {
//        StringBuilder str = new StringBuilder();
//        HttpClient httpclient = new DefaultHttpClient();
//        HttpPatch request = new HttpPatch(url.domain + domain);
//        request.addHeader(new BasicHeader("token", LavkaLavka.User.token));
//        try {
//            UrlEncodedFormEntity params = patchPairs(postData);
//            params.setContentEncoding(HTTP.UTF_8);
//            request.setEntity(params);
//
//            HttpResponse resp = httpclient.execute(request);
//            Log.i("patch url", domain);
//            Log.i("patch responce code", resp.getStatusLine().getStatusCode() + "");
//            InputStream is  = resp.getEntity().getContent();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//            String line;
//            while((line = reader.readLine()) != null){
//                str.append(line + "\n");
//            }
//            is.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return str.toString();
//    }

    public static void httpDelete(String content_type, String id) {
        URL url = null;
        try {
            url = new URL(Web.url.domain + content_type + "/" + id);
        } catch (MalformedURLException exception) {
//            OneOm.handleError(Thread.currentThread(), exception, "Web.httpDelete(String,String) MalformedURLException");
            exception.printStackTrace();
        }
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty(
                    "Content-Type", "application/x-www-form-urlencoded" );
//            httpURLConnection.addRequestProperty("token", LavkaLavka.User.token);
            httpURLConnection.setRequestMethod("DELETE");
//            httpURLConnection.setDoOutput(true);
            httpURLConnection.connect();
            httpURLConnection.getResponseCode();
        } catch (IOException exception) {
//            OneOm.handleError(Thread.currentThread(), exception, "Web.httpDelete(String,String) IOException");
            exception.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }

    public static String GET(String s, boolean fromOneOm) {
        return GET(s, fromOneOm, null);
    }

    public static String GET(String s, boolean fromOneOm, SocketAddress socketAddress) {
        try {
            StringBuilder content = new StringBuilder();

            URL url = new URL(s);

            Proxy proxy = socketAddress == null ? Proxy.NO_PROXY : new Proxy(Proxy.Type.HTTP, socketAddress);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(proxy);
            urlConnection.setRequestProperty("User-Agent", System.getProperty("http.agent"));
            if (fromOneOm) urlConnection.setRequestProperty("Content-Type", header.appjson);
            if (fromOneOm) urlConnection.setRequestProperty("Accept", header.appjson);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line);
                content.append("\n");
            }
            bufferedReader.close();
            return content.toString();
        } catch (IOException e) {
//            OneOm.handleError(Thread.currentThread(), e, "Web.GET(String,boolean,SocketAddress) IOException");
            e.printStackTrace();
        }
        return null;
    }

//    public static String search(Source.Origin origin, ArrayList<String> params) {
//        return search(origin, params, 10, false);
//    }
//
//    public static String search(Source.Origin origin, ArrayList<String> params, int time) {
//        return search(origin, params, time, false);
//    }
//
//    public static String search(Source.Origin origin, ArrayList<String> params, boolean useProxy) {
//        return search(origin, params, 10, useProxy);
//    }

//    public static String search(Source.Origin origin, ArrayList<String> params, int time, boolean useProxy) {
//
//        try {
//
//            String searchPageURL = Source.getByOrigin(origin).searchPage();
//            if (searchPageURL.length() == 0) {
//                searchPageURL = Source.getByOrigin(origin).search();
//            }
//
//            Log.d("search_url", searchPageURL + "");
//
//
//            switch (origin) {
//                case opensubtitles:
//                    if (params.size() < 5)
//                        throw new RuntimeException("Search for " + origin.name() + " must contains at least 5 params");
//                    String lang = params.get(0), langId = params.get(1), season = params.get(2), episode = params.get(3);
//                    String serialName = URLEncoder.encode(params.get(4), "UTF-8");
//                    searchPageURL = searchPageURL.replaceFirst("\\{lang\\}", lang);
//                    searchPageURL = searchPageURL.replaceAll("\\{lang_id\\}", langId);
//                    searchPageURL = searchPageURL.replaceAll("\\{season\\}", season);
//                    searchPageURL = searchPageURL.replaceAll("\\{ep\\}", episode);
//                    searchPageURL = searchPageURL.replaceAll("\\{serial_name\\}", serialName);
//                    break;
//                default:
//                    String searchString = URLEncoder.encode(params.get(0), "UTF-8");
//                    String  page = params.get(1);
//                    searchPageURL = searchPageURL.replaceFirst("\\{searchString\\}", searchString);
//                    searchPageURL = searchPageURL.replaceAll("\\{page\\}", page);
//                    break;
//
//            }
//
//            Log.i("url", searchPageURL);
//            StringBuilder content = new StringBuilder();
////            if (ACache.get(OneOm.context).equals())
//            URL url = new URL(searchPageURL);
//            // httpurlcinnection включить прокси(?)
//            // глобальная переменная - оспользовать прокси
//            //
//            Proxy proxy;
//            if (useProxy) {
//                SocketAddress sa = searchProxy(searchPageURL);
//                proxy = sa == null ? Proxy.NO_PROXY : new Proxy(Proxy.Type.HTTP, sa);
//            } else proxy = Proxy.NO_PROXY;
//
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(proxy);
//            urlConnection.setRequestProperty("User-Agent", System.getProperty("http.agent"));
//            urlConnection.setRequestMethod("GET");
//            urlConnection.connect();
//            InputStream inputStream = urlConnection.getInputStream();
//            InputStreamReader reader = new InputStreamReader(inputStream);
//            BufferedReader bufferedReader = new BufferedReader(reader);
//
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                content.append(line);
//            }
//            bufferedReader.close();
//            String result = content.toString();
//            Log.i("response in search", urlConnection.getResponseCode() + "");
//            Log.i("response in search", url.getPath());
//            Log.i("response in search", result);
//            Log.i("response in search", urlConnection.getResponseMessage());
//            Log.i("response in search", urlConnection.getHeaderFields().toString());
//            System.out.println(result);
////            ACache.get(OneOm.context).put(searchPageURL, result, time);
//            return result;
//        } catch (IOException e) {
////            OneOm.handleError(Thread.currentThread(), e, "Web.search(Origin,ArrayList<String>,int,boolean) IOException");
//            e.printStackTrace();
//        }
//        return null;
//    }

//    public static SocketAddress searchProxy(String targetUrl) {
//        if (proxies.size() == 0) {
//            String proxyResponse = Web.GET(url.proxies, false, null);
//            if (proxyResponse != null) {
//                ArrayList<String> proxyList = new ArrayList<>(Arrays.asList(proxyResponse.split("\n")));
//                for (String p : proxyList) {
//                    String[] proxy = p.split(":");
//                    String ip = proxy[0];
//                    int port = Integer.parseInt(proxy[1]);
//                    final InetSocketAddress socketAddress = new InetSocketAddress(ip, port);
//                    proxies.put(ip, socketAddress);
//                }
//                return lookOverProxies(targetUrl);
//            }
//        } else {
//            return lookOverProxies(targetUrl);
//        }
//
//
//        return null;
//    }
//
//    public static SocketAddress lookOverProxies(final String targetUrl) {
//        final ExecutorService service = Executors.newFixedThreadPool(proxies.size());
//        Log.d("host-port", proxies.size() + "");
//
//        ArrayList<Callable<SocketAddress>> searchThreads = new ArrayList<>();
//
//        for (final Map.Entry<String, SocketAddress> entry : proxies.entrySet()) {
//            searchThreads.add(new Callable<SocketAddress>() {
//                @Override
//                public SocketAddress call() throws Exception {
//                    Log.d("host-port", entry.getKey() + " " + entry.getValue());
//                    SocketAddress socketAddress = checkProxy("http://www.google.com", entry.getKey(), entry.getValue());
//                    if (socketAddress != null) {
//                        socketAddress = checkProxy(targetUrl, entry.getKey(), entry.getValue());
//                    }
//
//                    return socketAddress;
//                }
//            });
//        }
//
//        for (Callable<SocketAddress> callable : searchThreads) {
//            Future<SocketAddress> result = service.submit(callable);
//
//            SocketAddress socketAddress = null;
//            try {
//                socketAddress = result.get();
//            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//            }
//
//            if (socketAddress != null) {
//                return socketAddress;
//            }
//        }
//
//        service.shutdown();
//        try {
//            service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
//        } catch (InterruptedException e) {
////            OneOm.handleError(Thread.currentThread(), e, "Web.lookOverProxies(String) InterruptedException");
//            e.printStackTrace();
//        }
//
//        return null;
//    }

//    private static SocketAddress checkProxy(String url, String key, SocketAddress value) {
//        String response = GET(url, false, value);
//        if (response != null) {
//            return value;
//        } else {
//            synchronized (proxies) {
//                proxies.remove(key);
//            }
//            return null;
//        }
//    }

//    public static String disableSSLCertificateChecking(String s, boolean fromOneOm) {
//        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
//            public X509Certificate[] getAcceptedIssuers() {
//                return null;
//            }
//
//            @Override
//            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
//                // Not implemented
//            }
//
//            @Override
//            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
//                // Not implemented
//            }
//        } };
//
//        try {
//            SSLContext sc = SSLContext.getInstance("TLS");
//
//            sc.init(null, trustAllCerts, new java.security.SecureRandom());
//
//            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//
//            StringBuilder content = new StringBuilder();
//
//            URL url = new URL(s);
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestProperty("User-Agent", System.getProperty("http.agent"));
//            if (fromOneOm) urlConnection.setRequestProperty("Content-Type", header.appjson);
//            if (fromOneOm) urlConnection.setRequestProperty("Accept", header.appjson);
//            urlConnection.setRequestMethod("GET");
//            urlConnection.connect();
//            InputStream inputStream = urlConnection.getInputStream();
//            InputStreamReader reader = new InputStreamReader(inputStream);
//            BufferedReader bufferedReader = new BufferedReader(reader);
//
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                content.append(line);
//            }
//            bufferedReader.close();
//            return content.toString();
//
//
//        } catch (KeyManagementException | NoSuchAlgorithmException | IOException e) {
////            OneOm.handleError(Thread.currentThread(), e, "Web.disableSSLSertificateChecking()");
//            e.printStackTrace();
//        }
//        return s;
//    }

    public static String POST(String requestURL, HashMap<String, String> postDataParams) {

        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(Editor.httpDataPairsToString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";
            }
        } catch (Exception e) {
//            OneOm.handleError(Thread.currentThread(), e, "Web.POST(String, Map<String,String>)");
            e.printStackTrace();
        }

        return response;
    }

    public static String POST(String requestURL, String pairs) {

        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(pairs);

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";
            }
        } catch (Exception e) {
//            OneOm.handleError(Thread.currentThread(), e, "Web.POST(String,String)");
            e.printStackTrace();
        }

        return response;
    }

    public static void downloadFileFromURL (Context context, String url, String description, String title) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription(description);
        request.setTitle(title);
// in order for this if to run, you must use the android 3.2 to compile your app
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, Editor.filenameFromURL(url));

// get download service and enqueue file
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    public static void openUrlInWebView(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(context.getString(R.string.url_for_web_view_activity), url);
        context.startActivity(intent);
    }

    public static boolean isDownloadManagerAvailable(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return true;
        }
        return false;
    }


//    static class HttpPatch extends HttpPost {
//
//        public static final String METHOD_PATCH = "PATCH";
//
//        public HttpPatch(final String url) {
//            super(url);
//        }
//
//        @Override
//        public String getMethod() {
//            return METHOD_PATCH;
//        }
//    }
}
