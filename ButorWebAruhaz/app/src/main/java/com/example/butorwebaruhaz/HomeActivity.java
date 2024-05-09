package com.example.butorwebaruhaz;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.butorwebaruhaz.Adapter.ItemAdapter;
import com.example.butorwebaruhaz.Model.ItemModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore db;
    RecyclerView recyclerView;
    ArrayList<ItemModel> itemModelList;
    ItemAdapter itemAdapter;
    ProgressDialog progressDialog;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Termékek lekérése");
        progressDialog.show();
        auth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.recycle_view);
        db = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();

        LinearLayoutManager linlay = new LinearLayoutManager(this);
        linlay.setStackFromEnd(true);
        linlay.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linlay);

        itemModelList = new ArrayList<>();
        itemAdapter = new ItemAdapter(this,itemModelList);

        recyclerView.setAdapter(itemAdapter);

        loadItems();

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(HomeActivity.this, ItemUpdateAndDeleteActivity.class);
                        intent.putExtra("documentId", itemModelList.get(position).getDocumentId());
                        intent.putExtra("itemTitle", itemModelList.get(position).getItemTitle());
                        intent.putExtra("itemDescription", itemModelList.get(position).getItemDescription());
                        intent.putExtra("itemImage", itemModelList.get(position).getItemImage());
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Toast.makeText(HomeActivity.this, "Nope", Toast.LENGTH_SHORT).show();
                    }
                }));


        }

    private void loadItems() {
        db.collection("Items")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> itemList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot document : itemList) {
                            ItemModel itemModel = document.toObject(ItemModel.class);
                            assert itemModel != null;
                            itemModel.setDocumentId(document.getId());
                            itemModelList.add(itemModel);
                        }
                        itemAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    } else {
                        Toast.makeText(HomeActivity.this, "Nincs rekord az adatbázisban!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(HomeActivity.this, "Shit happens", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,  menu);
        MenuItem addItem = menu.findItem(R.id.menu_add_item);
        MenuItem addItemToCart = menu.findItem(R.id.menu_cart);
        if (Objects.equals(user.getEmail(), "admin@ad.min")) {
            addItem.setVisible(true);
            addItemToCart.setVisible(false);
        } else {
            addItem.setVisible(false);
            addItemToCart.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menu_logout) {
             auth.signOut();
             startActivity(new Intent(HomeActivity.this, MainActivity.class));
        }

        if (item.getItemId() == R.id.menu_add_item) {
            startActivity(new Intent(HomeActivity.this, AddItemActivity.class));
        }


        return super.onOptionsItemSelected(item);
    }
}