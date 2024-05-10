package com.jasonhong.fx.main.fxml.tts;

import com.jasonhong.core.common.Callback;
import com.jasonhong.core.common.Result;
import com.jasonhong.fx.main.layout.BottomStateBar;
import com.jasonhong.fx.util.FXUtil;
import com.jasonhong.fx.util.MediaPlayerUtil;
import com.jasonhong.fx.util.RecentFilesManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static com.jasonhong.fx.main.layout.MainLayer.BOTTOM_STATE_BAR;


public class  Controller {
    public Label labelCount; // 确保有一个无参数的构造方法
    public Menu recentMenu;
    public VBox consoleBox;
    public VBox centerVBox;
    public Region bottomPadding;
    public BorderPane rootPane;
    public TextArea resultTextArea;

    public Controller() {
        // 可以在这里进行初始化操作，但通常保持为空
    }
    @FXML
    public void initialize() {
        // 假设rootPane的高度是固定的，或者你可以在这里动态获取它
//        double rootPaneHeight = rootPane.getPrefHeight(); // 你可能需要在实际应用中替换为rootPane的实际高度
//        double centerVBoxHeight = rootPaneHeight * 3.0 / 5.0; // 计算centerVBox应该占据的高度

        // 设置centerVBox的高度（如果需要的话，但通常VBox会根据其内容自动调整高度）
        // centerVBox.setPrefHeight(centerVBoxHeight); // 这行可能不需要，取决于你的具体需求

        // 设置bottomPadding的高度以确保bottomVBox占据剩余空间（2/5）
//        double bottomPaddingHeight = rootPaneHeight - centerVBoxHeight;
//        bottomPadding.setPrefHeight(bottomPaddingHeight);
//        bottomPadding.setVisible(true); // 如果在FXML中设置为不可见的话，这里设置为可见

        // 可能还需要调整bottomVBox的VBox.vgrow属性以确保它占据剩余空间
//        VBox.setVgrow(consoleBox, Priority.ALWAYS);
    }

    @FXML
    public MenuItem openDirectoryMenuItem;
    @FXML
    public MenuBar openDirectoryMenuBar;

    @FXML
    public Button startConversionButton;
    @FXML
    private ListView<File> imageListView;

    private Window primaryStage;
    @FXML
//    private TextArea resultTextArea;
    private List<File> imageFiles;
private int totalSteps;
private int completedSteps;
    public Controller(Window primaryStage) {
        this.setPrimaryStage(primaryStage);
    }

    @FXML
    private void handleOpenDirectory(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File directory = directoryChooser.showDialog(getPrimaryStage());
        if (directory != null) {
            imageFiles = findImageFilesInDirectory(directory);
            if(imageFiles==null || imageFiles.size()==0){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("错误");
                alert.setHeaderText(null); // 可以设置标题上方的描述性文本，如果不需要则为null
                alert.setContentText("目录中没有找到相关文件！");
                alert.show();// 设置对话框的内容文本
                BOTTOM_STATE_BAR.completeProgress();
                startConversionButton.setDisable(true);
            }else{
                startConversionButton.setDisable(false);
            }
            imageListView.getItems().setAll(imageFiles);
            new RecentFilesManager(null).addRecentFile(directory);

        }
    }

