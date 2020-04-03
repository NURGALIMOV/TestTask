package ru.inurgalimov.persistence;

import java.util.Collection;

/**
 * Интерефейс для хранения.
 */
public interface IStorage<T> {
    /**
     * Добавляет новый элемент в хранилище.
     *
     * @param t новый элемент.
     */
    T add(T t);

    /**
     * Обновление информации о существующем элементе.
     *
     * @param t источник обновляемых данных.
     */
    boolean update(T t);

    /**
     * Удаление элемента из хранилища.
     *
     * @param t информация для удаления.
     */
    boolean delete(T t);

    /**
     * Возвращает список всех элементов в хранилище.
     */
    Collection<T> findAll();

    /**
     * Поиск элемента по ID.
     *
     * @param t источник искомого ID.
     */
    T findById(T t);
}
