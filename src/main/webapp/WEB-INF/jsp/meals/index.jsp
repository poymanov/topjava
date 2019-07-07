<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>

<html>
<jsp:include page="../fragments/headTag.jsp"/>
<body>
<jsp:include page="../fragments/bodyHeader.jsp"/>
<section>
    <h3><spring:message code="meal.title"/></h3>
    <form action="${pageContext.request.contextPath}/meals/filter">
        <dl>
            <dt><spring:message code="meal.date_from"/>:</dt>
            <dd><input type="date" name="startDate" value="${startDate}"></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.date_to"/>:</dt>
            <dd><input type="date" name="endDate" value="${endDate}"></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.time_from"/>:</dt>
            <dd><input type="time" name="startTime" value="${startTime}"></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.time_to"/>:</dt>
            <dd><input type="time" name="endTime" value="${endTime}"></dd>
        </dl>
        <button type="submit"><spring:message code="common.filter"/></button>
    </form>
    <hr/>
    <a href="${pageContext.request.contextPath}/meals/create"><spring:message code="common.add"/></a>
    <hr/>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th><spring:message code="meal.date_time"/></th>
            <th><spring:message code="meal.description"/></th>
            <th><spring:message code="meal.calories"/></th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr data-mealExcess="${meal.excess}">
                <td>${fn:formatDateTime(meal.dateTime)}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="${pageContext.request.contextPath}/meals/update?id=${meal.id}"><spring:message code="common.update"/></a></td>
                <td>

                    <form method="post" action="${pageContext.request.contextPath}/meals/delete?id=${meal.id}">
                        <input type="hidden" name="_method" value="delete">
                        <button type="submit"><spring:message code="common.delete"/></button>
                    </form>

<%--                    <a href="${pageContext.request.contextPath}/meals/delete?id=${meal.id}"><spring:message code="common.delete"/></a>--%>

                </td>
            </tr>
        </c:forEach>
    </table>
</section>
<jsp:include page="../fragments/footer.jsp"/>
</body>
</html>