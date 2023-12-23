package com.example.myapplication.utils;

import java.net.MalformedURLException;
import java.net.URL;

public class Utils {
    public static String getDomain(String urlString) {
        try {
            // Chuyển đổi đường dẫn URL thành đối tượng URL
            URL url = new URL(urlString);

            // Lấy host từ URL
            String host = url.getHost();

            // Kiểm tra xem host có bắt đầu bằng "www." không và loại bỏ nếu có
            if (host.startsWith("www.")) {
                host = host.substring(4);
            }

            return host;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
