package com.test.androidpackages.handler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.androidpackages.R;

import java.io.File;
import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {
    private List<File> files;
    private Context context;
    private FileClickListener fileClickListener;

    private final int DIRECTORY_TYPE = 0;
    private final int FILE_TYPE = 1;

    public FileAdapter(Context context) {
        this.context = context;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public void setFileClickListener(FileClickListener fileClickListener) {
        this.fileClickListener = fileClickListener;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //i - DIRECTORY_TYPE or FILE_TYPE
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.view_item_file, viewGroup, false);
        view.setOnClickListener(s -> fileClickListener.onFileClick((File)s.getTag()));
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder fileViewHolder, int i) {
        if (files.get(i).isDirectory()){
            fileViewHolder.imageView.setImageResource(R.drawable.ic_directory_92dp);
        }
        else {
            fileViewHolder.imageView.setImageResource(R.drawable.ic_file_92dp);
        }

        fileViewHolder.textView.setText(files.get(i).getName());
        fileViewHolder.itemView.setTag(files.get(i));
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (files.get(position).isDirectory()) ? DIRECTORY_TYPE : FILE_TYPE;
    }

    public class FileViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView textView;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.im_fl);
            textView = itemView.findViewById(R.id.name_fl);
        }
    }

    public interface FileClickListener{
        void onFileClick(File file);
    }
}
