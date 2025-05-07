
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Author;
import entity.MainCategory;
import entity.Publisher;
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
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author ASUS
 */
@WebServlet(name = "LoadAddProduct", urlPatterns = {"/LoadAddProduct"})
public class LoadAddProduct extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //create gson
        Gson gson = new Gson();
        //create session
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        //search main category from db
        Criteria criteria1 = session.createCriteria(MainCategory.class);
        criteria1.addOrder(Order.asc("mainCategory"));
        List<MainCategory> mainCategoryList = criteria1.list();

        //search sub category from db
        Criteria criteria2 = session.createCriteria(SubCategory.class);
        criteria2.addOrder(Order.asc("subCategory"));
        List<SubCategory> subCategoryList = criteria2.list();

        //search  author from db
        Criteria criteria3 = session.createCriteria(Author.class);
        criteria3.addOrder(Order.asc("authorName"));
        List<Author> authorList = criteria3.list();

        //search publisher from db
        Criteria criteria4 = session.createCriteria(Publisher.class);
        criteria4.addOrder(Order.asc("publisherName"));
        criteria4.add(Restrictions.eq("blockStatus", false));
        List<Publisher> publisherList = criteria4.list();

        //create newe json object
        JsonObject jsonObject = new JsonObject();
        //adding data to json object
        jsonObject.add("mainCategoryList", gson.toJsonTree(mainCategoryList));
        jsonObject.add("subCategoryList", gson.toJsonTree(subCategoryList));
        jsonObject.add("authorList", gson.toJsonTree(authorList));
        jsonObject.add("publisherList", gson.toJsonTree(publisherList));

        //set response content type
        response.setContentType("application/json");
        //write the json object to response as json text
        response.getWriter().write(gson.toJson(jsonObject));
        //close the session
        session.close();

    }

}
