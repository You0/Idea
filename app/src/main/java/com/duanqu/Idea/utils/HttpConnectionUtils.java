package com.duanqu.Idea.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class HttpConnectionUtils {
    private URL url = null;
    private HttpURLConnection connection = null;
    private StringBuffer sb = null;

    public HttpConnectionUtils(String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void setURL(String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    public HttpURLConnection GetConnection(String Method, String strings, String Cookie) {
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
            connection.setRequestMethod(Method);
            // connection.setInstanceFollowRedirects(false);
            if (Method.equals("GET")) {
                connection.setRequestProperty("Cookie", Cookie);
                return connection;
            } else if (Method.equals("POST")) {
                connection.setRequestProperty("Cookie", Cookie);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                sb = new StringBuffer();
                sb.append(strings);
                return connection;
            } else {
                System.out.println("Method Error");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;


    }


    public HttpURLConnection GetConnection(String Method, ArrayList<String> strings, String Cookie) {
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
            connection.setRequestMethod(Method);
            // connection.setInstanceFollowRedirects(false);
            if (Method.equals("GET")) {
                connection.setRequestProperty("Cookie", Cookie);
                return connection;
            } else if (Method.equals("POST")) {
                connection.setRequestProperty("Cookie", Cookie);
                sb = new StringBuffer();
                //System.out.println(strings.size());
                for (int i = 0; i < strings.size(); i = i + 2) {
                    if (strings.get(i + 1).equals("#")) {
                        if (i + 1 == strings.size() - 1) {
                            sb.append(strings.get(i) + "=");
                        } else {
                            sb.append(strings.get(i) + "=&");
                        }
                    } else {
                        if (i + 1 == strings.size() - 1) {
                            sb.append(strings.get(i) + "=" + strings.get(i + 1));
                        } else {
                            sb.append(strings.get(i) + "=" + strings.get(i + 1) + "&");
                        }
                    }
                }
                System.out.println(sb.toString());
                return connection;
            } else {
                System.out.println("Method Error");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public void connect() {
        try {
            OutputStream os = connection.getOutputStream();
            os.write(sb.toString().getBytes("utf-8")); // ��Զ��URL���ݲ���
            os.flush();
            os.close();
            int code = connection.getResponseCode();
            //System.out.println(sb.toString());
            //System.out.println("HTTP״̬��:"+code);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void release() {
        if (connection != null) {
            connection.disconnect();
        }

    }

    public static String Read(HttpURLConnection Connection) {
        BufferedReader is = null;
        StringBuffer result = new StringBuffer();
        String line = null;
        try {
            is = new BufferedReader(new InputStreamReader(Connection.getInputStream(), "utf-8"));

            line = is.readLine();

            while (line != null) {
                result.append(line);
                line = is.readLine();

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result.toString();
    }
}
