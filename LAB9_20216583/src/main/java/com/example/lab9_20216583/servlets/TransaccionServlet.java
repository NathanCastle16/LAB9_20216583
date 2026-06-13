package com.example.lab9_20216583.servlets;

import com.example.lab9_20216583.beans.Transaccion;
import com.example.lab9_20216583.beans.Usuario;
import com.example.lab9_20216583.daos.TransaccionDao;
import com.example.lab9_20216583.util.FlashUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

@WebServlet(name = "TransaccionServlet", urlPatterns = {"/transacciones"})
public class TransaccionServlet extends HttpServlet {

    private final TransaccionDao transaccionDao = new TransaccionDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "new":
                    mostrarFormulario(request, response);
                    break;
                case "delete":
                    borrarTransaccion(request, response);
                    break;
                case "list":
                default:
                    listarTransacciones(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Error en TransaccionServlet", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if ("create".equals(action)) {
                crearTransaccion(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/transacciones");
            }
        } catch (SQLException e) {
            throw new ServletException("Error al crear transacción", e);
        }
    }

    private void listarTransacciones(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        Usuario usuarioSesion = obtenerUsuarioSesion(request);
        FlashUtil.moveToRequest(request);

        request.setAttribute("listaTransacciones", transaccionDao.listarPorUsuario(usuarioSesion.getIdusuarios()));
        request.getRequestDispatcher("transacciones.jsp").forward(request, response);
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("transaccion-form.jsp").forward(request, response);
    }

    private void crearTransaccion(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        Usuario usuarioSesion = obtenerUsuarioSesion(request);

        String titulo = limpiar(request.getParameter("titulo"));
        String tipo = limpiar(request.getParameter("tipo"));
        String descripcion = limpiar(request.getParameter("descripcion"));
        String montoParam = limpiar(request.getParameter("monto"));

        ArrayList<String> errores = new ArrayList<>();
        double monto = 0;

        if (titulo.isEmpty()) {
            errores.add("El título es requerido.");
        }

        if (!"ingreso".equals(tipo) && !"egreso".equals(tipo)) {
            errores.add("El tipo debe ser ingreso o egreso.");
        }

        if (montoParam.isEmpty()) {
            errores.add("El monto es requerido.");
        } else {
            try {
                monto = Double.parseDouble(montoParam);
                if (monto <= 0) {
                    errores.add("El monto debe ser mayor a 0.");
                }
            } catch (NumberFormatException e) {
                errores.add("El monto debe ser numérico.");
            }
        }

        Transaccion transaccion = new Transaccion();
        transaccion.setTitulo(titulo);
        transaccion.setTipo(tipo);
        transaccion.setDescripcion(descripcion);
        transaccion.setMonto(monto);

        if (!errores.isEmpty()) {
            request.setAttribute("errores", errores);
            request.setAttribute("transaccionForm", transaccion);
            request.getRequestDispatcher("transaccion-form.jsp").forward(request, response);
            return;
        }

        transaccion.setFecha(LocalDate.now());
        transaccion.setUsuario(usuarioSesion);
        transaccionDao.crear(transaccion);

        FlashUtil.success(request, "Transacción registrada correctamente.");
        response.sendRedirect(request.getContextPath() + "/transacciones");
    }

    private void borrarTransaccion(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        Usuario usuarioSesion = obtenerUsuarioSesion(request);
        int idTransaccion = Integer.parseInt(request.getParameter("id"));

        boolean eliminado = transaccionDao.borrarSiPerteneceAlUsuario(idTransaccion, usuarioSesion.getIdusuarios());

        if (eliminado) {
            FlashUtil.success(request, "Transacción eliminada correctamente.");
        } else {
            FlashUtil.error(request, "No se pudo eliminar la transacción. Verifique que le pertenezca al usuario en sesión.");
        }

        response.sendRedirect(request.getContextPath() + "/transacciones");
    }

    private Usuario obtenerUsuarioSesion(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (Usuario) session.getAttribute("usuarioSesion");
    }

    private String limpiar(String texto) {
        return texto == null ? "" : texto.trim();
    }
}