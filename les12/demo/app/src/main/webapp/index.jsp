<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Добро пожаловать</title>
    <style>
        body {
            font-family: "Segoe UI", sans-serif;
            background: linear-gradient(to right, #6dd5ed, #2193b0);
            margin: 0;
            padding: 0;
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
            color: white;
        }

        .welcome-box {
            background: rgba(255, 255, 255, 0.1);
            padding: 40px 60px;
            border-radius: 20px;
            text-align: center;
            box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
        }

        h1 {
            font-size: 36px;
            margin-bottom: 20px;
        }

        p {
            font-size: 18px;
        }

        a {
            color: #ffffff;
            background-color: #2193b0;
            padding: 10px 20px;
            text-decoration: none;
            border-radius: 10px;
            transition: background 0.3s ease;
        }

        a:hover {
            background-color: #1b7a98;
        }
    </style>
</head>
<body>
<div class="welcome-box">
    <h1>Добро пожаловать в Spring MVC!</h1>
    <p>Это стартовая страница вашего Java веб-приложения.</p>
    <br/>
    <a href="jsp/hello">Перейти к Hello-странице JSP</a>
    <a href="jsp/students">Перейти к списку студентов JSP</a>
    <a href="thymleaf/hello">Перейти к Hello-странице Thymleaf</a>
    <a href="thymleaf/students">Перейти к списку студентов Thymleaf</a>
</div>
</body>
</html>