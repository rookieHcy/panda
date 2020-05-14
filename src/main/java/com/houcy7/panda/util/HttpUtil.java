package com.houcy7.panda.util;

import com.houcy7.panda.enums.CharSetEnum;
import com.houcy7.panda.enums.ContentTypeEnum;
import org.apache.http.HttpEntity;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * 原生java库封装http请求，包含post与get
 *
 * @author ningning.wei
 * @version 17/5/11.
 */
public class HttpUtil {

    private static Logger logger = getLogger(HttpUtil.class);
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * 默认超时时间为30秒
     */
    private static int DEFAULT_TIME_OUT = 60000;


//

    /**
     * content type 为form时（入参为Key－Value）的请求方式，默认超时时间为30秒
     *
     * @return respone的body
     */
    public static String sendPost(String url, Map<String, String> params, CharSetEnum charSet) {
        try {
            String urlModified = url.replace("api.tcredit.com", "api.tcredit.com:9090/tcredit");
            String requestBody = buildParam(params, charSet);
            return sendPost(urlModified, requestBody, ContentTypeEnum.FORM_URLENCODE, charSet);
        } catch (Exception ex) {
            logger.error("[HttpUtil] 请求参数拼装出错：", ex);
            return null;
        }
    }

    /**
     * jdk 原生 http post 请求，默认超时时间为30秒
     *
     * @param url         请求地址
     * @param body        入参String
     * @param contentType 内容类型
     * @param charSet     编码格式
     * @return respone的body
     */
    public static String sendPost(String url, String body, ContentTypeEnum contentType, CharSetEnum charSet) {
        return sendPost(url, body, contentType, charSet, DEFAULT_TIME_OUT);
    }

