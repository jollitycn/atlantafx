/* SPDX-License-Identifier: MIT */

package com.jasonhong.fx.main.page.showcase.blueprints;

import com.jasonhong.fx.main.fxml.audio.musicplayer.MusicPlayerPage;
import com.jasonhong.fx.main.fxml.ocr.ImageToText;
import com.jasonhong.fx.main.page.Page;
import com.jasonhong.fx.main.util.NodeUtils;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URI;

import static javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED;

public final class BlueprintsPage extends ScrollPane implements Page {

    public static final String NAME = "Blueprints";
    private final VBox wrapper;

    @Override
    public String getName() {
        return NAME;
    }

    public BlueprintsPage() {
        super();

        try {
            wrapper = new VBox();
            wrapper.setAlignment(Pos.TOP_CENTER);

            var loader = new FXMLLoader(
                    getClass().getResource("index.fxml")
            );
            VBox fxmlContent = loader.load();
//            loadTask(fxmlContent);
            ((Pane) fxmlContent).setMaxWidth(Page.MAX_WIDTH);
            VBox.setVgrow(fxmlContent, Priority.ALWAYS);
            wrapper.getChildren().setAll(fxmlContent);

            NodeUtils.setScrollConstraints(this, AS_NEEDED, true, AS_NEEDED, true);
            setMaxHeight(20_000);
            setContent(wrapper);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load FXML file", e);
        }

        setId("blueprints");
    }

    private void loadTask(VBox parent) {
// 假设你已经在某个方法中，并且有一个HBox的实例（或者你可以创建一个新的）
        HBox hBox = new HBox(20.0); // 设置间距为20

        try {
            FXMLLoader loader = new FXMLLoader(ImageToText.class.getResource("image-to-text.fxml"));
            Node checklistProposalNode = loader.load();
            hBox.getChildren().add(checklistProposalNode);

            // 加载"checklist-proposal.fxml"
              loader = new FXMLLoader(getClass().getResource("checklist-proposal.fxml"));
              checklistProposalNode = loader.load();
            hBox.getChildren().add(checklistProposalNode); // 将加载的节点添加到HBox中

            // 加载"message-editor.fxml"，并设置maxHeight（虽然通常不推荐使用这么大的值，除非有特别的需求）
            loader = new FXMLLoader(getClass().getResource("message-editor.fxml"));
            Node messageEditorNode = loader.load();
//            messageEditorNode.setMaxHeight(Double.MAX_VALUE); // 设置maxHeight为Double的最大值
            hBox.getChildren().add(messageEditorNode);

            // 加载"button-list.fxml"，并设置maxHeight
//            loader = new FXMLLoader(getClass().getResource("button-list.fxml"));
//            Node buttonListNode = loader.load();
////            buttonListNode.setMaxHeight(Double.MAX_VALUE); // 设置maxHeight为Double的最大值
//            hBox.getChildren().add(buttonListNode);

                        loader = new FXMLLoader(MusicPlayerPage.class.getResource("music-player.fxml"));
            Node buttonListNode = loader.load();
//            buttonListNode.setMaxHeight(Double.MAX_VALUE); // 设置maxHeight为Double的最大值
            hBox.getChildren().add(buttonListNode);
        } catch (IOException e) {
            // 处理异常，例如通过记录错误或显示错误消息
            e.printStackTrace();
        }

// 现在hBox包含了从三个不同的FXML文件加载的UI组件
        parent.getChildren().add(hBox);
    }

    @Override
    public Parent getView() {
        return this;
    }

    @Override
    public boolean canDisplaySourceCode() {
        return true;
    }

    @Override
    public @Nullable URI getJavadocUri() {
        return null;
    }

    @Override
    public Node getSnapshotTarget() {
        return wrapper;
    }

    @Override
    public void reset() {
    }
}
