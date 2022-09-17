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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet implementation class TopicDeletionServlet
 */
@WebServlet("/TopicDeletionServlet")
public class TopicDeletionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public TopicDeletionServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // session management
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            Person user = (Person) session.getAttribute("user");
            String role = user.getRole();
            if (role.equals("Admin") || role.equals("Assistant") || role.equals("Professor")) {

                //======================== DATA LOADING PART ========================
                List<Topic> topics = getTopics();

                request.setAttribute("role", user.getRole());
                request.setAttribute("topics", topics);
                request.getRequestDispatcher("topic_deletion.jsp").forward(request, response);
            } else {
                // the user is not admin, assistant or professor, redirect to the error page
                session.setAttribute("errorMessage", "Please check your user role.");
                request.getRequestDispatcher("no_access_page.jsp").forward(request, response);
            }
        } else {
            // the user is not logged in, redirect to the error page
            session.setAttribute("errorMessage", "Please log in first.");
            request.getRequestDispatcher("no_access_page.jsp").forward(request, response);
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

    private List<Topic> getTopics() {
        try (Connection con = DbUtils.getInstance().getConnection()) {
            if (con == null) {
                return null;
            }

            List<Topic> topics = new ArrayList<>();
            // get all topic list
            String query = "SELECT DISTINCT id, title, program_id, administr_validated, scientific_validated, confidential_internship "
                    + "FROM internship;";
            try (
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    ResultSet resultSet = preparedStatement.executeQuery();
            ) {
                while (resultSet.next()) {
                    Topic topic = new Topic(resultSet.getInt("id"),
                            resultSet.getString("title"),
                            resultSet.getInt("program_id"),
                            resultSet.getBoolean("administr_validated"),
                            resultSet.getBoolean("scientific_validated"),
                            resultSet.getBoolean("confidential_internship"));
                    topics.add(topic);
                }
            }

            return topics;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
