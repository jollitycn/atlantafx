package com.jasonhong.fx.main.component;

import com.jasonhong.ocr.txt.ChineseToPinyin;
import javafx.application.Application;
import javafx.geometry.Insets;  
import javafx.geometry.Pos;  
import javafx.scene.Scene;  
import javafx.scene.control.*;  
import javafx.scene.input.MouseEvent;  
import javafx.scene.layout.*;  
import javafx.stage.Stage;
import net.sourceforge.pinyin4j.PinyinHelper;

public class ChineseToPinyinApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10));

        TextField chineseTextField = new TextField();
        chineseTextField.setPromptText("请输入汉字");
        chineseTextField.setText("你好");
        Button convertButton = new Button("转换成拼音");
        convertButton.setOnMouseClicked(this::convertToPinyin);

        TextField pinyinTextField = new TextField();
        pinyinTextField.setEditable(false);
        TextField pinyinTextField2 = new TextField();
        pinyinTextField2.setEditable(false);
        root.getChildren().addAll(chineseTextField, convertButton, pinyinTextField, pinyinTextField2);

        Scene scene = new Scene(root, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("汉字转拼音转换器");
        primaryStage.show();

        // 将TextField和Label设置为成员变量，以便在事件处理器中访问  
        this.chineseTextField = chineseTextField;
        this.pinyinTextField = pinyinTextField;
        this.pinyinTextField2 = pinyinTextField2;
    }

    private TextField chineseTextField;
    private TextField pinyinTextField;
    private TextField pinyinTextField2;

    private void convertToPinyin(MouseEvent event) {
        String chineseText = chineseTextField.getText();
        StringBuilder pinyin = new StringBuilder();
        for (char c : chineseText.toCharArray()) {
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c);
            if (pinyinArray != null && pinyinArray.length > 0) {
                pinyin.append(pinyinArray[0]); // 取第一个读音，或者可以按需选择其他读音  
            } else {
                pinyin.append(c);
            }

        }
        pinyinTextField.setText(pinyin.toString());
        convertToPinyinSprate(event);
    }

    private void convertToPinyinSprate(MouseEvent event) {
        String chineseText = chineseTextField.getText();
        pinyinTextField2.setText(ChineseToPinyin.getPinyin(chineseText));

    }

    public static void main(String[] args) {
        launch(args);
    }
}