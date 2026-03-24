package com.example.intent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout phoneInputLayout;
    private TextInputLayout messageInputLayout;
    private TextInputEditText phoneNumberEditText;
    private TextInputEditText messageEditText;
    private MaterialButton callButton;
    private MaterialButton sendMessageButton;
    private MaterialButton networkDetailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        setupWindowInsets();
        initializeViews();
        setupClickListeners();
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeViews() {
        phoneInputLayout = findViewById(R.id.phoneInputLayout);
        messageInputLayout = findViewById(R.id.messageInputLayout);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        messageEditText = findViewById(R.id.messageEditText);
        callButton = findViewById(R.id.callButton);
        sendMessageButton = findViewById(R.id.sendMessageButton);
        networkDetailButton = findViewById(R.id.networkDetailButton);
    }

    private void setupClickListeners() {
        callButton.setOnClickListener(v -> handleCallAction());
        sendMessageButton.setOnClickListener(v -> handleMessageAction());
        networkDetailButton.setOnClickListener(v -> openNetworkSettings());
    }

    private void handleCallAction() {
        String phoneNumber = phoneNumberEditText.getText().toString().trim();

        if (TextUtils.isEmpty(phoneNumber)) {
            phoneInputLayout.setError("Phone number is required");
            showSnackbar("Please enter a phone number");
            return;
        }

        phoneInputLayout.setError(null);
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    private void handleMessageAction() {
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        String message = messageEditText.getText().toString().trim();

        boolean isValid = true;

        if (TextUtils.isEmpty(phoneNumber)) {
            phoneInputLayout.setError("Recipient number required");
            isValid = false;
        } else {
            phoneInputLayout.setError(null);
        }

        if (TextUtils.isEmpty(message)) {
            messageInputLayout.setError("Message cannot be empty");
            isValid = false;
        } else {
            messageInputLayout.setError(null);
        }

        if (!isValid) {
            showSnackbar("Please fill in the required fields");
            return;
        }

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + phoneNumber));
        intent.putExtra("sms_body", message);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            showSnackbar("No SMS application found");
        }
    }

    private void openNetworkSettings() {
        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            showSnackbar("Unable to open network settings");
        }
    }

    private void showSnackbar(String message) {
        Snackbar.make(findViewById(R.id.main), message, Snackbar.LENGTH_SHORT).show();
    }
}