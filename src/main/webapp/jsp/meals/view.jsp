<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
<jsp:useBean id="dateTimeFormatter" scope="request" type="java.time.format.DateTimeFormatter"/>
<head>
    <title>Meal - ${meal.id}</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
</head>
<body>
<main>
    <div class='container'>
        <h1>Meal - ${meal.id}</h1>
        <div class="mb-3">
            <a href="meals?id=${meal.id}&action=update" class="btn btn-success">Edit</a>
            <a href="meals?id=${meal.id}&action=delete" class="btn btn-danger">Delete</a>
            <a href="meals" class="btn btn-primary">Back</a>
        </div>
        <div>
            <p>
                <b>Date</b>: ${meal.dateTime.format(dateTimeFormatter)}
            </p>
            <p>
                <b>Description</b>: ${meal.description}
            </p>
            <p>
                <b>Calories</b>: ${meal.calories}
            </p>
        </div>
    </div>
</main>
</body>
</html>