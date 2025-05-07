/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.UserDTO;
import entity.Author;
import entity.MainCategory;
import entity.Product;
import entity.Publisher;
import entity.SubCategory;
import entity.User;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
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
@MultipartConfig
@WebServlet(name = "AddProduct", urlPatterns = {"/AddProduct"})
public class AddProduct extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //create gson object
        Gson gson = new Gson();
        //create response json object
        JsonObject responseJsonObject = new JsonObject();
        //set success false
        responseJsonObject.addProperty("success", false);

        //store data in variables from request
        String bookTitle = request.getParameter("bookTitle");
        String bookMainCategoryId = request.getParameter("bookMainCategoryId");
        String bookSubCategoryId = request.getParameter("bookSubCategoryId");
        String bookAuthorId = request.getParameter("bookAuthorId");
        String bookPublishedDate = request.getParameter("bookPublishedDate");
        String bookPublisherId = request.getParameter("bookPublisherId");
        String bookQuantity = request.getParameter("bookQuantity");
        String bookPrice = request.getParameter("bookPrice");
        String bookDescription = request.getParameter("bookDescription");
        //store image
        Part bookImage = request.getPart("bookImage");
        //create session
        Session session = HibernateUtil.getSessionFactory().openSession();
        //create transaction
        Transaction transaction = session.beginTransaction();

        //validations
        if (bookTitle.isEmpty()) {
            //empty title
            responseJsonObject.addProperty("message", "Please Fill Book Name");

        } else if (!Validations.isInteger(bookMainCategoryId)) {
            //empty main category
            responseJsonObject.addProperty("message", "Invalid Main Category");

        } else if (!Validations.isInteger(bookSubCategoryId)) {
            //empty sub categoty
            responseJsonObject.addProperty("message", "Invalid Sub Category");

        } else if (!Validations.isInteger(bookAuthorId)) {
            //invalid author
            responseJsonObject.addProperty("message", "Invalid Author");

        } else if (bookPublishedDate.isEmpty()) {
            //invalid published date
            responseJsonObject.addProperty("message", "Invalid Date");

        } else if (!Validations.isInteger(bookPublisherId)) {
            //invalid publisher
            responseJsonObject.addProperty("message", "Invalid Publisher");

        } else if (bookImage.getSubmittedFileName() == null) {
            //empty image
            responseJsonObject.addProperty("message", "Please Upload Image");

        } else if (!Validations.isInteger(bookQuantity)) {
            //invalid qty
            responseJsonObject.addProperty("message", "Invalid Quantity");

        } else if (Integer.parseInt(bookQuantity) <= 0) {
            //invalid qty range
            responseJsonObject.addProperty("message", "Quantity must be greater than 0");

        } else if (bookQuantity.isEmpty()) {
            //empty qty
            responseJsonObject.addProperty("message", "Please Fill Quantity");

        } else if (bookPrice.isEmpty()) {
            //empty price
            responseJsonObject.addProperty("message", "Please Fill Price");

        } else if (!Validations.isDouble(bookPrice)) {
            //invalid price
            responseJsonObject.addProperty("message", "Invalid Price");

        } else if (Double.parseDouble(bookPrice) <= 0) {
            //invalid price range
            responseJsonObject.addProperty("message", "Price must be greater than 0");

        } else if (bookDescription.isEmpty()) {
            //empty description
            responseJsonObject.addProperty("message", "Please Fill Description");

        } else {
            //search main category from db
            MainCategory mainCategory = (MainCategory) session.get(MainCategory.class, Integer.valueOf(bookMainCategoryId));

            if (mainCategory == null) {
                //not found main category
                responseJsonObject.addProperty("message", "Please Select a Valid Main Category");
            } else {
                //found main category
                //search sub category from db
                SubCategory subCategory = (SubCategory) session.get(SubCategory.class, Integer.valueOf(bookSubCategoryId));

                if (subCategory == null) {
                    //not found sub category
                    responseJsonObject.addProperty("message", "Please Select a Valid Sub Category");
                } else {
                    //found sub category
                    //compaire id from selected main category id and searched sub category's main category
                    if (subCategory.getMainCategory().getId() != mainCategory.getId()) {
                        //not matched : invalid sub category
                        responseJsonObject.addProperty("message", "Please Select a Valid Sub Category");
                    } else {
                        //search author from db
                        Author author = (Author) session.get(Author.class, Integer.valueOf(bookAuthorId));

                        if (author == null) {
                            //not found author
                            responseJsonObject.addProperty("message", "Please Select a Valid Author");
                        } else {
                            //search publisher from db
                            Publisher publisher = (Publisher) session.get(Publisher.class, Integer.valueOf(bookPublisherId));

                            if (publisher == null) {
                                //not found publisher
                                responseJsonObject.addProperty("message", "Please Select a Valid Publisher");
                            } else {
                                //validate the published date isdate
                                if (!Validations.isDate(bookPublishedDate)) {
                                    //invalid date
                                    responseJsonObject.addProperty("message", "Invalid Date");
                                } else {
                                    //validations completed

                                    //create new product object
                                    Product product = new Product();
                                    //adding data to object
                                    product.setActive(true);//active status
                                    product.setAuthor(author);//author
                                    product.setDateTime(new Date());//register date time
                                    product.setDescription(bookDescription);//description
                                    product.setPrice(Double.parseDouble(bookPrice));//price

                                    //split year from selected published date and add to object
                                    product.setPublishedYear(String.valueOf(Validations.getYear(bookPublishedDate)));

                                    product.setPublisher(publisher);//publisher
                                    product.setQty(Integer.parseInt(bookQuantity));//qty
                                    product.setSubCategory(subCategory);//sub category
                                    product.setTitle(bookTitle);//title

                                    //get user from http session
                                    UserDTO userDTO = (UserDTO) request.getSession().getAttribute("user");
                                    //search user from db
                                    Criteria criteria1 = session.createCriteria(User.class);
                                    criteria1.add(Restrictions.eq("email", userDTO.getEmail()));
                                    User user = (User) criteria1.uniqueResult();
                                    //add user to product object
                                    product.setUser(user);

                                    //save the product id and insert saving
                                    int pid = (int) session.save(product);
                                    //product insert to the database
                                    transaction.commit();

                                    //get application real path
                                    String applicationPath = request.getServletContext().getRealPath("");
                                    //change the directry and make new one
                                    String newApplicationPath = applicationPath.replace("build" + File.separator + "web", "web");

                                    //create common folder for store product images
                                    File mainFolder = new File(newApplicationPath + "//product-images//");
                                    //create inner folder wise product
                                    File productFolder = new File(newApplicationPath + "//product-images//product" + pid);
                                    //save the folders
                                    mainFolder.mkdir();
                                    productFolder.mkdir();

                                    //save image in created folder
                                    File file1 = new File(productFolder, "image" + pid + ".png");
                                    InputStream inputStream1 = bookImage.getInputStream();
                                    Files.copy(inputStream1, file1.toPath(), StandardCopyOption.REPLACE_EXISTING);

                                    //success true
                                    responseJsonObject.addProperty("success", true);
                                    responseJsonObject.addProperty("message", "New Product Added");
                                }
                            }
                        }
                    }

                }
            }

        }
        //set response content type
        response.setContentType("application/json");
        //write response json object to response
        response.getWriter().write(gson.toJson(responseJsonObject));
        //close db session
        session.close();

    }

}
