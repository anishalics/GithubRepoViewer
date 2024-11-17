package com.anishali.githubrepoviewer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.RepoViewHolder> {

    private List<Repo> repoList;

    public RepoAdapter(List<Repo> repoList) {
        this.repoList = repoList;
    }

    @NonNull
    @Override
    public RepoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new RepoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepoViewHolder holder, int position) {
        Repo repo = repoList.get(position);
        holder.name.setText(repo.getName());
        holder.description.setText(repo.getDescription());
    }

    @Override
    public int getItemCount() {
        return repoList.size();
    }

    static class RepoViewHolder extends RecyclerView.ViewHolder {
        TextView name, description;

        public RepoViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(android.R.id.text1);
            description = itemView.findViewById(android.R.id.text2);
        }
    }
}