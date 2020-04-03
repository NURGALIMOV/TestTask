package ru.inurgalimov.persistence;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Пользователь.
 */
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class User {

    /** Номер телефона пользователя. */
    private long phoneNumber;

    /** Фамилия. */
    private String surname;

    /** Имя. */
    private String name;

    /** Отчество. */
    private String middleName;
}
