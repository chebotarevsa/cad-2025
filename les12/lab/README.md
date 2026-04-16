# Отчет о лабораторной работе

## Цель работы
Освоить навыки работы со Spring MVC и Thymeleaf
## Выполнение работы

Добавляем зависимость Thymeleaf 
![1](img/img_2.png)

Создаем шаблоны
![1](img/img_6.png)

Реализовали REST-контроллер
![1](img/img_4.png)


Создаем коллекцию запросов в Postman
![1](img/img_5.png)


Выполняем запросы в браузере
![1](img/img.png)


Выполняем запросы в Postman
![1](img/img_3.png)

Диаграмма классов
```mermaid
classDiagram
direction BT
class App
class Category
class CategoryRepository {
<<Interface>>

}
class Client
class Customer
class CustomerRepository {
<<Interface>>

}
class DataLoader
class Order
class OrderDetail
class OrderDetailRepository {
<<Interface>>

}
class OrderRepository {
<<Interface>>

}
class OrderService
class Product
class ProductController
class ProductRepository {
<<Interface>>

}
class ProductRestController

Client "1" *--> "customerRepository 1" CustomerRepository 
Client  ..>  Order : «create»
Client  ..>  OrderDetail : «create»
Client "1" *--> "orderDetailRepository 1" OrderDetailRepository 
Client "1" *--> "orderService 1" OrderService 
Client "1" *--> "productRepository 1" ProductRepository 
Customer "1" *--> "orders *" Order 
Order "1" *--> "customer 1" Customer 
Order "1" *--> "orderDetails *" OrderDetail 
OrderDetail "1" *--> "product 1" Product 
OrderService "1" *--> "repository 1" OrderRepository 
Product "1" *--> "category 1" Category 
ProductController  ..>  Product : «create»
ProductController "1" *--> "repo 1" ProductRepository 
ProductRestController "1" *--> "repo 1" ProductRepository 


```

## Выводы
Научились использовать Thymeleaf и реализовывать Web приложения с помощью Spring MVC