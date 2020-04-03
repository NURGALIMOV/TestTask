package ru.inurgalimov.persistence;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Класс описывающий место в зале.
 */
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class Place {

    /** Идентификатор зала. */
    private int hallId;

    /** Ряд. */
    private int row;

    /** Место. */
    private int placeNum;

    /** Состояние. */
    private boolean booked;

    /** Забронировавший пользователь. */
    private User user;
}
