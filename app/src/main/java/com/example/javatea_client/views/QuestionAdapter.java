package com.example.javatea_client.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javatea_client.R;
import com.example.javatea_client.models.Question;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    // クリックイベント通知用のインターフェース
    public interface OnItemClickListener {
        void onItemClick(Question question);
    }

    private final List<Question> questionList;
    private final OnItemClickListener listener;

    public QuestionAdapter(List<Question> questionList, OnItemClickListener listener) {
        this.questionList = questionList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 1件分のレイアウトXML（item_question.xml）を読み込む
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questionList.get(position);
        holder.bind(question, listener);
    }

    @Override
    public int getItemCount() {
        return questionList != null ? questionList.size() : 0;
    }

    // ViewHolder: 1つのリスト項目内のViewを保持・管理
    static class QuestionViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
//        private final TextView tvBody;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            // item_question.xml 内の ID に合わせて指定
            tvTitle = itemView.findViewById(R.id.tv_item_title);
//            tvBody = itemView.findViewById(R.id.tv_item_body);
        }

        public void bind(Question question, OnItemClickListener listener) {
            if (question != null) {
                if (tvTitle != null) {
                    tvTitle.setText(question.getTitle());
                }
//                if (tvBody != null) {
//                    tvBody.setText(question.getBody());
//                }

                // アイテムタップ時の処理
                itemView.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onItemClick(question);
                    }
                });
            }
        }
    }
}