package com.vsics.fs;
//This is for testing
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//This is written by ayush
public class DeleteStudentServlet extends HttpServlet {
	// define all variable at top
	ServletContext sc = null;
	String driver = null;
	String url = null;
	String user = null;
	String password = null;
	Connection con = null;
	String DELETE_STD_BY_ID = null;
	PreparedStatement ps = null;
	PrintWriter pw = null;

	@Override
	public void init() throws ServletException {
		// read values from web.xml
		sc = getServletContext();

		driver = sc.getInitParameter("driver");
		url = sc.getInitParameter("url");
		user = sc.getInitParameter("user");
		password = sc.getInitParameter("pass");

		// load the driver class
		try {
			Class.forName(driver);

			// create the Connection object
			con = DriverManager.getConnection(url, user, password);

			// prepare SQl query
			DELETE_STD_BY_ID = "delete from student where sid=?";

			// get Precompiled sql query or PreparedStatement object
			if (con != null) {
				ps = con.prepareStatement(DELETE_STD_BY_ID);
			}
			// known exception
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} // handle unknown exception
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// set content type
		res.setContentType("text/html");
		// get PrintWriter object
		pw = res.getWriter();

		// read form data
		String id = req.getParameter("sid");
		int sid = Integer.parseInt(id);

		// set values to place holder or place resolver or ?
		int result = 0;
		try {
			if (ps != null) {

				ps.setInt(1, sid);

				// execute sql query
				result = ps.executeUpdate();
			}

			if (result == 0) {
				pw.println("<h1> Record not deleted for " + sid + "</h1>");
			} else {
				pw.println("<h1> Record  deleted for " + sid + "</h1>");
			}
			// Handling known exception
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// handling unknown exception
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void destroy() {
		// close all the connections in reverse order

		try {
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pw.close();
	}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
}
