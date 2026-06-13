package com.example.lab9_20216583.servlets;

import com.example.lab9_20216583.beans.Usuario;
import com.example.lab9_20216583.daos.UsuarioDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private final UsuarioDao usuarioDao = new UsuarioDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuarioSesion") != null) {
            response.sendRedirect(request.getContextPath() + "/usuarios");
            return;
        }

        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String correo = request.getParameter("correo");
        String password = request.getParameter("password");

        try {
            Usuario usuario = usuarioDao.validarLogin(correo, password);

            if (usuario != null) {
                HttpSession session = request.getSession();
                session.setAttribute("usuarioSesion", usuario);
                session.setMaxInactiveInterval(20 * 60);

                response.sendRedirect(request.getContextPath() + "/usuarios");
            } else {
                request.setAttribute("error", "Correo o contraseña incorrectos");
                request.setAttribute("correo", correo);
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Error al validar el inicio de sesión", e);
        }
    }
}