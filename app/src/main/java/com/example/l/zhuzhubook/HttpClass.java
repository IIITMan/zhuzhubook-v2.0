package com.example.l.zhuzhubook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class HttpClass {
    public String OpenGetUrl(String strUrl) {
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.connect();
//            InputStream inputStream = conn.getInputStream();
//            byte[] data = new byte[1024];
//            byte[] da = new byte[0];
//            String htmJson = null;
//            int length = 0;
//            while ((length = inputStream.read(data)) != -1) {
////                da=new byte[data.length+da.length];
////                da += data;
//                htmJson += new String(data, 0, length, Charset.forName("utf-8"));
//            }
//            htmJson=new String(htmJson.getBytes(),"utf-8");
            String data = null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer buffer = new StringBuffer();
            while ((data = reader.readLine()) != null) {
                buffer.append(data);
            }
            reader.close();
            conn.disconnect();
            return buffer.toString();
        } catch (Exception ex) {
            return null;
        }
    }

    public Bitmap GetImage(String strUrl) {
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.connect();
            InputStream inputStream = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            conn.disconnect();
            return bitmap;
        } catch (Exception ex) {
            return null;
        }
    }

    public String SaveImage(String url) {
        try {
            Bitmap bitmap = GetImage(url);
            String galleryPath = Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DCIM + File.separator + "zhuzhuBook" + File.separator;
            File file = new File(galleryPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            String fileName = +System.currentTimeMillis() + ".jpg";
            file = new File(galleryPath, fileName);
            FileOutputStream fos = new FileOutputStream(file);
            boolean IsSuccess = bitmap.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();
            return IsSuccess ? file.getPath() : null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }
}
