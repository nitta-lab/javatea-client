package com.example.javatea_client.views;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.javatea_client.viewModels.TimetableViewModel;
import com.example.javatea_client.R;
import com.example.javatea_client.viewModels.UserViewModel;

import java.util.*;

public class TimetableActivity extends AppCompatActivity {
    private TimetableViewModel timetableViewModel;
    //時間割が作られた年度一覧
    private ArrayList<String> years;
    private HashMap<Integer,HashMap<String,HashMap<String,HashMap<Integer,String>>>> timetableLecturesMap;
    private TextView selectedYearTextView;
    //JavaTea javatea;
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
        //timetableViewModelの初期化
        timetableViewModel = new ViewModelProvider(this).get(TimetableViewModel.class);
        //エラー表示
        timetableViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s != null){
                    Toast.makeText(TimetableActivity.this,s, Toast.LENGTH_SHORT).show();
                }
            }
        });
        //ユーザ情報の取得
        //javaTea = (JavaTea)getApplication();
        String userId = null;//後々Applicationからもらってくる予定
        String token = null;
        //dpに変換するコード
        float density = getResources().getDisplayMetrics().density;
        //ModeBarのセットアップ
        ModeBar.setup(this,"時間割");
        //Navigationのセットアップ
        Navigation.setup(this);
        //選んだ年度を表示するTextView
        selectedYearTextView = createSelectedYearTextView();
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        PopupWindow popup = new PopupWindow(layout,(int) (200*density),ViewGroup.LayoutParams.WRAP_CONTENT,true/*タップで閉じる*/);
        //PopupWindowを閉じたときの動き
        popup.setOnDismissListener(() -> {
            //Popupを開く前に開いていた情報をそのまま表示する。
            selectedYearTextView.setText(selectedYearTextView.getText().toString().substring(0, selectedYearTextView.getText().toString().length()-2) + " ▼");
        });
        //時間割が作られた年度一覧
        years = new ArrayList<>();
        //時間割表の状態を保存するMap
        timetableLecturesMap = new HashMap<>();
        //時間割が作られた年度の取得
        //timetableViewModel.loadTimetableYears(userId,token);
        //新しい年度が追加されたら更新
        timetableViewModel.getYears().observe(this, new Observer<List<Integer>>() {
            @Override
            public void onChanged(List<Integer> integers) {
                Collections.sort(integers);
                years.clear();
                for(int i=0;i<integers.size();i++){
                    int year = integers.get(i);
                    //時間割を保存するmapも一緒に更新
                    if(!timetableLecturesMap.containsKey(year)){
                        timetableLecturesMap.put(year,new HashMap<>());
                        timetableLecturesMap.get(year).put("前期",new HashMap<>());
                        timetableLecturesMap.get(year).put("後期",new HashMap<>());
                    }
                }
                setYearsFromTimetableLecturesMap();
                selectedYearTextView.setText(years.get(0) + " ▼");
                String currentYear = selectedYearTextView.getText().toString().substring(0, selectedYearTextView.getText().toString().length()-2);
                selectedYearTextView.setText(currentYear + " ▲");
                layout.removeAllViews();
                for(int i=0;i<years.size();i++){
                    TextView yearTextView = createYearTextView(i);
                    String year = years.get(i);
                    yearTextView.setOnClickListener(view ->{
                        if(year.equals("＋年度を追加する")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(TimetableActivity.this);
                            String[] selectYears = new String[40];
                            for(int j=2006;j<2046;j++){
                                selectYears[j-2006] = String.valueOf(j);
                            }
                            builder.setItems(selectYears, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int year = Integer.parseInt(selectYears[which]);
                                    //timetableViewModel.addYear(userId,year,token);
                                    if(!timetableLecturesMap.containsKey(year)){
                                        timetableLecturesMap.put(year,new HashMap<>());
                                        timetableLecturesMap.get(year).put("前期",new HashMap<>());
                                        timetableLecturesMap.get(year).put("後期",new HashMap<>());
                                        setYearsFromTimetableLecturesMap();
                                        selectedYearTextView.setText(year + "前期 ▼");
                                    }
                                    dialog.dismiss();
                                }
                            });
                        }else{
                            selectedYearTextView.setText(year + "前期 ▼");
                        }
                        popup.dismiss();//popupを閉じる
                    });
                    layout.addView(yearTextView);
                }
            }
        });
        //Popupを表示する
        selectedYearTextView.setOnClickListener(v ->{
            //一番初めは、一つも年度がないため追加してもらう。
            if("＋年度を追加する".contentEquals(selectedYearTextView.getText())){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                String[] selectYears = new String[40];
                for(int i=2006;i<2046;i++){
                    selectYears[i-2006] = String.valueOf(i);
                }
                builder.setItems(selectYears, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year = Integer.parseInt(selectYears[which]);
                        //timetableViewModel.addYear(userId,year,token);
                        if(!timetableLecturesMap.containsKey(year)){
                            timetableLecturesMap.put(year,new HashMap<>());
                            timetableLecturesMap.get(year).put("前期",new HashMap<>());
                            timetableLecturesMap.get(year).put("後期",new HashMap<>());
                            setYearsFromTimetableLecturesMap();
                            selectedYearTextView.setText(year + "前期 ▼");
                        }
                        dialog.dismiss();
                    }
                });
                builder.show();
//                Intent intent = new Intent(TimetableActivity.this,RegisterActivity.class);
//                startActivity(intent);
                return;
            }
            layout.removeAllViews();
            for(int i=0;i<years.size();i++){
                TextView yearTextView = createYearTextView(i);
                String year = years.get(i);
                yearTextView.setOnClickListener(view ->{
                    if(year.equals("＋年度を追加する")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        String[] selectYears = new String[40];
                        for(int j=2006;j<2046;j++){
                            selectYears[j-2006] = String.valueOf(j);
                        }
                        builder.setItems(selectYears, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int year = Integer.parseInt(selectYears[which]);
                                //timetableViewModel.addYear(userId,year,token);
                                if(!timetableLecturesMap.containsKey(year)){
                                    timetableLecturesMap.put(year,new HashMap<>());
                                    timetableLecturesMap.get(year).put("前期",new HashMap<>());
                                    timetableLecturesMap.get(year).put("後期",new HashMap<>());
                                    setYearsFromTimetableLecturesMap();
                                    selectedYearTextView.setText(year + "前期 ▼");
                                }
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }else{
                        selectedYearTextView.setText(year + " ▼");
                    }
                    popup.dismiss();//popupを閉じる
                });
                layout.addView(yearTextView);
            }
            popup.showAsDropDown(v, selectedYearTextView.getWidth() - popup.getWidth(),0);//view,幅,高さ
        });
        GridLayout timetable = findViewById(R.id.grid);
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
            timetable.addView(textView);
        }
        timetableViewModel.getLectureIds().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
            }
        });
    }
    //選んだ年度を表示するTextViewを作成
    private TextView createSelectedYearTextView(){
        TextView selectedYearTextView = findViewById(R.id.pop_up_window);
        selectedYearTextView.setGravity(Gravity.CENTER);
        selectedYearTextView.setTextSize(20);
        selectedYearTextView.setBackgroundResource(R.drawable.cell_border);
        selectedYearTextView.setText("＋年度を追加する");
        return selectedYearTextView;
    }

    private void setYearsFromTimetableLecturesMap(){
        years.clear();
        TreeSet<Integer> timetable = new TreeSet<>(timetableLecturesMap.keySet());
        if(timetable.isEmpty()){
            years.add("＋年度を追加する");
            return;
        }
        for(int year:timetable){
            years.add(year + "前期");
            years.add(year + "後期");
        }
        years.add("＋年度を追加する");
    }
    private TextView createYearTextView(int i){
        TextView yearTextView = new TextView(this);
        String year = years.get(i);
        yearTextView.setText(year);
        yearTextView.setTextSize(20);
        yearTextView.setGravity(Gravity.CENTER);
        if(i % 2 == 0){
            yearTextView.setBackgroundColor(Color.rgb(220,220,220));
        }else if(year.equals("＋年度を追加する")){
            yearTextView.setBackgroundColor(Color.WHITE);
        }else{
            yearTextView.setBackgroundColor(Color.rgb(180,180,180));
        }
        return yearTextView;
    }
    private GridLayout createTimetableGridLayout(){
        GridLayout timetable = findViewById(R.id.grid);
        for(int row=0;row<=7;row++){
            for(int col=0;col<=5;col++){
                TextView textView = new TextView(this);
                textView.setBackgroundResource(R.drawable.cell_border);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;//幅は自動設定
                params.height = 0;
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
            }
        }
        return timetable;
    }
}