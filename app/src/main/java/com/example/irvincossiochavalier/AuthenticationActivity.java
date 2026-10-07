package com.example.irvincossiochavalier;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AuthenticationActivity extends ComponentActivity {

    private ImageView btnBack;
    private ImageView btnSpeaker;
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_authentication);

        // Apply window insets for safe area padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rootLayout), (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());

            View topBar = findViewById(R.id.topBar);
            if (topBar != null) {
                float density = getResources().getDisplayMetrics().density;
                int pad16 = (int) (16 * density);
                topBar.setPadding(pad16, insets.top, pad16, 0);
            }

            View whiteCardContainer = findViewById(R.id.whiteCardContainer);
            if (whiteCardContainer != null) {
                float density = getResources().getDisplayMetrics().density;
                int pad24 = (int) (24 * density);
                whiteCardContainer.setPadding(pad24, pad24, pad24, pad24 + insets.bottom);
            }

            return windowInsets;
        });

        btnBack = findViewById(R.id.btnBack);
        btnSpeaker = findViewById(R.id.btnSpeaker);
        btnNext = findViewById(R.id.btnNext);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AuthenticationActivity.this, "Reproduciendo audio...", Toast.LENGTH_SHORT).show();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AuthenticationActivity.this, "Iniciando prueba de autenticación...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
