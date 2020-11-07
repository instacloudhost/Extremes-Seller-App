package com.instacloudhost.extremes.adapter;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.instacloudhost.extremes.AddCustomer;
import com.instacloudhost.extremes.R;
import com.instacloudhost.extremes.model.CustomerInputField;
import com.instacloudhost.extremes.model.DetailModel;
import com.instacloudhost.extremes.sections.WindsForm;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder>{

    private ArrayList<DetailModel> detailModels = new ArrayList<>();
    private Context context;

    public DetailAdapter(Context context, ArrayList<DetailModel> detailModels) {
        this.detailModels=detailModels;
        this.context=context;
    }
// Adadpters for buttons on Home
    @NonNull
    @Override
    public DetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_item_button,viewGroup,false);
        return new DetailAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailAdapter.ViewHolder viewHolder, int i) {
        viewHolder.title_icon.setText(detailModels.get(i).getName());
        Log.d("Layout:", detailModels.get(i).getInputField().getC1());

        Picasso.get()
                .load(detailModels.get(i).getIcon())
                .resize(150,150)
                .into(viewHolder.img_icon);


        if(detailModels.get(i).getLayout().equals("layout_three")){
            viewHolder.img_icon.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, WindsForm.class);
                    intent.putExtra("layout", detailModels.get(i).getLayout());
                    intent.putExtra("category", detailModels.get(i).getCategory());
                    intent.putExtra("title", detailModels.get(i).getName());
                    intent.putExtra("btn1", detailModels.get(i).getBtn1());
                    intent.putExtra("btn2", detailModels.get(i).getBtn2());
                    context.startActivity(intent);
                }
            });
        }else{
            viewHolder.img_icon.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AddCustomer.class);
                    intent.putExtra("layout", detailModels.get(i).getLayout());
                    intent.putExtra("category", detailModels.get(i).getCategory());
                    intent.putExtra("title", detailModels.get(i).getName());
                    intent.putExtra("btn1", detailModels.get(i).getBtn1());
                    intent.putExtra("btn2", detailModels.get(i).getBtn2());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return detailModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView img_icon;
        private TextView title_icon;
        private TextView layout_icon;
        private TextView category_icon;
        private TextView btn1_icon;
        private TextView btn2_icon;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_icon=(ImageView) itemView.findViewById(R.id.img_icon);
            title_icon=(TextView)itemView.findViewById(R.id.title_icon);
        }
    }
}
