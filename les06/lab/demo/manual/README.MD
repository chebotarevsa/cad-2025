# Сборка приложения

## Сборка

``` bash
cd src
javac com/example/app/*.java -d bin   
```

+ d - целевая директория 


## Создание Jar файла

``` bash
jar cfm GreetingApp.jar MANIFEST.MF -C bin/ .
java -jar GreetingApp.jar
```

+ c — создание JAR-файла,
+ f — имя файла JAR (GreetingApp.jar),
+ m — указание манифеста,
+ -C bin/ — указывает папку со скомпилированными классами


## Запуск и проверка

``` bash
java -jar GreetingApp.jar
```
