# Отчет о лаботаротоной работе

## Цель работы
Замена сервлетов на Spring MVC
## Выполнение работы
как и в 10 запуск сервера и билд+вар
переходим на страницу создания запроса
http://localhost:8080/petshop/view/order
вводим
http://localhost:8080/petshop/api/order
в постмане
[
    {
        "orderId": 1,
        "orderDate": 1749243600000,
        "totalPrice": 0.00,
        "status": "new",
        "shippingAddress": "МОСКВАААААААААА",
        "items": [],
        "customer": {
            "customerId": 5,
            "name": "Дмитрий Соколов",
            "email": "d.sokolov@example.com",
            "phone": "+79881112244",
            "address": "Новосибирск, ул. Гоголя, д. 12"
        },
        "customerName": "Дмитрий Соколов"
    }
]


Контрольные вопросы:
MVC и Spring MVC
1. Что означает аббревиатура MVC и каковы её основные компоненты?

MVC — это архитектурный шаблон, разделяющий приложение на три части:

    Model — данные и бизнес-логика.

    View — представление (HTML, шаблоны).

    Controller — обработка запросов и связь между Model и View.

2. Какую роль выполняет DispatcherServlet в Spring MVC?

DispatcherServlet — центральный компонент Spring MVC.
Он:

    Перехватывает все HTTP-запросы.

    Определяет, какой контроллер должен обработать запрос.

    Вызывает нужный метод контроллера.

    Передаёт результат в ViewResolver для отображения.

3. Какая аннотация используется для указания, что класс является контроллером?

@Controller

4. Чем отличаются аннотации @Controller и @RestController?

    @Controller — используется для обычных веб-страниц (возвращает View).

    @RestController = @Controller + @ResponseBody, используется для REST API, возвращает данные (JSON, XML) напрямую.

Пример:

@RestController
public class ApiController {
    @GetMapping("/user")
    public User getUser() {
        return new User("Tom");
    }
}

5. Какой аннотацией можно связать параметр метода с переменной из URL (например, /users/{id})?

@GetMapping("/users/{id}")
public String getUser(@PathVariable("id") Long id) {
    ...
}

Аннотация: @PathVariable
6. Что такое Model в Spring MVC и как она используется?

Model — интерфейс, с помощью которого передаются данные из контроллера в представление (View).

Пример:

@GetMapping("/hello")
public String hello(Model model) {
    model.addAttribute("name", "John");
    return "greeting"; // передаёт в greeting.html
}

7. Что делает аннотация @RequestMapping?

Связывает HTTP-запросы с методами контроллера.

Пример:

@RequestMapping(value = "/hello", method = RequestMethod.GET)

Также есть более короткие аннотации:

    @GetMapping

    @PostMapping

    @PutMapping

    @DeleteMapping

8. Какие HTTP-методы можно обрабатывать в Spring MVC и какими аннотациями?
HTTP-метод	Аннотация
GET	@GetMapping
POST	@PostMapping
PUT	@PutMapping
DELETE	@DeleteMapping
PATCH	@PatchMapping
Любой	@RequestMapping с указанием method = ...
9. Что такое ViewResolver и зачем он нужен в Spring MVC?

ViewResolver — компонент, который преобразует имя представления (например, "home") в реальный шаблон (например, home.html или home.jsp).

Пример конфигурации:

@Bean
public InternalResourceViewResolver viewResolver() {
    var resolver = new InternalResourceViewResolver();
    resolver.setPrefix("/WEB-INF/views/");
    resolver.setSuffix(".jsp");
    return resolver;
}

10. Как вернуть JSON из контроллера без использования шаблонов?

Использовать аннотацию @RestController или @ResponseBody:

@RestController
public class ApiController {
    @GetMapping("/api/user")
    public User getUser() {
        return new User("Alice");
    }
}

Или:

@Controller
public class ApiController {
    @GetMapping("/api/user")
    @ResponseBody
    public User getUser() {
        return new User("Alice");
    }
}

Spring автоматически сериализует объект в JSON с помощью Jackson.