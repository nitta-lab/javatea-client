package com.example.javatea_client.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javatea_client.R;
import com.example.javatea_client.models.Lecture;
import com.example.javatea_client.viewModels.TimetableViewModel;

import java.util.List;

public class LectureAdapter extends RecyclerView.Adapter<LectureAdapter.ViewHolder>{
    private final List<Lecture> lectureList; //科目の情報
    private final String userId;
    private final String token;
    private final int year;

    private final TimetableViewModel timetableviewmodel;

    //呼び出し時、表示したいデータをフィールドに渡す
    public LectureAdapter(List<Lecture> lectureList, TimetableViewModel timetableviewmodel, String userId, String token, int year){
        this.lectureList = lectureList;
        this.userId = userId;
        this.token = token;
        this.year = year;
        this.timetableviewmodel = timetableviewmodel;
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

        //項目がクリックされたとき(画面遷移はSetTimetableActivity.java側)
        holder.lectureName.setOnClickListener(v -> {
            timetableviewmodel.addLecture(userId, year, item.getLectureId(), token); //ViewModelに選択された科目を送る
        });
    }
}
