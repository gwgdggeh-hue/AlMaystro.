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

    private final int GOLD = Color.rgb(224, 190, 70);
    private final int DARK_GREEN = Color.rgb(5, 38, 27);
    private final int GREEN = Color.rgb(14, 70, 48);
    private final int LIGHT_GREEN = Color.rgb(25, 92, 65);

    private int dp(int value) {
        return (int) (value * getResources()
                .getDisplayMetrics().density + 0.5f);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showWelcome();
    }

    private int bgColor() {
        return darkMode
                ? DARK_GREEN
                : Color.rgb(245, 242, 232);
    }

    private int cardColor() {
        return darkMode
                ? GREEN
                : Color.WHITE;
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

    private TextView card(
            String title,
            String description) {

        LinearLayout box =
                new LinearLayout(this);

        box.setOrientation(
                LinearLayout.VERTICAL
        );

        box.setGravity(Gravity.CENTER);

        box.setPadding(
                dp(15),
                dp(12),
                dp(15),
                dp(12)
        );

        GradientDrawable bg =
                new GradientDrawable();

        bg.setColor(cardColor());
        bg.setCornerRadius(dp(20));
        bg.setStroke(dp(1), GOLD);

        box.setBackground(bg);

        TextView titleView =
                text(
                        title,
                        21,
                        GOLD
                );

        TextView descView =
                text(
                        description,
                        15,
                        secondaryTextColor()
                );

        box.addView(titleView);
        box.addView(descView);

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        p.setMargins(
                dp(10),
                dp(7),
                dp(10),
                dp(7)
        );

        box.setLayoutParams(p);

        return createClickableCard(box);
    }

    private TextView createClickableCard(
            LinearLayout box) {

        TextView result = new TextView(this);

        result.setText("");
        result.setVisibility(View.GONE);

        box.setTag(result);

        return result;
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

        if (subtitle != null) {
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

    private EditText input(
            String hint) {

        EditText e =
                new EditText(this);

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

            showStudentHome(n);
        });

        content.addView(login);

        TextView back =
                button("رجوع");

        back.setOnClickListener(
                v -> showRoles()
        );

        content.addView(back);
    }

    private void showStudentHome(
            String studentName) {

        prepareScreen();

        addHeader(
                "أهلًا يا " + studentName,
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
                        "تابع امتحاناتك ومذكراتك وأذكارك من مكان واحد",
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
                        "المراجعة المستمرة أفضل طريق لتثبيت المعلومات.",
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
                        "لا توجد امتحانات مسجلة حاليًا",
                        18,
                        secondaryTextColor()
                )
        );

        content.addView(
                text(
                        "بعد ربط Firebase ستظهر هنا الامتحانات والدرجات والتصحيح.",
                        15,
                        secondaryTextColor()
                )
        );

        TextView back =
                button("رجوع");

        back.setOnClickListener(
                v -> showStudentHome("الطالب")
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
                button("رجوع");

        back.setOnClickListener(
                v -> showStudentHome("الطالب")
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
                button("رجوع");

        back.setOnClickListener(
                v -> showStudentHome("الطالب")
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
                        "التطبيق سيستخدم Firebase لحفظ بيانات الامتحانات والنتائج بطريقة منظمة وآمنة.",
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
                                + "المايسترو شريف هيبه\n\n"
                                + "مع المبرمج او المطور محمود كليب\n"
                                + "للتواصل 01112244710",
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

        bar.setGravity(Gravity.CENTER);

        GradientDrawable bg =
                new GradientDrawable();

        bg.setColor(cardColor());
        bg.setCornerRadius(dp(22))
