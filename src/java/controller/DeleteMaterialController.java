/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

/**
 *
 * @author legia
 */

import dal.MaterialDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "DeleteMaterialController", urlPatterns = {"/delete-material"})
public class DeleteMaterialController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MaterialDAO materialDAO = new MaterialDAO();
        int materialId = Integer.parseInt(request.getParameter("id"));
        try {
            materialDAO.deleteMaterial(materialId);
        } catch (SQLException ex) {
            Logger.getLogger(DeleteMaterialController.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.sendRedirect("list-material");
    }
}