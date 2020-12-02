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
import model.Karyawan;

/**
 *
 * @author R. Z. Nurfirmansyah
 */
public class DtKaryawan {
    
    private final Connection conn;
    private PreparedStatement pst;
    private ResultSet rs;
//    mengubah format tanggal
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy");
    
    public DtKaryawan() {   //  Konstruktor
        conn = Koneksi.getConn();   //  Menghubungkan dengan koneksi
    }
    
//    mengambil semua data pada table karyawan
    public ArrayList<Karyawan> getAll() {
        ArrayList<Karyawan> list = new ArrayList<>();
        String getAll = "select * from karyawan order by nik";
        
        try {
            pst = conn.prepareStatement(getAll);
            rs = pst.executeQuery();
            while (rs.next()) { //  Jika ada data ke ...
                Karyawan k = new Karyawan();    //  Membuat object baru
                k.setNik(rs.getString("nik"));
                k.setNama(rs.getString("nama"));
                if (rs.getString("tmplahir") != null) {
                    k.setTmplahir(rs.getString("tmplahir"));
                } else {
                    k.setTmplahir("");
                }
                if (rs.getString("alamat") != null) {
                    k.setAlamat(rs.getString("alamat"));
                } else {
                    k.setAlamat("");
                }
                if (rs.getString("telepon") != null) {
                    k.setTelepon(rs.getString("telepon"));
                } else {
                    k.setTelepon("");
                }
                if (rs.getString("gender") != null) {
                    k.setGender(rs.getString("gender").equals("L") ? "Laki-Laki" : "Perempuan");
                } else {
                    k.setGender("");
                }
                String tgl = "";
                if (rs.getDate("tgllahir") != null) {
                    tgl = sdf.format(rs.getDate("tgllahir"));
                }
                k.setTgllahir(tgl);
                if (rs.getString("alamat") != null) {
                    k.setAlamat(rs.getString("alamat"));
                } else {
                    k.setAlamat("");
                }
                
                list.add(k);    //  Memasukkan data perbaris kedalam array list
            }
        } catch (SQLException ex) {
            System.out.println("Kesalahan : " + ex);
        }
        
        return list;
        
    }
    
    public boolean simpan(Karyawan k, String mode) {
        String sql = null;
        int a = 0;
        if (mode.equals("add")) {
            sql = "insert into karyawan (nama, gender, tmplahir, tgllahir, alamat, telepon, nik) values (?, ?, ?, ?, ?, ?, ?)";
        } else if (mode.equals("edit")) {
            sql = "update karyawan set nama=?, gender=?, tmplahir=?, tgllahir=?, alamat=?, telepon=? where nik=?";
        }
        
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, k.getNama());
            if (k.getGender().equals("")) pst.setString(2, null);
            else pst.setString(2, k.getGender());
            pst.setString(3, k.getTmplahir());
            if (k.getTgllahir().equals("")) pst.setString(4, null);
            else pst.setString(4, k.getTgllahir());
            pst.setString(5, k.getAlamat());
            pst.setString(6, k.getTelepon());
            pst.setString(7, k.getNik());
            
            a = pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DtKaryawan.class.getName()).log(Level.SEVERE, null, ex);
        }
        return a == 1;
    }
    
    public Karyawan getDtKaryawan(String nik) {
        String search = "select * from karyawan where nik = ?";
        
        Karyawan k = new Karyawan();
        
        try {
            pst = conn.prepareStatement(search);
            pst.setString(1, nik);
            rs = pst.executeQuery();
            if (rs.next()) {
                k.setNik(rs.getString("nik"));
                k.setNama(rs.getString("nama"));
                k.setGender(rs.getString("gender"));
                k.setTmplahir(rs.getString("tmplahir"));
                k.setTgllahir(rs.getString("tgllahir"));
                k.setTelepon(rs.getString("telepon"));
                k.setAlamat(rs.getString("alamat"));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DtKaryawan.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return k;
    }
    
    public boolean hapus(String nik) {
        String sql = "delete from karyawan where nik = ?";
        int a = 0;
        
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, nik);
            a = pst.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(DtKaryawan.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return a == 1;
    }
    
    public static void main(String[] args) {
        DtKaryawan kar = new DtKaryawan();
        System.out.println(kar.getAll());
        Karyawan k = new Karyawan();
        k.setNik("23442390");
        k.setNama("rakha");
        k.setGender("L");
        k.setTmplahir("Jakarta");
        k.setTgllahir("2002-02-19");
        k.setTelepon("097342423");
        k.setAlamat("disini");
        kar.simpan(k, "edit");
    }
    
}
