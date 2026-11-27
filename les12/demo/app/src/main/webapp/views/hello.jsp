<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head><title>Hello Page</title></head>
<body>
    <h2>Привет, ${name}!</h2>

    <% out.println("Привет из скриплета!"); %>

    <c:forEach var="i" begin="1" end="5">
        <p>Номер: ${i}</p>
    </c:forEach>

    <fmt:message key="welcomeMessage" />
    
</body>
</html>