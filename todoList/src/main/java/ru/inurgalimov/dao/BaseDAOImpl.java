package ru.inurgalimov.dao;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Базовый абстрактный класс доступа к данным.
 *
 * @param <T> тип данных.
 */
public abstract class BaseDAOImpl<T> implements IBaseDAO<T> {

    /** Фабрика сессий */
    private final SessionFactory sessionFactory;

    /**
     * Конструктор.
     *
     * @param configure конфигурация.
     */
    public BaseDAOImpl(Configuration configure) {
        sessionFactory = configure.buildSessionFactory();
    }

    /**
     * Возвращает фабрику сессий.
     *
     * @return фабрика сессий.
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Освобождает ресурсы.
     */
    public void shutDown() {
        sessionFactory.close();
    }
}
