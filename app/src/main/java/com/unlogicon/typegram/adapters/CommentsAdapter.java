package com.unlogicon.typegram.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
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
import com.unlogicon.typegram.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import ru.noties.markwon.SpannableConfiguration;
import ru.noties.markwon.il.AsyncDrawableLoader;
import ru.noties.markwon.il.NetworkSchemeHandler;
import ru.noties.markwon.view.MarkwonViewCompat;

/**
 * Nikita Korovkin 16.10.2018.
 */
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private Context context;

    private List<Article> comments;

    public CommentsAdapter(List<Article> comments){
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.comment_list_item, viewGroup, false);
        return new CommentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.ViewHolder viewHolder, int i) {

        SpannableConfiguration spannableConfiguration = SpannableConfiguration.builder(context)
                .asyncDrawableLoader(AsyncDrawableLoader.builder()
                        .executorService(Executors.newCachedThreadPool())
                        .addSchemeHandler(NetworkSchemeHandler.create(new OkHttpClient()))
                        .build())

                .build();
        viewHolder.comment.setMarkdown(spannableConfiguration, comments.get(i).getBody());

        String author = "@" + comments.get(i).getAuthor();

        viewHolder.author.setText(author);
        viewHolder.date.setText(DateUtils.timeAgo(comments.get(i).getCreatedAt()));

        Glide.with(context)
                .load(Constants.BASE_AVA_URL.replace("%s", comments.get(i).getAuthor()))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(viewHolder.avatar);
    }


    @Override
    public int getItemCount() {
        return comments != null ? comments.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView avatar;
        private AppCompatTextView author;
        private AppCompatTextView date;
        private MarkwonViewCompat comment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            avatar = itemView.findViewById(R.id.avatar);
            author = itemView.findViewById(R.id.author);
            date = itemView.findViewById(R.id.date);
            comment = itemView.findViewById(R.id.comment);
        }
    }
}
