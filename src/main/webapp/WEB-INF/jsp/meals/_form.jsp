<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<input type="hidden" name="id" value="${meal.id}">
<dl>
    <dt><spring:message code="meal.date_time"/>:</dt>
    <dd><input type="datetime-local" value="${meal.dateTime}" name="dateTime" required></dd>
</dl>
<dl>
    <dt><spring:message code="meal.description"/>:</dt>
    <dd><input type="text" value="${meal.description}" size=40 name="description" required></dd>
</dl>
<dl>
    <dt><spring:message code="meal.calories"/>:</dt>
    <dd><input type="number" value="${meal.calories}" name="calories" required></dd>
</dl>