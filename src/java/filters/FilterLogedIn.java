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
@WebFilter(urlPatterns = {"/login.html", "/register.html"})
public class FilterLogedIn implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    //filtering if user has loged in
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        //cast servlet request to http servlet request
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        //cast servlet response to http servlet response
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

         //check user in http session
        if (httpServletRequest.getSession().getAttribute("user") == null) {
             //user not found
             //continue the request
            chain.doFilter(request, response);
        } else {
            //user found
            //redirect to home page
            httpServletResponse.sendRedirect("index.html");
        }
    }

    @Override
    public void destroy() {

    }

}
