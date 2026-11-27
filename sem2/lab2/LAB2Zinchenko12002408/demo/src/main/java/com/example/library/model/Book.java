package com.example.library.model;

public class Book {
    private static Long nextId = 1L;

    private Long id;
    private String title;
    private String author;
    private int year;
    private boolean issued;

    public Book() {
        this.id = generateId();
    }

    public Book(String title, String author, int year) {
        this.id = generateId();
        this.title = title;
        this.author = author;
        this.year = year;
        this.issued = false;
    }

    private synchronized Long generateId() {
        return nextId++;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public boolean isIssued() { return issued; }
    public void setIssued(boolean issued) { this.issued = issued; }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", year=" + year +
                ", issued=" + issued +
                '}';
    }
}