/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.CartDTO;
import dto.UserDTO;
import entity.Cart;
import entity.Product;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.HibernateUtil;
import model.Validations;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author ASUS
 */
@WebServlet(name = "UpdateCartQty", urlPatterns = {"/UpdateCartQty"})
public class UpdateCartQty extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //create gson object
        Gson gson = new Gson();
        //create response json object
        JsonObject responseJsonObject = new JsonObject();
        //set success false
        responseJsonObject.addProperty("success", false);

        //create session
        Session session = HibernateUtil.getSessionFactory().openSession();
        //create transaction
        Transaction transaction = session.beginTransaction();

        String productQty = request.getParameter("qty");
        String productId = request.getParameter("pid");

        if (!Validations.isInteger(productId)) {
            //invalid pid
            responseJsonObject.addProperty("message", "Invalid Product");

        } else if (!Validations.isInteger(productQty)) {
            //invalid qty
            responseJsonObject.addProperty("message", "Invalid Quantity");

        } else if (Integer.parseInt(productQty) <= 0) {
            //invalid qty range
            responseJsonObject.addProperty("message", "Quantity must be greater than 0");

        } else if (productQty.isEmpty()) {
            //empty qty
            responseJsonObject.addProperty("message", "Please Fill Quantity");
        } else {

            //find the product from db
            Product product = (Product) session.get(Product.class, Integer.valueOf(productId));

            if (product != null) {
                //product found

                //cehck user is loged in or not
                if (request.getSession().getAttribute("user") != null) {
                    //user found :  db cart

                    //get user from http session
                    UserDTO userDTO = (UserDTO) request.getSession().getAttribute("user");

                    //get  user from db
                    Criteria criteria1 = session.createCriteria(User.class);
                    criteria1.add(Restrictions.eq("email", userDTO.getEmail()));
                    User user = (User) criteria1.uniqueResult();

                    //check in db cart
                    Criteria criteria2 = session.createCriteria(Cart.class);
                    criteria2.add(Restrictions.eq("user", user));
                    criteria2.add(Restrictions.eq("product", product));

                    if (!criteria2.list().isEmpty()) {
                        //item already found in cart
                        Cart cartItem = (Cart) criteria2.uniqueResult();
                        if ((Integer.parseInt(productQty)) <= product.getQty()) {
                            //update qty in  cart

                            cartItem.setQty(Integer.parseInt(productQty));

                            session.update(cartItem);
                            transaction.commit();
                            responseJsonObject.addProperty("success", true);
                            responseJsonObject.addProperty("message", "Cart item updated");

                        } else {
                            //can't update your cart , qty not available
                            responseJsonObject.addProperty("message", "Can't update your cart , Quantity not available");
                        }
                    }

                } else {
                    //session cart

                    //get the http session
                    HttpSession httpSession = request.getSession();

                    if (httpSession.getAttribute("sessionCart") != null) {
                        //session cart  found

                        //get session cart
                        ArrayList<CartDTO> sessionCart = (ArrayList<CartDTO>) httpSession.getAttribute("sessionCart");
                        //found products from session cart
                        CartDTO foundCartDTO = null;

                        //read session cart
                        for (CartDTO cartDTO : sessionCart) {
                            if (cartDTO.getProduct().getId() == product.getId()) {
                                //set cart item to  found cart dto
                                foundCartDTO = cartDTO;
                                break;
                            }
                        }
                        if (foundCartDTO != null) {
                            //product found
                            if ((Integer.parseInt(productQty)) <= product.getQty()) {
                                //update qty
                                foundCartDTO.setQty(Integer.parseInt(productQty));
                                responseJsonObject.addProperty("success", true);
                                responseJsonObject.addProperty("message", "Cart item updated");
                            } else {
                                //qty not available
                                responseJsonObject.addProperty("message", "Quantity not available");
                            }
                        }

                    }

                }
            } else {
                //product not found
                responseJsonObject.addProperty("message", "Product not found");
            }

        }

        //set response content type
        response.setContentType("application/json");
        //write resopne dto to response as json text
        response.getWriter().write(gson.toJson(responseJsonObject));
        //close hibernate session
        session.close();
    }

}
