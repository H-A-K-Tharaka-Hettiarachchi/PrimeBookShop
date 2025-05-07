/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import dto.CartDTO;
import dto.UserDTO;
import entity.Cart;
import entity.Product;
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
import javax.servlet.http.HttpSession;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author ASUS
 */
@WebServlet(name = "LoadCartItems", urlPatterns = {"/LoadCartItems"})
public class LoadCartItems extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //create gson object
        Gson gson = new Gson();
        //get http session
        HttpSession httpSession = request.getSession();
        //create hibernate session
        Session session = HibernateUtil.getSessionFactory().openSession();
        //create transaction
        Transaction transaction = session.beginTransaction();

        //create array to store cart item list
        ArrayList<CartDTO> CartDTOList = new ArrayList<>();

        try {

            //checking user is loged in or  not
            if (httpSession.getAttribute("user") != null) {
                //db cart
                //get user from http session
                UserDTO userDTO = (UserDTO) httpSession.getAttribute("user");

                //search user from db
                Criteria criteria1 = session.createCriteria(User.class);
                criteria1.add(Restrictions.eq("email", userDTO.getEmail()));
                User user = (User) criteria1.uniqueResult();

                //search cart items from db
                Criteria criteria2 = session.createCriteria(Cart.class);
                criteria2.add(Restrictions.eq("user", user));
                //store in list searched cart items
                List<Cart> cartList = criteria2.list();

                //reading searched cart list
                for (Cart cart : cartList) {

                    //create new cart dto
                    CartDTO cartDTO = new CartDTO();
                    //get the product from currently readed cart item
                    Product product = cart.getProduct();
                    //removing user from product
                    product.setUser(null);
                    //set product to cart dto
                    cartDTO.setProduct(product);
                    cartDTO.setQty(cart.getQty());
                    //add cart dto to cart item list
                    CartDTOList.add(cartDTO);
                }

            } else {
                //sessioin cart
                if (httpSession.getAttribute("sessionCart") != null) {

                    //get cart item list from session cart
                    CartDTOList = (ArrayList<CartDTO>) httpSession.getAttribute("sessionCart");
                    //read cart items from cart item list 
                    for (CartDTO cartDTO : CartDTOList) {
                        //set user null in cart's product
                        cartDTO.getProduct().setUser(null);
                    }

                } else {
                    //cart empty
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //close hibernate session
        session.close();
        //set response content type
        response.setContentType("application/json");
        //write response cart item list to response as json text
        response.getWriter().write(gson.toJson(CartDTOList));
    }
}
