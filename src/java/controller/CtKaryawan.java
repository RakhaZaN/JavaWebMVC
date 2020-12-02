/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.gson.Gson;
import dao.DtKaryawan;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Karyawan;

/**
 *
 * @author R. Z. Nurfirmansyah
 */
@WebServlet(name = "CtKaryawan", urlPatterns = {"/CtKaryawan"})
public class CtKaryawan extends HttpServlet {

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
        response.setContentType("application/json");     //  Memberikan respons berupa data json
        String page = request.getParameter("page");
        PrintWriter sout = response.getWriter();
        DtKaryawan dtk = new DtKaryawan();
        Gson gson = new Gson();
        if (null == page) {     //  Berada di halaman index karyawan
            List<Karyawan> listkar = dtk.getAll();
            String jsonData = gson.toJson(listkar);
            sout.println(jsonData);
        } else switch (page) {
            case "add":
                //  Berada di page tambah karyawan
                Karyawan check = dtk.getDtKaryawan(request.getParameter("nik"));
                if (check.getNik() != null) {
                    response.setContentType("text/html;charse=UTF-8");      //  Mengirim respons dengan format text
                    sout.print("NIK : " + check.getNik() + " - " + check.getNama() + " already exists");
                } else {
                    Karyawan k = new Karyawan();
                    k.setNik(request.getParameter("nik"));
                    k.setNama(request.getParameter("nama"));
                    k.setGender(request.getParameter("gender"));
                    k.setTmplahir(request.getParameter("tmplahir"));
                    k.setTgllahir(request.getParameter("tgllahir"));
                    k.setTelepon(request.getParameter("telepon"));
                    k.setAlamat(request.getParameter("alamat"));
                    if (dtk.simpan(k, page)) {
                        response.setContentType("text/html;chatset=UTF-8");
                        sout.print("New Karyawan added successfully");
                    } else {
                        response.setContentType("text/html;chatset=UTF-8");
                        sout.print("New Karyawan added Failed");
                    }
                }   break;
            case "edit":
                Karyawan k = new Karyawan();
                k.setNik(request.getParameter("nik"));
                k.setNama(request.getParameter("nama"));
                k.setGender(request.getParameter("gender"));
                k.setTmplahir(request.getParameter("tmplahir"));
                k.setTgllahir(request.getParameter("tgllahir"));
                k.setTelepon(request.getParameter("telepon"));
                k.setAlamat(request.getParameter("alamat"));
                if (dtk.simpan(k, page)) {
                    response.setContentType("text/html;chatset=UTF-8");
                    sout.print("Karyawan updated successfully");
                } else {
                    response.setContentType("text/html;chatset=UTF-8");
                    sout.print("Karyawan updated Failed");
                }
                break;
            case "delete":
                if (dtk.hapus(request.getParameter("nik"))) {
                    response.setContentType("text/html;chatset=UTF-8");
                    sout.print("Karyawan deleted successfully");
                } else {
                    response.setContentType("text/html;chatset=UTF-8");
                    sout.print("Karyawan deleted Failed");
                }
                break;
            case "show":
                String dtKar = gson.toJson(dtk.getDtKaryawan(request.getParameter("nik")));
                response.setContentType("application/json");
                sout.print(dtKar);
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
