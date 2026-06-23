package com.example.javatea_client.views;

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
    private final List<Lecture> lectureList;

    //呼び出し時、表示したいデータをフィールドに渡す
    public LectureAdapter(List<Lecture> lectureList){
        this.lectureList = lectureList;
    }

    //渡したデータの長さを取得
    @Override
    public int getItemCount(){
        return lectureList.size();
    }


    //ViewHolderクラスを定義
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final AppCompatButton lectureName;

        //表示したい場所idを紐づけ
        public ViewHolder(@NonNull View view){
            super(view);
            lectureName = view.findViewById(R.id.lecture_name_text);
        }
    }


    //取得したデータの長さ分の各行のビューを作成
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lecture_list, parent, false);
        return new ViewHolder(view);
    }

    //データをビューに紐づけ
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position){
        Lecture item = lectureList.get(position);
        holder.lectureName.setText(item.getName());
    }
}
