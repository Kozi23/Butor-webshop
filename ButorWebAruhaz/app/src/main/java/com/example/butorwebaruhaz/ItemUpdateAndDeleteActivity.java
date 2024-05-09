package com.example.butorwebaruhaz;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ItemUpdateAndDeleteActivity extends AppCompatActivity {

    ImageView itemImage;
    TextView itemTitle, itemDesc;
    Button addToCart;
    String id, image, title, desc;
    FirebaseFirestore db;
    FirebaseAuth auth;
    FirebaseUser user;
    ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_and_delete);

        id = getIntent().getStringExtra("documentId");
        image = getIntent().getStringExtra("itemImage");
        title = getIntent().getStringExtra("itemTitle");
        desc = getIntent().getStringExtra("itemDescription");

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        progressDialog = new ProgressDialog(this);

        itemImage = findViewById(R.id.item_image_update);
        itemTitle = findViewById(R.id.item_title_update);
        itemDesc = findViewById(R.id.item_desc_update);
        addToCart = findViewById(R.id.add_to_cart);

        Glide.with(this).load(image).into(itemImage);
        itemTitle.setText(title);
        itemDesc.setText(desc);

        addToCart.setOnClickListener(v -> {
            Intent intent = new Intent(ItemUpdateAndDeleteActivity.this, CartActivity.class);
            intent.putExtra("documentId", id);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_update_delete_menu, menu);
        MenuItem updateItem = menu.findItem(R.id.edit_item);
        MenuItem deleteItem = menu.findItem(R.id.delete_item);

        if (Objects.equals(user.getEmail(), "admin@ad.min")) {
            updateItem.setVisible(true);
            deleteItem.setVisible(true);
            addToCart.setVisibility(View.INVISIBLE);
            itemTitle.setEnabled(true);
            itemDesc.setEnabled(true);
        } else {
            updateItem.setVisible(false);
            deleteItem.setVisible(false);
            addToCart.setVisibility(View.VISIBLE);
            itemTitle.setEnabled(false);
            itemDesc.setEnabled(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.logout) {
            auth.signOut();
            startActivity(new Intent(ItemUpdateAndDeleteActivity.this, MainActivity.class));
        }
        if (item.getItemId() == R.id.edit_item) {
            updatePost(id, itemTitle.getText().toString().trim(), itemDesc.getText().toString().trim());
        }
        if (item.getItemId() == R.id.delete_item) {
            deletePost(id);
        }
        if (item.getItemId() == R.id.back_button) {
            startActivity(new Intent(ItemUpdateAndDeleteActivity.this, HomeActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void updatePost(String id, String newTitle, String newDesc) {
        db.collection("Items").document(id)
                .update("itemTitle", newTitle, "itemDescription", newDesc)
                .addOnCompleteListener(task -> {
                    Toast.makeText(this, "Sikeres frissítés", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ItemUpdateAndDeleteActivity.this, HomeActivity.class));
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Hiba, error: "+e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void deletePost(String id) {
        db.collection("Items").document(id)
                .delete()
                .addOnCompleteListener(task -> {
                    Toast.makeText(this, "Sikeres törlés", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ItemUpdateAndDeleteActivity.this, HomeActivity.class));
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Hiba, error: "+e.getMessage(), Toast.LENGTH_SHORT).show());
    }


}