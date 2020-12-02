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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Pinjam;

/**
 *
 * @author R. Z. Nurfirmansyah
 */
public class DtPinjam {
    
    private final Connection conn;
    private PreparedStatement pst;
    private ResultSet rs;
//    mengubah format tanggal
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy");
    
    public DtPinjam () {
        conn = Koneksi.getConn();
    }
    
//    mengambil semua data pada table Pinjam
    public ArrayList<Pinjam> getAll() {
        ArrayList<Pinjam> list = new ArrayList<>();
        String getAll = "SELECT p.*, a.nama AS nama_anggota, k.nama AS nama_petugas FROM pinjaman p, anggota a, karyawan k where p.noanggota = a.noanggota AND p.accpetugas = k.nik";
        
        try {
            pst = conn.prepareStatement(getAll);
            rs = pst.executeQuery();
            while (rs.next()) { //  Jika ada data ke ...
                Pinjam p = new Pinjam();    //  Membuat object baru
                p.setNopin(rs.getString("nopinjaman"));
                p.setNoa(rs.getString("noanggota"));
                p.setPokpin(formatRp(rs.getString("pokokpinjaman")));
                p.setBupin(formatRp(rs.getString("bungapinjaman")));
                p.setLapin(rs.getString("lamapinjaman"));
                p.setApok(formatRp(rs.getString("angsuranpokok")));
                p.setAbu(formatRp(rs.getString("angsuranbunga")));
                p.setAcp(rs.getString("accpetugas"));
                p.setNmang(rs.getString("nama_anggota"));
                p.setNmpet(rs.getString("nama_petugas"));
                String tgl = "";
                if (rs.getDate("tglpinjaman") != null) {
                    tgl = sdf.format(rs.getDate("tglpinjaman"));
                }
                p.setTglpin(tgl);
                
                list.add(p);    //  Memasukkan data perbaris kedalam array list
            }
        } catch (SQLException ex) {
            System.out.println("Kesalahan : " + ex);
        }
        
        return list;
        
    }
    
    public boolean simpan(Pinjam pin, String mode) {
        String sql = null;
        int a = 0;
        if (mode.equals("add")) {
            sql = "insert into pinjaman (noanggota, pokokpinjaman, bungapinjaman, tglpinjaman, lamapinjaman, angsuranpokok, angsuranbunga, accpetugas, nopinjaman) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        } else if (mode.equals("edit")) {
            sql = "update pinjaman set noanggota=?, pokokpinjaman=?, bungapinjaman=?, tglpinjaman=?, lamapinjaman=?, angsuranpokok=?, angsuranbunga=?, accpetugas=? where nopinjaman=?";
        }
        
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, pin.getNoa());
            pst.setString(2, pin.getPokpin());
            pst.setString(3, pin.getBupin());
            if (pin.getTglpin().equals("")) pst.setString(4, null);
            else pst.setString(4, pin.getTglpin());
            pst.setString(5, pin.getLapin());
            pst.setString(6, pin.getApok());
            pst.setString(7, pin.getAbu());
            pst.setString(8, pin.getAcp());
            pst.setString(9, pin.getNopin());
            
            a = pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DtKaryawan.class.getName()).log(Level.SEVERE, null, ex);
        }
        return a == 1;
    }
    
    public Pinjam getDtPin(String nopin) {
        String search = "select p.*, a.nama from pinjaman p, anggota a where nopinjaman = ? and p.noanggota = a.noanggota";
        
        Pinjam pin = new Pinjam();
        
        try {
            pst = conn.prepareStatement(search);
            pst.setString(1, nopin);
            rs = pst.executeQuery();
            if (rs.next()) {
                pin.setNopin(rs.getString("nopinjaman"));
                pin.setNoa(rs.getString("noanggota"));
                pin.setPokpin(rs.getString("pokokpinjaman"));
                pin.setBupin(rs.getString("bungapinjaman"));
                pin.setLapin(rs.getString("lamapinjaman"));
                pin.setApok(rs.getString("angsuranpokok"));
                pin.setAbu(rs.getString("angsuranbunga"));
                pin.setAcp(rs.getString("accpetugas"));
                pin.setTglpin(rs.getString("tglpinjaman"));
                pin.setNmang(rs.getString("nama"));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DtKaryawan.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return pin;
    }
    
    public boolean hapus(String nopin) {
        String sql = "delete from pinjaman where nopinjaman = ?";
        int a = 0;
        
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, nopin);
            a = pst.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(DtKaryawan.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return a == 1;
    }
    
    public String formatRp(String angka) {
        Double a = Double.parseDouble(angka);
        DecimalFormat uang = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols format = new DecimalFormatSymbols();
        format.setCurrencySymbol("Rp");
        format.setMonetaryDecimalSeparator(',');
        format.setGroupingSeparator('.');
        uang.setDecimalFormatSymbols(format);
        return uang.format(a);
    }
    
    public static void main(String[] args) {
        DtPinjam dtpin = new DtPinjam();
        System.out.println(dtpin.getDtPin("00001"));
//        Pinjam pin = new Pinjam();
//        pin.setNopin("00002");
//        pin.setTglpin("2020-11-16");
//        pin.setNoa("0004");
//        pin.setPokpin("1000000");
//        pin.setBupin("10000");
//        pin.setLapin("12");
//        pin.setAcp("00003");
//        pin.setApok("8000");
//        pin.setAbu("4000");
//        dtpin.simpan(pin, "add");
    }
    
    
}
