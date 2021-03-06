package com.example.android.housecut;
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
import org.json.JSONObject;
import org.json.JSONException;


/*House member class*/

//"http://housecut-145314.appspot.com/"

public class household_member_class {


    protected String current_household;
    protected String user_name;
    protected String name;
    protected String request = "http://10.0.2.2:8080/";
    protected String id;
    protected String token;
    protected String password;
    protected String email;
    protected boolean isAdmin = false;
    protected String errorMessage = null;



    //Default constructor for Housemember
    public household_member_class() {

        this.current_household = null;
        this.name = null;
        this.email = null;
        this.id = null;
    }

    //Conversion Constructor for Admin -> member

    //Constructor that will take in user data and register a user
    public household_member_class(String n, String e, String p) {
        this.name = n;
        this.email = e;
        this.password = p;

        //Call register
        this.register(n, e, p);
    }

      //Private function to connect to the server, and write/return JSON
    private JSONObject writeToServer (JSONObject json, String url) {

        JSONObject data = new JSONObject();

      try {
          //Open a connection (to the server) for POST

          URL url = new URL(url);

          //Declare connection object
          HttpURLConnection conn = (HttpURLConnection) url.openConnection();

          conn.setDoOutput(true);
          conn.setRequestMethod("POST");
          conn.setRequestProperty("Content-Type", "application/json");
          conn.setRequestProperty("Accept", "application/json");

          String requestBody = json.toString();

          //Opens up an outputstreamwriter for writing to server
          //retrieve output stream that matches with Server input stream..
          OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");

            //Write data to Server..
          out.write(requestBody);
          out.close();

          /* If Response code isn't 200, throw exception. */

          if (conn.getResponseCode() != 200) {
              throw new IOException(conn.getResponseMessage());
          }

          //To test what the server outputs AND finish sending request
          BufferedReader in = new BufferedReader(
                              new InputStreamReader(
                              conn.getInputStream()));

          //StringBuffer will hold JSON string
          StringBuffer result = new StringBuffer();
          String line = "";
          System.out.println("Output from Server .... \n");
          while ((line = in.readLine()) != null) {
              System.out.println(result);
              result.append(line);
          }

          //JSON string returned by server
          data = new JSONObject(result.toString());

          //Closes everything
          in.close();
          conn.disconnect();


      } catch (MalformedURLException e) {
          e.printStackTrace();
      } catch (IOException e) {
          e.printStackTrace();
      } catch (JSONException e) {
          e.printStackTrace();
      }
        //Return JSON from server
      return data;
    }

    /*Function that will register a house member via REST API requests*/

    public boolean register(String username, String email, String password) {
        //register assumes correct user input

        //set Object member data..
        this.setUserInfo(username, email, password);
        boolean success = false;

        //URL for connection to server using /register
        String url = request + "/register";

      try {
        //Creates JSON string to write to server via POST
        JSONObject json = new JSONObject();
        json.put("username", username);
        json.put("email", email);
        json.put("password", password);

        //Make call/write to server and take in returned JSON
        JSONObject serverJSON = writeToServer(json, url);
        success = serverJSON.getBoolean("success");

        //error checking
        if (success == true) {
            System.out.println("\nUser has been registered.");
        }
        else {
            //Set protected member string "errorMessage" to the server error message
            String message = serverJSON.getString("message");
            this.setErrorMessage(message);
        }

      } catch (JSONException e) {
          e.printStackTrace();
      }

        return success;
    }

    //calls household_member_class & just passes in the new password, as well as original data
    public void changePassword(String new_pass) {

        password = new_pass;
	    
	String url = request + "/changepassword";
	try {
        //Creates JSON string to write to server via POST
        JSONObject json = new JSONObject();
        json.put("password", newpass);

        //Make call/write to server and take in returned JSON
        JSONObject serverJSON = writeToServer(json, url);
        success = serverJSON.getBoolean("success");

        //error checking
        if (success == true) {
            System.out.println("\nPassword has been changed.");
        }
        else {
            //Set protected member string "errorMessage" to the server error message
            String message = serverJSON.getString("message");
            this.setErrorMessage(message);
        }

      } catch (JSONException e) {
          e.printStackTrace();
      }
	    
      return success;
    }

