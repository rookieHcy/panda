package com.houcy7.panda;

import com.houcy7.panda.util.HttpClientUtil;
import com.houcy7.panda.util.PdfDownloadUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName TestHttpClient
 * @Description TODO
 * @Author hou
 * @Date 2020/5/15 3:20 下午
 * @Version 1.0
 **/
public class TestHttpClientUtil {

    private final static String url = "https://dx.doi.org/";
    private final static String savePath = "/data/pdfs";
    private final static int SIZE = 5018492;

    public static void main(String[] args) throws Exception {
        Map<String, String> param = new HashMap<String, String>() {{
            put("hdl", "10.1021/acs.macromol.0c00650.s001");
        }};
//        String html =  "<html><head><title>Handle Redirect</title></head><body><a href=\"https://pubs.acs.org/doi/suppl/10.1021/acs.macromol.0c00650/suppl_file/ma0c00650_si_001.pdf\">https://pubs.acs.org/doi/suppl/10.1021/acs.macromol.0c00650/suppl_file/ma0c00650_si_001.pdf</a></body></html>";
//         <html><head><title>titleHandle Redirect</title></head><body><a href="https://pubs.acs.org/doi/suppl/10.1021/acs.macromol.0c00650/suppl_file/ma0c00650_si_001.pdf">https://pubs.acs.org/doi/suppl/10.1021/acs.macromol.0c00650/suppl_file/ma0c00650_si_001.pdf</a></body></html>
        String html = HttpClientUtil.httpPost(url, param, 1000);
        System.out.println(html);
        Document doc = Jsoup.parse(html);
        // https://pubs.acs.org/doi/suppl/10.1021/acs.macromol.0c00650/suppl_file/ma0c00650_si_001.pdf
        String attr = doc.select("a").attr("href");
        System.out.println(attr);

        Map<String, String> header = new HashMap<String, String>(){{
            put("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
            put("accept-encoding", "gzip, deflate, br");
            put("accept-language", "zh-CN,zh;q=0.9");
            put("cache-control", "no-cache");
            put("cookie", "__cfduid=de862d1407a7f1bda7ec1bc93d0df777a1589529553; ACSEnt=692614_6105_1589533271203; SERVER=WZ6myaEXBLGvp/nLMd/Vhg==; MAID=P6ARKYGZ5Gv5ii3PHHaJeg==; MACHINE_LAST_SEEN=2020-05-15T02%3A01%3A11.216-07%3A00; JSESSIONID=aaamjszI_5l0r81-wZ8hx; JSESSIONID=aaamjszI_5l0r81-wZ8hx; _ga=GA1.2.659142673.1589533272; _gid=GA1.2.353244887.1589533272; _gat=1; visid_incap_2209364=wmk0cIecTByJa5Gi7CAoMFdavl4AAAAAQUIPAAAAAAArpDNa1P8CCt1B75KeWkBN; nlbi_2209364=PfghJxIDrGMVmpPz7NhdMAAAAACJn00OtQApbUVMrMct8jSw; incap_ses_626_2209364=BrUuI1210gtYt5C7QQGwCFhavl4AAAAAlupXaTlAoi7qmCOd9GdscQ==; _fbp=fb.1.1589533280488.172667297; SnapABugRef=https%3A%2F%2Fpubs.acs.org%2F%20; SnapABugHistory=1#; SnapABugUserAlias=%23; SnapABugVisit=1#1589533281; __gads=ID=d81b1590b61d9a41:T=1589533281:S=ALNI_MaEqCgcaqDwqOeWWE8TPvPYGDs8ew; __adroll_fpc=67c64f0222821b72c37c9aa453ad9b06-1589533285249; __ar_v4=%7C3LBZJ4KXKBF2PJ46QCMT3X%3A20200514%3A1%7C6EITYRDI4FFGJPSZQ62UJJ%3A20200514%3A1%7CYYNIJCDRSZBE5PTMWNR5WD%3A20200514%3A1");
            put("pragma", "no-cache");
            put("referer", "https://dx.doi.org/");
            put("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");
        }};


//        String s = HttpClient.httpsGet(attr, savePath, header);
        String s = PdfDownloadUtil.downLoadByUrl(attr, savePath, header);
        System.out.println(s);

    }
}