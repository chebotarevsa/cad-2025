# Отчет о лаботаротоной работе №1 Gradle. Базовое приложение Spring

## Цель работы
Разработать приложение с возможностью конфигурации на фреймворке Spring. Реализовать загручик CSV файлов.

## Выполнение работы
В начале работы был установлен Temurin JDK 17.0.14. Далее, согласно руководству по установке был загружен и установлен Gradle 8.12.1.

С помощью Gradle, в директории les02/lab используя команду gradle init --package ru.bsuedu.cad.lab в терминале VSCode был создан проект Java Applications со следующими параметрами: 

java пакет - ru.bsuedu.cad.lab
Project name - product-table
Type - Application
Language - Java
Java version - 17
Structure - Single application project
DSL - Kotlin
Test framework - JUnit Jupiter
Согласно методическому пособию, при создании проекта было установлено согласие на перезапись файлов.

Для поддержки фреймворка Spring в файл ```libs.versions.toml``` была добавлена библиотека org.springframework:spring-context:6.2.2

После завершения конфигурации проекта было разработано приложение удовлетворяющее следующим требованиям:

1. Приложение должно представлять собой консольное приложение разработанное с помощью фреймворка Spring и конфигурируемое с помощью Java-конфигурации.
2. Приложение должно читать данные о товарах для магазина из csv-файла и выводить его в консоль в виде таблицы (см. рисунок).
3. CSV-файл должен располагаться в директории src/main/resources вашего приложения.

Структура приложения была разработана в соответсвии с диаграммой классов, предствленной в методическом пособии.

Были добавлены следующие классы

Reader, ResourceFileReader - класс читающий данные из csv-файла;
Parser, CSVParser - класс парсер CSV файла;

Листинг класса парсера файла:
``` java

package ru.bsuedu.cad.lab;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CSVParser implements Parser{

    @Override
    public List<Product> parse(String text) {
        List<Product> productsList = new ArrayList<>();

        String[] lines = text.split("\n");
        for (int i = 1; i < lines.length; i++)
        {
            String[] params = lines[i].split(",");
            productsList.add(new Product(
                stringToInteger(params[0]), 
                params[1], 
                params[2], 
                stringToInteger(params[3]), 
                stringToDecimal(params[4]),
                stringToInteger(params[5]),
                params[6],
                stringToCalendar(params[7]),
                stringToCalendar(params[8])));
        }
        return productsList;
    }

     public Date stringToCalendar(String dateString)
    {   
        try{
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            cal.setTime(sdf.parse(dateString));
            return cal.getTime();
        }
        catch (ParseException e)
        {
            return null; // нулевое год месяц день вписать
        }

    }

    public BigDecimal stringToDecimal(String priceString)
    {
        return new BigDecimal(priceString);

    }

    public Integer stringToInteger(String numberString)
    {
        return Integer.parseInt(numberString);
    }
    
}

```

ProductProvider, ConcreteProductProvider - класс предоставляющий список товаров;
Renderer, ConsoleTableRenderer - класс выводящий список товаров в консоль в виде таблицы;
Product - класс описывающий сущность "Товар".

Для проверки работоспособности и корректного вывода приложение было запущено с помощью команды gradle run. После запуска приложение 
вывело необходимую информацию в консоль и успешно завершилось.

## Выводы

В ходе выполнения лабораторной работы я разработал приложение с возможностью конфигурации на фреймворке Spring. Создал
структуру классоа и реализовать загручик CSV файлов.