package ru.inurgalimov.models;

import java.util.Calendar;

/**
 * Модель данных.
 */
public class Item {

    /** Идентификатор задачи. */
    private int id;

    /** Описание задачи. */
    private String description;

    /** Дата создания. */
    private Calendar created;

    /** Статус выполнения. */
    private boolean done;

    /**
     * Возвращает идентификатор задачи.
     *
     * @return идентификатор.
     */
    public int getId() {
        return id;
    }

    /**
     * Устанавливает идентификатор задачи.
     *
     * @param id идентификатор.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Возвращает описание задачи.
     *
     * @return описание задачи.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Устанавливает описание задачи.
     *
     * @param description описание задачи.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Возвращает дату создания задачи.
     *
     * @return дата создания задачи.
     */
    public Calendar getCreated() {
        return created;
    }

    /**
     * Устанавливает дату создания задачи.
     *
     * @param created дата создания.
     */
    public void setCreated(Calendar created) {
        this.created = created;
    }

    /**
     * Возвращает статус выполнения задачи.
     *
     * @return статус выполнения задачи.
     */
    public boolean isDone() {
        return done;
    }

    /**
     * Устанавливает статус выполнения задачи.
     *
     * @param done статус выполнения задачи.
     */
    public void setDone(boolean done) {
        this.done = done;
    }
}
