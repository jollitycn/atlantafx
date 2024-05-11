package com.jasonhong.fx.main.fxml.ocr;


import com.jasonhong.fx.main.page.Page;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URI;

public class ImageToText extends Application implements Page {

    public static final String NAME =  "图像识别";

    public Parent getRoot() {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("image-to-text.fxml"));

            root = loader.load();
            Parent finalRoot = root;
            Platform.runLater(() -> {
                        finalRoot.requestLayout();
                    });
//            Platform.runLater(() -> {
//                // 更新UI的代码
//                root.getChildren().clear();
////                root.getChildren().addAll(createNewContent());
//            });
            ImageToTextController controller =  loader.getController();
            controller.initTable();
            root.layout();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return root;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        // 加载FXML文件
        Parent root =   getRoot();
        Stage stage;
        if(primaryStage==null){
            stage = new Stage();
            stage.initOwner(primaryStage);
        }else{
            stage  = primaryStage;
        }

//
//        // 设置控制器（如果FXML文件中没有指定）
//        MainController controller = loader.getController();
//        primaryStage.setTitle("文字语音处理工具");
//        Label label = new Label("Welcome to JavaFX!");
////        StackPane root = new StackPane();
////        root.getChildren().add(label);
        // 创建一个新的Stage作为子窗口
//        subStage.getIcons().add(new Image("file:path/to/your/icon.png")); // 可选：设置图标
//        subStage.setScene(scene);
        stage.setScene(new Scene(root, 300, 250));
        stage.show();
    }

    @Override
    public String getName() {
        return this.NAME;
    }

    @Override
    public Parent getView() {
        return getRoot() ;
    }

    @Override
    public boolean canDisplaySourceCode() {
        return false;
    }

    @Override
    public @Nullable URI getJavadocUri() {
        return null;
    }

    @Override
    public @Nullable Node getSnapshotTarget() {
        return null;
    }

    @Override
    public void reset() {

    }

//    public static void main(String[] args) {
//        launch(args);
//    }
}