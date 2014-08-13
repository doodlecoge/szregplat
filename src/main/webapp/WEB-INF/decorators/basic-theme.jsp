<%--
  Created by IntelliJ IDEA.
  User: hch
  Date: 2014/5/28
  Time: 18:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
           prefix="decorator" %>
<html>
<head>
    <title><decorator:title/> - 12320 Web Service</title>
    <script type="text/javascript"
            src="<%= request.getContextPath() %>/js/jquery-1.11.1.js"></script>
    <style type="text/css">
        body {
            font-family: arial, "微软雅黑", "宋体", Tahoma, Geneva, sans-serif;
            padding: 0 5px;
        }
        .welcome {
            font-size: 36px;
            border-bottom: 1px solid #888;
            height: 50px;
            line-height: 50px;
            text-decoration: none;
            font-variant: small-caps;
            display: block;
            margin-bottom: 5px;
        }
    </style>
    <decorator:head/>
</head>
<body>
<a class="welcome" href="<%= request.getContextPath() %>/">
    12320 Web Service
</a>
<decorator:body/>
</body>
</html>
