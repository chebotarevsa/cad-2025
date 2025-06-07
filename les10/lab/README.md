# Отчет о лаботаротоной работе

## Цель работы
Создать веб интерфейс для работы с приложением
## Выполнение работы

Запускаем томкет стартап.бат
gradle build
gradle war
http://localhost:8080/pet-shop/order/list
окно создания запроса
При вводе запроса гет в постман
http://localhost:8080/pet-shop/product/list
Получаем
[
    {
        "product": "Сухой корм для собак",
        "category": "Корма",
        "amount": 50
    },
    {
        "product": "Игрушка для кошек \"Мышка\"",
        "category": "Игрушки",
        "amount": 200
    },
    {
        "product": "Лакомство для попугаев",
        "category": "Лакомства",
        "amount": 100
    },
    {
        "product": "Когтеточка для кошек",
        "category": "Аксессуары",
        "amount": 30
    },
    {
        "product": "Гель для чистки ушей собак",
        "category": "Средства ухода",
        "amount": 40
    },
    {
        "product": "Аквариум 50 литров",
        "category": "Аквариумистика",
        "amount": 10
    },
    {
        "product": "Наполнитель для кошачьего туалета",
        "category": "Наполнители",
        "amount": 60
    },
    {
        "product": "Шампунь для собак с алоэ",
        "category": "Средства ухода",
        "amount": 35
    },
    {
        "product": "Клетка для хомяков",
        "category": "Клетки",
        "amount": 20
    },
    {
        "product": "Поводок для собак 3м",
        "category": "Амуниция",
        "amount": 25
    }
]


Контрольные вопросы:
Сервлеты и веб-приложения
1. Что такое Servlet и зачем он нужен?

Servlet — это Java-класс, обрабатывающий HTTP-запросы и формирующий HTTP-ответы. Он работает внутри сервлет-контейнера (например, Tomcat) и позволяет создавать веб-приложения на Java.
2. Что делает web.xml и зачем он нужен в веб-приложении?

web.xml — это файл конфигурации веб-приложения (deployment descriptor), где можно:

    Регистрировать сервлеты и маппинги (<servlet>, <servlet-mapping>)

    Определять фильтры, слушатели, параметры

    Настраивать безопасность и загрузку компонентов

Он необходим для конфигурации до появления аннотаций (@WebServlet, @WebFilter и т.д.).
3. Что такое WAR-файл и чем он отличается от JAR?

    WAR (Web ARchive) — архив, содержащий веб-приложение (HTML, JSP, классы, web.xml, библиотеки и т.д.). Разворачивается на сервлет-контейнере.

    JAR (Java ARchive) — обычная библиотека Java-классов, не содержит веб-структуры.

Различие: WAR — для веб-приложений, JAR — для обычных Java-программ и библиотек.
4. Что такое ServletContext и как его использовать?

ServletContext — объект, представляющий среду выполнения приложения. Позволяет:

    Хранить глобальные атрибуты (setAttribute, getAttribute)

    Получать параметры из web.xml

    Получать путь к ресурсам (getResource, getRealPath)

    Получать версии сервлета и сервера

Пример:

ServletContext context = getServletContext();
context.setAttribute("appName", "ZooShop");

5. Чем отличается HttpServletRequest от HttpServletResponse?

    HttpServletRequest — объект, содержащий информацию о запросе (параметры, заголовки, URI, сессия и т.д.)

    HttpServletResponse — объект, позволяющий отправить ответ клиенту (записать HTML/JSON, установить заголовки, статус и т.д.)

6. Какой интерфейс нужно реализовать, чтобы создать Listener, реагирующий на запуск приложения?

ServletContextListener

Пример:

@WebListener
public class AppStartupListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent sce) {
        // Код при запуске
    }
    public void contextDestroyed(ServletContextEvent sce) {
        // Код при остановке
    }
}

7. Как получить доступ к Spring ApplicationContext внутри обычного сервлета?

WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
MyService myService = context.getBean(MyService.class);

Для этого ContextLoaderListener должен быть зарегистрирован и ApplicationContext загружен.
8. Что делает ContextLoaderListener в Spring-приложении?

ContextLoaderListener загружает корневой Spring ApplicationContext при старте приложения и связывает его с ServletContext, чтобы бины были доступны в сервлетах, фильтрах и слушателях.
9. Зачем нужно использовать @WebServlet и чем он лучше/хуже конфигурации в web.xml?

@WebServlet — аннотация для регистрации сервлета без web.xml.

Пример:

@WebServlet("/hello")
public class HelloServlet extends HttpServlet { ... }

Плюсы @WebServlet:

    Меньше кода, проще конфигурация

    Нет необходимости в web.xml

Минусы:

    Менее гибкая конфигурация (например, фильтры безопасности удобнее через web.xml)

    Не подходит для очень старых сервлет-контейнеров

10. Как можно использовать один Spring Bean в нескольких сервлетах?

    Получить ApplicationContext через WebApplicationContextUtils в каждом сервлете:

WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
MyService service = ctx.getBean(MyService.class);

    Все сервлеты будут использовать один и тот же бин, управляемый Spring (singleton по умолчанию).