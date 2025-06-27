# Отчет о лаботаротоной работе № 8 Основы тестирования

## Цель работы
Ознакомится с написанием Unit и интеграционных тестов

## Выполнение работы

Для настройки поддержки в проекте написания и выполненения Unit-тестов, а также для создания отчетов покрытия тестов были добавлена библиотека
org.junit.jupiter:junit-jupiter и плагин jacoco.

```java

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
    reports {
        xml.required = false
        csv.required = false
        html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
    }
}

```

Был написан Unit-тест для сервиса создания заказа и создан отчет о покрытии

```java

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    
    @Mock
    private OrderRepository orderRepository;
    
    @Mock
    private CustomerRepository customerRepository;
    
    @Mock
    private ProductRepository productRepository;
    
    @InjectMocks
    private OrderService orderService;

    @Test
    void getOrders_ShouldReturnOrdersList(){
        Customer customer = new Customer(1, "John", "john@gmail.com", "41-14-63", "Belgorod");
        Order order = new Order(new Date(2024, 11,8), new BigDecimal(1200), "new order", "Belgorod",
            customer);
        when(orderRepository.findAll()).thenReturn(List.of(order));
        var orders = orderService.getOrders();
        assertThat(orders.size()).isEqualTo(1);
        assertThat(orders.get(0).getCustomer().getName()).isEqualTo("John");

        verify(orderRepository).findAll();
    }
}

```

Был написан интеграционные тесты для тестирования взаимодействия сервиса создания заказа со слоем репозиториев.
В тестах были рассмортены как удачное так и неудачное взаимодействие слоев.

```java

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfigDB.class)
@Transactional
@TestInstance(Lifecycle.PER_CLASS)
public class OrderServiceIntegrationTest {
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setup(){
        Customer customer = new Customer();
        customer.setName("John");
        customerRepository.save(customer);
    }
    
    @Test
    void createOrder_ShouldCreateNewOrder(){
        orderService.createOrder(1, "Belgorod", new ArrayList<>());
        var result = orderRepository.findAll();

        int count = 0;
        Order newOrder = null;
        
        for (Order order : result) {
            count++;
            newOrder = order;
        }

        assertEquals(1, count);
        assertEquals("John", newOrder.getCustomer().getName());
        assertEquals("Belgorod", newOrder.getShippingAddress());
    }

    @Test
    void createOrder_CustomerShouldNotFind(){
        try{
            orderService.createOrder(2, "Belgorod", new ArrayList<>());
            fail("Error, exception not caused");
        }
        catch (Exception exception){
            assertEquals("java.util.NoSuchElementException", exception.getClass().getName());
        }
    }
}

 ```

## Выводы
В ходе выполнения лабораторной работы я ознакомился с написанием Unit и интеграционных тестов.