package main;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Arrays;
import java.util.Objects;


public class GestionUsuarios {
    static Connection con;

    private static String salt() {
        StringBuilder r = new StringBuilder();

        for (int k = 0; k < 14; k++) {

            r.append((char) (33 + (int) (Math.random() * 94)));
        }

        return r.toString();
    }

    private static byte[] sha256(String s) {
        final byte[] b = s.getBytes();

        try {
            MessageDigest m = MessageDigest.getInstance("SHA-256");
            return m.digest(b);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    private static void connect() throws SQLException {
        String url = "jdbc:mariadb://127.0.0.1:3310/registro";
        con = DriverManager.getConnection(url, "root", "");
    }

    public boolean add(String user, String password) throws SQLException {
        try {
            connect();
            String sql = "INSERT INTO usuario VALUES (?, ?, ?)";
            final PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, user);
            st.setString(2, salt());
            st.setBytes(3, sha256(password));
            int check = st.executeUpdate();
            con.close();
            return check > 0;
        } catch (Exception e) {
            con.close();
            return false;
        }
    }

    public boolean delete(String name) {
        try {
            connect();
            String sql = "DELETE FROM usuario WHERE ID = ?";
            final PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, name);
            int check = st.executeUpdate();
            st.close();
            con.close();
            return check > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean update(String actualizar, String password) {
        try {
            String sql = "UPDATE usuario SET SALT = ?, HASH = ? WHERE ID = ?;";
            connect();
            final PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, salt());
            st.setBytes(2, sha256(password));
            st.setString(3, actualizar);
            int check = st.executeUpdate();
            con.close();
            return check > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean login(String user, String password) {
        // No diferencia entre usuarios con el mismo ID
        try {
            String sqlUsuario = "SELECT ID FROM usuario WHERE ID = ?;";
            connect();
            final PreparedStatement id = con.prepareStatement(sqlUsuario);
            id.setString(1, user);
            final ResultSet r = id.executeQuery();
            if (!r.next()) return false;
            String comprobarUsuario = r.getString(1);
            if (Objects.equals(user, comprobarUsuario)) {
                String sqlHash = "SELECT HASH FROM usuario WHERE ID = ?;";
                final PreparedStatement hash = con.prepareStatement(sqlHash);
                hash.setString(1, user);
                final ResultSet r1 = hash.executeQuery();
                if (!r1.next()) return false;
                byte[] comprobarPassword = r1.getBytes(1);
                byte[] santo = sha256(password);
                return Arrays.equals(santo, comprobarPassword);
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
