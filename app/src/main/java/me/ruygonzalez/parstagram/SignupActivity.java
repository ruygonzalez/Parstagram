package me.ruygonzalez.parstagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {
    private EditText usernameInput;
    private EditText passwordInput;
    private EditText emailInput;
    private EditText handleInput;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        usernameInput = findViewById(R.id.username_et);
        passwordInput = findViewById(R.id.password_et);
        emailInput = findViewById(R.id.email_et);
        handleInput = findViewById(R.id.handle_et);
        registerBtn = findViewById(R.id.register_btn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get references to username, password and email that user typed once user clicks on Sign up button
                final String username = usernameInput.getText().toString();
                final String password = passwordInput.getText().toString();
                final String email = emailInput.getText().toString();
                final String handle = handleInput.getText().toString();
                signup(username, password, email, handle);
            }
        });
    }

    // sign up in Parse using username, password, email, and handle
    private void signup(String username, String password, String email, String handle){
        // Create the ParseUser
        ParseUser user = new ParseUser();
        // Set core properties
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.put("handle",handle);
        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now. (No error)
                    Log.d("SignupActivity", "Signup successful!");

                    // want to go to Home Activity with intent after successful log in
                    final Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish(); //so that user can't just press back and log out
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    Log.e("SignupActivity", "Sign up failure.");
                    e.printStackTrace();
                }
            }
        });
    }
}
// get data from textviews same way as in log in
// create new parceuser
// set username, password and email then signupinbackground
// then set intent (and finish) to go to main activity
// maybe add a (already have an account? Log in) button that goes back to sign in