# Spring MVC.Технологии представления. Thymeleaf

## "Модели" в MVC

### Объект Model, ModelMap, ModelAndView

Model, ModelMap и ModelAndView — это интерфейсы и классы, которые используются для передачи данных между контроллером и представлением в Spring MVC.

#### Model

+ Интерфейс для добавления атрибутов в модель, которые будут доступны в представлении.
+ Используется для передачи данных в шаблон или на страницу.
Пример использования:

``` java
@GetMapping("/greet")
public String greet(Model model) {
    model.addAttribute("message", "Hello, Spring MVC!");
    return "greeting"; // имя представления
}
```

#### ModelMap

+ Расширяет интерфейс Model, но с дополнительной функциональностью.
+ Работает по принципу Map (ключ-значение), где ключом является имя атрибута, а значением — данные, которые передаются в представление.
+ ModelMap используется для упрощения инициализации атрибутов с помощью цепочки вызовов.
Пример использования:

```java
@GetMapping("/student")
public String getStudent(ModelMap model) {
    model.put("student", new Student("John", "Doe"));
    return "studentDetails";
}
```

#### ModelAndView

+ Сложный объект, который содержит как данные модели, так и имя представления.
+ Используется для явного указания имени представления и атрибутов, которые будут переданы.
Пример использования:

``` java
@GetMapping("/user")
public ModelAndView getUser() {
    ModelAndView modelAndView = new ModelAndView("userDetails");
    modelAndView.addObject("user", new User("Alice", "Smith"));
    return modelAndView;
}
```

### Передача данных между контроллером и представлением

В Spring MVC данные передаются между контроллером и представлением через модель.

Шаги передачи данных:

1.	Добавление данных в модель:
В контроллере добавляем данные в модель с помощью метода addAttribute (для Model и ModelMap).
Пример:

``` java
@GetMapping("/greet")
public String greet(Model model) {
    model.addAttribute("message", "Hello, Spring MVC!");
    return "greeting"; // имя представления
}
```

2. Доступ к данным в представлении:
В представлении (например, JSP или Thymeleaf) можно получить доступ к данным с помощью выражений, например:

``` jsp
<h1>${message}</h1>
```

### Flash-атрибуты и RedirectAttributes

Flash-атрибуты позволяют передавать данные между запросами, особенно при использовании перенаправления (redirect).

Что такое Flash-атрибуты?

Flash-атрибуты — это временные атрибуты, которые сохраняются между запросами и автоматически удаляются после первого использования. Они часто применяются при перенаправлениях, чтобы сохранить данные, которые должны быть отображены в следующем запросе (например, сообщения о успешной операции).

Как использовать RedirectAttributes:

RedirectAttributes позволяет добавлять атрибуты, которые будут переданы на страницу после перенаправления (например, после успешной отправки формы).

Пример использования:

```java 
@PostMapping("/submit")
public String submitForm(@ModelAttribute("student") Student student, RedirectAttributes redirectAttributes) {
    // Сохраняем flash-атрибут
    redirectAttributes.addFlashAttribute("message", "Student successfully created!");

    // Перенаправляем на страницу с подтверждением
    return "redirect:/confirmation";
}
```

В контроллере для страницы подтверждения:

```java
@GetMapping("/confirmation")
public String confirmationPage(@ModelAttribute("message") String message, Model model) {
    model.addAttribute("confirmationMessage", message);
    return "confirmation";
}
```

## “Представление” в MVC

В Spring MVC представление (view) — это слой отображения данных пользователю.
Контроллер обрабатывает запрос, передаёт данные в модель, а представление визуализирует эти данные (чаще всего через HTML).

Spring MVC поддерживает множество шаблонизаторов, например:

+ JSP (с JSTL) — классика для Java EE
+ Thymeleaf (подключается вручную)
+ Freemarker, Velocity (устаревшие)
+ Можно реализовать свой ViewResolver для PDF, Excel и т.д.

В классическом Spring MVC JSP — это самый распространённый выбор.

### JSP

