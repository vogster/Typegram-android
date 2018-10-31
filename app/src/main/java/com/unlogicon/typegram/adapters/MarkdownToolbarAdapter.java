package com.unlogicon.typegram.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unlogicon.typegram.R;
import com.unlogicon.typegram.enums.MarkdownToolBarEnum;
import com.unlogicon.typegram.interfaces.listeners.MarkdownToolbarCLickListeners;
import com.unlogicon.typegram.markdown.MarkdownTextActions;

import java.util.List;

/**
 * Nikita Korovkin 22.10.2018.
 */
public class MarkdownToolbarAdapter extends RecyclerView.Adapter<MarkdownToolbarAdapter.ViewHolder> {

    private Context context;

    private List<MarkdownToolBarEnum> toolBarEnums;

    private MarkdownToolbarCLickListeners cLickListeners;

    private MarkdownTextActions markdownTextActions;

    public MarkdownToolbarAdapter() {
        toolBarEnums = MarkdownToolBarEnum.asList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.markdown_toolbar_item, viewGroup, false);
        return new MarkdownToolbarAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.icon.setImageResource(toolBarEnums.get(i).getDrawableId());
        if (cLickListeners != null){
            viewHolder.icon.setOnClickListener(v -> {
//                cLickListeners.onClick(toolBarEnums.get(i));
                markdownTextActions.onClick(toolBarEnums.get(i));
            });
        }
    }

    @Override
    public int getItemCount() {
        return toolBarEnums.size();
    }

    public void setMarkdownActions(MarkdownTextActions markdownTextActions) {
        this.markdownTextActions = markdownTextActions;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
        }
    }

    public void setcLickListeners(MarkdownToolbarCLickListeners cLickListeners) {
        this.cLickListeners = cLickListeners;
    }
}
