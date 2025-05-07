/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import dto.CartDTO;
import dto.ResponseDTO;
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
@WebServlet(name = "AddToCart", urlPatterns = {"/AddToCart"})
public class AddToCart extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //create gson object
        Gson gson = new Gson();
        //create new response dto object
        ResponseDTO responseDTO = new ResponseDTO();

        //create hibernate session
        Session session = HibernateUtil.getSessionFactory().openSession();
        //create transaction using session
        Transaction transaction = session.beginTransaction();
        
        
        try {
            //get the parameters from request
            String pid = request.getParameter("pid");// product id
            String qty = request.getParameter("qty");// qty

            //validate data
            if (!Validations.isInteger(pid)) {
                //product not found
                responseDTO.setContent("Product Not Found");
            } else if (!Validations.isInteger(qty)) {
                //invalid qty
                responseDTO.setContent("Invalid Quantity");
            } else {

                //convert data to related data types
                int productId = Integer.parseInt(pid);
                int productQty = Integer.parseInt(qty);

                //next level validations
                if (productQty <= 0) {
                    //qty must be greater than 0
                    responseDTO.setContent("Quantity must be greater than 0");
                } else {
                    //find the product from db
                    Product product = (Product) session.get(Product.class, productId);

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

                            if (criteria2.list().isEmpty()) {
                                //item not found cart
                                if (productQty <= product.getQty()) {
                                    //add product in to cart

                                    Cart cart = new Cart();
                                    cart.setProduct(product);
                                    cart.setQty(productQty);
                                    cart.setUser(user);

                                    session.save(cart);
                                    transaction.commit();
                                    responseDTO.setSuccess(true);
                                    responseDTO.setContent("Product added to Cart");

                                } else {
                                    //qty not available
                                    responseDTO.setContent("Quantity not available");
                                }
                            } else {
                                //item already found in cart
                                Cart cartItem = (Cart) criteria2.uniqueResult();
                                if ((cartItem.getQty() + productQty) <= product.getQty()) {
                                    //update qty in  cart

                                    cartItem.setQty(cartItem.getQty() + productQty);

                                    session.update(cartItem);
                                    transaction.commit();
                                    responseDTO.setSuccess(true);
                                    responseDTO.setContent("Cart item updated");

                                } else {
                                    //can't update your cart , qty not available
                                    responseDTO.setContent("Can't update your cart , Quantity not available");
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
                                    if ((foundCartDTO.getQty() + productQty) <= product.getQty()) {
                                        //update qty
                                        foundCartDTO.setQty(foundCartDTO.getQty() + productQty);
                                        responseDTO.setSuccess(true);
                                        responseDTO.setContent("Cart item updated");
                                    } else {
                                        //qty not available
                                        responseDTO.setContent("Quantity not available");
                                    }
                                } else {
                                    //product not found
                                    if (productQty <= product.getQty()) {
                                        //add to session cart
                                        CartDTO cartDTO = new CartDTO();
                                        cartDTO.setProduct(product);
                                        cartDTO.setQty(productQty);
                                        sessionCart.add(cartDTO);
                                        responseDTO.setSuccess(true);
                                        responseDTO.setContent("Product added to Cart");
                                    } else {
                                        //qty not available
                                        responseDTO.setContent("Quantity not available");
                                    }
                                }

                            } else {
                                //session cart not found
                                if (productQty <= product.getQty()) {
                                    //add to session cart
                                    //create session cart
                                    ArrayList<CartDTO> sessionCart = new ArrayList<>();

                                    //create cart dto (item) and add data
                                    CartDTO carDTO = new CartDTO();
                                    carDTO.setProduct(product);
                                    carDTO.setQty(productQty);
                                    //add cart dto to session cart
                                    sessionCart.add(carDTO);

                                    //session cart set to http session
                                    httpSession.setAttribute("sessionCart", sessionCart);
                                    responseDTO.setSuccess(true);
                                    responseDTO.setContent("Product added to Cart");

                                } else {
                                    //qty not available
                                    responseDTO.setContent("Quantity not available");
                                }
                            }

                        }
                    } else {
                        //product not found
                        responseDTO.setContent("Product not found");
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            responseDTO.setContent("Unable to process your request");
        }

        //set response content type
        response.setContentType("application/json");
        //write resopne dto to response as json text
        response.getWriter().write(gson.toJson(responseDTO));
        //close hibernate session
        session.close();
    }

}
