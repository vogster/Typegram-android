package com.unlogicon.typegram.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.unlogicon.typegram.Constants;
import com.unlogicon.typegram.R;
import com.unlogicon.typegram.models.Article;
import com.unlogicon.typegram.ui.activities.ArticleActivity;
import com.unlogicon.typegram.utils.DateUtils;

import java.util.List;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder>  {

    private List<Article> articles;

    private Context context;

    public MainRecyclerViewAdapter(List<Article> articles){
        this.articles = articles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.main_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.description.setText(articles.get(i).getBody().replaceAll("[\n]{2,}", "\n"));
        String author = "@" + articles.get(i).getAuthor();

        viewHolder.author.setText(author);
        viewHolder.date.setText(DateUtils.timeAgo(articles.get(i).getCreatedAt()));
        viewHolder.title.setText(articles.get(i).getTitle());
        if (articles.get(i).getTitle().equals("")){
            viewHolder.title.setVisibility(View.GONE);
        } else {
            viewHolder.title.setVisibility(View.VISIBLE);
            viewHolder.title.setText(articles.get(i).getTitle());
        }

        Glide.with(context)
                .load(Constants.BASE_AVA_URL.replace("%s", articles.get(i).getAuthor()))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(viewHolder.avatar);

        viewHolder.itemLayout.setOnClickListener(v ->{
            if (articles.get(i).getID() != null && articles.get(i).getAuthor() != null) {
                Intent intent = new Intent(context, ArticleActivity.class);
                intent.putExtra(ArticleActivity.ID_EXTRA, String.valueOf(articles.get(i).getID()));
                intent.putExtra(ArticleActivity.USER_EXTRA, articles.get(i).getAuthor());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout itemLayout;

        private AppCompatTextView author;
        private AppCompatTextView description;
        private AppCompatTextView date;
        private AppCompatTextView title;

        private AppCompatImageView avatar;

         ViewHolder(@NonNull View itemView) {
            super(itemView);

            author = itemView.findViewById(R.id.author);
            description = itemView.findViewById(R.id.description);
            date = itemView.findViewById(R.id.date);
            title = itemView.findViewById(R.id.title);

            avatar = itemView.findViewById(R.id.avatar);

            itemLayout = itemView.findViewById(R.id.itemLayout);
        }
    }
}
