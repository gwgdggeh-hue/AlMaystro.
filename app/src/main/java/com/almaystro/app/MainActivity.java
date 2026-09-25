package com.almaystro.app;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends Activity {

    // =========================
    // ألوان التطبيق
    // =========================

    private static final int DARK = Color.rgb(3, 34, 24);
    private static final int GREEN = Color.rgb(8, 57, 40);
    private static final int GREEN_2 = Color.rgb(12, 70, 49);
    private static final int GOLD = Color.rgb(224, 190, 70);
    private static final int GOLD_LIGHT = Color.rgb(248, 225, 145);
    private static final int WHITE = Color.WHITE;
    private static final int GRAY = Color.rgb(205, 210, 207);
    private static final int RED = Color.rgb(220, 80, 70);

    // =========================
    // بيانات الطالب
    // =========================

    private String studentName = "";
    private String examCode = "";

    // =========================
    // الامتحان
    // =========================

    private int currentQuestion = 0;
    private int score = 0;

    private final int[] selectedAnswers = {
            -1, -1, -1, -1, -1
    };

    private CountDownTimer examTimer;
    private long timeLeft = 5 * 60 * 1000;

    private TextView timerView;

    // =========================
    // أسئلة تجريبية فقط
    // سيتم لاحقاً نقلها إلى Firebase
    // =========================

    private final String[] questions = {
            "ما هي السنة التي سميت بعام الجماعة؟",
            "من هو أول الخلفاء الراشدين؟",
            "ما عاصمة الدولة الأموية؟",
            "في أي قارة تقع مصر؟",
            "ما اسم نهر مصر الرئيسي؟"
    };

    private final String[][] choices = {
            {
                    "41 هـ",
                    "40 هـ",
                    "42 هـ",
                    "43 هـ"
            },
            {
                    "عمر بن الخطاب",
                    "أبو بكر الصديق",
                    "عثمان بن عفان",
                    "علي بن أبي طالب"
            },
            {
                    "دمشق",
                    "بغداد",
                    "القاهرة",
                    "المدينة"
            },
            {
                    "آسيا",
                    "أفريقيا",
                    "أوروبا",
                    "أمريكا"
            },
            {
                    "النيل",
                    "الفرات",
                    "دجلة",
                    "الأردن"
            }
    };

    private final int[] correctAnswers = {
            0,
            1,
            0,
            1,
            0
    };

    // =========================
    // بداية التطبيق
    // =========================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(DARK);
        getWindow().setNavigationBarColor(DARK);

        showWelcome();
    }

    // =========================
    // أدوات التصميم
    // =========================

    private GradientDrawable bg(int color, float radius) {

        GradientDrawable d = new GradientDrawable();

        d.setColor(color);
        d.setCornerRadius(radius);

        return d;
    }

    private GradientDrawable outlinedCard() {

        GradientDrawable d = new GradientDrawable();

        d.setColor(GREEN);
        d.setCornerRadius(32);
        d.setStroke(2, GOLD);

        return d;
    }

    private TextView tv(
            String text,
            float size,
            int color,
            int gravity
    ) {

        TextView t = new TextView(this);

        t.setText(text);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setGravity(gravity);
        t.setIncludeFontPadding(true);

        return t;
    }

    private LinearLayout.LayoutParams lp(
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

    private Button goldButton(String title) {

        Button b = new Button(this);

        b.setText(title);
        b.setTextSize(17);
        b.setTextColor(DARK);
        b.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        b.setGravity(Gravity.CENTER);
        b.setAllCaps(false);

        b.setPadding(18, 8, 18, 8);

        b.setBackground(
                bg(GOLD, 60)
        );

        return b;
    }

    private Button outlineButton(String title) {

        Button b = new Button(this);

        b.setText(title);
        b.setTextSize(16);
        b.setTextColor(GOLD);
        b.setGravity(Gravity.CENTER);
        b.setAllCaps(false);

        GradientDrawable d = new GradientDrawable();

        d.setColor(GREEN);
        d.setCornerRadius(60);
        d.setStroke(2, GOLD);

        b.setBackground(d);

        return b;
    }

    private TextView ornament() {

        return tv(
                "✦  ✧  ❖  ✧  ✦",
                22,
                GOLD,
                Gravity.CENTER
        );
    }

    private EditText input(
            String hint
    ) {

        EditText e = new EditText(this);

        e.setHint(hint);
        e.setHintTextColor(Color.rgb(160, 175, 168));
        e.setTextColor(WHITE);
        e.setTextSize(17);
        e.setSingleLine(true);

        e.setGravity(
                Gravity.RIGHT |
                        Gravity.CENTER_VERTICAL
        );

        e.setPadding(18, 5, 18, 5);

        GradientDrawable d = new GradientDrawable();

        d.setColor(GREEN_2);
        d.setCornerRadius(30);
        d.setStroke(2, GOLD);

        e.setBackground(d);

        return e;
    }

    private LinearLayout baseLayout() {

        LinearLayout root = new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setGravity(Gravity.CENTER_HORIZONTAL);

        root.setPadding(
                20,
                20,
                20,
                20
        );

        root.setBackgroundColor(DARK);

        return root;
    }

    private ScrollView scroll(LinearLayout content) {

        ScrollView scroll = new ScrollView(this);

        scroll.setFillViewport(true);

        scroll.addView(content);

        return scroll;
    }

    // =========================
    // شاشة الترحيب
    // =========================

    private void showWelcome() {

        LinearLayout root = baseLayout();

        root.addView(
                ornament(),
                lp(
                        -1,
                        -2,
                        8,
                        8
                )
        );

        TextView welcome = tv(
                "أهلاً وسهلاً بك",
                21,
                GOLD_LIGHT,
                Gravity.CENTER
        );

        welcome.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        root.addView(
                welcome,
                lp(
                        -1,
                        -2,
                        5,
                        15
                )
        );

        LinearLayout card = new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setGravity(Gravity.CENTER);

        card.setPadding(
                22,
                28,
                22,
                28
        );

        card.setBackground(
                outlinedCard()
        );

        TextView title = tv(
                "المايسترو",
                40,
                GOLD,
                Gravity.CENTER
        );

        title.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        card.addView(title);

        TextView teacher = tv(
                "المايسترو شريف هيبه",
                23,
                WHITE,
                Gravity.CENTER
        );

        teacher.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        card.addView(
                teacher,
                lp(
                        -1,
                        -2,
                        8,
                        8
                )
        );

        card.addView(
                tv(
                        "━━━━━━━━━━━━",
                        18,
                        GOLD,
                        Gravity.CENTER
                )
        );

        TextView slogan = tv(
                "هتتعلم التاريخ ببساطة",
                19,
                GOLD_LIGHT,
                Gravity.CENTER
        );

        slogan.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        card.addView(
                slogan,
                lp(
                        -1,
                        -2,
                        10,
                        8
                )
        );

        card.addView(
                tv(
                        "تعلم • اختبر نفسك • تابع مستواك",
                        14,
                        GRAY,
                        Gravity.CENTER
                )
        );

        root.addView(
                card,
                lp(
                        -1,
                        -2,
                        5,
                        18
                )
        );

        root.addView(
                tv(
                        "❖  ───── ✦ ─────  ❖",
                        20,
                        GOLD,
                        Gravity.CENTER
                ),
                lp(
                        -1,
                        -2,
                        2,
                        12
                )
        );

        Button start = goldButton(
                "ابدأ الآن"
        );

        root.addView(
                start,
                lp(
                        -1,
                        60,
                        5,
                        12
                )
        );

        root.addView(
                tv(
                        "منصة المايسترو التعليمية",
                        14,
                        GRAY,
                        Gravity.CENTER
                )
        );

        start.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        showRoles();
                    }
                }
        );

        setContentView(
                scroll(root)
        );
    }

    // =========================
    // اختيار نوع المستخدم
    // =========================

    private void showRoles() {

        LinearLayout root = baseLayout();

        root.addView(
                ornament(),
                lp(
                        -1,
                        -2,
                        15,
                        15
                )
        );

        TextView title = tv(
                "مرحباً بك في المايسترو",
                28,
                GOLD,
                Gravity.CENTER
        );

        title.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        root.addView(
                title
        );

        root.addView(
                tv(
                        "اختر نوع الحساب للمتابعة",
                        16,
                        WHITE,
                        Gravity.CENTER
                ),
                lp(
                        -1,
                        -2,
                        6,
                        25
                )
        );

        LinearLayout student =
                roleCard(
                        "★",
                        "طالب",
                        "الامتحانات والنتائج ومتابعة المستوى"
                );

        root.addView(
                student,
                lp(
                        -1,
                        -2,
                        5,
                        15
                )
        );

        LinearLayout teacher =
                roleCard(
                        "◆",
                        "مدرس",
                        "إدارة الامتحانات والأسئلة والطلاب"
                );

        root.addView(
                teacher,
                lp(
                        -1,
                        -2,
                        5,
                        15
                )
        );

        student.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        showStudentLogin();
                    }
                }
        );

        teacher.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        showTeacherLogin();
                    }
                }
        );

        root.addView(
                tv(
                        "❖  ───── ✦ ─────  ❖",
                        20,
                        GOLD,
                        Gravity.CENTER
                )
        );

        setContentView(
                scroll(root)
        );
    }

    private LinearLayout roleCard(
            String icon,
            String title,
            String description
    ) {

        LinearLayout card = new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setGravity(
                Gravity.CENTER
        );

        card.setPadding(
                20,
                22,
                20,
                22
        );

        card.setBackground(
                outlinedCard()
        );

        card.addView(
                tv(
                        icon,
                        30,
                        GOLD,
                        Gravity.CENTER
                )
        );

        TextView t = tv(
                title,
                23,
                WHITE,
                Gravity.CENTER
        );

        t.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        card.addView(t);

        card.addView(
                tv(
                        description,
                        14,
                        GRAY,
                        Gravity.CENTER
                ),
                lp(
                        -1,
                        -2,
                        5,
                        0
                )
        );

        return card;
    }

    // =========================
    // دخول الطالب
    // =========================

    private void showStudentLogin() {

        LinearLayout root = baseLayout();

        root.addView(
                ornament(),
                lp(
                        -1,
                        -2,
                        10,
                        15
                )
        );

        TextView title = tv(
                "دخول الطالب",
                29,
                GOLD,
                Gravity.CENTER
        );

        title.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        root.addView(title);

        root.addView(
                tv(
                        "اكتب بياناتك للدخول إلى الامتحان",
                        16,
                        WHITE,
                        Gravity.CENTER
                ),
                lp(
                        -1,
                        -2,
                        5,
                        20
                )
        );

        LinearLayout card = new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setPadding(
                20,
                25,
                20,
                25
        );

        card.setBackground(
                outlinedCard()
        );

        card.addView(
                tv(
                        "اسم الطالب",
                        17,
                        GOLD_LIGHT,
                        Gravity.RIGHT
                )
        );

        EditText name =
                input("اكتب اسمك");

        card.addView(
                name,
                lp(
                        -1,
                        58,
                        5,
                        18
                )
        );

        card.addView(
                tv(
                        "كود الامتحان",
                        17,
                        GOLD_LIGHT,
                        Gravity.RIGHT
                )
        );

        EditText code =
                input("اكتب كود الامتحان");

        card.addView(
                code,
                lp(
                        -1,
                        58,
                        5,
                        20
                )
        );

        Button login =
                goldButton(
                        "دخول إلى الامتحان"
                );

        card.addView(
                login,
                lp(
                        -1,
                        58,
                        5,
                        5
                )
        );

        root.addView(
                card,
                lp(
                        -1,
                        -2,
                        5,
                        15
                )
        );

        Button back =
                outlineButton("رجوع");

        root.addView(
                back,
                lp(
                        -1,
                        55,
                        5,
                        5
                )
        );

        login.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        String n =
                                name.getText()
                                        .toString()
                                        .trim();

                        String c =
                                code.getText()
                                        .toString()
                                        .trim();

                        if (n.isEmpty()) {

                            Toast.makeText(
                                    MainActivity.this,
                                    "اكتب اسم الطالب أولاً",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        if (c.isEmpty()) {

                            Toast.makeText(
                                    MainActivity.this,
                                    "اكتب كود الامتحان أولاً",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        studentName = n;
                        examCode = c;

                        startExam();
                    }
                }
        );

        back.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        showRoles();
                    }
                }
        );

        setContentView(
                scroll(root)
        );
    }

    // =========================
    // بداية الامتحان
    // =========================

    private void startExam() {

        currentQuestion = 0;
        score = 0;

        timeLeft =
                5 * 60 * 1000;

        for (int i = 0;
             i < selectedAnswers.length;
             i++) {

            selectedAnswers[i] = -1;
        }

        showExam();

        startTimer();
    }

    // =========================
    // شاشة الامتحان
    // =========================

    private void showExam() {

        LinearLayout root = baseLayout();

        TextView title = tv(
                "امتحان المايسترو",
                26,
                GOLD,
                Gravity.CENTER
        );

        title.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        root.addView(
                title,
                lp(
                        -1,
                        -2,
                        5,
                        5
                )
        );

        root.addView(
                tv(
                        "الطالب: " + studentName,
                        15,
                        GOLD_LIGHT,
                        Gravity.RIGHT
                ),
                lp(
                        -1,
                        -2,
                        2,
                        5
                )
        );

        root.addView(
                tv(
                        "كود الامتحان: " + examCode,
                        13,
                        GRAY,
                        Gravity.RIGHT
                ),
                lp(
                        -1,
                        -2,
                        2,
                        8
                )
        );

        timerView = tv(
                "05:00",
                20,
                GOLD,
                Gravity.CENTER
        );

        timerView.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        root.addView(
                timerView,
                lp(
                        -1,
                        45,
                        5,
                        8
                )
        );

        TextView counter = tv(
                "السؤال "
                        + (currentQuestion + 1)
                        + " من "
                        + questions.length,
                16,
                WHITE,
                Gravity.CENTER
        );

        root.addView(
                counter,
                lp(
                        -1,
                        -2,
                        3,
                        12
                )
        );

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setPadding(
                20,
                22,
                20,
                22
        );

        card.setBackground(
                outlinedCard()
        );

        TextView q =
                tv(
                        questions[currentQuestion],
                        20,
                        WHITE,
                        Gravity.RIGHT
                );

        q.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        card.addView(
                q,
                lp(
                        -1,
                        -2,
                        0,
                        18
                )
        );

        RadioGroup group =
                new RadioGroup(this);

        group.setOrientation(
                RadioGroup.VERTICAL
        );

        for (int i = 0;
             i < choices[currentQuestion].length;
             i++) {

            RadioButton radio =
                    new RadioButton(this);

            radio.setId(
                    View.generateViewId()
            );

            radio.setText(
                    choices[currentQuestion][i]
            );

            radio.setTextSize(17);
            radio.setTextColor(WHITE);

            radio.setButtonTintList(
                    ColorStateList.valueOf(GOLD)
            );

            radio.setGravity(
                    Gravity.RIGHT |
                            Gravity.CENTER_VERTICAL
            );

            group.addView(
                    radio,
                    lp(
                            -1,
                            50,
                            3,
                            3
                    )
            );
        }

        card.addView(group);

        root.addView(
                card,
                lp(
                        -1,
                        -2,
                        5,
                        15
                )
        );

        Button next =
                goldButton(
                        currentQuestion ==
                                questions.length - 1
                                ? "تسليم الامتحان"
                                : "السؤال التالي"
                );

        root.addView(
                next,
                lp(
                        -1,
                        58,
                        5,
                        8
                )
        );

        Button exit =
                outlineButton(
                        "خروج من الامتحان"
                );

        root.addView(
                exit,
                lp(
                        -1,
                        52,
                        5,
                        5
                )
        );

        next.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        int selected = -1;

                        for (int i = 0;
                             i < group.getChildCount();
                             i++) {

                            RadioButton r =
                                    (RadioButton)
                                            group.getChildAt(i);

                            if (r.isChecked()) {

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

                        selectedAnswers[currentQuestion] =
                                selected;

                        if (currentQuestion ==
                                questions.length - 1) {

                            finishExam();

                        } else {

                            currentQuestion++;

                            showExam();
                        }
                    }
                }
        );

        exit.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        if (examTimer != null) {
                            examTimer.cancel();
                            examTimer = null;
                        }

                        showStudentLogin();
                    }
                }
        );

        setContentView(
                scroll(root)
        );
    }

    // =========================
    // المؤقت
