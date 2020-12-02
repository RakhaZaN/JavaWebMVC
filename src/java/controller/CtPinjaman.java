/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.gson.Gson;
import dao.DtPinjam;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Pinjam;

/**
 *
 * @author R. Z. Nurfirmansyah
 */
@WebServlet(name = "CtPinjaman", urlPatterns = {"/CtPinjaman"})
public class CtPinjaman extends HttpServlet {

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
        DtPinjam dtp = new DtPinjam();
        Pinjam p = new Pinjam();
        Gson gson = new Gson();
        if (null == page) {     //  Berada di halaman index karyawan
            List<Pinjam> listang = dtp.getAll();
            String jsonData = gson.toJson(listang);
            sout.println(jsonData);
        } else switch (page) {
            case "add":
                //  Berada di page tambah karyawan
                p.setNopin(request.getParameter("nopin"));
                p.setNoa(request.getParameter("noa"));
                p.setPokpin(request.getParameter("pokpin"));
                p.setBupin(request.getParameter("bupin"));
                p.setLapin(request.getParameter("lapin"));
                p.setTglpin(request.getParameter("tglpin"));
                p.setApok(request.getParameter("apok"));
                p.setAbu(request.getParameter("abu"));
                p.setAcp(request.getParameter("acp"));
                if (dtp.simpan(p, page)) {
                    response.setContentType("text/html;charset=UTF-8");
                    sout.print("New Pinjaman added successfully");
                } else {
                    response.setContentType("text/html;charset=UTF-8");
                    sout.print("New Pinjaman added Failed");
                }
                break;
            case "edit":
                p.setNopin(request.getParameter("nopin"));
                p.setNoa(request.getParameter("noa"));
                p.setPokpin(request.getParameter("pokpin"));
                p.setBupin(request.getParameter("bupin"));
                p.setLapin(request.getParameter("lapin"));
                p.setTglpin(request.getParameter("tglpin"));
                p.setApok(request.getParameter("apok"));
                p.setAbu(request.getParameter("abu"));
                p.setAcp(request.getParameter("acp"));
                if (dtp.simpan(p, page)) {
                    response.setContentType("text/html;charset=UTF-8");
                    sout.print("Data Pinjaman updated successfully");
                } else {
                    response.setContentType("text/html;charset=UTF-8");
                    sout.print("Data Pinjaman updated Failed");
                }
                break;
            case "delete":
                if (dtp.hapus(request.getParameter("nopin"))) {
                    response.setContentType("text/html;charset=UTF-8");
                    sout.print("Data Pinjaman deleted successfully");
                } else {
                    response.setContentType("text/html;charset=UTF-8");
                    sout.print("Data Pinjaman deleted Failed");
                }
                break;
            case "show":
                String dtAng = gson.toJson(dtp.getDtPin(request.getParameter("nopin")));
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
