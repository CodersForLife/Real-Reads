package com.vizy.newsapp.realread.adapter;

import android.content.Context;
import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vizy.newsapp.realread.R;
import com.vizy.newsapp.realread.model.Article;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private List<Article> articleList;
    public Context context;

    public ArticleAdapter(List<Article> articleList, Context context){
        this.articleList=articleList;
        this.context=context;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item= LayoutInflater.from(parent.getContext()).inflate(R.layout.home_screen, parent, false);
        return new ArticleViewHolder(item);    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        final Article article=articleList.get(position);
        holder.title.setText(article.getTitle());
        holder.description.setText(article.getDescription());
        Picasso.with(context).load(article.getUrlToImage()).into(holder.newsImage);
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder{

        private ImageView newsImage;
        private TextView title;
        private TextView description;
        private Button share;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            newsImage=(ImageView)itemView.findViewById(R.id.news_image);
            title=(TextView)itemView.findViewById(R.id.news_heading);
            description=(TextView)itemView.findViewById(R.id.news_description);
            share=(Button)itemView.findViewById(R.id.share1);
        }
    }
}
