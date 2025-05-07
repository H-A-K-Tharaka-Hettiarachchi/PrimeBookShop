/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import model.Mail;
import model.Validations;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author ASUS
 */
@WebServlet(name = "Register", urlPatterns = {"/Register"})
public class Register extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //Response DTO
        ResponseDTO responseDTO = new ResponseDTO();
        //GSON
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        //Load data to User DTO from Request Parameters
        final UserDTO userDTO = gson.fromJson(request.getReader(), UserDTO.class);

        //Validation
        if (userDTO.getFname().isEmpty()) {
            responseDTO.setContent("Please Enter Your First Name!");

        } else if (userDTO.getLname().isEmpty()) {
            responseDTO.setContent("Please Enter Your Last Name!");

        } else if (userDTO.getEmail().isEmpty()) {
            responseDTO.setContent("Please Enter Your Email!");

        } else if (!Validations.isEmailValid(userDTO.getEmail())) {
            responseDTO.setContent("Invalid Email!");

        } else if (userDTO.getPassword().isEmpty()) {
            responseDTO.setContent("Please Enter Your Password!");

        } else if (userDTO.getConfiremPassword().isEmpty()) {
            responseDTO.setContent("Please Enter Your Confirm Password!");

        } else if (!Validations.isPasswordValid(userDTO.getPassword())) {
            responseDTO.setContent("Invalid Password!(It must have digits,special characters,uppercase letters and be at least 8 characters long)");

        } else if (!userDTO.getPassword().equals(userDTO.getConfiremPassword())) {
            responseDTO.setContent("Your Password and Confirm Password not matching!");

        } else {

            //Connecting with database
            Session session = HibernateUtil.getSessionFactory().openSession();
            //Data transaction
            Transaction transaction = session.beginTransaction();

            //Criteria search from user entity
            Criteria criteria1 = session.createCriteria(User.class);
            //Filter with given email
            criteria1.add(Restrictions.eq("email", userDTO.getEmail()));

            try {
                //Is exists email
                if (!criteria1.list().isEmpty()) {
                    responseDTO.setContent("This Email Already Exists");
                } else {

                    //generate verification code
                    int code = (int) (Math.random() * 100000000);

                    //Create user entity object and adding the data
                    final User user = new User();
                    user.setBlockStatus(false);
                    user.setEmail(userDTO.getEmail());
                    user.setFname(userDTO.getFname());
                    user.setLname(userDTO.getLname());
                    user.setPassword(userDTO.getPassword());
                    user.setStatus(false);
                    user.setVerificationCode(String.valueOf(code));

                    /*send verification email
                    start*/
                    Thread sendMailThread = new Thread() {
                        @Override
                        public void run() {
                            Mail.sendMail(userDTO.getEmail(), "Prime Book Shop  Verification Code :" + user.getVerificationCode(),
                                    "<!DOCTYPE html>\n"
                                    + "<html lang=\"en\">\n"
                                    + "<head>\n"
                                    + "    <meta charset=\"UTF-8\">\n"
                                    + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                                    + "    <title>Verification Code</title>\n"
                                    + "    <style>\n"
                                    + "        body {\n"
                                    + "            margin: 0;\n"
                                    + "            padding: 0;\n"
                                    + "            font-family: Arial, sans-serif;\n"
                                    + "            background-color: #f4f4f4;\n"
                                    + "        }\n"
                                    + "        .container {\n"
                                    + "            width: 100%;\n"
                                    + "            max-width: 600px;\n"
                                    + "            margin: auto;\n"
                                    + "            background-color: #ffffff;\n"
                                    + "            border-radius: 8px;\n"
                                    + "            overflow: hidden;\n"
                                    + "            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\n"
                                    + "        }\n"
                                    + "        .header {\n"
                                    + "            background-color: #003366;\n"
                                    + "            padding: 20px;\n"
                                    + "            text-align: center;\n"
                                    + "            color: #ffffff;\n"
                                    + "        }\n"
                                    + "        .content {\n"
                                    + "            padding: 20px;\n"
                                    + "            text-align: center;\n"
                                    + "        }\n"
                                    + "        .code {\n"
                                    + "            font-size: 24px;\n"
                                    + "            font-weight: bold;\n"
                                    + "            color: #003366;\n"
                                    + "            margin: 20px 0;\n"
                                    + "        }\n"
                                    + "        .footer {\n"
                                    + "            background-color: #f4f4f4;\n"
                                    + "            padding: 20px;\n"
                                    + "            text-align: center;\n"
                                    + "            color: #666666;\n"
                                    + "            font-size: 14px;\n"
                                    + "        }\n"
                                    + "    </style>\n"
                                    + "</head>\n"
                                    + "<body>\n"
                                    + "    <div class=\"container\">\n"
                                    + "        <div class=\"header\">\n"
                                    + "            <h1>Verification Code</h1>\n"
                                    + "        </div>\n"
                                    + "        <div class=\"content\">\n"
                                    + "            <p>Hello,</p>\n"
                                    + "            <p>Thank you for registering with us. Please use the following code to verify your email address:</p>\n"
                                    + "            <div class=\"code\">\n"
                                    + "                " + user.getVerificationCode() + "\n"
                                    + "            </div>\n"
                                    + "            <p>If you did not request this, please ignore this email.</p>\n"
                                    + "        </div>\n"
                                    + "        <div class=\"footer\">\n"
                                    + "            <p>&copy; 2024 Prime Book Shop. All rights reserved.</p>\n"
                                    + "        </div>\n"
                                    + "    </div>\n"
                                    + "</body>\n"
                                    + "</html>");
                        }

                    };
                    sendMailThread.start();
                    /*send verification email
                    end*/

                    //Saving data 
                    session.save(user);
                    //Insert data to database
                    transaction.commit();

                    //Store user email in httpsession 
                    request.getSession().setAttribute("email", userDTO.getEmail());
                    //Response status success
                    responseDTO.setSuccess(true);
                    responseDTO.setContent("Registration Complete. Please Check Your Inbox for Verification Code!");
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //close the session
                session.close();
            }

        }
        //Response content type
        response.setContentType("application/json");
        //Convert response dto onject to json text
        response.getWriter().write(gson.toJson(responseDTO));
    }

}
