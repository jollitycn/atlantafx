package com.jasonhong.fx.main.util;

import javafx.scene.Scene;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.Set;


public class DialogUtil {
    private static final RecentFilesManager recentFilesManager = new RecentFilesManager(RecentFilesManager.RecentFileType.DIALOG);

    public static @Nullable List<File> openAudioDailog(Scene screne, Set<String> exts) {
        var extensions = exts.stream().map(s -> "*." + s).toList();
        var fileChooser = new FileChooser();
       List<File> recentFiles=  recentFilesManager.getRecentFiles();
        if (!(recentFiles == null || recentFiles.isEmpty())) {
            fileChooser.setInitialDirectory(recentFilesManager.getRecentFiles().get(0));
        }
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(
                "audio files (" + String.join(", ", extensions) + ")",
                extensions
        ));
        List<File> files = fileChooser.showOpenMultipleDialog(screne.getWindow());
        if (files == null || files.isEmpty()) {
            return null;
        } else {
            recentFilesManager.addRecentFile(files.get(0).getParentFile());
        }
        return files;
    }
}
