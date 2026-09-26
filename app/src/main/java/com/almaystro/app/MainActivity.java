package com.almaystro.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.UUID;

public class MainActivity extends Activity {

    private static final int GOLD = Color.rgb(224, 190, 70);
    private static final int DARK = Color.rgb(5, 38, 27);
    private static final int GREEN = Color.rgb(14, 70, 48);
    private static final int LIGHT = Color.rgb(247, 245, 238);
    private static final int WHITE = Color.WHITE;
    private static final int BLACK = Color.rgb(25, 25, 25);
    private static final int GRAY = Color.rgb(105, 105, 105);
    private static final int RED = Color.rgb(170, 55, 50);

    private SharedPreferences prefs;

    private LinearLayout root;
    private LinearLayout content;

    private String role = "";
    private String currentStudent = "";
    private String currentTeacher = "";

    private String currentExamId = "";
    private JSONArray currentQuestions = new JSONArray();
    private int currentQuestion = 0;
    private int[] currentAnswers = new int[0];

    private CountDownTimer examTimer;

    private boolean darkMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences("ALMAYSTRO_DATA", MODE_PRIVATE);
        darkMode = prefs.getBoolean("dark_mode", false);

        getWindow().setStatusBarColor(DARK);
        getWindow().setNavigationBarColor(DARK);

