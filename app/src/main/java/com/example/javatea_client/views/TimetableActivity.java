package com.example.javatea_client.views;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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

import com.example.javatea_client.viewModels.LectureViewModel;
import com.example.javatea_client.viewModels.TimetableViewModel;
import com.example.javatea_client.R;

import java.util.*;

public class TimetableActivity extends AppCompatActivity {
    //ユーザ情報
    private String userId;
    private String token;
    //ViewModel
    private TimetableViewModel timetableViewModel;
    private LectureViewModel lectureViewModel;
    private LinearLayout layout;
    private PopupWindow popup;
    private TextView selectedYearTextView;
    private GridLayout timetable;
    private TextView currentSemester;
    //時間割が作られた年度一覧
    private ArrayList<String> years;
    private HashMap<Integer,HashSet<String>> timetableLecturesMap;//年度と授業IDのmap
    private HashMap<String,HashMap<Integer,TextView>> currentTimetable;//時間割
    private boolean isDecidedYear;
    //JavaTea javatea;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //dpに変換するコード
        float density = getResources().getDisplayMetrics().density;
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_timetable);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //ユーザ情報の取得
        //Javatea javaTea = (Javatea) getApplication();
        userId = "test01";
        token = "ffa8ee3c-7e70-45bd-91a2-300214ae3e33";

        //ViewModelの初期化
        timetableViewModel = new ViewModelProvider(this).get(TimetableViewModel.class);
        //lectureViewModel = new ViewModelProvider(this).get(LectureViewModel.class);

        //時間割が作られた年度一覧の初期化
        years = new ArrayList<>();
        //年度ごとの授業の状態を保存するMapの初期化
        timetableLecturesMap = new HashMap<>();

        //エラー表示
        timetableViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s != null){
                    Toast.makeText(TimetableActivity.this,s, Toast.LENGTH_SHORT).show();
                }
            }
        });
//        lectureViewModel.getError().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//                if(s != null){
//                    Toast.makeText(TimetableActivity.this,s, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        //ModeBarのセットアップ
        ModeBar.setup(this,"時間割");
        //Navigationのセットアップ
        Navigation.setup(this);
        //年度の部
        //選んだ年度を表示するTextView
        selectedYearTextView = createSelectedYearTextView();
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        popup = new PopupWindow(layout,(int) (200*density),ViewGroup.LayoutParams.WRAP_CONTENT,true/*タップで閉じる*/);
        //PopupWindowを閉じたときの動き
        popup.setOnDismissListener(() -> {
            //Popupを開く前に開いていた情報をそのまま表示する。
            selectedYearTextView.setText(selectedYearTextView.getText().toString().substring(0, selectedYearTextView.getText().toString().length()-2) + " ▼");
        });
        //年度と時間割の取得
