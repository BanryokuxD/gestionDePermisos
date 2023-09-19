package co.com.uniminuto.actividad2_permisos;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 2;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 3;
    private static final int CALL_PHONE_PERMISSION_REQUEST_CODE = 4;

    private TextView tvCameraPermissionStatus;
    private TextView tvLocationPermissionStatus;
    private TextView tvStoragePermissionStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCameraPermissionStatus = findViewById(R.id.tvCameraPermissionStatus);
        tvLocationPermissionStatus = findViewById(R.id.tvLocationPermissionStatus);
        tvStoragePermissionStatus = findViewById(R.id.tvStoragePermissionStatus);

        Button btnCameraPermission = findViewById(R.id.btnCameraPermission);
        Button btnLocationPermission = findViewById(R.id.btnLocationPermission);
        Button btnStoragePermission = findViewById(R.id.btnStoragePermission);
        Button btnMakeCall = findViewById(R.id.btnMakeCall);

        btnCameraPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCameraPermission();
            }
        });

        btnLocationPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLocationPermission();
            }
        });

        btnStoragePermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestStoragePermission();
            }
        });

        btnMakeCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCallPhonePermission();
            }
        });

        // Verificar el estado de los permisos y solicitarlos si es necesario
        checkPermissionStatus();
    }

    private void checkPermissionStatus() {
        int cameraPermissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int locationPermissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int storagePermissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // Verificar el estado de cada permiso y solicitarlos si es necesario
        if (cameraPermissionStatus != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission();
        }
        if (locationPermissionStatus != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
        }
        if (storagePermissionStatus != PackageManager.PERMISSION_GRANTED) {
            requestStoragePermission();
        }

        // Actualizar el estado de los permisos en los TextViews
        updatePermissionStatus(tvCameraPermissionStatus, cameraPermissionStatus);
        updatePermissionStatus(tvLocationPermissionStatus, locationPermissionStatus);
        updatePermissionStatus(tvStoragePermissionStatus, storagePermissionStatus);
    }

    private void updatePermissionStatus(TextView textView, int permissionStatus) {
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            textView.setText("Permiso Otorgado");
        } else {
            textView.setText("Permiso No Otorgado");
        }
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Explicación adicional antes de solicitar el permiso.
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Permiso de Ubicación");
            builder.setMessage("Necesitamos acceder a su ubicación para proporcionarle información relevante.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                }
            });
            builder.setNegativeButton("Cancelar", null);
            builder.show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST_CODE);
    }

    private void requestCallPhonePermission() {
        // Comprueba si tienes el permiso CALL_PHONE
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            // Si tienes el permiso, realiza la llamada telefónica
            makePhoneCall();
        } else {
            // Si no tienes el permiso, solicítalo al usuario
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    CALL_PHONE_PERMISSION_REQUEST_CODE);
        }
    }

    private void makePhoneCall() {
        // Número de teléfono al que se realizará la llamada
        String phoneNumber = "123456789"; // Reemplaza esto con el número que desees llamar

        // Crea una intención para realizar la llamada
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));

        // Comprueba si la aplicación de teléfono está disponible en el dispositivo
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                tvCameraPermissionStatus.setText("Permiso Otorgado");
            } else {
                tvCameraPermissionStatus.setText("Permiso No Otorgado");
            }
        } else if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                tvLocationPermissionStatus.setText("Permiso Otorgado");
            } else {
                tvLocationPermissionStatus.setText("Permiso No Otorgado");
            }
        } else if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                tvStoragePermissionStatus.setText("Permiso Otorgado");
            } else {
                tvStoragePermissionStatus.setText("Permiso No Otorgado");
            }
        } else if (requestCode == CALL_PHONE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso otorgado, realiza la llamada telefónica
                makePhoneCall();
            } else {
                // Permiso denegado, puedes mostrar un mensaje al usuario
                // indicando que el permiso es necesario para realizar llamadas.
            }
        }
    }
}
