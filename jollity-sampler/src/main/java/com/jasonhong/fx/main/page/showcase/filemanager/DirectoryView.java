/* SPDX-License-Identifier: MIT */

package com.jasonhong.fx.main.page.showcase.filemanager;

import javafx.scene.layout.Pane;

import java.nio.file.Path;
import java.util.function.Consumer;

interface DirectoryView {

    Pane getView();

    FileList getFileList();

    void setDirectory(Path path);

    void setOnAction(Consumer<Path> handler);
}
