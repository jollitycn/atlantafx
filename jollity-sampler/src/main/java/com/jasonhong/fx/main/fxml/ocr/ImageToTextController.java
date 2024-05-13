package com.jasonhong.fx.main.fxml.ocr;

import atlantafx.base.theme.Tweaks;
import com.jasonhong.core.common.Callback;
import com.jasonhong.core.common.Result;
import com.jasonhong.fx.main.event.DefaultEventBus;
import com.jasonhong.fx.main.event.ScreenCaptureEvent;
import com.jasonhong.fx.main.fake.domain.ocr.ImageToTextInfo;
import com.jasonhong.fx.main.page.components.tablView.ProgressBarTableCell;
import com.jasonhong.fx.main.util.FXUtil;
import com.jasonhong.fx.main.util.MediaPlayerUtil;
import com.jasonhong.fx.main.util.RecentFilesManager;
import com.jasonhong.ocr.TesseractOCR;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static com.jasonhong.fx.main.fxml.ocr.ImageToText.SUPPORTED_MEDIA_TYPES;
//import static com.jasonhong.fx.main.layout.MainLayer.BOTTOM_STATE_BAR;
import static com.jasonhong.fx.main.layout.MainLayerTab.BOTTOM_STATE_BAR;
import static com.jasonhong.fx.main.page.Page.*;
import static javafx.collections.FXCollections.observableArrayList;
//
//import static com.jasonhong.fx.main.MainController.PGORGRESS_BAR;
//import static com.jasonhong.fx.main.MainController.PGORGRESS_BAR_LABEL;

public class ImageToTextController {

    //    private static final JComponent PGORGRESS_BAR = ;
    public Label labelCount; // 确保有一个无参数的构造方法
    //    public Menu recentMenu;
//    public MenuItem snapshotButton;
    public BorderPane rootPane;

    public ImageToTextController() {
        // 可以在这里进行初始化操作，但通常保持为空
        DefaultEventBus.getInstance().subscribe(ScreenCaptureEvent.class, this::eventHandling);
    }
//
//    @FXML
//    public MenuItem openDirectoryMenuItem;
//    @FXML
////    public MenuBar openDirectoryMenuBar;

    @FXML
    public Button startConversionButton;
    @FXML
    private TableView<ImageToTextInfo> imageListView;

    private Window primaryStage;
    @FXML
//    private TextArea resultTextArea;
    private ObservableList<ImageToTextInfo> imageFiles;
    private ObservableList<File> toConvertFiles;
    private int totalSteps;
    private int completedSteps;

    //    public ImageToTextController() {
//
//    }
    public ImageToTextController(Window primaryStage) {

        this.setPrimaryStage(primaryStage);


// 创建列并添加到TableView中

    }