// المؤقت
    // =========================

    private void startTimer() {

        if (examTimer != null) {
            examTimer.cancel();
        }

        examTimer =
                new CountDownTimer(
                        timeLeft,
                        1000
                ) {

                    @Override
                    public void onTick(
                            long millis
                    ) {

                        timeLeft =
                                millis;

                        if (timerView != null) {

                            timerView.setText(
                                    formatTime(millis)
                            );

                            if (millis <= 30000) {

                                timerView.setTextColor(
                                        RED
                                );
                            }
                        }
                    }

                    @Override
                    public void onFinish() {

                        timeLeft = 0;

                        examTimer = null;

                        Toast.makeText(
                                MainActivity.this,
                                "انتهى وقت الامتحان",
                                Toast.LENGTH_LONG
                        ).show();

                        calculateScore();
                    }
                };

        examTimer.start();
    }

    private String formatTime(
            long millis
    ) {

        long seconds =
                millis / 1000;

        long minutes =
                seconds / 60;

        seconds =
                seconds % 60;

        return String.format(
                Locale.US,
                "%02d:%02d",
                minutes,
                seconds
        );
    }

    // =========================
    // النتيجة
    // =========================

    private void finishExam() {

        if (examTimer != null) {

            examTimer.cancel();
            examTimer = null;
        }

        calculateScore();
    }

    private void calculateScore() {

        score = 0;

        for (int i = 0;
             i < correctAnswers.length;
             i++) {

            if (selectedAnswers[i] ==
                    correctAnswers[i]) {

                score++;
            }
        }

        showResult();
    }

    private void showResult() {

        LinearLayout root = baseLayout();

        root.addView(
                ornament(),
                lp(
                        -1,
                        -2,
                        10,
                        15
                )
        );

        TextView title =
                tv(
                        "نتيجة الامتحان",
                        30,
                        GOLD,
                        Gravity.CENTER
                );

        title.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        root.addView(title);

        root.addView(
                tv(
                        "الطالب: "
                                + studentName,
                        17,
                        WHITE,
                        Gravity.CENTER
                ),
                lp(
                        -1,
                        -2,
                        12,
                        5
                )
        );

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setGravity(
                Gravity.CENTER
        );

        card.setPadding(
                25,
                30,
                25,
                30
        );

        card.setBackground(
                outlinedCard()
        );

        TextView scoreText =
                tv(
                        score
                                + " / "
                                + questions.length,
                        45,
                        GOLD,
                        Gravity.CENTER
                );

        scoreText.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        card.addView(scoreText);

        int wrong =
                questions.length - score;

        card.addView(
                tv(
                        "الإجابات الصحيحة: "
                                + score,
                        17,
                        WHITE,
                        Gravity.CENTER
                ),
                lp(
                        -1,
                        -2,
                        12,
                        5
                )
        );

        card.addView(
                tv(
                        "الإجابات الخاطئة: "
                                + wrong,
                        17,
                        GOLD_LIGHT,
                        Gravity.CENTER
                )
        );

        root.addView(
                card,
                lp(
                        -1,
                        -2,
                        20,
                        20
                )
        );

        Button again =
                goldButton(
                        "العودة إلى الطالب"
                );

        root.addView(
                again,
                lp(
                        -1,
                        58,
                        5,
                        10
                )
        );

        again.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        showStudentLogin();
                    }
                }
        );

        setContentView(
                scroll(root)
        );
    }

    // =========================
    // دخول المدرس
