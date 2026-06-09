package com.example.javatea_client.views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.javatea_client.R;

public class TimetableActivity extends AppCompatActivity {
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_timetable);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Navigation.setup(this);//Navigationクラスを呼び出す
        TextView yearSelect = findViewById(R.id.pop_up_window);
        yearSelect.setGravity(Gravity.CENTER);
        yearSelect.setTextSize(16);
        yearSelect.setBackgroundResource(R.drawable.cell_border);
        //yearは持ってくるはず
        String[] years = {
                "2025",
                "2026",
                "＋年度を追加する"
        };
        yearSelect.setText(years[0] + " ▼");
        float density = getResources().getDisplayMetrics().density;
        yearSelect.setOnClickListener(v ->{
            String currentYear = yearSelect.getText().toString().substring(0,yearSelect.getText().toString().length()-2);
            yearSelect.setText(currentYear + " ▲");
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            PopupWindow popup = new PopupWindow(layout,(int) (200*density),ViewGroup.LayoutParams.WRAP_CONTENT,true/*タップで閉じる*/);
            popup.setOnDismissListener(() -> {
                yearSelect.setText(yearSelect.getText().toString().substring(0,yearSelect.getText().toString().length()-2) + " ▼");
            });
            for(int i=0;i<years.length;i++){
                TextView textView = new TextView(this);
                String year = years[i];
                textView.setText(year);
                textView.setGravity(Gravity.CENTER);
                if(i % 2 == 0){
                    textView.setBackgroundColor(Color.rgb(220,220,220));
                }else if(year.equals("＋年度を追加する")){
                    textView.setBackgroundColor(Color.WHITE);
                }else{
                    textView.setBackgroundColor(Color.rgb(180,180,180));
                }
                textView.setOnClickListener(view ->{
                    if(year.equals("＋年度を追加する")){
                        Intent intent = new Intent(TimetableActivity.this,RegisterActivity.class);
                        startActivity(intent);
                    }else{
                        yearSelect.setText(year + " ▼");
                    }
                    popup.dismiss();//popupを閉じる
                });
                layout.addView(textView);
            }
            popup.showAsDropDown(v,yearSelect.getWidth() - popup.getWidth(),0);//view,幅,高さ
        });

        GridLayout gridLayout = findViewById(R.id.grid);
        for(int i=0;i<48;i++){
            TextView textView = new TextView(this);
            textView.setBackgroundResource(R.drawable.cell_border);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;//幅は自動設定
            params.height = 0;
            int row = i/6;
            int col = i%6;
            if(row == 0){
                switch (col){
                    case 1:
                        textView.setText("月");
                        break;
                    case 2:
                        textView.setText("火");
                        break;
                    case 3:
                        textView.setText("水");
                        break;
                    case 4:
                        textView.setText("木");
                        break;
                    case 5:
                        textView.setText("金");
                        break;
                }
                textView.setGravity(Gravity.CENTER);
            }else if(col == 0){
                switch (row){
                    case 1:
                        textView.setText("1");
                        break;
                    case 2:
                        textView.setText("2");
                        break;
                    case 3:
                        textView.setText("3");
                        break;
                    case 4:
                        textView.setText("4");
                        break;
                    case 5:
                        textView.setText("5");
                        break;
                    case 6:
                        textView.setText("6");
                        break;
                    case 7:
                        textView.setText("7");
                        break;
                }
                textView.setGravity(Gravity.CENTER);
            }
            textView.setTextSize(16);
            params.rowSpec = GridLayout.spec(row,row == 0 ? 0.7f : 1f);//rowが0の時幅を小さくする
            params.columnSpec = GridLayout.spec(col,col == 0 ? 0.7f : 1f);//colが0の時幅を小さくする

            textView.setLayoutParams(params);
//            textView.setTag(new Point(row,col));//将来的に授業のIDを保存出来たらうれしい
//            textView.setOnClickListener(v -> {
//                //Q&Aに飛ぶ予定
//            });
            //長押しなら編集
            textView.setOnLongClickListener(v -> {
                Intent intent = new Intent(TimetableActivity.this,SetTimetableActivity.class);
                startActivity(intent);
                return true;
            });

            gridLayout.addView(textView);
        }

    }
}