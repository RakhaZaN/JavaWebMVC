/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.gson.Gson;
import dao.DtAngsuran;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Angsuran;

/**
 *
 * @author R. Z. Nurfirmansyah
 */
@WebServlet(name = "CtAngsuran", urlPatterns = {"/CtAngsuran"})
public class CtAngsuran extends HttpServlet {

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
        DtAngsuran dtp = new DtAngsuran();
        Gson gson = new Gson();
        if (null == page) {     //  Berada di halaman index karyawan
            List<Angsuran> listang = dtp.getAll();
            String jsonData = gson.toJson(listang);
            sout.println(jsonData);
        } else switch (page) {
            case "add":
                //  Berada di page tambah karyawan
                Angsuran check = dtp.getDtAngsur(request.getParameter("nopin"), request.getParameter("angsurke"));
                if (check.getNopin() != null) {
                    response.setContentType("text/html;charse=UTF-8");      //  Mengirim respons dengan format text
                    sout.print("No Pinjaman : " + check.getNopin() + " And Angsur ke-"+ check.getAke() + " already exists");
                } else {
                    Angsuran p = new Angsuran();
                    p.setNopin(request.getParameter("nopin"));
                    p.setAke(request.getParameter("angsurke"));
                    p.setTglangsur(request.getParameter("tglangsur"));
                    p.setBea(request.getParameter("bea"));
                    p.setSipin(request.getParameter("sipin"));
                    p.setNokar(request.getParameter("nokar"));
                    if (dtp.simpan(p, page)) {
                        response.setContentType("text/html;chatset=UTF-8");
                        sout.print("New Angsuran added successfully");
                    } else {
                        response.setContentType("text/html;chatset=UTF-8");
                        sout.print("New Angsuran added Failed");
                    }
                }   break;
            case "edit":
                Angsuran p = new Angsuran();
                p.setNopin(request.getParameter("nopin"));
                p.setAke(request.getParameter("angsurke"));
                p.setTglangsur(request.getParameter("tglangsur"));
                p.setBea(request.getParameter("bea"));
                p.setSipin(request.getParameter("sipin"));
                p.setNokar(request.getParameter("nokar"));
                if (dtp.simpan(p, page)) {
                    response.setContentType("text/html;chatset=UTF-8");
                    sout.print("Data Angsuran updated successfully");
                } else {
                    response.setContentType("text/html;chatset=UTF-8");
                    sout.print("Data Angsuran updated Failed");
                }
                break;
            case "delete":
                if (dtp.hapus(request.getParameter("nopin"), request.getParameter("angsurke"))) {
                    response.setContentType("text/html;chatset=UTF-8");
                    sout.print("Data Angsuran deleted successfully");
                } else {
                    response.setContentType("text/html;chatset=UTF-8");
                    sout.print("Data Angsuran deleted Failed");
                }
                break;
            case "show":
                String dtAng = gson.toJson(dtp.getDtAngsur(request.getParameter("nopin"), request.getParameter("angsurke")));
                response.setContentType("application/json");
                sout.print(dtAng);
                break;
            case "check":
                int data = dtp.getAngsur(request.getParameter("nopin"));
                response.setContentType("text/html;charset=UTF-8");
                sout.print(data);
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
