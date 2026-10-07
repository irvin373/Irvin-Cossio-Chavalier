package com.example.irvincossiochavalier;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InformationActivity extends ComponentActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

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

                checkAndHandleLocation();
            }
        });
    }

    private boolean isLocationPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isGpsEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null) return false;
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void checkAndHandleLocation() {
        if (!isLocationPermissionGranted() || !isGpsEnabled()) {
            showLocationModalDialog();
        } else {
            Toast.makeText(InformationActivity.this, "Validacion exitosa", Toast.LENGTH_LONG).show();
        }
    }

    private void showLocationModalDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_gps_location);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setGravity(Gravity.BOTTOM);
        }

        Button btnContinueLocation = dialog.findViewById(R.id.btnContinueLocation);
        btnContinueLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (!isLocationPermissionGranted()) {
                    ActivityCompat.requestPermissions(InformationActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            LOCATION_PERMISSION_REQUEST_CODE);
                } else if (!isGpsEnabled()) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            }
        });

        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (!isGpsEnabled()) {
                    showLocationModalDialog();
                } else {
                    Toast.makeText(this, "Permiso de ubicación concedido", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
