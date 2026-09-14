package com.almaystro.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ScrollView;

public class MainActivity extends Activity {

    LinearLayout mainLayout;

    int gold = Color.rgb(224, 190, 70);
    int darkGreen = Color.rgb(5, 38, 27);

    private int dp(int value) {
        return (int) (value * getResources()
                .getDisplayMetrics().density + 0.5f);
    }

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

    private TextView makeText(String text, float size, int color) {
        TextView view = new TextView(this);

        view.setText(text);
        view.setTextSize(size);
        view.setTextColor(color);
        view.setGravity(Gravity.CENTER);
        view.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        view.setIncludeFontPadding(true);
        view.setPadding(dp(8), dp(8), dp(8), dp(8));

        return view;
    }

    private TextView makeButton(String text) {
        TextView button = new TextView(this);

        button.setText(text);
        button.setTextSize(20);
        button.setTextColor(darkGreen);
        button.setGravity(Gravity.CENTER);
        button.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        button.setIncludeFontPadding(true);
        button.setSingleLine(false);
        button.setPadding(dp(8), dp(5), dp(8), dp(5));

        GradientDrawable background = new GradientDrawable();
        background.setColor(gold);
        background.setCornerRadius(dp(40));

        button.setBackground(background);
        button.setClickable(true);
        button.setFocusable(true);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dp(65)
                );

        params.setMargins(dp(30), dp(8), dp(30), dp(8));
        button.setLayoutParams(params);

        return button;
    }

    private void prepareLayout() {
        mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setGravity(Gravity.CENTER);
        mainLayout.setPadding(dp(25), dp(25), dp(25), dp(25));
        mainLayout.setBackground(makeBackground());
    }

    private void showWelcomeScreen() {
        prepareLayout();

        mainLayout.addView(makeText("✦  ❖  ✦", 25, gold));

        ImageView logo = new ImageView(this);
        logo.setImageResource(R.drawable.maestro);
        logo.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        LinearLayout.LayoutParams imageParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dp(230)
                );

        imageParams.setMargins(0, dp(10), 0, dp(5));
        mainLayout.addView(logo, imageParams);

        mainLayout.addView(makeText("المايسترو", 38, gold));
        mainLayout.addView(makeText("المايسترو شريف هيبه", 21, Color.WHITE));
        mainLayout.addView(makeText("هتتعلم التاريخ ببساطة", 18, Color.LTGRAY));
        mainLayout.addView(makeText("❖", 26, gold));

        TextView startButton = makeButton("ابدأ الآن");

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRoleScreen();
            }
        });

        mainLayout.addView(startButton);
        mainLayout.addView(makeText("✦  ❖  ✦", 22, gold));

        setContentView(mainLayout);
    }

    private void showRoleScreen() {
        prepareLayout();

        mainLayout.addView(makeText("✦  ❖  ✦", 25, gold));
        mainLayout.addView(makeText("اختر نوع الدخول", 30, gold));

        mainLayout.addView(makeText(
                "من فضلك اختر هل أنت طالب أم مدرس",
                18,
                Color.WHITE
        ));

        TextView studentButton = makeButton("أنا طالب");

        studentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessageScreen("دخول الطالب");
            }
        });

        mainLayout.addView(studentButton);

        TextView teacherButton = makeButton("أنا مدرس");

        teacherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessageScreen("دخول المدرس");
            }
        });

        mainLayout.addView(teacherButton);

        TextView backButton = makeButton("رجوع");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWelcomeScreen();
            }
        });

        mainLayout.addView(backButton);
        mainLayout.addView(makeText("✦  ❖  ✦", 22, gold));

        setContentView(mainLayout);
    }

    private void showMessageScreen(String title) {
        prepareLayout();

        mainLayout.addView(makeText("✦  ❖  ✦", 25, gold));
        mainLayout.addView(makeText(title, 30, gold));

        mainLayout.addView(makeText(
                "هذه الشاشة سيتم تجهيزها في الخطوة القادمة",
                18,
                Color.WHITE
        ));

        TextView backButton = makeButton("رجوع");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRoleScreen();
            }
        });

        mainLayout.addView(backButton);

        setContentView(mainLayout);
    }
            }
