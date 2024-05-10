package com.jasonhong.fx.main.fake.domain.ocr;

import com.jasonhong.core.common.Result;
import com.jasonhong.core.common.Status;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;

import java.io.File;
import java.io.Serializable;

public class ImageToTextInfo implements Serializable {
    private String id;
    private Image image;
    private File resultTextFile;
    private File sourceFile;


//    private Status status= Status.NONE;

    private Result result;
    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public File getResultTextFile() {
        return resultTextFile;
    }

    public void setResultTextFile(File resultTextFile) {
        this.resultTextFile = resultTextFile;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
//
//    public Status getStatus() {
//        return status;
//    }
//
//
//    public void setStatus(Status status) {
//        this.status = status;
//    }

    public File getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(File sourceFile) {
        this.sourceFile = sourceFile;
    }
public String getFileName(){
        String fileName = "";
        if(sourceFile!=null){
            return sourceFile.getName();
        }
    return fileName;
}

public Long getFileLength(){
        long lenth = 0;
        if(sourceFile!=null) {
            lenth = sourceFile.length();
        }
    return Long.valueOf(lenth);
}

    public double getProgress() {
        double progress = 0;
        if(result!=null) {
            switch (result.getCode()) {
                case Success:
                    progress = 1;
                    break;
                case NONE:
                    progress = 0;
                    break;
                case ERROR:
                case FAILED:
                    // 假设我们不想表示完成，所以用一个接近但不是100%的值
                    progress = 0.9;  // 或者您可以设置一个特定的错误样式
                    break;
                case RUNNING,START:
                    // 这里您可能需要一个外部机制来更新进度，例如使用Platform.runLater()
                    // progressBar.setProgress(...); // 根据实际进度更新
                    progress = 0.5;// 初始或未知进度
                    break;
                default:
                    progress = 0; // 默认为0或其他初始值
                    break;
            }
        }

        return   progress;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getResultFileName()
    {
        if(resultTextFile!=null){
            return resultTextFile.getName();
        }
        return "";
    }

}
