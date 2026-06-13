<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Nueva transacción</title>
  <%@include file="WEB-INF/jspf/styles.jspf"%>
</head>
<body>
<%@include file="WEB-INF/jspf/navbar.jspf"%>

<div class="container">
  <h1>Nueva Transacción</h1>

  <%@include file="WEB-INF/jspf/messages.jspf"%>

  <form method="post" action="${pageContext.request.contextPath}/transacciones?action=create">
    <label for="titulo">Título:</label>
    <input type="text" id="titulo" name="titulo" value="${transaccionForm.titulo}" required>

    <label for="tipo">Tipo:</label>
    <select id="tipo" name="tipo" required>
      <option value="">Seleccione un tipo</option>
      <option value="ingreso" ${transaccionForm.tipo == 'ingreso' ? 'selected="selected"' : ''}>Ingreso</option>
      <option value="egreso" ${transaccionForm.tipo == 'egreso' ? 'selected="selected"' : ''}>Egreso</option>
    </select>

    <label for="monto">Monto:</label>
    <input type="number" id="monto" name="monto" step="0.01" min="0.01" value="${transaccionForm.monto}" required>

    <label for="descripcion">Descripción:</label>
    <textarea id="descripcion" name="descripcion">${transaccionForm.descripcion}</textarea>

    <p class="muted">La fecha se registrará automáticamente con la fecha actual.</p>

    <div class="actions">
      <button class="btn btn-primary" type="submit">Guardar</button>
      <a class="btn btn-secondary" href="${pageContext.request.contextPath}/transacciones">Cancelar</a>
    </div>
  </form>
</div>
</body>
</html>