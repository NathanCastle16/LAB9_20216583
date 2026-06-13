<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Inicio de sesión</title>
</head>
<body>
<div class="container-sm">
  <h1>Gestión de Gastos</h1>
  <p class="muted">Ingrese con correo y contraseña.</p>

  <c:if test="${not empty error}">
    <div class="alert alert-error">${error}</div>
  </c:if>

  <form method="post" action="${pageContext.request.contextPath}/login">
    <label for="correo">Correo:</label>
    <input type="email" id="correo" name="correo" value="${correo}" required>

    <label for="password">Contraseña:</label>
    <input type="password" id="password" name="password" required>

    <div class="actions">
      <button class="btn btn-primary" type="submit">Ingresar</button>
    </div>
  </form>

  <p class="muted">
    Usuario de prueba: gabriel.guerra@gmail.com / gabriel123
  </p>
</div>
</body>
</html>

