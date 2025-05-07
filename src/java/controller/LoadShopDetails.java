/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.ResponseDTO;
import entity.Product;
import entity.SubCategory;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import model.Validations;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author ASUS
 */
@WebServlet(name = "LoadShopDetails", urlPatterns = {"/LoadShopDetails"})
public class LoadShopDetails extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //create response json object
        JsonObject responseJsonObject = new JsonObject();

        //create gson object
        Gson gson = new Gson();
        //create hibernate session
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            //get product id from request parameter
            String productId = request.getParameter("pid");

            //first validation : check product id is int
            if (Validations.isInteger(productId)) {

                /*start : single product view*/
                //search product from db
                Product product = (Product) session.get(Product.class, Integer.valueOf(productId));
                //set product's user password,verification code,email(loging details) : null
                product.getUser().setPassword(null);
                product.getUser().setVerificationCode(null);
                product.getUser().setEmail(null);
                /*end : single product view*/

 /*start : related product loading*/
                //search sub category from db
                Criteria criteria1 = session.createCriteria(SubCategory.class);
                criteria1.add(Restrictions.eq("mainCategory", product.getSubCategory().getMainCategory()));
                List<SubCategory> subCategoryList = criteria1.list();

                //search product from db 
                Criteria criteria2 = session.createCriteria(Product.class);
                //filter by product id
                criteria2.add(Restrictions.ne("id", product.getId()));
                //filter by selcted sub category
                criteria2.add(Restrictions.in("subCategory", subCategoryList));
                criteria2.setMaxResults(6);

                List<Product> productList = criteria2.list();

                //reading product list
                for (Product product1 : productList) {
                    //set product's user password,verification code,email(loging details) : null
                    product1.getUser().setPassword(null);
                    product1.getUser().setVerificationCode(null);
                    product1.getUser().setEmail(null);
                }
                /*end : related product loading*/

                //add single product to response json object
                responseJsonObject.add("product", gson.toJsonTree(product));
                //add related product list to response json object
                responseJsonObject.add("productList", gson.toJsonTree(productList));

            } else {
                //second validation 

                if (productId.equals("shopLoad")) {

                    /*start : shop product loading*/
                    //search product from db 
                    Criteria criteria1 = session.createCriteria(Product.class);
                    //filter by status
                    criteria1.add(Restrictions.eq("active", true));
                    //filter by published year
                    criteria1.addOrder(Order.desc("publishedYear"));
                    //filter by date time
                    criteria1.addOrder(Order.desc("dateTime"));
                    criteria1.setMaxResults(2);

                    List<Product> productList = criteria1.list();

                    //reading product list
                    for (Product product1 : productList) {
                        //set product's user password,verification code,email(loging details) : null
                        product1.getUser().setPassword(null);
                        product1.getUser().setVerificationCode(null);
                        product1.getUser().setEmail(null);
                    }
                    /*end : related product loading*/

                    //add related product list to response json object
                    responseJsonObject.add("productList", gson.toJsonTree(productList));

                } else {
                    //invalid id
                    responseJsonObject.addProperty("message", "Product Not Found");
                }

            }
            //set response content type
            response.setContentType("application/json");
            //write response json object to response ans json text
            response.getWriter().write(gson.toJson(responseJsonObject));
            //close hibernate session
            session.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