JSP (JavaServer Pages) — это серверная технология Java для создания динамических HTML-страниц, встраивая Java-код прямо в HTML.
Это своего рода шаблонный язык, который компилируется в сервлеты и обрабатывается на стороне сервера.

+ 1. Когда клиент делает HTTP-запрос к .jsp странице, контейнер (например, Tomcat):
+ 2. Компилирует JSP в Java-класс (сервлет) (один раз при первом вызове)
+ 3. Выполняет сервлет, формируя HTML-ответ
+ 4. Отправляет результат клиенту

Обычно страницы хранятся в папке:
/WEB-INF/views/

``` text
src/
└── main/
    └── webapp/
        ├── WEB-INF/
        │   └── views/
        │       ├── home.jsp
        │       └── hello.jsp
        └── index.jsp
```

Чтобы Spring знал, где искать представления (например, .jsp), нужно настроить ViewResolver.

```java
@Bean
public InternalResourceViewResolver viewResolver() {
    InternalResourceViewResolver resolver = new InternalResourceViewResolver();
    resolver.setPrefix("/WEB-INF/views/");
    resolver.setSuffix(".jsp");
    return resolver;
}
```

Теперь, если контроллер возвращает home, Spring отобразит файл:
/WEB-INF/views/hello.jsp

index.jsp — это входная точка веб-приложения по умолчанию (при обращении к /), но он должен лежать в webapp/, а не в WEB-INF, иначе контейнер его не увидит напрямую.

+ При переходе на корень сайта: http://localhost:8080/
+ Если в web.xml не указано иное, то index.jsp будет вызван автоматически
+ Но если использовать DispatcherServlet с маппингом /, то он перехватывает и корень, и index.jsp никогда не будет вызван напрямую

#### Скриплеты (устарело, но знать стоит)

Скриплеты (или JSP scriptlets) — это куски Java-кода, которые можно вставить прямо в тело HTML-страницы в JSP (Java Server Pages). Скриплеты были популярны в старых версиях JSP, но с развитием технологий, таких как Expression Language (EL) и JSTL, их использование значительно сократилось. Тем не менее, важно понимать, что они представляют собой часть истории JSP, и иногда их всё ещё можно встретить в старых проектах.

``` jsp 
<%
    String message = "Hello, JSP!";
    out.println(message); // Печатает строку на страницу
%>
```

Когда JSP-страница обрабатывается сервером, все скриплеты в ней выполняются на стороне сервера. Результаты выполнения кода (например, значение переменных, вывод через out.println()) вставляются в HTML-код страницы. Рендеринг страницы осуществляется до того, как она отправляется пользователю.

```jsp
<%
    int a = 10;
    int b = 20;
    int sum = a + b;
    out.println("Сумма: " + sum);
%>
```

```jsp
<table border="1">
    <%
        for (int i = 1; i <= 5; i++) {
            out.println("<tr><td>" + i + "</td></tr>");
        }
    %>
</table>
```

#### Expression Language (EL)

Expression Language (EL) — это язык выражений, встроенный в JSP (и не только), который позволяет легко обращаться к данным в Java-контексте прямо из HTML/JSP без Java-кода.

EL (Expression Language) — это простой синтаксис ${...} для доступа к:
+ атрибутам (из request, session, application, page)
+ свойствам JavaBeans
+ методам (в JSP 2.2+)
+ коллекциям, массивам, мапам
+ встроенным объектам

```jsp 
    <p>Имя: ${name}</p>                     <!-- name из Model -->
    <p>Сессия: ${sessionScope.user}</p>    <!-- атрибут из session -->
    <p>Первый элемент списка: ${list[0]}</p>
    <p>Значение в Map: ${map['key']}</p>
```

