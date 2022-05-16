package net.usermanagement.dao;

import java.awt.List;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.*;
import com.mysql.cj.protocol.Resultset;

import net.usermanagement.model.User;

public class UserDAO {

	//DAO stands for data acess object , it is a design patter used to seprate all the database related operations.
	//	This dao class provides CRUD database operations for table users in the database 
	
	
	String url ="jdbc:mysql://localhost:3306/demo";
	String usernamee = "root";
	String password = "root";
	
	private static final String INSERT_USERS_SQL = "INSERT INTO user" + "  (name,email,country) " + " VALUES (?,?,?);";	

	private static final String SELECT_USER_BY_ID = " SELECT id,name,email,country FROM user WHERE id =?";
	private static final String SELECT_ALL_USER = " SELECT * from user";
	private static final String DELETE_USER_SQL = " DELETE FROM user where id=?";
	private static final String UPDATE_USER_SQL = "UPDATE user SET name =?,email =?,country = ? where id=?";

	
	protected Connection getConnection() throws ClassNotFoundException, SQLException {
		Connection connection = null;
 		try {
			
			Class.forName("com.mysql.jdbc.Driver");
		 connection = DriverManager.getConnection(url, usernamee, password);
		}
			 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		catch (ClassNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		//problem with returning connection object,fix it its not correct timestamp 16.20
		
		return connection ;
		
	}
	
	
	
//CRUD OPERATIONS
	
//*******Create or insert user
	public void insertUser(User user) {
		
		try 
			
			(Connection connection =getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL);){
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getCountry());
			
			preparedStatement.executeUpdate();
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
//****UPDATE USER
	
	public boolean updateUser(User user) throws SQLException, ClassNotFoundException{
		
		boolean rowUpdated = false ;
		try (Connection connection =getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_SQL);){
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getCountry());
			preparedStatement.setInt(4, user.getId());
			
			rowUpdated= preparedStatement.executeUpdate() >0;
			}
		
	//20.00 timestamp to solve this error related to brackets refer java guides repo for code	
	
	return rowUpdated;
		}
	
	//select user by Id
	
	public User selectUser(int id) throws ClassNotFoundException, SQLException {
		
		User user = null;
		//1.Establish connection
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);)  {
			preparedStatement.setInt(1, id);
			System.out.println(preparedStatement);
		ResultSet rs =preparedStatement.executeQuery();
		
		while(rs.next()) {
			
		String name =rs.getString("name");
			String email =rs.getString("email");
			String country = rs.getString("country");
			user = new User(name, email, country);
		}
	}
			catch (SQLException e) {
				e.printStackTrace();
			}
			
		return user;
	}
	
	//Select users
	
	
	public java.util.List<User> selectAllUsers() throws ClassNotFoundException, SQLException {
		
		java.util.List<User> users = new ArrayList<>(); 		
		//1.Establish connection
		try (Connection connection = getConnection();
				
				//2.Create a statement using connection object
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);)  {
			System.out.println(preparedStatement);
			
			//3.Execute or update query
		ResultSet rs =preparedStatement.executeQuery();
		
		while(rs.next()) {
			
			int id =rs.getInt("id");
			String name =rs.getString("name");
			String email =rs.getString("email");
			String country = rs.getString("country");
			users.add(new User(name, email, country));
		}
	}
			catch (SQLException e) {
				e.printStackTrace();
			}
			
		return users;
	}
	
	
	
	//Delete users
	
	public boolean deleteUser(int id) throws ClassNotFoundException, SQLException {
		
		boolean rowDeleted;
		try
		(Connection connection = getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_SQL);) {
			preparedStatement.setInt(1, id);
		rowDeleted = preparedStatement.executeUpdate() >0; //gives a error cannot convert from int to boolean so add > 0
				
		}	
		return rowDeleted;
	}
	
	}
