package com.example.javatea_client.views;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javatea_client.R;
import com.example.javatea_client.models.Lecture;

import java.util.List;

public class LectureAdapter extends RecyclerView.Adapter<LectureAdapter.ViewHolder>{
    private final List<Lecture> lectureList; //科目の情報
    private final Context context; //このリストがある画面の情報(SetTimetableActivity.java)

    //呼び出し時、表示したいデータをフィールドに渡す
    public LectureAdapter(Context context, List<Lecture> lectureList){
        this.lectureList = lectureList;
        this.context = context;
    }

    //渡したデータの長さを取得
    @Override
    public int getItemCount(){
        return lectureList.size();
    }


    //ViewHolderクラスを定義(一行の情報を入れる箱みたいなものを先に作成する)
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final AppCompatButton lectureName;

        //表示したい場所idを紐づけ
        public ViewHolder(@NonNull View view){
            super(view);
            lectureName = view.findViewById(R.id.lecture_name_text); //lecture_name_text:部品のid(item_lecture_list.xml内)
        }
    }


    //取得したデータの長さ分の各行のViewHolderを作成
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lecture_list, parent, false);
        return new ViewHolder(view);
    }

    //データをViewHolderに紐づけ
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position){
        Lecture item = lectureList.get(position); //LectureListから順にデータをitemに入れる
        holder.lectureName.setText(item.getName()); //lectureNameにitemのデータをset

        //項目がクリックされたとき
        holder.lectureName.setOnClickListener(v -> {
            Intent intent = new Intent(context, TimetableActivity.class); //画面遷移
            //intent.putExtra("lectureName", item.getName());
            context.startActivity(intent);
        });
    }
}
