package com.example.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    TextInputEditText etPassword, etID;
    TextView tvBottomTextSignup;
    Button btnLogin;
    String TAG = "BillyBayot";
    String URL = "https://harvest.dermocura.net/PHP_API/login.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        etPassword = findViewById(R.id.etPassword);
        etID = findViewById(R.id.etID);
        btnLogin = findViewById(R.id.btnLogin);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, Dashboard.class);
                startActivity(i);
            }
        });


        tvBottomTextSignup = findViewById(R.id.tvBottomTextSignup);

        Button loginButton = findViewById(R.id.btnLogin);

        tvBottomTextSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, SignupActivity.class);
                startActivity(i);}
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate input fields
                if (validateInputFields()) {
                    // Perform login if validation passes
                    String username = etID.getText().toString().trim();
                    String password = etPassword.getText().toString().trim();
                    makeHTTPRequest(username, password);
                }
            }
        });
    }

    private boolean validateInputFields() {
        String username = etID.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            etID.setError("Username is required");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            return false;
        }

        // Validation passed
        return true;
    }

//     private void performLogin(String username, String password) {
//     String url = "http://harvestassistantfinalii/api/login.php";
//     RequestQueue rq = Volley.newRequestQueue(Login.this);
//     StringRequest postRequest = new StringRequest(Request.Method.POST, url,
//             response -> {
//                 try {
//                     JSONObject jsonResponse = new JSONObject(response);
//                     boolean success = jsonResponse.getBoolean("success");
//                     String message = jsonResponse.getString("message");

//                     if (success) {
//                         // Extract the userData object
//                         JSONObject userDataJson = jsonResponse.getJSONObject("userData");

//                         // Assuming your user data class matches these fields:
//                         UserData userData = new UserData(
//                                 userDataJson.getString("rsbsa_num"),
//                                 userDataJson.getInt("farmerID"),
//                                 userDataJson.getString("firstName"),
//                                 userDataJson.getString("lastName"),
//                                 userDataJson.getString("contactNumber"),
//                                 userDataJson.getString("area")
//                         );

//                         // Save token or any other relevant data to SharedPreferences
//                         saveUserDataToSharedPreferences(userData);

//                         Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//                         Intent intent = new Intent(Login.this, Dashboard.class);
//                         startActivity(intent);
//                         finish(); // Finish the login activity
//                     } else {
//                         // Login failed
//                         Log.e("Login Error", message);
//                         Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//                     }

//                 } catch (Exception e) {
//                     e.printStackTrace();
//                     Toast.makeText(this, "Error processing login", Toast.LENGTH_SHORT).show();
//                 }
//             },
//             error -> {
//                 if (error.networkResponse != null) {
//                     int statusCode = error.networkResponse.statusCode;
//                     String responseData = new String(error.networkResponse.data);
//                     Log.e("Network Error", "Status Code: " + statusCode);
//                     Log.e("Network Error", "Response Data: " + responseData);
//                     Toast.makeText(this, "Error: " + responseData, Toast.LENGTH_SHORT).show();
//                 } else {
//                     Log.e("Network Error", "Network error: " + error.toString());
//                     Toast.makeText(this, "Network error: " + error.toString(), Toast.LENGTH_SHORT).show();
//                 }
//             }
//     ) {
//         @Override
//         protected Map<String, String> getParams() {
//             Map<String, String> params = new HashMap<>();
//             Log.i("LOGIN-CRED", "username" + username);
//             Log.i("LOGIN-CRED", "password" + password);
//             params.put("rsbsa_num", username);
//             params.put("password", password);
//             return params;
//         }

//         @Override
//         public Map<String, String> getHeaders() throws AuthFailureError {
//             Map<String, String> headers = new HashMap<>();
//             headers.put("Content-Type", "application/json");
//             return headers;
//         }
//     };

//     rq.add(postRequest);
// }

    private void makeHTTPRequest(String username, String password) {
        // Define keys for the JSON request body
        String keyEmail = "rsbsa_num";
        String keyPassword = "password";

        // Create a JSON object for the request body
        JSONObject requestBody = new JSONObject();

        // Create a Volley request queue
        RequestQueue queue = Volley.newRequestQueue(this);

        // Populate the JSON request body
        try {
            requestBody.put(keyEmail, username);
            requestBody.put(keyPassword, password);
        } catch (JSONException e) {
            Log.e(TAG + " makeHTTPRequest", String.valueOf(e));
            return;
        }

        // Create a JsonObjectRequest for a POST request to the specified URL
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                requestBody,
                this::onRequestSuccess,
                this::onRequestError
        );

        // Log the JSON request body for debugging
        String stringJSON = requestBody.toString();
        Log.i(TAG + " makeHTTPRequest", stringJSON);

        // Add the request to the Volley request queue
        queue.add(request);
    }

    private void onRequestSuccess(JSONObject response) {
        try {
            // Extract success status and message from the JSON response
            boolean success = response.getBoolean("success");
            String message = response.getString("message");

            if (success) {
                // Login successful
                Log.d(TAG + " onRequestSuccess", "Message Response: " + message);
                Log.d(TAG + " onRequestSuccess", "JSON Received: " + response);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login.this, Dashboard.class);
                startActivity(intent);
                finish();
            } else {
                // Login failed
                Log.e(TAG + " onRequestSuccess", "Message Response: " + message);
            }
        } catch (JSONException e) {
            Log.e(TAG + " onRequestSuccess", String.valueOf(e));
            Log.e(TAG + " onRequestSuccess", "Error parsing JSON response");
        }
    }

    private void onRequestError(VolleyError error) {
        // Log and highlight entry
        Log.e(TAG + " onRequestError", "Error Response: " + error.getMessage());
    }

    private void saveTokenToSharedPreferences(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.apply();
    }

    // Method to retrieve the token from SharedPreferences
    private String getTokenFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getString("token", "");
    }
}