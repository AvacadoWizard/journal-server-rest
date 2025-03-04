package com.journal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Entry {
    private String title;
    private LocalDate datePublished;
    private String entryText;

    public Entry(String title, String datePublished, String entryText) {
        this.title = title;
        setDatePublished(datePublished);
        this.entryText = entryText;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(String datePublished) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            this.datePublished = LocalDate.parse(datePublished, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
        }
    }

    public String getEntryText() {
        return entryText;
    }

    public void setEntryText(String entryText) {
        this.entryText = entryText;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "title='" + title + '\'' +
                ", datePublished=" + datePublished +
                ", entryText='" + entryText + '\'' +
                '}';
    }
}