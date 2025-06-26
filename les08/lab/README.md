# Отчет о лаботаротоной работе №4 Технологии работы с базами данных. JPA. Spring Data

## Цель работы
Провести рефакторинг приложения. Расширить приложение новыми сущностями и привести структуру приложения в соответствие со "слоистой архитектурой".

## Выполнение работы

Результаты выполнения лабораторной работы №3 были скопированы в директорию /les08/lab/.

После этого был создан DataSource соответствующий следующим требованиям:

1. Должна использоваться база данных H2
2. Для реализации DataSource необходимо использовать библиотеку HikariCP, а именно HikariDataSource
3. Для работы с базой данных должна использоваться библиотека HIbernate, использующая технологию ORM
4. Схема схема данных должна создаваться автоматически на основании JPA сущностей.

```java

@Configuration
@ComponentScan(basePackages = "ru.bsuedu.cad.lab")
@PropertySource("classpath:db/jdbc.properties")
@EnableJpaRepositories(basePackages = "ru.bsuedu.cad.lab.repository")
@EnableTransactionManagement
public class Config {
    private static Logger LOGGER = LoggerFactory.getLogger(Config.class);

    @Value("${jdbc.driverClassName}")
    private String driverClassName;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;
    
    @Bean
    public DataSource dataSource(){
         LOGGER.info("Конфигурация базы данных");
        try {
            var hc = new HikariConfig();
            hc.setJdbcUrl(url);
            hc.setDriverClassName(driverClassName);
            hc.setUsername(username);
            hc.setPassword(password);
            var dataSource= new HikariDataSource(hc);
            dataSource.setMaximumPoolSize(25); // 25 is a good enough data pool size, it is a database in a container after all
            return dataSource;
        } catch (Exception e) {
            LOGGER.error("Hikari DataSource bean cannot be created!", e);
            return null;
        }
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = 
            new LocalContainerEntityManagerFactoryBean();
            em.setDataSource(dataSource());
        
        em.setPackagesToScan("ru.bsuedu.cad.lab.entity");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setDatabasePlatform("org.hibernate.dialect.H2Dialect");
        em.setJpaVendorAdapter(vendorAdapter);

        // Дополнительные свойства JPA/Hibernate
        Properties properties = new Properties();
        properties.put(Environment.HBM2DDL_AUTO, "create-drop");
        properties.put(Environment.DIALECT, "org.hibernate.dialect.H2Dialect");
        properties.put(Environment.FORMAT_SQL, true);
        properties.put(Environment.USE_SQL_COMMENTS, false);
        properties.put(Environment.SHOW_SQL, true);
        properties.put(Environment.MAX_FETCH_DEPTH, 3);
        properties.put(Environment.STATEMENT_BATCH_SIZE, 10);
        properties.put(Environment.STATEMENT_FETCH_SIZE, 50);
        em.setJpaProperties(properties);

        return em;
    }
    @Bean
    public PlatformTransactionManager transactionManager(
        @Autowired EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
   
}

 ```

Далее структура пакетов проекта была переработана и имеет следующий вид:

- ru.bsuedu.cad.lab - основной пакет
- ru.bsuedu.cad.lab.entity - JPA сущности
- ru.bsuedu.cad.lab.repository - репозитории
- ru.bsuedu.cad.lab.service - сервисы
- ru.bsuedu.cad.lab.app - приложение

Согласно схеме данных былы созданы следующие сущности

1. Сущность "Customers"

    ```java
    
    @Entity
    @Table(name = "customers")
    public class Customer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private int customerId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    public Customer(){

    }

    public Customer(int customerId, String name, String email, String phone, String address) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    }

    ```

2. Сущность "Products"

    ```java

    @Entity
    @Table(name = "products")
    public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int productID;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    @Column(name = "image_url")
    private String imageURL;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToOne()
    @JoinColumn(name = "category_id")
    private Category category;

    public Product(){
        
    }

    public Product(int productID, String name, String description, Category category, BigDecimal price, int stock_quantity,
            String imageURL, Date createdAt, Date updatedAt) {
        this.productID = productID;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.stockQuantity = stock_quantity;
        this.imageURL = imageURL;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }


    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stock_quantity) {
        this.stockQuantity = stock_quantity;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString(){
        return name;
    }


    }

    ```

3. Сущность "Categories"

    ```java

    @Entity
    @Table(name = "categories")
    public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private int categoryID;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    public Category(){
        
    }
    
    public Category(int categoryID, String name, String description) {
        this.categoryID = categoryID;
        this.name = name;
        this.description = description;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    }

    ```

4. Сущность "Orders"

    ```java

    @Entity
    @Table(name = "orders")
    public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private int orderId;

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "status")
    private String status;

    @Column(name = "shipping_address")
    private String shippingAddress;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "order")
    private List<OrderDetail>  orderDetails = new ArrayList<>();

    

    public Order(){

    }
    
    public Order(Date orderDate, BigDecimal totalPrice, String status, String shippingAddress,
            Customer customer) {
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.status = status;
        this.shippingAddress = shippingAddress;
        this.customer = customer;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomerId(Customer customer) {
        this.customer = customer;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }
    }

    ```

5. Сущность "Order Detail"

    ```java

    @Entity
    @Table(name = "order_details")
    public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private int orderDetailId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price")
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public OrderDetail(){

    }

    public OrderDetail(int quantity, BigDecimal price, Order order, Product product) {
        this.quantity = quantity;
        this.price = price;
        this.order = order;
        this.product = product;
    }

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    }

    ```

В пакете ru.bsuedu.cad.lab.repository были реализованы репозитории для каждой сущности.
Репозитории содержат методы по созданию, получению записи по идентификатору и получение всех записей для каждой сущности.

В пакете ru.bsuedu.cad.lab.service был создан сервис для создания заказа и получения списка всех заказов.

``` java

@Service
public class OrderService {
    final private OrderRepository orderRepository;
    final private CustomerRepository customerRepository;
    final private ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    public List<Order> getOrders(){
        List<Order> orders = new ArrayList<>();
        for (Order order : orderRepository.findAll()) {
            orders.add(order);
        }
        return orders;
    }

    public List<Order> getOrdersByGuava(){
       return Lists.newArrayList(orderRepository.findAll());
    }

    @Transient
    public Order createOrder(int customerId, int productId, int quantity, BigDecimal price){
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        Product product = productRepository.findById(productId).orElseThrow();
        Date orderDate = new Date(2024,03,22);
        BigDecimal totalPrice = price.multiply(new BigDecimal (quantity));
        Order newOrder = new Order(orderDate, totalPrice, "новый заказ", "Белгород", customer);
        OrderDetail orderItem = new OrderDetail(quantity, totalPrice, newOrder, product);
        List<OrderDetail> items = new ArrayList<>();
        items.add(orderItem);
        newOrder.setOrderDetails(items);
        return orderRepository.save(newOrder);
    }
    
}

``` 

В пакете ru.bsuedu.cad.lab.app реализован клиент для сервиса создания заказа, который создает новый заказ.
Создание заказа должно выполняется в рамках транзакции, а информация о создании заказа была выведена в лог.

```java

@Component
public class OrderClient {
    final private OrderService orderService;

    public OrderClient(OrderService orderService) {
        this.orderService = orderService;
    }
    
    public void createOrder(){
        orderService.createOrder(7, 9, 3, new BigDecimal(1200));
    }
}

```

## Выводы
В ходе выполнения лабораторной работы были выполнены следующие задачи:
   - проведен рефакторинг приложения.
   - приложение расширено новыми сущностями
   - структуру приложения была приведена в соответствие со "слоистой архитектурой".