//        timetableViewModel.loadTimetable(userId,token);
        //新しい年度が追加されたら更新
        timetableViewModel.getTimetable().observe(this, new Observer<TreeMap<Integer, HashSet<String>>>() {
            @Override
            public void onChanged(TreeMap<Integer, HashSet<String>> timetable) {
                Log.d("TIMETABLE", "onChanged");
                ArrayList<Integer> integers = new ArrayList<>(timetable.keySet());
                Collections.sort(integers);
                years.clear();
                for(int i=0;i<integers.size();i++){
                    int year = integers.get(i);
                    //時間割を保存するmapも一緒に更新
                    if(!timetableLecturesMap.containsKey(year)){
                        timetableLecturesMap.put(year,timetable.get(year));
                    }
                }
                setYearsFromTimetableLecturesMap();
//                String currentYear = selectedYearTextView.getText().toString().substring(0, selectedYearTextView.getText().toString().length()-2);
                updateLayout();
            }
        });
        //Popupを表示する
        selectedYearTextView.setOnClickListener(v ->{
            //一番初めは、一つも年度がないため追加してもらう。
            if("＋年度を追加する".contentEquals(selectedYearTextView.getText())){
                AlertDialog.Builder builder = createAddYearAlertDialogBuilder();
                builder.show();
                return;
            }
            layout.removeAllViews();
            for(int i=0;i<years.size();i++){
                TextView yearTextView = createYearTextView(i);
                String year = years.get(i);
                yearTextView.setOnClickListener(view ->{
                    if(year.equals("＋年度を追加する")){
                        AlertDialog.Builder builder = createAddYearAlertDialogBuilder();
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

        //時間割の部
        GridLayout timetable = createTimetableGridLayout();

    }

    //選んだ年度を表示するTextViewを作成
    private TextView createSelectedYearTextView(){
        TextView selectedYearTextView = findViewById(R.id.pop_up_window);
        selectedYearTextView.setGravity(Gravity.CENTER);
        selectedYearTextView.setTextSize(20);
        selectedYearTextView.setBackgroundResource(R.drawable.cell_border);
        selectedYearTextView.setText("＋年度を追加する");
        isDecidedYear = false;
        return selectedYearTextView;
    }
    //years(表示される年度の候補のlist)を更新するメソッド
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
    private void updateLayout(){
        layout.removeAllViews();
        for(int i=0;i<years.size();i++){
            TextView yearTextView = createYearTextView(i);
            String year = years.get(i);
            yearTextView.setOnClickListener(view ->{
                if(year.equals("＋年度を追加する")){
                    AlertDialog.Builder builder = createAddYearAlertDialogBuilder();
                    builder.show();
                }else{
                    selectedYearTextView.setText(year + " ▼");
                }
                popup.dismiss();//popupを閉じる
            });
            layout.addView(yearTextView);
        }
        if(selectedYearTextView.getText().toString().equals("＋年度を追加する")){
            if(years.get(0).equals("＋年度を追加する")){
                selectedYearTextView.setText(years.get(0));
            }else{
                isDecidedYear = true;
                selectedYearTextView.setText(years.get(0) + " ▼");
            }
        }else{
            isDecidedYear = true;
        }
    }
    //年度の候補を表示するtextViewを作成する(偶数番目と奇数番目で色を変える)
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
    private AlertDialog.Builder createAddYearAlertDialogBuilder(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] selectYears = new String[40];
        for(int i=2006;i<2046;i++){
            selectYears[i-2006] = String.valueOf(i);
        }
        builder.setItems(selectYears, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int year = Integer.parseInt(selectYears[which]);
//                timetableViewModel.addYear(userId,year,token);
                dialog.dismiss();
            }
        });
        return builder;
    }
    //時間割表のUIを作成する
    private GridLayout createTimetableGridLayout(){
        timetable = findViewById(R.id.grid);
        String[] day = {"月","火","水","木","金"};
        currentTimetable = new HashMap<>();
        for(int row=0;row<=7;row++){
            for(int col=0;col<=5;col++){
                TextView textView = new TextView(this);
                textView.setBackgroundResource(R.drawable.cell_border);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;//幅は自動設定
                params.height = 0;
                if(row == 0&&col > 0){
                    textView.setText(day[col-1]);
                }else if(col == 0&&row > 0){
                    textView.setText(String.valueOf(row));
                }else if(col == 0){
                    if(((String)selectedYearTextView.getText()).contains("前期")){
                        textView.setText("前期");
                    }else if(((String)selectedYearTextView.getText()).contains("後期")){
                        Log.d("前期、後期",selectedYearTextView.getText().toString());
                        textView.setText("後期");
                    }
                }
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(16);
                params.rowSpec = GridLayout.spec(row,row == 0 ? 0.7f : 1f);//rowが0の時幅を小さくする
                params.columnSpec = GridLayout.spec(col,col == 0 ? 0.7f : 1f);//colが0の時幅を小さくする
                textView.setLayoutParams(params);
                textView.setTag(new Point(row,col));
                if(col != 0&&row != 0){
                    if(!currentTimetable.containsKey(day[col-1])){
                        currentTimetable.put(day[col-1],new HashMap<>());
                    }
                    currentTimetable.get(day[col-1]).put(row,textView);
                    textView.setOnLongClickListener(v -> {
                        if(!isDecidedYear){
                            return false;
                        }
                        Intent intent = new Intent(TimetableActivity.this,SetTimetableActivity.class);
                        Point p = (Point) v.getTag();
                        intent.putExtra("year",Integer.parseInt(selectedYearTextView.getText().toString().substring(0, selectedYearTextView.getText().toString().length()-2)));
                        intent.putExtra("day",day[p.y-1]);
                        intent.putExtra("period",p.x);
                        startActivity(intent);
                        return true;
                    });
                }else if(col == 0&&row == 0){
                    currentSemester = textView;
                    timetable.addView(currentSemester);
                    continue;
                }
                timetable.addView(textView);
            }
        }
        return timetable;
    }

    private void setCurrentTimetable(){
        String curSemester;

        if(((String)selectedYearTextView.getText()).contains("前期")){
            curSemester = "前期";
            currentSemester.setText(curSemester);
        }else if(((String)selectedYearTextView.getText()).contains("後期")){
            curSemester = "後期";
            currentSemester.setText(curSemester);
        }else{
            curSemester ="";
            currentSemester.setText(curSemester);
        }

        for(String lectureId:timetableLecturesMap.get(selectedYearTextView.getText())){
            //Lecture lecture = lectureViewModel.getLecture(lectureId);
            String name = "基礎英語";
            Integer grade = 2;
            String semester = "前期";
            Integer frame = 2;//コマ数
            String day = "水";
            Integer period = 2;
            if(semester.equals(curSemester)||semester.equals("通年")){
                for(int i=0;i<frame;i++){
                    TextView curLectureTextView = currentTimetable.get(day).get(period + i);
                    curLectureTextView.setText(name);
                    currentTimetable.get(day).put(period,curLectureTextView);
                    timetable.addView(curLectureTextView);
                }
            }
        }
    }

    private float startY;

    // スワイプ開始位置（画面上端から100px以内）
    private static final float START_AREA = 600f;

    // 下方向へこの距離以上動いたら発火
    private static final float SWIPE_THRESHOLD = 100f;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(!isDecidedYear){
            return super.dispatchTouchEvent(ev);
        }
        switch (ev.getAction()) {

            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();
                break;

            case MotionEvent.ACTION_UP:

                float endY = ev.getY();
                float diffY = endY - startY;

                // 画面上部から下方向へスワイプ
                if (startY <= START_AREA &&
                        diffY >= SWIPE_THRESHOLD) {

//                    Intent intent =
//                            new Intent(this, otherLecturesActivity.class);
//                    startActivity(intent);

                    return true;
                }

                break;
        }

        return super.dispatchTouchEvent(ev);
    }
}