package com.jasonhong.fx.main.fxml.tts;


import com.jasonhong.fx.main.page.Page;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URI;

public class App extends Application implements Page {
public App(){

}
    public Parent getRoot()  {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return root;
    }

    @Override
    public void start(Stage primaryStage)   {
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

    public static final String NAME =  "文本转语音";
    @Override
    public String getName() {
        return NAME;
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