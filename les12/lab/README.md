# Отчет о лаботаротоной работе №6 Разработка Web-приложений с использованием технологии Spring MVC

## Цель работы
Разработка интерфейса для приложения с использованием технологии Spring MVC

## Выполнение работы
Для использования технологии Spring MVC была добавлена библиотека org.springframework:spring-webmvc.

Был реализован REST API для работы с заказами с целью:
- получения списка заказов;
- получения заказа по идентификатору;
- создание заказа;
- удаление заказа;
- изменение заказа.

```java

@RestController
@RequestMapping("/api/v1/order")
public class OrderAPIController {

    final private OrderService orderService;

    public OrderAPIController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("")
    public List<OrderDTO> getOrders(){
        var orders = orderService.getOrders();
        return orders.stream().map(OrderMapper::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public OrderDTO getOrdersById(@PathVariable(name = "id") int id){
        return OrderMapper.toDTO(orderService.getById(id));
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable(name = "id") int id){
        orderService.deleteById(id);
    }

    @PostMapping("")
    public OrderDTO createOrder(@RequestBody CreateOrderDTO newOrder){
        var items = newOrder.getItems().stream().map(OrderItemMapper::toItem).collect(Collectors.toList());
        var order = orderService.createOrder(newOrder.getCustomerId(),newOrder.getAddress(), items);
        return OrderMapper.toDTO(order);
    }

    @PatchMapping("/{id}")
    public void updateOrder(@PathVariable(name = "id") int id, @RequestBody UpdateOrderDTO orderDTO){
        orderService.updateOrder(id, orderDTO.getDate(), orderDTO.getStatus(), orderDTO.getCustomerId(), null);
    }

}

```

Для обновления интерфейса приложения был подключен шаблонизатор Thymeleaf.

Подключение Thymeleaf в классе ```Config.java```:

```java

@Bean
    public ViewResolver viewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding("UTF-8");
        return resolver;
    }

```

С использованием шаблонизатора были реализованы следующие Web-интерфейсы для работы с заказами:

- шаблон интерфейса для получения списка заказов;

    ```HTML

    <!DOCTYPE html>
    <html lang="en" xmlns:th="http://www.thymeleaf.org">
    <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body class="container mt-5">
    <h2 class="mb-4">Список заказов</h2>

    <table class="table table-striped table-bordered">
        <thead class="table-dark">
            <tr>
                <th>#</th>
                <th>Дата заказа</th>
                <th>Итоговая стоимость</th>
                <th>Статус</th>
                <th>Адрес</th>
                <th>Клиент</th>
                <th>Удалить?</th>
            </tr>
        </thead>
        <tbody>
            <tr th:each="order, iterStat : ${orders}">
                <td>
                    <a th:href="@{/view/v1/order/edit/{id}(id=${order.orderId})}" th:text="${order.orderId}">Номер заказа</a>
                </td>
                <td th:text="${order.orderDate}">Иван Иванов</td>
                <td th:text="${order.totalPrice}">ИВТ-101</td>
                <td th:text="${order.status}">1</td>
                <td th:text="${order.shippingAddress}">Иван Иванов</td>
                <td th:text="${order.customerName}">ИВТ-101</td>
                <td>
                    <a class="btn btn-danger" th:href="@{/view/v1/order/delete/{id}(id=${order.orderId})}" th:text="X">Удалить</a>
                </td>
            </tr>
        </tbody>
    </table>

    <a href="order/create" class="btn btn-primary">Создать заказ</a>
    

    </body>
    </html>

    ```

- шаблон интерфейса для создания заказа;
  
    ```HTML 
    
    <!DOCTYPE html>
    <html lang="en" xmlns:th="http://www.thymeleaf.org">
    <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body class="container mt-5">
    <h2>Создать новый заказ</h2>

    <form th:action="@{/view/v1/order/create}" th:object="${order}" method="post">
        <div class="mb-3">
            <label for="address" class="form-label">Адрес</label>
            <input type="text" th:field="*{address}" class="form-control" id="address" placeholder="Введите адрес">
        </div>


        <div class="mb-3">
            <label for="customerId" class="form-label">Покупатель</label>
            <select th:field="*{customerId}" class="form-select" id="customerId">
                <option th:each="c : ${customers}" th:value="${c.customerId}" th:text="${c.name}">Покупатель</option>
            </select>
        </div>

        <button type="submit" class="btn btn-primary">Сохранить</button>
    </form>
    </body>
    </html>

    ```

- шаблон интерфейса для изменения заказа;
  
    ```HTML

    <!DOCTYPE html>
    <html lang="en" xmlns:th="http://www.thymeleaf.org">
    <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body class="container mt-5">
    <h2>Изменить заказ</h2>

    <form th:action="@{/view/v1/order/edit}" th:object="${order}" method="post">
        <div class="mb-3">
            <label for="orderId" class="form-label">Номер заказа</label>
            <input type="text" th:field="*{orderId}" class="form-control" id="orderId" placeholder="Номер заказа" readonly>
        </div>
        
        <div class="mb-3">
            <label for="address" class="form-label">Адрес</label>
            <input type="text" th:field="*{address}" class="form-control" id="address" placeholder="Введите адрес">
        </div>


        <div class="mb-3">
            <label for="customerId" class="form-label">Покупатель</label>
            <select th:field="*{customerId}" class="form-select" id="customerId">
                <option th:each="c : ${customers}" th:value="${c.customerId}" th:text="${c.name}">Покупатель</option>
            </select>
        </div>

        <button type="submit" class="btn btn-primary">Сохранить</button>
    </form>
    </body>
    </html>

    ```

Для тестирования работоспособности приложения был выполнен деплой приложения на сервер Apache Tomcat 11.

## Выводы

В ходе выполнения лабораторной работы я разработал интерфейс для приложения с использованием технологии Spring MVC и шаблонизатора Thymeleaf.