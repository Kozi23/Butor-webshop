package com.example.butorwebaruhaz;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;

public class AddItemActivity extends AppCompatActivity {

    EditText item_name, item_desc;
    Button item_pushBtn;
    ImageView item_image;

    ProgressDialog progressDialog;

    FirebaseFirestore db;

    Uri imageUri = null;
    private static final int GALLERY_IMAGE_CODE = 100;
    private static final int CAMERA_CODE = 200;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_item);


        db = FirebaseFirestore.getInstance();
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Új termék");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);


        permission();
        item_name = findViewById(R.id.item_name);
        item_desc = findViewById(R.id.item_description);
        item_pushBtn = findViewById(R.id.item_upload);
        item_image = findViewById(R.id.item_image);
        progressDialog = new ProgressDialog(this);


        item_image.setOnClickListener(v -> {
            Toast.makeText(AddItemActivity.this, "Image tapped", Toast.LENGTH_SHORT).show();
            imagePickDialog();
        });

        item_pushBtn.setOnClickListener(v -> {
            String title = item_name.getText().toString().trim();
            String description = item_desc.getText().toString().trim();

            if (TextUtils.isEmpty(title)) {
                item_name.setError("A termék név kötelező!");
            } else if (TextUtils.isEmpty(description)) {
                item_desc.setError("A termék leírás kötelező!");
            } else {
                pushData(title, description);
            }
        });


    }

    private void pushData(String title, String description) {
        final String currentTime = String.valueOf(System.currentTimeMillis());
        String filepath = "Item/" + "item_" + currentTime;
        progressDialog.setMessage("A termék feltöltése folyamatban");
        progressDialog.show();

        if (item_image.getDrawable() != null) {
            Bitmap bitmap = ((BitmapDrawable) item_image.getDrawable()).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] data = byteArrayOutputStream.toByteArray();

            StorageReference ref = FirebaseStorage.getInstance().getReference().child(filepath);
            ref.putBytes(data).addOnSuccessListener(taskSnapshot -> {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful()) ;

                String downloadUri = uriTask.getResult().toString();

                if (uriTask.isSuccessful()) {
                    HashMap<String, Object> items = new HashMap<>();
                    items.put("itemTitle", title);
                    items.put("itemImage", downloadUri);
                    items.put("itemDescription", description);
                    items.put("itemTime", currentTime);
                    progressDialog.dismiss();
                    db.collection("Items")
                            .add(items)
                            .addOnCompleteListener(task -> {
                                Toast.makeText(AddItemActivity.this, "Sikeres feltöltés", Toast.LENGTH_SHORT).show();
                                item_name.setText("");
                                item_desc.setText("");
                                item_image.setImageURI(null);
                                imageUri = null;
                                startActivity(new Intent(AddItemActivity.this, HomeActivity.class));
                            })
                            .addOnFailureListener(e -> Toast.makeText(AddItemActivity.this, "Hiba, error: "+e.getMessage(), Toast.LENGTH_SHORT).show());
                }

            }).addOnFailureListener(e -> Toast.makeText(AddItemActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void imagePickDialog() {
        String[] options = {"Kamera", "Galéria"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Kérem válasszon képet");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                startCamera();
            } else {
                galleryPick();
            }
        });
        builder.create().show();
    }

    private void startCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Ideiglenes kép");
        contentValues.put(MediaStore.Images.Media.TITLE, "Ideiglenes leírás");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAMERA_CODE);

    }

    private void galleryPick() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_IMAGE_CODE);
    }

    private void permission(){
        Dexter.withContext(this)
                .withPermissions(
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
                    @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}
                    @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
                }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_IMAGE_CODE) {
                assert data != null;
                imageUri = data.getData();
                item_image.setImageURI(imageUri);
            }
            if (requestCode == CAMERA_CODE) {
                item_image.setImageURI(imageUri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}