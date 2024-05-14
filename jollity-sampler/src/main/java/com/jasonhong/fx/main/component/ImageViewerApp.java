package com.jasonhong.fx.main.component;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;  
import javafx.scene.Scene;  
import javafx.scene.control.*;  
import javafx.scene.image.Image;  
import javafx.scene.image.ImageView;  
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;  
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;  
  
public class ImageViewerApp extends Application {
    private  String basePath =
            "E:\\studio\\project\\audioPlayer\\data\\books\\eae3219072633303eae86e3\\当你又忙又累，必须人间清醒";
    private  String imageDirPath = String.valueOf(Path.of(getBasePath(),"page")); // 替换为你的图片目录


    @Override  
    public void start(Stage primaryStage) throws IOException {
        VBox imagePane = createImagePane();

//        // 将两个面板放入水平布局中
//        HBox root = new HBox(10, imagePane); // 10是两个面板之间的间距
//        root.setAlignment(Pos.TOP_CENTER); // 设置布局对齐方式
//        root.setPadding(new Insets(10)); // 设置内边距

        // 创建场景和舞台
        Scene scene = new Scene(imagePane, 800, 680); // 设置场景大小
        primaryStage.setScene(scene);
        primaryStage.setTitle("Audio and Image Viewer");
        primaryStage.show();
    }

    // 创建一个ListView来展示图片
    ListView<Path> imageViewList = new ListView<>();
    private VBox createImagePane() throws IOException {
        VBox imagePane = new VBox();
        imagePane.setPadding(new Insets(10));

        // 加载图片目录中的所有图片
        List<Path> imageFiles = loadImageFiles(imageDirPath);

        imageViewList.setCellFactory(lv -> new ListCell<Path>() {
            private final ImageView imageView = new ImageView();

            {
                // 设置ListCell的背景色为黑色
                setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
                // 适配ImageView到ListCell的大小
                imageView.setFitHeight(getHeight() - getInsets().getTop() - getInsets().getBottom());
                imageView.setFitWidth(getWidth() - getInsets().getLeft() - getInsets().getRight());

            }

            @Override
            protected void updateItem(Path item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // 加载图片并设置到ImageView
                    // 假设你想要的宽度和高度
                    double desiredWidth =  740;
                    double desiredHeight = 740;

// 保持图片原始的宽高比，可能需要先计算一个缩放比例
// 这里为了简单起见，我们直接指定了宽度和高度

// 加载图片并指定宽度和高度（这将自动缩放图片）
                    Image image = new Image(item.toUri().toString(), desiredWidth, desiredHeight, true, true);

                    imageView.setImage(image);

                    // 适配ImageView到ListCell的大小
                    imageView.setFitHeight(getHeight() - getInsets().getTop() - getInsets().getBottom());
                    imageView.setFitWidth(getWidth() - getInsets().getLeft() - getInsets().getRight());
//                    imageView.setFitWidth(getWidth()  ); // 假设你想要图片占据屏幕宽度的一半
//                    imageView.setFitHeight(getHeight() ); // 假设你想要图片占据屏幕高度的一半
                    imageView.setPreserveRatio(true); // 保持图片比例

                    setGraphic(imageView);
                    setText(null);
                }
            }
        });

        // 将图片路径添加到ListView中
        imageViewList.getItems().addAll(imageFiles);

       setButtons();
        // 将按钮和ListView放入布局中
//        HBox buttonBox = new HBox(5, root);

        // 添加到VBox中，并使用ScrollPane来处理滚动
        ScrollPane scrollPane = new ScrollPane(imageViewList);
        scrollPane.setFitToWidth(true); // 根据需要设置滚动窗格的属性
        scrollPane.setFitToHeight(true); // 根据需要设置滚动窗格的属性
        imagePane.getChildren().add(scrollPane);
        imagePane.getChildren().add( setButtons());
        imagePane.setAlignment(Pos.CENTER);
        // 设置VBox的填充属性，如果父容器是StackPane，这将使VBox填充整个StackPane
        VBox.setVgrow(scrollPane, Priority.ALWAYS); // 使VBox中的ScrollPane始终尝试占用额外空间

