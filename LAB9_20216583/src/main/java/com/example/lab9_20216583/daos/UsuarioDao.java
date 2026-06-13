package com.example.lab9_20216583.daos;

import com.example.lab9_20216583.beans.Usuario;
import com.example.lab9_20216583.dtos.UsuarioDto;
import com.example.lab9_20216583.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UsuarioDao extends DaoBase<Usuario> {

    public Usuario validarLogin(String correo, String passwordPlano) throws SQLException {
        String sql = "SELECT idusuarios, nombre, apellido, pass, dni, correo " +
                "FROM usuarios WHERE correo = ? AND pass = ?";

        String passwordHash = PasswordUtil.sha256(passwordPlano);

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, correo);
            pstmt.setString(2, passwordHash);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        }

        return null;
    }

    public ArrayList<UsuarioDto> listarDto() throws SQLException {
        ArrayList<UsuarioDto> lista = new ArrayList<>();

        String sql = "SELECT idusuarios, nombre, apellido, dni, correo " +
                "FROM usuarios ORDER BY idusuarios";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                UsuarioDto dto = new UsuarioDto();
                dto.setId(rs.getInt("idusuarios"));
                dto.setNombre(rs.getString("nombre"));
                dto.setApellido(rs.getString("apellido"));
                dto.setDni(rs.getString("dni"));
                dto.setCorreo(rs.getString("correo"));
                lista.add(dto);
            }
        }

        return lista;
    }

    @Override
    public void crear(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios(nombre, apellido, pass, dni, correo) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getApellido());
            pstmt.setString(3, PasswordUtil.sha256(usuario.getPass()));
            pstmt.setString(4, usuario.getDni());
            pstmt.setString(5, usuario.getCorreo());

            pstmt.executeUpdate();
        }
    }

    @Override
    public void borrar(int id) throws SQLException {
        throw new UnsupportedOperationException("No se solicita borrar usuarios en este laboratorio");
    }

    public boolean existeDni(String dni) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE dni = ?";
        return existePorCampo(sql, dni);
    }

    public boolean existeCorreo(String correo) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE correo = ?";
        return existePorCampo(sql, correo);
    }

    private boolean existePorCampo(String sql, String valor) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, valor);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdusuarios(rs.getInt("idusuarios"));
        usuario.setNombre(rs.getString("nombre"));
        usuario.setApellido(rs.getString("apellido"));
        usuario.setPass(rs.getString("pass"));
        usuario.setDni(rs.getString("dni"));
        usuario.setCorreo(rs.getString("correo"));
        return usuario;
    }
}