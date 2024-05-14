package com.jasonhong.fx.main.media;

import java.io.BufferedReader;
import java.io.FileReader;  
import java.io.IOException;  
import java.nio.charset.StandardCharsets;  
import java.security.MessageDigest;  
import java.security.NoSuchAlgorithmException;  
  
public class TextSummaryGenerator {  
    public static void main(String[] args) {  
        String filePath = "path/to/your/textfile.txt"; // 替换为你的文本文件路径  
        String textContent = readFile(filePath);  
        String summary = generateSummary(textContent);  
        System.out.println("Summary: " + summary);  
    }  
  
    public static String readFile(String filePath) {  
        StringBuilder contentBuilder = new StringBuilder();  
  
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {  
            String line;  
            while ((line = br.readLine()) != null) {  
                contentBuilder.append(line).append("\n");  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
  
        return contentBuilder.toString();  
    }  
  
    public static String generateSummary(String text) {  
        try {  
            MessageDigest md = MessageDigest.getInstance("MD5");  
            byte[] messageDigest = md.digest(text.getBytes(StandardCharsets.UTF_8));  
            StringBuilder hexString = new StringBuilder();  
            for (byte aMessageDigest : messageDigest) {  
                String hex = Integer.toHexString(0xff & aMessageDigest);  
                if (hex.length() == 1) hexString.append('0');  
                hexString.append(hex);  
            }  
            return hexString.toString();  
        } catch (NoSuchAlgorithmException e) {  
            throw new RuntimeException(e);  
        }  
    }  
}