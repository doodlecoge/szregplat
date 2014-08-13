<%--
  Created by IntelliJ IDEA.
  User: zq
  Date: 2014/5/24
  Time: 16:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>Triggers</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/common.css"/>
</head>
<body>
<c:choose>
    <c:when test="${fn:length(triggers) == 0}">
        no triggers
    </c:when>
    <c:otherwise>
        <table class="tbl">
            <thead>
            <tr>
                <th>Stage</th>
                <th>Action</th>
                <th>Selector</th>
                <th>Attribute</th>
                <th>Old Value</th>
                <th>New Value</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${triggers}" var="trigger">
                <tr>
                    <td><c:out value="${trigger.stage}"/></td>
                    <td><c:out value="${trigger.action}"/></td>
                    <td style="max-width: 300px;"><c:out value="${trigger.selector}"/></td>
                    <td><c:out value="${trigger.attribute}"/></td>
                    <td><c:out value="${trigger.oldValue}"/></td>
                    <td><c:out value="${trigger.newValue}"/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:otherwise>
</c:choose>
</body>
</html>
