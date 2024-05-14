package com.jasonhong.fx.main.util;

import javafx.scene.Scene;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.Set;


public class DialogUtil {

    public static final String AUDIO = "Audio Files";
    private static final RecentFilesManager recentFilesManager = new RecentFilesManager(RecentFilesManager.RecentFileType.DIALOG);
    public static File chooseOutputFile( String extName, Set<String> exts) {
        FileChooser fileChooser = new FileChooser();
        var extensions = exts.stream().map(s -> "*." + s).toList();
//        var fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(
                extName +" (" + String.join(", ", extensions) + ")",
                extensions
        ));
//        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("WAV files (*.wav)", "*.wav"));
       File outputFile = fileChooser.showSaveDialog(null);
        return outputFile;
    }

    public static @Nullable List<File> openAudioDailog(Scene screne,String extName,  Set<String> exts) {
        var extensions = exts.stream().map(s -> "*." + s).toList();
        var fileChooser = new FileChooser();
       List<File> recentFiles=  recentFilesManager.getRecentFiles();
        if (!(recentFiles == null || recentFiles.isEmpty())) {
            fileChooser.setInitialDirectory(recentFilesManager.getRecentFiles().get(0));
        }
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(
                extName + " (" + String.join(", ", extensions) + ")",
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
