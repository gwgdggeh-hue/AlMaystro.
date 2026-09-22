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

    private static final int DARK_GREEN = Color.rgb(4, 35, 25);
    private static final int GREEN = Color.rgb(8, 57, 40);
    private static final int GOLD = Color.rgb(224, 190, 70);
    private static final int LIGHT_GOLD = Color.rgb(248, 225, 145);
    private static final int WHITE = Color.WHITE;
    private static final int GRAY = Color.rgb(210, 210, 210);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(DARK_GREEN);
        getWindow().setNavigationBarColor(DARK_GREEN);

        showWelcome();
    }

    private GradientDrawable roundedBackground(int color, float radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(radius);
        return drawable;
    }

    private GradientDrawable borderedCard() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(GREEN);
        drawable.setCornerRadius(35);
        drawable.setStroke(3, GOLD);
        return drawable;
    }

    private TextView makeText(
            String value,
            float size,
            int color,
            int gravity
    ) {
        TextView text = new TextView(this);

        text.setText(value);
        text.setTextSize(size);
        text.setTextColor(color);
        text.setGravity(gravity);

        text.setIncludeFontPadding(true);

        return text;
    }

    private LinearLayout.LayoutParams layoutParams(
            int width,
            int height,
            int top,
            int bottom
    ) {
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(width, height);

        params.setMargins(0, top, 0, bottom);

        return params;
    }

    private TextView decoration() {
        return makeText(
                "✦  ✧  ❖  ✧  ✦",
                22,
                GOLD,
                Gravity.CENTER
        );
    }

    private Button mainButton(String value) {

        Button button = new Button(this);

        button.setText(value);
        button.setTextSize(18);
        button.setTextColor(DARK_GREEN);

        button.setAllCaps(false);
        button.setGravity(Gravity.CENTER);

        // مهم جدًا: منع قص الكلام داخل الزر
        button.setMinHeight(0);
        button.setMinimumHeight(0);
        button.setIncludeFontPadding(true);

        // مساحة داخلية مريحة للنص
        button.setPadding(20, 10, 20, 10);

        button.setBackground(
                roundedBackground(GOLD, 50)
        );

        return button;
    }

    private void showWelcome() {

        LinearLayout root = new LinearLayout(this);

        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(24, 20, 24, 24);
        root.setBackgroundColor(DARK_GREEN);

        root.addView(
                decoration(),
                layoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        5,
                        8
                )
        );

        TextView welcome = makeText(
                "أهلاً وسهلاً بك",
                20,
                LIGHT_GOLD,
                Gravity.CENTER
        );

        welcome.setTypeface(null, 1);

        root.addView(
                welcome,
                layoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        5,
                        18
                )
        );

        // بطاقة المايسترو
        LinearLayout card = new LinearLayout(this);

        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER);
        card.setPadding(25, 28, 25, 28);
        card.setBackground(borderedCard());

        TextView title = makeText(
                "المايسترو",
                40,
                GOLD,
                Gravity.CENTER
        );

        title.setTypeface(null, 1);

        card.addView(
                title,
                layoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        0,
                        10
                )
        );

        TextView teacher = makeText(
                "المايسترو شريف هيبه",
                23,
                WHITE,
                Gravity.CENTER
        );

        teacher.setTypeface(null, 1);

        card.addView(
                teacher,
                layoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        4,
                        8
                )
        );

        TextView line = makeText(
                "━━━━━━━━━━━━",
                18,
                GOLD,
                Gravity.CENTER
        );

        card.addView(
                line,
                layoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        0,
                        8
                )
        );

        TextView subtitle = makeText(
                "هتتعلم التاريخ ببساطة",
                19,
                LIGHT_GOLD,
                Gravity.CENTER
        );

        subtitle.setTypeface(null, 1);

        card.addView(
                subtitle,
                layoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        4,
                        8
                )
        );

        TextView description = makeText(
                "تعلم • اختبر نفسك • تابع مستواك",
                14,
                GRAY,
                Gravity.CENTER
        );

        card.addView(
                description,
                layoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        4,
                        0
                )
        );

        root.addView(
                card,
                layoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        8,
                        18
                )
        );

        TextView middleDecoration = makeText(
                "❖  ───── ✦ ─────  ❖",
                20,
                GOLD,
                Gravity.CENTER
        );

        root.addView(
                middleDecoration,
                layoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        2,
                        12
                )
        );

        // زر ابدأ الآن
        Button startButton = mainButton("ابدأ الآن");

        root.addView(
                startButton,
                layoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        0,
                        12
                )
        );

        TextView footer = makeText(
                "منصة المايسترو التعليمية",
                14,
                GRAY,
                Gravity.CENTER
        );

        root.addView(
                footer,
                layoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        5,
                        0
                )
        );

        startButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showRoles();
                    }
                }
        );

        setContentView(root);
    }

    private void showRoles() {

        LinearLayout root = new LinearLayout(this);

        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(24, 25, 24, 25);
        root.setBackgroundColor(DARK_GREEN);

        root.addView(
                decoration(),
                layoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        5,
                        18
                )
        );

        TextView title = makeText(
                "مرحباً بك في المايسترو",
                28,
                GOLD,
                Gravity.CENTER
        );

        title.setTypeface(null, 1);

        root.addView(
                title,
                layoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        5,
                        8
                )
        );

        TextView subtitle = makeText(
                "اختر نوع الحساب للمتابعة",
                17,
                WHITE,
                Gravity.CENTER
        );

        root.addView(
                subtitle,
                layoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        3,
                        25
                )
        );

        LinearLayout studentCard = createRoleCard(
                "طالب",
                "الدخول إلى الامتحانات والنتائج"
        );

        root.addView(
                studentCard,
                layoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        5,
                        15
                )
        );

        LinearLayout teacherCard = createRoleCard(
                "مدرس",
                "إدارة الامتحانات والطلاب والنتائج"
        );

        root.addView(
                teacherCard,
                layoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        5,
                        15
                )
        );

        TextView bottomDecoration = makeText(
                "❖  ───── ✦ ─────  ❖",
                20,
                GOLD,
                Gravity.CENTER
        );

        root.addView(
                bottomDecoration,
                layoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        15,
                        5
                )
        );

        studentCard.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showMessage("قسم الطالب");
                    }
                }
        );

        teacherCard.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showMessage("قسم المدرس");
                    }
                }
        );

        setContentView(root);
    }

    private LinearLayout createRoleCard(
            String titleValue,
            String descriptionValue
    ) {

        LinearLayout card = new LinearLayout(this);

        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER);
        card.setPadding(20, 20, 20, 20);
        card.setBackground(borderedCard());

        TextView icon = makeText(
                titleValue.equals("طالب") ? "★" : "◆",
                30,
                GOLD,
                Gravity.CENTER
        );

        TextView title = makeText(
                titleValue,
                23,
                WHITE,
                Gravity.CENTER
        );

        title.setTypeface(null, 1);

        TextView description = makeText(
                descriptionValue,
                14,
                GRAY,
                Gravity.CENTER
        );

        card.addView(icon);
        card.addView(title);
        card.addView(description);

        return card;
    }

    private void showMessage(String message) {

        LinearLayout root = new LinearLayout(this);

        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(30, 30, 30, 30);
        root.setBackgroundColor(DARK_GREEN);

        root.addView(
                decoration(),
                layoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        5,
                        25
                )
        );

        TextView title = makeText(
                message,
                30,
                GOLD,
                Gravity.CENTER
        );

        title.setTypeface(null, 1);

        root.addView(
                title,
                layoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        5,
                        15
                )
        );

        TextView info = makeText(
                "سيتم تجهيز هذا القسم في المرحلة القادمة",
                17,
                WHITE,
                Gravity.CENTER
        );

        root.addView(
                info,
                layoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        5,
                        0
                )
        );

        setContentView(root);
    }
            }
