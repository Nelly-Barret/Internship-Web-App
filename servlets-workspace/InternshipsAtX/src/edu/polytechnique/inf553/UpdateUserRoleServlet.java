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
 * Servlet implementation class UpdateUserInfo
 */
@WebServlet("/UpdateUserRoleServlet")
public class UpdateUserRoleServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public UpdateUserRoleServlet() {
        super();
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println(this.getClass().getName() + " doGet method called with path " + request.getRequestURI() + " and parameters " + request.getQueryString());
        // session management
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            Person user = (Person) session.getAttribute("user");
            String role = user.getRole();
            if (role.equals("Admin")) {
                int rid = Integer.parseInt(request.getParameter("rid"));
                int pid = Integer.parseInt(request.getParameter("pid"));
                Connection con = DbUtils.getInstance().getConnection();
                try {
                    if (con == null) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    }

                    // update user role, set isolation level SERIALIZABLE
                    String query = "UPDATE person_roles SET role_id = ? WHERE person_id = ?";
                    try (PreparedStatement ps = con.prepareStatement(query)) {
                        ps.setInt(1, rid);
                        ps.setInt(2, pid);
                        ps.executeUpdate();
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    DbUtils.getInstance().releaseConnection(con);
                }

                response.setStatus(200);
            } else {
                // the user is not admin, redirect to the error page
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
        } else {
            // the user is not logged in, redirect to the error page
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
