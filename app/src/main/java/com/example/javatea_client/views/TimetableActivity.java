package com.example.javatea_client.views;

import android.graphics.Point;
import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.javatea_client.R;

public class TimetableActivity extends AppCompatActivity {

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
        //Navigation.setup(this);//Navigationクラスを呼び出す
        GridLayout gridLayout = findViewById(R.id.grid);
        for(int i=0;i<35;i++){
            TextView textView = new TextView(this);
            textView.setBackgroundResource(R.drawable.cell_border);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;//幅は自動設定
            params.height = 0;

            int row = i/7;
            int col = i%7;

            params.rowSpec = GridLayout.spec(row,1f);
            params.columnSpec = GridLayout.spec(col,col == 0 ? 0.5f : 1f);//colが0の時は幅を小さくする

            textView.setLayoutParams(params);
            textView.setTag(new Point(row,col));

            textView.setOnClickListener(v -> {
                Point cur = (Point) v.getTag();
                ((TextView) v).setText(cur.x + "," + cur.y);
            });

            gridLayout.addView(textView);
        }

    }
}