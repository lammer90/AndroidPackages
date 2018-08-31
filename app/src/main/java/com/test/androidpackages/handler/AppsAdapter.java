package com.test.androidpackages.handler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.androidpackages.model.AppInfo;
import com.test.androidpackages.R;

import java.util.ArrayList;
import java.util.List;

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.ElementViewHolder> {
    private List<AppInfo> infos = new ArrayList<>();

    @NonNull
    @Override
    public ElementViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.view_item_app, viewGroup, false);
        return new ElementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ElementViewHolder viewHolder, int i) {
        AppInfo info = infos.get(i);
        viewHolder.iconView.setImageDrawable(info.getIcon());
        viewHolder.name.setText(info.getName());
        viewHolder.version.setText(info.getVersionName());
    }

    @Override
    public int getItemCount() {
        return infos.size();
    }

    public void setInfos(List<AppInfo> infos) {
        this.infos = infos;
    }

    static class ElementViewHolder extends RecyclerView.ViewHolder{
        private final ImageView iconView;
        private final TextView name;
        private final TextView version;


        ElementViewHolder(@NonNull View itemView) {
            super(itemView);

            iconView = itemView.findViewById(R.id.icon_iv);
            name = itemView.findViewById(R.id.name_tv);
            version = itemView.findViewById(R.id.version_tv);
        }
    }
}