| Выражение               | Что делает                              |
|------------------------|------------------------------------------|
| `${user.name}`         | Получает свойство `name` у JavaBean     |
| `${list[0]}`           | Первый элемент списка                   |
| `${map['key']}`        | Значение по ключу в Map                 |
| `${param.id}`          | Значение параметра `id` из URL/query    |
| `${header['User-Agent']}` | Заголовок запроса                    |
| `${sessionScope.cart}` | Атрибут `cart` из сессии               |
| `${pageScope.name}`    | Атрибут `name` на уровне страницы      |
| `${applicationScope.appName}` | Атрибут `appName` из applicationScope |
| `${empty list}`        | Проверка на null или пустоту коллекции |
| `${not empty user}`    | Проверка на непустоту (если не null)    |
| `${name == 'John'}`    | Сравнение значений                     |
| `${age >= 18}`         | Сравнение чисел (больше или равно)     |
| `${a + b}`             | Операция сложения двух значений        |
| `${user.address.city}` | Доступ к вложенному свойству JavaBean  |
| `${list[0].name}`      | Доступ к свойству объекта в коллекции  |
| `${map['key'].value}`  | Доступ к свойству объекта в Map        |

#### STL

JSTL (JavaServer Pages Standard Tag Library) — это стандартная библиотека тегов для JSP, которая облегчает разработку динамических веб-приложений. Она включает набор стандартных тегов для выполнения типичных задач, таких как:

+ Управление потоками
+ Условные операторы и циклы
+ Форматирование чисел и дат
+ Работа с XML
+ Перевод сообщений

JSTL позволяет избежать использования скриплетов в JSP и поддерживает более чистую архитектуру, делая код легче для восприятия и поддержки.

Чтобы использовать JSTL нужно добавить зависимость

```kotlin
    dependencies {
        implementation("jakarta.servlet.jsp.jstl:jakarta.servlet.jsp.jstl-api:3.0.2") // API
        implementation("org.glassfish.web:jakarta.servlet.jsp.jstl:3.0.1") // реализация
    }
```

Для использования JSTL в JSP необходимо подключить нужную библиотеку тегов с помощью директивы <%@ taglib %>.

```jsp
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
```

1. c — это префикс для тегов из Core библиотеки JSTL (управление потоками, условные операторы, циклы и т.д.)
2. fmt — это префикс для тегов, связанных с форматированием (например, числа и даты)

##### Условные операторы

``` jsp 
<c:if test="${user.loggedIn}">
    <p>Добро пожаловать, ${user.name}!</p>
</c:if>

<c:choose>
    <c:when test="${age >= 18}">
        <p>Совершеннолетний</p>
    </c:when>
    <c:otherwise>
        <p>Несовершеннолетний</p>
    </c:otherwise>
</c:choose>
```

+ <c:if> — проверяет условие и выводит содержимое, если условие истинно.
+ <c:choose> — аналог конструкции switch или if-else.

##### Циклы

```jsp
<c:forEach var="i" begin="1" end="5">
    <p>Номер: ${i}</p>
</c:forEach>
```

```jsp 
    <!-- Перебираем список студентов -->
    <c:forEach var="student" items="${students}">
        <tr>
            <td>${student.id}</td>
            <td>${student.name}</td>
            <td>${student.group}</td>
        </tr>
    </c:forEach>
```

+ <c:forEach> — используется для перебора коллекции или диапазона значений.

##### Вывод данных с форматированием

``` jsp
<fmt:formatNumber value="${price}" type="currency" />
<fmt:formatDate value="${date}" pattern="dd.MM.yyyy" /
```

#### ✅ Достоинства и ❌ Недостатки JSP + JSTL

| Категория         | Преимущества (✅)                                                        | Недостатки (❌)                                                                 |
|-------------------|-------------------------------------------------------------------------|----------------------------------------------------------------------------------|
| ✅ Простота        | Легко начать, особенно для новичков                                      | Устаревший подход, не соответствует современным требованиям                      |
| ✅ Интеграция      | Хорошо интегрируется с сервлетами и Spring MVC                         | Неудобно для сложной логики, необходимость смешивать HTML и Java                |
| ✅ JSTL            | JSTL предоставляет циклы, условия, интернационализацию                 | JSTL не гибкая, часто приходится использовать скриплеты                         |
| ✅ Распространённость | Поддерживается всеми контейнерами (Tomcat, Jetty и др.)             | Зависимость от компиляции на сервере, сложнее отлаживать                        |
| ✅ Натуральный HTML | Шаблоны можно редактировать как обычные HTML-файлы                    | HTML-файлы невалидны до компиляции, т.к. содержат JSP-специфичный синтаксис     |
| ❌ Безопасность     | -                                                                       | Могут быть уязвимости при неправильной обработке данных                         |
| ❌ Производительность | -                                                                    | Комpиляция JSP на лету → медленный первый запуск                                |
| ❌ Архитектура       | -                                                                    | Поощряет смешение представления и логики ("God JSP Pages")                      |

