package com.jasonhong.fx.main.demo.subscription.main;
  
import javafx.event.ActionEvent;  
import javafx.fxml.FXML;  
  
public class MainController {  
  
    @FXML  
    private void handleImageToText(ActionEvent event) {  
        // 显示图片转文字界面或执行操作  
        System.out.println("图片转文字功能被触发");  
        // ...  
    }  
  
    @FXML  
    private void handleTextToSpeech(ActionEvent event) {  
        // 显示文字转语音界面或执行操作  
        System.out.println("文字转语音功能被触发");  
        // ...  
    }  
  
    // 其他功能的事件处理程序  
  
    // ...  
}