    /* Uses endpoint /deleteaccount & token */
    public boolean deleteAccount(String token) {
        boolean success = false;

        //URL to open connection with
        String url = request + "/deleteaccount";

      try {
        //For JSON..
        JSONObject jsonToken = new JSONObject();
        jsonToken.put("token", token);

        //Write/Get JSON from server
        JSONObject data = writeToServer(jsonToken, url);
        success = data.getBoolean("success");

        //error checking
        if (success == true) {
            success = true;
            System.out.println("Account has been deleted.");
        }
        else {
            //Set protected member string "errorMessage" to the server error message
            String message = data.getString("message");
            this.setErrorMessage(message);
        }

      } catch (JSONException e) {
          e.printStackTrace();
      }

        return success;
    } //End Function

    //Join a certain household
    public boolean addHouseholdMember(String token, String uID = null,
                                      String hhName = null, String hhPass = null)
    {

        boolean success = false;
        if (current_household == null) {
            current_household = h;

            //Begin Server call for /household/roommates
            try {

                //Open a connection (to the server) for POST

                URL url = new URL (request + "/household/roommates");

                //Declare connection object
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");


                //Creates JSON string to write to server via POST
                JSONObject json = new JSONObject();
                json.put("token", this.getToken());
                json.put("userId", )
                json.put("householdName", h);
                json.put("householdPassword", p);
                String requestBody = json.toString();

                //Opens up an outputstreamwriter for writing to server
                //retrieve output stream that matches with Server input stream..
                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");

                //Write with JSON....
                out.write(requestBody);
                out.close();

                /* If Response code isn't 200, throw exception. */

                if (conn.getResponseCode() != 200) {
                    throw new IOException(conn.getResponseMessage());
                }

                //To test what the server outputs AND finish sending request
                BufferedReader in = new BufferedReader(
                                                       new InputStreamReader(
                                                                             conn.getInputStream()));

                //StringBuffer will hold JSON string
                StringBuffer result = new StringBuffer();
                String line = "";
                System.out.println("Output from Server .... \n");
                while ((line = in.readLine()) != null) {
                    System.out.println(result);
                    result.append(line);
                }

                //JSON string returned by server
                JSONObject data = new JSONObject(result.toString());
                success = data.getBoolean("success");

                //error checking
                if (success == true)
                    System.out.println("Account has been deleted.");
                else {
                    String message = data.getString("message");
                    //Set protected member string "errorMessage" to the server error message
                    this.setErrorMessage(message);
                }

                in.close();
                conn.disconnect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        else {
            this.setErrorMessage("You must leave your current household first.");
            success = false;
        }
          //return true/false based on server response
        return success;
    } //End Function


	public bool removeHouseholdMember(String uID = null)
	{
		//written by Logan Vega//
		try {
			String url = request + "/household/roommates";
			//Get /household /roommates
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");

			JSONObject json = new JSONObject();
			json.put("operation", "remove");

        //error checking for Admins
      if (isAdmin && uID != null) {

        json.put("userID", uID);
      }
			String requestBody = json.toString();

			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");

			out.write(requestBody);
			out.close();

			//getResponseCode()!

			BufferedReader in = new BufferedReader(
					    new InputStreamReader(
					    conn.getInputStream()));

			StringBuffer result = new StringBuffer();
			String line = "";
			System.out.println("Output from Server .... \n");
			while ((line = in.readLine()) != null) {
				System.out.println(result);
				result.append(line);
			}

			//JSON string returned by server
			JSONObject data = new JSONObject(result);
			Bool success = data.getBoolean("success");

			if (success == true)
			//member was removed
				return true;
			else {
			//member was not removed
				String message = data.getString("message");
			}
		        } catch (MalformedURLException e) {

               		e.printStackTrace();

            		} catch (IOException e) {

                	e.printStackTrace();
          		} catch (JSONException e) {

                	e.printStackTrace();
     			}

		//fails if trying to remove the only admin, must appoint a new admin before removing
		return false;
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

    public String getID() {
        return id;
    }

    public String getToken() {
        return token;	//Check
    }

    //Return current_household field
    public String getHousehold() {
        return current_household;
    }

    public String errorMessage() {
        return errorMessage;
    }

    /* Private mutator functions */

    private void setUserInfo(String u, String e, String p) {
        this.name = u;
        this.email = e;
        this.password = p;
    }

    private void setErrorMessage(String m) {
        this.errorMessage = m;
    }

} //End of Class
