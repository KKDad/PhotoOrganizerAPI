package org.stapledon.photo.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResourceHelper {
    private static final Logger LOG = LoggerFactory.getLogger(ResourceHelper.class);

    @SuppressWarnings("java:S1141")
    public static String getContents(String resource) {
        String contents = null;
        Path resourcePath;
        try {
            var url = ResourceHelper.class.getClassLoader().getResource(resource);
            if (url != null && url.toURI().getScheme().equals("jar")) {
                FileSystem fs = null;
                try {
                    fs = FileSystems.newFileSystem(url.toURI(), new HashMap<>());
                    LOG.trace("Using a newFileSystem");
                    resourcePath = fs.getPath(resource);
                    contents = ResourceHelper.getContents(resourcePath);
                } catch (IOException | FileSystemAlreadyExistsException e) {
                    if (fs != null) {
                        fs.close();
                    }
                    fs = FileSystems.getFileSystem(url.toURI());
                    LOG.debug("Using a getFileSystem");
                    resourcePath = fs.getPath(resource);
                    contents = ResourceHelper.getContents(resourcePath);
                } finally {
                    if (fs != null)
                        fs.close();
                }
            } else if (url != null) {
                contents = ResourceHelper.getContents(Path.of(url.toURI()));
            }
            return contents;
        } catch (URISyntaxException | IOException e) {
            LOG.error(e.getLocalizedMessage());
        }
        return null;
    }

    public static String getContents(Path resource) {
        try {
            var contents = new StringBuilder();
            try (Stream<String> ss = Files.lines(resource)) {
                for (String line : ss.collect(Collectors.toList()))
                    contents.append(line).append(System.getProperty("line.separator"));
            }
            return contents.toString();
        } catch (IOException | NullPointerException e) {
            LOG.error("Error loading resource {}: {}", resource, e.getLocalizedMessage(), e);
        }
        return null;
    }
}
