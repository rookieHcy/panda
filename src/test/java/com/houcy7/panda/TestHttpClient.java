package com.houcy7.panda;

import com.houcy7.panda.util.PdfDownloadUtil;

import java.io.File;
import java.io.IOException;

/**
 * @ClassName TestHttpClient
 * @Description TODO
 * @Author hou
 * @Date 2020/5/15 3:20 下午
 * @Version 1.0
 **/
public class TestHttpClient {

    private final static String url = "https://sci-hub.im/downloads-ii/2020-04-10/11/chen2020.pdf?download=true";
    private final static String savePath = "/data/pdfs";
    private final static int SIZE = 5018492;

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                for (int i1 = 0; i1 < 10; i1++) {
                    try {
                        String s = PdfDownloadUtil.downLoadByUrl(url, savePath);
                        File file = new File(savePath + "/" + s);
                        if(file.length() != SIZE){
                            System.out.println("bingo!bingo!bingo!bingo!bingo!");
                        }
                        System.out.println("Thread = " + Thread.currentThread().getName() + "; name = " + s + "; size = " + file.length());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}