    private List<File> findImageFilesInDirectory(File directory) {
        List<File> imageFiles = new ArrayList<>();
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isFile() && (file.getName().toLowerCase().endsWith(".txt"))) {
                imageFiles.add(file);
                labelCount.setText("检测到：" + imageFiles.size() + "份文本");
            }
        }
        return imageFiles;
    }

    @FXML
    private void handleExit(ActionEvent event) {
        // 实现退出程序的逻辑

    }

    public Window getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Window primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    public void handleStartConversion(ActionEvent actionEvent) {
        startConversionButton.setDisable(true);
        BOTTOM_STATE_BAR.initProgress();
// 假设你有一个OCR服务的URL
//        String ocrServiceUrl = "http://your-ocr-service-url/ocr";

        // 这里只是一个示例，你应该使用更复杂的逻辑来处理文件和上传
        // 例如，你可以使用文件选择器让用户选择一个图像文件，并将其发送到OCR服务

        // 模拟OCR调用（实际情况下，你会发送图像文件到OCR服务）
        if (imageFiles == null || imageFiles.isEmpty()) {
            FXUtil.showErrorAlert("请在文件菜单中添加要处理的目录文件");
            return;
        }

//        new Thread(() -> {
        try {
            // 创建一个 Callback 实例
            Callback<Result> callback = new Callback<Result>() {
                @Override
                public void onAction(Result result) {
                    System.out.println("正在处理: " + result);
                    Platform.runLater(()-> {
                        updateConsole(result);
                    });
                }

                @Override
                public void onResult(Result result) {
                    System.out.println("任务完成，结果: " + result);
                    // 在这里可以执行任何需要的操作，例如更新UI等
Platform.runLater(()->{
   updateConsole(result);
//    resultTextArea.appendText((+ result.getMsg()+"\n");
    updateProgress(result);
});
                }

                @Override
                public void onError(Result result,Exception e) {
                    e.printStackTrace();
                    Platform.runLater(()->{
                        updateConsole(result);
                        updateProgress(result);
                    });
                }

                private void updateProgress(Result result) {
                    completedSteps++;
                    BOTTOM_STATE_BAR.updateProgress(result.getMsg(),completedSteps,totalSteps);
                    if (completedSteps == totalSteps) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("消息");
                        alert.setHeaderText(null); // 可以设置标题上方的描述性文本，如果不需要则为null
                        alert.setContentText("处理结束！"); // 设置对话框的内容文本
                        BOTTOM_STATE_BAR.completeProgress();
                        startConversionButton.setDisable(false);
                        MediaPlayerUtil.play(MediaPlayerUtil.AUDIO_HANDLED);
                        alert.show();
                    }
                }
            };
            completedSteps  = 0;
            totalSteps = imageFiles.size();
            MediaPlayerUtil.play(MediaPlayerUtil.AUDIO_HANDLING);

//            TesseractOCR.createDocumentsByTask(imageFiles,callback);
            Service.createDocumentsByTask(imageFiles,callback);

            // 更新GUI（必须在JavaFX线程中执行）
//                Platform.runLater(() -> resultTextArea.setText(response.toString()));

//            } catch (Exception e) {
//                e.printStackTrace();
//                // 在GUI中显示错误（如果需要）
//            }
//        }).start();

        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateConsole(Result result) {

//        resultTextArea.appendText(result.getId()+":" + result.getMsg() +"/n");
        if (result.getData() != null) {
            if (result.getData() instanceof File) {
                resultTextArea.appendText(((File) result.getData()).getName() + result.getMsg() + "\n");
            }else{
                resultTextArea.appendText( result.getData() + result.getMsg() + "\n");
            }  }
//        HBox hb = new HBox();
//        hb.setId(String.valueOf(result.getId()));
//        Label labelInfoId = new Label();
//        Label labelState = new Label();
//        Label labelMsg = new Label();
//        if (result.getCode() == 1) {
//            labelMsg.getStyleClass().add("green-label");
//        } else if (result.getCode() == 0) {
//            labelMsg.getStyleClass().add("red-label");
//        } else {
//            labelMsg.getStyleClass().add("normal-label");
//        }
//        labelMsg.setText(result.getMsg());
//        if (result.getData() != null) {
//            if (result.getData() instanceof File) {
//                labelInfoId.setText(((File) result.getData()).getName());
//            } else {
//                labelInfoId.setText(result.getData().toString());
//            }
//
//        }
////        hb.getChildren().addAll(labelInfoId, labelState, labelMsg);
////        if(consoleBox.getChildren()!=null && consoleBox.getChildren().size()>0) {
////            if (consoleBox.getChildren().get(Integer.parseInt(hb.getId())) != null) {
////                hb = (HBox) consoleBox.getChildren().get(Integer.parseInt(hb.getId()));
////                hb.getChildren().clear();
////            } else {
////                consoleBox.getChildren().add(hb);
////            }
////        }else{
////        }
//        hb.getChildren().addAll(labelInfoId, labelState, labelMsg);
//        consoleBox.getChildren().add(hb);

    }

    public void handleOpenRecently(ActionEvent actionEvent) {
        List<File> recentFiles = new RecentFilesManager("tts").getRecentFiles();
        recentMenu.getItems().clear();
        for (File fileName : recentFiles) { // 假设 recentFiles 是一个包含最近打开文件名的列表
            MenuItem menuItem = new MenuItem(fileName.getPath());
            menuItem.setOnAction(event -> {
                if (fileName.exists()) {
                    if (fileName.isFile()) {
                        imageListView.getItems().setAll(fileName);
                    } else {
                        imageFiles = findImageFilesInDirectory(fileName);
                        imageListView.getItems().setAll(imageFiles);
                    }
                }
            });
            recentMenu.getItems().add(menuItem);
        }
//        openRecently.
    }

    // 辅助方法，例如加载图片到ListView，调用OCR服务等
    // ...
}