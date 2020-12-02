/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import connection.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;

/**
 *
 * @author R. Z. Nurfirmansyah
 */
public class DtUser {
    
    private final Connection conn;
    private PreparedStatement pst;
    private ResultSet rs;
    
    public DtUser() {
        conn = Koneksi.getConn();
    }
    
//    Mengambil Semua data User
    public ArrayList<User> getall() {
        ArrayList<User> list = new ArrayList<>();
        String query = "select * from user";
        
        try {
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();
            while(rs.next()) {
                User u = new User();
                u.setUserid(rs.getString("userid"));
                u.setPass(rs.getString("password"));
                u.setNik(rs.getString("nik"));
                u.setLevel(rs.getString("level"));
                u.setAktif(rs.getString("aktif"));
                list.add(u);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DtUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return list;
    }
    
    public boolean simpan(User u, String mode) {
        String sql = null;
        int a = 0;
        if (mode.equals("add")) {
            sql = "insert into user (password, nik, level, aktif, userid) values (?, ?, ?, ?, ?)";
        } else if (mode.equals("edit")) {
            sql = "update user set password=?, nik=?, level=?, aktif=? where userid=?";
        }
        
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, u.getPass());
            pst.setString(2, u.getNik());
            pst.setString(3, u.getLevel());
            pst.setString(4, u.getAktif());
            pst.setString(5, u.getUserid());
            
            a = pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DtKaryawan.class.getName()).log(Level.SEVERE, null, ex);
        }
        return a == 1;
    }
    
    public User getDtUser(String userid) {
        String search = "select * from user where userid= ?";
        
        User u = new User();
        
        try {
            pst = conn.prepareStatement(search);
            pst.setString(1, userid);
            rs = pst.executeQuery();
            if (rs.next()) {
                u.setUserid(rs.getString("userid"));
                u.setPass(rs.getString("password"));
                u.setNik(rs.getString("nik"));
                u.setLevel(rs.getString("level"));
                u.setAktif(rs.getString("aktif"));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DtKaryawan.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return u;
    }
    
    public boolean hapus(String userid) {
        String sql = "delete from user where userid = ?";
        int a = 0;
        
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, userid);
            a = pst.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(DtKaryawan.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return a == 1;
    }
    
    public User login(String userid, String pass) {
        String query = "select * from user where userid=? and password=?";
        User u = new User();
        
        try {
            pst = conn.prepareStatement(query);
            pst.setString(1, userid);
            pst.setString(2, pass);
            rs = pst.executeQuery();
            while (rs.next()) {
                u.setUserid(rs.getString("userid"));
                u.setNik(rs.getString("nik"));
                u.setLevel(rs.getString("level"));
                u.setAktif(rs.getString("aktif"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DtUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return u;
    }
    
    
    public static void main(String[] args) {
        DtUser dtu = new DtUser();
        System.out.println(dtu.getall());
    }
}
