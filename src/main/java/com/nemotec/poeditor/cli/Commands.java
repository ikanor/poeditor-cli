package com.nemotec.poeditor.cli;

import be.lukin.poeditor.FileTypeEnum;
import be.lukin.poeditor.POEditorClient;
import be.lukin.poeditor.models.Language;
import be.lukin.poeditor.models.UploadDetails;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class Commands {

    private static final int API_LIMIT_OFFSET_TIME = 11000;

    private static List<File> findBundles(Path path, String pattern) throws IOException {
        List<File> bundles = new ArrayList<>();

        Files.walk(path)
        .filter((Path p) -> p.getFileName().toString().matches(pattern))
        .forEach((Path p) -> bundles.add(new File(p.toString())));
        return bundles;
    }

    private static String makeFileTag(Path path, File file) {
        return path.relativize(file.toPath().getParent()).toString();
    }

    public static void push(String apiKey, String projectId, String inputFile, Language lang, Path path) throws IOException, InterruptedException {
        POEditorClient client = new POEditorClient(apiKey);

        for (File terms : findBundles(path, inputFile)) {
            UploadDetails response;
            String tag = makeFileTag(path, terms);
            response = client.uploadTerms(projectId, terms, new String[]{tag}, null, null);
            Thread.sleep(API_LIMIT_OFFSET_TIME);
            response = client.uploadLanguage(projectId, terms, lang.code, false);
            Thread.sleep(API_LIMIT_OFFSET_TIME);
        }
    }

    public static void pull(String apiKey, String projectId, String inputFile, String outputFile, Path path) throws IOException {
        POEditorClient client = new POEditorClient(apiKey);

        for (File terms : findBundles(path, inputFile)) {
            String tag = makeFileTag(path, terms);
            for (Language lang : client.getProjectLanguages(projectId)) {
                String filename = terms.getParent() + "/" + MessageFormat.format(outputFile, lang.code);
                client.export(projectId, lang.code, FileTypeEnum.PROPERTIES, null, new File(filename), new String[]{tag});
            }
        }
    }

}
