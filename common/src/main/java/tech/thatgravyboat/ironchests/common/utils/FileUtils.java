package tech.thatgravyboat.ironchests.common.utils;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import tech.thatgravyboat.ironchests.IronChests;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.*;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileUtils {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final String JSON = ".json";
    public static final String ZIP = ".zip";

    public static void streamFilesAndParse(Path directoryPath, BiConsumer<Reader, String> instructions, String errorMessage) {
        try (Stream<Path> zipStream = Files.walk(directoryPath);
             Stream<Path> jsonStream = Files.walk(directoryPath)) {
            zipStream.filter(f -> f.getFileName().toString().endsWith(ZIP)).forEach(path -> addZippedFile(path, instructions));
            jsonStream.filter(f -> f.getFileName().toString().endsWith(JSON)).forEach(path -> addFile(path, instructions));
        } catch (IOException e) {
            LOGGER.error(errorMessage, e);
        }
    }

    private static void addFile(Path path, BiConsumer<Reader, String> instructions) {
        File f = path.toFile();
        try {
            parseType(f, instructions);
        } catch (IOException e) {
            LOGGER.warn("File not found: {}", path);
        }
    }

    private static void addZippedFile(Path file, BiConsumer<Reader, String> instructions) {
        try (ZipFile zf = new ZipFile(file.toString())) {
            zf.stream()
                    .filter(zipEntry -> zipEntry.getName().endsWith(JSON))
                    .forEach(zipEntry -> {
                        try {
                            parseType(zf, zipEntry, instructions);
                        } catch (IOException e) {
                            LOGGER.error("Could not parse zip entry: {}", zipEntry.getName());
                        }
                    });
        } catch (IOException e) {
            LOGGER.warn("Could not read Zip File: {}", file.getFileName());
        }
    }

    private static void parseType(File file, BiConsumer<Reader, String> consumer) throws IOException {
        String name = file.getName();
        name = name.substring(0, name.indexOf('.'));

        Reader r = Files.newBufferedReader(file.toPath());

        consumer.accept(r, name);
    }

    private static void parseType(ZipFile zf, ZipEntry zipEntry, BiConsumer<Reader, String> consumer) throws IOException {
        String name = zipEntry.getName();
        name = name.substring(name.lastIndexOf("/") + 1, name.indexOf('.'));

        InputStream input = zf.getInputStream(zipEntry);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));

        consumer.accept(reader, name);
    }

    public static void setupDefaultFiles(String dataPath, Path targetPath, Path modPath) {
        if (Files.isRegularFile(modPath)) {
            try(FileSystem fileSystem = FileSystems.newFileSystem(modPath)) {
                Path path = fileSystem.getPath(dataPath);
                if (Files.exists(path)) {
                    copyFiles(path, targetPath);
                }
            } catch (IOException e) {
                LOGGER.error("Could not load source {}!!", modPath);
                e.printStackTrace();
            }
        } else if (Files.isDirectory(modPath)) {
            copyFiles(Paths.get(modPath.toString(), dataPath), targetPath);
        }
    }

    public static void setupDefaultFiles(String dataPath, Path targetPath) {
        for (Path modRoot : ModUtils.getModFilePath(IronChests.MODID)) {
            setupDefaultFiles(dataPath, targetPath, modRoot);
        }
    }

    private static void copyFiles(Path source, Path targetPath) {
        try (Stream<Path> sourceStream = Files.walk(source)) {
            sourceStream.filter(f -> f.getFileName().toString().endsWith(JSON))
                    .forEach(path -> {
                        try {
                            Files.copy(path, Paths.get(targetPath.toString(), path.getFileName().toString()), StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException e) {
                            LOGGER.error("Could not copy file: {}, Target: {}", path, targetPath);
                        }
                    });
        } catch (IOException e) {
            LOGGER.error("Could not stream source files: {}", source);
        }
    }

    public static void writeEmptyFile(File file) {
        try {
            org.apache.commons.io.FileUtils.write(file, "", StandardCharsets.UTF_8);
        } catch (Exception e) {
            //Ignore
        }
    }
}
