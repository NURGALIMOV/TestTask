package ru.inurgalimov.logic;

import ru.inurgalimov.dao.BaseDAOImpl;
import ru.inurgalimov.models.Item;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Класс предоставления сервисов работы с данными {@link Item}.
 */
public class ItemService extends AbstractService<String, Item> {

    /**
     * Хранит действия над данными.
     */
    private Map<String, Consumer<Item>> actions = Map.of(
            "add", getStorage()::add,
            "update", getStorage()::update,
            "remove", getStorage()::remove
    );

    /**
     * Конструктор.
     *
     * @param aStorage объект обеспечивающий доступ к данным.
     */
    public ItemService(BaseDAOImpl<Item> aStorage) {
        super(aStorage);
    }

    @Override
    public List<Item> handleRequest(String s, Item item) {
        actions.getOrDefault(s, i -> {
        }).accept(item);
        return getStorage().getAll();
    }

    @Override
    public void shutDown() {
        getStorage().shutDown();
    }

}
