package net.usermanagement.web.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.usermanagement.dao.UserDAO;
import net.usermanagement.model.User;


@WebServlet("/")
public class UserServletController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       private UserDAO userDAO;
    
    public UserServletController() {
 
        super();
        this.userDAO =new UserDAO();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
	String action =request.getServletPath();
	
	
	
	switch (action) {
	case "/new":
		showNewForm(request, response);
		break;
	case "/insert": 
		insertUser(request, response);
	break;
	case "/delete": 
		try {
			deleteUser(request, response);
		} catch (ClassNotFoundException | SQLException | IOException e) {
			
			e.printStackTrace();
		}
		break;
	case "/edit": 
		try {
			showEditForm(request, response);
		} catch (ClassNotFoundException | SQLException | ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		break;
	case "/update": 
		try {
			updateUser(response, request);
		} catch (ClassNotFoundException | SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		break;
		
		default:
		try {
			listUser(request, response);
		} catch (ClassNotFoundException | SQLException | ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			break;
		}
	
	}
	
	private void showNewForm(HttpServletRequest request, HttpServletResponse responce) throws ServletException, IOException {
		
		
		
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("user-form.jsp");
		requestDispatcher.forward(request, responce);
	}
	
	private void insertUser(HttpServletRequest request, HttpServletResponse responce) throws IOException {
		
		String name =request.getParameter("name");
		String email =request.getParameter("email");
		String country = request.getParameter("country");
		User newuser=new User(name, email, country);
		userDAO.insertUser(newuser);
		responce.sendRedirect("list");
		
	}
	
	
	private void deleteUser(HttpServletRequest request, HttpServletResponse responce) throws ClassNotFoundException, SQLException, IOException {
		
		int id =Integer.parseInt(request.getParameter("id")); 
		userDAO.deleteUser(id);
		responce.sendRedirect("list");//why list here we delete the records then  we redirect it to the list page, hence.
		
		
	}
	
	
	public void showEditForm(HttpServletRequest request,HttpServletResponse responce) throws ClassNotFoundException, SQLException, ServletException, IOException {
		
		
		int id =Integer.parseInt(request.getParameter("id"));
		User existingUser = userDAO.selectUser(id);
		RequestDispatcher dispatcher =request.getRequestDispatcher("user-form.jsp");
		request.setAttribute("user", existingUser);
		dispatcher.forward(request, responce);
		
	}
	
	private void updateUser(HttpServletResponse responce, HttpServletRequest request) throws ClassNotFoundException, SQLException, IOException {
		
		
		int id = Integer.parseInt(request.getParameter("id")) ;
		String name =request.getParameter("name");
		String email = request.getParameter("email");
		String country = request.getParameter("country");
		
		User user = new User(name, email, country);
		userDAO.updateUser(user);
		responce.sendRedirect("list");
	}	

	private void listUser(HttpServletRequest request, HttpServletResponse responce) throws ClassNotFoundException, SQLException, ServletException, IOException {
		
		List<User> listUser = userDAO.selectAllUsers();
		request.setAttribute("listUser",listUser);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("user-list.jsp");
		requestDispatcher.forward(request, responce);
		
		
		
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		this.doGet(request, response);
	}

}
