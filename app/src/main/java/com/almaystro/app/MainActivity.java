package com.almaystro.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.content.SharedPreferences;
import android.text.InputType;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Locale;

public class MainActivity extends Activity {

    // =========================================================
    // VARIABLES
    // =========================================================

    private LinearLayout root;
    private LinearLayout content;
    private LinearLayout bottomBar;

    private SharedPreferences prefs;

    private CountDownTimer examTimer;

    private boolean darkMode = true;
    private boolean examRunning = false;

    private String currentStudent = "";
    private String currentTeacher = "";
    private String currentExamId = "";
    private String currentExamCode = "";

    private static final int GOLD = Color.rgb(224, 190, 70);
    private static final int DARK_GREEN = Color.rgb(5, 38, 27);
    private static final int GREEN = Color.rgb(14, 70, 48);
    private static final int LIGHT_BG = Color.rgb(246, 242, 232);
    private static final int WHITE = Color.WHITE;
    private static final int BLACK = Color.rgb(30, 30, 30);
    private static final int GRAY = Color.rgb(120, 120, 120);

    private static final String PREFS = "almaystro_data";

    // =========================================================
    // CREATE
    // =========================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences(PREFS, MODE_PRIVATE);

        darkMode = prefs.getBoolean("dark_mode", true);

        applyBrightness();

