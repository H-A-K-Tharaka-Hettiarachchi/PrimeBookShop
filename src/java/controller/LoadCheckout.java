/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.UserDTO;
import entity.Address;
import entity.Cart;
import entity.City;
import entity.District;
import entity.Province;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author ASUS
 */
@WebServlet(name = "LoadCheckout", urlPatterns = {"/LoadCheckout"})
public class LoadCheckout extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //create gson object
        Gson gson = new Gson();
        //create json object
        JsonObject jsonObject = new JsonObject();
        //set success false
        jsonObject.addProperty("success", false);

        //create http session
        HttpSession httpSession = request.getSession();

        //create hibernate session
        Session session = HibernateUtil.getSessionFactory().openSession();

        //check user from http session
        if (httpSession.getAttribute("user") != null) {
            //loged in

            try {
                //get user from http session
                UserDTO userDTO = (UserDTO) httpSession.getAttribute("user");

                //search user from db
                Criteria criteria1 = session.createCriteria(User.class);
                criteria1.add(Restrictions.eq("email", userDTO.getEmail()));
                User user = (User) criteria1.uniqueResult();

                //get user's last address from db
                Criteria criteria2 = session.createCriteria(Address.class);
                criteria2.add(Restrictions.eq("user", user));
                criteria2.addOrder(Order.desc("id"));
                criteria2.setMaxResults(1);
                Address address = (Address) criteria2.uniqueResult();

                //get province from db
                Criteria criteria3 = session.createCriteria(Province.class);
                criteria3.addOrder(Order.asc("province"));
                List<Province> provinceList = criteria3.list();

                //get district from db
                Criteria criteria4 = session.createCriteria(District.class);
                criteria4.addOrder(Order.asc("district"));
                List<District> districtList = criteria4.list();

                //get cities from db
                Criteria criteria5 = session.createCriteria(City.class);
                criteria5.addOrder(Order.asc("city"));
                List<City> cityList = criteria5.list();

                //get cart item from db
                Criteria criteria6 = session.createCriteria(Cart.class);
                criteria6.add(Restrictions.eq("user", user));
                List<Cart> cartList = criteria6.list();

                //pack province list in json object
                jsonObject.add("provinceList", gson.toJsonTree(provinceList));
                //pack district list in json object
                jsonObject.add("districtList", gson.toJsonTree(districtList));
                //pack city list in json object
                jsonObject.add("cityList", gson.toJsonTree(cityList));

                //pack cart items in json object
                for (Cart cart : cartList) {
                    cart.setUser(null);
                    cart.getProduct().setUser(null);
                }
                jsonObject.add("cartItemList", gson.toJsonTree(cartList));

                if (address != null) {

                    //pack address in json object
                    address.setUser(null);
                    jsonObject.add("address", gson.toJsonTree(address));

                    jsonObject.addProperty("success", true);

                } else {
                    jsonObject.addProperty("success", false);
                    jsonObject.addProperty("message", "You have not current Address : Please fill new address");
                }

                jsonObject.addProperty("success", true);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            //not loged in
            jsonObject.addProperty("message", "Not Signed In");
        }

        //set response content type
        response.setContentType("application/json");
        //write json object to response as json text
        response.getWriter().write(gson.toJson(jsonObject));
        //close hibernate session
        session.close();

    }
}
