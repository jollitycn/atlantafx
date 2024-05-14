package com.jasonhong.fx.main.component;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FileMenu extends Menu {


    private EventTarget eventSource;

    public File getSelectedFile() {
        return selectedFile;
    }

    public void setSelectedFile(File selectedFile) {
        this.selectedFile = selectedFile;
    }

    private File selectedFile;

    public FileMenu(Stage primaryStage, String dirPath) {
        // 创建一个文件菜单
        this.setText("File");

        // 创建一个打开文件的菜单项
        MenuItem openFileItem = new MenuItem("Open File");
        openFileItem.setOnAction(event -> {
            // 创建一个文件选择器
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open File");
            fileChooser.setInitialDirectory(new File(dirPath));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files", "*.mp3")
//                new FileExtensionFilter("All Files", "*.*")
            );
            // 显示文件选择器并获取用户选择的文件
            File selectedFile = fileChooser.showOpenDialog(primaryStage);

            if (selectedFile != null) {

                // 创建并触发自定义事件
                FileSelectedEvent e = new FileSelectedEvent(this, FileSelectedEvent.FILE_SELECTED, selectedFile);
                Event.fireEvent(this, e);
                // 处理文件选择逻辑，例如显示文件内容或执行其他操作
                System.out.println("File selected: " + selectedFile.getPath());
            }
        });

        // 将打开文件的菜单项添加到文件菜单中
        getItems().add(openFileItem);
    }
}
