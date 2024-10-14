package org.stapledon.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Item {

    private String name;
    private ItemType type;
    private Object content;
    private long size;    
    private LocalDate date;
    private List<Item> childItems;


    public enum ItemType {
        FOLDER,
        FILE
    }
}
