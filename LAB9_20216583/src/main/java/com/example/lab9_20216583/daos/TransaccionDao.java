package com.example.lab9_20216583.daos;

import com.example.lab9_20216583.beans.Transaccion;
import com.example.lab9_20216583.beans.Usuario;
import com.example.lab9_20216583.dtos.TransaccionDto;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TransaccionDao extends DaoBase<Transaccion> {

    public ArrayList<TransaccionDto> listarPorUsuario(int idUsuario) throws SQLException {
        ArrayList<TransaccionDto> lista = new ArrayList<>();

        String sql = "SELECT " +
                "t.idtransacciones, t.titulo, t.monto, t.tipo, t.descripcion, t.fecha, " +
                "u.idusuarios, u.nombre, u.apellido, u.dni, u.correo " +
                "FROM transacciones t " +
                "INNER JOIN usuarios u ON t.usuarios_idusuarios = u.idusuarios " +
                "WHERE t.usuarios_idusuarios = ? " +
                "ORDER BY t.fecha DESC, t.idtransacciones DESC";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearTransaccionDto(rs));
                }
            }
        }

        return lista;
    }

    @Override
    public void crear(Transaccion transaccion) throws SQLException {
        String sql = "INSERT INTO transacciones(monto, descripcion, titulo, fecha, usuarios_idusuarios, tipo) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, transaccion.getMonto());
            pstmt.setString(2, transaccion.getDescripcion());
            pstmt.setString(3, transaccion.getTitulo());
            pstmt.setDate(4, Date.valueOf(transaccion.getFecha()));
            pstmt.setInt(5, transaccion.getUsuario().getIdusuarios());
            pstmt.setString(6, transaccion.getTipo());

            pstmt.executeUpdate();
        }
    }

    @Override
    public void borrar(int id) throws SQLException {
        throw new UnsupportedOperationException("Use borrarSiPerteneceAlUsuario para validar la sesión");
    }

    public boolean borrarSiPerteneceAlUsuario(int idTransaccion, int idUsuario) throws SQLException {
        String sql = "DELETE FROM transacciones " +
                "WHERE idtransacciones = ? AND usuarios_idusuarios = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idTransaccion);
            pstmt.setInt(2, idUsuario);

            return pstmt.executeUpdate() > 0;
        }
    }

    private TransaccionDto mapearTransaccionDto(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdusuarios(rs.getInt("idusuarios"));
        usuario.setNombre(rs.getString("nombre"));
        usuario.setApellido(rs.getString("apellido"));
        usuario.setDni(rs.getString("dni"));
        usuario.setCorreo(rs.getString("correo"));

        TransaccionDto dto = new TransaccionDto();
        dto.setId(rs.getInt("idtransacciones"));
        dto.setTitulo(rs.getString("titulo"));
        dto.setMonto(rs.getDouble("monto"));
        dto.setTipo(rs.getString("tipo"));
        dto.setDescripcion(rs.getString("descripcion"));
        dto.setFecha(rs.getDate("fecha").toLocalDate());
        dto.setUsuario(usuario);

        return dto;
    }
}