package com.example.myapplication.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.listeners.OnItemHistoryClickListener;
import com.example.myapplication.listeners.OnItemHistorySearchClickListener;
import com.example.myapplication.model.Website;

import java.util.List;

public class HistorySearchAdapter extends RecyclerView.Adapter<HistorySearchAdapter.HistorySearchViewHolder> {
    private List<String> searchList;
    private OnItemHistorySearchClickListener listener;

    public HistorySearchAdapter(List<String> searchList, OnItemHistorySearchClickListener listener) {
        this.searchList = searchList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HistorySearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_website_home, parent, false);
        return new HistorySearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistorySearchViewHolder holder, int position) {
        String searchSTR = searchList.get(position);
        holder.txtSearch.setText(searchSTR);
        holder.txtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onclick(searchSTR.trim());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (searchList != null) {
            return searchList.size();
        }
        return 0;
    }

    static class HistorySearchViewHolder extends RecyclerView.ViewHolder {
        private TextView txtSearch;

        public HistorySearchViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSearch = itemView.findViewById(R.id.txt_search_history);
        }
    }
}
