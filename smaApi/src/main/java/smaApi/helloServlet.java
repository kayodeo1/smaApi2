package smaApi;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/sayHello")
public class helloServlet extends HttpServlet{


	@Override
	public void service( HttpServletRequest req, HttpServletResponse res) throws IOException {

		String name = req.getParameter("name");

		PrintWriter out = res.getWriter();


		out.println("<html><head></head><body>");
		out.println("<h1 style=color:red;>Hello "+ name+"</h1>");
		out.print("</body></html>");



	}

}
