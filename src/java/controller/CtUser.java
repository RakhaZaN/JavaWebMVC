/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.gson.Gson;
import dao.DtUser;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.User;

/**
 *
 * @author R. Z. Nurfirmansyah
 */
@WebServlet(name = "CtUser", urlPatterns = {"/CtUser"})
public class CtUser extends HttpServlet {

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
        DtUser dtu = new DtUser();
        Gson gson = new Gson();
        User u = new User();
        if(null == page) {
            List<User> list = dtu.getall();
            String jsonData = gson.toJson(list);
            sout.println(jsonData);
        } else switch (page) {
            case "add":
                u.setUserid(request.getParameter("userid"));
                u.setPass(request.getParameter("pass"));
                u.setNik(request.getParameter("nik"));
                u.setLevel(request.getParameter("level"));
                u.setAktif(request.getParameter("aktif"));
                response.setContentType("text/html;charset=UTF-8");
                if (dtu.simpan(u, page)) {
                    sout.print("Data User Added Successfuly");
                } else {
                    sout.print("Data User Added Failed");
                }
                break;
            case "edit":
                u.setUserid(request.getParameter("userid"));
                u.setPass(request.getParameter("pass"));
                u.setNik(request.getParameter("nik"));
                u.setLevel(request.getParameter("level"));
                u.setAktif(request.getParameter("aktif"));
                response.setContentType("text/html;charset=UTF-8");
                if (dtu.simpan(u, page)) {
                    sout.print("Data User Updated Successfuly");
                } else {
                    sout.print("Data User Updated Failed");
                }
                break;
            case "show":
                String data = gson.toJson(dtu.getDtUser(request.getParameter("userid")));
                response.setContentType("application/json");
                sout.print(data);
                break;
            case "delete":
                if (dtu.hapus(request.getParameter("userid"))) {
                    response.setContentType("text/html;charset=UTF-8");
                    sout.print("Data User deleted successfully");
                } else {
                    response.setContentType("text/html;charset=UTF-8");
                    sout.print("Data User deleted Failed");
                }
                break;
            case "login":
                String log = gson.toJson(dtu.login(request.getParameter("userid"), request.getParameter("pass")));
                System.out.println(log);
                if (log.equals("{}")) {
                    response.setContentType("text/html;charset=UTF-8");
                    sout.print("failed");
                } else {
                    response.setContentType("application/json");
                    sout.print(log);
                }
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
