package com.example.irvincossiochavalier;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InformationActivity extends ComponentActivity {

    private ImageView btnBack;
    private EditText etPhoneNumber;
    private EditText etIdCard;
    private EditText etComplement;
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_information);

        // Apply window insets for safe area padding (status bar & navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rootLayout), (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());

            // Add top padding to top bar for status bar safe area
            View topBar = findViewById(R.id.topBar);
            if (topBar != null) {
                float density = getResources().getDisplayMetrics().density;
                int pad16 = (int) (16 * density);
                topBar.setPadding(pad16, insets.top, pad16, 0);
            }

            // Add bottom padding to white card container for navigation bar safe area
            View whiteCardContainer = findViewById(R.id.whiteCardContainer);
            if (whiteCardContainer != null) {
                float density = getResources().getDisplayMetrics().density;
                int pad24 = (int) (24 * density);
                whiteCardContainer.setPadding(pad24, pad24, pad24, pad24 + insets.bottom);
            }

            return windowInsets;
        });

        btnBack = findViewById(R.id.btnBack);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etIdCard = findViewById(R.id.etIdCard);
        etComplement = findViewById(R.id.etComplement);
        btnNext = findViewById(R.id.btnNext);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = etPhoneNumber.getText().toString().trim();
                String idCard = etIdCard.getText().toString().trim();
                String complement = etComplement.getText().toString().trim();

                if (phoneNumber.isEmpty() || phoneNumber.length() > 8 || !phoneNumber.matches("\\d+")) {
                    etPhoneNumber.setError("El numero de telefono deben contener mas de 8 digitos");
                    etPhoneNumber.requestFocus();
                    return;
                }

                if (idCard.isEmpty() || idCard.length() != 10 || !idCard.matches("\\d+")) {
                    etIdCard.setError("el Carnet de identidad deben ser 10 digitos");
                    etIdCard.requestFocus();
                    return;
                }

                if (!complement.isEmpty()) {
                    complement = complement.toUpperCase();
                    if (complement.length() != 2 || !complement.matches("[A-Z0-9]+")) {
                        etComplement.setError("El complemento debe ser solo 2 caracteres alfanumericos");
                        etComplement.requestFocus();
                        return;
                    }
                }

                Toast.makeText(InformationActivity.this,
                        "Validacion exitosa",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