    public void initTable() {
//snippet_1:start

        Button btnFile = new Button("文件", new FontIcon(Feather.FILE));
        Button btnFolder = new Button("目录", new FontIcon(Feather.FOLDER));
        Button btnSnagshot = new Button("截屏", new FontIcon(Feather.IMAGE));
        labelCount = new Label();
        var iconMenuBtn = new MenuButton("最近的项目", new FontIcon(Feather.MORE_HORIZONTAL));
        startConversionButton = new Button("开始", new FontIcon(Feather.PLAY));
        final var toolbar1 = new ToolBar(btnFile, btnFolder, iconMenuBtn, btnSnagshot,
//                new Button("New", new FontIcon(Feather.PLUS)),
//                new Button("文件", new FontIcon(Feather.FILE)),
//                new Button("目录", new FontIcon(Feather.FILE)),
//                new Button("Save", new FontIcon(Feather.SAVE)),
                new Separator(Orientation.VERTICAL), startConversionButton, labelCount, new Region()//,
//                new Button("Clean", new FontIcon(Feather.ROTATE_CCW)),
//                new Button("Compile", new FontIcon(Feather.LAYERS)),
//                        new Button("开始", new FontIcon(Feather.PLAY))

        );

        iconMenuBtn.getStyleClass().addAll(
                Tweaks.NO_ARROW
        );

        iconMenuBtn.getItems().setAll(handleOpenRecently());
        btnFile.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                handleOpenFile(mouseEvent);
            }
        });

        btnFolder.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                handleOpenDirectory(mouseEvent);
            }
        });
        btnSnagshot.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                handleSnapshot(mouseEvent);
            }
        });
        startConversionButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                handleStartConversion(mouseEvent);
            }
        });


        HBox topBox = new HBox();
        topBox.getChildren().add(toolbar1);
        HBox.setHgrow(topBox, Priority.ALWAYS);
        HBox.setHgrow(labelCount, Priority.ALWAYS);
        rootPane.setTop(topBox);

        var indexCol = new TableColumn<ImageToTextInfo, String>("");
        indexCol.setCellFactory(col -> {
            var cell = new TableCell<ImageToTextInfo, String>();
            StringBinding value = Bindings.when(cell.emptyProperty())
                    .then("")
                    .otherwise(cell.indexProperty().add(1).asString());
            cell.textProperty().bind(value);
            return cell;
        });
        imageListView.getColumns().add(indexCol);
        var brandCol = new TableColumn<ImageToTextInfo, String>("");
        brandCol.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        imageListView.getColumns().add(brandCol);

        var fileSizeCol = new TableColumn<ImageToTextInfo, Long>("");
        fileSizeCol.setCellValueFactory(new PropertyValueFactory<>("fileLength"));
        imageListView.getColumns().add(fileSizeCol);

        var progressColumn = new TableColumn<ImageToTextInfo, Double>("");
        progressColumn.setCellValueFactory(new PropertyValueFactory<>("progress"));
        progressColumn.setCellFactory(ProgressBarTableCell.forTableColumn());

        imageListView.getColumns().add(progressColumn);

//        var resultFileNameCol = new TableColumn<ImageToTextInfo, Double>("");
//        resultFileNameCol.setCellValueFactory(new PropertyValueFactory<>("resultFileName"));
//        resultFileNameCol.setCellFactory(ProgressBarTableCell.forTableColumn());

//        imageListView.getColumns().add(resultFileNameCol);
        var resultFileNameCol = new TableColumn<ImageToTextInfo, String>("");
        resultFileNameCol.setCellValueFactory(new PropertyValueFactory<>("resultFileName"));
        imageListView.getColumns().add(resultFileNameCol);


//
        imageFiles = FXCollections.observableArrayList();
        toConvertFiles = FXCollections.observableArrayList();
//        imageFiles.add(new ImageToTextInfo());
        imageListView.setItems(imageFiles);
        imageListView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
//        imageListView.getSelectionModel().setCellSelectionEnabled(true);
//        playground();
//        rootPane.getChildren().add(playground());
    }


    Feather randomIcon() {
        return Feather.values()[RANDOM.nextInt(Feather.values().length)];
    }

    @FXML
    private void handleOpenFile(Event event) {
        var extensions = SUPPORTED_MEDIA_TYPES.stream().map(s -> "*." + s).toList();
        var fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(
                "图片文件 (" + String.join(", ", extensions) + ")",
                extensions
        ));
        List<File> files = fileChooser.showOpenMultipleDialog(rootPane.getScene().getWindow());
        if (files == null || files.isEmpty()) {
            return;
        }
        toConvertFiles = FXCollections.observableArrayList();
         imageFiles = FXCollections.observableArrayList();
        for (File file : files) {
            ImageToTextInfo info = new ImageToTextInfo();
            info.setSourceFile(file);
            imageFiles.add(info);
            toConvertFiles.add(file);
            new RecentFilesManager("image-to-text").addRecentFile(file);
        }

        imageListView.getItems().setAll(imageFiles);
        labelCount.setText("检测到：" + imageFiles.size() + "张图片");
