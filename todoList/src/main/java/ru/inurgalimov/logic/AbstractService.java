package ru.inurgalimov.logic;

import ru.inurgalimov.dao.BaseDAOImpl;

/**
 * Абстрактный класс предоставления сервисов работы с данными.
 */
public abstract class AbstractService<K, T> implements IService<K, T> {

    /** Объект обеспечивающий доступ к данным. */
    private final BaseDAOImpl<T> storage;

    /**
     * Конструктор.
     *
     * @param aStorage объект обеспечивающий доступ к данным.
     */
    public AbstractService(BaseDAOImpl<T> aStorage) {
        storage = aStorage;
    }

    /**
     * Возвращает объект обеспечивающий доступ к данным.
     *
     * @return объект обеспечивающий доступ к данным.
     */
    public BaseDAOImpl<T> getStorage() {
        return storage;
    }

    /** Освобождает ресурсы. */
    public abstract void shutDown();

}
