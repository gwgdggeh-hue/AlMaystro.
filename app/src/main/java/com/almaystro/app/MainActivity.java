package com.almaystro.app;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    private final int DARK_GREEN = Color.rgb(4, 35, 25);
    private final int GREEN = Color.rgb(8, 57, 40);
    private final int GOLD = Color.rgb(224, 190, 70);
    private final int LIGHT_GOLD = Color.rgb(248, 225, 145);
    private final int WHITE = Color.WHITE;
    private final int GRAY = Color.rgb(210, 210, 210);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showWelcome();
    }

    private GradientDrawable background(int color, float radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(radius);
        return drawable;
    }

    private GradientDrawable goldBorder() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(GREEN);
        drawable.setCornerRadius(35);
        drawable.setStroke(3, GOLD);
        return drawable;
    }

    private TextView text(
            String value,
            float size,
            int color,
            int gravity
    ) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setGravity(gravity);
        t.setPadding(10, 10, 10, 10);
        return t;
    }

    private TextView decoration(String value) {
        TextView t = text(value, 22, GOLD, Gravity.CENTER);
        return t;
    }

    private Button mainButton(String value) {
        Button b = new Button(this);
        b.setText(value);
        b.setTextSize(18);
        b.setTextColor(DARK_GREEN);
        b.setAllCaps(false);
        b.setGravity(Gravity.CENTER);

        GradientDrawable bg = background(GOLD, 45);
        b.setBackground(bg);

        return b;
    }

    private LinearLayout.LayoutParams params(
            int width,
            int height,
            int top,
            int bottom
    ) {
        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(width, height);

        p.setMargins(0, top, 0, bottom);
        return p;
    }

    private void showWelcome() {

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(25, 20, 25, 25);
        root.setBackgroundColor(DARK_GREEN);

        TextView topDecoration =
                decoration("✦  ✧  ❖  ✧  ✦");

        root.addView(
                topDecoration,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        5,
                        5
                )
        );

        TextView welcome =
                text("أهلاً وسهلاً بك", 19, LIGHT_GOLD, Gravity.CENTER);

        root.addView(
                welcome,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        5,
                        5
                )
        );

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER);
        card.setPadding(25, 30, 25, 30);
        card.setBackground(goldBorder());

        TextView logo =
                text("المايسترو", 40, GOLD, Gravity.CENTER);
        logo.setTypeface(null, 1);

        card.addView(
                logo,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        5,
                        5
                )
        );

        TextView teacher =
                text(
                        "المايسترو شريف هيبه",
                        23,
                        WHITE,
                        Gravity.CENTER
                );
        teacher.setTypeface(null, 1);

        card.addView(
                teacher,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        8,
                        5
                )
        );

        TextView line =
                text(
                        "━━━━━━━━━━━━",
                        18,
                        GOLD,
                        Gravity.CENTER
                );

        card.addView(
                line,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        3,
                        3
                )
        );

        TextView subtitle =
                text(
                        "هتتعلم التاريخ ببساطة",
                        19,
                        LIGHT_GOLD,
                        Gravity.CENTER
                );

        card.addView(
                subtitle,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        5,
                        5
                )
        );

        TextView description =
                text(
                        "تعلم • اختبر نفسك • تابع مستواك",
                        14,
                        GRAY,
                        Gravity.CENTER
                );

        card.addView(
                description,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        8,
                        5
                )
        );

        root.addView(
                card,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        20,
                        20
                )
        );

        TextView bottomDecoration =
                decoration("❖  ───── ✦ ─────  ❖");

        root.addView(
                bottomDecoration,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        5,
                        15
                )
        );

        Button start = mainButton("ابدأ الآن");

        root.addView(
                start,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        65,
                        5,
                        10
                )
        );

        TextView footer =
                text(
                        "منصة المايسترو التعليمية",
                        13,
                        GRAY,
                        Gravity.CENTER
                );

        root.addView(
                footer,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        8,
                        5
                )
        );

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
        root.setPadding(25, 25, 25, 25);
        root.setBackgroundColor(DARK_GREEN);

        root.addView(
                decoration("✦  ✧  ❖  ✧  ✦"),
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        5,
                        20
                )
        );

        TextView title =
                text(
                        "مرحباً بك في المايسترو",
                        28,
                        GOLD,
                        Gravity.CENTER
                );
        title.setTypeface(null, 1);

        root.addView(
                title,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        5,
                        8
                )
        );

        TextView subtitle =
                text(
                        "اختر نوع الحساب للمتابعة",
                        17,
                        WHITE,
                        Gravity.CENTER
                );

        root.addView(
                subtitle,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        5,
                        25
                )
        );

        LinearLayout studentCard = roleCard(
                "👨‍🎓",
                "طالب",
                "الدخول إلى الامتحانات والنتائج"
        );

        root.addView(
                studentCard,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        5,
                        15
                )
        );

        LinearLayout teacherCard = roleCard(
                "👨‍🏫",
                "مدرس",
                "إدارة الامتحانات والطلاب والنتائج"
        );

        root.addView(
                teacherCard,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        5,
                        15
                )
        );

        TextView bottom =
                decoration("❖  ───── ✦ ─────  ❖");

        root.addView(
                bottom,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        15,
                        5
                )
        );

        studentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessage("قسم الطالب");
            }
        });

        teacherCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessage("قسم المدرس");
            }
        });

        setContentView(root);
    }

    private LinearLayout roleCard(
            String icon,
            String title,
            String description
    ) {

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER);
        card.setPadding(20, 20, 20, 20);
        card.setBackground(goldBorder());

        TextView iconText =
                text(icon, 30, GOLD, Gravity.CENTER);

        TextView titleText =
                text(title, 22, WHITE, Gravity.CENTER);
        titleText.setTypeface(null, 1);

        TextView descriptionText =
                text(description, 14, GRAY, Gravity.CENTER);

        card.addView(iconText);
        card.addView(titleText);
        card.addView(descriptionText);

        return card;
    }

    private void showMessage(String message) {

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(30, 30, 30, 30);
        root.setBackgroundColor(DARK_GREEN);

        TextView decoration =
                decoration("✦  ✧  ❖  ✧  ✦");

        root.addView(decoration);

        TextView title =
                text(message, 30, GOLD, Gravity.CENTER);
        title.setTypeface(null, 1);

        root.addView(
                title,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        25,
                        10
                )
        );

        TextView info =
                text(
                        "سيتم تجهيز هذا القسم في المرحلة القادمة",
                        17,
                        WHITE,
                        Gravity.CENTER
                );

        root.addView(info);

        setContentView(root);
    }
    }
