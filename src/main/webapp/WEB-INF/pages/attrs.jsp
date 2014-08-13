<%--
  Created by IntelliJ IDEA.
  User: zq
  Date: 2014/5/25
  Time: 10:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title></title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/common.css"/>
</head>
<body>
<c:choose>
    <c:when test="${fn:length(attrMap) == 0}">
        no attributes
    </c:when>
    <c:otherwise>
        <table class="tbl">
            <thead>
            <tr>
                <th>For TriggerInfo Use</th>
                <th>Real Attribute</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${attrMap}" var="attr">
                <tr>
                    <td><c:out value="${attr.key}"/></td>
                    <td><c:out value="${attr.value}"/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:otherwise>
</c:choose>
</body>
</html>
