package com.example.myapplication.adapters;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.listeners.OnItemSearchResultClickListener;
import com.example.myapplication.model.SearchResult;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder> {
    private List<SearchResult> list;
    private OnItemSearchResultClickListener listener;

    public SearchResultAdapter(List<SearchResult> list, OnItemSearchResultClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_search, parent, false);
        return new SearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
        SearchResult result = list.get(position);
        if (result == null) {
            return;
        }
        holder.txtDomain.setText(result.getDomain().trim());
        holder.txtDomain.setPaintFlags(holder.txtDomain.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.txtTitle.setText(result.getTitle().trim());
        holder.txtSnippet.setText(result.getSnippet().trim());

        if (result.getImgUrl().trim().equals("") || result.getUrl() == null) {
            holder.img.setVisibility(View.GONE);
        }

        Glide.with(holder.img.getContext())
                .load(result.getImgUrl().trim())
                .into(holder.img);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCLick(result.getTitle().trim(),result.getUrl().trim());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list.size() > 0) {
            return list.size();
        }
        return 0;
    }

    public class SearchResultViewHolder extends RecyclerView.ViewHolder {
        private TextView txtDomain, txtTitle, txtSnippet;
        private ImageView img;
        private LinearLayout layout;

        public SearchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDomain = itemView.findViewById(R.id.txt_custom_search_domain);
            txtTitle = itemView.findViewById(R.id.txt_custom_search_title);
            txtSnippet = itemView.findViewById(R.id.txt_custom_search_snippet);
            img = itemView.findViewById(R.id.img_custom_search);
            layout = itemView.findViewById(R.id.layout_custom_search_item);
        }
    }
}
