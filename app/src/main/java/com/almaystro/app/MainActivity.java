package com.almaestro.program;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    private int DARK_GREEN = Color.rgb(27, 94, 32);
    private int GOLD = Color.rgb(212, 175, 55);
    private int WHITE = Color.WHITE;
    private int BLACK = Color.BLACK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showWelcomeScreen();
    }

    private void showWelcomeScreen() {

        LinearLayout main = new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);
        main.setGravity(Gravity.CENTER);
        main.setPadding(40, 40, 40, 40);
        main.setBackgroundColor(DARK_GREEN);

        TextView title = new TextView(this);
        title.setText("المايسترو");
        title.setTextColor(GOLD);
        title.setTextSize(38);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams titleParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        titleParams.setMargins(0, 0, 0, 25);
        main.addView(title, titleParams);

        TextView welcome = new TextView(this);
        welcome.setText("مرحبًا بك في تطبيق المايسترو");
        welcome.setTextColor(WHITE);
        welcome.setTextSize(21);
        welcome.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams welcomeParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        welcomeParams.setMargins(0, 0, 0, 50);
        main.addView(welcome, welcomeParams);

        Button startButton = new Button(this);
        startButton.setText("ابدأ الآن");
        startButton.setTextSize(20);
        startButton.setTextColor(BLACK);
        startButton.setBackgroundColor(GOLD);

        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        main.addView(startButton, buttonParams);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRoleScreen();
            }
        });

        setContentView(main);
    }

    private void showRoleScreen() {

        LinearLayout main = new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);
        main.setGravity(Gravity.CENTER);
        main.setPadding(40, 40, 40, 40);
        main.setBackgroundColor(DARK_GREEN);

        TextView title = new TextView(this);
        title.setText("اختر نوع المستخدم");
        title.setTextColor(GOLD);
        title.setTextSize(30);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);

        main.addView(title);

        Button studentButton = new Button(this);
        studentButton.setText("طالب");
        studentButton.setTextSize(20);
        studentButton.setTextColor(BLACK);
        studentButton.setBackgroundColor(GOLD);

        LinearLayout.LayoutParams p1 =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        p1.setMargins(0, 50, 0, 20);
        main.addView(studentButton, p1);

        Button teacherButton = new Button(this);
        teacherButton.setText("معلم");
        teacherButton.setTextSize(20);
        teacherButton.setTextColor(BLACK);
        teacherButton.setBackgroundColor(GOLD);

        main.addView(teacherButton);

        studentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage("تم اختيار الطالب");
            }
        });

        teacherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage("تم اختيار المعلم");
            }
        });

        setContentView(main);
    }

    private void showMessage(String message) {
        android.widget.Toast.makeText(
                this,
                message,
                android.widget.Toast.LENGTH_SHORT
        ).show();
    }
            }
