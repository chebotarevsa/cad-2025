package ru.bsuedu.cad.lab;

import lombok.*;

/**
 * Простой класс для хранения данных о категории.
 * Соответствует таблице CATEGORIES.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Category {
    private int categoryId;
    private String name;
    private String description;
}