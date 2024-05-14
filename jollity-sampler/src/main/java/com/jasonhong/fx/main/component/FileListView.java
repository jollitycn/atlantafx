package com.jasonhong.fx.main.component;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class FileListView extends ListView {


    // 指定要显示文件列表的文件夹路径
    String folderPath = "/path/to/your/folder"; // 替换为实际文件夹路径

    public FileListView(String folderPath) throws IOException {
        this.folderPath = folderPath;
        init();
    }

    private void init() throws IOException {
        // 获取文件夹路径的Path对象
        Path folderPathObj = Paths.get(folderPath);

        // 检查文件夹是否存在
        if (!Files.exists(folderPathObj) || !Files.isDirectory(folderPathObj)) {
            System.out.println("Folder does not exist or is not a directory.");
            return;
        }

        // 获取文件夹中所有文件的名称列表
        ObservableList<String> fileNames = FXCollections.observableArrayList(
                Files.list(folderPathObj)
                        .filter(Files::isRegularFile)
                        .map(Path::getFileName)
                        .map(Object::toString)
                        .collect(Collectors.toList())
        );

    }
}
