package edu.polytechnique.inf553;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.List;

/*
 * Test out the hack! (only works if forgotten to put the '?' instead of the strings concatenation) Put this in passwords in SigninServlet :
 * pppppppp'); drop table error; insert into person(name, email, creation_date, valid, password) values ('fds, aaaa', 'fds.aaaa@gmail.com', '2020-12-14', false, '12345678
 */

@MultipartConfig(maxFileSize = 16177215)    // upload file's size up to 16MB
public class UploadSlidesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor of UploadTopicServlet
     * Query the programs and their categories
     */
    public UploadSlidesServlet() {
        super();
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println(this.getClass().getName() + " doGet method called with path " + request.getRequestURI());
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doPost called with parameter " + request.getQueryString());

        int studentId = Integer.parseInt(request.getParameter("studentId"));
        Part file = request.getPart("slides");

        try (Connection con = DbUtils.getInstance().getConnection()) {
            if (con == null) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }

            // update the slides
            String query = "UPDATE person SET slides = ?, timestamp_slides = ? WHERE id = ?;";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                InputStream inputStream = file.getInputStream();
                if (inputStream != null) {
                    ps.setBinaryStream(1, inputStream);
                    ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                    ps.setInt(3, studentId);
                    int row = ps.executeUpdate();
                    if (row <= 0) {
                        System.out.println("ERROR: File was not uploaded and saved into database");
                    }
                } else {
                    System.out.println("input stream is null");
                }
            }

            // get the internship to display it in the student view
            request.setAttribute("userTopic", CommonInterface.getTopicOfUser(studentId, con));
            // also set the list of programs of the user as well as the categories of each program
            List<Program> programs = CommonInterface.getProgramsOfUser(studentId, con);
            request.setAttribute("programs", programs);
            request.setAttribute("topicsPerCategory", CommonInterface.getTopicsPerCategory(programs, con));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        request.getRequestDispatcher("student_view.jsp").forward(request, response);
    }
}