        showHome();
    }

    /* =========================================================
       HOME
       ========================================================= */

    private void showHome() {

        role = "";

        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(backgroundColor());

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);

        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(16), dp(16), dp(16), dp(20));

        scroll.addView(content);

        LinearLayout hero = panel();
        hero.setPadding(dp(16), dp(18), dp(16), dp(18));

        ImageView image = new ImageView(this);

        int imageId = getResources().getIdentifier(
                "maestro",
                "drawable",
                getPackageName()
        );

        if (imageId != 0) {
            image.setImageResource(imageId);
            image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }

        hero.addView(
                image,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(145)
                )
        );

        TextView title = text(
                "المايسترو شريف هيبه",
                27,
                GOLD,
                true
        );

        title.setGravity(Gravity.CENTER);

        hero.addView(title);

        TextView subtitle = text(
                "هتتعلم التاريخ ببساطة",
                16,
                textColor(),
                false
        );

        subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(0, dp(5), 0, 0);

        hero.addView(subtitle);

        TextView decoration = text(
                "✦  ✦  ✦  منصة المايسترو التعليمية  ✦  ✦  ✦",
                12,
                GOLD,
                false
        );

        decoration.setGravity(Gravity.CENTER);
        decoration.setPadding(0, dp(13), 0, 0);

        hero.addView(decoration);

        content.addView(
                hero,
                lp(-1, -2, 0, 0, 0, 14)
        );

        TextView choose = text(
                "اختار القسم",
                20,
                textColor(),
                true
        );

        choose.setGravity(Gravity.RIGHT);

        content.addView(
                choose,
                lp(-1, -2, 0, 4, 0, 10)
        );

        LinearLayout roles = horizontal();

        roles.addView(
                roleCard(
                        "👨‍🎓",
                        "طالب",
                        "الامتحانات والنتائج",
                        true
                ),
                new LinearLayout.LayoutParams(
                        0,
                        dp(165),
                        1
                )
        );

        roles.addView(spaceW(10));

        roles.addView(
                roleCard(
                        "👨‍🏫",
                        "مدرس",
                        "إدارة الامتحانات والطلاب",
                        false
                ),
                new LinearLayout.LayoutParams(
                        0,
                        dp(165),
                        1
                )
        );

        content.addView(roles);

        content.addView(sectionTitle("أقسام المايسترو"));

        LinearLayout row1 = horizontal();

        row1.addView(
                infoCard(
                        "📝",
                        "الامتحانات",
                        "امتحاناتك"
                ),
                new LinearLayout.LayoutParams(
                        0,
                        dp(105),
                        1
                )
        );

        row1.addView(spaceW(8));

        row1.addView(
                infoCard(
                        "📚",
                        "المذكرات",
                        "شرح ومراجعة"
                ),
                new LinearLayout.LayoutParams(
                        0,
                        dp(105),
                        1
                )
        );

        row1.addView(spaceW(8));

        row1.addView(
                infoCard(
                        "🤲",
                        "الأذكار",
                        "أذكار يومية"
                ),
                new LinearLayout.LayoutParams(
                        0,
                        dp(105),
                        1
                )
        );

        content.addView(row1);

        LinearLayout row2 = horizontal();

        row2.addView(
                infoCard(
                        "🔔",
                        "التحديثات",
                        "آخر الأخبار"
                ),
                new LinearLayout.LayoutParams(
                        0,
                        dp(105),
                        1
                )
        );

        row2.addView(spaceW(8));

        row2.addView(
                infoCard(
                        "🏆",
                        "الإنجازات",
                        "تقدمك"
                ),
                new LinearLayout.LayoutParams(
                        0,
                        dp(105),
                        1
                )
        );

        row2.addView(spaceW(8));

        row2.addView(
                infoCard(
                        "⚙",
                        "الإعدادات",
                        "إعدادات التطبيق"
                ),
                new LinearLayout.LayoutParams(
                        0,
                        dp(105),
                        1
                )
        );

        content.addView(
                row2,
                lp(-1, -2, 0, 8, 0, 0)
        );

        TextView footer = text(
                "✦ المايسترو ✦\n\n" +
                "مع المبرمج أو المطور محمود كليب\n" +
                "للتواصل: 01112244710",
                12,
                GRAY,
                false
        );

        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0, dp(20), 0, dp(5));

        content.addView(footer);

        root.addView(
                scroll,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        root.addView(homeBottom());

        setContentView(root);
    }

    private View roleCard(
            String icon,
            String title,
            String subtitle,
            boolean student
    ) {

        LinearLayout box = clickablePanel();

        box.setGravity(Gravity.CENTER);
        box.setPadding(
                dp(8),
                dp(12),
                dp(8),
                dp(12)
        );

        TextView iconText = text(
                icon,
                34,
                GOLD,
                false
        );

        iconText.setGravity(Gravity.CENTER);

        box.addView(
                iconText,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(50)
                )
        );

        TextView titleText = text(
                title,
                20,
                textColor(),
                true
        );

        titleText.setGravity(Gravity.CENTER);

        box.addView(titleText);

        TextView subText = text(
                subtitle,
                11,
                GRAY,
                false
        );

        subText.setGravity(Gravity.CENTER);
        subText.setPadding(0, dp(4), 0, 0);

        box.addView(subText);

        box.setOnClickListener(v -> {

            if (student) {
                showStudentLogin();
            } else {
                showTeacherLogin();
            }

        });

        return box;
    }

    private View infoCard(
            String icon,
            String title,
            String subtitle
    ) {

        LinearLayout box = clickablePanel();

        box.setGravity(Gravity.CENTER);
        box.setPadding(
                dp(4),
                dp(8),
                dp(4),
                dp(8)
        );

        TextView iconText = text(
                icon,
                25,
                GOLD,
                false
        );

        iconText.setGravity(Gravity.CENTER);

        box.addView(iconText);

        TextView titleText = text(
                title,
                13,
                textColor(),
                true
        );

        titleText.setGravity(Gravity.CENTER);

        box.addView(titleText);

        TextView subText = text(
                subtitle,
                9,
                GRAY,
                false
        );

        subText.setGravity(Gravity.CENTER);

        box.addView(subText);

        box.setOnClickListener(v ->
                toast("اختار طالب أو مدرس من الشاشة الرئيسية")
        );

        return box;
    }

    private LinearLayout homeBottom() {

        LinearLayout bar = navBar();

        bar.addView(
                navItem(
                        "⌂",
                        "الرئيسية",
                        v -> showHome()
                ),
                new LinearLayout.LayoutParams(
                        0,
                        -1,
                        1
                )
        );

        bar.addView(
                navItem(
                        "📝",
                        "الامتحانات",
                        v -> toast("اختار طالب أو مدرس أولاً")
                ),
                new LinearLayout.LayoutParams(
                        0,
                        -1,
                        1
                )
        );

        bar.addView(
                navItem(
                        "🤲",
                        "الأذكار",
                        v -> showAzkar(false)
                ),
                new LinearLayout.LayoutParams(
                        0,
                        -1,
                        1
                )
        );

        bar.addView(
                navItem(
                        "⚙",
                        "الإعدادات",
                        v -> showSettings(false)
                ),
                new LinearLayout.LayoutParams(
                        0,
                        -1,
                        1
                )
        );

        return bar;
    }

    /* =========================================================
       STUDENT LOGIN
       ========================================================= */

    private void showStudentLogin() {

        role = "student";

        LinearLayout page = pageBase(
                "دخول الطالب",
                "ادخل اسمك وكود الامتحان"
        );

        EditText name = input("الاسم الثلاثي");
        EditText code = input("كود الامتحان");

        page.addView(label("اسم الطالب"));

        page.addView(
                name,
                lp(-1, dp(58), 0, 0, 0, 12)
        );

        page.addView(label("كود الامتحان"));

        page.addView(
                code,
                lp(-1, dp(58), 0, 0, 0, 16)
        );

        Button enter = primary("دخول إلى الامتحان");

        page.addView(
                enter,
                lp(-1, dp(56), 0, 0, 0, 10)
        );

        Button account = secondary("الدخول إلى حسابي");

        page.addView(
                account,
                lp(-1, dp(52), 0, 0, 0, 10)
        );

        enter.setOnClickListener(v -> {

            String studentName =
                    name.getText().toString().trim();

            String examCode =
                    code.getText().toString().trim();

            if (studentName.isEmpty()) {
                toast("اكتب اسم الطالب");
                return;
            }

            if (examCode.isEmpty()) {
                toast("اكتب كود الامتحان");
                return;
            }

            JSONObject exam =
                    findExamByCode(examCode);

            if (exam == null) {
                toast("الكود غير صحيح أو غير موجود");
                return;
            }

            if (isCodeUsed(
                    examCode,
                    studentName
            )) {
                toast("هذا الكود تم استخدامه من قبل");
                return;
            }

            currentStudent = studentName;

            prefs.edit()
                    .putString(
                            "last_student",
                            studentName
                    )
                    .apply();

            startExam(
                    exam,
                    examCode
            );
        });

        account.setOnClickListener(v -> {

            String n =
                    name.getText().toString().trim();

            if (!n.isEmpty()) {
                currentStudent = n;
            } else {
                currentStudent =
                        prefs.getString(
                                "last_student",
                                ""
                        );
            }

            if (currentStudent.isEmpty()) {
                toast("اكتب اسم الطالب أولاً");
                return;
            }

            showStudentHome();
        });

        addBack(page, this::showHome);

        setPage(page, true);
    }

    /* =========================================================
       STUDENT HOME
       ========================================================= */

    private void showStudentHome() {

        if (currentStudent.isEmpty()) {
            currentStudent =
                    prefs.getString(
                            "last_student",
                            ""
                    );
        }

        if (currentStudent.isEmpty()) {
            showStudentLogin();
            return;
        }

        role = "student";

        LinearLayout page = pageBase(
                "أهلاً يا " + currentStudent,
                "لوحة الطالب الرئيسية"
        );

        page.addView(
                sectionTitle("اختار القسم")
        );

        LinearLayout row1 = horizontal();

        row1.addView(
                actionCard(
                        "📝",
                        "امتحاناتي",
                        "الامتحانات المتاحة",
                        v -> showStudentExams()
                ),
                new LinearLayout.LayoutParams(
                        0,
                        dp(130),
                        1
                )
        );

        row1.addView(spaceW(10));

        row1.addView(
                actionCard(
                        "📊",
                        "نتائجي",
                        "درجات وتصحيح",
                        v -> showStudentResults()
                ),
                new LinearLayout.LayoutParams(
                        0,
                        dp(130),
                        1
                )
        );

        page.addView(row1);

        LinearLayout row2 = horizontal();

        row2.addView(
                actionCard(
                        "📚",
                        "المذكرات",
                        "شرح ومراجعة",
                        v -> showNotes(false)
                ),
                new LinearLayout.LayoutParams(
                        0,
                        dp(130),
                        1
                )
        );

        row2.addView(spaceW(10));

        row2.addView(
                actionCard(
                        "🤲",
                        "الأذكار",
                        "أذكار يومية",
                        v -> showAzkar(true)
                ),
                new LinearLayout.LayoutParams(
                        0,
                        dp(130),
                        1
                )
        );

        page.addView(
                row2,
                lp(-1, -2, 0, 10, 0, 0)
        );

        LinearLayout row3 = horizontal();

        row3.addView(
                actionCard(
                        "🏆",
                        "إنجازاتي",
                        "تقدمك",
                        v -> showAchievements()
                ),
                new LinearLayout.LayoutParams(
                        0,
                        dp(130),
                        1
                )
        );

        row3.addView(spaceW(10));

        row3.addView(
                actionCard(
                        "🔔",
                        "التحديثات",
                        "آخر الأخبار",
                        v -> showUpdates()
                ),
                new LinearLayout.LayoutParams(
                        0,
                        dp(130),
                        1
                )
        );

        page.addView(row3);

        page.addView(
                messageCard(
                        "ملاحظة\n\n" +
                        "المدرس هو المسؤول عن إنشاء الامتحانات والأسئلة والأكواد والمذكرات.\n" +
                        "لا توجد أسئلة تجريبية داخل التطبيق."
                )
        );

        Button logout =
                primary("تسجيل الخروج");

        page.addView(
                logout,
                lp(-1, dp(54), 0, 18, 0, 8)
        );

        logout.setOnClickListener(v -> {

            currentStudent = "";

            prefs.edit()
                    .remove("last_student")
                    .apply();

            showHome();
        });

        addBack(page, this::showHome);

        setPage(page, true);
    }

    /* =========================================================
       STUDENT EXAMS
       ========================================================= */

    private void showStudentExams() {

        LinearLayout page = pageBase(
                "امتحاناتي",
                "الامتحانات التي أضافها المدرس"
        );

        JSONArray exams =
                readArray("exams");

        if (exams.length() == 0) {

            page.addView(
                    emptyCard(
                            "لا توجد امتحانات حالياً.\n\n" +
                            "عندما يضيف المدرس امتحاناً سيظهر هنا."
                    )
            );

        } else {

            for (int i = 0;
                 i < exams.length();
                 i++) {

                JSONObject exam =
                        exams.optJSONObject(i);

                if (exam == null) continue;

                String id =
                        exam.optString(
                                "id"
                        );

                String title =
                        exam.optString(
                                "title",
                                "امتحان"
                        );

                JSONArray questions =
                        exam.optJSONArray(
                                "questions"
                        );

                int questionCount =
                        questions == null
                                ? 0
                                : questions.length();

                int duration =
                        exam.optInt(
                                "duration",
                                30
                        );

                LinearLayout card =
                        panel();

                card.setPadding(
                        dp(14),
                        dp(13),
                        dp(14),
                        dp(13)
                );

                card.addView(
                        text(
                                "📝  " + title,
                                17,
                                textColor(),
                                true
                        )
                );

                card.addView(
                        text(
                                questionCount +
                                " سؤال  •  " +
                                duration +
                                " دقيقة",
                                12,
                                GRAY,
                                false
                        )
                );

                Button open =
                        secondary(
                                "دخول بالكود"
                        );

                card.addView(
                        open,
                        lp(-1, dp(46), 0, 10, 0, 0)
                );

                open.setOnClickListener(
                        v -> askExamCode(id)
                );

                page.addView(
                        card,
                        lp(-1, -2, 0, 0, 0, 10)
                );
            }
        }

        addBack(
                page,
                this::showStudentHome
        );

        setPage(page, true);
    }

    private void askExamCode(
            String examId
    ) {

        EditText code =
                input("كود الامتحان");

        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle("دخول الامتحان")
                        .setView(code)
                        .setNegativeButton(
                                "إلغاء",
                                null
                        )
                        .setPositiveButton(
                                "دخول",
                                null
                        )
                        .create();

        dialog.setOnShowListener(
                d -> {

                    dialog.getButton(
                            AlertDialog.BUTTON_POSITIVE
                    ).setOnClickListener(
                            v -> {

                                String c =
                                        code.getText()
                                                .toString()
                                                .trim();

                                JSONObject exam =
                                        findExamByCode(c);

                                if (exam == null) {

                                    code.setError(
                                            "الكود غير صحيح"
                                    );

                                    return;
                                }

                                if (
                                        !examId.isEmpty()
                                        &&
                                        !exam.optString(
                                                "id"
                                        ).equals(examId)
                                ) {

                                    code.setError(
                                            "الكود لا يخص هذا الامتحان"
                                    );

                                    return;
                                }

                                if (
                                        isCodeUsed(
                                                c,
                                                currentStudent
                                        )
                                ) {

                                    code.setError(
                                            "تم استخدام الكود من قبل"
                                    );

                                    return;
                                }

                                dialog.dismiss();

                                startExam(
                                        exam,
                                        c
                                );
                            }
                    );
                }
        );

        dialog.show();
    }

    /* =========================================================
       EXAM
       ========================================================= */

    private void startExam(
            JSONObject exam,
            String code
    ) {

        JSONArray questions =
                exam.optJSONArray(
                        "questions"
                );

        if (
                questions == null
                ||
                questions.length() == 0
        ) {

            toast(
                    "هذا الامتحان لا يحتوي على أسئلة بعد.\n" +
                    "المدرس يحتاج لإضافة الأسئلة أولاً."
            );

            return;
        }

        currentExamId =
                exam.optString("id");

        currentQuestions =
                questions;

        currentQuestion = 0;

        currentAnswers =
                new int[questions.length()];

        Arrays.fill(
                currentAnswers,
                -1
        );

        int minutes =
                Math.max(
                        1,
                        exam.optInt(
                                "duration",
                                30
                        )
                );

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
        );

        showQuestion();

        if (examTimer != null) {
            examTimer.cancel();
        }

        examTimer =
                new CountDownTimer(
                        minutes * 60000L,
                        1000
                ) {

                    @Override
                    public void onTick(
                            long millisUntilFinished
                    ) {

                        TextView timer =
                                findViewById(
                                        5001
                                );

                        if (timer != null) {

                            timer.setText(
                                    "الوقت المتبقي: " +
                                    formatTime(
                                            millisUntilFinished
                                    )
                            );
                        }
                    }

                    @Override
                    public void onFinish() {

                        submitExam(true);
                    }
                }.start();
    }

    private void showQuestion() {

        LinearLayout page =
                pageBase(
                        "الامتحان",
                        "السؤال " +
                        (currentQuestion + 1) +
                        " من " +
                        currentQuestions.length()
                );

        TextView timer =
                text(
                        "الوقت المتبقي",
                        15,
                        GOLD,
                        true
                );

        timer.setId(5001);

        timer.setGravity(
                Gravity.CENTER
        );

        page.addView(
                timer,
                lp(-1, dp(45), 0, 0, 0, 8)
        );

        JSONObject question =
                currentQuestions.optJSONObject(
                        currentQuestion
                );

        if (question == null) {

            submitExam(false);

            return;
        }

        LinearLayout questionBox =
                panel();

        questionBox.setPadding(
                dp(16),
                dp(18),
                dp(16),
                dp(18)
        );

        TextView q =
                text(
                        question.optString(
                                "question",
                                ""
                        ),
                        19,
                        textColor(),
                        true
                );

        q.setGravity(
                Gravity.RIGHT
        );

        q.setIncludeFontPadding(true);

        questionBox.addView(q);

        page.addView(
                questionBox,
                lp(-1, -2, 0, 0, 0, 12)
        );

        String[] letters =
                {
                        "أ",
                        "ب",
                        "ج",
                        "د"
                };

        for (int i = 0; i < 4; i++) {

            final int index = i;

            RadioButton option =
                    new RadioButton(this);

            option.setText(
                    letters[i] +
                    "  " +
                    question.optString(
                            "o" + i,
                            ""
                    )
            );

            option.setTextSize(15);

            option.setTextColor(
                    textColor()
            );

            option.setGravity(
                    Gravity.RIGHT |
                    Gravity.CENTER_VERTICAL
            );

            option.setPadding(
                    dp(12),
                    dp(8),
                    dp(12),
                    dp(8)
            );

            option.setButtonTintList(
                    new ColorStateList(
                            new int[][]{
                                    new int[]{
                                            android.R.attr.state_checked
                                    },
                                    new int[]{}
                            },
                            new int[]{
                                    GOLD,
                                    GRAY
                            }
                    )
            );

            if (
                    currentAnswers[
                            currentQuestion
                    ] == i
            ) {

                option.setChecked(true);
            }

            option.setOnClickListener(
                    v ->
                            currentAnswers[
                                    currentQuestion
                            ] = index
            );

            page.addView(
                    option,
                    lp(-1, dp(58), 0, 0, 0, 7)
            );
        }

        LinearLayout buttons =
                horizontal();

        Button previous =
                secondary("السابق");

        Button next =
                primary(
                        currentQuestion ==
                        currentQuestions.length() - 1
                                ? "إنهاء الامتحان"
                                : "التالي"
                );

        buttons.addView(
                previous,
                new LinearLayout.LayoutParams(
                        0,
                        dp(54),
                        1
                )
        );

        buttons.addView(spaceW(8));

        buttons.addView(
                next,
                new LinearLayout.LayoutParams(
                        0,
                        dp(54),
                        1
                )
        );

        page.addView(
                buttons,
                lp(-1, dp(54), 0, 16, 0, 0)
        );

        previous.setEnabled(
                currentQuestion > 0
        );

        previous.setOnClickListener(
                v -> {

                    currentQuestion--;

                    showQuestion();
                }
        );

        next.setOnClickListener(
                v -> {

                    if (
                            currentAnswers[
                                    currentQuestion
                            ] < 0
                    ) {

                        toast(
                                "اختار إجابة قبل الانتقال"
                        );

                        return;
                    }

                    if (
                            currentQuestion <
                            currentQuestions.length() - 1
                    ) {

                        currentQuestion++;

                        showQuestion();

                    } else {

                        confirmSubmit();
                    }
                }
        );

        setContentView(page);
    }

    private void confirmSubmit() {

        new AlertDialog.Builder(this)
                .setTitle(
                        "تسليم الامتحان"
                )
                .setMessage(
                        "هل أنت متأكد من تسليم الامتحان؟"
                )
                .setNegativeButton(
                        "رجوع",
                        null
                )
                .setPositiveButton(
                        "تسليم",
                        (d, w) ->
                                submitExam(false)
                )
                .show();
    }

    private void submitExam(
            boolean automatic
    ) {

        if (examTimer != null) {

            examTimer.cancel();

            examTimer = null;
        }

        getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_SECURE
        );

        JSONObject exam =
                findExamById(
                        currentExamId
                );

        if (exam == null) {

            showStudentHome();

            return;
        }

        JSONArray questions =
                exam.optJSONArray(
                        "questions"
                );

        int correct = 0;
        int wrong = 0;
        int answered = 0;

        if (questions != null) {

            for (
                    int i = 0;
                    i < questions.length();
                    i++
            ) {

                JSONObject q =
                        questions.optJSONObject(i);

                int answer =
                        currentAnswers[i];

                if (answer >= 0) {

                    answered++;

                    if (
                            q != null
                            &&
                            answer ==
                            q.optInt(
                                    "correct",
                                    -100
                            )
                    ) {

                        correct++;

                    } else {

                        wrong++;
                    }
                }
            }
        }

        JSONObject result =
                new JSONObject();

        try {

            result.put(
                    "id",
                    UUID.randomUUID()
                            .toString()
            );

            result.put(
                    "student",
                    currentStudent
            );

            result.put(
                    "examId",
                    currentExamId
            );

            result.put(
                    "exam",
                    exam.optString(
                            "title"
                    )
            );

            result.put(
                    "correct",
                    correct
            );

            result.put(
                    "wrong",
                    wrong
            );

            result.put(
                    "answered",
                    answered
            );

            result.put(
                    "total",
                    questions == null
                            ? 0
                            : questions.length()
            );

            result.put(
                    "automatic",
                    automatic
            );

            JSONArray answers =
                    new JSONArray();

            for (
                    int answer :
                    currentAnswers
            ) {

                answers.put(answer);
            }

            result.put(
                    "answers",
                    answers
            );

            result.put(
                    "time",
                    System.currentTimeMillis()
            );

            appendArray(
                    "results",
                    result
            );

            markCodeUsed(
                    exam.optString(
                            "code",
                            ""
                    ),
                    currentStudent
            );

        } catch (Exception ignored) {
        }

        showResult(
                result,
                exam
        );
    }

    /* =========================================================
       RESULT
       ========================================================= */

    private void showResult(
            JSONObject result,
            JSONObject exam
    ) {

        LinearLayout page =
                pageBase(
                        "نتيجة الامتحان",
                        "تم حفظ النتيجة في حساب الطالب"
                );

        int correct =
                result.optInt(
                        "correct"
                );

        int wrong =
                result.optInt(
                        "wrong"
                );

        int answered =
                result.optInt(
                        "answered"
                );

        int total =
                result.optInt(
                        "total"
                );

        TextView score =
                text(
                        correct +
                        " / " +
                        total,
                        36,
                        GOLD,
                        true
                );

        score.setGravity(
                Gravity.CENTER
        );

        page.addView(
                score,
                lp(-1, dp(70), 0, 5, 0, 5)
        );

        page.addView(
                messageCard(
                        "الإجابات الصحيحة: " +
                        correct +
                        "\nالإجابات الخاطئة: " +
                        wrong +
                        "\nالإجابات المجابة: " +
                        answered +
                        "\nغير المجاب: " +
                        Math.max(
                                0,
                                total - answered
                        )
                )
        );

        JSONArray questions =
                exam.optJSONArray(
                        "questions"
                );

        JSONArray answers =
                result.optJSONArray(
                        "answers"
                );

        if (
                questions != null
                &&
                answers != null
        ) {

            page.addView(
                    sectionTitle(
                            "مراجعة الإجابات"
                    )
            );

            for (
                    int i = 0;
                    i < questions.length();
                    i++
            ) {

                JSONObject q =
                        questions.optJSONObject(i);

                if (q == null) continue;

                int answer =
                        answers.optInt(
                                i,
                                -1
                        );

                int correctAnswer =
                        q.optInt(
                                "correct",
                                -1
                        );

                boolean right =
                        answer ==
                        correctAnswer;

                LinearLayout card =
                        panel();

                card.addView(
                        text(
                                (i + 1) +
                                ". " +
                                (
                                        right
                                                ? "✓ إجابة صحيحة"
                                                : "✗ إجابة غير صحيحة"
                                ),
                                15,
                                right
                                        ? GREEN
                                        : RED,
                                true
                        )
                );

                card.addView(
                        text(
                                q.optString(
                                        "question"
                                ),
                                14,
                                textColor(),
                                true
                        )
                );

                if (answer >= 0) {

                    card.addView(
                            text(
                                    "إجابتك: " +
                                    q.optString(
                                            "o" +
                                            answer
                                    ),
                                    13,
                                    GRAY,
                                    false
                            )
                    );
                }

                if (correctAnswer >= 0) {

                    card.addView(
                            text(
                                    "الإجابة الصحيحة: " +
                                    q.optString(
                                            "o" +
                                            correctAnswer
                                    ),
                                    13,
                                    GREEN,
                                    false
                            )
                    );
                }

                String explanation =
                        q.optString(
                                "explanation",
                                ""
                        );

                if (!explanation.isEmpty()) {

                    card.addView(
                            text(
                                    "الشرح: " +
                                    explanation,
                                    13,
                                    textColor(),
                                    false
                            )
                    );
                }

                page.addView(
                        card,
                        lp(-1, -2, 0, 0, 0, 8)
                );
            }
        }

        Button home =
                primary(
                        "العودة لحسابي"
                );

        page.addView(
                home,
                lp(-1, dp(54), 0, 16, 0, 8)
        );

        home.setOnClickListener(
                v -> showStudentHome()
        );

        setPage(
                page,
                true
        );
    }

    /* =========================================================
       STUDENT RESULTS
       ========================================================= */

    private void showStudentResults() {

        LinearLayout page =
                pageBase(
                        "نتائجي",
                        "كل نتائج الامتحانات الخاصة بك"
                );

        JSONArray results =
                readArray(
                        "results"
                );

        boolean found = false;

        for (
                int i = results.length() - 1;
                i >= 0;
                i--
        ) {

            JSONObject r =
                    results.optJSONObject(i);

            if (r == null) continue;

            if (
                    !r.optString(
                            "student"
                    ).equals(
                            currentStudent
                    )
            ) continue;

            found = true;

            int correct =
                    r.optInt(
                            "correct"
                    );

            int total =
                    r.optInt(
                            "total"
                    );

            LinearLayout card =
                    panel();

            card.addView(
                    text(
                            "📝 " +
                            r.optString(
                                    "exam"
                            ),
                            17,
                            textColor(),
                            true
                    )
            );

            card.addView(
                    text(
                            "النتيجة: " +
                            correct +
                            " / " +
                            total,
                            15,
                            GOLD,
                            true
                    )
            );

            card.addView(
                    text(
                            "صحيح: " +
                            r.optInt("correct") +
                            "   |   خطأ: " +
                            r.optInt("wrong") +
                            "   |   مجاب: " +
                            r.optInt("answered"),
                            12,
                            GRAY,
                            false
                    )
            );

            page.addView(
                    card,
                    lp(-1, -2, 0, 0, 0, 8)
            );
        }

        if (!found) {

            page.addView(
                    emptyCard(
                            "لا توجد نتائج حتى الآن."
                    )
            );
        }

        addBack(
                page,
                this::showStudentHome
        );

        setPage(
                page,
                true
        );
    }

    /* =========================================================
       TEACHER LOGIN
       ========================================================= */

    private void showTeacherLogin() {

        role = "teacher";

        LinearLayout page =
                pageBase(
                        "دخول المدرس",
                        "مساحة خاصة لإدارة المحتوى"
                );

        EditText name =
                input(
                        "اسم المدرس"
                );

        EditText code =
                input(
                        "رمز الدخول"
                );

        page.addView(
                label("اسم المدرس")
        );

        page.addView(
                name,
                lp(-1, dp(58), 0, 0, 0, 12)
        );

        page.addView(
                label("رمز الدخول")
        );

        page.addView(
                code,
                lp(-1, dp(58), 0, 0, 0, 16)
        );

        page.addView(
                messageCard(
                        "رمز الدخول التجريبي المحلي: 1234\n\n" +
                        "يمكن تغيير نظام الدخول لاحقاً عند ربط Firebase."
                )
        );

        Button enter =
                primary(
                        "دخول إلى لوحة المدرس"
                );

        page.addView(
                enter,
                lp(-1, dp(56), 0, 8, 0, 8)
        );

        enter.setOnClickListener(v -> {

            String teacherName =
                    name.getText()
                            .toString()
                            .trim();

            String password =
                    code.getText()
                            .toString()
                            .trim();

            if (teacherName.isEmpty()) {

                toast(
                        "اكتب اسم المدرس"
                );

                return;
            }

            if (!password.equals("1234")) {

                toast(
                        "رمز الدخول غير صحيح"
                );

                return;
            }

            currentTeacher =
                    teacherName;

            prefs.edit()
                    .putString(
                            "teacher_name",
                            teacherName
                    )
                    .apply();

            showTeacherHome();
        });

        addBack(
                page,
                this::showHome
        );

        setPage(
                page,
                true
        );
    }

    /* =========================================================
       TEACHER HOME
       ========================================================= */

    private void showTeacherHome() {

        role = "teacher";

        if (currentTeacher.isEmpty()) {

            currentTeacher =
                    prefs.getString(
                            "teacher_name",
                            "المدرس"
                    );
        }

        LinearLayout page =
                pageBase(
                        "أهلاً يا " +
                        currentTeacher,
                        "لوحة تحكم المدرس"
                );

        page.addView(
                sectionTitle(
                        "إدارة المنصة"
                )
        );

        LinearLayout row1 =
                horizontal();

        row1.addView(
                actionCard(
                        "📝",
                        "الامتحانات",
                        "إنشاء وإدارة",
                        v -> teacherExams()
                ),
                new LinearLayout.LayoutParams(
                        0,
                        dp(130),
                        1
                )
        );

        row1.addView(spaceW(10));

        row1.addView(
                actionCard(
                        "❓",
                        "الأسئلة",
                        "بنك الأسئلة",
                        v -> teacherQuestions()
                ),
                new LinearLayout.LayoutParams(
                        0,
                        dp(130),
                        1
                )
        );

        page.addView(row1);

        LinearLayout row2 =
                horizontal();

        row2.addView(
                actionCard(
                        "🔑",
                        "الأكواد",
                        "أكواد الامتحانات",
                        v -> teacherCodes()
                ),
                new LinearLayout.LayoutParams(
                        0,
                        dp(130),
                        1
                )
        );

        row2.addView(spaceW(10));

        row2.addView(
                actionCard(
                        "👨‍🎓",
                        "الطلاب",
                        "النتائج والدرجات",
                        v -> teacherResults()
                ),
                new LinearLayout.LayoutParams(
                        0,
                        dp(130),
                        1
                )
        );

        page.addView(
                row2,
                lp(-1, -2, 0, 10, 0, 0)
        );

        LinearLayout row3 =
                horizontal();

        row3.addView(
                actionCard(
                        "📚",
                        "المذكرات",
                        "إضافة شرح",
                        v -> showNotes(true)
                ),
                new LinearLayout.LayoutParams(
                        0,
                        dp(130),
                        1
                )
        );

        row3.addView(spaceW(10));

        row3.addView(
                actionCard(
                        "👥",
                        "المجموعات",
                        "مجموعات الطلاب",
                        v -> teacherGroups()
                ),
                new LinearLayout.LayoutParams(
                        0,
                        dp(130),
                        1
                )
        );

        page.addView(row3);

        LinearLayout row4 =
                horizontal();

        row4.addView(
                actionCard(
                        "🔔",
                        "التحديثات",
                        "إدارة التنبيهات",
                        v -> teacherUpdates()
                ),
                new LinearLayout.LayoutParams(
                        0,
                        dp(130),
                        1
                )
        );

        row4.addView(spaceW(10));

        row4.addView(
                actionCard(
                        "⚙",
                        "الإعدادات",
                        "إعدادات المدرس",
                        v -> showSettings(true)
                ),
                new LinearLayout.LayoutParams(
                        0,
                        dp(130),
                        1
                )
        );

        page.addView(row4);

        page.addView(
                messageCard(
                        "كل المحتوى التعليمي تحت تحكم المدرس.\n\n" +
                        "لا يوجد أي امتحان أو سؤال أو كود جاهز من التطبيق."
                )
        );

        Button logout =
                primary(
                        "تسجيل الخروج"
                );

        page.addView(
                logout,
                lp(-1, dp(54), 0, 15, 0, 8)
        );

        logout.setOnClickListener(
                v -> {

                    currentTeacher = "";

                    prefs.edit()
                            .remove(
                                    "teacher_name"
                            )
                            .apply();

                    showHome();
                }
        );

        addBack(
                page,
                this::showHome
        );

        setPage(
                page,
                true
        );
    }

    /* =========================================================
       TEACHER EXAMS
       ========================================================= */

    private void teacherExams() {

        LinearLayout page =
                pageBase(
                        "إدارة الامتحانات",
                        "المدرس ينشئ الامتحانات بنفسه"
                );

        Button add =
                primary(
                        "＋ إنشاء امتحان جديد"
                );

        page.addView(
                add,
                lp(-1, dp(55), 0, 0, 0, 12)
        );

        add.setOnClickListener(
                v -> createExam()
        );

        JSONArray exams =
                readArray("exams");

        if (exams.length() == 0) {

            page.addView(
                    emptyCard(
                            "لا توجد امتحانات.\n" +
                            "اضغط إنشاء امتحان جديد."
                    )
            );

        } else {

            for (
                    int i = 0;
                    i < exams.length();
                    i++
            ) {

                JSONObject exam =
                        exams.optJSONObject(i);

                if (exam == null) continue;

                String id =
                        exam.optString("id");

                String title =
                        exam.optString(
                                "title"
                        );

                JSONArray questions =
                        exam.optJSONArray(
                                "questions"
                        );

                int count =
                        questions == null
                                ? 0
                                : questions.length();

                LinearLayout card =
                        panel();

                card.addView(
                        text(
                                "📝 " + title,
                                17,
                                textColor(),
                                true
                        )
                );

                card.addView(
                        text(
                                "المدة: " +
                                exam.optInt(
                                        "duration",
                                        30
                                ) +
                                " دقيقة\n" +
                                "عدد الأسئلة: " +
                                count +
                                "\nالكود: " +
                                exam.optString(
                                        "code",
                                        "بدون كود"
                                ),
                                12,
                                GRAY,
                                false
                        )
                );

                LinearLayout buttons =
                        horizontal();

                Button questionsButton =
                        secondary(
                                "إدارة الأسئلة"
                        );

                Button delete =
                        secondary(
                                "حذف"
                        );

                buttons.addView(
                        questionsButton,
                        new LinearLayout.LayoutParams(
                                0,
                                dp(48),
                                1
                        )
                );

                buttons.addView(
                        spaceW(7)
                );

                buttons.addView(
                        delete,
                        new LinearLayout.LayoutParams(
                                0,
                                dp(48),
                                1
                        )
                );

                card.addView(
                        buttons
                );

                questionsButton.setOnClickListener(
                        v -> addQuestion(id)
                );

                delete.setOnClickListener(
                        v -> deleteExam(id)
                );

                page.addView(
                        card,
                        lp(-1, -2, 0, 0, 0, 9)
                );
            }
        }

        addBack(
                page,
                this::showTeacherHome
        );

        setPage(
                page,
                true
        );
    }

    private void createExam() {

        LinearLayout box =
                new LinearLayout(this);

        box.setOrientation(
                LinearLayout.VERTICAL
        );

        box.setPadding(
                dp(8),
                dp(5),
                dp(8),
                dp(5)
        );

        EditText title =
                input(
                        "اسم الامتحان"
                );

        EditText duration =
                input(
                        "مدة الامتحان بالدقائق"
                );

        EditText code =
                input(
                        "كود الامتحان"
                );

        box.addView(title);
        box.addView(
                duration,
                lp(-1, dp(55), 0, 8, 0, 0)
        );
        box.addView(
                code,
                lp(-1, dp(55), 0, 8, 0, 0)
        );

        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle(
                                "إنشاء امتحان"
                        )
                        .setView(box)
                        .setNegativeButton(
                                "إلغاء",
                                null
                        )
                        .setPositiveButton(
                                "حفظ",
                                null
                        )
                        .create();

        dialog.setOnShowListener(
                d -> {

                    dialog.getButton(
                            AlertDialog.BUTTON_POSITIVE
                    ).setOnClickListener(
                            v -> {

                                String t =
                                        title.getText()
                                                .toString()
                                                .trim();

                                String du =
                                        duration.getText()
                                                .toString()
                                                .trim();

                                String c =
                                        code.getText()
                                                .toString()
                                                .trim();

                                if (t.isEmpty()) {

                                    title.setError(
                                            "اكتب اسم الامتحان"
                                    );

                                    return;
                                }

                                if (c.isEmpty()) {

                                    code.setError(
                                            "اكتب كود الامتحان"
                                    );

                                    return;
                                }

                                if (
                                        findExamByCode(c)
                                        != null
                                ) {

                                    code.setError(
                                            "هذا الكود مستخدم بالفعل"
                                    );

                                    return;
                                }

                                int minutes = 30;

                                try {

                                    minutes =
                                            Integer.parseInt(
                                                    du
                                            );

                                } catch (
                                        Exception ignored
                                ) {
                                }

                                JSONObject exam =
                                        new JSONObject();

                                try {

                                    exam.put(
                                            "id",
                                            UUID.randomUUID()
                                                    .toString()
                                    );

                                    exam.put(
                                            "title",
                                            t
                                    );

                                    exam.put(
                                            "duration",
                                            Math.max(
                                                    1,
                                                    minutes
                                            )
                                    );

                                    exam.put(
                                            "code",
                                            c
                                    );

                                    exam.put(
                                            "teacher",
                                            currentTeacher
                                    );

                                    exam.put(
                                            "questions",
                                            new JSONArray()
                                    );

                                    appendArray(
                                            "exams",
                                            exam
                                    );

                                } catch (
                                        Exception ignored
                                ) {
                                }

                                dialog.dismiss();

                                toast(
                                        "تم إنشاء الامتحان"
                                );

                                teacherExams();
                            }
                    );
                }
        );

        dialog.show();
    }

    private void deleteExam(
            String id
    ) {

        new AlertDialog.Builder(this)
                .setTitle(
                        "حذف الامتحان"
                )
                .setMessage(
                        "هل تريد حذف هذا الامتحان؟"
                )
                .setNegativeButton(
                        "إلغاء",
                        null
                )
                .setPositiveButton(
                        "حذف",
                        (d, w) -> {

                            JSONArray exams =
                                    readArray(
                                            "exams"
                                    );

                            JSONArray result =
                                    new JSONArray();

                            for (
                                    int i = 0;
                                    i < exams.length();
                                    i++
                            ) {

                                JSONObject e =
                                        exams.optJSONObject(
                                                i
                                        );

                                if (e == null) continue;

                                if (
                                        !e.optString(
                                                "id"
                                        ).equals(id)
                                ) {

                                    result.put(e);
                                }
                            }

                            saveArray(
                                    "exams",
                                    result
                            );

                            teacherExams();
                        }
                )
                .show();
    }

    /* =========================================================
       QUESTIONS
       ========================================================= */

    private void addQuestion(
            String examId
    ) {

        JSONObject exam =
                findExamById(
                        examId
                );

        if (exam == null) return;

        LinearLayout box =
                new LinearLayout(this);

        box.setOrientation(
                LinearLayout.VERTICAL
        );

        EditText question =
                input(
                        "نص السؤال"
                );

        box.addView(question);

        EditText a =
                input(
                        "الإجابة أ"
                );

        EditText b =
                input(
                        "الإجابة ب"
                );

        EditText c =
                input(
                        "الإجابة ج"
                );

        EditText d =
                input(
                        "الإجابة د"
                );

        EditText correct =
                input(
                        "رقم الإجابة الصحيحة: 1 أو 2 أو 3 أو 4"
                );

        EditText explanation =
                input(
                        "شرح الإجابة - اختياري"
                );

        box.addView(a);
        box.addView(b);
        box.addView(c);
        box.addView(d);
        box.addView(correct);
        box.addView(explanation);

        ScrollView scroll =
                new ScrollView(this);

        scroll.addView(box);

        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle(
                                "إضافة سؤال"
                        )
                        .setView(scroll)
                        .setNegativeButton(
                                "إلغاء",
                                null
                        )
                        .setPositiveButton(
                                "إضافة",
                                null
                        )
                        .create();

        dialog.setOnShowListener(
                x -> {

                    dialog.getButton(
                            AlertDialog.BUTTON_POSITIVE
                    ).setOnClickListener(
                            v -> {

                                String q =
                                        question
                                                .getText()
                                                .toString()
                                                .trim();

                                if (q.isEmpty()) {

                                    question.setError(
                                            "اكتب السؤال"
                                    );

                                    return;
                                }

                                int answer;

                                try {

                                    answer =
                                            Integer.parseInt(
                                                    correct
                                                            .getText()
                                                            .toString()
                                                            .trim()
                                            ) - 1;

                                } catch (
                                        Exception e
                                ) {

                                    correct.setError(
                                            "اكتب رقم من 1 إلى 4"
                                    );

                                    return;
                                }

                                if (
                                        answer < 0
                                        ||
                                        answer > 3
                                ) {

                                    correct.setError(
                                            "رقم الإجابة من 1 إلى 4"
                                    );

                                    return;
                                }

                                JSONObject item =
                                        new JSONObject();

                                try {

                                    item.put(
                                            "id",
                                            UUID.randomUUID()
                                                    .toString()
                                    );

                                    item.put(
                                            "question",
                                            q
                                    );

                                    item.put(
                                            "o0",
                                            a.getText()
                                                    .toString()
                                    );

                                    item.put(
                                            "o1",
                                            b.getText()
                                                    .toString()
                                    );

                                    item.put(
                                            "o2",
                                            c.getText()
                                                    .toString()
                                    );

                                    item.put(
                                            "o3",
                                            d.getText()
                                                    .toString()
                                    );

                                    item.put(
                                            "correct",
                                            answer
                                    );

                                    item.put(
                                            "explanation",
                                            explanation
                                                    .getText()
                                                    .toString()
                                    );

                                } catch (
                                        Exception ignored
                                ) {
                                }

                                JSONArray questions =
                                        exam.optJSONArray(
                                                "questions"
                                        );

                                if (questions == null) {
                                    questions =
                                            new JSONArray();
                                }

                                questions.put(item);

                                updateExamQuestions(
                                        examId,
                                        questions
                                );

                                dialog.dismiss();

                                toast(
                                        "تمت إضافة السؤال"
                                );

                                teacherExams();
                            }
                    );
                }
        );

        dialog.show();
    }

    private void updateExamQuestions(
            String examId,
            JSONArray questions
    ) {

        JSONArray exams =
                readArray(
                        "exams"
                );

        JSONArray output =
                new JSONArray();

        for (
                int i = 0;
                i < exams.length();
                i++
        ) {

            JSONObject e =
                    exams.optJSONObject(i);

            if (e == null) continue;

            if (
                    e.optString("id")
                            .equals(examId)
            ) {

                try {
                    e.put(
                            "questions",
                            questions
                    );
                } catch (Exception ignored) {
                }
            }

            output.put(e);
        }

        saveArray(
                "exams",
                output
        );
    }

    private void teacherQuestions() {

        LinearLayout page =
                pageBase(
                        "بنك الأسئلة",
                        "كل الأسئلة التي أنشأها المدرس"
                );

        JSONArray exams =
                readArray("exams");

        boolean found = false;

        for (
                int i = 0;
                i < exams.length();
                i++
        ) {

            JSONObject exam =
                    exams.optJSONObject(i);

            if (exam == null) continue;

            JSONArray qs =
                    exam.optJSONArray(
                            "questions"
                    );

            if (
                    qs == null
                    ||
                    qs.length() == 0
            ) continue;

            found = true;

            page.addView(
                    text(
                            "📝 " +
                            exam.optString(
                                    "title"
                            ),
                            17,
                            GOLD,
                            true
                    ),
                    lp(-1, -2, 0, 8, 0, 5)
            );

            for (
                    int j = 0;
                    j < qs.length();
                    j++
            ) {

                JSONObject q =
                        qs.optJSONObject(j);

                if (q == null) continue;

                LinearLayout card =
                        panel();

                card.addView(
                        text(
                                (j + 1) +
                                ". " +
                                q.optString(
                                        "question"
                                ),
                                14,
                                textColor(),
                                true
                        )
                );

                card.addView(
                        text(
                                "الإجابة الصحيحة: " +
                                q.optString(
                                        "o" +
                                        q.optInt(
                                                "correct",
                                                0
                                        )
                                ),
                                12,
                                GREEN,
                                false
                        )
                );

                page.addView(
                        card,
                        lp(-1, -2, 0, 0, 0, 7)
                );
            }
        }

        if (!found) {

            page.addView(
                    emptyCard(
                            "لا توجد أسئلة حتى الآن.\n" +
                            "أنشئ امتحاناً ثم أضف الأسئلة."
                    )
            );
        }

        addBack(
                page,
                this::showTeacherHome
        );

        setPage(
                page,
                true
        );
    }

    /* =========================================================
       CODES
       ========================================================= */

    private void teacherCodes() {

        LinearLayout page =
                pageBase(
                        "أكواد الامتحانات",
                        "إدارة أكواد دخول الطلاب"
                );

        JSONArray exams =
                readArray("exams");

        if (exams.length() == 0) {

            page.addView(
                    emptyCard(
                            "لا توجد امتحانات أو أكواد."
                    )
            );

        } else {

            for (
                    int i = 0;
                    i < exams.length();
                    i++
            ) {

                JSONObject e =
                        exams.optJSONObject(i);

                if (e == null) continue;

                page.addView(
                        messageCard(
                                "الامتحان: " +
                                e.optString("title") +
                                "\nالكود: " +
                                e.optString(
                                        "code",
                                        ""
                                )
                        )
                );
            }
        }

        addBack(
                page,
                this::showTeacherHome
        );

        setPage(
                page,
                true
        );
    }

    /* =========================================================
       TEACHER RESULTS
       ========================================================= */

    private void teacherResults() {

        LinearLayout page =
                pageBase(
                        "نتائج الطلاب",
                        "كل النتائج المحفوظة على الجهاز"
                );

        JSONArray results =
                readArray("results");

        if (results.length() == 0) {

            page.addView(
                    emptyCard(
                            "لا توجد نتائج حتى الآن."
                    )
            );

        } else {

            for (
                    int i = results.length() - 1;
                    i >= 0;
                    i--
            ) {

                JSONObject r =
                        results.optJSONObject(i);

                if (r == null) continue;

                LinearLayout card =
                        panel();

                card.addView(
                        text(
                                "👨‍🎓 " +
                                r.optString(
                                        "student"
                                ),
                                16,
                                textColor(),
                                true
                        )
                );

                card.addView(
                        text(
                                "الامتحان: " +
                                r.optString(
                                        "exam"
                                ),
                                13,
                                GRAY,
                                false
                        )
                );

                card.addView(
                        text(
                                "الدرجة: " +
                                r.optInt(
                                        "correct"
                                ) +
                                " / " +
                                r.optInt(
                                        "total"
                                ),
                                15,
                                GOLD,
                                true
                        )
                );

                page.addView(
                        card,
                        lp(-1, -2, 0, 0, 0, 8)
                );
            }
        }

        addBack(
                page,
                this::showTeacherHome
        );

        setPage(
                page,
                true
        );
    }

    /* =========================================================
       NOTES
       ========================================================= */

    private void showNotes(
            boolean teacher
    ) {

        LinearLayout page =
                pageBase(
                        teacher
                                ? "إدارة المذكرات"
                                : "مذكرات الشرح",
                        teacher
                                ? "المدرس يضيف المذكرات"
                                : "المذكرات التي أضافها المدرس"
                );

        if (teacher) {

            Button add =
                    primary(
                            "＋ إضافة مذكرة"
                    );

            page.addView(
                    add,
                    lp(-1, dp(54), 0, 0, 0, 12)
            );

            add.setOnClickListener(
                    v -> createNote()
            );
        }

        JSONArray notes =
                readArray(
                        "notes"
                );

        if (notes.length() == 0) {

            page.addView(
                    emptyCard(
                            "لا توجد مذكرات حتى الآن."
                    )
            );

        } else {

            for (
                    int i = 0;
                    i < notes.length();
                    i++
            ) {

                JSONObject n =
                        notes.optJSONObject(i);

                if (n == null) continue;

                LinearLayout card =
                        panel();

                card.addView(
                        text(
                                "📚 " +
                                n.optString(
                                        "title"
                                ),
                                17,
                                GOLD,
                                true
                        )
                );

                card.addView(
                        text(
                                n.optString(
                                        "body"
                                ),
                                14,
                                textColor(),
                                false
                        )
                );

                page.addView(
                        card,
                        lp(-1, -2, 0, 0, 0, 9)
                );
            }
        }

        addBack(
                page,
                teacher
                        ? this::showTeacherHome
                        : this::showStudentHome
        );

        setPage(
                page,
                true
        );
    }

    private void createNote() {

        LinearLayout box =
                new LinearLayout(this);

        box.setOrientation(
                LinearLayout.VERTICAL
        );

        EditText title =
                input(
                        "عنوان المذكرة"
                );

        EditText body =
                input(
                        "محتوى المذكرة"
                );

        body.setMinHeight(
                dp(160)
        );

        box.addView(title);
        box.addView(body);

        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle(
                                "إضافة مذكرة"
                        )
                        .setView(box)
                        .setNegativeButton(
                                "إلغاء",
                                null
                        )
                        .setPositiveButton(
                                "حفظ",
                                null
                        )
                        .create();

        dialog.setOnShowListener(
                x -> {

                    dialog.getButton(
                            AlertDialog.BUTTON_POSITIVE
                    ).setOnClickListener(
                            v -> {

                                if (
                                        title.getText()
                                                .toString()
                                                .trim()
                                                .isEmpty()
                                ) {

                                    title.setError(
                                            "اكتب العنوان"
                                    );

                                    return;
                                }

                                JSONObject note =
                                        new JSONObject();

                                try {

                                    note.put(
                                            "id",
                                            UUID.randomUUID()
                                                    .toString()
                                    );

                                    note.put(
                                            "title",
                                            title.getText()
                                                    .toString()
                                    );

                                    note.put(
                                            "body",
                                            body.getText()
                                                    .toString()
                                    );

                                    appendArray(
                                            "notes",
                                            note
                                    );

                                } catch (Exception ignored) {
                                }

                                dialog.dismiss();

                                showNotes(true);
                            }
                    );
                }
        );

        dialog.show();
    }

    /* =========================================================
       AZKAR
       ========================================================= */

    private void showAzkar(
            boolean logged
    ) {

        LinearLayout page =
                pageBase(
                        "الأذكار",
                        "ورد يومي بسيط"
                );

        page.addView(
                azkarCard(
                        "أذكار الصباح",
                        "ابدأ يومك بذكر الله، والاستغفار، والصلاة على النبي ﷺ."
                )
        );

        page.addView(
                azkarCard(
                        "أذكار المساء",
                        "اختم يومك بذكر الله والدعاء والاستغفار."
                )
        );

        page.addView(
                azkarCard(
                        "الاستغفار",
                        "استغفر الله وأتوب إليه."
                )
        );

        page.addView(
                azkarCard(
                        "الصلاة على النبي ﷺ",
                        "اللهم صل وسلم وبارك على نبينا محمد."
                )
        );

        addBack(
                page,
                logged
                        ? this::showStudentHome
                        : this::showHome
        );

        setPage(
                page,
                true
        );
    }

    private View azkarCard(
            String title,
            String body
    ) {

        LinearLayout card =
                panel();

        card.addView(
                text(
                        "🤲 " + title,
                        17,
                        GOLD,
                        true
                )
        );

        card.addView(
                text(
                        body,
                        14,
                        textColor(),
                        false
                )
        );

        return card;
    }

    /* =========================================================
       GROUPS
       ========================================================= */

    private void teacherGroups() {

        LinearLayout page =
                pageBase(
                        "مجموعات الطلاب",
                        "تنظيم الطلاب داخل مجموعات"
                );

        String[] groups =
                {
                        "جروب أولى بكالوريا بنات",
                        "جروب أولى بكالوريا ولاد",
                        "جروب تانية بكالوريا بنات",
                        "جروب تانية بكالوريا ولاد",
                        "طلاب المايسترو تالتة إعدادي",
                        "جروب ذكرى ومنفعة"
                };

        for (String group : groups) {

            page.addView(
                    messageCard(
                            "👥  " + group
                    )
            );
        }

        addBack(
                page,
                this::showTeacherHome
        );

        setPage(
                page,
                true
        );
    }

    /* =========================================================
       UPDATES
       ========================================================= */

    private void showUpdates() {

        LinearLayout page =
                pageBase(
                        "التحديثات",
                        "آخر أخبار المنصة"
                );

        JSONArray updates =
                readArray(
                        "updates"
                );

        if (updates.length() == 0) {

            page.addView(
                    emptyCard(
                            "لا توجد تحديثات حالياً."
                    )
            );

        } else {

            for (
                    int i = 0;
                    i < updates.length();
                    i++
            ) {

                JSONObject u =
                        updates.optJSONObject(i);

                if (u == null) continue;

                page.addView(
                        messageCard(
                                "🔔 " +
                                u.optString(
                                        "text"
                                )
                        )
                );
            }
        }

        addBack(
                page,
                this::showStudentHome
        );

        setPage(
                page,
                true
        );
    }

    private void teacherUpdates() {

        LinearLayout page =
                pageBase(
                        "إدارة التحديثات",
                        "إضافة رسالة تظهر للطلاب"
                );

        Button add =
                primary(
                        "＋ إضافة تحديث"
                );

        page.addView(
                add,
                lp(-1, dp(54), 0, 0, 0, 12)
        );

        add.setOnClickListener(
                v -> {

                    EditText input =
                            input(
                                    "نص التحديث"
                            );

                    new AlertDialog.Builder(this)
                            .setTitle(
                                    "تحديث جديد"
                            )
                            .setView(input)
                            .setNegativeButton(
                                    "إلغاء",
                                    null
                            )
                            .setPositiveButton(
                                    "نشر",
                                    (d, w) -> {

                                        JSONObject u =
                                                new JSONObject();

                                        try {

                                            u.put(
                                                    "text",
                                                    input.getText()
                                                            .toString()
                                            );

                                            appendArray(
                                                    "updates",
                                                    u
                                            );

                                        } catch (
                                                Exception ignored
                                        ) {
                                        }

                                        teacherUpdates();
                                    }
                            )
                            .show();
                }
        );

        addBack(
                page,
                this::showTeacherHome
        );

        setPage(
                page,
                true
        );
    }

    /* =========================================================
       ACHIEVEMENTS
       ========================================================= */

    private void showAchievements() {

        LinearLayout page =
                pageBase(
                        "إنجازاتي",
                        "تابع تقدمك في المذاكرة"
                );

        JSONArray results =
                readArray(
                        "results"
                );

        int exams = 0;
        int correct = 0;

        for (
                int i = 0;
                i < results.length();
                i++
        ) {

            JSONObject r =
                    results.optJSONObject(i);

            if (r == null) continue;

            if (
                    r.optString(
                            "student"
                    ).equals(
                            currentStudent
                    )
            ) {

                exams++;

                correct +=
                        r.optInt(
                                "correct"
                        );
            }
        }

        page.addView(
                messageCard(
                        "🏆 الامتحانات المنجزة: " +
                        exams +
                        "\n\n" +
                        "✓ مجموع الإجابات الصحيحة: " +
                        correct
                )
        );

        page.addView(
                messageCard(
                        "استمر في التعلم والمراجعة، وكل نتيجة جديدة ستظهر في حسابك."
                )
        );

        addBack(
                page,
                this::showStudentHome
        );

        setPage(
                page,
                true
        );
    }

    /* =========================================================
       SETTINGS
       ========================================================= */

    private void showSettings(
            boolean teacher
    ) {

        LinearLayout page =
                pageBase(
                        "الإعدادات",
                        "تحكم في مظهر التطبيق"
                );

        page.addView(
                sectionTitle(
                        "المظهر"
                )
        );

        Button theme =
                secondary(
                        darkMode
                                ? "تفعيل الوضع النهاري"
                                : "تفعيل الوضع الليلي"
                );

        page.addView(
                theme,
                lp(-1, dp(54), 0, 0, 0, 10)
        );

        theme.setOnClickListener(
                v -> {

                    darkMode =
                            !darkMode;

                    prefs.edit()
                            .putBoolean(
                                    "dark_mode",
                                    darkMode
                            )
                            .apply();

                    showSettings(
                            teacher
                    );
                }
        );

        page.addView(
                messageCard(
                        "🔒 أثناء الامتحان يتم تفعيل حماية الشاشة لمنع التقاط الشاشة."
                )
        );

        page.addView(
                messageCard(
                        "ℹ️ التطبيق في هذه النسخة يحفظ البيانات محلياً على الجهاز."
                )
        );

        page.addView(
                messageCard(
                        "👨‍💻 مع المبرمج أو المطور محمود كليب\n01112244710"
                )
        );

        addBack(
                page,
                teacher
                        ? this::showTeacherHome
                        : (
                        role.equals("student")
                                ? this::showStudentHome
                                : this::showHome
                )
        );

        setPage(
                page,
                true
        );
    }

    /* =========================================================
       UI HELPERS
       ========================================================= */

    private LinearLayout pageBase(
            String title,
            String subtitle
    ) {

        LinearLayout page =
                new LinearLayout(this);

        page.setOrientation(
                LinearLayout.VERTICAL
        );

        page.setBackgroundColor(
                backgroundColor()
        );

        ScrollView scroll =
                new ScrollView(this);

        LinearLayout inside =
                new LinearLayout(this);

        inside.setOrientation(
                LinearLayout.VERTICAL
        );

        inside.setPadding(
                dp(16),
                dp(16),
                dp(16),
                dp(25)
        );

        LinearLayout header =
                panel();

        header.setPadding(
                dp(16),
                dp(16),
                dp(16),
                dp(16)
        );

        TextView t =
                text(
                        title,
                        24,
                        GOLD,
                        true
                );

        t.setGravity(
                Gravity.RIGHT
        );

        header.addView(t);

        TextView s =
                text(
                        subtitle,
                        13,
                        textColor(),
                        false
                );

        s.setGravity(
                Gravity.RIGHT
        );

        header.addView(s);

        inside.addView(
                header,
                lp(-1, -2, 0, 0, 0, 14)
        );

        scroll.addView(inside);

        page.addView(
                scroll,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        return page;
    }

    private void setPage(
            LinearLayout page,
            boolean navigation
    ) {

        if (!navigation) {

            setContentView(page);

            return;
        }

        LinearLayout wrapper =
                new LinearLayout(this);

        wrapper.setOrientation(
                LinearLayout.VERTICAL
        );

        wrapper.setBackgroundColor(
                backgroundColor()
        );

        wrapper.addView(
                page,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        wrapper.addView(
                bottomForRole(),
                new LinearLayout.LayoutParams(
                        -1,
                        dp(70)
                )
        );

        setContentView(wrapper);
    }

    private LinearLayout bottomForRole() {

        LinearLayout bar =
                navBar();

        bar.addView(
                navItem(
                        "⌂",
                        "الرئيسية",
                        v -> {
                            if (
                                    role.equals(
                                            "teacher"
                                    )
                            ) {
                                showTeacherHome();
                            } else if (
                                    role.equals(
                                            "student"
                                    )
                            ) {
                                showStudentHome();
                            } else {
                                showHome();
                            }
                        }
                ),
                new LinearLayout.LayoutParams(
                        0,
                        -1,
                        1
                )
        );

        bar.addView(
                navItem(
                        "📝",
                        "الامتحانات",
                        v -> {
                            if (
                                    role.equals(
                                            "teacher"
                                    )
                            ) {
                                teacherExams();
                            } else if (
                                    role.equals(
                                            "student"
                                    )
                            ) {
                                showStudentExams();
                            } else {
                                toast(
                                        "اختار طالب أو مدرس"
                                );
                            }
                        }
                ),
                new LinearLayout.LayoutParams(
                        0,
                        -1,
                        1
                )
        );

        bar.addView(
                navItem(
                        "📚",
                        "المذكرات",
                        v -> {
                            if (
                                    role.equals(
                                            "teacher"
                                    )
                            ) {
                                showNotes(true);
                            } else if (
                                    role.equals(
                                            "student"
                                    )
                            ) {
                                showNotes(false);
                            } else {
                                toast(
                                        "اختار طالب أو مدرس"
                                );
                            }
                        }
                ),
                new LinearLayout.LayoutParams(
                        0,
                        -1,
                        1
                )
        );

        bar.addView(
                navItem(
                        "🤲",
                        "الأذكار",
                        v -> showAzkar(
                                role.equals(
                                        "student"
                                )
                        )
                ),
                new LinearLayout.LayoutParams(
                        0,
                        -1,
                        1
                )
        );

        bar.addView(
                navItem(
                        "⚙",
                        "الإعدادات",
                        v -> showSettings(
                                role.equals(
                                        "teacher"
                                )
                        )
                ),
                new LinearLayout.LayoutParams(
                        0,
                        -1,
                        1
                )
        );

        return bar;
    }

    private LinearLayout navBar() {

        LinearLayout bar =
                new LinearLayout(this);

        bar.setOrientation(
                LinearLayout.HORIZONTAL
        );

        bar.setGravity(
                Gravity.CENTER
        );

        bar.setPadding(
                dp(5),
                dp(4),
                dp(5),
                dp(4)
        );

        bar.setBackgroundColor(
                DARK
        );

        return bar;
    }

    private View navItem(
            String icon,
            String title,
            View.OnClickListener listener
    ) {

        LinearLayout item =
                new LinearLayout(this);

        item.setOrientation(
                LinearLayout.VERTICAL
        );

        item.setGravity(
                Gravity.CENTER
        );

        TextView i =
                text(
                        icon,
                        20,
                        GOLD,
                        false
                );

        i.setGravity(
                Gravity.CENTER
        );

        item.addView(i);

        TextView t =
                text(
                        title,
                        9,
                        WHITE,
                        false
                );

        t.setGravity(
                Gravity.CENTER
        );

        item.addView(t);

        item.setOnClickListener(
                listener
        );

        return item;
    }

    private TextView sectionTitle(
            String title
    ) {

        TextView t =
                text(
                        "✦  " + title,
                        19,
                        GOLD,
                        true
                );

        t.setGravity(
                Gravity.RIGHT
        );

        t.setPadding(
                0,
                dp(12),
                0,
                dp(8)
        );

        return t;
    }

    private TextView label(
            String text
    ) {

        TextView t =
                text(
                        text,
                        14,
                        textColor(),
                        true
                );

        t.setGravity(
                Gravity.RIGHT
        );

        return t;
    }

    private EditText input(
            String hint
    ) {

        EditText e =
                new EditText(this);

        e.setHint(hint);
        e.setTextSize(15);
        e.setTextColor(textColor());
        e.setHintTextColor(GRAY);
        e.setGravity(
                Gravity.RIGHT |
                Gravity.CENTER_VERTICAL
        );

        e.setPadding(
                dp(14),
                dp(8),
                dp(14),
                dp(8)
        );

        GradientDrawable bg =
                new GradientDrawable();

        bg.setColor(
                darkMode
                        ? Color.rgb(
                                20,
                                48,
                                38
                        )
                        : WHITE
        );

        bg.setCornerRadius(
                dp(12)
        );

        bg.setStroke(
                dp(1),
                GOLD
        );

        e.setBackground(bg);

        return e;
    }

    private Button primary(
            String title
    ) {

        Button b =
                new Button(this);

        b.setText(title);
        b.setTextSize(14);
        b.setTextColor(DARK);
        b.setGravity(Gravity.CENTER);
        b.setAllCaps(false);
        b.setPadding(
                dp(8),
                0,
                dp(8),
                0
        );

        GradientDrawable bg =
                new GradientDrawable();

        bg.setColor(GOLD);

        bg.setCornerRadius(
                dp(14)
        );

        b.setBackground(bg);

        return b;
    }

    private Button secondary(
            String title
    ) {

        Button b =
                new Button(this);

        b.setText(title);
        b.setTextSize(13);
        b.setTextColor(GOLD);
        b.setGravity(Gravity.CENTER);
        b.setAllCaps(false);

        GradientDrawable bg =
                new GradientDrawable();

        bg.setColor(
                darkMode
                        ? Color.rgb(
                                18,
                                50,
                                38
                        )
                        : WHITE
        );

        bg.setCornerRadius(
                dp(14)
        );

        bg.setStroke(
                dp(1),
                GOLD
        );

        b.setBackground(bg);

        return b;
    }

    private LinearLayout panel() {

        LinearLayout p =
                new LinearLayout(this);

        p.setOrientation(
                LinearLayout.VERTICAL
        );

        GradientDrawable bg =
                new GradientDrawable();

        bg.setColor(
                darkMode
                        ? Color.rgb(
                                12,
                                48,
                                35
                        )
                        : WHITE
        );

        bg.setCornerRadius(
                dp(18)
        );

        bg.setStroke(
                dp(1),
                darkMode
                        ? Color.rgb(
                                100,
                                90,
                                45
                        )
                        : Color.rgb(
                                220,
                                205,
                                150
                        )
        );

        p.setBackground(bg);

        return p;
    }

    private LinearLayout clickablePanel() {

        LinearLayout p =
                panel();

        p.setClickable(true);

        p.setFocusable(true);

        return p;
    }

    private LinearLayout actionCard(
            String icon,
            String title,
            String subtitle,
            View.OnClickListener listener
    ) {

        LinearLayout card =
                clickablePanel();

        card.setGravity(
                Gravity.CENTER
        );

        card.setPadding(
                dp(5),
                dp(9),
                dp(5),
                dp(9)
        );

        TextView i =
                text(
                        icon,
                        27,
                        GOLD,
                        false
                );

        i.setGravity(
                Gravity.CENTER
        );

        card.addView(i);

        TextView t =
                text(
                        title,
                        14,
                        textColor(),
                        true
                );

        t.setGravity(
                Gravity.CENTER
        );

        card.addView(t);

        TextView s =
                text(
                        subtitle,
                        9,
                        GRAY,
                        false
                );

        s.setGravity(
                Gravity.CENTER
        );

        card.addView(s);

        card.setOnClickListener(
                listener
        );

        return card;
    }

    private LinearLayout messageCard(
            String message
    ) {

        LinearLayout card =
                panel();

        card.setPadding(
                dp(14),
                dp(13),
                dp(14),
                dp(13)
        );

        TextView t =
                text(
                        message,
                        13,
                        textColor(),
                        false
                );

        t.setGravity(
                Gravity.RIGHT
        );

        card.addView(t);

        card.setPadding(
                dp(15),
                dp(15),
                dp(15),
                dp(15)
        );

        contentSafeAdd(card);

        return card;
    }

    private LinearLayout emptyCard(
            String message
    ) {

        LinearLayout card =
                panel();

        card.setGravity(
                Gravity.CENTER
        );

        card.setPadding(
                dp(20),
                dp(25),
                dp(20),
                dp(25)
        );

        TextView t =
                text(
                        message,
                        14,
                        GRAY,
                        false
                );

        t.setGravity(
                Gravity.CENTER
        );

        card.addView(t);

        return card;
    }

    private void contentSafeAdd(
            View v
    ) {

        if (content != null) {
            // يستخدم فقط مع الصفحات التي أنشأها pageBase.
            // لا يضيف شيئاً إذا كانت الصفحة غير موجودة.
        }
    }

    private LinearLayout horizontal() {

        LinearLayout l =
                new LinearLayout(this);

        l.setOrientation(
                LinearLayout.HORIZONTAL
        );

        l.setGravity(
                Gravity.CENTER
        );

        return l;
    }

    private TextView text(
            String value,
            float size,
            int color,
            boolean bold
    ) {

        TextView t =
                new TextView(this);

        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);

        if (bold) {
            t.setTypeface(
                    null,
                    android.graphics.Typeface.BOLD
            );
        }

        t.setIncludeFontPadding(true);

        t.setPadding(
                dp(2),
                dp(3),
                dp(2),
                dp(3)
        );

        return t;
    }

    private LinearLayout spaceW(
            int width
    ) {

        LinearLayout s =
                new LinearLayout(this);

        s.setLayoutParams(
                new LinearLayout.LayoutParams(
                        dp(width),
                        1
                )
        );

        return s;
    }

    private void addBack(
            LinearLayout page,
            final Runnable action
    ) {

        Button back =
                secondary(
                        "← رجوع"
                );

        LinearLayout.LayoutParams params =
                lp(
                        -1,
                        dp(52),
                        0,
                        10,
                        0,
                        0
                );

        page.addView(
                back,
                params
        );

        back.setOnClickListener(
                v -> action.run()
        );
    }

    /* =========================================================
       DATA
       ========================================================= */

    private JSONArray readArray(
            String key
    ) {

        String raw =
                prefs.getString(
                        key,
                        "[]"
                );

        try {

            return new JSONArray(raw);

        } catch (Exception e) {

            return new JSONArray();
        }
    }

    private void saveArray(
            String key,
            JSONArray array
    ) {

        prefs.edit()
                .putString(
                        key,
                        array.toString()
                )
                .apply();
    }

    private void appendArray(
            String key,
            JSONObject object
    ) {

        JSONArray array =
                readArray(key);

        array.put(object);

        saveArray(
                key,
                array
        );
    }

    private JSONObject findExamById(
            String id
    ) {

        JSONArray exams =
                readArray(
                        "exams"
                );

        for (
                int i = 0;
                i < exams.length();
                i++
        ) {

            JSONObject e =
                    exams.optJSONObject(i);

            if (e == null) continue;

            if (
                    e.optString(
                            "id"
                    ).equals(id)
            ) {

                return e;
            }
        }

        return null;
    }

    private JSONObject findExamByCode(
            String code
    ) {

        JSONArray exams =
                readArray(
                        "exams"
                );

        for (
                int i = 0;
                i < exams.length();
                i++
        ) {

            JSONObject e =
                    exams.optJSONObject(i);

            if (e == null) continue;

            if (
                    e.optString(
                            "code",
                            ""
                    ).equals(code)
            ) {

                return e;
            }
        }

        return null;
    }

    private boolean isCodeUsed(
            String code,
            String student
    ) {

        JSONArray used =
                readArray(
                        "used_codes"
                );

        for (
                int i = 0;
                i < used.length();
                i++
        ) {

            JSONObject item =
                    used.optJSONObject(i);

            if (item == null) continue;

            if (
                    item.optString(
                            "code"
                    ).equals(code)
                    &&
                    item.optString(
                            "student"
                    ).equals(student)
            ) {

                return true;
            }
        }

        return false;
    }

    private void markCodeUsed(
            String code,
            String student
    ) {

        if (code.isEmpty()) return;

        JSONObject item =
                new JSONObject();

        try {

            item.put(
                    "code",
                    code
            );

            item.put(
                    "student",
                    student
            );

            item.put(
                    "time",
                    System.currentTimeMillis()
            );

        } catch (Exception ignored) {
        }

        appendArray(
                "used_codes",
                item
        );
    }

    private void saveString(
            String key,
            String value
    ) {

        prefs.edit()
                .putString(
                        key,
                        value
                )
                .apply();
    }

    /* =========================================================
       COLORS / LAYOUT
       ========================================================= */

    private int backgroundColor() {

        return darkMode
                ? Color.rgb(
                        4,
                        27,
                        20
                )
                : LIGHT;
    }

    private int textColor() {

        return darkMode
                ? WHITE
                : BLACK;
    }

    private LinearLayout.LayoutParams lp(
            int width,
            int height,
            int left,
            int top,
            int right,
            int bottom
    ) {

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        width,
                        height
                );

        p.setMargins(
                dp(left),
                dp(top),
                dp(right),
                dp(bottom)
        );

        return p;
    }

    private int dp(
            int value
    ) {

        return (int)(
                value *
                getResources()
                        .getDisplayMetrics()
                        .density
        );
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
                java.util.Locale.US,
                "%02d:%02d",
                minutes,
                seconds
        );
    }

    private void toast(
            String message
    ) {

        Toast.makeText(
                this,
                message,
                Toast.LENGTH_SHORT
        ).show();
    }

    /* =========================================================
       BACK BUTTON
       ========================================================= */

    @Override
    public void onBackPressed() {

        if (examTimer != null) {

            toast(
                    "لا يمكن الخروج أثناء الامتحان"
            );

            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {

        if (examTimer != null) {

            examTimer.cancel();

            examTimer = null;
        }

        getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_SECURE
        );

        super.onDestroy();
    }
}
