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
