package com.trantien.news.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.trantien.news.callbacks.ILoadMore;
import com.trantien.news.databinding.ItemLoadingBinding;
import com.trantien.news.databinding.RecyItemBinding;
import com.trantien.news.models.headline.Article;

import java.util.ArrayList;
import java.util.List;

public class ArticleAdapter extends ListAdapter<Article, RecyclerView.ViewHolder> {
    private static final int TYPE_DATA = 0;
    private static final int TYPE_LOADING = 1;


    private static final DiffUtil.ItemCallback<Article> DIFF_CALLBACK = new DiffUtil.ItemCallback<Article>() {
        @Override
        public boolean areItemsTheSame(@NonNull Article oldItem, @NonNull Article newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) && oldItem.getSource().equals(newItem.getSource());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Article oldItem, @NonNull Article newItem) {
            return oldItem.equals(newItem);
        }
    };
    private static final String TAG = "ArticleAdapter";
    private OnItemClickListener listener;
    private ILoadMore listenerLoad;

    public ArticleAdapter(@NonNull OnItemClickListener listener, @NonNull ILoadMore listenerLoad) {
        super(DIFF_CALLBACK);

        Log.d(TAG, "ArticleAdapter: ");
        this.listener = listener;
        this.listenerLoad = listenerLoad;
    }

    @Override
    public int getItemViewType(int position) {
        Log.d(TAG, "getItemViewType: " + position);
        return getItem(position) == null ? TYPE_LOADING : TYPE_DATA;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: " + viewType);
        switch (viewType) {
            case TYPE_LOADING:
                return new LoadingViewHolder(ItemLoadingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }

        return new DataViewHolder(RecyItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_LOADING:
                ((LoadingViewHolder) holder).showLoading();
                break;
            case TYPE_DATA:
                ((DataViewHolder) holder).bindData(getItem(position));
                break;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public void submitList(@Nullable List<Article> list) {
        super.submitList(list != null ? new ArrayList<>(list) : null);
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {
        private RecyItemBinding binding;

        public DataViewHolder(@NonNull RecyItemBinding itemBinding) {
            super(itemBinding.getRoot());
            binding = itemBinding;

            itemView.setOnClickListener((v) -> listener.onItemClick(v, getAdapterPosition()));
        }

        public void bindData(Article article) {
            binding.setArticle(article);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        private ItemLoadingBinding binding;

        public LoadingViewHolder(@NonNull ItemLoadingBinding itemBinding) {
            super(itemBinding.getRoot());
            binding = itemBinding;
        }

        public void showLoading() {
            listenerLoad.onLoadMore();
            binding.progressBar.setIndeterminate(true);
        }
    }
}
