package com.almaystro.app;

import android.app.AlertDialog;
import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends Activity {

    private LinearLayout content;
    private SharedPreferences prefs;
    private CountDownTimer examTimer;

    private boolean darkMode = true;
    private boolean examRunning = false;

    private String studentName = "";
    private String teacherName = "";
    private String activeExamId = "";
    private String activeCode = "";

    private JSONArray activeQuestions = new JSONArray();
    private int currentQuestion = 0;
    private int remainingSeconds = 0;

    private final int GOLD = Color.rgb(224, 190, 70);
    private final int GREEN = Color.rgb(14, 70, 48);
    private final int DARK = Color.rgb(5, 38, 27);
    private final int WHITE = Color.WHITE;
    private final int BLACK = Color.rgb(30, 30, 30);
    private final int GRAY = Color.rgb(150, 150, 150);

    private static final String PREFS = "almaystro_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        darkMode = prefs.getBoolean("dark_mode", true);

        seedData();
        applyBrightness();
        showWelcome();
    }

    // =========================================================
    // BASIC HELPERS
    // =========================================================

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private int foreground() {
        return darkMode ? WHITE : BLACK;
    }

    private int secondary() {
        return darkMode ? Color.rgb(210, 210, 210) : Color.rgb(90, 90, 90);
    }

    private int surface() {
        return darkMode ? Color.rgb(18, 55, 42) : Color.WHITE;
    }

    private GradientDrawable background() {
        GradientDrawable g = new GradientDrawable();
        g.setColor(darkMode ? DARK : Color.rgb(245, 242, 234));
        return g;
    }

    private GradientDrawable cardBackground() {
        GradientDrawable g = new GradientDrawable();
        g.setColor(surface());
        g.setCornerRadius(dp(18));
        g.setStroke(dp(1), GOLD);
        return g;
    }

    private TextView text(String value, float size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        t.setTypeface(Typeface.DEFAULT, bold ? Typeface.BOLD : Typeface.NORMAL);
        t.setPadding(dp(8), dp(8), dp(8), dp(8));
        return t;
    }

    private TextView button(String value) {
        TextView b = text(value, 16, WHITE, true);
        b.setGravity(Gravity.CENTER);

        GradientDrawable g = new GradientDrawable();
        g.setColor(GREEN);
        g.setCornerRadius(dp(18));
        g.setStroke(dp(1), GOLD);

        b.setBackground(g);

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(-1, dp(55));

        p.setMargins(dp(8), dp(5), dp(8), dp(5));
        b.setLayoutParams(p);

        return b;
    }

    private EditText input(String hint) {
        EditText e = new EditText(this);

        e.setHint(hint);
        e.setTextSize(16);
        e.setTextColor(foreground());
        e.setHintTextColor(secondary());
        e.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        e.setSingleLine(true);
        e.setPadding(dp(15), dp(5), dp(15), dp(5));

        GradientDrawable g = new GradientDrawable();
        g.setColor(surface());
        g.setCornerRadius(dp(15));
        g.setStroke(dp(1), GOLD);

        e.setBackground(g);

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(-1, dp(54));

        p.setMargins(dp(8), dp(5), dp(8), dp(5));
        e.setLayoutParams(p);

        return e;
    }

    private void base() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(10), dp(10), dp(10), dp(10));
        root.setBackground(background());
        root.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.addView(root);

        content = root;
        setContentView(scroll);
    }

    private void header(String title, String subtitle) {
        content.addView(text("✦ ❖ ✦", 20, GOLD, true));
        content.addView(text(title, 27, GOLD, true));

        if (subtitle != null && !subtitle.isEmpty()) {
            content.addView(text(subtitle, 15, secondary(), false));
        }

        content.addView(text("❖", 18, GOLD, true));
    }

    private void addButton(String value, View.OnClickListener listener) {
        TextView b = button(value);
        b.setOnClickListener(listener);
        content.addView(b);
    }

    private void addCard(String value) {
        TextView t = text(value, 16, foreground(), false);
        t.setBackground(cardBackground());

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(-1, -2);

        p.setMargins(dp(5), dp(6), dp(5), dp(6));
        content.addView(t, p);
    }

    private void back(View.OnClickListener listener) {
        addButton("↩ رجوع", listener);
    }

    private void toast(String value) {
        Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
    }

    private int number(String value, int fallback) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return fallback;
        }
    }

    // =========================================================
    // WELCOME
    // =========================================================

    private void showWelcome() {
        base();

        ImageView image = new ImageView(this);

        try {
            image.setImageResource(R.drawable.maestro);
        } catch (Exception ignored) {
        }

        image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        content.addView(
                image,
                new LinearLayout.LayoutParams(-1, dp(190))
        );

        content.addView(text("المايسترو", 38, GOLD, true));
        content.addView(text("المايسترو شريف هيبه", 21, foreground(), true));
        content.addView(text("هتتعلم التاريخ ببساطة", 18, secondary(), false));
        content.addView(text("✦ منصة الامتحانات والمراجعة ✦", 15, GOLD, true));

        addButton("🚀 ابدأ الآن", v -> roles());

        content.addView(
                text(
                        "طالب • مدرس • امتحانات • نتائج • مذكرات • أذكار",
                        14,
                        secondary(),
                        false
                )
        );
    }

    private void roles() {
        base();
        header("مرحبًا بك في المايسترو", "اختر نوع الدخول");

        addButton("👨‍🎓 أنا طالب", v -> studentLogin());
        addButton("👨‍🏫 أنا مدرس", v -> teacherLogin());
        addButton("ℹ️ عن التطبيق", v -> about());
    }

    // =========================================================
    // STUDENT LOGIN
    // =========================================================

    private void studentLogin() {
        base();
        header("دخول الطالب", "اكتب الاسم وكود الامتحان");

        EditText name = input("اسم الطالب");
        EditText code = input("كود الامتحان");

        content.addView(name);
        content.addView(code);

        content.addView(
                text(
                        "للتجربة: DEMO123",
                        14,
                        GOLD,
                        true
                )
        );

        addButton("📝 دخول الامتحان", v -> {

            String n = name.getText().toString().trim();
            String c = code.getText().toString().trim().toUpperCase(Locale.ROOT);

            if (n.isEmpty()) {
                name.setError("اكتب اسم الطالب");
                return;
            }

            if (c.isEmpty()) {
                code.setError("اكتب كود الامتحان");
                return;
            }

            JSONObject exam = findExamByCode(c);

            if (exam == null) {
                code.setError("الكود غير موجود");
                return;
            }

            String usedKey = n.toLowerCase(Locale.ROOT) + "|" + c;

            if (isCodeUsed(usedKey)) {
                code.setError("هذا الكود استُخدم بالفعل لهذا الطالب");
                return;
            }

            studentName = n;
            activeCode = c;
            activeExamId = exam.optString("id");

            startExam(exam);
        });

        back(v -> roles());
    }

    // =========================================================
    // STUDENT HOME
    // =========================================================

    private void studentHome() {
        base();
        header(
                "أهلًا يا " +
                        (studentName.isEmpty() ? "طالب" : studentName),
                "لوحة الطالب"
        );

        addCard(
                "🎓 مرحبًا بك في المايسترو\n" +
                        "منصة الامتحانات والمراجعة التعليمية"
        );

        addButton("📝 دخول امتحان", v -> studentLogin());
        addButton("📊 امتحاناتي ونتائجي", v -> studentResults());
        addButton("📚 مذكرات الشرح", v -> studentNotes());
        addButton("🤲 الأذكار", v -> azkar());
        addButton("🏆 تقدمي وإنجازاتي", v -> progress());
        addButton("🔔 التحديثات", v -> updates());
        addButton("⚙️ الإعدادات", v -> studentSettings());
        addButton("ℹ️ عن التطبيق", v -> about());

        studentNavigation();
    }

    // =========================================================
    // STUDENT RESULTS
    // =========================================================

    private void studentResults() {
        base();
        header("امتحاناتي", studentName);

        JSONArray results = array("results");
        boolean found = false;

        for (int i = 0; i < results.length(); i++) {
            try {
                JSONObject r = results.getJSONObject(i);

                if (!studentName.equals(r.optString("student"))) {
                    continue;
                }

                found = true;

                int total = r.optInt("total");
                int correct = r.optInt("correct");
                int answered = r.optInt("answered");

                String mistakes = r.optString("mistakes", "");

                StringBuilder s = new StringBuilder();

                s.append("📝 ")
                        .append(r.optString("exam"))
                        .append("\n");

                s.append("✅ الصحيح: ")
                        .append(correct)
                        .append("/")
                        .append(total)
                        .append("\n");

                s.append("✍️ تمت الإجابة: ")
                        .append(answered)
                        .append("\n");

                s.append("⬜ بدون إجابة: ")
                        .append(total - answered)
                        .append("\n");

                if (!mistakes.isEmpty()) {
                    s.append("\n❌ أخطاء للمراجعة:\n")
                            .append(mistakes);
                } else {
                    s.append("\n🌟 لا توجد أخطاء.");
                }

                addCard(s.toString());

            } catch (Exception ignored) {
            }
        }

        if (!found) {
            addCard("لا توجد نتائج لهذا الطالب حتى الآن.");
        }

        back(v -> studentHome());
        studentNavigation();
    }

    // =========================================================
    // NOTES
    // =========================================================

    private void studentNotes() {
        base();
        header("مذكرات الشرح", "المذكرات المتاحة");

        JSONArray notes = array("notes");

        if (notes.length() == 0) {
            addCard("لا توجد مذكرات حاليًا.");
        }

        for (int i = 0; i < notes.length(); i++) {
            try {
                JSONObject n = notes.getJSONObject(i);

                addCard(
                        "📖 " +
                                n.optString("title") +
                                "\n\n" +
                                n.optString("body")
                );

            } catch (Exception ignored) {
            }
        }

        back(v -> studentHome());
        studentNavigation();
    }

    // =========================================================
    // AZKAR
    // =========================================================

    private void azkar() {
        base();
        header("الأذكار", "ذكر الله وطمأنينة القلب");

        addCard(
                "☀️ أذكار الصباح\n\n" +
                        "آية الكرسي\n" +
                        "الإخلاص والفلق والناس\n" +
                        "أصبحنا وأصبح الملك لله\n" +
                        "سبحان الله وبحمده\n" +
                        "أستغفر الله وأتوب إليه"
        );

        addCard(
                "🌙 أذكار المساء\n\n" +
                        "آية الكرسي\n" +
                        "الإخلاص والفلق والناس\n" +
                        "أمسينا وأمسى الملك لله\n" +
                        "سبحان الله وبحمده\n" +
                        "أستغفر الله وأتوب إليه"
        );

        addCard(
                "📿 ورد الذكر\n\n" +
                        "سبحان الله\n" +
                        "الحمد لله\n" +
                        "الله أكبر\n" +
                        "لا إله إلا الله\n" +
                        "أستغفر الله"
        );

        addButton("📿 عداد الذكر", v -> dhikrCounter());

        back(v -> studentHome());
        studentNavigation();
    }

    private void dhikrCounter() {
        final int[] count = {0};

        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(20), dp(20), dp(20), dp(20));

        TextView number = text("0", 42, GOLD, true);
        number.setGravity(Gravity.CENTER);

        TextView plus = button("📿 اضغط للذكر");

        plus.setOnClickListener(v -> {
            count[0]++;
            number.setText(String.valueOf(count[0]));
        });

        box.addView(number);
        box.addView(plus);

        new AlertDialog.Builder(this)
                .setTitle("عداد الذكر")
                .setView(box)
                .setPositiveButton("إغلاق", null)
                .show();
    }

    // =========================================================
    // PROGRESS
    // =========================================================

    private void progress() {
        base();
        header("تقدمي وإنجازاتي", studentName);

        JSONArray results = array("results");

        int exams = 0;
        int correct = 0;
        int total = 0;

        for (int i = 0; i < results.length(); i++) {
            try {
                JSONObject r = results.getJSONObject(i);

                if (studentName.equals(r.optString("student"))) {
                    exams++;
                    correct += r.optInt("correct");
                    total += r.optInt("total");
                }

            } catch (Exception ignored) {
            }
        }

        addCard(
                "🏆 الامتحانات المنجزة: " + exams +
                        "\n\n✅ الإجابات الصحيحة: " + correct +
                        "\n\n📚 إجمالي الأسئلة: " + total
        );

        addCard(
                exams == 0
                        ? "ابدأ أول امتحان لتسجيل أول إنجاز لك."
                        : "🌟 استمر في المراجعة والتدريب."
        );

        back(v -> studentHome());
        studentNavigation();
    }

    // =========================================================
    // UPDATES
    // =========================================================

    private void updates() {
        base();
        header("التحديثات", "آخر أخبار المايسترو");

        addCard(
                prefs.getString(
                        "update",
                        "🔔 لا توجد تحديثات جديدة حاليًا."
                )
        );

        back(v -> studentHome());
        studentNavigation();
    }

    // =========================================================
    // STUDENT SETTINGS
    // =========================================================

    private void studentSettings() {
        base();
        header("الإعدادات", "تخصيص التطبيق");

        addCard(
                "👤 الطالب\n" +
                        (studentName.isEmpty()
                                ? "غير مسجل"
                                : studentName)
        );

        addButton(
                darkMode
                        ? "☀️ تفعيل الوضع النهاري"
                        : "🌙 تفعيل الوضع الليلي",
                v -> {
                    darkMode = !darkMode;

                    prefs.edit()
                            .putBoolean("dark_mode", darkMode)
                            .apply();

                    studentSettings();
                }
        );

        addButton("🔆 التحكم في السطوع", v -> brightnessDialog());

        addButton("🔐 الخصوصية والأمان", v -> privacy());

        addButton("ℹ️ عن التطبيق", v -> about());

        addButton("🚪 تسجيل الخروج", v -> {
            studentName = "";
            activeCode = "";
            activeExamId = "";
            roles();
        });

        back(v -> studentHome());
    }

    private void brightnessDialog() {
        SeekBar seek = new SeekBar(this);
        seek.setMax(100);

        int saved = prefs.getInt("brightness", 100);
        seek.setProgress(saved);

        seek.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(
                            SeekBar bar,
                            int progress,
                            boolean fromUser
                    ) {
                        float value =
                                Math.max(0.05f, progress / 100f);

                        WindowManager.LayoutParams p =
                                getWindow().getAttributes();

                        p.screenBrightness = value;
                        getWindow().setAttributes(p);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar bar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar bar) {
                    }
                }
        );

        new AlertDialog.Builder(this)
                .setTitle("سطوع التطبيق")
                .setView(seek)
                .setPositiveButton(
                        "حفظ",
                        (d, w) ->
                                prefs.edit()
                                        .putInt(
                                                "brightness",
                                                seek.getProgress()
                                       
