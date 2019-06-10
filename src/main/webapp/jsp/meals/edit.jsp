<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
<jsp:useBean id="dateTimeFormatter" scope="request" type="java.time.format.DateTimeFormatter"/>
<head>
    <title>Meal</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
</head>
<body>
<main>
    <div class='container'>
        <c:choose>
            <c:when test="${meal.id == 0}">
                <h1>Add Meal</h1>
            </c:when>
            <c:otherwise>
                <h1>Edit - ${meal.id}</h1>
            </c:otherwise>
        </c:choose>

        <form method="post">
            <input type="hidden" name="uuid" value="${meal.id}">

            <div class="form-group">
                <label for="date">Date</label>
                <input name="date" type="datetime-local" class="form-control" id="date" value="${meal.dateTime}">
            </div>

            <div class="form-group">
                <label for="description">Description</label>
                <textarea name="description" id="description" class="form-control" rows="5">${meal.description}</textarea>
            </div>

            <div class="form-group">
                <label for="calories">Calories</label>
                <input name="calories" type="number" class="form-control" id="calories" value="${meal.calories}">
            </div>

            <button type="submit" class="btn btn-primary">Submit</button>
            <a href="/topjava/meals" class="btn btn-success">Back</a>
        </form>
    </div>
</main>
</body>
</html>