//        Platform.runLater(()->{
//            imageListView.refresh();
//        });

    }

    @FXML
    private void handleOpenDirectory(Event event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File directory = directoryChooser.showDialog(getPrimaryStage());
        if (directory != null) {
            imageFiles = findImageFilesInDirectory(directory);
            if (imageFiles == null || imageFiles.size() == 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("错误");
                alert.setHeaderText(null); // 可以设置标题上方的描述性文本，如果不需要则为null
                alert.setContentText("目录中没有找到图片！");
                alert.show();// 设置对话框的内容文本
//                PGORGRESS_BAR.setVisible(false);
//                PGORGRESS_BAR_LABEL.setVisible(false);
                startConversionButton.setDisable(true);

            } else {
                startConversionButton.setDisable(false);
            }
            imageListView.getItems().setAll(imageFiles);
            new RecentFilesManager(null).addRecentFile(directory);

        }
    }

    private ObservableList<ImageToTextInfo> findImageFilesInDirectory(File directory) {
        toConvertFiles = FXCollections.observableArrayList();
        ObservableList<ImageToTextInfo> imageFiles = FXCollections.observableArrayList();
        if(directory.isFile()) {
            extracted(directory, imageFiles);
        }else{

            for (File file : Objects.requireNonNull(directory.listFiles())) {
                extracted(file, imageFiles);
            }
        }
        return imageFiles;
    }

    private void extracted(File file, ObservableList<ImageToTextInfo> imageFiles) {
        if (file.isFile() && (file.getName().toLowerCase().endsWith(".png") || file.getName().toLowerCase().endsWith(".jpg")) || file.getName().toLowerCase().endsWith(".jpeg")) {
            ImageToTextInfo info = new ImageToTextInfo();
            //Todo:加載後臺加載 info.setImage(FXUtil.loadImageFromFile(file));
            info.setSourceFile(file);
            imageFiles.add(info);

            toConvertFiles.add(file);
            labelCount.setText("检测到：" + imageFiles.size() + "张图片");
        }
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
    public void handleStartConversion(Event actionEvent) {
        startConversionButton.setDisable(true);
        BOTTOM_STATE_BAR.initProgress();


// 假设你有一个OCR服务的URL
//        String ocrServiceUrl = "http://your-ocr-service-url/ocr";

        // 这里只是一个示例，你应该使用更复杂的逻辑来处理文件和上传
        // 例如，你可以使用文件选择器让用户选择一个图像文件，并将其发送到OCR服务

        // 模拟OCR调用（实际情况下，你会发送图像文件到OCR服务）
        if (imageFiles == null || imageFiles.size() == 0) {
            FXUtil.showErrorAlert("请在文件菜单中添加要处理的目录文件");
            return;
        }

//        new Thread(() -> {
        try {
            // 创建一个 Callback 实例
            Callback<Result> callback = new Callback<Result>() {
                @Override
                public void onAction(Result result) {
                    System.out.println("正在执行: " + result);
                    Platform.runLater(() -> {
                        try {
                            handleResult(result);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }

                @Override
                public void onResult(Result result) {
                    System.out.println("任务完成: " + result);
                    // 在这里可以执行任何需要的操作，例如更新UI等
                    Platform.runLater(() -> {
//                        File file = (File) result.getData();
                        //resultTextArea.appendText(((File) result.getData()).getName() + result.getMsg() + "\n");
                        try {
                            handleResult(result);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        updateProgress(result);
                    });
                }

                @Override
                public void onError(Result result, Exception e) {
                    e.printStackTrace();
                    Platform.runLater(() -> {

                        //resultTextArea.appendText(((File) result.getData()).getName() + result.getMsg() + "\n");
                        try {
                            handleResult(result);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        updateProgress(result);
                    });
                }

                private void handleResult(Result result) throws IOException {
                    int index = result.getId();
                    switch (result.getCode()) {
                        case START: {
                            ImageToTextHandler.updateResult(imageFiles, result);
                        }
                        case SUCCESS: {
                            ImageToTextHandler.saveOutput(imageFiles, result, (File) result.getData());
                            break;
                        }
                        case null, default: {
                        }
                    }
                    Platform.runLater(() -> {
                        imageListView.refresh();
                    });
                }

                private void updateProgress(Result result) {
                    completedSteps++;
                    BOTTOM_STATE_BAR.updateProgress(result.getMsg(), completedSteps, totalSteps);

                    if (completedSteps == totalSteps) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("消息");
                        alert.setHeaderText(null); // 可以设置标题上方的描述性文本，如果不需要则为null
                        alert.setContentText("创建结束！"); // 设置对话框的内容文本
                        BOTTOM_STATE_BAR.completeProgress();

                        startConversionButton.setDisable(false);
                        MediaPlayerUtil.play(MediaPlayerUtil.AUDIO_HANDLED);
                        alert.show();
                    }
                }
            };
            completedSteps = 0;
            totalSteps = toConvertFiles.size();
            MediaPlayerUtil.play(MediaPlayerUtil.AUDIO_HANDLING);
            TesseractOCR.createDocumentsByService(toConvertFiles, callback);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    List<File> recentFiles;
    private MenuItem[] createItems(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> new MenuItem(FAKER.babylon5().character()))
                .toArray(MenuItem[]::new);
    }
    public MenuItem[] handleOpenRecently( ) {
        List<MenuItem> menuItems = new ArrayList<>();
        if (recentFiles == null) {
            recentFiles = new RecentFilesManager(null).getRecentFiles();
//            recentMenu.getItems().clear();
            for (File fileName : recentFiles) { // 假设 recentFiles 是一个包含最近打开文件名的列表
                MenuItem menuItem = new MenuItem(fileName.getPath());
                menuItem.setOnAction(event -> {
                    if (fileName.exists()) {
                        if (fileName.isFile()) {
                            ImageToTextInfo info = new ImageToTextInfo();
                            info.setSourceFile(fileName);
                            toConvertFiles.clear();
                            toConvertFiles.add(fileName);
                            imageListView.getItems().setAll(info);
                        } else {
                            imageFiles = findImageFilesInDirectory(fileName);
                            imageListView.getItems().setAll(imageFiles);
                        }
                    }
                });
                menuItems.add(menuItem);
            }
        }
        return menuItems.toArray(new MenuItem[0]);
//        openRecently.
    }

    //    @Subscribe
    public void eventHandling(ScreenCaptureEvent event) { // 事件接收者
//        dataLoading();
        // 设定输出文件的路径和名称

        // 尝试保存BufferedImage到文件
        try {
            String outputFile = "data/temp/snapeshot_" + System.currentTimeMillis() + ".jpg"; //可以是jpg, png, bmp等，取决于你使用的ImageWriter
            File outputfile = new File(outputFile);
            if (!outputfile.exists()) {
                outputfile.mkdirs();
            }
            ImageIO.write((BufferedImage) event.getObject(), "jpg", outputfile);

//            if (imageFiles == null || toConvertFiles==null) {
//                toConvertFiles = FXCollections.observableArrayList();
//                imageFiles = FXCollections.observableArrayList();
//            }
            if (imageFiles.size() > 0) {
                toConvertFiles.add(0, outputfile);
                ImageToTextInfo info = new ImageToTextInfo();
                info.setSourceFile(outputfile);
                imageFiles.set(0, info);
                imageListView.getItems().add(0, info);
            } else {
                toConvertFiles.add(0, outputfile);
                ImageToTextInfo info = new ImageToTextInfo();
                info.setSourceFile(outputfile);
                imageFiles.add(info);
                imageListView.getItems().add(info);
            }

            System.out.println("Image has been saved!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save image!");
        }
    }

    public void handleSnapshot(Event actionEvent) {
        com.jasonhong.fx.main.util.ScreenCaptureApp.handleSnapshot(actionEvent);
    }


    // 辅助方法，例如加载图片到ListView，调用OCR服务等
    // ...
}