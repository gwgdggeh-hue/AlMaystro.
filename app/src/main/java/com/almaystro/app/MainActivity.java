package com.almaystro.app;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    private final int DARK_GREEN = Color.rgb(5, 38, 27);
    private final int GOLD = Color.rgb(224, 190, 70);
    private final int WHITE = Color.WHITE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showWelcome();
    }

    private void showWelcome() {

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(40, 40, 40, 40);
        root.setBackgroundColor(DARK_GREEN);

        TextView title = new TextView(this);
        title.setText("المايسترو");
        title.setTextColor(GOLD);
        title.setTextSize(34);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(null, 1);

        TextView teacher = new TextView(this);
        teacher.setText("المايسترو شريف هيبه");
        teacher.setTextColor(WHITE);
        teacher.setTextSize(22);
        teacher.setGravity(Gravity.CENTER);

        TextView subtitle = new TextView(this);
        subtitle.setText("هتتعلم التاريخ ببساطة");
        subtitle.setTextColor(WHITE);
        subtitle.setTextSize(18);
        subtitle.setGravity(Gravity.CENTER);

        Button start = new Button(this);
        start.setText("ابدأ الآن");
        start.setTextSize(18);

        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        buttonParams.setMargins(0, 50, 0, 0);

        root.addView(title);
        root.addView(teacher);
        root.addView(subtitle);
        root.addView(start, buttonParams);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRoles();
            }
        });

        setContentView(root);
    }

    private void showRoles() {

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(40, 40, 40, 40);
        root.setBackgroundColor(DARK_GREEN);

        TextView title = new TextView(this);
        title.setText("اختر نوع الحساب");
        title.setTextColor(GOLD);
        title.setTextSize(28);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(null, 1);

        Button student = new Button(this);
        student.setText("طالب");
        student.setTextSize(18);

        Button teacher = new Button(this);
        teacher.setText("مدرس");
        teacher.setTextSize(18);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(0, 30, 0, 0);

        root.addView(title);
        root.addView(student, params);
        root.addView(teacher, params);

        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessage("قسم الطالب");
            }
        });

        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessage("قسم المدرس");
            }
        });

        setContentView(root);
    }

    private void showMessage(String message) {

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(40, 40, 40, 40);
        root.setBackgroundColor(DARK_GREEN);

        TextView text = new TextView(this);
        text.setText(message);
        text.setTextColor(GOLD);
        text.setTextSize(28);
        text.setGravity(Gravity.CENTER);

        root.addView(text);

        setContentView(root);
    }
    }
