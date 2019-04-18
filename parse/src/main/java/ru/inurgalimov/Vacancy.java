package ru.inurgalimov;

/**
 * @author Ilshat Nurgalimov
 * @since 06.05.2019
 */
public class Vacancy {
    private final String name;
    private final String text;
    private final String link;
    private final String date;

    public Vacancy(final String name, final String text, final String link, final String date) {
        this.name = name;
        this.text = text;
        this.link = link;
        this.date = date;
    }

    public String getName() {
        return this.name;
    }

    public String getText() {
        return this.text;
    }

    public String getLink() {
        return this.link;
    }

    public String getDate() {
        return date;
    }
}
