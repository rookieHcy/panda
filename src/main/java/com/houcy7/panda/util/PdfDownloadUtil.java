package com.houcy7.panda.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName PdfDownloadUtil
 * @Description TODO
 * @Author hou
 * @Date 2020/5/15 5:44 下午
 * @Version 1.0
 **/
@Slf4j
public class PdfDownloadUtil {

    /**
     * 从网络Url中下载文件
     *
     * @param urlStr
     * @param savePath
     * @throws IOException
     */
    public static String downLoadByUrl(String urlStr, String savePath, Map<String, String> header) throws Exception {
        String fileName = null;
        URL url = new URL(urlStr);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        header.forEach(urlConnection::setRequestProperty);
        int responseCode = urlConnection.getResponseCode();
        log.info("GET Response Code :: {}", responseCode);
        FileOutputStream fos = null;
        ByteArrayOutputStream bos = null;
        InputStream inputStream = null;
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            try {
                fileName = DateUtil.getYear2MS() + ".pdf";
                inputStream = urlConnection.getInputStream();
                byte[] buffer = new byte[1024 * 8];
                int len = 0;
                bos = new ByteArrayOutputStream();
                int count = 0;
                while ((len = inputStream.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                    if (bos.size() / 1000 != count) {
                        count ++;
                        log.info("{}下载了{}k", fileName, count);
                    }
                }

                log.info("{}下载了{}kb, {}字节", fileName, count, bos.size());
                //文件保存位置
                File saveDir = new File(savePath);
                if (!saveDir.exists()) {
                    boolean mkdirResult = saveDir.mkdir();
                    if (mkdirResult) {
                        log.info("HttpClient create dir {}！", savePath);
                    } else {
                        log.error("HttpClient create dir {} error！", savePath);
                    }
                }

                File file = new File(saveDir + File.separator + fileName);
                fos = new FileOutputStream(file);
                fos.write(bos.toByteArray());
                fos.close();
                inputStream.close();
            } catch (IOException e) {
                log.error("HttpClient error: {}", e.getMessage());
            } finally {
                if (fos != null) {
                    fos.close();
                }
                if (bos != null) {
                    bos.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                urlConnection.disconnect();
                log.info("urlConnection closed.");
            }
        } else {
            log.error("GET request not worked");
        }
        return fileName;
    }


    public static String downLoadByUrl(String url, String savePath) throws Exception {
        Map<String, String> header = new HashMap<>();
        header.put("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        return downLoadByUrl(url, savePath, header);
    }


}