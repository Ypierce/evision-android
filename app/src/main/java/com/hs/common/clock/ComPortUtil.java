package com.hs.common.clock;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ComPortUtil {
    public static String receive(InputStream inputStream) {
        byte[] buffer = new byte[64];
        if (inputStream == null) return null;
        int size = 0;
        //ToastUtils.showCenter(this, "开始读取字节内容");
        try {
            size = inputStream.read(buffer);
            if (size > 0) {
                String str = bcdToString(buffer);
                return str;
            } else {
                return "";
            }
        } catch (IOException e) {
            return null;
        }
    }

    public static boolean send(String str, OutputStream outputStream) throws IOException {
        byte[] data = str.getBytes();
        return send(data, outputStream);
    }

    public static boolean send(byte[] data, OutputStream outputStream) throws IOException {

        if (data.length > 0) {
            outputStream.write(data);
            outputStream.flush();
            return true;
        }
        return false;
    }


    /**
     * 将String转成BCD码
     * @param s
     * @return
     */
    public static byte[] StrToBCDBytes(String s) {
        s = s.replace(" ", "");
        if (s.length() % 2 != 0) {
            s = "0" + s;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        char[] cs = s.toCharArray();
        //十六进制大小写转换
        for (char c : cs) {
            c = Character.toUpperCase(c);
        }
        for (int i = 0; i < cs.length; i += 2) {
            int high = cs[i] > 64 ? cs[i] - 55 : cs[i] - 48;
            int low = cs[i + 1] > 64 ? cs[i + 1] - 55 : cs[i + 1] - 48;
            baos.write(high << 4 | low);
        }
        return baos.toByteArray();
    }

    /**
     * 将BCD码转成String
     *
     * @param b
     * @return \
     */
    public static String bcdToString(byte[] b) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            int h = ((b[i] & 0xff) >> 4);
            h = h > 9 ? h + 55 : h + 48;
            sb.append((char) h);
            int l = (b[i] & 0x0f);
            l = l > 9 ? l + 55 : l + 48;
            sb.append((char) l);
        }
        return sb.toString();
    }
}
