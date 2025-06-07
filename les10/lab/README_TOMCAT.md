# Настройка Tomcat 11 и деплой WAR

## Сборка WAR
Из корня проекта `lab/app` запустить:
./gradlew war

Файл появится в:
build/libs/app.war

##Установка и настройка Apache Tomcat 11
Запусти сервер:
Windows: `bin/startup.bat`
Открой браузер:
http://localhost:8080

##Деплой
Скопируй файл `app.war` в директорию:
<TOMCAT_HOME>/webapps/
После этого приложение станет доступно по адресу:
http://localhost:8080/pet-shop/orders/list

##Тестирование REST-сервиса
Postman:
GET http://localhost:8080/pet-shop/product/list