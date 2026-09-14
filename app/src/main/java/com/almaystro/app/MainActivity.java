package com.almaystro.app;

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
import android.widget.ImageView;

public class MainActivity extends Activity {

    LinearLayout mainLayout;

    int gold = Color.rgb(224, 190, 70);
    int darkGreen = Color.rgb(5, 38, 27);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showWelcomeScreen();
    }

    private GradientDrawable makeBackground() {
        return new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[]{
                        Color.rgb(5, 38, 27),
                        Color.rgb(14, 70, 48),
                        Color.rgb(5, 38, 27)
                }
        );
    }

    private Button makeButton(String text) {
        Button button = new Button(this);
        button.setText(text);
        button.setTextSize(21);
        button.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        button.setTextColor(darkGreen);

        GradientDrawable background = new GradientDrawable();
        background.setColor(gold);
        background.setCornerRadius(40);

        button.setBackground(background);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        70
                );

        params.setMargins(35, 12, 35, 12);
        button.setLayoutParams(params);

        return button;
    }

    private TextView makeText(String text, float size, int color) {
        TextView view = new TextView(this);
        view.setText(text);
        view.setTextSize(size);
        view.setTextColor(color);
        view.setGravity(Gravity.CENTER);
        view.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        view.setPadding(10, 10, 10, 10);
        return view;
    }

    private void showWelcomeScreen() {

        mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setGravity(Gravity.CENTER);
        mainLayout.setPadding(30, 35, 30, 35);
        mainLayout.setBackground(makeBackground());

        mainLayout.addView(makeText("✦  ❖  ✦", 25, gold));

        ImageView logo = new ImageView(this);
        logo.setImageResource(R.drawable.maestro);
        logo.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        LinearLayout.LayoutParams imageParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        280
                );

        imageParams.setMargins(0, 20, 0, 10);
        mainLayout.addView(logo, imageParams);

        mainLayout.addView(makeText("المايسترو", 40, gold));
        mainLayout.addView(makeText("المايسترو شريف هيبه", 22, Color.WHITE));

        TextView subtitle = makeText(
                "هتتعلم التاريخ ببساطة",
                19,
                Color.LTGRAY
        );

        mainLayout.addView(subtitle);
        mainLayout.addView(makeText("❖", 28, gold));

        Button startButton = makeButton("ابدأ الآن");

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRoleScreen();
            }
        });

        mainLayout.addView(startButton);
        mainLayout.addView(makeText("✦  ❖  ✦", 22, gold));

        setContentView(mainLayout);
    }

    private void showRoleScreen() {

        mainLayout.removeAllViews();

        mainLayout.setGravity(Gravity.CENTER);
        mainLayout.setPadding(30, 40, 30, 40);

        mainLayout.addView(makeText("✦  ❖  ✦", 25, gold));
        mainLayout.addView(makeText("اختر نوع الدخول", 30, gold));

        TextView description = makeText(
                "من فضلك اختر هل أنت طالب أم مدرس",
                18,
                Color.WHITE
        );

        description.setPadding(10, 25, 10, 35);
        mainLayout.addView(description);

        Button studentButton = makeButton("أنا طالب");

        studentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessageScreen("دخول الطالب");
            }
        });

        mainLayout.addView(studentButton);

        Button teacherButton = makeButton("أنا مدرس");

        teacherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessageScreen("دخول المدرس");
            }
        });

        mainLayout.addView(teacherButton);

        Button backButton = makeButton("رجوع");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWelcomeScreen();
            }
        });

        mainLayout.addView(backButton);

        mainLayout.addView(makeText("✦  ❖  ✦", 22, gold));

        setContentView(mainLayout);
    }

    private void showMessageScreen(String title) {

        mainLayout.removeAllViews();

        mainLayout.setGravity(Gravity.CENTER);

        mainLayout.addView(makeText("✦  ❖  ✦", 25, gold));
        mainLayout.addView(makeText(title, 30, gold));

        mainLayout.addView(makeText(
                "هذه الشاشة سيتم تجهيزها في الخطوة القادمة",
                18,
                Color.WHITE
        ));

        Button backButton = makeButton("رجوع");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRoleScreen();
            }
        });

        mainLayout.addView(backButton);

        setContentView(mainLayout);
    }
            }
