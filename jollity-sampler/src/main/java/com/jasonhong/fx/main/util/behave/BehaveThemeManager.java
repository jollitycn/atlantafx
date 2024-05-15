package com.jasonhong.fx.main.util.behave;

import com.jasonhong.core.io.SeriliazeableUtil;
import com.jasonhong.fx.main.theme.AccentColor;
import com.jasonhong.fx.main.theme.ThemeManager;
import com.jasonhong.fx.main.util.BeanUtil;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import static com.jasonhong.core.io.SeriliazeableUtil.fromString;

public class BehaveThemeManager {

    public static class BehaveTheme implements Serializable {
        @Override
        public String toString() {
            return BeanUtil.toString(this);
        }
private int zoom;
        private static final long serialVersionUID = 1L;
        private String themeName;
        private String fontId;
        private int fontSize;
        private AccentColor.Type accentColorType;

        public String getThemeName() {
            return themeName;
        }

        public void setThemeName(String themeName) {
            this.themeName = themeName;
        }

        public String getFontId() {
            return fontId;
        }

        public void setFontId(String fontId) {
            this.fontId = fontId;
        }

        public int getFontSize() {
            return fontSize;
        }

        public void setFontSize(int fontSize) {
            this.fontSize = fontSize;
        }

        public AccentColor.Type getAccentColorType() {
            return accentColorType;
        }

        public void setAccentColorType(AccentColor.Type accentColorType) {
            this.accentColorType = accentColorType;
        }

        public int getZoom() {
            return zoom;
        }

        public void setZoom(int zoom) {
            this.zoom = zoom;
        }
    }

    private String PREF_KEY_PREFIX = "behave_theme_";
    private static Preferences prefs = Preferences.userNodeForPackage(BehaveThemeManager.class);
    private String CONFIG_FILE = "data/config/behave_theme.config"; // 配置文件名称

    public BehaveThemeManager() {
        try {
            FileUtils.createParentDirectories(new File(CONFIG_FILE));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        loadRecentFiles();
    }


    public void save(BehaveTheme file) {
        save(file, true);
    }

    public void save(BehaveTheme file, boolean saveToFile) {
        if (file == null) {
            return;
        }
        // 移除已存在的条目（如果有）  
        try {
            removeRecentFile(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 将新文件添加到列表的开头
//        List<BehaveTheme> files = getRecentFilesList();
//        files.add(0, file);
//
//        // 如果列表太长，则移除最后一个条目
//        if (files.size() > MAX_RECENT_FILES) {
//            files.remove(MAX_RECENT_FILES - 1);
//        }

        // 更新首选项
//        int i = 0;
//        for (BehaveTheme filePath : files) {
            try {
                prefs.put(PREF_KEY_PREFIX + 0, SeriliazeableUtil.toString(file));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
//
//            i++;
//        }

        if (saveToFile) {
            // 写入配置文件
            try {
                FileUtils.delete(new File(CONFIG_FILE));
            } catch (IOException e) {
//            throw new RuntimeException(e);
            }
            saveRecentFiles();
        }
    }

    // 保存最近文件列表到配置文件
    private void saveRecentFiles() {
        //保存之前先清除配置文件

        try {
            Object object = prefs.get(PREF_KEY_PREFIX + 0, null);
            if (object != null) {
                System.out.println("write config:" + PREF_KEY_PREFIX + " with " + SeriliazeableUtil.fromString(object.toString()));
                FileUtils.writeStringToFile(new File(CONFIG_FILE), object.toString(), StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            // 处理文件保存错误
            e.printStackTrace();
        }
    }

    public BehaveTheme getRecentFile() {
        BehaveTheme bt = createNew() ;
        try {
           String string = prefs.get(PREF_KEY_PREFIX + 0, null);
           if(string!=null) {
               bt = (BehaveTheme) fromString(string);
           }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return bt;
    }

    private BehaveTheme createNew() {
        BehaveTheme bt = new BehaveTheme();
        bt.setZoom(ThemeManager.DEFAULT_ZOOM);
        bt.setFontSize(ThemeManager.DEFAULT_FONT_SIZE);
        bt.setFontId(ThemeManager.DEFAULT_FONT_FAMILY_NAME);
        bt.setThemeName(ThemeManager.getInstance().getDefaultTheme().getName());
        return bt;
    }

//    public List<BehaveTheme> getRecentFiles() {
//        List<BehaveTheme> fileList = new ArrayList<>();
//        for (int i = 0; prefs.get(PREF_KEY_PREFIX + i, null) != null; i++) {
//            try {
//                fileList.add((BehaveTheme) fromString(prefs.get(PREF_KEY_PREFIX + i, null)));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            } catch (ClassNotFoundException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        return fileList;
//    }

    //
    private List<BehaveTheme> getRecentFilesList() {
        List<BehaveTheme> files = new ArrayList<>();
        for (int i = 0; prefs.get(PREF_KEY_PREFIX + i, null) != null; i++) {
            try {
                files.add((BehaveTheme) fromString(prefs.get(PREF_KEY_PREFIX + i, null)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return files;
    }

    private void removeRecentFile(BehaveTheme file) throws IOException {
        if (file != null) {
            List<BehaveTheme> files = getRecentFilesList();
            files.remove(file);
            removePrefs();
            // 更新首选项以反映更改
            int index = 0;

            for (BehaveTheme filePath : files) {
                prefs.put(PREF_KEY_PREFIX + index, SeriliazeableUtil.toString(filePath));
                index++;
            }


        }
    }

    private void removePrefs() {
        // 清除剩余的、旧的、不再使用的首选项键
        String[] keys = null;
        try {
            keys = prefs.keys();
        } catch (BackingStoreException e) {
            throw new RuntimeException(e);
        }

        if (keys != null) {
            for (String key : keys) {
                if (key.toLowerCase().startsWith(PREF_KEY_PREFIX)) {
                    prefs.remove(key);
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
        try {

            String value = FileUtils.readFileToString(new File(CONFIG_FILE), StandardCharsets.UTF_8);
            if (value.isEmpty()) {
                removePrefs();
                return;
            }
            BehaveTheme line;
            save((BehaveTheme) fromString(value), false);
            // 这里需要解析每一行来创建 RecentFile 对象
            // 假设每行包含文件路径和最后访问时间，用逗号分隔

        }catch (Exception ex){
            ex.printStackTrace();
            removePrefs();
        }
    }

    public static void main(String[] args) throws BackingStoreException {

        BehaveThemeManager manager = new BehaveThemeManager();
////        manager.loadRecentFiles(); // 加载最近文件列表
        manager.save(new BehaveTheme());
//        manager.addRecentFile(new File("/path/to/file2.txt"));
        BehaveTheme recentFiles = manager.getRecentFile();
        System.out.println(recentFiles);
    }
}