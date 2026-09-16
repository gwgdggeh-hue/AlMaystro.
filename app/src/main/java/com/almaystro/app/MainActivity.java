package com.almaystro.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private LinearLayout content;
    private boolean darkMode = true;
    private String currentStudentName = "الطالب";

    private final int GOLD = Color.rgb(224, 190, 70);
    private final int DARK_GREEN = Color.rgb(5, 38, 27);
    private final int GREEN = Color.rgb(14, 70, 48);

    private int dp(int value) {
        return (int) (value *
                getResources()
                        .getDisplayMetrics()
                        .density + 0.5f);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showWelcome();
    }

    private int cardColor() {
        return darkMode ? GREEN : Color.WHITE;
    }

    private int mainTextColor() {
        return darkMode
                ? Color.WHITE
                : Color.rgb(35, 35, 35);
    }

    private int secondaryTextColor() {
        return darkMode
                ? Color.LTGRAY
                : Color.rgb(90, 90, 90);
    }

    private GradientDrawable background() {

        return new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,

                darkMode
                        ? new int[]{
                        DARK_GREEN,
                        GREEN,
                        DARK_GREEN
                }

                        : new int[]{
                        Color.rgb(245, 242, 232),
                        Color.WHITE,
                        Color.rgb(235, 232, 220)
                }
        );
    }

    private TextView text(
            String value,
            float size,
            int color) {

        TextView t = new TextView(this);

        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setGravity(Gravity.CENTER);

        t.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        t.setIncludeFontPadding(true);

        t.setPadding(
                dp(8),
                dp(6),
                dp(8),
                dp(6)
        );

        return t;
    }

    private TextView button(String value) {

        TextView b = new TextView(this);

        b.setText(value);
        b.setTextSize(18);

        b.setTextColor(
                darkMode
                        ? DARK_GREEN
                        : Color.rgb(20, 60, 45)
        );

        b.setGravity(Gravity.CENTER);

        b.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        b.setPadding(
                dp(8),
                dp(5),
                dp(8),
                dp(5)
        );

        b.setClickable(true);
        b.setFocusable(true);

        GradientDrawable bg =
                new GradientDrawable();

        bg.setColor(GOLD);
        bg.setCornerRadius(dp(30));

        b.setBackground(bg);

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dp(62)
                );

        p.setMargins(
                dp(22),
                dp(6),
                dp(22),
                dp(6)
        );

        b.setLayoutParams(p);

        return b;
    }

    private EditText input(String hint) {

        EditText e = new EditText(this);

        e.setHint(hint);
        e.setTextSize(17);

        e.setTextColor(
                mainTextColor()
        );

        e.setHintTextColor(
                secondaryTextColor()
        );

        e.setGravity(Gravity.CENTER);
        e.setSingleLine(true);

        GradientDrawable bg =
                new GradientDrawable();

        bg.setColor(cardColor());
        bg.setCornerRadius(dp(18));
        bg.setStroke(dp(1), GOLD);

        e.setBackground(bg);

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dp(58)
                );

        p.setMargins(
                dp(18),
                dp(6),
                dp(18),
                dp(6)
        );

        e.setLayoutParams(p);

        return e;
    }

    private void prepareScreen() {

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setGravity(
                Gravity.CENTER_HORIZONTAL
        );

        root.setPadding(
                dp(18),
                dp(18),
                dp(18),
                dp(18)
        );

        root.setBackground(
                background()
        );

        ScrollView scroll =
                new ScrollView(this);

        scroll.setFillViewport(true);

        scroll.setBackground(
                background()
        );

        content = root;

        scroll.addView(root);

        setContentView(scroll);
    }

    private void addHeader(
            String title,
            String subtitle) {

        content.addView(
                text(
                        "✦  ❖  ✦",
                        24,
                        GOLD
                )
        );

        content.addView(
                text(
                        title,
                        30,
                        GOLD
                )
        );

        if (subtitle != null &&
                !subtitle.isEmpty()) {

            content.addView(
                    text(
                            subtitle,
                            17,
                            secondaryTextColor()
                    )
            );
        }

        content.addView(
                text(
                        "❖",
                        23,
                        GOLD
                )
        );
    }

    private void showWelcome() {

        prepareScreen();

        ImageView logo =
                new ImageView(this);

        logo.setImageResource(
                R.drawable.maestro
        );

        logo.setScaleType(
                ImageView.ScaleType.CENTER_INSIDE
        );

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dp(190)
                );

        content.addView(logo, p);

        content.addView(
                text(
                        "المايسترو",
                        38,
                        GOLD
                )
        );

        content.addView(
                text(
                        "المايسترو شريف هيبه",
                        21,
                        mainTextColor()
                )
        );

        content.addView(
                text(
                        "هتتعلم التاريخ ببساطة",
                        18,
                        secondaryTextColor()
                )
        );

        content.addView(
                text(
                        "✦  ❖  ✦",
                        23,
                        GOLD
                )
        );

        TextView start =
                button("ابدأ الآن");

        start.setOnClickListener(
                v -> showRoles()
        );

        content.addView(start);

        content.addView(
                text(
                        "منصة تعليمية للامتحانات والمراجعة والتطوير",
                        14,
                        secondaryTextColor()
                )
        );
    }

    private void showRoles() {

        prepareScreen();

        addHeader(
                "اختيار نوع الدخول",
                "اختر طريقة الدخول إلى المايسترو"
        );

        TextView student =
                button("👨‍🎓  أنا طالب");

        student.setOnClickListener(
                v -> showStudentLogin()
        );

        content.addView(student);

        TextView teacher =
                button("👨‍🏫  أنا مدرس");

        teacher.setOnClickListener(
                v -> showTeacherLogin()
        );

        content.addView(teacher);

        TextView back =
                button("رجوع");

        back.setOnClickListener(
                v -> showWelcome()
        );

        content.addView(back);
    }

    private void showStudentLogin() {

        prepareScreen();

        addHeader(
                "دخول الطالب",
                "اكتب اسمك وكود الامتحان"
        );

        EditText name =
                input("اكتب اسم الطالب");

        EditText code =
                input("اكتب كود الامتحان");

        content.addView(name);
        content.addView(code);

        TextView login =
                button("دخول الطالب");

        login.setOnClickListener(v -> {

            String n =
                    name.getText()
                            .toString()
                            .trim();

            String c =
                    code.getText()
                            .toString()
                            .trim();

            if (n.isEmpty()) {

                name.setError(
                        "اكتب اسم الطالب"
                );

                return;
            }

            if (c.isEmpty()) {

                code.setError(
                        "اكتب كود الامتحان"
                );

                return;
            }

            currentStudentName = n;

            showStudentHome();
        });

        content.addView(login);

        TextView back =
                button("رجوع");

        back.setOnClickListener(
                v -> showRoles()
        );

        content.addView(back);
    }

    private void showStudentHome() {

        prepareScreen();

        addHeader(
                "أهلًا يا " + currentStudentName,
                "نتمنى لك التوفيق والنجاح 🌟"
        );

        content.addView(
                text(
                        "رحلتك التعليمية",
                        24,
                        GOLD
                )
        );

        content.addView(
                text(
                        "كل أدواتك التعليمية في مكان واحد",
                        15,
                        secondaryTextColor()
                )
        );

        TextView exam =
                button("📝  دخول امتحان");

        exam.setOnClickListener(
                v -> showStudentLogin()
        );

        content.addView(exam);

        TextView myExams =
                button("📊  امتحاناتي");

        myExams.setOnClickListener(
                v -> showMyExams()
        );

        content.addView(myExams);

        TextView notes =
                button("📚  مذكرات الشرح");

        notes.setOnClickListener(
                v -> showNotes()
        );

        content.addView(notes);

        TextView azkar =
                button("🤲  الأذكار");

        azkar.setOnClickListener(
                v -> showAzkar()
        );

        content.addView(azkar);

        content.addView(
                text(
                        "💡 فائدة اليوم",
                        23,
                        GOLD
                )
        );

        content.addView(
                text(
                        "المراجعة المستمرة تساعد على تثبيت المعلومات.",
                        16,
                        mainTextColor()
                )
        );

        TextView settings =
                button("⚙️  الإعدادات");

        settings.setOnClickListener(
                v -> showSettings()
        );

        content.addView(settings);

        TextView logout =
                button("تسجيل خروج");

        logout.setOnClickListener(
                v -> showRoles()
        );

        content.addView(logout);

        addBottomBar();
    }

    private void showMyExams() {

        prepareScreen();

        addHeader(
                "امتحاناتي",
                "سجل الامتحانات والنتائج"
        );

        content.addView(
                text(
                        "📊 سجل الامتحانات",
                        23,
                        GOLD
                )
        );

        content.addView(
                text(
                        "لا توجد امتحانات مسجلة حاليًا.",
                        18,
                        mainTextColor()
                )
        );

        content.addView(
                text(
                        "بعد ربط Firebase ستظهر هنا الامتحانات والدرجات والتصحيح والأخطاء مع شرحها.",
                        15,
                        secondaryTextColor()
                )
        );

        TextView back =
                button("رجوع للرئيسية");

        back.setOnClickListener(
                v -> showStudentHome()
        );

        content.addView(back);

        addBottomBar();
    }

    private void showNotes() {

        prepareScreen();

        addHeader(
                "مذكرات الشرح",
                "مراجعة التاريخ بطريقة بسيطة"
        );

        content.addView(
                text(
                        "📖 المذكرات التعليمية",
                        23,
                        GOLD
                )
        );

        content.addView(
                text(
                        "سيتم تنظيم المذكرات حسب الصف والمادة والفصل الدراسي.",
                        16,
                        mainTextColor()
                )
        );

        content.addView(
                text(
                        "📌 مذكرات جديدة\n\n"
                                + "⭐ مراجعات مهمة\n\n"
                                + "🧠 أسئلة للمراجعة",
                        17,
                        secondaryTextColor()
                )
        );

        TextView back =
                button("رجوع للرئيسية");

        back.setOnClickListener(
                v -> showStudentHome()
        );

        content.addView(back);

        addBottomBar();
    }

    private void showAzkar() {

        prepareScreen();

        addHeader(
                "الأذكار",
                "اذكر الله واطمئن قلبك 🤍"
        );

        content.addView(
                text(
                        "☀️ أذكار الصباح",
                        23,
                        GOLD
                )
        );

        content.addView(
                text(
                        "سبحان الله وبحمده\n"
                                + "سبحان الله العظيم\n"
                                + "أستغفر الله وأتوب إليه",
                        18,
                        mainTextColor()
                )
        );

        content.addView(
                text(
                        "🌙 أذكار المساء",
                        23,
                        GOLD
                )
        );

        content.addView(
                text(
                        "قراءة الأذكار يوميًا تساعد على تذكر الله والمحافظة على الورد.",
                        17,
                        secondaryTextColor()
                )
        );

        TextView back =
                button("رجوع للرئيسية");

        back.setOnClickListener(
                v -> showStudentHome()
        );

        content.addView(back);

        addBottomBar();
    }

    private void showSettings() {

        prepareScreen();

        addHeader(
                "الإعدادات",
                "تحكم في تجربة استخدام التطبيق"
        );

        TextView mode =
                button(
                        darkMode
                                ? "☀️  تفعيل الوضع الفاتح"
                                : "🌙  تفعيل الوضع الداكن"
                );

        mode.setOnClickListener(v -> {

            darkMode = !darkMode;

            showSettings();
        });

        content.addView(mode);

        TextView brightness =
                button("🔆  السطوع");

        brightness.setOnClickListener(
                v -> Toast.makeText(
                        this,
                        "إعداد السطوع سيتم تطويره لاحقًا",
                        Toast.LENGTH_SHORT
                ).show()
        );

        content.addView(brightness);

        TextView notifications =
                button("🔔  الإشعارات");

        notifications.setOnClickListener(
                v -> Toast.makeText(
                        this,
                        "إعداد الإشعارات سيتم تفعيله لاحقًا",
                        Toast.LENGTH_SHORT
                ).show()
        );

        content.addView(notifications);

        content.addView(
                text(
                        "🔒 الخصوصية والأمان",
                        21,
                        GOLD
                )
        );

        content.addView(
                text(
                        "سيتم استخدام Firebase لاحقًا لحفظ بيانات الامتحانات والنتائج بطريقة منظمة.",
                        15,
                        secondaryTextColor()
                )
        );

        content.addView(
                text(
                        "عن التطبيق",
                        21,
                        GOLD
                )
        );

        content.addView(
                text(
                        "المايسترو\n"
                                + "المايسترو شريف هيبه",
                        15,
                        mainTextColor()
                )
        );

        TextView back =
                button("رجوع");

        back.setOnClickListener(
                v -> showWelcome()
        );

        content.addView(back);
    }

    private void addBottomBar() {

        LinearLayout bar =
                new LinearLayout(this);

        bar.setOrientation(
                LinearLayout.HORIZONTAL
        );

        bar.setGravity(
                Gravity.CENTER
        );

        GradientDrawable bg =
                new GradientDrawable();

        bg.setColor(cardColor());
        bg.setCornerRadius(dp(22));
        bg.setStroke(dp(1), GOLD);

        bar.setBackground(bg);

        TextView home =
                navButton("الرئيسية");

        home.setOnClickListener(
                v -> showStudentHome()
        );

        TextView exams =
                navButton("امتحاناتي");

        exams.setOnClickListener(
                v -> showMyExams()
        );

        TextView azkar =
                navButton("الأذكار");

        azkar.setOnClickListener(
                v -> showAzkar()
        );

        TextView notes =
                navButton("المذكرات");

        notes.setOnClickListener(
                v -> showNotes()
        );

        TextView settings =
                navButton("الإعدادات");

        settings.setOnClickListener(
                v -> showSettings()
        );

        bar.addView(home);
        bar.addView(exams);
        bar.addView(azkar);
        bar.addView(notes);
        bar.addView(settings);

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dp(72)
                );

        p.setMargins(
                dp(4),
                dp(20),
                dp(4),
                dp(8)
        );

        content.addView(bar, p);
    }

    private TextView navButton(String value) {

        TextView t =
                new TextView(this);

        t.setText(value);
        t.setTextSize(12);
        t.setTextColor(GOLD);
        t.setGravity(Gravity.CENTER);

        t.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        0,
                        Line
