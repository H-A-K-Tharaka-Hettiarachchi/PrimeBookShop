/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.MainCategory;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

/**
 *
 * @author ASUS
 */
@WebServlet(name = "LoadMainCategories", urlPatterns = {"/LoadMainCategories"})
public class LoadMainCategories extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //create gson
        Gson gson = new Gson();
        //create session
        Session session = HibernateUtil.getSessionFactory().openSession();

        //search main category from db
        Criteria criteria1 = session.createCriteria(MainCategory.class);
        criteria1.addOrder(Order.asc("mainCategory"));
        List<MainCategory> mainCategoryList = criteria1.list();

        //create newe json object
        JsonObject jsonObject = new JsonObject();
        //adding data to json object
        jsonObject.add("mainCategoryList", gson.toJsonTree(mainCategoryList));

        //set response content type
        response.setContentType("application/json");
        //write the json object to response as json text
        response.getWriter().write(gson.toJson(jsonObject));
        //close the session
        session.close();
    }

}
