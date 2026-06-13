package com.example.lab9_20216583.servlets;

import com.example.lab9_20216583.beans.Usuario;
import com.example.lab9_20216583.daos.UsuarioDao;
import com.example.lab9_20216583.util.FlashUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "UsuarioServlet", urlPatterns = {"/usuarios"})
public class UsuarioServlet extends HttpServlet {

    private final UsuarioDao usuarioDao = new UsuarioDao();

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
                case "list":
                default:
                    listarUsuarios(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Error en UsuarioServlet", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if ("create".equals(action)) {
                crearUsuario(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/usuarios");
            }
        } catch (SQLException e) {
            throw new ServletException("Error al crear usuario", e);
        }
    }

    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        FlashUtil.moveToRequest(request);
        request.setAttribute("listaUsuarios", usuarioDao.listarDto());
        request.getRequestDispatcher("usuarios.jsp").forward(request, response);
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("usuario-form.jsp").forward(request, response);
    }

    private void crearUsuario(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        String nombre = limpiar(request.getParameter("nombre"));
        String apellido = limpiar(request.getParameter("apellido"));
        String dni = limpiar(request.getParameter("dni"));
        String correo = limpiar(request.getParameter("correo"));
        String password = request.getParameter("password") == null ? "" : request.getParameter("password");

        ArrayList<String> errores = new ArrayList<>();

        if (nombre.isEmpty()) {
            errores.add("El nombre es requerido.");
        }

        if (apellido.isEmpty()) {
            errores.add("El apellido es requerido.");
        }

        if (dni.isEmpty()) {
            errores.add("El DNI es requerido.");
        } else if (!dni.matches("\\d{8}")) {
            errores.add("El DNI debe tener 8 dígitos numéricos.");
        } else if (usuarioDao.existeDni(dni)) {
            errores.add("El DNI ya está registrado.");
        }

        if (correo.isEmpty()) {
            errores.add("El correo es requerido.");
        } else if (!correo.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            errores.add("El correo no tiene un formato válido.");
        } else if (usuarioDao.existeCorreo(correo)) {
            errores.add("El correo ya está registrado.");
        }

        if (password.isEmpty()) {
            errores.add("La contraseña es requerida.");
        } else if (password.length() < 8) {
            errores.add("La contraseña debe tener mínimo 8 caracteres.");
        } else if (!password.matches(".*[A-Za-z].*") || !password.matches(".*\\d.*")) {
            errores.add("La contraseña debe contener letras y números.");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setDni(dni);
        usuario.setCorreo(correo);

        if (!errores.isEmpty()) {
            request.setAttribute("errores", errores);
            request.setAttribute("usuarioForm", usuario);
            request.getRequestDispatcher("usuario-form.jsp").forward(request, response);
            return;
        }

        usuario.setPass(password);
        usuarioDao.crear(usuario);
        FlashUtil.success(request, "Usuario registrado correctamente.");
        response.sendRedirect(request.getContextPath() + "/usuarios");
    }

    private String limpiar(String texto) {
        return texto == null ? "" : texto.trim();
    }
}