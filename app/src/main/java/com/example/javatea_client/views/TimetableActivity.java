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
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.javatea_client.Javatea;
import com.example.javatea_client.models.Lecture;
import com.example.javatea_client.viewModels.TimetableViewModel;
import com.example.javatea_client.R;

import java.time.LocalDateTime;
import java.util.*;

public class TimetableActivity extends AppCompatActivity {
    //定数
    private final String[] DAYS = {"月","火","水","木","金"};
    private final String FIRST = "前期";
    private final String SECOND = "後期";
    private final String ADD_YEAR = "＋年度を追加する";
    //ユーザ情報
    private String userId;
    private String token;
    private final int currentYear = LocalDateTime.now().getYear();
    private final int currentMonth = LocalDateTime.now().getMonthValue();
    private int intentYear;
    private String intentSemester;
    //ViewModel
    private TimetableViewModel timetableViewModel;
    private LinearLayout layout;
    private PopupWindow popup;
    private TextView selectedYearTextView;
    private GridLayout timetable;
    private TextView currentSemester;
    //時間割が作られた年度一覧
    private ArrayList<String> years;
    private HashMap<Integer, HashSet<Lecture>> timetableLecturesMap;//年度と授業IDのmap
    private HashMap<String, HashMap<Integer, TextView>> currentTimetable;//時間割
    private boolean isDecidedYear;//年度が決まっているかどうか。

