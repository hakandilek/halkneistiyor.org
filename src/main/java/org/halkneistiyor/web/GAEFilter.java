package org.halkneistiyor.web;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Erdinc Yilmazel (eyilmazel@tripadvisor.com)
 * @since 6/11/13
 */
public class GAEFilter implements Filter
{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        HttpServletRequest req = (HttpServletRequest) request;
        String requestURI = req.getRequestURI();
        if (requestURI.startsWith("/_ah"))
        {
            request.getRequestDispatcher(requestURI).forward(request, response);
        }
        else
        {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy()
    {
    }
}
