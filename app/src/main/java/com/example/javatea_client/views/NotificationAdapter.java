package com.example.javatea_client.views;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javatea_client.R;
import com.example.javatea_client.models.Question;

import java.util.List;
import java.util.Map;
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    // 通知として表示するQuestion一覧
    private final List<Question> questionList;
    private final boolean isBestAnswerNotification;
    private final Map<String, String> userNames;

    // ActivityからQuestion一覧を受け取る
    public NotificationAdapter(List<Question> questionList, boolean isBestAnswerNotification, Map<String, String> userNames) {
        this.questionList = questionList;
        this.isBestAnswerNotification = isBestAnswerNotification;
        this.userNames = userNames;
    }

    // Questionの件数を返す
    @Override
    public int getItemCount() {
        return questionList.size();
    }

    // 通知1行分の部品を保持する
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView notificationTitle;
        public final TextView answerCount;

        public ViewHolder(@NonNull View view) {
            super(view);

            notificationTitle = view.findViewById(R.id.tvNotificationTitle);
            answerCount = view.findViewById(R.id.tvAnswerCount);
        }
    }

    // item_notification.xmlを使って1行分を作る
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    // Questionのデータを1行分のViewに入れる
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Question item = questionList.get(position);

        // 質問タイトルを表示
        if (isBestAnswerNotification) {
            String name = userNames.get(item.getUid());

            if (name == null) {
                name = "質問者";
            }

            holder.notificationTitle.setText(name + "からベストアンサーに選ばれました！\n（Q：" + item.getTitle() + "）");
            holder.answerCount.setVisibility(View.GONE);

        } else {
            holder.notificationTitle.setText("Q：" + item.getTitle());
            holder.answerCount.setVisibility(View.VISIBLE);
        }

        // 質問タイトルを押したとき
        holder.notificationTitle.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), AnswerListActivity.class);

            // 押した質問のqidを渡す
            intent.putExtra("qid", item.getQid());

            // 押した質問のタイトルを渡す
            intent.putExtra("title", item.getTitle());

            // 回答一覧画面へ移動
            v.getContext().startActivity(intent);
        });
    }
}