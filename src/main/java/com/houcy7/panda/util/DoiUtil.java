package com.houcy7.panda.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName DoiUtil
 * @Description 根据论文名称获取doi
 * @Author hou
 * @Date 2020/5/13 5:02 下午
 * @Version 1.0
 **/
@Slf4j
public class DoiUtil {
    private final static String POI_URL = "https://api.crossref.org/works?query=%22content%22&";

    public static String getDoi(String content) {
        try {
            String replace = content.replaceAll("\\s", "+");
            log.info(replace);
            String url = POI_URL.replace("content", replace);
            log.info("真正获取doi的url是：{}", url);
            String result = HttpClient.httpClientGet(url, StandardCharsets.UTF_8);

            // 此处可能会返回很多个结果 只取第一个
            // 查看样例 https://api.crossref.org/works?query=%22Nitric%20Oxide-Induced%20Stromal%20Depletion%20for%20Improved%20Nanoparticle%20Penetration%20in%20Pancreatic%20Cancer%20Treatment%22&
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (!"OK".equalsIgnoreCase(jsonObject.getString("status"))) {
                throw new RuntimeException("crossref返回结果状态码不为ok");
            }

            // 获取doi所在的数组
            JSONArray jsonArray = jsonObject.getJSONObject("message").getJSONArray("items");
            if (null == jsonArray || jsonArray.isEmpty()) {
                throw new RuntimeException("crossref返回结果数组为空");
            }

            // 返回结果不为空 获取第一个
            log.info("crossref返回结果个数：{}，只取第一个", jsonArray.size());
            JSONObject DOI = jsonArray.getJSONObject(0);
            String doi = DOI.getString("DOI");
            log.info("{}通过crossref获取DOI完成, doi:{}", content, doi);
            return doi;
        } catch (Exception e) {
            log.error("{}通过crossref获取DOI异常, {}", content, e.getMessage());
            throw new RuntimeException(String.format("%s通过crossref获取DOI异常, %s", content, e.getMessage()));
        }
    }

    public static void main(String[] args) {
//        String doi = getDoi("Nitric Oxide-Induced Stromal Depletion for Improved Nanoparticle Penetration in Pancreatic Cancer Treatment");
        String doi = getDoi("ATP Suppression by pH-Activated Mitochondria-Targeted Delivery of Nitric Oxide Nanoplatform for Drug Resistance Reversal and Metastasis Inhibition");
        System.out.println(doi);
    }
}