    //JavaTea javatea;
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
        Javatea javaTea = (Javatea) getApplication();
        if (javaTea.getView().equals("Register")) {
            getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                }
            });
        }
        javaTea.setView("Timetable");
        userId = javaTea.getUserId();
        token = javaTea.getToken();

        Intent intent = getIntent();
        intentYear = intent.getIntExtra("year", 0);
        intentSemester = intent.getStringExtra("semester");
        //ViewModelの初期化
        timetableViewModel = new ViewModelProvider(this).get(TimetableViewModel.class);

        //エラー表示
        timetableViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    Log.d("Error", s);
                    Toast.makeText(TimetableActivity.this, s, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //ModeBarのセットアップ
        ModeBar.setup(this, "時間割");
        //Navigationのセットアップ
        Navigation.setup(this);
        //年度の部
        //時間割が作られた年度一覧の初期化
        years = new ArrayList<>();
        //年度ごとの授業の状態を保存するMapの初期化
        timetableLecturesMap = new HashMap<>();
        //選んだ年度を表示するTextView
        selectedYearTextView = createSelectedYearTextView();
        //選ぶことが可能な年度を保存するLinearLayout
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        //layoutをPopupWindowに表示する
        popup = new PopupWindow(layout, (int) (200 * density), ViewGroup.LayoutParams.WRAP_CONTENT, true/*タップで閉じる*/);
        //PopupWindowを閉じたときの動き
        popup.setOnDismissListener(() -> {
            //Popupを開く前に開いていた情報をそのまま表示する。
            if(!getSelectedYearText().equals((ADD_YEAR))){
                selectedYearTextView.setText(getSelectedYearText().substring(0, getSelectedYearText().length() - 2) + " ▼");
            }
        });
        //Popupを表示する
        selectedYearTextView.setOnClickListener(v -> {
            //一番初めは、一つも年度がないため追加してもらう。
            if (getSelectedYearText().contains(ADD_YEAR)) {
                AlertDialog.Builder builder = createAddYearAlertDialogBuilder();
                builder.show();
                return;
            }
            layout.removeAllViews();
            selectedYearTextView.setText(getSelectedYearText().substring(0, selectedYearTextView.getText().toString().length() - 2) + " ▲");
            for (int i = 0; i < years.size(); i++) {
                TextView yearTextView = createYearTextView(i);
                String year = years.get(i);
                yearTextView.setOnClickListener(view -> {
                    if (year.equals(ADD_YEAR)) {
                        AlertDialog.Builder builder = createAddYearAlertDialogBuilder();
                        builder.show();
                    } else {
                        selectedYearTextView.setText(year + " ▼");
                        setCurrentTimetable();
                    }
                    popup.dismiss();//popupを閉じる
                });
                layout.addView(yearTextView);
            }
            popup.showAsDropDown(v, selectedYearTextView.getWidth() - popup.getWidth(), 0);//view,幅,高さ
        });
        //時間割の部
        GridLayout timetable = createTimetableGridLayout();
        //年度と時間割の取得
        timetableViewModel.loadTimetable(userId, token);
        //新しい年度が追加されたら更新
        timetableViewModel.getTimetable().observe(this, new Observer<TreeMap<Integer, HashSet<Lecture>>>() {
            @Override
            public void onChanged(TreeMap<Integer, HashSet<Lecture>> timetable) {
                Log.d("TIMETABLE", "onChanged");
                ArrayList<Integer> integers = new ArrayList<>(timetable.keySet());
                Collections.sort(integers);
                years.clear();
                for (int i = 0; i < integers.size(); i++) {
                    int year = integers.get(i);
                    //時間割を保存するmapも一緒に更新
                    if (!timetableLecturesMap.containsKey(year)) {
                        timetableLecturesMap.put(year, timetable.get(year));
                    }
                }
                setYearsFromTimetableLecturesMap();
                updateLayout();
                setCurrentTimetable();
            }
        });

    }
    private String getSelectedYearText(){
        return selectedYearTextView.getText().toString();
    }

    private int getSelectedYear(){
        return Integer.parseInt(getSelectedYearText().substring(0,4));
    }

    //選んだ年度を表示するTextViewを作成
    private TextView createSelectedYearTextView() {
        TextView selectedYearTextView = findViewById(R.id.pop_up_window);
        selectedYearTextView.setGravity(Gravity.CENTER);
        selectedYearTextView.setTextSize(20);
        selectedYearTextView.setBackgroundResource(R.drawable.cell_border);
        selectedYearTextView.setText(ADD_YEAR);
        isDecidedYear = false;
        return selectedYearTextView;
    }

    //years(表示される年度の候補のlist)を更新するメソッド
    private void setYearsFromTimetableLecturesMap() {
        years.clear();
        TreeSet<Integer> timetable = new TreeSet<>(timetableLecturesMap.keySet());
        if (timetable.isEmpty()) {
            years.add(ADD_YEAR);
            return;
        }
        for (int year : timetable) {
            years.add(year + FIRST);
            years.add(year + SECOND);
        }
        years.add(ADD_YEAR);
    }

    private void updateLayout() {
        layout.removeAllViews();//一旦すべて削除
        for (int i = 0; i < years.size(); i++) {
            TextView yearTextView = createYearTextView(i);
            String year = years.get(i);
            yearTextView.setOnClickListener(view -> {
                if (year.equals(ADD_YEAR)) {
                    AlertDialog.Builder builder = createAddYearAlertDialogBuilder();
                    builder.show();
                } else {
                    selectedYearTextView.setText(year + " ▼");
                }
                popup.dismiss();//popupを閉じる
            });
            layout.addView(yearTextView);
        }
        if (getSelectedYearText().equals(ADD_YEAR)) {
            if (years.get(0).equals(ADD_YEAR)) {
                selectedYearTextView.setText(years.get(0));
            } else {
                isDecidedYear = true;
                if (intentYear != 0) {//事前にyearが決まっている場合はそれ
                    if (years.contains(intentYear + intentSemester)) {
                        selectedYearTextView.setText(intentYear + intentSemester + " ▼");
                    } else {
                        selectedYearTextView.setText(years.get(0) + " ▼");
                    }
                } else if (currentMonth >= 4) {//4月以降であれば現在の年、それ以前なら現在の年-1を追加する
                    if (years.contains(currentYear + FIRST)) {
                        selectedYearTextView.setText(currentYear + FIRST + " ▼");
                    } else {
                        selectedYearTextView.setText(years.get(0) + " ▼");
                    }
                } else {
                    if (years.contains((currentYear - 1) + FIRST)) {
                        selectedYearTextView.setText((currentYear - 1) + FIRST + " ▼");
                    } else {
                        selectedYearTextView.setText(years.get(0) + " ▼");
                    }
                }

            }
        } else {
            isDecidedYear = true;
        }
    }

    //年度の候補を表示するtextViewを作成する(偶数番目と奇数番目で色を変える)
    private TextView createYearTextView(int i) {
        TextView yearTextView = new TextView(this);
        String year = years.get(i);
        yearTextView.setText(year);
        yearTextView.setTextSize(20);
        yearTextView.setGravity(Gravity.CENTER);
        if (i % 2 == 0) {
            yearTextView.setBackgroundColor(Color.rgb(220, 220, 220));
        } else if (year.equals(ADD_YEAR)) {
            yearTextView.setBackgroundColor(Color.WHITE);
        } else {
            yearTextView.setBackgroundColor(Color.rgb(180, 180, 180));
        }
        return yearTextView;
    }

    private AlertDialog.Builder createAddYearAlertDialogBuilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] selectYears = new String[currentYear - 2010 + 2];
        for (int i = 2010; i <= currentYear + 1; i++) {
            selectYears[i - 2010] = String.valueOf(i);
        }
        builder.setCancelable(true);//戻るボタンで戻れるようにする。
        builder.setItems(selectYears, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int year = Integer.parseInt(selectYears[which]);
                timetableViewModel.addYear(userId, year, token);
                dialog.dismiss();
            }
        });
        return builder;
    }

    //時間割表のUIを作成する
    private GridLayout createTimetableGridLayout() {
        timetable = findViewById(R.id.grid);
        currentTimetable = new HashMap<>();
        for (int row = 0; row <= 7; row++) {
            for (int col = 0; col <= 5; col++) {
                TextView textView = new TextView(this);
                textView.setBackgroundResource(R.drawable.cell_border);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;//幅は自動設定
                params.height = 0;
                if (row == 0 && col > 0) {
                    textView.setText(DAYS[col - 1]);
                } else if (col == 0 && row > 0) {
                    textView.setText(String.valueOf(row));
                } else if (col == 0) {
                    if (getSelectedYearText().contains(FIRST)) {
                        textView.setText(FIRST);
                    } else if (getSelectedYearText().contains(SECOND)) {
                        textView.setText(SECOND);
                    }
                }
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(16);
                params.rowSpec = GridLayout.spec(row, row == 0 ? 0.7f : 1f);//rowが0の時幅を小さくする
                params.columnSpec = GridLayout.spec(col, col == 0 ? 0.7f : 1f);//colが0の時幅を小さくする
                textView.setLayoutParams(params);
                textView.setTag(new Point(row, col));
                if (col != 0 && row != 0) {
                    if (!currentTimetable.containsKey(DAYS[col - 1])) {
                        currentTimetable.put(DAYS[col - 1], new HashMap<>());
                    }
                    textView.setTextSize(10);
                    currentTimetable.get(DAYS[col - 1]).put(row, textView);
                    textView.setOnLongClickListener(v -> {
                        if (!isDecidedYear) {
                            return false;
                        }
                        Intent intent = new Intent(TimetableActivity.this, SetTimetableActivity.class);
                        Point p = (Point) v.getTag();
                        intent.putExtra("year", getSelectedYear());
                        intent.putExtra("day", DAYS[p.y - 1]);
                        intent.putExtra("period", p.x);
                        startActivity(intent);
                        return true;
                    });
                } else if (col == 0 && row == 0) {
                    currentSemester = textView;
                    timetable.addView(currentSemester);
                    continue;
                }
                timetable.addView(textView);
            }
        }
        return timetable;
    }

    private void setCurrentTimetable() {
        String curSemester;

        if (getSelectedYearText().contains(FIRST)) {
            curSemester = FIRST;
            currentSemester.setText(curSemester);
        } else if (getSelectedYearText().contains(SECOND)) {
            curSemester = SECOND;
            currentSemester.setText(curSemester);
        } else {
            curSemester = "";
            currentSemester.setText(curSemester);
        }
        if (!getSelectedYearText().equals(ADD_YEAR)) {
            int currentYear = getSelectedYear();
            for (String day : currentTimetable.keySet()) {
                for (Integer period : currentTimetable.get(day).keySet()) {
                    currentTimetable.get(day).get(period).setText("");
                }
            }
            for (Lecture lecture : timetableLecturesMap.get(currentYear)) {
                String name = lecture.getName();
                Integer grade = lecture.getGrade();
                String semester = lecture.getSemester();
                Integer frame = lecture.getFrame();//コマ数
                String day = lecture.getDay();
                Integer period = lecture.getPeriod();
                if (semester.equals(curSemester) || semester.equals("通年")) {
                    for (int i = 0; i < frame; i++) {
                        currentTimetable.get(day).get(period + i).setText(name);
                    }
                }
            }
        }
    }

    private float startY;
    // 上方向へこの距離以上動いたら発火
    private static final float SWIPE_THRESHOLD = 400f;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!isDecidedYear) {
            return super.dispatchTouchEvent(ev);
        }

        switch (ev.getAction()) {

            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();

                break;

            case MotionEvent.ACTION_UP:

                float endY = ev.getY();
                float diffY = startY - endY;

                // 画面下方向から上部へスワイプ
                if (diffY >= SWIPE_THRESHOLD) {
                    Intent intent = new Intent(this, OtherLecturesActivity.class);
                    intent.putExtra("year", getSelectedYear());
                    if (getSelectedYearText().contains(FIRST)) {
                        intent.putExtra("semester", FIRST);
                    } else {
                        intent.putExtra("semester", SECOND);
                    }
                    startActivity(intent);
                    return true;
                }

                break;
        }

        return super.dispatchTouchEvent(ev);
    }
}