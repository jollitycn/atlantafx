package com.jasonhong.fx.main.fxml.download;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.jasonhong.fx.main.fxml.ocr.ImageToText.SUPPORTED_MEDIA_TYPES;

public class FileDownloadTask extends Pane {
    private String url;
    //    private String fileName;
    //    private Label statusLabel;
    public final ReadOnlyObjectWrapper<String> statusLabel = new ReadOnlyObjectWrapper<>();
    public final ReadOnlyObjectWrapper<String> fileName = new ReadOnlyObjectWrapper<>();
    public final ReadOnlyObjectWrapper<String> fileDisplayName = new ReadOnlyObjectWrapper<>();
    public final ReadOnlyObjectWrapper<ProgressBar> progressBar = new ReadOnlyObjectWrapper<>();

    Task<Void> downloadTask;
    private long startTime ;
    private long endTime;
    AtomicReference<File> selectedFile = new AtomicReference<>();
    int fileLength;
    public FileDownloadTask(String url) {
        this.url = url;
        progressBar.set(new ProgressBar(0));
//        statusLabel = new Label();
        statusLabel.set("等待下载...");
        fileName.set(this.url.substring(this.url.lastIndexOf("/") + 1));

//        Button startDownloadButton = new Button("开始下载");
//        startDownloadButton.setOnAction(e -> downloadFile( ));
//        getChildren().addAll(progressBar, statusLabel, startDownloadButton);
    }


    public void downloadFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(fileName.get());
        // 创建一个包含通配符的列表来模拟“全部文件”过滤器
        List<String> allFilesExtensions = Collections.singletonList("*"); // 使用通配符 "*"
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("全部文件 (*)", allFilesExtensions));

// 或者，如果你想保留其他过滤器并添加“全部文件”过滤器
// fileChooser.getExtensionFilters().addAll(otherFilters...);
// fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("全部文件 (*)", Collections.singletonList("*")));

        fileChooser.setTitle("选择保存位置");
        selectedFile.set(fileChooser.showSaveDialog(null));
        if (selectedFile.get() == null) {
            // 用户取消了文件选择
//            Platform.runLater(() -> statusLabel.setText("文件选择取消"));
//            updateMessage("文件选择取消");
//            return null;

            statusLabel.set("文件选择取消");
            return;
        }
        fileDisplayName.set(fileName.get()+" -> "+selectedFile.get().getName());
        downloadTask=  new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                startTime = System.currentTimeMillis();
                // 连接到URL并获取输入流

                // 设置代理服务器的地址和端口
//                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.example.com", 8080));
//                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection(proxy);
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                var extensions = SUPPORTED_MEDIA_TYPES.stream().map(s -> "*." + s).toList();
                connection.setRequestMethod("GET");

//                snowflake 192.0.2.4:80 8838024498816A039FCBBAB14E6F40A0843051FA fingerprint=8838024498816A039FCBBAB14E6F40A0843051FA url=https://snowflake-broker.torproject.net.global.prod.fastly.net/ fronts=cdn.yelp.com,www.shazam.com,www.cosmopolitan.com,www.esquire.com ice=stun:stun.l.google.com:19302,stun:stun.antisip.com:3478,stun:stun.bluesip.net:3478,stun:stun.dus.net:3478,stun:stun.epygi.com:3478,stun:stun.sonetel.net:3478,stun:stun.uls.co.za:3478,stun:stun.voipgate.com:3478,stun:stun.voys.nl:3478 utls-imitate=hellorandomizedalpn
                fileLength = connection.getContentLength();
                try (InputStream inputStream = connection.getInputStream();
                     FileOutputStream outputStream = new FileOutputStream(selectedFile.get())) {

                    // 读取输入流并写入输出流
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    long totalBytesRead = 0;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        // 检查任务是否被取消
                        if (isCancelled()) {
                            // 如果被取消，则中断当前线程（可选，但可能不是最佳实践）
                            // Thread.currentThread().interrupt();
                            // 清理资源（如果需要）
                            // ...
                            // 然后返回
                            return null;
                        }
                        outputStream.write(buffer, 0, bytesRead);
                        totalBytesRead += bytesRead;
                        // 格式化百分比并更新消息
                        double percentage = (100.0 * totalBytesRead) / fileLength;
                        endTime =  System.currentTimeMillis();
                        DecimalFormat decimalFormat = new DecimalFormat("#.00"); // 保留两位小数，如果需要一位则改为"#.0"
                        String message = "正在下载："  +totalBytesRead/1024+ "/"+fileLength/1024+" - "+ decimalFormat.format(percentage) + "% - " + Duration.ofMillis(endTime-startTime).getSeconds() +"s - " +  fileLength/1024/Duration.ofMillis(endTime-startTime).getSeconds() +" kb/s";
                        updateMessage(message);
                        updateProgress(totalBytesRead, fileLength);
                    }

//                    updateMessage("完成:" + fileLength/1024 +"KB  - " +  Duration.ofMillis(endTime-startTime).getSeconds() +"s");

                }
                return null;
            }

            @Override
            protected void updateMessage(String msg) {
                super.updateMessage(msg);
                Platform.runLater(() -> statusLabel.set(msg));
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                Platform.runLater(() -> statusLabel.set("完成:" + fileLength/1024 +"KB - " +  Duration.ofMillis(endTime-startTime).getSeconds()));
            }

            @Override
            protected void cancelled() {
                super.cancelled();  Platform.runLater(() -> statusLabel.set("已取消"));
                downloadTask = null;
            }
            @Override
            protected void failed() {
                super.failed();
                Platform.runLater(() -> {
                    statusLabel.set("下载失败: " + getException().getMessage());
                    // ... 在某个时间点，你想要取消绑定并直接设置ProgressBar的progress值
                    progressBar.get().progressProperty().unbind(); // 取消绑定
                    progressBar.get().setProgress(0); // 直接设置值
                    getException().printStackTrace();
                });
            }
        };

        // 开始任务并绑定进度条
        progressBar.get().progressProperty().bind(downloadTask.progressProperty());
        new Thread(downloadTask).start();
    }


    public void open() {
        // 指定要打开的目录路径（这里以你的用户目录为例）
//        File directory = new File(System.getProperty("user.home"));

        // 检查Desktop类是否支持文件浏览器操作
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
            try {
                // 转换为URI
//                URI fileURI = directory.toURI();

                // 打开资源管理器并导航到该目录
                Desktop.getDesktop().open(selectedFile.get().getParentFile());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void cancel() {
        downloadTask.cancel(true);
    }

//    public void pause() {
//        downloadTask.();
//    }
}
