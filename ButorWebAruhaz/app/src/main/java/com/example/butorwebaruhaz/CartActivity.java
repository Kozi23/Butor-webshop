package com.example.butorwebaruhaz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class CartActivity extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth auth;
    FirebaseUser user;
    Button purchase;
    TextView title, desc;
    ImageView image;
    EditText name, address, phone;
    ProgressDialog progressDialog;
    String itemToPurchaseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Termékek lekérése");
        progressDialog.show();

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        image = findViewById(R.id.cart_item_image);
        title = findViewById(R.id.cart_item_title);
        desc = findViewById(R.id.cart_item_desc);

        name = findViewById(R.id.orderer_name);
        address = findViewById(R.id.orderer_address);
        phone = findViewById(R.id.orderer_phone);

        purchase = findViewById(R.id.purchase);

        itemToPurchaseId = getIntent().getStringExtra("documentId");

        loadItem();

        purchase.setOnClickListener(v -> purchaseItem());
    }

    private void purchaseItem() {
        progressDialog.setMessage("A termék vásárlása");
        progressDialog.show();

        HashMap<String, Object> order = new HashMap<>();
        order.put("email", user.getEmail());
        order.put("name", name.getText().toString().trim());
        order.put("address", address.getText().toString().trim());
        order.put("phone", phone.getText().toString().trim());
        order.put("orderedItemId", itemToPurchaseId);

        db.collection("Orders")
                .add(order)
                .addOnCompleteListener(task -> {
                    Toast.makeText(this, "Sikeres rendelés", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CartActivity.this, HomeActivity.class));
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Hoppá, error: "+e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void loadItem() {
        db.collection("Items").document(itemToPurchaseId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Glide.with(this).load(document.getString("itemImage")).into(image);
                            title.setText(document.getString("itemTitle"));
                            desc.setText(document.getString("itemDescription"));
                            progressDialog.dismiss();
                        } else {
                            // A dokumentum nem létezik
                            Toast.makeText(this, "Nincs ilyen adat", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}