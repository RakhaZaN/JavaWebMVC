/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.gson.Gson;
import dao.DtAnggota;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Anggota;

/**
 *
 * @author R. Z. Nurfirmansyah
 */
@WebServlet(name = "CtAnggota", urlPatterns = {"/CtAnggota"})
public class CtAnggota extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        String page = request.getParameter("page");
        PrintWriter sout = response.getWriter();
        DtAnggota dta = new DtAnggota();
        Gson gson = new Gson();
        if (null == page) {     //  Berada di halaman index karyawan
            List<Anggota> listang = dta.getAll();
            String jsonData = gson.toJson(listang);
            sout.println(jsonData);
        } else switch (page) {
            case "add":
                //  Berada di page tambah karyawan
                Anggota check = dta.getDtAnggota(request.getParameter("noa"));
                if (check.getNoAnggota() != null) {
                    response.setContentType("text/html;charse=UTF-8");      //  Mengirim respons dengan format text
                    sout.print("No Anggota : " + check.getNoAnggota() + " - " + check.getNama() + " already exists");
                } else {
                    Anggota a = new Anggota();
                    a.setNoAnggota(request.getParameter("noa"));
                    a.setNama(request.getParameter("nama"));
                    a.setGender(request.getParameter("gender"));
                    a.setTmplahir(request.getParameter("tmplahir"));
                    a.setTgllahir(request.getParameter("tgllahir"));
                    a.setTelepon(request.getParameter("telepon"));
                    a.setAlamat(request.getParameter("alamat"));
                    if (dta.simpan(a, page)) {
                        response.setContentType("text/html;chatset=UTF-8");
                        sout.print("New Anggota added successfully");
                    } else {
                        response.setContentType("text/html;chatset=UTF-8");
                        sout.print("New Anggota added Failed");
                    }
                }   break;
            case "edit":
                Anggota a = new Anggota();
                a.setNoAnggota(request.getParameter("noa"));
                a.setNama(request.getParameter("nama"));
                a.setGender(request.getParameter("gender"));
                a.setTmplahir(request.getParameter("tmplahir"));
                a.setTgllahir(request.getParameter("tgllahir"));
                a.setTelepon(request.getParameter("telepon"));
                a.setAlamat(request.getParameter("alamat"));
                if (dta.simpan(a, page)) {
                    response.setContentType("text/html;chatset=UTF-8");
                    sout.print("Anggota updated successfully");
                } else {
                    response.setContentType("text/html;chatset=UTF-8");
                    sout.print("Anggota updated Failed");
                }
                break;
            case "delete":
                if (dta.hapus(request.getParameter("noa"))) {
                    response.setContentType("text/html;chatset=UTF-8");
                    sout.print("Anggota deleted successfully");
                } else {
                    response.setContentType("text/html;chatset=UTF-8");
                    sout.print("Anggota deleted Failed");
                }
                break;
            case "show":
                String dtAng = gson.toJson(dta.getDtAnggota(request.getParameter("noa")));
                response.setContentType("application/json");
                sout.print(dtAng);
                break;
            default:
                break;
        }
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
