/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.ResponseDTO;
import dto.UserDTO;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "Verification", urlPatterns = {"/Verification"})
public class Verification extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //Response DTO
        ResponseDTO responseDTO = new ResponseDTO();
        //GSON
        Gson gson = new Gson();
        //Load data to  jsonObject from Request Parameters
        JsonObject dto = gson.fromJson(request.getReader(), JsonObject.class);
        String verificationCode = dto.get("verificationCode").getAsString();
        //Connecting with database
        Session session = HibernateUtil.getSessionFactory().openSession();
        //Data transaction
        Transaction transaction = session.beginTransaction();

        try {

            //check email from httpsession
            if (request.getSession().getAttribute("email") != null) {

                //store email from httpsession
                String email = request.getSession().getAttribute("email").toString();

                //Criteria search from user entity
                Criteria criteria1 = session.createCriteria(User.class);
                //Filter with given email
                criteria1.add(Restrictions.eq("email", email));
                //Filter with given verification code
                criteria1.add(Restrictions.eq("verificationCode", verificationCode));

                //checking is user found
                if (!criteria1.list().isEmpty()) {

                    //get the user
                    User user = (User) criteria1.list().get(0);
                    //update user status = true
                    user.setStatus(true);

                    //update data 
                    session.update(user);
                    //update data to database
                    transaction.commit();

                    //create user dto
                    UserDTO userDTO = new UserDTO();
                    //set data to user dto
                    userDTO.setFname(user.getFname());
                    userDTO.setLname(user.getLname());
                    userDTO.setEmail(email);
                    //remove email from httpsession
                    request.getSession().removeAttribute("email");
                    //set user to httpsession
                    request.getSession().setAttribute("user", userDTO);

                    //Response status success
                    responseDTO.setSuccess(true);
                    responseDTO.setContent("Verification Success");

                } else {
                    responseDTO.setContent("Invalid Verification Code");
                }

            } else {
                responseDTO.setContent("Verification unavailable. Please Log In");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        //Response content type
        response.setContentType("application/json");
        //Convert response dto onject to json text
        response.getWriter().write(gson.toJson(responseDTO));
    }

}