        return imagePane;
    }

    private BorderPane setButtons() {
        // 创建上一页和下一页按钮
        Button prevButton = new Button("< prev page");
        Button nextButton = new Button("> next page");

        // 设置按钮的事件处理程序
        prevButton.setOnAction(e -> {
            int currentIndex = imageViewList.getSelectionModel().getSelectedIndex();
            if (currentIndex > 0) {
                imageViewList.getSelectionModel().selectPrevious();
            } else {
                // 如果已经是第一页，可以选择不执行任何操作或跳转到最后一页
                imageViewList.getSelectionModel().selectLast();
            }
        });

        nextButton.setOnAction(e -> {
            int currentIndex = imageViewList.getSelectionModel().getSelectedIndex();
            if (currentIndex < imageViewList.getItems().size() - 1) {
                imageViewList.getSelectionModel().selectNext();
            } else {
                // 如果已经是最后一页，可以选择不执行任何操作或跳转到第一页
                imageViewList.getSelectionModel().selectFirst();
            }
        });


        BorderPane root = new BorderPane();
        root.setLeft(prevButton); // 左对齐
        root.setRight(nextButton);
        setScrollControl(root);// 右对齐  // 设置间距为5
        return root;
    }

    private ScrollPane scrollPane;
    private Timeline scrollTimeline;
    private double scrollSpeed = 10; // 初始滚动速度，可以根据需要调整
    private void setScrollControl( BorderPane root){
        // 滑块来控制滚动速度
        Slider slider = new Slider(1, 100, scrollSpeed); // 范围从1到100，初始值为scrollSpeed
        // 滑块来控制滚动速度
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                scrollSpeed = newValue.doubleValue();
                if (scrollTimeline != null && scrollTimeline.getStatus() == Timeline.Status.RUNNING) {
                    // 如果Timeline正在运行，更新其关键帧的持续时间
//
//                    // 创建一个 Timeline，设置动画的持续时间为 2 秒
//                    Timeline timeline = new Timeline(
//                          new KeyFrame(Duration.millis(1000 / scrollSpeed, new KeyValue(rect.fillProperty(), Color.BLUE)), // 开始时的颜色
//                           new KeyFrame(Duration.millis(1000 / scrollSpeed, new KeyValue(rect.fillProperty(), Color.RED)),  // 1 秒后的颜色
//                             new KeyFrame(Duration.millis(1000 / scrollSpeed, new KeyValue(rect.fillProperty(), Color.GREEN)) // 2 秒后的颜色
//                     );
//
//                    // 设置 Timeline 的循环次数为 1（默认也是 1）
//                    timeline.setCycleCount(1);
//                    // 启动 Timeline
//                    timeline.play();
                }
            }
        });

        Button startStopButton = new Button("开始/停止滚动");
        startStopButton.setOnAction(event -> {
            if (scrollTimeline == null || scrollTimeline.getStatus() == Timeline.Status.STOPPED) {
                // 创建或重启Timeline
                scrollTimeline = new Timeline(new KeyFrame(Duration.millis(1000 / scrollSpeed),
                        String.valueOf(new Runnable() {
                            @Override
                            public void run() {
                                scrollPane.setVvalue(scrollPane.getVvalue() + 1); // 假设垂直滚动
                            }
                        })));
                scrollTimeline.setCycleCount(Timeline.INDEFINITE);
                scrollTimeline.play();
            } else {
                // 停止Timeline
                scrollTimeline.stop();
            }
        });

        VBox controls = new VBox(10, new Label("滚动速度控制:"), slider, startStopButton);
        controls.setAlignment(Pos.CENTER);
        controls.setPadding(new Insets(10));
        root.setCenter(controls);
    }
  
    private List<Path> loadImageFiles(String directoryPath) throws IOException {
        return Files.walk(Paths.get(directoryPath))  
                .filter(Files::isRegularFile)  
                .filter(path -> path.toString().endsWith(".png")) // 根据需要添加更多图片格式
                .collect(Collectors.toList());  
    }  
  
    private ImageView createImageView(Path imagePath) {  
        Image image = new Image(imagePath.toUri().toString());  
        return new ImageView(image);  
    }  
  
    public static void main(String[] args) {  
        launch(args);
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
        imageDirPath = String.valueOf(Path.of(basePath,"page")); // 替换为你的图片目录


    }
}