package ru.otus.java.basic.http.app;

import java.math.BigDecimal;
import java.util.*;

public class ItemsRepository {
    private List<Item> items;

    public ItemsRepository() {
        this.items = new ArrayList<>(Arrays.asList(
           new Item(1L, "Bread", BigDecimal.valueOf(80)),
           new Item(2L, "Milk", BigDecimal.valueOf(120)),
           new Item(3L, "Cheese", BigDecimal.valueOf(200))
        ));
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public Item add(Item item) {
        long newId = items.stream().mapToLong(Item::getId).max().orElse(0L) +1L;
        item.setId(newId);
        items.add(item);
        return item;
    }
}