### Thymeleaf

Thymeleaf — это серверный шаблонизатор для Java-приложений, предназначенный для генерации HTML, XML, текстовых и других представлений на стороне сервера.

Он используется в первую очередь в Spring MVC и Spring Boot для создания динамических HTML-страниц, где данные вставляются прямо в HTML-шаблоны.

Thymeleaf используется для:

+ Генерации динамических HTML-страниц
+ Разделения логики и представления (MVC)
+ Упрощения верстки с привязкой к Java-объектам
+ Создания форм, списков, условий, циклов прямо в HTML

#### История Thymeleaf

| Год     | Событие                                                             |
|---------|----------------------------------------------------------------------|
| ~2011   | Первый релиз Thymeleaf от **Daniel Fernández**                      |
| 2013    | Широкое распространение в Spring-сообществе                         |
| Сегодня | Является **де-факто стандартом** в Spring Boot для генерации HTML   |

### Подключение Thymeleaf

Добавьте в build.gradle.kts

```kotlin

    implementation("org.thymeleaf:thymeleaf:3.1.2.RELEASE")
    implementation("org.thymeleaf:thymeleaf-spring6:3.1.2.RELEASE") // для Spring 6 / Jakarta
```

Сконфигурируйте необходимые бины.

``` java
    @Bean
    public ClassLoaderTemplateResolver templateResolver() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/"); // папка в resources
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(false);
        return resolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver());
        return engine;
    }

    @Bean
    public ViewResolver viewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding("UTF-8");
        return resolver;
    }
```

#### Преимущества Thymeleaf

| Преимущество               | Описание                                                                 |
|----------------------------|--------------------------------------------------------------------------|
| Натуральные шаблоны     | HTML-файлы можно открывать прямо в браузере без запуска сервера           |
| Удобные циклы и условия | `th:each`, `th:if`, `th:switch` позволяют управлять логикой прямо в HTML |
| Интеграция с Spring     | Полная поддержка `Model`, `@ModelAttribute`, Spring MVC                  |
| Безопасность по умолчанию | HTML-escaping защищает от XSS                                           |
| Привязка форм           | Простая работа с формами через `th:object`, `th:field`                   |
| Расширяемость           | Возможность создавать собственные теги и диалекты                         |
| Тестируемость           | Можно рендерить шаблоны без сервера, удобно для тестов                   |

#### HTML-атрибуты

Thymeleaf использует HTML-атрибуты с префиксом th:, чтобы внедрять данные, условия, циклы и прочее. Всё выглядит как обычный HTML, что делает шаблоны “натуральными” и валидными.

