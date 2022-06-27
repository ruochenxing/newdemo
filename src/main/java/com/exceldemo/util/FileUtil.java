package com.exceldemo.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static List<String> listDir(String filePath) {
        List<String> fileList = new ArrayList<>();
        File dir = new File(filePath);
        File[] file_List = dir.listFiles();
        for (File f : file_List) {
            if (f.getName().startsWith("details2022")) {
                fileList.add(f.getName());
            }
        }
        return fileList;
    }

    public static List<String> readFileLines(String fileName) throws IOException {
        File fin = new File(fileName);
        List<String> contents = new ArrayList<>();
        FileInputStream fis = new FileInputStream(fin);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String line = null;
        while ((line = br.readLine()) != null) {
            contents.add(line);
        }
        br.close();
        return contents;
    }

    public static void saveFile(String context, String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fileWriter);
            bw.write(context);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void appendFile(String content, String file) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
            out.write(content + "\r\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取文件内文本
     * 
     * @param filepath
     * @return
     */
    public static String getFileText(String filepath, boolean replaceSpace) {
        StringBuffer sb = new StringBuffer();
        File file = new File(filepath);
        try (Reader reader = new InputStreamReader(new FileInputStream(file));) {
            int ch = 0;
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (replaceSpace) {
            return sb.toString().replaceAll("\r\n", "");
        } else {
            return sb.toString();
        }
    }
}
