package com.almaestro.program;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private int GOLD = Color.rgb(212, 175, 55);
    private int DARK_GREEN = Color.rgb(18, 72, 52);
    private int WHITE = Color.WHITE;
    private int BLACK = Color.rgb(25, 25, 25);
    private int GRAY = Color.rgb(245, 245, 245);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showWelcomeScreen();
    }

    private void showWelcomeScreen() {

        LinearLayout main = new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);
        main.setGravity(Gravity.CENTER);
        main.setPadding(40, 60, 40, 40);
        main.setBackgroundColor(DARK_GREEN);

        TextView logo = new TextView(this);
        logo.setText("🎓");
        logo.setTextSize(70);
        logo.setGravity(Gravity.CENTER);

        TextView title = new TextView(this);
        title.setText("المايسترو");
        title.setTextSize(38);
        title.setTextColor(GOLD);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);

        TextView subtitle = new TextView(this);
        subtitle.setText("منصة تعليمية لمادة التاريخ");
        subtitle.setTextSize(19);
        subtitle.setTextColor(WHITE);
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(0, 15, 0, 10);

        TextView welcome = new TextView(this);
        welcome.setText("أهلاً بك في المايسترو\nهنا يبدأ طريقك نحو التفوق");
        welcome.setTextSize(21);
        welcome.setTextColor(WHITE);
        welcome.setGravity(Gravity.CENTER);
        welcome.setPadding(0, 30, 0, 50);

        Button startButton = new Button(this);
        startButton.setText("ابدأ الآن");
        startButton.setTextSize(20);
        startButton.setTextColor(BLACK);
        startButton.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        GradientDrawable buttonBackground = new GradientDrawable();
        buttonBackground.setColor(GOLD);
        buttonBackground.setCornerRadius(40);
        startButton.setBackground(buttonBackground);

        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        65
                );

        buttonParams.setMargins(20, 10, 20, 20);

        main.addView(logo);
        main.addView(title);
        main.addView(subtitle);
        main.addView(welcome);
        main.addView(startButton, buttonParams);

        setContentView(main);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRoleScreen();
            }
        });
    }

    private void showRoleScreen() {

        LinearLayout main = new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);
        main.setGravity(Gravity.CENTER);
        main.setPadding(35, 50, 35, 40);
        main.setBackgroundColor(GRAY);

        TextView title = new TextView(this);
        title.setText("مرحباً بك في المايسترو");
        title.setTextSize(28);
        title.setTextColor(DARK_GREEN);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 0, 0, 40);

        TextView question = new TextView(this);
        question.setText("من فضلك اختر نوع الحساب");
        question.setTextSize(21);
        question.setTextColor(BLACK);
        question.setGravity(Gravity.CENTER);
        question.setPadding(0, 0, 0, 35);

        Button studentButton = new Button(this);
        studentButton.setText("👨‍🎓  طالب");
        studentButton.setTextSize(20);
        studentButton.setTextColor(WHITE);
        studentButton.setBackgroundColor(DARK_GREEN);

        Button teacherButton = new Button(this);
        teacherButton.setText("👨‍🏫  مدرس");
        teacherButton.setTextSize(20);
        teacherButton.setTextColor(BLACK);
        teacherButton.setBackgroundColor(GOLD);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        70
                );

        params.setMargins(20, 15, 20, 15);

        main.addView(title);
        main.addView(question);
        main.addView(studentButton, params);
        main.addView(teacherButton, params);

        setContentView(main);

        studentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStudentScreen();
            }
        });

        teacherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTeacherScreen();
            }
        });
    }

    private void showStudentScreen() {

        Toast.makeText(
                this,
                "قسم الطالب سيتم تجهيزه في الخطوة القادمة",
                Toast.LENGTH_LONG
        ).show();
    }

    private void showTeacherScreen() {

        Toast.makeText(
                this,
                "قسم المدرس سيتم تجهيزه في الخطوة القادمة",
                Toast.LENGTH_LONG
        ).show();
    }
                         }
