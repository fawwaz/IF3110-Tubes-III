<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>
<%@ include file= "./header.jsp" %>
<form name="edit" action="verifyEdit" method="post">
<%
try {
	  //Load the JDBC driver
			String uname = "root";
			String pass = "";
			String url = "jdbc:mysql://localhost/progin_13511059";
			Class.forName ("com.mysql.jdbc.Driver").newInstance ();
	        Connection con = DriverManager.getConnection (url, uname, pass);
		
		  
	  //Create a Statement object and call its executeUpdate 
	  //method to insert a record
	  Statement s = con.createStatement();
	  String sql = "SELECT * FROM user WHERE username ='"+session.getAttribute("username")+"'";
	  ResultSet rs = s.executeQuery(sql);
	  while (rs.next()) {
		out.println("Change Password: <input type='password' onkeyup='checkPass(this)' name='password' value='"+rs.getString(3)+"'><div id='err_pass'></div><br>");
	    out.println("Confirm Password: <input type='password' onkeyup='confirmPassword(this)' name='repassword' value='"+rs.getString(3)+"'><div id='err_repass'></div><br>");
		out.println("Nama Lengkap: <input type='text' name='fullname' onkeyup='checkFullName(this)' value='"+ rs.getString(2)+"'><div id='err_fullname'></div><br>");
		out.println("Nomor Hand Phone: <input type='text' name='hpnum' value='"+ rs.getString(5)+"'><br>");
		out.println("Alamat : <input type='text' name='address' value='"+ rs.getString(6)+"'><br>");
		out.println("Provinsi : <input type='text' name='province' value='"+ rs.getString(7)+"'><br>");
		out.println("Kecamatan : <input type='text' name='kecamatan' value='"+ rs.getString(8)+"'><br>");
		out.println("Kode Pos : <input type='text' name='postalcode' value='"+ rs.getString(9)+"'><br>");
		out.println("<input type='submit' id='subedit' value='Edit'>");
		out.println("<div id='edit_error'></div>");
	  }
	  rs.close();
	  s.close();
	  con.close();
	}
	catch (ClassNotFoundException e1) {
	  // JDBC driver class not found, print error message to the console
	  System.out.println(e1.toString());
	}
	catch (SQLException e2) {
	  // Exception when executing java.sql related commands, print error message to the console
	  System.out.println(e2.toString());
	}
	catch (Exception e3) {
	  // other unexpected exception, print error message to the console
	  System.out.println(e3.toString());
	}
%>
</form>
</body>
</html>