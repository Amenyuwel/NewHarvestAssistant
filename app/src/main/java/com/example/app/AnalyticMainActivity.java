package com.example.app;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class AnalyticMainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.analytics_layout);

        // Set the ActionBar background color
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Set the ActionBar background color
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.matcha)));
            // Remove ActionBar Title
            actionBar.setTitle("");
        }

        ImageView backButton = findViewById(R.id.analyticsBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call method to handle back button click
                onBackButtonClick();
            }
        });
    }

    private void onBackButtonClick() {
        // Handle back button click, for example:
        finish();
    }
}
