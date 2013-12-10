package Data;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tubesII.wbd.GlobalConfig;

import com.google.gson.Gson;

public class barang extends HttpServlet{
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try{
			/*EXAMPLE CODE*/
			/*Used for query .. DONT CHANGE THIS LINES*/
			String action=request.getParameter("action");
			String parameters=request.getParameter("parameters").trim();
			String parameter[]=parameters.split(",");
			GlobalConfig GC = new GlobalConfig();
			Class.forName ("com.mysql.jdbc.Driver").newInstance ();
			//Connection con = DriverManager.getConnection (GC.geturl(), GC.getuser(), GC.getpass());
			Connection con = DriverManager.getConnection ("jdbc:mysql://localhost/progin_13511059", "root", "");
			PrintWriter out =  response.getWriter();
			// End OF DONT CHANGE
			
			
			response.setContentType("text/html");
	        response.setHeader("Cache-control", "no-cache, no-store");
	        response.setHeader("Pragma", "no-cache");
	        response.setHeader("Expires", "-1");
			response.setHeader("Access-Control-Allow-Origin","*");
			response.setHeader("Access-Control-Allow-Methods","GET,POST,PUT,DELETE,OPTIONS");
			response.setHeader("Access-Control-Allow-Headers","X-Requested-With, Content-Type, Content-Length");
			out.print("action :" + action + parameters);
			if(action.equals("cari")){
				// TODO customize for funtion 
				// FORMAT INPUT : action = register & parameters = username , nama_lengkap , password, email ,handphone , address , province , state , postcode , n_pembelian
				// FORMAT OUTPUT : {Status_operasi: Success/Failed}
				// Silahakan GOoogling explorasi syntax SQL untuk insert, soalnya beda sama con.executeSQL kalau gak salah con.updateSQL
				
				Statement statement = con.createStatement();
				
				String name = parameter[0];
				String harga = parameter[1];
				String kategori = parameter[2];
				Integer laman=Integer.parseInt(parameter[3]);
				Integer i=(laman-1)*10;
				String order=parameter[4];
				out.print("action :" + name+harga+kategori+laman+i+order);
				String n_item_query=new String();
				String search_query=new String();
				
				if (!name.equals("")) {
					if(!harga.equals("")){ //ada harga
						if(!kategori.equals("")){
							n_item_query = "SELECT COUNT(nama_barang) AS n_item FROM barang WHERE nama_barang like '%"+name+"%' AND harga_barang BETWEEN 0 AND "+harga+" AND kategori_barang = "+kategori;
							search_query = "SELECT * FROM barang WHERE nama_barang like '%"+name+"%' AND harga_barang BETWEEN 0 AND "+harga+" AND kategori_barang = "+kategori+" ORDER BY "+order+" ASC LIMIT "+i+", 10";
						}else { //kategori kosong
							n_item_query = "SELECT COUNT(nama_barang) AS n_item FROM barang WHERE nama_barang like '%"+name+"%' AND harga_barang BETWEEN 0 AND "+harga;
							search_query = "SELECT * FROM barang WHERE nama_barang like '%"+name+"%' AND harga_barang BETWEEN 0 AND "+harga+" ORDER BY "+order+" ASC LIMIT "+i+", 10";
						}
					}else{ //harga kosong
						if(!kategori.equals("")) {
							 n_item_query = "SELECT COUNT(nama_barang) AS n_item FROM barang WHERE nama_barang like '%"+name+"%' AND kategori_barang = "+kategori;
							 search_query = "SELECT * FROM barang WHERE nama_barang like '%"+name+"%' AND kategori_barang = "+kategori+" ORDER BY "+order+" ASC LIMIT "+i+", 10";
						} else {
							 n_item_query = "SELECT COUNT(nama_barang) AS n_item FROM barang WHERE nama_barang like '%"+name+"%'";
							 search_query = "SELECT * FROM barang WHERE nama_barang like '%"+name+"%' ORDER BY "+order+" ASC LIMIT "+i+", 10";
						}
						
					}
				}else{// nama kosong
					if(!harga.equals("")){ //ada harga
						if(!kategori.equals("")){ //ada kategori
							n_item_query = "SELECT COUNT(nama_barang) AS n_item FROM barang WHERE harga_barang BETWEEN 0 AND "+harga+" AND kategori_barang = "+kategori;
							search_query = "SELECT * FROM barang WHERE harga_barang BETWEEN 0 AND "+harga+" AND kategori_barang = "+kategori+" ORDER BY "+order+" ASC LIMIT "+i+", 10";
						}else { //kategori kosong
							n_item_query = "SELECT COUNT(nama_barang) AS n_item FROM barang WHERE harga_barang BETWEEN 0 AND "+harga;
							search_query = "SELECT * FROM barang WHERE harga_barang BETWEEN 0 AND "+harga+" ORDER BY "+order+" ASC LIMIT "+i+", 10";
						}
					} else { //gak ada harga
						if(!kategori.equals("")) {
							n_item_query = "SELECT COUNT(nama_barang) AS n_item FROM barang WHERE kategori_barang = "+kategori;
							search_query = "SELECT * FROM barang WHERE kategori_barang = "+kategori+" ORDER BY "+order+" ASC LIMIT "+i+", 10";
						}
					}
				}
				

				
				Statement s1 = con.createStatement();
				Statement s2 = con.createStatement();
				ResultSet rs1 = s1.executeQuery(n_item_query);
				ResultSet rs2 = s2.executeQuery(search_query);
				
				Integer n_item=0;
				ArrayList<barang_data> Hasil_search=new ArrayList<>();
				while(rs1.next()){
					n_item = Integer.parseInt(rs1.getString(1));  
				}

				if(n_item.equals(0)){
					out.print("{\"status\":\"success\",\"hasil\":\"[]\"}");
				}else{
					while (rs2.next()) {
					  	barang_data b=new barang_data();
					  	b.setId_barang(rs2.getObject(1).toString());
					  	b.setNama_barang(rs2.getObject(2).toString());
					  	b.setGambar_barang(rs2.getObject(3).toString());
					  	b.setHarga_barang(rs2.getObject(4).toString());
					  	b.setKategori_barang(rs2.getObject(5).toString());
					  	b.setN_beli(rs2.getObject(6).toString());
					  	b.setKeterangan(rs2.getObject(7).toString());
					  	b.setStok(rs2.getObject(8).toString());
					  	Hasil_search.add(b);
					  	
			  		}
				}
				
				
			  int nextLaman = laman+1;
			  int prevLaman = laman-1;
			  
			  Gson gson = new Gson();
			  String output_search=gson.toJson(Hasil_search);
			  out.print("{\"status\":\"success\",\"link\":{\"name\":\""+name+"\",\"harga\":\""+harga+"\",\"next_laman\":\""+nextLaman+"\",\"prev_laman\":\""+prevLaman+"\",\"kategori\":\""+kategori+"\",\"n_item\":\""+n_item+"\"},\"output_search\":\""+output_search+"\"}");
			  /*if (n_item > 10) {
					if (laman == 1) {
						out.print("{\"status\":\"success\",\"link\":{\"name\":\""+name+"\",\"harga\":\""+harga+"\",\"next_laman\":\""+nextLaman+"\",\"prev_laman\":\""+prevLaman+"\",\"kategori\":\""+kategori+"\",\"n_item\":\""+n_item+"\"},\"output_search\":\""+output_search+"\"}");
					}
					else if (laman >= n_item/10) {
						out.print("{\"status\":\"success\",\"link\":{\"name\":\""+name+"\",\"harga\":\""+harga+"\",\"next_laman\":\""+nextLaman+"\",\"prev_laman\":\""+prevLaman+"\",\"kategori\":\""+kategori+"\",\"n_item\":\""+n_item+"\"},\"output_search\":\""+output_search+"\"}");
					} else {
						
					}
					
				}*/
				
				
				
			}else if(action.equals("update")){
			
				Statement statement = con.createStatement();
				
				String username = parameter[0];
				String fullname = parameter[1];
				String password = parameter[2];
				String email = parameter[3];
				String hpnum = parameter[4];
				String address = parameter[5];
				String province = parameter[6];
				String kecamatan = parameter[7];
				String postcode = parameter[8];
				
				String query = "UPDATE `user` SET nama_lengkap='"+fullname+"', password='"+password+"', handphone="+hpnum+", address='"+address+"', province='"+province+"', state='"+kecamatan+"', postcode ="+postcode+", email='"+email+"'  WHERE username= '"+ username+"'";
				
				
				int status = statement.executeUpdate(query);
				
				if(status==1){
					out.print("{ \"Status_operasi\" : \"berhasil\" }");
				}else{
					out.print("{ \"Status_operasi\" : \"gagal\" }");
				}
				
			}else if(action.equals("login")){
			
				Statement statement = con.createStatement();
				
				String username = parameter[0];
				String password = parameter[1];
				boolean status = false;
				
				ResultSet rs = null;
				
				rs = statement.executeQuery("SELECT * FROM user where username ='"+username+"' AND password = '"+password+"'");
				status = rs.next();
				
				if (status)
					out.print("{ \"Status_operasi\" : \"berhasil\" , \"Username\" : \""+username+"\"}");
				else
					out.print("{ \"Status_operasi\" : \"gagal\" }");
					
			}else if(action.equals("find")){
				Statement statement = con.createStatement();
				
				String username = parameter[0];
				
				boolean status = false;
				//ResultSet rs = null;
				
				ResultSet rs = statement.executeQuery("SELECT * FROM user where username ='"+username+"'");
				
				
				
				
				/*
				ArrayList<String> HasilQuery_username= new ArrayList<>();
				ArrayList<String> HasilQuery_nama_lengkap= new ArrayList<>();
				ArrayList<String> HasilQuery_password= new ArrayList<>();
				ArrayList<String> HasilQuery_email= new ArrayList<>();
				ArrayList<String> HasilQuery_handphone= new ArrayList<>();
				ArrayList<String> HasilQuery_address= new ArrayList<>();
				ArrayList<String> HasilQuery_province= new ArrayList<>();
				ArrayList<String> HasilQuery_state= new ArrayList<>();
				ArrayList<String> HasilQuery_postcode= new ArrayList<>();
				*/
				ArrayList<user_data> List_User_data = new ArrayList<>();

				while(rs.next()){
					user_data UD = new user_data();
					
					out.print(rs.getObject(1).toString());
					
					UD.setUsername(rs.getObject(1).toString());
					UD.setNama_lengkap(rs.getObject(2).toString());
					UD.setPassword(rs.getObject(3).toString());
					UD.setEmail(rs.getObject(4).toString());
					UD.setHandphone(rs.getObject(5).toString());
					UD.setAddress(rs.getObject(6).toString());
					UD.setProvince(rs.getObject(7).toString());
					UD.setState(rs.getObject(8).toString());
					UD.setPostcode(rs.getObject(9).toString());
					UD.setN_pembelian(rs.getObject(10).toString());

					

					List_User_data.add(UD);
					/*
					HasilQuery_username.add();
					HasilQuery_nama_lengkap.add((String) rs.getObject(2));
					HasilQuery_password.add((String) rs.getObject(3));
					HasilQuery_email.add((String) rs.getObject(4));
					HasilQuery_handphone.add((String) rs.getObject(5));
					HasilQuery_address.add((String) rs.getObject(6));
					HasilQuery_province.add((String) rs.getObject(7));
					HasilQuery_state.add((String) rs.getObject(8));
					HasilQuery_postcode .add((String) rs.getObject(9));
					*/
				}
				
				
				//Convert Java Object into JSON object
				Gson gson = new Gson();
				
				String output = gson.toJson(List_User_data);
				status = List_User_data.isEmpty();
				if (!status)
					out.print("{ \"Status_operasi\" : \"berhasil\" , \"hasil\" : "+output+"  }");
				else
					out.print("{ \"Status_operasi\" : \"gagal\" }");
				
			}
		}catch(Exception e){
			
		}
	}
}
