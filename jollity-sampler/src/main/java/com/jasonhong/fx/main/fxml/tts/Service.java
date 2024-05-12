package com.jasonhong.fx.main.fxml.tts;

import com.jasonhong.core.common.Callback;
import com.jasonhong.core.common.Result;
import com.jasonhong.core.common.Status;
import com.jasonhong.fx.main.util.FXUtil;
import com.jasonhong.fx.main.util.MediaPlayerUtil;
import com.jasonhong.media.audio.util.SupportedFileFormat;
import com.jasonhong.services.mq.tts.client.TextToSpeakMqttPublisher;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

public class Service {
    public static String basePath =
            "E:\\studio\\project\\audioPlayer\\data\\books\\eae3219072633303eae86e3\\当你又忙又累，必须人间清醒\\page";

    public static void createDocumentsByTask(List<File> files, Callback<Result> callback) throws ExecutionException, InterruptedException {
        File outputDir = new File(basePath); // basePath 需要提前定义或作为参数传入
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        List<Future<Void>> futures = new ArrayList<>();
        int i = 0 ;
        for (File file : files) {
            String user = String.valueOf(UUID.randomUUID());
             createDocumentsByTask(i,user,file,outputDir, callback);
        }

    }
    private static final ExecutorService executorService = Executors.newFixedThreadPool(1);

    public static void createDocumentsByTask (int index,String user,File file, File outputDir, Callback<Result> callback) throws ExecutionException, InterruptedException {

//        AtomicLong id = new AtomicLong( System.currentTimeMillis());
        //设置字体库路径
//        String filePath  = new File("tessdata\\").getAbsolutePath();


        // 使用 CompletableFuture 执行异步任务，并在完成后调用回调方法

        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
                    Result result = new Result(index);
                    result.setMsg("创建任务：" + file.getAbsolutePath());
                    result.setCode(Status.RUNNING);
                    result.setData(file);

                    try {
                        if(file.length()>1024*256){
                            FXUtil.showErrorAlert("系统暂不支持超过256KB的文件，请处理后再继续！");
                            MediaPlayerUtil.playRandom(MediaPlayerUtil.AUDIO_TEXT_FILE_ERRORS);
                        }

                        System.out.println("Processing: " + user + " " + file.getName() );
                        callback.onAction(result);
                        TextToSpeakMqttPublisher publisher = new TextToSpeakMqttPublisher();
                       String  fileName = file.getName().replaceFirst("\\.[^.]+$",  SupportedFileFormat.MP3.getFilesuffixWithDot());
                       String outPutPath = String.valueOf(Path.of(file.getParent(),fileName));
                        publisher.init(index,user,file.getAbsolutePath(),outPutPath,callback);
                        System.out.println("Processed: " + user + " " + file.getName() + ", Output: " + outPutPath);

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
//                    result.setMsg("处理完成");
//                    result.setCode(1);
//                    result.setData(file);
//                    return   result;
                     return null;
                }, executorService)
                .thenAccept(result -> {
                    // 调用回调接口来处理结果
//                    if(result!=null) {
//                        if (result.getCode()==1){
//                            callback.onResult(result);
//                        }
//                    }
                })
                .exceptionally(e -> {
                    e.printStackTrace();
                    Result result = new Result(index);
                    result.setMsg("处理异常" + e.getMessage());
                    result.setCode(Status.ERROR);
                    result.setData(file);
                    // 如果有异常发生，调用回调接口的 onError 方法
                    callback.onError(result,(Exception) e);
                    return null; // exceptionally 需要返回一个与 CompletableFuture 相同的类型，但在这里我们只是返回 null
                });
//        if(future!=null){
//            future.get();
//        }
        // 注意：在 main 方法中，我们不需要调用 future.get()，因为我们是通过回调来处理结果的

        // 在应用程序结束时关闭线程池
        // executorService.shutdown(); // 优雅关闭
        // executorService.awaitTermination(60, TimeUnit.SECONDS); // 等待线程池中的任务完成
    }

    public static void shutdown(){
        if(executorService!=null) {
            executorService.shutdown();
        }
    }
}