<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<title>Home Page</title>
<body>
<h2>
    Welcome, ${username}
</h2>
<table>
    <tr>
        <th>Id</th>
        <th>Username</th>
        <th>Display Name</th>
    </tr>
    <c:forEach var="user" items="${users}">
    <tr>
        <td>${user.id}</td>
        <td>${user.username}</td>
        <td>${user.displayName}</td>
    </tr>
    </c:forEach>
</table>
</body>
</html>