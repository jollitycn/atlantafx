package com.jasonhong.fx.main.fxml.ocr;

import com.jasonhong.core.common.Result;
import com.jasonhong.core.common.Status;
import com.jasonhong.fx.main.fake.domain.ocr.ImageToTextInfo;
import javafx.collections.ObservableList;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

public class ImageToTextHandler {
    public static void saveOutput(List<ImageToTextInfo> imageFiles,Result result, File file) throws IOException {
        ImageToTextInfo info = imageFiles.get(result.getId());
        String outputFileName = info.getSourceFile().getName().replaceFirst("\\.[^.]+$", ".txt");
        String source = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        File localFile = new File(String.valueOf(Path.of(info.getSourceFile().getParent(), outputFileName)));
        FileUtils.writeStringToFile(localFile, source, Charset.forName(String.valueOf(StandardCharsets.UTF_8)));
        info.setResultTextFile(localFile);
        info.setResult(result);
//        info.setStatus(Status.Success);
    }

    public static void updateResult(ObservableList<ImageToTextInfo> imageFiles, Result result) {
        ImageToTextInfo info = imageFiles.get(result.getId());
        info.setResult(result);
    }
}
