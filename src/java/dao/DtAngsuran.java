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
import model.Angsuran;

/**
 *
 * @author R. Z. Nurfirmansyah
 */
public class DtAngsuran {
    
    private final Connection conn;
    private PreparedStatement pst, pst1;
    private ResultSet rs, rs1;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    
    public DtAngsuran() {
        conn = Koneksi.getConn();
    }
    
    
//    Mengambil Semua data
    public ArrayList<Angsuran> getAll() {
        ArrayList<Angsuran>  list = new ArrayList<>();
        String query = "Select an.*,a.noanggota, a.nama as nmanggota, k.nama as nmkaryawan from angsuran an, pinjaman p, anggota a, karyawan k where an.nopinjaman = p.nopinjaman AND an.nokaryawan = k.nik AND p.noanggota = a.noanggota";
        
        try {
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();
            while(rs.next()) {
                Angsuran an = new Angsuran();
                an.setNopin(rs.getString("nopinjaman"));
                an.setNoa(rs.getString("noanggota"));
                an.setNmanggota(rs.getString("nmanggota"));
                an.setAke(rs.getString("angsurke"));
                an.setBea(formatRp(rs.getString("besarangsur")));
                an.setSipin(formatRp(rs.getString("sisapinjaman")));
                an.setNmkar(rs.getString("nmkaryawan"));
                int count = 0;
                try {
                    String qry =  "select * from angsuran where nopinjaman = ?";
                    pst1 = conn.prepareStatement(qry);
                    pst1.setString(1, rs.getString("nopinjaman"));
                    rs1 = pst1.executeQuery();
                    while(rs1.next()) {
                        count += 1;
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(DtAngsuran.class.getName()).log(Level.SEVERE, null, ex);
                }
                an.setIsLast(rs.getString("angsurke").equals(Integer.toString(count)));
                
                list.add(an);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DtAngsuran.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return list;
    }
    
    public boolean simpan(Angsuran an, String mode) {
        String sql = null;
        int a = 0;
        if (mode.equals("add")) {
            sql = "insert into angsuran (tglangsur, besarangsur, sisapinjaman, nokaryawan, nopinjaman, angsurke) values (?, ?, ?, ?, ?, ?)";
        } else if (mode.equals("edit")) {
            sql = "update angsuran set tglangsur=?, besarangsur=?, sisapinjaman=?, nokaryawan=? where nopinjaman=? and angsurke=?";
        }
        
        try {
            pst = conn.prepareStatement(sql);
            if (an.getTglangsur().equals("")) pst.setString(1, null);
            else pst.setString(1, an.getTglangsur());
            pst.setString(2, an.getBea());
            pst.setString(3, an.getSipin());
            pst.setString(4, an.getNokar());
            pst.setString(5, an.getNopin());
            pst.setString(6, an.getAke());

            a = pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DtKaryawan.class.getName()).log(Level.SEVERE, null, ex);
        }
        return a == 1;
    }
    
    public Angsuran getDtAngsur(String nopin, String angsurke) {
        String search = "select an.*, a.noanggota, a.nama, k.nama as nmkaryawan  from angsuran an, pinjaman p, anggota a, karyawan k where an.nopinjaman = ? and angsurke = ? and an.nopinjaman = p.nopinjaman and p.noanggota = a.noanggota and an.nokaryawan = k.nik";
        
        Angsuran an = new Angsuran();
        
        try {
            pst = conn.prepareStatement(search);
            pst.setString(1, nopin);
            pst.setString(2, angsurke);
            rs = pst.executeQuery();
            if (rs.next()) {
                an.setNopin(rs.getString("nopinjaman"));
                an.setNoa(rs.getString("noanggota"));
                an.setNmanggota(rs.getString("nama"));
                an.setAke(rs.getString("angsurke"));
                an.setBea(rs.getString("besarangsur"));
                an.setSipin(rs.getString("sisapinjaman"));
                an.setNokar(rs.getString("nokaryawan"));
                an.setNmkar(rs.getString("nmkaryawan"));
                an.setTglangsur(rs.getString("tglangsur"));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DtKaryawan.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return an;
    }
    
    public boolean hapus(String nopin, String angsurke) {
        String sql = "delete from angsuran where nopinjaman = ? and angsurke=?";
        int a = 0;
        
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, nopin);
            pst.setString(2, angsurke);
            a = pst.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(DtKaryawan.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return a == 1;
    }
    
    public Integer getAngsur(String nopin) {
        String search = "select * from angsuran where nopinjaman = ?";
        int count = 0;
        
        try{
            pst = conn.prepareStatement(search);
            pst.setString(1, nopin);
            rs = pst.executeQuery();
            while(rs.next()) {
                count += 1;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DtAngsuran.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return count +1;
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
        DtAngsuran dtan = new DtAngsuran();
        System.out.println(dtan.getAll());
//        System.out.println(dtan.getDtAngsur("12321", "1"));
        Angsuran an = new Angsuran();
        an.setNopin("12321");
        an.setAke("1");
        an.setTglangsur("2020-11-20");
        an.setBea("341800");
        an.setSipin("683200");
        an.setNokar("00003");
        dtan.simpan(an, "edit");
    }
    
}
