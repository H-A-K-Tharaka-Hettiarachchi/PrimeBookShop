package filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ASUS
 */
@WebFilter(urlPatterns = {"/my-account.html","/add-product.html","/checkout.html"})
public class FilterIsLogedIn implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }
    //filtering is user has loged in or not
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        //cast servlet request to http servlet request
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
         //cast servlet response to http servlet response
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        //check user in http session
        if (httpServletRequest.getSession().getAttribute("user") != null) {
            //user found
            //continue the request 
            chain.doFilter(request, response);
        } else {
            //not found
            //redirect to login page
            httpServletResponse.sendRedirect("login.html");
        }

    }

    @Override
    public void destroy() {

    }

}
