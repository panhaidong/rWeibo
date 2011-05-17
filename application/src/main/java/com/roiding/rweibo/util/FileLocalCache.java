package com.roiding.rweibo.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import com.roiding.rweibo.Constants;



/**
 * sdcard缓存文件存取工具类 添加按行读取函数:storeLine(),loadLine()
 * 
 */
public class FileLocalCache {
    private static final String cacheDir = Constants.CACHE_STORE_PATH + "/cache/";

    public static boolean checkDir() {

        File f = new File(cacheDir);
        if (!f.exists()) {
            return f.mkdirs();
        }
        return true;
    }

    /**
     * 根据url从sdcard取得缓存文件读取输入流
     * 
     * @param url 生成文件名的URL
     * @return 文件输入流FileInputStream
     */
    public static InputStream load(String url) {
        if (checkDir()) {

            String md5 = md5(url);
            File f = new File(cacheDir + md5);
            if (f.exists()) {
                try {
                    return new FileInputStream(f);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 将输入流保存到sdcard文件，并返回该文件的FileInputStream
     * 
     * @param url 生成文件名的URL
     * @param in 输入流
     * @return 保存文件的FileInputStream
     */
    public static InputStream save(String url, InputStream in) {
        if (checkDir()) {
            String md5 = md5(url);
            try {
                File f = new File(cacheDir + md5);
                if (!f.exists()) {
                    f.createNewFile();
                }
                FileOutputStream out = new FileOutputStream(f);

                int b;
                do {
                    b = in.read();
                    if (b != -1) {
                        out.write(b);
                    }
                } while (b != -1);
                in.close();
                out.flush();
                out.close();
                return new FileInputStream(f);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return in;
        }

    }

    /**
     * 将url转换成md5对应的字符串
     * 
     * @param url
     * @return
     */
    public static String md5(String url) {
        try {
            MessageDigest md5 = java.security.MessageDigest.getInstance("MD5");
            md5.update(url.getBytes("UTF-8"));
            byte messageDigest[] = md5.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String t = Integer.toHexString(0xFF & messageDigest[i]);
                if (t.length() == 1) {
                    hexString.append("0" + t);
                } else {
                    hexString.append(t);
                }
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void printErrorStackTrace(Exception ex) {
        if (FileLocalCache.checkDir()) {

            File f = new File("/sdcard/fojapalm/error.log");

            try {
                if (!f.exists())
                    f.createNewFile();
                ex.printStackTrace(new PrintStream(f));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 清空sdcard下的文件
     */
    public static void clearCache() {
        File f = new File(cacheDir);
        if (f.exists() && f.isDirectory()) {
            File flist[] = f.listFiles();
            for (int i = 0; flist != null && i < flist.length; i++) {
                flist[i].delete();
            }

        }
    }

    /**
     * 得到缓存文件sdcard的大小
     * 
     * @return
     */
    public static String getCacheSize() {
        long size = 0;
        File f = new File(cacheDir);
        if (f.exists() && f.isDirectory()) {
            File flist[] = f.listFiles();
            for (int i = 0; flist != null && i < flist.length; i++) {
                size = size + flist[i].length();
            }

        }
        if (size < 1000) {
            return "0k";
        } else if (size < 1000000) {
            return size / 1000 + "k";
        } else {
            return size / 1000000 + "m";
        }
    }

    /**
     * 根据URL从sdcard缓存文件读取数据
     * 
     * @param url 指定文件名称的URL
     * @return 文件内容字符串
     */
    public static String load2(String url) {
        String md5 = md5(url);
        File f = new File(cacheDir + md5);
        // Cache in an hour
        long expiredTime = 3600000l;
        // 没有超时时间
        expiredTime = Long.MAX_VALUE;

        if (f.exists() && System.currentTimeMillis() - f.lastModified() < expiredTime) {

            try {
                FileInputStream fstream = new FileInputStream(f);
                long length = f.length();

                byte[] bytes = new byte[(int) length];

                // Read in the bytes
                int offset = 0;
                int numRead = 0;
                while (offset < bytes.length && (numRead = fstream.read(bytes, offset, bytes.length - offset)) >= 0) {
                    offset += numRead;
                }

                return new String(bytes, "UTF-8");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 将URL抓取的内容保存到sdcard缓存文件
     * 
     * @param url url
     * @param c url对应的文件内容字符串
     * @param append true，表示追加到文件中
     */
    public static void store(String url, String c, boolean append) {
        if (!checkDir())
            return;

        String md5 = md5(url);
        File f = new File(cacheDir + md5);

        try {
            FileOutputStream out;
            out = new FileOutputStream(f, append);
            out.write(c.getBytes("UTF-8"));
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // /**
    // * 将日志保存到sdcard缓存文件
    // *
    // * @param url 文件名url
    // * @param log 日志内容
    // * @param append true，表示追加到文件中
    // */
    // public static void storeLine(String url, String log, boolean append) {
    // if (!checkDir()) return;
    //
    // String md5 = md5(url);
    // File f = new File(cacheDir + md5);
    // try {
    // FileOutputStream out;
    // out = new FileOutputStream(f, append);
    // out.write((log+"\n").getBytes("UTF-8"));
    // out.close();
    // } catch (FileNotFoundException e) {
    // e.printStackTrace();
    // } catch (UnsupportedEncodingException e) {
    // e.printStackTrace();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }

    /**
     * 将日志按行写入文件
     * 
     * @param url 日志文件名url
     * @param content 写入内容
     * @param isAppend 是追加还是替换
     */
    public static void storeLine(String url, String content, boolean isAppend) {
        if (!checkDir())
            return;

        String md5 = md5(url);
        File f = new File(cacheDir + md5);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(f, isAppend));
            writer.write(content + "\n");
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据URL从缓存文件按行读取数据，并返回字符串数组
     * 
     * @param url 指定文件名称的URL
     * @return 文件内容字符串数组，每行为一个字符串
     */
    public static List<String> loadLine(String url) {
        List<String> tmpList = new ArrayList<String>();

        String md5 = md5(url);
        File f = new File(cacheDir + md5);
        if (!f.exists())
            return tmpList;

        FileReader fr = null;
        BufferedReader bf = null;
        try {
            fr = new FileReader(f);
            bf = new BufferedReader(fr);
            String line = bf.readLine();
            while (line != null) {
                line = line.trim();
                if (!line.equals("")) {
                    tmpList.add(line);
                }
                line = bf.readLine();
            }
            bf.close();
            fr.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return tmpList;

    }
}
