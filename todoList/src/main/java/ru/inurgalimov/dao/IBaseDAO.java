package ru.inurgalimov.dao;

import java.util.List;

/**
 * Общий интерфейс доступа к данным.
 *
 * @param <T> тип данных.
 */
public interface IBaseDAO<T> {

    /**
     * Добавляет данные.
     *
     * @param t добавляемые данные.
     */
    void add(T t);

    /**
     * Обновляет данные.
     *
     * @param t обновляемые данные.
     */
    void update(T t);

    /**
     * Удаляет данные.
     *
     * @param t удаляемые данные.
     */
    void remove(T t);

    /**
     * Возвращает список данных.
     *
     * @return список данных.
     */
    List<T> getAll();

}
