/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.CartDTO;
import dto.ResponseDTO;
import dto.UserDTO;
import entity.Cart;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author ASUS
 */
@WebServlet(name = "LogIn", urlPatterns = {"/LogIn"})
public class LogIn extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //Response DTO
        ResponseDTO responseDTO = new ResponseDTO();
        //GSON
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        //Connecting with database
        Session session = HibernateUtil.getSessionFactory().openSession();
        //Data transaction
        Transaction transaction = session.beginTransaction();

        //Load data to User DTO from Request Parameters
        final UserDTO userDTO = gson.fromJson(request.getReader(), UserDTO.class);

        try {
            //validation
            if (userDTO.getEmail().isEmpty()) {
                responseDTO.setContent("Please Enter Your Email");
            } else if (userDTO.getPassword().isEmpty()) {
                responseDTO.setContent("Please Enter Your Password");
            } else {

                //Criteria search from user entity
                Criteria criteria1 = session.createCriteria(User.class);
                //Filter with given email
                criteria1.add(Restrictions.eq("email", userDTO.getEmail()));
                //Filter with given password
                criteria1.add(Restrictions.eq("password", userDTO.getPassword()));

                //is user found
                if (!criteria1.list().isEmpty()) {

                    //get user
                    User user = (User) criteria1.list().get(0);

                    //check verified or not verified
                    if (!user.isStatus()) {
                        //Store user email in httpsession 
                        request.getSession().setAttribute("email", userDTO.getEmail());
                        //response : unverified
                        responseDTO.setContent("UnVerified");

                    } else {
                        //verified

                        //set data for user dto
                        userDTO.setFname(user.getFname());
                        userDTO.setLname(user.getLname());
                        userDTO.setPassword(null);
                        //set user dto to httpsession
                        request.getSession().setAttribute("user", userDTO);

                        
                        
                        //transfer session cart to db cart
                        if (request.getSession().getAttribute("sessionCart") != null) {
                            //session cart found from http session
                            
                            //get session cart from http session
                            ArrayList<CartDTO> sessionCart = (ArrayList<CartDTO>) request.getSession().getAttribute("sessionCart");

                            //search cart from db
                            Criteria criteria2 = session.createCriteria(Cart.class);
                            criteria2.add(Restrictions.eq("user", user));
                            List<Cart> dbCart = criteria2.list();

                            
                            if (dbCart.isEmpty()) {
                                //db cart is empty
                                //add all session cart items into db cart
                                //reading session cart
                                for (CartDTO cartDTO : sessionCart) {
                                    
                                    //create new cart object
                                    Cart cart = new Cart();
                                    cart.setProduct(cartDTO.getProduct());//*
                                    cart.setQty(cartDTO.getQty());
                                    cart.setUser(user);
                                    //save cart to db
                                    session.save(cart);

                                }
                            } else {
                                //found items in db cart
                                //update exsisting products add another products to db cart
                                
                                //reading session cart
                                for (CartDTO cartDTO : sessionCart) {
                                    //is found cart item in db : false
                                    boolean isFoundInDBCart = false;
                                    //reading db cart
                                    for (Cart cart : dbCart) {
                                        //comparing db cart and session cart
                                        if (cartDTO.getProduct().getId() == cart.getProduct().getId()) {
                                            //same item found in session cart and db cart
                                            isFoundInDBCart = true;
                                            if ((cartDTO.getQty() + cart.getQty()) <= cart.getProduct().getQty()) {
                                                //qty available
                                                cart.setQty(cartDTO.getQty() + cart.getQty());
                                                session.update(cart);
                                            } else {
                                                //qty not available
                                                //update available qty 
                                                cart.setQty(cart.getProduct().getQty());
                                                session.update(cart);
                                            }
                                        }
                                    }
                                    if (!isFoundInDBCart) {
                                        //not found cart items in db cart
                                        //create new cart object
                                        Cart cart = new Cart();
                                        cart.setProduct(cartDTO.getProduct());//*
                                        cart.setQty(cartDTO.getQty());
                                        cart.setUser(user);
                                        //save to db cart
                                        session.save(cart);
                                    }
                                }
                            }
                            //after transfer cart items to db cart from session cart , removing session cart
                            request.getSession().removeAttribute("sessionCart");
                            //all update in to db
                            transaction.commit();
                        }

                        //Response status success
                        responseDTO.setSuccess(true);
                        responseDTO.setContent("Sign In Success");
                    }

                } else {
                    responseDTO.setContent("Invalid Details. Please try again!");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //close the session
            session.close();
        }

        //Response content type
        response.setContentType("application/json");
        //Convert response dto onject to json text
        response.getWriter().write(gson.toJson(responseDTO));
    }

}
