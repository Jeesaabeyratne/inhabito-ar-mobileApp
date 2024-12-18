package com.example.livo.customer;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.livo.Database;
import com.example.livo.R;
import com.example.livo.company.CompanyLogin;
import com.example.livo.company.CompanySignup;
import com.example.livo.databinding.ActivityCustomerSignupBinding;

public class CustomerSignup extends AppCompatActivity {

    ActivityCustomerSignupBinding binding;
    Database database;
    TextView loginRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_signup);

        binding = ActivityCustomerSignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = new Database(this);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        binding.signupButton.setOnClickListener(view -> {
                String email = binding.signupEmail.getText().toString();
                String password = binding.signupPass.getText().toString();
                String confirmpassword = binding.signupConfirmpass.getText().toString();

                if (email.isEmpty() || password.isEmpty() || confirmpassword.isEmpty())
                    Toast.makeText(CustomerSignup.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                else {
                    if (password.equals(confirmpassword)) {
                        Boolean checkuserEmail = database.checkEmail(email);

                        if (!checkuserEmail) {
                            Boolean insert = database.insertData(email, password);
                            if (insert == true) {

                                CustomerSession customerSession = CustomerSession.getInstance(CustomerSignup.this);
                                customerSession.setEmail(email); // Save email in session after successful signup

                                Toast.makeText(CustomerSignup.this, "Signup Success", Toast.LENGTH_SHORT).show();

                                //after sign-up user should add data
                                Intent intent = new Intent(getApplicationContext(), CustomerData.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(CustomerSignup.this, "Signup Failed", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(CustomerSignup.this, "User Already Exist please login", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(CustomerSignup.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                    }
                }
        });

        loginRedirectText.setOnClickListener(view -> {
            // Redirect to the login activity
            Intent intent = new Intent(CustomerSignup.this, CustomerLogin.class);
            startActivity(intent);
            finish(); // Optionally finish the current activity
        });
    }
}