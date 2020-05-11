package com.houcy7.panda.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author : zixiu
 * @desc :
 * @email : zixiu@icongtai.com
 * @date : 2020/4/3 23:57
 **/

@Slf4j
public class HttpClient {

    private final static Integer EXPIRED_TIME = 60;

    //thread-safe
    private static CloseableHttpClient httpClient;
    private static PoolingHttpClientConnectionManager cm;

    static {
        init();
        closeExpiredConnectionsPeriodTask(EXPIRED_TIME);
    }

    private static void init() {
        cm = new PoolingHttpClientConnectionManager();
        // max connections
        cm.setMaxTotal(20);
        // max connections per route
        cm.setDefaultMaxPerRoute(2);
        // set max connections for a specified route
//        cm.setMaxPerRoute();

        final RequestConfig requestConfig = RequestConfig.custom()
                // the socket timeout (SO_TIMEOUT) in milliseconds
                .setSocketTimeout(5000)
                // the timeout in milliseconds until a connection is established.
                .setConnectTimeout(5000)
                // the timeout in milliseconds used when requesting a connection from the connection pool.
                .setConnectionRequestTimeout(5000)
                .build();
        httpClient = HttpClients.custom().setConnectionManager(cm).setDefaultRequestConfig(requestConfig).build();
    }

    private static void closeExpiredConnectionsPeriodTask(int timeUnitBySecond) {
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    TimeUnit.SECONDS.sleep(timeUnitBySecond);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                cm.closeExpiredConnections();
            }
        }).start();
    }

    public static String httpClientGet(String url, Charset charset) throws IOException {
        //创建http post
//        HttpPost httpPost = new HttpPost(url);
        HttpGet httpGet = new HttpGet(url);
        //模拟浏览器设置头
        httpGet.setHeader(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36");
//        httpGet.setHeader("Content-type", "application/json");

        //返回类型
        CloseableHttpResponse response = null;

        try {
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), charset);
                return content;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
            httpClient.close();
        }
        return null;
    }


    public static String httpsGet(String url, String path) throws IOException {
        //SSL Context
//        SSLContext sslContext = SSLContext.getInstance("SSL");
//        TrustManager[] tm = {}

        URL u = new URL(url);
        HttpURLConnection urlConnection = (HttpURLConnection) u.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36");

        urlConnection.setConnectTimeout(6000000);
        urlConnection.setReadTimeout(6000000);
        int responseCode = urlConnection.getResponseCode();
        log.info("GET Response Code :: {}", responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            try {
                InputStream inputStream = urlConnection.getInputStream();
                String saveResult = saveFile(inputStream, path);
                if (!StringUtils.isEmpty(saveResult)) {
                    inputStream.close();
                    log.info("inputStreamClose");
                }
                return saveResult;
            } catch (IOException e) {
                log.error("HttpClient error: {}", e.getMessage());
            } finally {
                urlConnection.disconnect();
                log.info("urlConnection closed.");
            }
        } else {
            log.error("GET request not worked");
        }
        return null;
    }

    private static String saveFile(InputStream inputStream, String path) throws IOException {
        //文件保存位置
        File saveDir = new File(path);
        if (!saveDir.exists()) {
            boolean mkdirResult = saveDir.mkdir();
            if (mkdirResult) {
                log.info("HttpClient create dir {}！", path);
            } else {
                log.error("HttpClient create dir {} error！", path);
            }

        }

        String fileName = DateUtil.getYear2MS() + ".pdf";

        String savePath = saveDir + File.separator + fileName;
        File file = new File(savePath);
        FileOutputStream fos = new FileOutputStream(file);
        try {
            int byteread = 0;
            byte[] buffer = new byte[1204];
            while ((byteread = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, byteread);
            }
            log.info("{}  download finished!", fileName);
        } catch (IOException e) {
            log.error("error : {}", Arrays.toString(e.getStackTrace()));
            return null;
        } finally {
            fos.flush();
            fos.close();
        }
        return fileName;
    }

    //文件流到字节数组
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

//    public static void main(String[] args) throws IOException {
////        String s = httpClientGet("https://sci-hub.shop/downloads-ii/2020-03-11/93/10.1038@s41467-020-14336-7.pdf?download=true", StandardCharsets.UTF_8);
////        boolean result = httpsGet("https://sci-hub.shop/downloads-ii/2020-03-11/93/10.1038@s41467-020-14336-7.pdf?download=true");
//        boolean result = httpsGet("https://sci-hub.shop/downloads-ii/10.1021/acsmacrolett.9b00606?download=true", "download/");
//        if (result) {
//            System.exit(0);
//        }
//    }
}
