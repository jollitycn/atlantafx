package com.jasonhong.fx.main.demo.subscription.mqtt;

import com.jasonhong.services.mq.rabiitmq.AudioMessage;
import com.rabbitmq.client.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.DeliverCallback;
import javafx.event.Event;
import javafx.event.EventTarget;


// ... 其他导入和代码 ...

public class AudioConsumer  {
    private final static String QUEUE_NAME = "audio_queue";
    private final static String TEMP_AUDIO_FILE_DIR = "E:/studio/project/audioPlayer/src/assets/audios/received/";// 临时音频文件
    private static final String EXCHANGE_NAME = "Audio_Exchange";


    private EventTarget eventSource;

    private final static String TEMP_AUDIO_FILE_NAME = "temp_audio.mp3";// 临时音频文件
    // private final static String TEMP_AUDIO_FILE = TEMP_AUDIO_FILE_DIR + TEMP_AUDIO_FILE_NAME;


    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;
private  String  queueName;
    public AudioConsumer(EventTarget eventSource) throws IOException, TimeoutException   {
        this.eventSource =eventSource;
        factory = new ConnectionFactory();
        factory.setHost("localhost");
        connection = factory.newConnection();
        channel = connection.createChannel();
        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
           queueName=  channel.queueDeclare().getQueue();
        // 声明队列，如果队列不存在则创建它
//        try {
//            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        // 绑定队列到交换机
        channel.queueBind(queueName, EXCHANGE_NAME, ""); // fanout交换机不需要路由键

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    }

    public void startConsuming() throws IOException {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
          //  String message = new String(delivery.getBody(), "UTF-8");
            AudioMessage am = null;
            try {
                if(delivery.getBody()!=null) {
                     am = AudioMessage.deserialize(delivery.getBody());

                    System.out.println(" [x] Received audio data: " + am);
                }else{
                    System.err.println(" [x] Received empty audio data: "  );
                }
            // 播放接收到的音频数据
                saveAudio(am);
            //playAudio(delivery.getBody());
            //通知target
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }

    private void saveAudio(AudioMessage am) {
            // 将音频字节写入临时文件
        String TEMP_AUDIO_FILE = TEMP_AUDIO_FILE_DIR +am.getId()+ TEMP_AUDIO_FILE_NAME;
        System.out.println("save to " + TEMP_AUDIO_FILE);
            try (FileOutputStream fos = new FileOutputStream(TEMP_AUDIO_FILE)) {
                fos.write(am.getData());
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        // 创建并触发自定义事件
        if(eventSource!=null) {
            AudioMessageEvent e = new AudioMessageEvent(eventSource, AudioMessageEvent.RECEIVED, new File(TEMP_AUDIO_FILE));
            Event.fireEvent(eventSource, e);
        }

//            // 创建Media实例指向临时文件
//            Media media = new Media(new File(TEMP_AUDIO_FILE).toURI().toString());
//            MediaPlayer mediaPlayer = new MediaPlayer(media);
//            MediaView mediaView = new MediaView(mediaPlayer);
//
//            // 如果需要，将MediaView添加到场景中
//            // 注意：你可能需要调整场景布局来适应MediaView
//            // StackPane root = ...; // 假设root是你的布局根节点
//            // root.getChildren().add(mediaView);
//
//            // 播放音频
//            mediaPlayer.play();
//
//            // 在播放完毕后删除临时文件（如果需要）
//            mediaPlayer.setOnEndOfMedia(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Files.deleteIfExists(Paths.get(TEMP_AUDIO_FILE));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
        }

    public void stopConsuming() throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }

    public static void main(String[] argv) throws Exception {
        AudioConsumer consumer = new AudioConsumer(null);
        consumer.startConsuming();
        // 在这里可以添加逻辑来处理应用程序的关闭和资源的释放
    }
}