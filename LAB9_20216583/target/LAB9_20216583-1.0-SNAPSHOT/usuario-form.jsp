<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Nuevo usuario</title>
  <%@include file="WEB-INF/jspf/styles.jspf"%>
</head>
<body>
<%@include file="WEB-INF/jspf/navbar.jspf"%>

<div class="container">
  <h1>Nuevo Usuario</h1>

  <%@include file="WEB-INF/jspf/messages.jspf"%>

  <form method="post" action="${pageContext.request.contextPath}/usuarios?action=create">
    <label for="nombre">Nombre:</label>
    <input type="text" id="nombre" name="nombre" value="${usuarioForm.nombre}" required>

    <label for="apellido">Apellido:</label>
    <input type="text" id="apellido" name="apellido" value="${usuarioForm.apellido}" required>

    <label for="dni">DNI:</label>
    <input type="text" id="dni" name="dni" value="${usuarioForm.dni}" maxlength="8" required>

    <label for="correo">Correo:</label>
    <input type="email" id="correo" name="correo" value="${usuarioForm.correo}" required>

    <label for="password">Contraseña:</label>
    <input type="password" id="password" name="password" required>
    <p class="muted">Mínimo 8 caracteres. Debe contener letras y números.</p>

    <div class="actions">
      <button class="btn btn-primary" type="submit">Guardar</button>
      <a class="btn btn-secondary" href="${pageContext.request.contextPath}/usuarios">Cancelar</a>
    </div>
  </form>
</div>
</body>
</html>