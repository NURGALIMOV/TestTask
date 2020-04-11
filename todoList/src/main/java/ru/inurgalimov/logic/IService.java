package ru.inurgalimov.logic;

import java.util.List;

/**
 * Общий интерфейс предоставления сервисов работы с данными.
 */
public interface IService<K, T> {

    /**
     * Обрабатывает запрос на предоставления необходимого сервиса.
     */
    List<T> handleRequest(K k, T t);

}
