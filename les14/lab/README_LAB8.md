# Отчет о лабораторной работе

## Цель работы
Освоить навыки тестирования приложений
## Выполнение работы

Добавляем зависимость Spring Starter Test и Spring Starter WebMvc Test
![1](img/img_4.png)

Добавляем плагин JaCoCo
![1](img/img_5.png)

Запускаем тесты
![1](img/img_6.png)

Проверяем что JaCoCo сформировал отчет
![1](img/img_7.png)

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
class OrderController
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
class SecurityUsersConfig

Client "1" *--> "customerRepository 1" CustomerRepository 
Client  ..>  Order : «create»
Client  ..>  OrderDetail : «create»
Client "1" *--> "orderDetailRepository 1" OrderDetailRepository 
Client "1" *--> "orderService 1" OrderService 
Client "1" *--> "productRepository 1" ProductRepository 
Customer "1" *--> "orders *" Order 
Order "1" *--> "customer 1" Customer 
Order "1" *--> "orderDetails *" OrderDetail 
OrderController "1" *--> "customerRepository 1" CustomerRepository 
OrderController  ..>  Order : «create»
OrderController  ..>  OrderDetail : «create»
OrderController "1" *--> "orderRepository 1" OrderRepository 
OrderController "1" *--> "productRepository 1" ProductRepository 
OrderDetail "1" *--> "product 1" Product 
OrderService "1" *--> "repository 1" OrderRepository 
Product "1" *--> "category 1" Category 
ProductController  ..>  Product : «create»
ProductController "1" *--> "repo 1" ProductRepository 
ProductRestController "1" *--> "repo 1" ProductRepository 


```

## Выводы
Научились проводить юнит-тестирование и интеграционное тестирование средствами Spring