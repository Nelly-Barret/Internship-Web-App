package edu.polytechnique.inf553;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor (see InternshipServlet)
	 */
	public HomeServlet() {
		super();	
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// session management
		HttpSession session = request.getSession(false);
		if(session!=null && session.getAttribute("user")!= null) {
			Person user = (Person)session.getAttribute("user");
			String role = user.getRole();
			request.getRequestDispatcher("dashboard.jsp").forward(request, response);
			
		}else {
			request.getRequestDispatcher("home.jsp").forward(request, response);
		}
	}
}
