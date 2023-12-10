package org.stapledon.data;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.stapledon.entities.Folder;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FolderService {

    @Value("${organizer.main.base-path")
    private String basePath;

    private static final List<Folder> folderCache = new ArrayList<>();

    private static final Object lock = new Object();

    public List<Folder> getAllFolders() {
        if (folderCache.isEmpty()) {
            synchronized (lock) {
                if (folderCache.isEmpty()) {
                    File baseDirectory = new File(basePath);
                    if (baseDirectory.exists() && baseDirectory.isDirectory()) {
                        getAllFoldersRecursive(baseDirectory, folderCache);
                    }
                }
            }
        }
        return folderCache;
    }

    private void getAllFoldersRecursive(File directory, List<Folder> folders) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    Folder folder = new Folder(file.getName(), new Date(file.lastModified()), new ArrayList<>());
                    folders.add(folder);
                    getAllFoldersRecursive(file, folder.getChildFolders());
                }
            }
        }
    }
}
