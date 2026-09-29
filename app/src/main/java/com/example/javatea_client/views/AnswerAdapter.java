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

    //表示する回答一覧
    private final List<Answer> answerList;

    //ベストアンサーの回答ID
    private final String bestAnswerId;

    //回答が押されたことをFragmentへ伝えるためのlistener
    private final OnAnswerClickListener listener;


    //コンストラクタ
    public AnswerAdapter(List<Answer> answerList, String bestAnswerId, OnAnswerClickListener listener) {
        this.answerList = answerList;
        this.bestAnswerId = bestAnswerId;
        this.listener = listener;
    }


    //回答1件分のXMLを作成する
    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_answer, parent, false);

        return new AnswerViewHolder(view);

    }


    //回答データをXMLに表示する
    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {

        //表示する位置の回答を1件取得
        Answer answer = answerList.get(position);

        //回答者名を表示
        holder.tvAnswerUser.setText(answer.getName() + "さんの回答");

        //現在の回答がベストアンサーか確認
        if (answer.getAid() != null && answer.getAid().equals(bestAnswerId)) {

            holder.tvBestAnswer.setVisibility(View.VISIBLE);

        } else {

            holder.tvBestAnswer.setVisibility(View.GONE);
        }

        //回答1件分の欄が押されたとき
        holder.itemView.setOnClickListener(view -> {

            //押された回答をFragmentへ渡す
            listener.onAnswerClick(answer);
        });
    }


    //表示する回答数を返す
    @Override
    public int getItemCount() {
        return answerList.size();
    }


    //回答1件分のXML部品を保持する
    public static class AnswerViewHolder extends RecyclerView.ViewHolder {

        TextView tvAnswerUser;
        TextView tvBestAnswer;

        public AnswerViewHolder(@NonNull View itemView) {
            super(itemView);

            //item_answer.xmlとの接続
            tvAnswerUser = itemView.findViewById(R.id.tvAnswerUser);

            tvBestAnswer = itemView.findViewById(R.id.tvBestAnswer);
        }
    }


    //回答が押されたことをFragmentへ伝えるためのinterface
    public interface OnAnswerClickListener {

        void onAnswerClick(Answer answer);
    }
}