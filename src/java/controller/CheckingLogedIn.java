/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.ResponseDTO;
import dto.UserDTO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ASUS
 */
@WebServlet(name = "CheckingLogedIn", urlPatterns = {"/CheckingLogedIn"})
public class CheckingLogedIn extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //create response dto
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setSuccess(false);

        //create gson builder
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        //check user from http session
        if (request.getSession().getAttribute("user") != null) {
            //alredy signin

            //set success true
            responseDTO.setSuccess(true);

        } else {
            //not signin
            responseDTO.setContent("Not Signed In");
        }

        //set response content type
        response.setContentType("application/json");
        //write response dto as json text
        response.getWriter().write(gson.toJson(responseDTO));
    }

}
