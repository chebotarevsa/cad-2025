# Отчет о лабораторной работе

## Цель работы
Освоить навыки работы с Jakarta Servlet API 
## Выполнение работы


Создаем образ с Tomcat
![1](img/img_2.png)

Запускаем контейнер
![1](img/img_3.png)


Создаем заказ
![1](img/img_1.png)

Проверяем создаем заказа
![1](img/img.png)

Проверяем REST API через Postman
![1](img/img_5.png)

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
class CreateOrderServlet
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
class OrderServlet
class Product
class ProductRepository {
<<Interface>>

}
class ProductRestServlet

Category "1" *--> "products *" Product 
Client "1" *--> "customerRepository 1" CustomerRepository 
Client  ..>  Order : «create»
Client  ..>  OrderDetail : «create»
Client "1" *--> "orderDetailRepository 1" OrderDetailRepository 
Client "1" *--> "orderService 1" OrderService 
Client "1" *--> "productRepository 1" ProductRepository 
CreateOrderServlet  ..>  Category : «create»
CreateOrderServlet  ..>  Customer : «create»
CreateOrderServlet  ..>  Order : «create»
CreateOrderServlet  ..>  OrderDetail : «create»
CreateOrderServlet "1" *--> "orderDetailRepository 1" OrderDetailRepository 
CreateOrderServlet "1" *--> "orderRepository 1" OrderRepository 
CreateOrderServlet  ..>  Product : «create»
Customer "1" *--> "orders *" Order 
Order "1" *--> "customer 1" Customer 
Order "1" *--> "orderDetails *" OrderDetail 
OrderDetail "1" *--> "product 1" Product 
OrderService "1" *--> "repository 1" OrderRepository 
OrderServlet "1" *--> "orderRepository 1" OrderRepository 
Product "1" *--> "category 1" Category 
ProductRestServlet "1" *--> "productRepository 1" ProductRepository 


```

## Выводы
Научились использовать Jakarta Servlet и реализовывать REST API эндпоинты