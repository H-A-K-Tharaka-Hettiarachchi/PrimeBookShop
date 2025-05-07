/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import entity.OrderItem;
import entity.OrderStatus;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.UserDTO;
import entity.Address;
import entity.Cart;
import entity.City;
import entity.Orders;
import entity.Product;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.HibernateUtil;
import model.Payhere;
import model.Validations;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author ASUS
 */
@WebServlet(name = "Checkout", urlPatterns = {"/Checkout"})
public class Checkout extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //create gson object
        Gson gson = new Gson();

        //create json object fro store data object from request
        JsonObject requestObject = gson.fromJson(request.getReader(), JsonObject.class);

        //create response object
        JsonObject responseObject = new JsonObject();
        //set success false
        responseObject.addProperty("success", false);

        //get http session 
        HttpSession httpSession = request.getSession();

        //create hibernate session
        Session session = HibernateUtil.getSessionFactory().openSession();
        //create transaction
        Transaction transaction = session.beginTransaction();

        //get data from request
        boolean isDifferentAddress = requestObject.get("isDifferentAddress").getAsBoolean();
        String firstName = requestObject.get("firstName").getAsString();
        String lastName = requestObject.get("lastName").getAsString();
        String addressLine1 = requestObject.get("addressLine1").getAsString();
        String addressLine2 = requestObject.get("addressLine2").getAsString();
        String mobile = requestObject.get("mobile").getAsString();
        String postalCode = requestObject.get("postalCode").getAsString();
        String provinceId = requestObject.get("province").getAsString();
        String districtId = requestObject.get("district").getAsString();
        String cityId = requestObject.get("city").getAsString();

        //check is user loged in
        if (httpSession.getAttribute("user") != null) {
            // user signed in

            //get user from http session
            UserDTO userDTO = (UserDTO) httpSession.getAttribute("user");
            //get user from db
            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", userDTO.getEmail()));
            User user = (User) criteria1.uniqueResult();

            //check is different address
            if (!isDifferentAddress) {
                //get current address

                //search current address from db
                Criteria criteria2 = session.createCriteria(Address.class);
                criteria2.add(Restrictions.eq("user", user));
                criteria2.addOrder(Order.desc("id"));
                criteria2.setMaxResults(1);

                if (criteria2.list().isEmpty()) {
                    //current address not found. please create a new address
                    responseObject.addProperty("message", "Current Address not found. Please create a new address");
                } else {
                    //get current address 
                    Address address = (Address) criteria2.list().get(0);

                    //******complete checkout process
                    saveOrders(session, user, address, responseObject, transaction);
                }

            } else {
                //create new address

                //validations
                if (firstName.isEmpty()) {
                    //check first name is empty
                    responseObject.addProperty("message", "Please Fill First Name!");

                } else if (lastName.isEmpty()) {
                    //check last name is empty
                    responseObject.addProperty("message", "Please Fill Last Name!");

                } else if (!Validations.isInteger(provinceId)) {
                    //check province is valid
                    responseObject.addProperty("message", "Invalid Province!");

                } else if (!Validations.isInteger(districtId)) {
                    //check district is valid
                    responseObject.addProperty("message", "Invalid District!");

                } else if (!Validations.isInteger(cityId)) {
                    //check city is valid
                    responseObject.addProperty("message", "Invalid City!");

                } else {
                    //check city from db

                    //search city from db
                    Criteria criteria3 = session.createCriteria(City.class);
                    criteria3.add(Restrictions.eq("id", Integer.valueOf(cityId)));

                    //check city found or not
                    if (criteria3.list().isEmpty()) {
                        //city not found
                        responseObject.addProperty("message", "Invalid City Selected!");
                    } else {
                        //city found

                        //get the selected city from db
                        City city = (City) criteria3.list().get(0);

                        // validations
                        if (addressLine1.isEmpty()) {
                            //check address line 1  is empty
                            responseObject.addProperty("message", "Please Fill Address Line 1!");

                        } else if (addressLine2.isEmpty()) {
                            //check address line 2  is empty
                            responseObject.addProperty("message", "Please Fill Address Line 2!");

                        } else if (postalCode.isEmpty()) {
                            //check postal code  is empty
                            responseObject.addProperty("message", "Please Fill Postal Code!");

                        } else if (postalCode.length() != 5) {
                            //check postal code length  is 5
                            responseObject.addProperty("message", "Invalid  Postal Code!");

                        } else if (!Validations.isInteger(postalCode)) {
                            //check postal code  is integer
                            responseObject.addProperty("message", "Invalid  Postal Code!");

                        } else if (mobile.isEmpty()) {
                            //check mobile  is empty
                            responseObject.addProperty("message", "Please Fill Mobile!");

                        } else if (!Validations.isMobileNumberValid(mobile)) {
                            //validate mobile number
                            responseObject.addProperty("message", "Invalid Mobile Number!");

                        } else {
                            //create newe address

                            Address address = new Address();
                            address.setCity(city);
                            address.setFirstName(firstName);
                            address.setLastName(lastName);
                            address.setLine1(addressLine1);
                            address.setLine2(addressLine2);
                            address.setMobile(mobile);
                            address.setPostalCode(postalCode);
                            address.setUser(user);

                            session.save(address);
                            //***complete the checkout

                            saveOrders(session, user, address, responseObject, transaction);
                        }

                    }

                }

            }

        } else {
            // user not sign in
            responseObject.addProperty("message", "User not Signed in");
        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseObject));

    }

    private void saveOrders(Session session, User user, Address address, JsonObject responseObject, Transaction transaction) {

        try {

            //create order in db
            Orders orders = new Orders();
            orders.setAddress(address);
            orders.setDateTime(new Date());
            orders.setUser(user);

            //save order id 
            int orderId = (int) session.save(orders);

            //get cart items
            Criteria criteria4 = session.createCriteria(Cart.class);
            criteria4.add(Restrictions.eq("user", user));
            List<Cart> cartList = (List<Cart>) criteria4.list();

            //get order status (1.payment pending) from db
            OrderStatus orderStatus = (OrderStatus) session.get(OrderStatus.class, 1);

            //create order item in db
            double amount = 0;
            String items = "";
            
            //reading searched cart list
            for (Cart cartItem : cartList) {
                
                //calculate amount
                amount += cartItem.getQty() * cartItem.getProduct().getPrice();
                
                //calculate amount with shipping
                if (address.getCity().getDistrict().getProvince().getId() == 9) {
                    amount += 1000;
                } else {
                    amount += 2000;
                }
                //calculate amount

                //get item details
                items += cartItem.getProduct().getTitle() + " X " + cartItem.getQty();
                //get item details
                
                //get product from cart item
                Product product = cartItem.getProduct();

                //create order item
                OrderItem orderItem = new OrderItem();
                orderItem.setOrders(orders);
                orderItem.setOrderStatus(orderStatus);
                orderItem.setProduct(product);
                orderItem.setQty(cartItem.getQty());
                //save order item
                session.save(orderItem);

                //update product qty in db
                product.setQty(product.getQty() - cartItem.getQty());
                session.update(product);

                //delete cart item from db
                session.delete(cartItem);
            }

            transaction.commit();

            //start set payment data
            JsonObject payhere = new JsonObject();

            String merchant_id = "1221052";
            String formatedAmount = new DecimalFormat("0.00").format(amount);
            String currency = "LKR";
            String merchantSecret = "MzI1NTgxMzMwNDM4NDA2NDc5NjQyNDQzMjc5MTU0MjYzNTI2ODE1";
            String merchantSecretMD5Hash = Payhere.generateMD5(merchantSecret).toUpperCase();

            payhere.addProperty("sandbox", true);
            payhere.addProperty("merchant_id", merchant_id);

            payhere.addProperty("return_url", "");
            payhere.addProperty("cancel_url", "");
            payhere.addProperty("notify_url", "");// /VerifyPayments

            payhere.addProperty("first_name", user.getFname());
            payhere.addProperty("last_name", user.getLname());
            payhere.addProperty("email", user.getEmail());
            payhere.addProperty("phone", address.getMobile());
            payhere.addProperty("address", address.getLine1()+""+address.getLine2());
            payhere.addProperty("city", address.getCity().getCity());
            payhere.addProperty("country", "Sri Lanka");
            payhere.addProperty("order_id", String.valueOf(orderId));
            payhere.addProperty("items", items);
            payhere.addProperty("currency", currency);
            payhere.addProperty("amount", formatedAmount);

            //generate md5 hash
            //mechantId + orderId +amountFormated + currency +merchantSecretMD5Hah
            String md5Hash = Payhere.generateMD5(merchant_id + orderId + formatedAmount + currency + merchantSecretMD5Hash).toUpperCase();
            payhere.addProperty("hash", md5Hash);
            //generate md5 hash
            //end set payment data
            responseObject.add("payhereJson", payhere);
            responseObject.addProperty("message", "Checkout Completed");
            responseObject.addProperty("success", true);  

        } catch (Exception e) {
            //roll back transaction
            transaction.rollback();
            e.printStackTrace();
        } finally {
            //close hibernate session
            session.close();
        }

    }

}
