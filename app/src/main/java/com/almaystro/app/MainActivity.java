package com.almaystro.app;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final int DARK_GREEN = Color.rgb(4, 35, 25);
    private static final int GREEN = Color.rgb(8, 57, 40);
    private static final int GOLD = Color.rgb(224, 190, 70);
    private static final int LIGHT_GOLD = Color.rgb(248, 225, 145);
    private static final int WHITE = Color.WHITE;
    private static final int GRAY = Color.rgb(210, 210, 210);

    private String studentName = "";
    private String examCode = "";

    private int currentQuestion = 0;
    private int score = 0;

    private int[] answers = {-1, -1, -1, -1, -1};

    private final String[] questions = {
            "ما هي السنة التي سميت بعام الجماعة؟",
            "من هو أول الخلفاء الراشدين؟",
            "ما عاصمة الدولة الأموية؟",
            "في أي قارة تقع مصر؟",
            "ما اسم نهر مصر الرئيسي؟"
    };

    private final String[][] choices = {
            {"41 هـ", "40 هـ", "42 هـ", "43 هـ"},
            {"عمر بن الخطاب", "أبو بكر الصديق", "عثمان بن عفان", "علي بن أبي طالب"},
            {"دمشق", "بغداد", "القاهرة", "المدينة"},
            {"آسيا", "أفريقيا", "أوروبا", "أمريكا"},
            {"النيل", "الفرات", "دجلة", "الأردن"}
    };

    private final int[] correctAnswers = {
            0,
            1,
            0,
            1,
            0
    };

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

    private EditText inputField(String hint, int inputType) {

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

        card.addView(
                text(
                        "━━━━━━━━━━━━",
                        18,
                        GOLD,
                        Gravity.CENTER
                )
        );

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

        card.addView(
                text(
                        "تعلم • اختبر نفسك • تابع مستواك",
                        14,
                        GRAY,
                        Gravity.CENTER
                )
        );

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

        card.addView(
                text(
                        iconValue,
                        30,
                        GOLD,
                        Gravity.CENTER
                )
        );

        TextView title = text(
                titleValue,
                23,
                WHITE,
                Gravity.CENTER
        );

        title.setTypeface(null, 1);

        card.addView(title);

        card.addView(
                text(
                        descriptionValue,
                        14,
                        GRAY,
                        Gravity.CENTER
                )
        );

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

        root.addView(title);

        root.addView(
                text(
                        "أدخل بياناتك للمتابعة",
                        17,
                        WHITE,
                        Gravity.CENTER
                ),
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

        card.addView(
                text(
                        "اسم الطالب",
                        17,
                        LIGHT_GOLD,
                        Gravity.RIGHT
                )
        );

        EditText nameInput = inputField(
                "اكتب اسمك",
                android.text.InputType.TYPE_CLASS_TEXT
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

        card.addView(
                text(
                        "كود الامتحان",
                        17,
                        LIGHT_GOLD,
                        Gravity.RIGHT
                )
        );

        EditText codeInput = inputField(
                "اكتب كود الامتحان",
                android.text.InputType.TYPE_CLASS_TEXT |
                        android.text.InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
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

        loginButton.setOnClickListener(new View.OnClickListener() {
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

                studentName = name;
                examCode = code;

                currentQuestion = 0;
                score = 0;

                for (int i = 0; i < answers.length; i++) {
                    answers[i] = -1;
                }

                showExam();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRoles();
            }
        });

        setContentView(root);
    }

    private void showExam() {

        LinearLayout root = new LinearLayout(this);

        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(18, 18, 18, 18);
        root.setBackgroundColor(DARK_GREEN);

        TextView header = text(
                "امتحان المايسترو",
                26,
                GOLD,
                Gravity.CENTER
        );

        header.setTypeface(null, 1);

        root.addView(
                header,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        5,
                        5
                )
        );

        TextView student = text(
                "الطالب: " + studentName,
                15,
                LIGHT_GOLD,
                Gravity.RIGHT
        );

        root.addView(
                student,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        2,
                        8
                )
        );

        TextView counter = text(
                "السؤال " + (currentQuestion + 1)
                        + " من " + questions.length,
                17,
                WHITE,
                Gravity.CENTER
        );

        counter.setTypeface(null, 1);

              root.addView(
                counter,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        5,
                        12
                )
        );

        LinearLayout questionCard = new LinearLayout(this);

        questionCard.setOrientation(LinearLayout.VERTICAL);
        questionCard.setPadding(20, 22, 20, 20);
        questionCard.setBackground(cardBackground());

        TextView questionText = text(
                questions[currentQuestion],
                21,
                WHITE,
                Gravity.RIGHT
        );

        questionText.setTypeface(null, 1);

        questionCard.addView(
                questionText,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        0,
                        15
                )
        );

        RadioGroup group = new RadioGroup(this);
        group.setOrientation(RadioGroup.VERTICAL);
        group.setGravity(Gravity.RIGHT);

        for (int i = 0; i < choices[currentQuestion].length; i++) {

            RadioButton radio = new RadioButton(this);

            radio.setId(View.generateViewId());
            radio.setText(choices[currentQuestion][i]);
            radio.setTextSize(17);
            radio.setTextColor(WHITE);
            radio.setGravity(
                    Gravity.RIGHT | Gravity.CENTER_VERTICAL
            );

            radio.setButtonTintList(
                    android.content.res.ColorStateList.valueOf(GOLD)
            );

            group.addView(
                    radio,
                    params(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            5,
                            5
                    )
            );
        }

        questionCard.addView(group);

        root.addView(
                questionCard,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        5,
                        15
                )
        );

        Button next = goldButton(
                currentQuestion == questions.length - 1
                        ? "تسليم الامتحان"
                        : "السؤال التالي"
        );

        root.addView(
                next,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        5,
                        8
                )
        );

        Button exit = outlineButton("خروج من الامتحان");

        root.addView(
                exit,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        5,
                        5
                )
        );

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selected = -1;

                for (int i = 0; i < group.getChildCount(); i++) {

                    RadioButton radio =
                            (RadioButton) group.getChildAt(i);

                    if (radio.isChecked()) {
                        selected = i;
                        break;
                    }
                }

                if (selected == -1) {

                    Toast.makeText(
                            MainActivity.this,
                            "اختار إجابة أولاً",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }

                answers[currentQuestion] = selected;

                if (currentQuestion == questions.length - 1) {

                    calculateResult();

                } else {

                    currentQuestion++;
                    showExam();
                }
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStudentLogin();
            }
        });

        setContentView(root);
    }

    private void calculateResult() {

        score = 0;

        for (int i = 0; i < questions.length; i++) {

            if (answers[i] == correctAnswers[i]) {
                score++;
            }
        }

        showResult();
    }

    private void showResult() {

        LinearLayout root = new LinearLayout(this);

        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(25, 25, 25, 25);
        root.setBackgroundColor(DARK_GREEN);

        root.addView(
                decorations(),
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        5,
                        20
                )
        );

        TextView title = text(
                "نتيجة الامتحان",
                30,
                GOLD,
                Gravity.CENTER
        );

        title.setTypeface(null, 1);

        root.addView(title);

        root.addView(
                text(
                        "الطالب: " + studentName,
                        18,
                        WHITE,
                        Gravity.CENTER
                ),
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        15,
                        8
                )
        );

        LinearLayout resultCard = new LinearLayout(this);

        resultCard.setOrientation(LinearLayout.VERTICAL);
        resultCard.setGravity(Gravity.CENTER);
        resultCard.setPadding(25, 30, 25, 30);
        resultCard.setBackground(cardBackground());

        TextView scoreText = text(
                score + " / " + questions.length,
                42,
                GOLD,
                Gravity.CENTER
        );

        scoreText.setTypeface(null, 1);

        resultCard.addView(scoreText);

        resultCard.addView(
                text(
                        "عدد الإجابات الصحيحة",
                        18,
                        WHITE,
                        Gravity.CENTER
                )
        );

        int wrong = questions.length - score;

        resultCard.addView(
                text(
                        "الصحيح: " + score
                                + "   |   الخطأ: " + wrong,
                        16,
                        LIGHT_GOLD,
                        Gravity.CENTER
                ),
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        15,
                        0
                )
        );

        root.addView(
                resultCard,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        20,
                        20
                )
        );

        Button again = goldButton(
                "العودة لتسجيل الدخول"
        );

        root.addView(
                again,
                params(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        5,
                        10
                )
        );

        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStudentLogin();
            }
        });

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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRoles();
            }
        });

        setContentView(root);
    }
            }
