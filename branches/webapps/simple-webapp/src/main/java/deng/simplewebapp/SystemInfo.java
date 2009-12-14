package deng.simplewebapp;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

public class SystemInfo extends HttpServlet {
  @lombok.Data
  public static class PageData {
    private Properties sysprops = System.getProperties();
    private Date timestamp = new Date();
    private String webappDirectory;
  }
  
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    ServletContext ctx = getServletContext();
    PageData pageData = new PageData();
    pageData.setWebappDirectory(ctx.getRealPath("/"));
    req.setAttribute("pageData", pageData);
    req.getRequestDispatcher("/systeminfo.jsp").forward(req, resp);
  }
}
