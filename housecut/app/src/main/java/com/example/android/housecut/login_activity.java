package com.example.android.housecut;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.lang.String;

//Implemented by Nick and Jose and Chris
public class login_activity extends AppCompatActivity {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private TextView mMessageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (EditText) findViewById(R.id.email1);
        mPasswordView = (EditText) findViewById(R.id.password1);
        mMessageView = (TextView)findViewById(R.id.loginmessage);


        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        Button mEmailRegisterButton = (Button) findViewById(R.id.email_register_button);


        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });


        mEmailRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        mMessageView.setVisibility(View.GONE);

    }

    public void registerUser() {
        Intent intent = new Intent(login_activity.this, register_activity.class);
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        startActivity(intent);
    }

    public void attemptLogin() {
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        mMessageView.setVisibility(View.VISIBLE);
        mMessageView.setText("Loading...");

        LoginRunner loginRunner = new LoginRunner(this, mMessageView);
        loginRunner.execute(email, password);
    }



    class LoginRunner extends AsyncTask<String, Void, String> {

        private Context ctx;
        private TextView mMessageView;

        public LoginRunner(Context ctx, TextView mMessageView){
            this.ctx = ctx;
            this.mMessageView = mMessageView;
        }

        @Override
        protected String doInBackground(String... params) {
            return loginToServer(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(String responseString) {
            if (responseString.equals("success")) {
                HouseCutApp app = ((HouseCutApp)this.ctx.getApplicationContext());
                household_member_class user = app.getUser();

                finish();
                if (user.getHousehold().equals("0")) {
                    Intent intent = new Intent(this.ctx, join_household_activity.class);
                    this.ctx.startActivity(intent);
                    overridePendingTransition(0, 0);
                }
                else {
                    Intent intent = new Intent(this.ctx, main_page_activity.class);
                    this.ctx.startActivity(intent);
                    overridePendingTransition(0, 0);
                }
            }
            else {
                this.mMessageView.setVisibility(View.VISIBLE);
                this.mMessageView.setText(responseString);
            }
        }

        public String loginToServer(String email, String password){
            //Catch invalid Encoder setting exception

            //System.out.println(email);


            try {

                //Open a connection (to the server) for POST

                URL url = new URL ("http://10.0.2.2:8080/login");

                //Declare connection object
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");


                //Creates JSON string to write to server via POST
                JSONObject json = new JSONObject();
                json.put("email", email);

                json.put("password", password);

                //Opens up an outputstreamwriter for writing to server
                //retrieve output stream that matches with Server input stream..
                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");

                //OR, with JSON....
                out.write(json.toString());

                out.close();	//flush?  .writeBytes?

			/*If HTTP connection fails, throw exception*/


                //To test what the server outputs AND finish sending request
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                conn.getInputStream()));

                //StringBuffer will hold JSON string
                StringBuffer result = new StringBuffer();
                String line;
                System.out.println("Output from Server .... \n");
                while ((line = in.readLine()) != null) {
                    System.out.println(result);
                    result.append(line);
                }

                //JSON string returned by server
                JSONObject data = new JSONObject(String.valueOf(result));

                System.out.println(data);

                Boolean success = data.getBoolean("success");

                String responseString = "";

                if (success) {
                    System.out.println("Login success\n");

                    /* create user */
                    String householdId = data.getString("householdId");
                    String name = data.getString("displayName");
                    String userId = data.getString("id");
                    String token = data.getString("token");
                    household_member_class user = new household_member_class();
                    user.setHouseholdId(householdId);
                    user.setName(name);
                    user.setToken(token);
                    user.setId(userId);
                    ((HouseCutApp)this.ctx.getApplicationContext()).setUser(user);

                    in.close();
                    conn.disconnect();

                    System.out.println("Getting roommates");
                    /* get roommates */

                    //Checks to make sure the user has a household, otherwise sets it as a success already
                    if( user.getHousehold().equals( "0" ) ){
                        responseString = "success";

                    //Deals with household members
                    } else {
                        try {
                            url = new URL("http://10.0.2.2:8080/household/roommates?token=" + token);

                            //Declare connection object
                            conn = (HttpURLConnection) url.openConnection();

                            conn.setRequestMethod("GET");

                            in = new BufferedReader(
                                    new InputStreamReader(conn.getInputStream()));

                            result = new StringBuffer();
                            while ((line = in.readLine()) != null) {
                                System.out.println(result);
                                result.append(line);
                            }

                            data = new JSONObject(String.valueOf(result));

                            System.out.println(data);

                            success = data.getBoolean("success");

                            Household household = new Household();

                            if (success) {
                                JSONArray roommates = data.getJSONArray("roommates");
                                for (int i = 0; i < roommates.length(); i++) {
                                    JSONObject roommateJSON = roommates.getJSONObject(i);
                                    String roommateName = roommateJSON.getString("displayName");
                                    String roommateId = roommateJSON.getString("id");
                                    household.addRoommate(roommateName, roommateId);
                                }

                                in.close();
                                conn.disconnect();

                           /* get household name */
                                try {
                                    url = new URL("http://10.0.2.2:8080/household/name?token=" + token);

                                    //Declare connection object
                                    conn = (HttpURLConnection) url.openConnection();

                                    conn.setRequestMethod("GET");

                                    in = new BufferedReader(
                                            new InputStreamReader(conn.getInputStream()));

                                    result = new StringBuffer();
                                    while ((line = in.readLine()) != null) {
                                        System.out.println(result);
                                        result.append(line);
                                    }

                                    data = new JSONObject(String.valueOf(result));

                                    System.out.println(data);

                                    success = data.getBoolean("success");

                                    if (success) {
                                        String householdName = data.getString("name");
                                        household.setName(householdName);

                                        ((HouseCutApp) this.ctx.getApplicationContext()).setHousehold(household);
                                        responseString = "success";
                                    } else {
                                        String message = data.getString("message");
                                        System.out.println(message + "\n");
                                        responseString = data.getString("message");
                                    }
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();

                                } catch (IOException e) {

                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                String message = data.getString("message");
                                System.out.println(message + "\n");
                                responseString = data.getString("message");
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();

                        } catch (IOException e) {

                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }
                else {
                    System.out.println("Login failure\n");
                    String message = data.getString("message");
                    System.out.println(message + "\n");
                    responseString = data.getString("message");
                }

                in.close();
                conn.disconnect();

                return responseString;

            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return "Failure";
        }
    }
}
