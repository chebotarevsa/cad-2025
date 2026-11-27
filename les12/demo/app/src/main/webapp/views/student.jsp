<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>Список пользователей</title>
</head>
<body>
    <h1>Список пользователей</h1>
    <table border="1">
        <thead>
            <tr>
                <th>Имя</th>
                <th>Возраст</th>
                <th>Электронная почта</th>
            </tr>
        </thead>
        <tbody>
            <!-- Перебираем список студентов -->
            <c:forEach var="student" items="${students}">
                <tr>
                    <td>${student.id}</td>
                    <td>${student.name}</td>
                    <td>${student.group}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>