package com.jasonhong.fx.main.util;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
public class RecentFilesManager {

    private static final String PREF_KEY_PREFIX = "recentFile_";
    private static final int MAX_RECENT_FILES = 10; // 最大历史记录数量  
    private Preferences prefs;
    private static final String CONFIG_FILE = "recent_files.config"; // 配置文件名称

    public RecentFilesManager(String appName) {
        prefs = Preferences.userNodeForPackage(RecentFilesManager.class);
    }

    public void addRecentFile(File file)   {
        if(file==null){
            return;
        }
        // 移除已存在的条目（如果有）  
        removeRecentFile(file);

        // 将新文件添加到列表的开头  
        List<String> files = getRecentFilesList();
        files.add(0, file.getAbsolutePath());

        // 如果列表太长，则移除最后一个条目  
        if (files.size() > MAX_RECENT_FILES) {
            files.remove(MAX_RECENT_FILES - 1);
        }

        // 更新首选项  
        int i = 0;
        for (String filePath : files) {
            prefs.put(PREF_KEY_PREFIX + i, filePath);
            i++;
        }

        // 清除剩余的旧条目（如果有）  
        String[] keys = null;
        try {
            keys = prefs.keys();
        } catch (BackingStoreException e) {
            throw new RuntimeException(e);
        }
        if(keys!=null) {
            for (String key : keys) {
//            String key = prefs.keys().nextElement();  
                if (!key.startsWith(PREF_KEY_PREFIX) || Integer.parseInt(key.substring(PREF_KEY_PREFIX.length())) >= files.size()) {
                    prefs.remove(key);
                }
            }
        }
        // 写入配置文件
        saveRecentFiles();
    }

    // 保存最近文件列表到配置文件
    private void saveRecentFiles() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CONFIG_FILE))) {
            String[] keys = prefs.keys();
            for (int i = 0; prefs.get(PREF_KEY_PREFIX + i, null) != null; i++) {
                writer.write(prefs.get(PREF_KEY_PREFIX + i, ""));writer.newLine();
            }
        } catch (IOException e) {
            // 处理文件保存错误
            e.printStackTrace();
        } catch (BackingStoreException e) {
            throw new RuntimeException(e);
        }
    }

    public List<File> getRecentFiles() {
        List<String> files = getRecentFilesList();
        List<File> fileList = new ArrayList<>();
        for (String filePath : files) {
            fileList.add(new File(filePath));
        }
        return fileList;
    }

    private List<String> getRecentFilesList() {
        List<String> files = new ArrayList<>();
        for (int i = 0; prefs.get(PREF_KEY_PREFIX + i, null) != null; i++) {
            files.add(prefs.get(PREF_KEY_PREFIX + i, ""));
        }
        return files;
    }

    private void removeRecentFile(File file)   {
        if(file!=null) {
            List<String> files = getRecentFilesList();
            files.remove(file.getAbsolutePath());
            // 更新首选项以反映更改
            int index = 0;
            for (String filePath : files) {
                prefs.put(PREF_KEY_PREFIX + index, filePath);
                index++;
            }


            // 清除剩余的、旧的、不再使用的首选项键
            String[] keys = null;
            try {
                keys = prefs.keys();
            } catch (BackingStoreException e) {
                throw new RuntimeException(e);
            }

            if (keys != null) {
                for (String key : keys) {
                    if (!key.startsWith(PREF_KEY_PREFIX) || !isValidKeyIndex(key, files.size())) {
                        prefs.remove(key);
                    }
                }
            }
        }
    }

    // 辅助方法，用于检查键的索引是否有效
    private boolean isValidKeyIndex(String key, int maxIndex) {
        if (!key.startsWith(PREF_KEY_PREFIX)) {
            return false;
        }
        try {
            int index = Integer.parseInt(key.substring(PREF_KEY_PREFIX.length()));
            return index < maxIndex;
        } catch (NumberFormatException e) {
            // 如果无法解析为整数，则认为它不是一个有效的键
            return false;
        }
    }

    // 加载最近文件列表
    private void loadRecentFiles() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {

                addRecentFile(new File(line));
                // 这里需要解析每一行来创建 RecentFile 对象
                // 假设每行包含文件路径和最后访问时间，用逗号分隔
//                String[] parts = line.split(",");
//                if (parts.length == 2) {
//                    RecentFile file = new RecentFile();
//                    file.setFilePath(parts[0]);
//                    file.setLastAccessTime(Long.parseLong(parts[1]));
//                    recentFiles.add(file);
//
//                    // 如果超过最大数量，移除最旧的文件
//                    if (recentFiles.size() > MAX_FILES) {
//                        recentFiles.remove(0);
//                    }
//                }
            }
        } catch (FileNotFoundException e) {
//            FileUtils
             }catch (IOException e) {
            // 处理文件加载错误，可能是文件不存在或其他IO问题
            e.printStackTrace();
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws BackingStoreException {

        RecentFilesManager manager = new RecentFilesManager("MyApp");
        manager.loadRecentFiles(); // 加载最近文件列表
        manager.addRecentFile(new File("/path/to/file1.txt"));
        manager.addRecentFile(new File("/path/to/file2.txt"));
        List<File> recentFiles = manager.getRecentFiles();
        System.out.println(Arrays.toString(recentFiles.toArray(new File[0])));
    }
}