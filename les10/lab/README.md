# Отчет о лаботаротоной работе № 5 Разработка и развертывание Web-приложений

## Цель работы
Добавить Web-приложения в разрабатываемый проект

## Выполнение работы

В первую очередь, для выполнения лабораторной работы был установлен и настроен Apache Tomcat 11

Проект был перенастроен для того чтобы в результате сборки формировался WAR-файл.

```java

tasks.war {
    archiveFileName.set("petshop.war")
}

```

Был реализован Java-сервлет формирующий Web-страницу с информацией о заказах.
На страницу была добавлена кнопка для перехода на форму создания заказа.

```java

@WebServlet("/order/list")
public class OrderListServlet extends HttpServlet{

    private OrderService orderService;

     public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        this.orderService = context.getBean(OrderService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
        throws ServletException, IOException {
            var orders = orderService.getOrders();
            PrintWriter out = resp.getWriter();

        out.println("<html><body>");
        out.println("<h1>Orders: </h1>");
        for (Order order : orders) {
            out.println("<h2>" + order.toString() + "</h2>");
        }
        out.println("<a href='create'><button>Create</button></a>");
        out.println("</body></html>");
    }
    
}

```

После этого в приложении был создан Java-сервлет формирующий Web-страницу с формой для создания заказа.
После создания заказа открывается список заказов.

```java

@WebServlet("/order/create")
public class OrderCreateServlet extends HttpServlet{

    private OrderService orderService;

     public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        this.orderService = context.getBean(OrderService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
        throws ServletException, IOException {
            var orders = orderService.getOrders();
            PrintWriter out = resp.getWriter();

        out.println("<html><body>");
        out.println("<h1>Create order</h1>");
        out.println("<form action='create' method='post'><label for='customerID'>Enter customer ID:</label><input type='text' name='customerID' id='customerID' required/><br/>" + 
        "<label for='productID'>Enter product ID:</label> <input type='text' name='productID' id='productID' required /><br/>" + 
        "<label for='quantity'>Enter product quantity:</label> <input type='text' name='quantity' id='quantity' required /><br/>" + 
        "<button type='submit'>Submit</button></form>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        int customerID = Integer.parseInt(req.getParameter("customerID"));
        int productID = Integer.parseInt(req.getParameter("productID"));
        int quantity = Integer.parseInt(req.getParameter("quantity"));
        orderService.createOrder(customerID, productID, quantity);
        resp.sendRedirect("list");
    }
    
    
}

```

Также был Реализован Java-сервлет представляющий REST сервис для получения информации о продуктах.
Для каждого продукта выводится следующая информация: название продукта, название категории, количество на складе.

```java

@WebServlet("/product/list")
public class ProductListServlet extends HttpServlet{

    private ProductService productService;

     public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        this.productService = context.getBean(ProductService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
        throws ServletException, IOException {
            List<ProductDTO> listDTOs = productService.getProducts().stream().map(x -> new ProductDTO(x.getName(), x.getCategory().getName(), x.getStockQuantity())).collect(Collectors.toList());
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(listDTOs);
            PrintWriter out = resp.getWriter();
            out.println(json);
    }
    
}

```

Дополнительно для вывода информации о каждом продукте, был разработан класс ```ProductDTO.java```

``` java

public class ProductDTO {

    @JsonProperty("product")
    private String productName;
    @JsonProperty("category")
    private String categoryName;
    @JsonProperty("quantity")
    private int stockQuantity;

    public ProductDTO(String productName, String categoryName, int stockQuantity) {
        this.productName = productName;
        this.categoryName = categoryName;
        this.stockQuantity = stockQuantity;
    }

    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public int getStockQuantity() {
        return stockQuantity;
    }
    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
    
}

```

В конце работы был выполнен деплой приложения на сервер Apache Tomcat 11 и протестирована работу REST сервиса используя Postman.

## Выводы
В ходе выполнения лабораторной работы был установлен и настроен Apache Tomcat и добавлены Web-приложения в разрабатываемый проект.