    /**
     * JDK原生post请求
     *
     * @param url         访问地址
     * @param body        request请求体
     * @param contentType 请求类型
     * @param charSet     编码格式
     * @param timeOut     超时时间，单位毫秒
     * @return 响应体string
     */
    public static String sendPost(String url, String body, ContentTypeEnum contentType, CharSetEnum charSet, int timeOut) {
        URLConnection conn = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            conn = realUrl.openConnection();
            // 设置通用的请求头属性
            conn.setRequestProperty("Content-Type", String.format("%s;charset=%s", contentType.getName(), charSet.getName()));
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setConnectTimeout(timeOut);
            conn.setReadTimeout(timeOut);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
        } catch (Exception e) {
            logger.error(String.format("[HttpUtil] 连接错误："), e);
            return null;
        }
        return sendRequest(conn, body, charSet);
    }


    /**
     * 发送GET请求
     *
     * @param url     访问地址
     * @param params  k-v的形式
     * @param charSet 字符集枚举型
     * @return 远程响应结果response Body
     */
    public static String sendGet(String url, Map<String, String> params, CharSetEnum charSet) {
        return sendGet(url, params, charSet, DEFAULT_TIME_OUT);
    }

    /**
     * 发送GET请求
     *
     * @param url     目的地址
     * @param params  k-v形式，请求参数，Map类型。
     * @param charSet 服务段字符编码格式
     * @param timeOut 超时时间，单位毫秒
     * @return 远程响应结果response Body
     */
    public static String sendGet(String url, Map<String, String> params,
                                 CharSetEnum charSet, int timeOut) {
        StringBuffer stringBuffer = new StringBuffer();// 存储参数
        String paramUrl = "";// 编码之后的参数
        HttpURLConnection httpConn = null;
        try {
            if (params != null) {
                // 编码请求参数
                if (params.size() == 1) {
                    for (String name : params.keySet()) {
                        stringBuffer.append(name).append("=").append(
                                URLEncoder.encode(params.get(name),
                                        charSet.getName()));
                    }
                    paramUrl = stringBuffer.toString();
                } else {
                    for (String name : params.keySet()) {
                        stringBuffer.append(name).append("=").append(
                                URLEncoder.encode(params.get(name),
                                        charSet.getName())).append("&");
                    }
                    String temp_params = stringBuffer.toString();
                    paramUrl = temp_params.substring(0, temp_params.length() - 1);
                }

            }
            String full_url = url;
            if (paramUrl.length() > 0)
                full_url = full_url + "?" + paramUrl;

            logger.debug(full_url);
            LOGGER.info(full_url);
//          System.out.println(full_url);

            // 创建URL对象
            URL connURL = new URL(full_url);
            // 打开URL连接
            httpConn = (HttpURLConnection) connURL
                    .openConnection();
            // 设置通用属性
            httpConn.setRequestProperty("Accept", "*/*");
            httpConn.setRequestProperty("Connection", "Keep-Alive");
            httpConn.setRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
            httpConn.setConnectTimeout(timeOut);
            httpConn.setReadTimeout(timeOut);
            // 建立实际的连接
            httpConn.connect();
            // 响应头部获取


        } catch (Exception e) {
            logger.error(String.format("[HttpUtil] 连接错误："), e);
            return null;

        }
        return sendRequest(httpConn, null, charSet);
    }

    private static String sendRequest(URLConnection conn, String body, CharSetEnum charSet) {
        OutputStreamWriter ow;
        PrintWriter out = null;
        InputStreamReader ir;
        BufferedReader in = null;// 读取响应输入流
        StringBuilder stringBuilder = new StringBuilder();
        ;

        try {
            if (body != null) {
                ow = new OutputStreamWriter(conn.getOutputStream(), charSet.getName());
                out = new PrintWriter(ow, true);
                // 发送请求参数
                out.print(body);
                // flush输出流的缓冲
                out.flush();
            }
            // 定义BufferedReader输入流来读取URL的响应
            Map<String, List<String>> headers = conn.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : headers.keySet()) {
//                System.out.println(key + "\t：\t" + headers.get(key));
                logger.debug(String.format("\t%s : \t%s", key, headers.get(key)));
            }

            ir = new InputStreamReader(conn.getInputStream(), charSet.getName());
            in = new BufferedReader(ir);
            String line;
            while ((line = in.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e1) {
            logger.error(String.format("[HttpUtil] 请求发送错误："), e1);
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (Exception ex) {
                logger.error(String.format("[HttpUtil] 连接断开错误："), ex);
            }
        }

        return stringBuilder.toString();
    }

    /**
     * 构建入参，将参数构建为 key1=value1&key2=value2&...keyn=valuen 的形式
     *
     * @param param 将map改造成 k1＝v1&k2=v2&k3=v3....kn=vn(n为map的size)
     * @return 构造好的字符串，如果map为null则返回空字符串
     */
    public static String buildParam(Map<String, String> param, CharSetEnum charSetEnum) throws Exception {
        if (param == null || param.isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : param.entrySet()) {
            if (StringUtil.isBlank(entry.getValue())) {
                builder.append(String.format("%s=%s&", entry.getKey(), entry.getValue()));
            } else {
                builder.append(String.format("%s=%s&", entry.getKey(), URLEncoder.encode(entry.getValue(), charSetEnum.getName())));
            }
        }
        builder.deleteCharAt(builder.length() - 1);// delete the last "&"
        return builder.toString();
    }

    public static String sendPost(String url, String body) {
        return sendPost(url, body, ContentTypeEnum.JSON, CharSetEnum.UTF8);
    }


//    /**
//     * 上传文件
//     */
//    public static String uploadFile(String url, MultipartFile file,
//                                    String fileName, Map<String, String> params, int timeout)
//            throws Exception {
//        String respStr = null;
//        BasicHttpClientConnectionManager clientConnectionManager = new BasicHttpClientConnectionManager();
//        SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(timeout).build();
//
//        CloseableHttpClient httpclient = null;
//        try {
//            httpclient = HttpClientBuilder.create().setConnectionManager(clientConnectionManager).build();
//            HttpPost httppost = new HttpPost(url);
//            RequestConfig config = RequestConfig.custom().setConnectTimeout(timeout)
//                    .setConnectionRequestTimeout(timeout).build();
//            clientConnectionManager.setSocketConfig(socketConfig);
//            httppost.setConfig(config);
//            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.RFC6532);
//            multipartEntityBuilder.addBinaryBody(fileName, file.getInputStream(), ContentType.DEFAULT_BINARY, file.getOriginalFilename());
//            // 设置上传的其他参数
//            setUploadParams(multipartEntityBuilder, params);
//
//            HttpEntity reqEntity = multipartEntityBuilder.build();
//            httppost.setEntity(reqEntity);
//
//            CloseableHttpResponse response = httpclient.execute(httppost);
//            try {
//                System.out.println(response.getStatusLine());
//                HttpEntity resEntity = response.getEntity();
//                respStr = getRespString(resEntity);
//                EntityUtils.consume(resEntity);
//            } finally {
//                response.close();
//            }
//        } finally {
//            if (httpclient != null)
//                httpclient.close();
//        }
//        System.out.println("resp=" + respStr);
//        return respStr;
//    }
//
//
//    /**
//     * 设置上传文件时所附带的其他参数
//     *
//     * @param multipartEntityBuilder
//     * @param params
//     */
//    private static void setUploadParams(MultipartEntityBuilder multipartEntityBuilder,
//                                        Map<String, String> params) {
//        if (params != null && params.size() > 0) {
//            Set<String> keys = params.keySet();
//            for (String key : keys) {
//                multipartEntityBuilder
//                        .addPart(key, new StringBody(params.get(key),
//                                ContentType.TEXT_PLAIN));
//            }
//        }
//    }

    /**
     * 将返回结果转化为String
     *
     * @param entity
     * @return
     * @throws Exception
     */
    private static String getRespString(HttpEntity entity) throws Exception {
        if (entity == null) {
            return null;
        }
        InputStream is = entity.getContent();
        StringBuffer strBuf = new StringBuffer();
        byte[] buffer = new byte[4096];
        int r = 0;
        while ((r = is.read(buffer)) > 0) {
            strBuf.append(new String(buffer, 0, r, "UTF-8"));
        }
        return strBuf.toString();
    }


}
