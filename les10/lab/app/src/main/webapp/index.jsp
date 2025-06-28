<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Pet Store</title>
</head>
<body>
<h1>Добро пожаловать в магазин зоотоваров!</h1>
<ul>
    <li><a href="${pageContext.request.contextPath}/orders">Список заказов</a></li>
    <li><a href="${pageContext.request.contextPath}/create-order">Создать заказ</a></li>
    <li><a href="${pageContext.request.contextPath}/api/products">REST API продуктов</a></li>
</ul>
</body>
</html>