# Отчет о лабораторной работе

## Цель работы
Освоить навыки работы со Spring Data Jpa и слоистой архитектуры приложений
## Выполнение работы

Создали логически раздельные пакеты
![1](img/img_4.png)

Для реализации транзакций использовали аннотацию @Transactional.
Кроме того добавили логгирование
![2](img/img_1.png)

Реализовали клиент для сохранения тестового заказа
![3](img/img_2.png)

При запуске создаются таблицы
![3](img/img_3.png)

При запуске приложения видно, что данные сохраняются в БД из CSV
![3](img/img.png)

Видно что данные о заказе сохраняются корректно
![3](img/img_5.png)


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
class ProductRepository {
<<Interface>>

}

Category "1" *--> "products *" Product 
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

```

## Выводы
Научились использовать Spring Data Jpa, работать с H2 и применили концепцию слоистой архитектуры на практике