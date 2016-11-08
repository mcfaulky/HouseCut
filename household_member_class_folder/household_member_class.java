package com.housecut.housecut.household_member_class_folder;
//Code by: Adam Faulkner
//Debugging by: Jose Fernandes
//10/09/2016
//class testing, V.1.0

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLEncoder; 


/*House member class*/

//"http://housecut-145314.appspot.com/"

public class household_member_class {


		protected String current_household;
		protected String user_name;
		protected String name;
		protected String request = "http://housecut-145314.appspot.com/";	//this might not be needed
		protected int ID;
		protected String password;
		protected String email;
		protected String role = "member"; // roles can be member or admin
	
	

	//Default constructor
	public household_member_class() {
		
			//Default constructor for Housemember
		this.current_household = "null";
		this.name = "null";
		this.email = "null";
		this.ID = 0;
	}
	
	
		//Constructor that will take in user data and register a user
	public household_member_class(String n, String e, String p) {
		this.name = n;
		this.email = e;
		this.password = p;

			//Call register 
		this.register(n, e, p);
	}
	
		/*Function that will register a house member via REST API requests*/
	public void register(String name, String email, String password) {
	
			 //Encode POST values to send to HTTP Server
        String enc_pass = null;
        String enc_name = null;
        String enc_email = null;

		//Catch invalid Encoder setting exception
		
	try {
		    enc_name = URLEncoder.encode(name, "UTF-8");
            enc_pass = URLEncoder.encode(password, "UTF-8");
            enc_email= URLEncoder.encode(email, "UTF-8");
			
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
		
	try {

			//Open a connection (to the server) for POST
	
		URL url = new URL ("http://housecut-145314.appspot.com/register");
		
			//Declare connection object
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
			
			//Register the user
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("username", enc_name);
		conn.setRequestProperty("email", enc_email);
		conn.setRequestProperty("password", enc_pass);
		
	
		//Opens up an outputstreamwriter for writing to server
		
		OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());	//retrieve output stream that matches with Server input stream
		out.write("username=" + enc_name);		//what will be written..
		out.write("email=" + enc_email);
		out.write("password=" + enc_pass);
		out.close();	//flush?  .writeBytes?
		
			/*If HTTP connection fails, throw exception*/
				//might ought to be 200
		if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
			throw new RuntimeException("Failed : HTTP Error code : " + conn.getResponseCode());
		}
		
			//To test what the server outputs AND finish sending request
		BufferedReader in = new BufferedReader(
							new InputStreamReader(
								conn.getInputStream()));
				
		String dataString;
		System.out.println("Output from Server .... \n");
		while ((dataString = in.readLine()) != null) {
			System.out.println(dataString);
		}
		
		in.close();
				
		conn.disconnect();
		
	} catch (MalformedURLException e) {
		
		e.printStackTrace();
		
	} catch (IOException e) {
		
		e.printStackTrace();
	}
	}
	
		//calls household_member_class & just passes in the new password, as well as original data
	public void changePassword(String new_pass) {
		
		register(getName(), getEmail(), new_pass);
		
		password = new_pass;		
	}
	
		/* Uses endpoint /deleteaccount & token */
	
	public void deleteAccount(String token) {
		
			//Encode token to send to HTTP Server
		String enc_token = null;

		//Catch invalid Encoder setting exception
		
	try {
		
			enc_token = URLEncoder.encode(token, "UTF-8");	
			
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
		
	try {

			//Open a connection (to the server) for POST
	
		URL url = new URL ("http://housecut-145314.appspot.com/deleteaccount");
		
			//Declare connection object
		HttpURLConnection conn = 
				(HttpURLConnection) url.openConnection();
		
			
			//Delete the user
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("token", enc_token);

		
			/* If Response code isn't 200, throw exception. */
		
		if (conn.getResponseCode() != 200) {
			throw new IOException(conn.getResponseMessage());
		}
		
	
			//Opens up an outputstreamwriter for writing to server
		
		OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());	//retrieve output stream that matches with Server input stream
		out.write("token:" + enc_token);
		out.close();	//flush?
		
			/* If HTTP connection fails, throw exception. */

		if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
			throw new RuntimeException("Failed : HTTP Error code : "
				+ conn.getResponseCode());
		}
		
			//To test what the server outputs
		BufferedReader in = new BufferedReader(
				new InputStreamReader((conn.getInputStream())));
				
		String dataString;
		System.out.println("Output from Server .... \n");
		while ((dataString = in.readLine()) != null) {
			System.out.println(dataString);
		}
		
		in.close();
				
		conn.disconnect();
		
	} catch (MalformedURLException e) {
		
		e.printStackTrace();
		
	} catch (IOException e) {
		
		e.printStackTrace();
	}
	
	}

	
	/*	//log user out of account
	public void logout() {
		
		
	}*/
	
	
	public void joinHousehold(String h) {
		
			//Join a certain household
		
		if (current_household == null)
			current_household = h;
		else
			System.out.println("You must leave your current household first.\n");
		//Might have to call a popup error, or simply not let the user have the option to
		//join Household until they have left their current one
	}
	
		//user leaves current_household
	public void leaveHousehold() {
		
		current_household = null;
		
	}	
	
	
	/* Simple getter functions */
	
		//Return name field
	public String getName() {
		return name;
	}
	
		//Return password field
	public String getPassword() {
		return password;
	}
	
		//Return email field
	public String getEmail() {
		return email;
	}
	
		//Return current_household field
	public String getHousehold() {
		return current_household;
	}
	
}