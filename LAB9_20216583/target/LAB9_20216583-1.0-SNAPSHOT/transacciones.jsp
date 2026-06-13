<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Lista de transacciones</title>
  <%@include file="WEB-INF/jspf/styles.jspf"%>
</head>
<body>
<%@include file="WEB-INF/jspf/navbar.jspf"%>

<div class="container">
  <h1>Lista de Transacciones</h1>

  <%@include file="WEB-INF/jspf/messages.jspf"%>

  <a class="btn btn-primary" href="${pageContext.request.contextPath}/transacciones?action=new">
    Nueva Transacción
  </a>

  <table>
    <thead>
    <tr>
      <th>ID</th>
      <th>Título</th>
      <th>Monto</th>
      <th>Tipo</th>
      <th>Descripción</th>
      <th>Fecha</th>
      <th>Usuario</th>
      <th>Acción</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="transaccion" items="${listaTransacciones}">
      <tr>
        <td>${transaccion.id}</td>
        <td>${transaccion.titulo}</td>
        <td>S/ ${transaccion.monto}</td>
        <td>${transaccion.tipo}</td>
        <td>
          <c:choose>
            <c:when test="${empty transaccion.descripcion}">-</c:when>
            <c:otherwise>${transaccion.descripcion}</c:otherwise>
          </c:choose>
        </td>
        <td>${transaccion.fecha}</td>
        <td>${transaccion.usuario.nombre} ${transaccion.usuario.apellido}</td>
        <td>
          <a class="btn btn-danger"
             href="${pageContext.request.contextPath}/transacciones?action=delete&id=${transaccion.id}"
             onclick="return confirm('¿Seguro que deseas eliminar esta transacción?');">
            Borrar
          </a>
        </td>
      </tr>
    </c:forEach>

    <c:if test="${empty listaTransacciones}">
      <tr>
        <td colspan="8">No hay transacciones registradas para el usuario en sesión.</td>
      </tr>
    </c:if>
    </tbody>
  </table>
</div>
</body>
</html>