        showWelcome();
    }

    // =========================================================
    // BASIC HELPERS
    // =========================================================

    private int dp(int value) {
        return (int) (value *
                getResources().getDisplayMetrics().density + 0.5f);
    }

    private int foreground() {
        return darkMode ? WHITE : BLACK;
    }

    private int secondaryText() {
        return darkMode ? Color.rgb(205, 205, 205) : GRAY;
    }

    private int cardColor() {
        return darkMode ? Color.rgb(18, 58, 43) : WHITE;
    }

    private GradientDrawable roundedBackground(
            int color,
            int strokeColor,
            int radius
    ) {
        GradientDrawable bg = new GradientDrawable();

        bg.setColor(color);
        bg.setCornerRadius(dp(radius));

        if (strokeColor != Color.TRANSPARENT) {
            bg.setStroke(dp(1), strokeColor);
        }

        return bg;
    }

    private LinearLayout verticalLayout() {

        LinearLayout layout = new LinearLayout(this);

        layout.setOrientation(LinearLayout.VERTICAL);

        return layout;
    }

    private LinearLayout horizontalLayout() {

        LinearLayout layout = new LinearLayout(this);

        layout.setOrientation(LinearLayout.HORIZONTAL);

        return layout;
    }

    private LinearLayout.LayoutParams margins(
            int width,
            int height,
            int left,
            int top,
            int right,
            int bottom
    ) {

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(width, height);

        p.setMargins(
                dp(left),
                dp(top),
                dp(right),
                dp(bottom)
        );

        return p;
    }

    // =========================================================
    // TEXT
    // =========================================================

    private TextView text(
            String value,
            float size,
            int color,
            boolean bold
    ) {

        TextView t = new TextView(this);

        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);

        t.setGravity(
                Gravity.CENTER
        );

        t.setTypeface(
                Typeface.DEFAULT,
                bold ? Typeface.BOLD : Typeface.NORMAL
        );

        t.setPadding(
                dp(8),
                dp(8),
                dp(8),
                dp(8)
        );

        return t;
    }

    private TextView rightText(
            String value,
            float size,
            int color,
            boolean bold
    ) {

        TextView t = text(value, size, color, bold);

        t.setGravity(
                Gravity.RIGHT |
                Gravity.CENTER_VERTICAL
        );

        return t;
    }

    // =========================================================
    // BUTTON
    // =========================================================

    private TextView button(String value) {

        TextView b =
                text(
                        value,
                        16,
                        WHITE,
                        true
                );

        b.setGravity(Gravity.CENTER);

        b.setBackground(
                roundedBackground(
                        GREEN,
                        GOLD,
                        20
                )
        );

        b.setPadding(
                dp(10),
                dp(10),
                dp(10),
                dp(10)
        );

        b.setMinHeight(dp(55));

        return b;
    }

    private TextView goldButton(String value) {

        TextView b =
                text(
                        value,
                        16,
                        DARK_GREEN,
                        true
                );

        b.setGravity(Gravity.CENTER);

        b.setBackground(
                roundedBackground(
                        GOLD,
                        Color.TRANSPARENT,
                        20
                )
        );

        b.setPadding(
                dp(10),
                dp(10),
                dp(10),
                dp(10)
        );

        b.setMinHeight(dp(55));

        return b;
    }

    private TextView secondaryButton(String value) {

        TextView b =
                text(
                        value,
                        15,
                        darkMode ? WHITE : GREEN,
                        true
                );

        b.setGravity(Gravity.CENTER);

        b.setBackground(
                roundedBackground(
                        cardColor(),
                        GOLD,
                        18
                )
        );

        b.setMinHeight(dp(50));

        return b;
    }

    private EditText input(String hint) {

        EditText e = new EditText(this);

        e.setHint(hint);
        e.setTextSize(16);

        e.setTextColor(foreground());
        e.setHintTextColor(secondaryText());

        e.setGravity(
                Gravity.RIGHT |
                Gravity.CENTER_VERTICAL
        );

        e.setSingleLine(true);

        e.setPadding(
                dp(15),
                dp(5),
                dp(15),
                dp(5)
        );

        e.setBackground(
                roundedBackground(
                        cardColor(),
                        GOLD,
                        16
                )
        );

        return e;
    }

    // =========================================================
    // ROOT
    // =========================================================

    private void createRoot() {

        root = verticalLayout();

        root.setBackground(
                darkMode
                        ? createDarkGradient()
                        : createLightGradient()
        );

        setContentView(root);
    }

    private GradientDrawable createDarkGradient() {

        GradientDrawable g =
                new GradientDrawable(
                        GradientDrawable.Orientation.TL_BR,
                        new int[]{
                                DARK_GREEN,
                                GREEN,
                                DARK_GREEN
                        }
                );

        return g;
    }

    private GradientDrawable createLightGradient() {

        GradientDrawable g =
                new GradientDrawable(
                        GradientDrawable.Orientation.TL_BR,
                        new int[]{
                                LIGHT_BG,
                                WHITE,
                                Color.rgb(235, 230, 215)
                        }
                );

        return g;
    }

    private void createScreen() {

        createRoot();

        ScrollView scroll = new ScrollView(this);

        scroll.setFillViewport(true);

        content = verticalLayout();

        content.setPadding(
                dp(12),
                dp(10),
                dp(12),
                dp(10)
        );

        scroll.addView(
                content,
                new ScrollView.LayoutParams(
                        -1,
                        -2
                )
        );

        root.addView(
                scroll,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );
    }

    // =========================================================
    // HEADER
    // =========================================================

    private void header(
            String title,
            String subtitle
    ) {

        TextView ornaments =
                text(
                        "✦   ❖   ✦",
                        20,
                        GOLD,
                        true
                );

        content.addView(ornaments);

        TextView titleView =
                text(
                        title,
                        27,
                        GOLD,
                        true
                );

        titleView.setPadding(
                dp(10),
                dp(15),
                dp(10),
                dp(5)
        );

        content.addView(titleView);

        if (subtitle != null &&
                !subtitle.trim().isEmpty()) {

            content.addView(
                    text(
                            subtitle,
                            15,
                            secondaryText(),
                            false
                    )
            );
        }

        content.addView(
                text(
                        "❖",
                        17,
                        GOLD,
                        true
                )
        );
    }

    // =========================================================
    // ADD BUTTON
    // =========================================================

    private void addButton(
            String value,
            android.view.View.OnClickListener listener
    ) {

        TextView b = button(value);

        b.setOnClickListener(listener);

        content.addView(
                b,
                margins(
                        -1,
                        58,
                        5,
                        6,
                        5,
                        6
                )
        );
    }

    private void addGoldButton(
            String value,
            android.view.View.OnClickListener listener
    ) {

        TextView b = goldButton(value);

        b.setOnClickListener(listener);

        content.addView(
                b,
                margins(
                        -1,
                        58,
                        5,
                        6,
                        5,
                        6
                )
        );
    }

    private void backButton(
            android.view.View.OnClickListener listener
    ) {

        addButton(
                "↩ رجوع",
                listener
        );
    }

    private void toast(String message) {

        Toast.makeText(
                this,
                message,
                Toast.LENGTH_SHORT
        ).show();
    }

    // =========================================================
    // JSON STORAGE
    // =========================================================

    private JSONArray getArray(String key) {

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

    private int number(
            String value,
            int fallback
    ) {

        try {

            int n =
                    Integer.parseInt(
                            value.trim()
                    );

            return n;

        } catch (Exception e) {

            return fallback;
        }
    }

    // =========================================================
    // WELCOME
    // =========================================================

    private void showWelcome() {

        examRunning = false;

        if (examTimer != null) {
            examTimer.cancel();
            examTimer = null;
        }

        createScreen();

        ImageView image =
                new ImageView(this);

        try {

            image.setImageResource(
                    R.drawable.maestro
            );

        } catch (Exception ignored) {
        }

        image.setScaleType(
                ImageView.ScaleType.CENTER_INSIDE
        );

        content.addView(
                image,
                margins(
                        -1,
                        190,
                        5,
                        5,
                        5,
                        5
                )
        );

        content.addView(
                text(
                        "المايسترو",
                        38,
                        GOLD,
                        true
                )
        );

        content.addView(
                text(
                        "المايسترو شريف هيبه",
                        21,
                        foreground(),
                        true
                )
        );

        content.addView(
                text(
                        "هتتعلم التاريخ ببساطة",
                        18,
                        secondaryText(),
                        false
                )
        );

        content.addView(
                text(
                        "✦ منصة الامتحانات والمراجعة ✦",
                        15,
                        GOLD,
                        true
                )
        );

        addGoldButton(
                "🚀 ابدأ الآن",
                v -> roles()
        );

        content.addView(
                text(
                        "طالب • مدرس • امتحانات • نتائج • مذكرات • أذكار",
                        13,
                        secondaryText(),
                        false
                )
        );
    }

    // =========================================================
    // ROLES
    // =========================================================

    private void roles() {

        createScreen();

        header(
                "مرحبًا بك في المايسترو",
                "اختر نوع الدخول"
        );

        addGoldButton(
                "👨‍🎓 أنا طالب",
                v -> studentLogin()
        );

        addButton(
                "👨‍🏫 أنا مدرس",
                v -> teacherLogin()
        );

        addButton(
                "ℹ️ عن التطبيق",
                v -> about()
        );
    }

    // =========================================================
    // STUDENT LOGIN
    // =========================================================

    private void studentLogin() {

        createScreen();

        header(
                "دخول الطالب",
                "اكتب اسمك وكود الامتحان"
        );

        EditText name =
                input("اسم الطالب");

        EditText code =
                input("كود الامتحان");

        content.addView(
                name,
                margins(
                        -1,
                        58,
                        5,
                        5,
                        5,
                        5
                )
        );

        content.addView(
                code,
                margins(
                        -1,
                        58,
                        5,
                        5,
                        5,
                        5
                )
        );

        addGoldButton(
                "📝 دخول وبدء الامتحان",
                v -> {

                    String studentName =
                            name.getText()
                                    .toString()
                                    .trim();

                    String examCode =
                            code.getText()
                                    .toString()
                                    .trim();

                    if (studentName.isEmpty()) {

                        name.setError(
                                "اكتب اسم الطالب"
                        );

                        return;
                    }

                    if (examCode.isEmpty()) {

                        code.setError(
                                "اكتب كود الامتحان"
                        );

                        return;
                    }

                    JSONObject exam =
                            findExamByCode(
                                    examCode
                            );

                    if (exam == null) {

                        code.setError(
                                "الكود غير موجود أو غير صالح"
                        );

                        return;
                    }

                    String useKey =
                            studentName
                                    .toLowerCase(Locale.ROOT)
                                    + "|"
                                    + examCode
                                    .toLowerCase(Locale.ROOT);

                    if (isCodeUsed(useKey)) {

                        code.setError(
                                "هذا الكود مستخدم بالفعل لهذا الطالب"
                        );

                        return;
                    }

                    currentStudent =
                            studentName;

                    currentExamCode =
                            examCode;

                    currentExamId =
                            exam.optString(
                                    "id"
                            );

                    markCodeUsed(useKey);

                    startExam(exam);
                }
        );

        backButton(
                v -> roles()
        );
    }

    // =========================================================
    // FIND EXAM
    // =========================================================

    private JSONObject findExamById(
            String id
    ) {

        JSONArray exams =
                getArray("exams");

        for (int i = 0;
             i < exams.length();
             i++) {

            try {

                JSONObject e =
                        exams.getJSONObject(i);

                if (id.equals(
                        e.optString("id")
                )) {

                    return e;
                }

            } catch (Exception ignored) {
            }
 
