package edu.polytechnique.inf553;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Servlet implementation class UpdateProgramCategoryServlet
 */
@WebServlet("/UpdateProgramCategoryServlet")
public class UpdateProgramCategoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateProgramCategoryServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(this.getClass().getName() + " doGet method called with path " + request.getRequestURI() + " and parameters " + request.getQueryString());
		// session management
		HttpSession session = request.getSession(false);
		if(session!=null && session.getAttribute("user")!= null) {
			Person user = (Person)session.getAttribute("user");
			String role = user.getRole();
			if (role.equals("Admin") || role.equals("Professor")  ) {
				String type = request.getParameter("type");
				int pid = Integer.parseInt(request.getParameter("pid"));
				int cid = Integer.parseInt(request.getParameter("cid"));
				try (Connection con = DbUtils.getConnection()) {
					if (con == null) {
						response.sendError(HttpServletResponse.SC_FORBIDDEN);
					}
					String query;
					// update user program, set isolation level SERIALIZABLE
					if (type.equals("add")) {
						// add program
						query = "insert into program_category(program_id, cat_id) values (?,?)";
					} else {
						// delete program
						query = "DELETE FROM program_category WHERE program_id = ? AND cat_id = ?";
					}
					try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, pid);
            ps.setInt(2, cid);
            ps.executeUpdate();
          }
					
				} catch(SQLException e) {
					e.printStackTrace();
					// query errors
					response.sendError(HttpServletResponse.SC_FORBIDDEN);
				}
				
				response.setStatus( 200 );
			}else {
				// the user is not admin, redirect to the error page
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
			}
		}else {
			// the user is not logged in, redirect to the error page
			response.setStatus( HttpServletResponse.SC_FORBIDDEN );
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