| Атрибут         | Назначение                            | Пример                                           |
|-----------------|----------------------------------------|--------------------------------------------------|
| `th:text`       | Подставляет текст                     | `<p th:text="${message}">Привет</p>`             |
| `th:utext`      | Подставляет HTML без экранирования    | `<div th:utext="${htmlContent}"></div>`          |
| `th:each`       | Цикл по коллекции                     | `<li th:each="item : ${items}" th:text="${item}"/>` |
| `th:if` / `th:unless` | Условный вывод                 | `<p th:if="${user.loggedIn}">Добро пожаловать</p>` |
| `th:switch`, `th:case` | Множественный выбор (аналог switch) |                                                |
| `th:href`       | Ссылка с динамическим значением       | `<a th:href="@{/students/{id}(id=${s.id})}">`     |
| `th:src`        | Динамический путь к изображению       | `<img th:src="@{/images/logo.png}" />`           |
| `th:value`      | Значение input-поля                   | `<input type="text" th:value="${user.name}" />`  |
| `th:action`     | URL для формы                         | `<form th:action="@{/submit}" method="post">`    |
| `th:object`     | Привязка формы к объекту              | `<form th:object="${student}">`                  |
| `th:field`      | Автоматическая привязка поля формы    | `<input th:field="*{name}" />`                   |
| `th:fragment`   | Определение фрагмента (вставка блоков) | `<div th:fragment="footer">...</div>`           |
| `th:insert` / `th:replace` | Вставка другого шаблона/фрагмента | `<div th:replace="fragments/header :: head"/>`  |

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title th:text="${title}">Моя страница</title>
</head>
<body>
    <h1 th:text="'Привет, ' + ${user.name}">Привет!</h1>

    <ul>
        <li th:each="item : ${items}" th:text="${item}">Элемент</li>
    </ul>

    <p th:if="${user.admin}">Вы администратор</p>

    <form th:action="@{/submit}" th:object="${formData}" method="post">
        <input type="text" th:field="*{email}" />
        <button type="submit">Отправить</button>
    </form>
</body>
</html>
```

#### Работа с формами в Thymeleaf

##### Настройка контроллера

Контроллер должен:

+ отдавать форму с объектом
+ принимать POST-запрос и обрабатывать данные

```java
@GetMapping("/students/new")
public String showCreateForm(Model model) {
    model.addAttribute("student", new Student());
    return "student-form";
}

@PostMapping("/students")
public String saveStudent(@ModelAttribute Student student) {
    studentService.save(student);
    return "redirect:/students";
}
```

##### Шаблон формы (student-form.html)

``` html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Создание студента</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body class="p-4">

<h2>Создать студента</h2>

<form th:action="@{/students}" th:object="${student}" method="post" class="needs-validation">

    <div class="mb-3">
        <label for="name" class="form-label">Имя</label>
        <input type="text" id="name" th:field="*{name}" class="form-control"/>
    </div>

    <div class="mb-3">
        <label for="email" class="form-label">Email</label>
        <input type="email" id="email" th:field="*{email}" class="form-control"/>
    </div>

    <div class="mb-3">
        <label for="group" class="form-label">Группа</label>
        <select id="group" th:field="*{group}" class="form-select">
            <option th:each="g : ${groups}" th:value="${g}" th:text="${g}">Группа</option>
        </select>
    </div>

    <button type="submit" class="btn btn-primary">Сохранить</button>
</form>

</body>
</html>
```

##### Модель Java (Student.java)

``` java
public class Student {
    private String name;
    private String email;
    private String group;

    // геттеры и сеттеры
}
``` 

##### Основные теги формы в Thymeleaf

| Атрибут        | Назначение                                                                 |
|----------------|----------------------------------------------------------------------------|
| `th:object`    | Привязывает форму к объекту модели                                         |
| `th:field`     | Привязывает поле формы к свойству объекта. Автоматически задаёт name/id/value |
| `th:value`     | Устанавливает значение поля вручную                                        |
| `th:action`    | Задаёт URL для отправки формы                                              |
| `th:method`    | Определяет HTTP-метод (`GET`, `POST`, и т.д.)                             |
| `th:errors`    | Показывает сообщения об ошибках валидации для конкретного поля            |
| `th:classappend` | Условное добавление CSS-класса (например, для ошибки валидации)         |


## Обзор WebApplicationInitializer (альтернатива web.xml)

WebApplicationInitializer — это интерфейс из Spring, который позволяет программно конфигурировать веб-приложение вместо использования устаревшего web.xml.

Он был добавлен в Servlet 3.0 и используется Spring как точка входа в приложение при его старте на сервере (например, Tomcat).

``` java

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class MyWebAppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // Создаем Spring-контекст
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(AppConfig.class); // Класс с @Configuration

        // Создаем и регистрируем DispatcherServlet
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher",
                new DispatcherServlet(context));
        
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/"); // URL-маска
    }
}

```
