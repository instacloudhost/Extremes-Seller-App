package com.instacloudhost.extremes.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.instacloudhost.extremes.R;
import com.instacloudhost.extremes.model.CustomerButtonModel;
import com.instacloudhost.extremes.pages.CustomersViewPageActivity;

import java.util.ArrayList;

public class CustomerButtonAdapter extends RecyclerView.Adapter<CustomerButtonAdapter.ViewHolder>{

    private ArrayList<CustomerButtonModel> mCustomerButtonModel = new ArrayList<>();
    private Context context;

    public CustomerButtonAdapter(Context context, ArrayList<CustomerButtonModel> mCustomerButtonModel) {
        this.mCustomerButtonModel=mCustomerButtonModel;
        this.context=context;
    }

    @NonNull
    @Override
    public CustomerButtonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_activity_view_customer,viewGroup,false);
        return new CustomerButtonAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerButtonAdapter.ViewHolder viewHolder, int i) {

        viewHolder.btn_title.setText(mCustomerButtonModel.get(i).getTitle());
        viewHolder.btn_title.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CustomersViewPageActivity.class);
                intent.putExtra("category", mCustomerButtonModel.get(i).getCategory());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCustomerButtonModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private Button btn_title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            btn_title=(Button)itemView.findViewById(R.id.txt_btn_title);
        }
    }
}