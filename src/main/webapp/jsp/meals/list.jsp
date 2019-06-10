<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="dateTimeFormatter" scope="request" type="java.time.format.DateTimeFormatter"/>
<html>
<head>
    <title>Meals</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/open-iconic/1.1.1/font/css/open-iconic-bootstrap.min.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<main>
    <div class='container'>
        <h1>Meals</h1>
        <div class="mb-2">
            <a href="meals?action=create" class="btn btn-success">Create</a>
        </div>
        <table class='table table-bordered'>
            <tr>
                <th>Id</th>
                <th>Date</th>
                <th>Description</th>
                <th>Calories</th>
                <th></th>
                <th></th>
                <th></th>
            </tr>
            <c:forEach items="${meals}" var="meal">

                <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
                <tr <c:if test="${meal.excess}">class="bg-red"</c:if>>
                    <td>${meal.id}</td>
                    <td>${meal.dateTime.format(dateTimeFormatter)}</td>
                    <td>${meal.description}</td>
                    <td>${meal.calories}</td>
                    <td><a href="meals?id=${meal.id}&action=view"><span class="oi oi-eye"></span></a></td>
                    <td><a href="meals?id=${meal.id}&action=update"><span class="oi oi-pencil"></span></a></td>
                    <td><a href="meals?id=${meal.id}&action=delete"><span class="oi oi-trash"></span></a></td>
                </tr>
            </c:forEach>
        </table>
    </div>
</main>
</body>
</html>