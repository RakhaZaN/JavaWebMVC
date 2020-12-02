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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Anggota;

/**
 *
 * @author R. Z. Nurfirmansyah
 */
public class DtAnggota {
    
    private final Connection conn;
    private PreparedStatement pst;
    private ResultSet rs;
//    mengubah format tanggal
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy");
    
    public DtAnggota() {   //  Konstruktor
        conn = Koneksi.getConn();   //  Menghubungkan dengan koneksi
    }
    
//    mengambil semua data pada table karyawan
    public ArrayList<Anggota> getAll() {
        ArrayList<Anggota> list = new ArrayList<>();
        String getAll = "select * from anggota order by noanggota";
        
        try {
            pst = conn.prepareStatement(getAll);
            rs = pst.executeQuery();
            while (rs.next()) { //  Jika ada data ke ...
                Anggota a = new Anggota();    //  Membuat object baru
                a.setNoAnggota(rs.getString("noanggota"));
                a.setNama(rs.getString("nama"));
                if (rs.getString("tmplahir") != null) {
                    a.setTmplahir(rs.getString("tmplahir"));
                } else {
                    a.setTmplahir("");
                }
                if (rs.getString("alamat") != null) {
                    a.setAlamat(rs.getString("alamat"));
                } else {
                    a.setAlamat("");
                }
                if (rs.getString("telepon") != null) {
                    a.setTelepon(rs.getString("telepon"));
                } else {
                    a.setTelepon("");
                }
                if (rs.getString("gender") != null) {
                    a.setGender(rs.getString("gender").equals("L") ? "Laki-Laki" : "Perempuan");
                } else {
                    a.setGender("");
                }
                String tgl = "";
                if (rs.getDate("tgllahir") != null) {
                    tgl = sdf.format(rs.getDate("tgllahir"));
                }
                a.setTgllahir(tgl);
                if (rs.getString("alamat") != null) {
                    a.setAlamat(rs.getString("alamat"));
                } else {
                    a.setAlamat("");
                }
                
                list.add(a);    //  Memasukkan data perbaris kedalam array list
            }
        } catch (SQLException ex) {
            System.out.println("Kesalahan : " + ex);
        }
        
        return list;
        
    }
    
    public boolean simpan(Anggota an, String mode) {
        String sql = null;
        int a = 0;
        if (mode.equals("add")) {
            sql = "insert into anggota (nama, gender, tmplahir, tgllahir, alamat, telepon, noanggota) values (?, ?, ?, ?, ?, ?, ?)";
        } else if (mode.equals("edit")) {
            sql = "update anggota set nama=?, gender=?, tmplahir=?, tgllahir=?, alamat=?, telepon=? where noanggota=?";
        }
        
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, an.getNama());
            if (an.getGender().equals("")) pst.setString(2, null);
            else pst.setString(2, an.getGender());
            pst.setString(3, an.getTmplahir());
            if (an.getTgllahir().equals("")) pst.setString(4, null);
            else pst.setString(4, an.getTgllahir());
            pst.setString(5, an.getAlamat());
            pst.setString(6, an.getTelepon());
            pst.setString(7, an.getNoAnggota());
            
            a = pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DtKaryawan.class.getName()).log(Level.SEVERE, null, ex);
        }
        return a == 1;
    }
    
    public Anggota getDtAnggota(String noa) {
        String search = "select * from anggota where noanggota = ?";
        
        Anggota a = new Anggota();
        
        try {
            pst = conn.prepareStatement(search);
            pst.setString(1, noa);
            rs = pst.executeQuery();
            if (rs.next()) {
                a.setNoAnggota(rs.getString("noanggota"));
                a.setNama(rs.getString("nama"));
                a.setGender(rs.getString("gender"));
                a.setTmplahir(rs.getString("tmplahir"));
                a.setTgllahir(rs.getString("tgllahir"));
                a.setTelepon(rs.getString("telepon"));
                a.setAlamat(rs.getString("alamat"));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DtKaryawan.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return a;
    }
    
    public boolean hapus(String noa) {
        String sql = "delete from anggota where noanggota = ?";
        int a = 0;
        
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, noa);
            a = pst.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(DtKaryawan.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return a == 1;
    }
    
    public static void main(String[] args) {
        DtAnggota ang = new DtAnggota();
        System.out.println(ang.getAll());
        Anggota a = new Anggota();
        a.setNoAnggota("001");
        a.setNama("zahran");
        a.setGender("L");
        a.setTmplahir("Jakarta");
        a.setTgllahir("2002-02-19");
        a.setTelepon("097342423");
        a.setAlamat("disini");
        ang.simpan(a, "add");
    }
    
}
