package com.zzwl.jpkit.network;


import com.zzwl.jpkit.core.JSON;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

public final class NetUtil {
    public final static String HTTP = "http://";
    public final static String HTTPS = "https://";
    private final static String GET = "GET";
    private final static String POST = "POST";


    private NetUtil() {
    }

    /**
     * 获取网络JSON内容
     *
     * @param url 网络地址
     * @return 网络JSON内容
     */
    public static String getJSON(String url) {
        return doGet(url, null);
    }

    /**
     * 获取网络JSON内容
     *
     * @param url  网络地址
     * @param pram 请求头参数
     * @return 网络JSON内容
     */
    public static String getJSON(String url, Map<String, String> pram) {
        return doGet(url, pram);
    }

    /**
     * get请求
     *
     * @param url  请求地址
     * @param pram 请求头参数
     * @return 结果
     */
    public static String doGet(String url, Map<String, String> pram) {
        return doService(url, GET, pram, null);
    }

    /**
     * post 请求
     *
     * @param url  请求地址
     * @param pram 请求头参数
     * @param data 请求参数
     * @return 结果
     */
    public static String doPost(String url, Map<String, String> pram, Map<String, Object> data) {
        return doService(url, POST, pram, data);
    }

    /**
     * 基础请求
     *
     * @param url    请求地址
     * @param method 请求方法
     * @param pram   请求头参数
     * @param data   请求参数
     * @return 结果
     */
    public static String doService(String url, String method, Map<String, String> pram, Map<String, Object> data) {
        InputStream is = null;
        PrintWriter out = null;
        try {
            URL u = new URL(url);
            HttpURLConnection con = (HttpURLConnection) u.openConnection();
            con.setRequestMethod(method);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            if (!Objects.isNull(pram)) {
                for (String s : pram.keySet()) {
                    con.setRequestProperty(s, pram.get(s));
                }
            }
            con.connect(); //获取连接
            // Post 请求传入参数
            if (method.equals(POST)) {
                out = new PrintWriter(con.getOutputStream());
                out.write(JSON.stringify(data).terse());
                out.flush();
            }
            is = con.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder s = new StringBuilder();
            String bs;
            while (!Objects.isNull(bs = reader.readLine())) {
                s.append(bs);
            }
            reader.close();
            return s.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                // log
                System.out.println(e.getMessage());
            }
        }
    }
}
