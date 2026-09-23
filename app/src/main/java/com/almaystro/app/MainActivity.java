package com.almaystro.app;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    private GradientDrawable background(int color, float radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(radius);
        return drawable;
    }

    private GradientDrawable cardBackground() {
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
        TextView view = new TextView(this);

        view.setText(value);
        view.setTextSize(size);
        view.setTextColor(color);
        view.setGravity(gravity);
        view.setIncludeFontPadding(true);

        return view;
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

    private TextView decorations() {
        return text(
                "✦  ✧  ❖  ✧  ✦",
                22,
                GOLD,
                Gravity.CENTER
        );
    }

    private Button goldButton(String title) {
        Button button = new Button(this);

        button.setText(title);
        button.setTextSize(18);
        button.setTextColor(DARK_GREEN);
        button.setAllCaps(false);
        button.setGravity(Gravity.CENTER);

        button.setMinHeight(0);
        button.setMinimumHeight(0);
        button.setIncludeFontPadding(true);
        button.setPadding(20, 12, 20, 12);

        button.setBackground(
                background(GOLD, 50)
        );

        return button;
    }

    private Button outlineButton(String title) {
        Button button = new Button(this);

        button.setText(title);
        button.setTextSize(17);
        button.setTextColor(GOLD);
        button.setAllCaps(false);
        button.setGravity(Gravity.CENTER);

        button.setMinHeight(0);
        button.setMinimumHeight(0);
        button.setIncludeFontPadding(true);
        button.setPadding(20, 10, 20, 10);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(GREEN);
        drawable.setCornerRadius(50);
        drawable.setStroke(2, GOLD);

        button.setBackground(drawable);

        return button;
    }

    private EditText inputField(
            String hint,
            int inputType
    ) {
        EditText editText = new EditText(this);

        editText.setHint(hint);
        editText.setHintTextColor(Color.rgb(170, 180, 175));
        editText.setTextColor(WHITE);
        editText.setTextSize(17);

        editText.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);

        editText.setSingleLine(true);
        editText.setInputType(inputType);

        editText.setPadding(20, 5, 20, 5);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.rgb(12, 68, 48));
        drawable.setCornerRadius(30);
        drawable.setStroke(2, GOLD);

        editText.setBackground(drawable);

        return editText;
    }

    private void showWelcome() {

        LinearLayout root = new LinearLayout(this);

        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(24, 20, 24, 24);
        root.setBackgroundColor(DARK_GREEN);

        root.addView(
                decorations(),
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        5,
                        8
                )
        );

        TextView welcome = text(
                "أهلاً وسهلاً بك",
                20,
                LIGHT_GOLD,
                Gravity.CENTER
        );

        welcome.setTypeface(null, 1);

        root.addView(
                welcome,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        5,
                        18
                )
        );

        LinearLayout card = new LinearLayout(this);

        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER);
        card.setPadding(25, 28, 25, 28);
        card.setBackground(cardBackground());

        TextView title = text(
                "المايسترو",
                40,
                GOLD,
                Gravity.CENTER
        );

        title.setTypeface(null, 1);

        card.addView(title);

        TextView teacher = text(
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
                        8
                )
        );

        TextView line = text(
                "━━━━━━━━━━━━",
                18,
                GOLD,
                Gravity.CENTER
        );

        card.addView(line);

        TextView subtitle = text(
                "هتتعلم التاريخ ببساطة",
                19,
                LIGHT_GOLD,
                Gravity.CENTER
        );

        subtitle.setTypeface(null, 1);

        card.addView(
                subtitle,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        8,
                        8
                )
        );

        TextView description = text(
                "تعلم • اختبر نفسك • تابع مستواك",
                14,
                GRAY,
                Gravity.CENTER
        );

        card.addView(description);

        root.addView(
                card,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        8,
                        18
                )
        );

        root.addView(
                text(
                        "❖  ───── ✦ ─────  ❖",
                        20,
                        GOLD,
                        Gravity.CENTER
                ),
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        2,
                        12
                )
        );

        Button start = goldButton("ابدأ الآن");

        root.addView(
                start,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        0,
                        12
                )
        );

        root.addView(
                text(
                        "منصة المايسترو التعليمية",
                        14,
                        GRAY,
                        Gravity.CENTER
                )
        );

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRoles();
            }
        });

        setContentView(root);
    }

    private void showRoles() {

        LinearLayout root = new LinearLayout(this);

        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(24, 25, 24, 25);
        root.setBackgroundColor(DARK_GREEN);

        root.addView(
                decorations(),
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        5,
                        18
                )
        );

        TextView title = text(
                "مرحباً بك في المايسترو",
                28,
                GOLD,
                Gravity.CENTER
        );

        title.setTypeface(null, 1);

        root.addView(title);

        root.addView(
                text(
                        "اختر نوع الحساب للمتابعة",
                        17,
                        WHITE,
                        Gravity.CENTER
                ),
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        5,
                        25
                )
        );

        LinearLayout student = roleCard(
                "★",
                "طالب",
                "الدخول إلى الامتحانات والنتائج"
        );

        root.addView(
                student,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        5,
                        15
                )
        );

        LinearLayout teacher = roleCard(
                "◆",
                "مدرس",
                "إدارة الامتحانات والطلاب والنتائج"
        );

        root.addView(
                teacher,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        5,
                        15
                )
        );

        root.addView(
                text(
                        "❖  ───── ✦ ─────  ❖",
                        20,
                        GOLD,
                        Gravity.CENTER
                )
        );

        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStudentLogin();
            }
        });

        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage("قسم المدرس");
            }
        });

        setContentView(root);
    }

    private LinearLayout roleCard(
            String iconValue,
            String titleValue,
            String descriptionValue
    ) {

        LinearLayout card = new LinearLayout(this);

        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER);
        card.setPadding(20, 20, 20, 20);
        card.setBackground(cardBackground());

        TextView icon = text(
                iconValue,
                30,
                GOLD,
                Gravity.CENTER
        );

        TextView title = text(
                titleValue,
                23,
                WHITE,
                Gravity.CENTER
        );

        title.setTypeface(null, 1);

        TextView description = text(
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

    private void showStudentLogin() {

        LinearLayout root = new LinearLayout(this);

        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(24, 25, 24, 25);
        root.setBackgroundColor(DARK_GREEN);

        root.addView(
                decorations(),
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        5,
                        15
                )
        );

        TextView title = text(
                "مرحباً يا طالب المايسترو",
                27,
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

        TextView subtitle = text(
                "أدخل بياناتك للمتابعة",
                17,
                WHITE,
                Gravity.CENTER
        );

        root.addView(
                subtitle,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        3,
                        20
                )
        );

        LinearLayout card = new LinearLayout(this);

        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(22, 25, 22, 25);
        card.setBackground(cardBackground());

        TextView nameLabel = text(
                "اسم الطالب",
                17,
                LIGHT_GOLD,
                Gravity.RIGHT
        );

        card.addView(
                nameLabel,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        0,
                        6
                )
        );

        EditText nameInput = inputField(
                "اكتب اسمك",
                InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        );

        card.addView(
                nameInput,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        58,
                        0,
                        18
                )
        );

        TextView codeLabel = text(
                "كود الامتحان",
                17,
                LIGHT_GOLD,
                Gravity.RIGHT
        );

        card.addView(
                codeLabel,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        0,
                        6
                )
        );

        EditText codeInput = inputField(
                "اكتب كود الامتحان",
                InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
        );

        card.addView(
                codeInput,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        58,
                        0,
                        22
                )
        );

        Button loginButton = goldButton("دخول إلى الامتحان");

        card.addView(
                loginButton,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        0,
                        5
                )
        );

        root.addView(
                card,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        5,
                        18
                )
        );

        Button back = outlineButton("رجوع");

        root.addView(
                back,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        5,
                        10
                )
        );

        root.addView(
                text(
                        "❖  ───── ✦ ─────  ❖",
                        20,
                        GOLD,
                        Gravity.CENTER
                )
        );

        loginButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String name =
                                nameInput.getText().toString().trim();

                        String code =
                                codeInput.getText().toString().trim();

                        if (name.isEmpty()) {
                            Toast.makeText(
                                    MainActivity.this,
                                    "اكتب اسم الطالب أولاً",
                                    Toast.LENGTH_SHORT
                            ).show();
                            return;
                        }

                        if (code.isEmpty()) {
                            Toast.makeText(
                                    MainActivity.this,
                                    "اكتب كود الامتحان أولاً",
                                    Toast.LENGTH_SHORT
                            ).show();
                            return;
                        }

                        showMessage(
                                "تم إدخال بيانات الطالب\n\n"
                                        + name
                                        + "\n\nالكود: "
                                        + code
                        );
                    }
                }
        );

        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showRoles();
                    }
                }
        );

        setContentView(root);
    }

    private void showMessage(String message) {

        LinearLayout root = new LinearLayout(this);

        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(30, 30, 30, 30);
        root.setBackgroundColor(DARK_GREEN);

        root.addView(
                decorations(),
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        5,
                        25
                )
        );

        TextView title = text(
                message,
                24,
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
                        20
                )
        );

        Button back = outlineButton("رجوع");

        root.addView(
                back,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        5,
                        5
                )
        );

        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showRoles();
                    }
                }
        );

        setContentView(root);
    }
}
