package com.example.javatea_client.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javatea_client.R;
import com.example.javatea_client.models.Answer;

import java.util.List;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder> {

    private final List<Answer> answerList;
    private final String bestAnswerId;
    private final OnAnswerClickListener listener;

    public AnswerAdapter(List<Answer> answerList, String bestAnswerId, OnAnswerClickListener listener) {
        this.answerList = answerList;
        this.bestAnswerId = bestAnswerId;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_answer, parent, false);

        return new AnswerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {

        Answer answer = answerList.get(position);

        holder.tvAnswerUser.setText(answer.getName() + "さんの回答");

        if (answer.getAid() != null && answer.getAid().equals(bestAnswerId)) {

            holder.tvBestAnswer.setVisibility(View.VISIBLE);

        } else {
            holder.tvBestAnswer.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(view -> {
            listener.onAnswerClick(answer);
        });
    }

    @Override
    public int getItemCount() {
        return answerList.size();
    }

    public static class AnswerViewHolder extends RecyclerView.ViewHolder {

        TextView tvAnswerUser;
        TextView tvBestAnswer;

        public AnswerViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAnswerUser = itemView.findViewById(R.id.tvAnswerUser);

            tvBestAnswer = itemView.findViewById(R.id.tvBestAnswer);
        }
    }

    public interface OnAnswerClickListener {
        void onAnswerClick(Answer answer);
    }
}