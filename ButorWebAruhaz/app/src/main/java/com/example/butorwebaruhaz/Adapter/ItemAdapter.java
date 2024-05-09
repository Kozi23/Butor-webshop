package com.example.butorwebaruhaz.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.butorwebaruhaz.Model.ItemModel;
import com.example.butorwebaruhaz.R;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.Holder> {

    Context context;
    List<ItemModel> itemModels;

    public ItemAdapter(Context context, List<ItemModel> itemModels) {
        this.context = context;
        this.itemModels = itemModels;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        String title = itemModels.get(position).getItemTitle();
        String description = itemModels.get(position).getItemDescription();
        String imgage = itemModels.get(position).getItemImage();
        String id = itemModels.get(position).getDocumentId();
        holder.itemTitle.setText(title);
        holder.itemDescription.setText(description);
        Glide.with(context).load(imgage).into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return itemModels.size();
    }

    class Holder extends RecyclerView.ViewHolder{

        ImageView itemImage;
        TextView itemTitle, itemDescription;

        public Holder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_Image);
            itemTitle = itemView.findViewById(R.id.item_Title);
            itemDescription = itemView.findViewById(R.id.item_Desc);
        }
    }


}
