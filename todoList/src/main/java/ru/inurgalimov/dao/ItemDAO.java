package ru.inurgalimov.dao;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import ru.inurgalimov.models.Item;

import java.util.List;

/**
 * Доступ к данным {@link Item}.
 */
public class ItemDAO extends BaseDAOImpl<Item> {

    /** Единственный экземпляр класса. */
    private static final ItemDAO INSTANCE =
            new ItemDAO( new Configuration().configure("/ru/inurgalimov/hibernate.cfg.xml"));

    /**
     * Конструктор.
     *
     * @param configure конфигурация.
     */
    private ItemDAO(Configuration configure) {
        super(configure);
    }

    /**
     * Возвращает единственный экземпляр класса.
     *
     * @return экземпляр класса.
     */
    public static ItemDAO getInstance() {
        return INSTANCE;
    }

    @Override
    public void add(Item item) {
        try(Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(item);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Item item) {
        try(Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            session.update(item);
            session.getTransaction().commit();
        }
    }

    @Override
    public void remove(Item item) {
        try(Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            session.delete(item);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Item> getAll() {
        try(Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            return session.createQuery("from Item").list();
        }
    }
}
