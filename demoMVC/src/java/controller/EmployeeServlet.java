/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Employee;
import service.EmployeeService;
import service.EmployeeServiceImplement;

/**
 *
 * @author nkmq21
 */
@WebServlet(name = "EmployeeServlet", urlPatterns = {"/empservlet"})
public class EmployeeServlet extends HttpServlet {

    private EmployeeService empService = new EmployeeServiceImplement();

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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet EmployeeServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EmployeeServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "edit":
                empEditBtn(request, response);
                break;

            default:
                listEmployees(request, response);
                break;
        }
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
        String action = request.getParameter("action");
        if ("update".equals(action)) {
            empUpdate(request, response);
        }
        if ("insert".equals(action)) {
            empInsert(request, response);
        }
        if ("remove".equals(action)) {
            empRemove(request, response);
        }
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

    private void listEmployees(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Employee> empList = empService.findAll();
        request.setAttribute("employeeList", empList);
        RequestDispatcher rd = request.getRequestDispatcher("/employee/empList.jsp");
        rd.forward(request, response);
    }

    private void empEditBtn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Employee em = empService.findByID(id);
        request.setAttribute("employee", em);
        RequestDispatcher rd = request.getRequestDispatcher("/employee/empEdit.jsp");
        rd.forward(request, response);
    }

    private void empUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("userID"));
        String name = request.getParameter("userName");
        String email = request.getParameter("userEmail");
        String address = request.getParameter("userAddress");

        Employee updatedEmployee = new Employee(id, name, email, address);

        empService.update(id, updatedEmployee);

        listEmployees(request, response);
    }

    private void empInsert(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("userID"));
        String name = request.getParameter("userName");
        String email = request.getParameter("userEmail");
        String address = request.getParameter("userAddress");

        if (empService.existID(id)) {
            request.setAttribute("duplicateError", "The employe with this ID has been registered");
            RequestDispatcher rd = request.getRequestDispatcher("employee/empInsert.jsp");
            rd.forward(request, response);
        } else {
            Employee newEmployee = new Employee(id, name, email, address);
            empService.save(newEmployee);
            response.sendRedirect("empservlet");
        }
    }

    private void empRemove(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("userID"));

        if (empService.existID(id)) {
            empService.remove(id);
            response.sendRedirect("empservlet");
        } else {
            request.setAttribute("errorMes", "Employee Not Found");
            RequestDispatcher rd = request.getRequestDispatcher("employee/empRemove.jsp");
            rd.forward(request, response);
        }
    }

}
