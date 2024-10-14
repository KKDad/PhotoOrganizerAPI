package org.stapledon.data;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.stapledon.entities.Item;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    @Value("${organizer.main.base-path}")
    private String basePath;

    private final List<Item> itemCache = Collections.synchronizedList(new ArrayList<>());
    private static final Object lock = new Object();

    public List<Item> getAllFolders() {
        if (itemCache.isEmpty()) {
            synchronized (lock) {
                if (itemCache.isEmpty()) {
                    File baseDirectory = new File(basePath);
                    if (baseDirectory.exists() && baseDirectory.isDirectory()) {
                        getAllFoldersRecursive(baseDirectory, itemCache);
                    }
                }
            }
        }
        synchronized (itemCache) {
            return new ArrayList<>(itemCache);
        }
    }

    private void getAllFoldersRecursive(File directory, List<Item> items) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    Item item = Item.builder()
                            .name(file.getName())
                            .date(LocalDate.ofEpochDay(file.lastModified() / 1000 / 60 / 60 / 24))
                            .childItems(new ArrayList<>())
                            .type(Item.ItemType.FOLDER)
                            .build();
                    items.add(item);
                    getAllFoldersRecursive(file, item.getChildItems());
                }
            }
        }
    }
}
