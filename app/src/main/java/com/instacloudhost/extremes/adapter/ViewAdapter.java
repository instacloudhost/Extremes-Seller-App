package com.instacloudhost.extremes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.instacloudhost.extremes.R;
import com.instacloudhost.extremes.model.Message;

import java.util.ArrayList;

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder>{

    private ArrayList<Message> mTestModel;

    public ViewAdapter(ArrayList<Message> mTestModel) {
        this.mTestModel=mTestModel;
    }

    @NonNull
    @Override
    public ViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_cust_view_details,viewGroup,false);
        return new ViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAdapter.ViewHolder viewHolder, int i) {

        //viewHolder.cx_name.setText(mTestModel.get(i).getId());
        //viewHolder.cx_name2.setText(mTestModel.get(i).getAgentId());
        //viewHolder.cx_name3.setText(mTestModel.get(i).getAgentType());
        viewHolder.cx_name4.setText(mTestModel.get(i).getCustomerName());
        viewHolder.cx_name5.setText(mTestModel.get(i).getStatus());
        //viewHolder.cx_name6.setText(mTestModel.get(i).getPhoto());
        //viewHolder.cx_name7.setText(mTestModel.get(i).getServiceProof());
        //viewHolder.cx_name8.setText(mTestModel.get(i).getCategory());
        //viewHolder.cx_name9.setText(mTestModel.get(i).getCustomFields());
        //viewHolder.cx_name10.setText(mTestModel.get(i).getLocation());
        viewHolder.cx_name11.setText(mTestModel.get(i).getServiceDate());
        viewHolder.cx_name12.setText(mTestModel.get(i).getServiceTime());
        viewHolder.cx_name13.setText(mTestModel.get(i).getMobile());
    }

    @Override
    public int getItemCount() {
        return mTestModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //private TextView cx_name;
        //private TextView cx_name2;
        //private TextView cx_name3;
        private TextView cx_name4;
        private TextView cx_name5;
        //private TextView cx_name6;
        //private TextView cx_name7;
        //private TextView cx_name8;
        //private TextView cx_name9;
        //private TextView cx_name10;
        private TextView cx_name11;
        private TextView cx_name12;
        private TextView cx_name13;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //cx_name=(TextView) itemView.findViewById(R.id.txt_test1);
            //cx_name2=(TextView) itemView.findViewById(R.id.txt_test2);
            //cx_name3=(TextView) itemView.findViewById(R.id.txt_test3);
            cx_name4=(TextView) itemView.findViewById(R.id.txt_test4);
            cx_name5=(TextView) itemView.findViewById(R.id.txt_test5);
            //cx_name6=(TextView) itemView.findViewById(R.id.txt_test6);
            //cx_name7=(TextView) itemView.findViewById(R.id.txt_test7);
            //cx_name8=(TextView) itemView.findViewById(R.id.txt_test8);
            //cx_name9=(TextView) itemView.findViewById(R.id.txt_test9);
            cx_name11=(TextView) itemView.findViewById(R.id.txt_test11);
            cx_name12=(TextView) itemView.findViewById(R.id.txt_test12);
            cx_name13=(TextView) itemView.findViewById(R.id.txt_test13);
        }
    }
}