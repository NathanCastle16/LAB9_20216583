<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Lista de usuarios</title>
  <%@include file="WEB-INF/jspf/styles.jspf"%>
</head>
<body>
<%@include file="WEB-INF/jspf/navbar.jspf"%>

<div class="container">
  <h1>Lista de Usuarios</h1>

  <%@include file="WEB-INF/jspf/messages.jspf"%>

  <a class="btn btn-primary" href="${pageContext.request.contextPath}/usuarios?action=new">
    Nuevo Usuario
  </a>

  <table>
    <thead>
    <tr>
      <th>ID</th>
      <th>Nombre</th>
      <th>Apellido</th>
      <th>DNI</th>
      <th>Correo</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="usuario" items="${listaUsuarios}">
      <tr>
        <td>${usuario.id}</td>
        <td>${usuario.nombre}</td>
        <td>${usuario.apellido}</td>
        <td>${usuario.dni}</td>
        <td>${usuario.correo}</td>
      </tr>
    </c:forEach>

    <c:if test="${empty listaUsuarios}">
      <tr>
        <td colspan="5">No hay usuarios registrados.</td>
      </tr>
    </c:if>
    </tbody>
  </table>
</div>
</body>
</html>