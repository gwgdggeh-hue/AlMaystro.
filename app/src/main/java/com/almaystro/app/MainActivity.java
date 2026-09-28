package com.almaystro.app;

import android.app.AlertDialog;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends Activity {

    // =========================================================
    // الألوان
    // =========================================================

    private static final int GOLD = Color.rgb(218, 177, 58);
    private static final int GOLD_LIGHT = Color.rgb(244, 220, 133);

    private static final int GREEN_DARK = Color.rgb(4, 35, 25);
    private static final int GREEN = Color.rgb(12, 67, 46);
    private static final int GREEN_LIGHT = Color.rgb(25, 91, 63);

    private static final int WHITE = Color.WHITE;
    private static final int BLACK = Color.rgb(18, 18, 18);
    private static final int GRAY = Color.rgb(110, 110, 110);
    private static final int GRAY_LIGHT = Color.rgb(242, 242, 238);

    // =========================================================
    // التخزين
    // =========================================================

    private static final String PREFS = "AL_MAYSTRO_DATA";

    private static final String KEY_DARK = "dark_mode";
    private static final String KEY_STUDENT = "student_name";
    private static final String KEY_TEACHER = "teacher_name";

    private static final String KEY_EXAMS = "exams";
    private static final String KEY_CODES = "codes";
    private static final String KEY_RESULTS = "results";
    private static final String KEY_NOTES = "notes";
    private static final String KEY_VIDEOS = "videos";
    private static final String KEY_USED_CODES = "used_codes";
    private static final String KEY_UPDATE = "update_text";

    // =========================================================
    // المتغيرات
    // =========================================================

    private SharedPreferences prefs;

    private LinearLayout root;
    private LinearLayout content;
    private LinearLayout bottomBar;

    private boolean darkMode = true;

    private String currentStudent = "";
    private String currentTeacher = "";

    private String currentExamId = "";
    private JSONObject currentExam;

    private JSONArray currentQuestions;
    private int currentQuestion = 0;

    private ArrayList<Integer> studentAnswers =
            new ArrayList<>();

    private CountDownTimer examTimer;

    private boolean examRunning = false;
    private boolean examSubmitted = false;

    // =========================================================
    // اختيار الملفات
    // =========================================================

    private static final int PICK_NOTE_FILE = 1001;
    private static final int PICK_VIDEO_FILE = 1002;

    private String pendingNoteTitle = "";
    private String pendingNoteDescription = "";

    private String pendingVideoTitle = "";
    private String pendingVideoDescription = "";

    // =========================================================
    // onCreate
    // =========================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences(
                PREFS,
                MODE_PRIVATE
        );

        darkMode = prefs.getBoolean(
                KEY_DARK,
                true
        );

        applyWindowColors();
        showWelcome();
    }

    // =========================================================
    // أدوات عامة
    // =========================================================

    private int dp(int value) {

        return (int) (
                value *
                        getResources()
                                .getDisplayMetrics()
                                .density
                        + 0.5f
        );
    }

    private int foreground() {

        return darkMode
                ? WHITE
                : BLACK;
    }

    private int secondaryForeground() {

        return darkMode
                ? Color.rgb(210, 210, 210)
                : GRAY;
    }

    private int cardColor() {

        return darkMode
                ? Color.rgb(16, 61, 45)
                : WHITE;
    }

    private void applyWindowColors() {

        Window window = getWindow();

        window.setStatusBarColor(
                darkMode
                        ? GREEN_DARK
                        : GREEN
        );

        window.setNavigationBarColor(
                darkMode
                        ? BLACK
                        : WHITE
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
    // الخلفية
    // =========================================================

    private GradientDrawable backgroundDrawable() {

        GradientDrawable drawable =
                new GradientDrawable(
                        GradientDrawable.Orientation.TL_BR,
                        darkMode
                                ? new int[]{
                                GREEN_DARK,
                                GREEN,
                                Color.rgb(7, 49, 34)
                        }
                                : new int[]{
                                Color.rgb(247, 243, 232),
                                WHITE,
                                Color.rgb(235, 230, 214)
                        }
                );

        drawable.setCornerRadius(
                dp(18)
        );

        return drawable;
    }

    // =========================================================
    // إنشاء الشاشة الأساسية
    // =========================================================

    private void createScreen() {

        root = new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setBackground(
                backgroundDrawable()
        );

        root.setPadding(
                dp(10),
                dp(10),
                dp(10),
                dp(8)
        );

        ScrollView scroll =
                new ScrollView(this);

        scroll.setFillViewport(true);

        content = new LinearLayout(this);

        content.setOrientation(
                LinearLayout.VERTICAL
        );

        content.setGravity(
                Gravity.TOP
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

        setContentView(root);
    }

    // =========================================================
    // النصوص
    // =========================================================

    private TextView text(
            String value,
            float size,
            int color,
            boolean bold
    ) {

        TextView tv =
                new TextView(this);

        tv.setText(value);

        tv.setTextSize(size);

        tv.setTextColor(color);

        tv.setGravity(
                Gravity.CENTER
        );

        tv.setTypeface(
                Typeface.DEFAULT,
                bold
                        ? Typeface.BOLD
                        : Typeface.NORMAL
        );

        tv.setIncludeFontPadding(true);

        tv.setLineSpacing(
                dp(3),
                1.08f
        );

        tv.setPadding(
                dp(10),
                dp(8),
                dp(10),
                dp(8)
        );

        return tv;
    }

    private TextView sectionTitle(
            String title
    ) {

        TextView tv =
                text(
                        title,
                        25,
                        GOLD,
                        true
                );

        tv.setGravity(
                Gravity.RIGHT
        );

        tv.setPadding(
                dp(12),
                dp(16),
                dp(12),
                dp(10)
        );

        return tv;
    }

    // =========================================================
    // الكروت
    // =========================================================

    private LinearLayout card() {

        LinearLayout layout =
                new LinearLayout(this);

        layout.setOrientation(
                LinearLayout.VERTICAL
        );

        layout.setPadding(
                dp(14),
                dp(14),
                dp(14),
                dp(14)
        );

        GradientDrawable bg =
                new GradientDrawable();

        bg.setColor(
                cardColor()
        );

        bg.setCornerRadius(
                dp(20)
        );

        bg.setStroke(
                dp(1),
                darkMode
                        ? Color.rgb(65, 110, 88)
                        : Color.rgb(220, 205, 165)
        );

        layout.setBackground(bg);

        return layout;
    }

    // =========================================================
    // الأزرار
    // =========================================================

    private TextView mainButton(
            String title
    ) {

        TextView button =
                text(
                        title,
                        16,
                        GREEN_DARK,
                        true
                );

        button.setGravity(
                Gravity.CENTER
        );

        GradientDrawable bg =
                new GradientDrawable();

        bg.setColor(
                GOLD
        );

        bg.setCornerRadius(
                dp(24)
        );

        button.setBackground(bg);

        button.setPadding(
                dp(12),
                dp(12),
                dp(12),
                dp(12)
        );

        button.setMinHeight(
                dp(56)
        );

        return button;
    }

    private TextView secondaryButton(
            String title
    ) {

        TextView button =
                text(
                        title,
                        15,
                        foreground(),
                        true
                );

        button.setGravity(
                Gravity.CENTER
        );

        GradientDrawable bg =
                new GradientDrawable();

        bg.setColor(
                cardColor()
        );

        bg.setCornerRadius(
                dp(20)
        );

        bg.setStroke(
                dp(1),
                GOLD
        );

        button.setBackground(bg);

        button.setMinHeight(
                dp(52)
        );

        return button;
    }

    private void addMainButton(
            String title,
            View.OnClickListener listener
    ) {

        TextView button =
                mainButton(title);

        button.setOnClickListener(
                listener
        );

        content.addView(
                button,
                margins(
                        -1,
                        -2,
                        10,
                        6,
                        10,
                        6
                )
        );
    }

    private void addSecondaryButton(
            String title,
            View.OnClickListener listener
    ) {

        TextView button =
                secondaryButton(title);

        button.setOnClickListener(
                listener
        );

        content.addView(
                button,
                margins(
                        -1,
                        -2,
                        10,
                        5,
                        10,
                        5
                )
        );
    }

    // =========================================================
    // EditText
    // =========================================================

    private EditText input(
            String hint
    ) {

        EditText edit =
                new EditText(this);

        edit.setHint(hint);

        edit.setHintTextColor(
                secondaryForeground()
        );

        edit.setTextColor(
                foreground()
        );

        edit.setTextSize(16);

        edit.setGravity(
                Gravity.RIGHT |
                        Gravity.CENTER_VERTICAL
        );

        edit.setPadding(
                dp(16),
                dp(10),
                dp(16),
                dp(10)
        );

        edit.setSingleLine(false);

        GradientDrawable bg =
                new GradientDrawable();

        bg.setColor(
                darkMode
                        ? Color.rgb(25, 45, 38)
                        : Color.rgb(250, 250, 247)
        );

        bg.setCornerRadius(
                dp(16)
        );

        bg.setStroke(
                dp(1),
                GOLD
        );

        edit.setBackground(bg);

        return edit;
    }

    // =========================================================
    // المسافات
    // =========================================================

    private LinearLayout.LayoutParams margins(
            int width,
            int height,
            int left,
            int top,
            int right,
            int bottom
    ) {

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        width,
                        height
                );

        params.setMargins(
                dp(left),
                dp(top),
                dp(right),
                dp(bottom)
        );

        return params;
    }

    // =========================================================
    // الزخارف
    // =========================================================

    private void decorationTop() {

        TextView d =
                text(
                        "✦   ❖   ✦   ❖   ✦",
                        19,
                        GOLD,
                        true
                );

        content.addView(
                d,
                margins(
                        -1,
                        -2,
                        5,
                        3,
                        5,
                        3
                )
        );
    }

    private void decorationBottom() {

        TextView d =
                text(
                        "❖   ✦   ❖",
                        17,
                        GOLD,
                        true
                );

        content.addView(
                d,
                margins(
                        -1,
                        -2,
                        5,
                        15,
                        5,
                        8
                )
        );
    }

    // =========================================================
    // عنوان الشاشة
    // =========================================================

    private void header(
            String title,
            String subtitle
    ) {

        decorationTop();

        TextView t =
                sectionTitle(title);

        content.addView(
                t,
                margins(
                        -1,
                        -2,
                        4,
                        3,
                        4,
                        0
                )
        );

        if (
                subtitle != null &&
                        !subtitle.trim().isEmpty()
        ) {

            TextView s =
                    text(
                            subtitle,
                            15,
                            secondaryForeground(),
                            false
                    );

            s.setGravity(
                    Gravity.RIGHT
            );

            content.addView(
                    s,
                    margins(
                            -1,
                            -2,
                            8,
                            0,
                            8,
                            8
                    )
            );
        }
    }

    // =========================================================
    // شاشة الترحيب
    // =========================================================

    private void showWelcome() {

        stopExamTimer();

        examRunning = false;
        examSubmitted = false;

        currentStudent = "";
        currentTeacher = "";

        createScreen();

        ImageView logo =
                new ImageView(this);

        try {
            logo.setImageResource(
                    R.drawable.maestro
            );
        } catch (Exception ignored) {
        }

        logo.setScaleType(
                ImageView.ScaleType.CENTER_INSIDE
        );

        content.addView(
                logo,
                margins(
                        -1,
                        dp(230),
                        15,
                        20,
                        15,
                        5
                )
        );

        TextView title =
                text(
                        "المايسترو",
                        38,
                        GOLD,
                        true
                );

        content.addView(
                title,
                margins(
                        -1,
                        -2,
                        10,
                        5,
                        10,
                        2
                )
        );

        TextView teacher =
                text(
                        "المايسترو شريف هيبه",
                        22,
                        foreground(),
                        true
                );

        content.addView(
                teacher
        );

        TextView slogan =
                text(
                        "هتتعلم التاريخ ببساطة",
                        18,
                        GOLD_LIGHT,
                        false
                );

        content.addView(
                slogan
        );

        decorationTop();

        LinearLayout intro =
                card();

        TextView introText =
                text(
                        "منصة تعليمية متكاملة\n\n"
                                + "امتحانات • نتائج • مذكرات • فيديوهات\n"
                                + "أذكار وأوراد • متابعة • إنجازات",
                        17,
                        foreground(),
                        true
                );

        introText.setGravity(
                Gravity.CENTER
        );

        intro.addView(
                introText
        );

        content.addView(
                intro,
                margins(
                        -1,
                        -2,
                        15,
                        18,
                        15,
                        12
                )
        );

        addMainButton(
                "🚀 ابدأ الآن",
                v -> showRoleScreen()
        );

        decorationBottom();
    }

    // =========================================================
    // شاشة اختيار الدور
    // =========================================================

    private void showRoleScreen() {

        createScreen();

        header(
                "مرحبًا بك في المايسترو",
                "اختار القسم اللي عايز تدخله"
        );

        LinearLayout roles =
                new LinearLayout(this);

        roles.setOrientation(
                LinearLayout.HORIZONTAL
        );

        roles.setGravity(
                Gravity.CENTER
        );

        roles.setWeightSum(2f);

        LinearLayout studentCard =
                card();

        studentCard.setGravity(
                Gravity.CENTER
        );

        TextView studentIcon =
                text(
                        "👨‍🎓",
                        42,
                        GOLD,
                        false
                );

        TextView studentText =
                text(
                        "طالب",
                        20,
                        foreground(),
                        true
                );

        studentCard.addView(
                studentIcon
        );

        studentCard.addView(
                studentText
        );

        studentCard.setOnClickListener(
                v -> studentLogin()
        );

        LinearLayout teacherCard =
                card();

        teacherCard.setGravity(
                Gravity.CENTER
        );

        TextView teacherIcon =
                text(
                        "👨‍🏫",
                        42,
                        GOLD,
                        false
                );

        TextView teacherText =
                text(
                        "مدرس",
                        20,
                        foreground(),
                        true
                );

        teacherCard.addView(
                teacherIcon
        );

        teacherCard.addView(
                teacherText
        );

        teacherCard.setOnClickListener(
                v -> teacherLogin()
        );

        roles.addView(
                studentCard,
                new LinearLayout.LayoutParams(
                        0,
                        dp(160),
                        1f
                )
        );

        roles.addView(
                teacherCard,
                new LinearLayout.LayoutParams(
                        0,
                        dp(160),
                        1f
                )
        );

        content.addView(
                roles,
                margins(
                        -1,
                        -2,
                        6,
                        10,
                        6,
                        15
                )
        );

        addSecondaryButton(
                "ℹ️ عن التطبيق",
                v -> about()
        );

        addSecondaryButton(
                "↩ الرجوع",
                v -> showWelcome()
        );

        decorationBottom();
    }

    // =========================================================
    // دخول الطالب
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
                        dp(62),
                        10,
                        8,
                        10,
                        6
                )
        );

        content.addView(
                code,
                margins(
                        -1,
                        dp(62),
                        10,
                        6,
                        10,
                        12
                )
        );

        addMainButton(
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

                    if (
                            studentName.isEmpty()
                    ) {
                        name.setError(
                                "اكتب اسم الطالب"
                        );
                        return;
                    }

                    if (
                            examCode.isEmpty()
                    ) {
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
                                "الكود غير موجود"
                        );
                        return;
                    }

                    String usedKey =
                            studentName
                                    .toLowerCase(
                                            Locale.ROOT
                                    )
                                    + "|"
                                    + examCode
                                    .toLowerCase(
                                            Locale.ROOT
                                    );

                    if (
                            isCodeUsed(
                                    usedKey
                            )
                    ) {
                        code.setError(
                                "هذا الكود مستخدم بالفعل لهذا الطالب"
                        );
                        return;
                    }

                    JSONArray questions =
                            exam.optJSONArray(
                                    "questions"
                            );

                    if (
                            questions == null ||
                            questions.length() == 0
                    ) {
                        toast(
                                "الامتحان لا يحتوي على أسئلة بعد"
                        );
                        return;
                    }

                    currentStudent =
                            studentName;

                    prefs.edit()
                            .putString(
                                    KEY_STUDENT,
                                    studentName
                            )
                            .apply();

                    markCodeUsed(
                            usedKey
                    );

                    startExam(
                            exam
                    );
                }
        );

        addSecondaryButton(
                "↩ رجوع",
                v -> showRoleScreen()
        );
    }

    // =========================================================
    // الصفحة الرئيسية للطالب
    // =========================================================

    private void studentHome() {

        createScreen();

        header(
                "الرئيسية",
                "أهلًا يا " +
                        (
                                currentStudent.isEmpty()
                                        ? "طالب"
                                        : currentStudent
                        )
        );

        LinearLayout welcome =
                card();

        welcome.addView(
                text(
                        "✦ منصة المايسترو التعليمية ✦",
                        21,
                        GOLD,
                        true
                )
        );

        welcome.addView(
                text(
                        "اختار القسم من الشريط السفلي",
                        15,
                        secondaryForeground(),
                        false
                )
        );

        content.addView(
                welcome,
                margins(
                        -1,
                        -2,
                        10,
                        8,
                        10,
                        15
                )
        );

        // أقسام سريعة على شكل بطاقات
        LinearLayout row1 =
                new LinearLayout(this);

        row1.setOrientation(
                LinearLayout.HORIZONTAL
        );

        addHomeCard(
                row1,
                "📝",
                "امتحان جديد",
                v -> studentLogin()
        );

        addHomeCard(
                row1,
                "📊",
                "نتائجي",
                v -> studentResults()
        );

        content.addView(
                row1,
                margins(
                        -1,
                        dp(135),
                        5,
                        5,
                        5,
                        5
                )
        );

        LinearLayout row2 =
                new LinearLayout(this);

        row2.setOrientation(
                LinearLayout.HORIZONTAL
        );

        addHomeCard(
                row2,
                "📚",
                "المذكرات",
                v -> studentNotes()
        );

        addHomeCard(
                row2,
                "🎬",
                "الفيديوهات",
                v -> studentVideos()
        );

        content.addView(
                row2,
                margins(
                        -1,
                        dp(135),
                        5,
                        5,
                        5,
                        5
                )
        );

        LinearLayout row3 =
                new LinearLayout(this);

        row3.setOrientation(
                LinearLayout.HORIZONTAL
        );

        addHomeCard(
                row3,
                "🤲",
                "الورد والأذكار",
                v -> azkar()
        );

        addHomeCard(
                row3,
                "🏆",
                "إنجازاتي",
                v -> progress()
        );

        content.addView(
                row3,
                margins(
                        -1,
                        dp(135),
                        5,
                        5,
                        5,
                        5
                )
        );

        studentBottomNavigation(
                0
        );
    }

    // =========================================================
    // بطاقة الرئيسية
    // =========================================================

    private void addHomeCard(
            LinearLayout row,
            String icon,
            String title,
            View.OnClickListener listener
    ) {

        LinearLayout card =
                card();

        card.setGravity(
                Gravity.CENTER
        );

        TextView iconText =
                text(
                        icon,
                        30,
                        GOLD,
                        false
                );

        TextView titleText =
                text(
                        title,
                        15,
                        foreground(),
                        true
                );

        card.addView(
                iconText
        );

        card.addView(
                titleText
        );

        card.setOnClickListener(
                listener
        );

        row.addView(
                card,
                new LinearLayout.LayoutParams(
                        0,
                        -1,
                        1f
                )
        );

        LinearLayout.LayoutParams p =
                (LinearLayout.LayoutParams)
                        card.getLayoutParams();

        p.setMargins(
                dp(5),
                dp(4),
                dp(5),
                dp(4)
        );

        card.setLayoutParams(p);
    }

    // =========================================================
    // شريط الطالب
    // =========================================================

    private void studentBottomNavigation(
            int selected
    ) {

        bottomBar =
                new LinearLayout(this);

        bottomBar.setOrientation(
                LinearLayout.HORIZONTAL
        );

        bottomBar.setGravity(
                Gravity.CENTER
        );

        GradientDrawable bg =
                new GradientDrawable();

        bg.setColor(
                darkMode
                        ? Color.rgb(8, 48, 34)
                        : WHITE
        );

        bg.setCornerRadius(
                dp(20)
        );

        bg.setStroke(
                dp(1),
                GOLD
        );

        bottomBar.setBackground(bg);

        addNavItem(
                bottomBar,
                "الرئيسية",
                selected == 0,
                v -> studentHome()
        );

        addNavItem(
                bottomBar,
                "امتحاناتي",
                selected == 1,
                v -> studentResults()
        );

        addNavItem(
                bottomBar,
                "الأذكار",
                selected == 2,
                v -> azkar()
        );

        addNavItem(
                bottomBar,
                "المذكرات",
                selected == 3,
                v -> studentNotes()
        );

        addNavItem(
                bottomBar,
                "الإعدادات",
                selected == 4,
                v -> studentSettings()
        );

        root.addView(
                bottomBar,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(68)
                )
        );
    }

    private void addNavItem(
            LinearLayout bar,
            String title,
            boolean selected,
            View.OnClickListener listener
    ) {

        TextView item =
                text(
                        title,
                        11,
                        selected
                                ? GOLD
                                : foreground(),
                        true
                );

        item.setGravity(
                Gravity.CENTER
        );

        item.setOnClickListener(
                listener
        );

        bar.addView(
                item,
                new LinearLayout.LayoutParams(
                        0,
                        -1,
                        1f
                )
        );
    }

    // =========================================================
    // نتائج الطالب
    // =========================================================

    private void studentResults() {

        createScreen();

        header(
                "امتحاناتي ونتائجي",
                currentStudent
        );

        JSONArray results =
                getArray(
                        KEY_RESULTS
                );

        boolean found = false;

        for (
                int i = 0;
                i < results.length();
                i++
        ) {

            try {

                JSONObject result =
                        results.getJSONObject(i);

                if (
                        !currentStudent.equals(
                                result.optString(
                                        "student"
                                )
                        )
                ) {
                    continue;
                }

                found = true;

                LinearLayout c =
                        card();

                String exam =
                        result.optString(
                                "exam"
                        );

                int correct =
                        result.optInt(
                                "correct"
                        );

                int total =
                        result.optInt(
                                "total"
                        );

                int answered =
                        result.optInt(
                                "answered"
                        );

                int wrong =
                        result.optInt(
                                "wrong"
                        );

                c.addView(
                        text(
                                "📝 " + exam,
                                19,
                                GOLD,
                                true
                        )
                );

                c.addView(
                        text(
                                "الدرجة: "
                                        + correct
                                        + " / "
                                        + total
                                        + "\n"
                                        + "تمت الإجابة: "
                                        + answered
                                        + "\n"
                                        + "الإجابات الخاطئة: "
                                        + wrong,
                                16,
                                foreground(),
                                false
                        )
                );

                String mistakes =
                        result.optString(
                                "mistakes",
                                ""
                        );

                if (
                        !mistakes.isEmpty()
                ) {

                    c.addView(
                            text(
                                    "\n📌 مراجعة الأخطاء:\n"
                                            + mistakes,
                                    15,
                                    secondaryForeground(),
                                    false
                            )
                    );
                }

                content.addView(
                        c,
                        margins(
                                -1,
                                -2,
                                10,
                                6,
                                10,
                                6
                        )
                );

            } catch (Exception ignored) {
            }
        }

        if (!found) {

            content.addView(
                    text(
                            "لا توجد نتائج مسجلة لك حتى الآن.",
                            17,
                            secondaryForeground(),
                            true
                    )
            );
        }

        studentBottomNavigation(
                1
        );
    }

    // =========================================================
    // المذكرات للطالب
    // =========================================================

    private void studentNotes() {

        createScreen();

        header(
                "مذكرات الشرح",
                "المذكرات المنشورة من المدرس"
        );

        JSONArray notes =
                getArray(
                        KEY_NOTES
                );

        if (
                notes.length() == 0
        ) {

            content.addView(
                    text(
                            "📚 لا توجد مذكرات منشورة حاليًا.",
                            17,
                            secondaryForeground(),
                            true
                    )
            );
        }

        for (
                int i = 0;
                i < notes.length();
                i++
        ) {

            try {

                JSONObject note =
                        notes.getJSONObject(i);

                LinearLayout c =
                        card();

                c.addView(
                        text(
                                "📖 "
                                        + note.optString(
                                        "title"
                                ),
                                19,
                                GOLD,
                                true
                        )
                );

                String description =
                        note.optString(
                                "description"
                        );

                if (
                        !description.isEmpty()
                ) {

                    c.addView(
                            text(
                                    description,
                                    15,
                                    foreground(),
                                    false
                            )
                    );
                }

                String uri =
                        note.optString(
                                "uri",
                                ""
                        );

                if (
                        !uri.isEmpty()
                ) {

                    addCardButton(
                            c,
                            "📂 فتح المرفق",
                            v -> openUri(
                                    uri
                            )
                    );
                }

                content.addView(
                        c,
                        margins(
                                -1,
                                -2,
                                10,
                                6,
                                10,
                                6
                        )
                );

            } catch (Exception ignored) {
            }
        }

        studentBottomNavigation(
                3
        );
    }

    // =========================================================
    // فيديوهات الطالب
    // =========================================================

    private void studentVideos() {

        createScreen();

        header(
                "الفيديوهات",
                "المحتوى المرئي"
        );

        JSONArray videos =
                getArray(
                        KEY_VIDEOS
                );

        if (
                videos.length() == 0
        ) {

            content.addView(
                    text(
                            "🎬 لا توجد فيديوهات منشورة حاليًا.",
                            17,
                            secondaryForeground(),
                            true
                    )
            );
        }

        for (
                int i = 0;
                i < videos.length();
                i++
        ) {

            try {

                JSONObject video =
                        videos.getJSONObject(i);

                LinearLayout c =
                        card();

                c.addView(
                        text(
                                "🎬 "
                                        + video.optString(
                                        "title"
                                ),
                                19,
                                GOLD,
                                true
                        )
                );

                c.addView(
                        text(
                                video.optString(
                                        "description",
                                        ""
                                ),
                                15,
                                foreground(),
                                false
                        )
                );

                String uri =
                        video.optString(
                                "uri",
                                ""
                        );

                if (
                        !uri.isEmpty()
                ) {

                    addCardButton(
                            c,
                            "▶ تشغيل الفيديو",
                            v -> playVideo(
                                    uri,
                                    video.optString(
                                            "title"
                                    )
                            )
                    );
                }

                content.addView(
                        c,
                        margins(
                                -1,
                                -2,
                                10,
                                6,
                                10,
                                6
                        )
                );

            } catch (Exception ignored) {
            }
        }

        studentBottomNavigation(
                0
        );
    }

    // =========================================================
    // الأذكار والورد
    // =========================================================

    private void azkar() {

        createScreen();

        header(
                "الورد والأذكار",
                "ذكر الله وطمأنينة القلب"
        );

        addZikrCard(
                "☀️ أذكار الصباح",
                "آية الكرسي\n"
                        + "الإخلاص والفلق والناس\n"
                        + "أصبحنا وأصبح الملك لله\n"
                        + "اللهم إني أسألك خير هذا اليوم\n"
                        + "سبحان الله وبحمده\n"
                        + "أستغفر الله وأتوب إليه\n"
                        + "لا إله إلا الله وحده لا شريك له"
        );

        addZikrCard(
                "🌙 أذكار المساء",
                "آية الكرسي\n"
                        + "الإخلاص والفلق والناس\n"
                        + "أمسينا وأمسى الملك لله\n"
                        + "اللهم بك أمسينا وبك أصبحنا\n"
                        + "سبحان الله وبحمده\n"
                        + "أستغفر الله وأتوب إليه"
        );

        addZikrCard(
                "📿 ورد التسبيح والاستغفار",
                "سبحان الله\n"
                        + "الحمد لله\n"
                        + "الله أكبر\n"
                        + "لا إله إلا الله\n"
                        + "أستغفر الله وأتوب إليه\n"
                        + "اللهم صل وسلم على نبينا محمد ﷺ"
        );

        addMainButton(
                "📿 عداد الذكر",
                v -> dhikrCounter()
        );

        studentBottomNavigation(
                2
        );
    }

    private void addZikrCard(
            String title,
            String body
    ) {

        LinearLayout c =
                card();

        c.addView(
                text(
                        title,
                        21,
                        GOLD,
                        true
                )
        );

        TextView bodyText =
                text(
                        body,
                        17,
                        foreground(),
                        false
                );

        bodyText.setGravity(
                Gravity.RIGHT
        );

        c.addView(
                bodyText
        );

        content.addView(
                c,
                margins(
                        -1,
                        -2,
                        10,
                        6,
                        10,
                        6
                )
        );
    }

    // =========================================================
    // عداد الذكر
    // =========================================================

    private void dhikrCounter() {

        final int[] count = {
                0
        };

        LinearLayout box =
                new LinearLayout(this);

        box.setOrientation(
                LinearLayout.VERTICAL
        );

        box.setPadding(
                dp(20),
                dp(20),
                dp(20),
                dp(20)
        );

        TextView number =
                text(
                        "0",
                        50,
                        GOLD,
                        true
                );

        Button plus =
                new Button(this);

        plus.setText(
                "📿 اضغط للذكر"
        );

        plus.setOnClickListener(
                v -> {

                    count[0]++;

                    number.setText(
                            String.valueOf(
                                    count[0]
                            )
                    );
                }
        );

        box.addView(
                number,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(100)
                )
        );

        box.addView(
                plus,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        new AlertDialog.Builder(this)
                .setTitle(
                        "عداد الذكر"
                )
                .setView(box)
                .setPositiveButton(
                        "إغلاق",
                        null
                )
                .show();
    }

    // =========================================================
    // الإنجازات
    // =========================================================

    private void progress() {

        createScreen();

        header(
                "إنجازاتي",
                "تقدمك في المايسترو"
        );

        JSONArray results =
                getArray(
                        KEY_RESULTS
                );

        int exams = 0;
        int correct = 0;
        int total = 0;

        for (
                int i = 0;
                i < results.length();
                i++
        ) {

            try {

                JSONObject r =
                        results.getJSONObject(i);

                if (
                        currentStudent.equals(
                                r.optString(
                                        "student"
                                )
                        )
                ) {

                    exams++;

                    correct +=
                            r.optInt(
                                    "correct"
                            );

                    total +=
                            r.optInt(
                                    "total"
                            );
                }

            } catch (Exception ignored) {
            }
        }

        LinearLayout c =
                card();

        c.addView(
                text(
                        "🏆 ملخص التقدم",
                        22,
                        GOLD,
                        true
                )
        );

        c.addView(
                text(
                        "الامتحانات المنجزة: "
                                + exams
                                + "\n"
                                + "الإجابات الصحيحة: "
                                + correct
                                + "\n"
                                + "إجمالي الأسئلة: "
                                + total,
                        18,
                        foreground(),
                        false
                )
        );

        content.addView(
                c,
                margins(
                        -1,
                        -2,
                        10,
                        10,
                        10,
                        10
                )
        );

        studentBottomNavigation(
                0
        );
    }

    // =========================================================
    // إعدادات الطالب
    // =========================================================

    private void studentSettings() {

        createScreen();

        header(
                "الإعدادات",
                "تخصيص التطبيق"
        );

        LinearLayout profile =
                card();

        profile.addView(
                text(
                        "👤 بيانات الطالب",
                        20,
                        GOLD,
                        true
                )
        );

        profile.addView(
                text(
                        "الاسم: "
                                + currentStudent,
                        16,
                        foreground(),
                        false
                )
        );

        content.addView(
                profile,
                margins(
                        -1,
                        -2,
                        10,
                        8,
                        10,
                        8
                )
        );

        addMainButton(
                darkMode
                        ? "☀️ تفعيل الوضع الفاتح"
                        : "🌙 تفعيل الوضع الداكن",
                v -> {

                    darkMode =
                            !darkMode;

                    prefs.edit()
                            .putBoolean(
                                    KEY_DARK,
                                    darkMode
                            )
                            .apply();

                    applyWindowColors();

                    studentSettings();
                }
        );

        addSecondaryButton(
                "🔆 ضبط السطوع",
                v -> brightnessDialog()
        );

        addSecondaryButton(
                "🔐 الخصوصية والأمان",
                v -> privacy()
        );

        addSecondaryButton(
                "ℹ️ عن التطبيق",
                v -> about()
        );

        addSecondaryButton(
                "🚪 تسجيل الخروج",
                v -> showRoleScreen()
        );

        studentBottomNavigation(
                4
        );
    }

    // =========================================================
    // السطوع
    // =========================================================

    private void brightnessDialog() {

        final android.widget.SeekBar seekBar =
                new android.widget.SeekBar(this);

        seekBar.setMax(
                100
        );

        WindowManager.LayoutParams current =
                getWindow()
                        .getAttributes();

        int progress =
                current.screenBrightness < 0
                        ? 100
                        : (int)
                        (
                                current.screenBrightness
                                        * 100
                        );

        seekBar.setProgress(
                progress
        );

        seekBar.setOnSeekBarChangeListener(
                new android.widget.SeekBar
                        .OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(
                            android.widget.SeekBar bar,
                            int value,
                            boolean fromUser
                    ) {

                        float brightness =
                                Math.max(
                                        0.05f,
                                        value / 100f
                                );

                        WindowManager.LayoutParams p =
                                getWindow()
                                        .getAttributes();

                        p.screenBrightness =
                                brightness;

                        getWindow()
                                .setAttributes(p);
                    }

                    @Override
                    public void onStartTrackingTouch(
                            android.widget.SeekBar bar
                    ) {
                    }

                    @Override
                    public void onStopTrackingTouch(
                            android.widget.SeekBar bar
                    ) {
                    }
                }
        );

        new AlertDialog.Builder(this)
                .setTitle(
                        "سطوع التطبيق"
                )
                .setView(
                        seekBar
                )
                .setPositiveButton(
                        "تم",
                        null
                )
                .show();
    }

    // =========================================================
    // الخصوصية
    // =========================================================

    private void privacy() {

        createScreen();

        header(
                "الخصوصية والأمان",
                "حماية بيانات المستخدم"
        );

        LinearLayout c =
                card();

        c.addView(
                text(
                        "🔐 حماية الامتحان",
                        21,
                        GOLD,
                        true
                )
        );

        c.addView(
                text(
                        "أثناء الامتحان يتم منع لقطات الشاشة "
                                + "ومحاولة الخروج من شاشة الامتحان.\n\n"
                                + "المؤقت ينتهي تلقائيًا.\n\n"
                                + "كود الامتحان لا يسمح بمحاولة ثانية "
                                + "لنفس الطالب.",
                        16,
                        foreground(),
                        false
                )
        );

        content.addView(
                c,
                margins(
                        -1,
                        -2,
                        10,
                        10,
                        10,
                        10
                )
        );

        addSecondaryButton(
                "↩ رجوع",
                v -> studentHome()
        );
    }

    // =========================================================
    // دخول المدرس
    // =========================================================

    private void teacherLogin() {

        createScreen();

        header(
                "دخول المدرس",
                "لوحة تحكم المايسترو"
        );

        EditText name =
                input(
                        "اسم المدرس"
                );

        EditText code =
                input(
                        "كود الدخول"
                );

        code.setInputType(
                android.text.InputType.TYPE_CLASS_NUMBER
                        |
                        android.text.InputType
                                .TYPE_NUMBER_VARIATION_PASSWORD
        );

        content.addView(
                name,
                margins(
                        -1,
                        dp(62),
                        10,
                        10,
                        10,
                        6
                )
        );

        content.addView(
                code,
                margins(
                        -1,
                        dp(62),
                        10,
                        6,
                        10,
                        12
                )
        );

        LinearLayout info =
                card();

        info.addView(
                text(
                        "ملاحظة",
                        18,
                        GOLD,
                        true
                )
        );

        info.addView(
                text(
                        "في النسخة الحالية كود دخول المدرس المحلي هو 1234.",
                        14,
                        secondaryForeground(),
                        false
                )
        );

        content.addView(
                info,
                margins(
                        -1,
                        -2,
                        10,
                        5,
                        10,
                        12
                )
        );

        addMainButton(
                "👨‍🏫 دخول لوحة المدرس",
                v -> {

                    String teacherName =
                            name.getText()
                                    .toString()
                                    .trim();

                    String teacherCode =
                            code.getText()
                                    .toString()
                                    .trim();

                    if (
                            teacherName.isEmpty()
                    ) {

                        name.setError(
                                "اكتب اسم المدرس"
                        );

                        return;
                    }

                    if (
                            !"1234".equals(
                                    teacherCode
                            )
                    ) {

                        code.setError(
                                "كود الدخول غير صحيح"
                        );

                        return;
                    }

                    currentTeacher =
                            teacherName;

                    prefs.edit()
                            .putString(
                                    KEY_TEACHER,
                                    teacherName
                            )
                            .apply();

                    teacherHome();
                }
        );

        addSecondaryButton(
                "↩ رجوع",
                v -> showRoleScreen()
        );
    }

    // =========================================================
    // الصفحة الرئيسية للمدرس
    // =========================================================

    private void teacherHome() {

        createScreen();

        header(
                "لوحة المدرس",
                "أهلًا يا " +
                        (
                                currentTeacher.isEmpty()
                                        ? "مدرس"
                                        : currentTeacher
                        )
        );

        JSONArray exams =
                getArray(
                        KEY_EXAMS
                );

        JSONArray results =
                getArray(
                        KEY_RESULTS
                );

        LinearLayout statistics =
                card();

        statistics.addView(
                text(
                        "📊 إحصائيات سريعة",
                        21,
                        GOLD,
                        true
                )
        );

        statistics.addView(
                text(
                        "الامتحانات: "
                                + exams.length()
                                + "\n"
                                + "النتائج: "
                                + results.length(),
                        17,
                        foreground(),
                        false
                )
        );

        content.addView(
                statistics,
                margins(
                        -1,
                        -2,
                        10,
                        5,
                        10,
                        10
                )
        );

        // لوحة الأقسام
        LinearLayout row1 =
                new LinearLayout(this);

        row1.setOrientation(
                LinearLayout.HORIZONTAL
        );

        addTeacherHomeCard(
                row1,
                "📝",
                "الامتحانات",
                v -> examManager()
        );

        addTeacherHomeCard(
                row1,
                "❓",
                "الأسئلة",
                v -> questionManager()
        );

        content.addView(
                row1,
                margins(
                        -1,
                        dp(135),
                        5,
                        5,
                        5,
                        5
                )
        );

        LinearLayout row2 =
                new LinearLayout(this);

        row2.setOrientation(
                LinearLayout.HORIZONTAL
        );

        addTeacherHomeCard(
                row2,
                "🔑",
                "الأكواد",
                v -> codes()
        );

        addTeacherHomeCard(
                row2,
                "📊",
                "النتائج",
                v -> teacherResults()
        );

        content.addView(
                row2,
                margins(
                        -1,
                        dp(135),
                        5,
                        5,
                        5,
                        5
                )
        );

        LinearLayout row3 =
                new LinearLayout(this);

        row3.setOrientation(
                LinearLayout.HORIZONTAL
        );

        addTeacherHomeCard(
                row3,
                "📚",
                "المذكرات",
                v -> teacherNotes()
        );

        addTeacherHomeCard(
                row3,
                "🎬",
                "الفيديوهات",
                v -> teacherVideos()
        );

        content.addView(
                row3,
                margins(
                        -1,
                        dp(135),
                        5,
                        5,
                        5,
                        5
                )
        );

        LinearLayout row4 =
                new LinearLayout(this);

        row4.setOrientation(
                LinearLayout.HORIZONTAL
        );

        addTeacherHomeCard(
                row4,
                "👥",
                "المجموعات",
                v -> groups()
        );

        addTeacherHomeCard(
                row4,
                "⚙️",
                "الإعدادات",
                v -> teacherSettings()
        );

        content.addView(
                row4,
                margins(
                        -1,
                        dp(135),
                        5,
                        5,
                        5,
                        5
                )
        );

        teacherBottomNavigation(
                0
        );
    }

    private void addTeacherHomeCard(
            LinearLayout row,
            String icon,
            String title,
            View.OnClickListener listener
    ) {

        LinearLayout c =
                card();

        c.setGravity(
                Gravity.CENTER
        );

        c.addView(
                text(
                        icon,
                        30,
                        GOLD,
                        false
                )
        );

        c.addView(
                text(
                        title,
                        15,
                        foreground(),
                        true
                )
        );

        c.setOnClickListener(
                listener
        );

        row.addView(
                c,
                new LinearLayout.LayoutParams(
                        0,
                        -1,
                        1f
                )
        );

        LinearLayout.LayoutParams p =
                (LinearLayout.LayoutParams)
                        c.getLayoutParams();

        p.setMargins(
                dp(5),
                dp(4),
                dp(5),
                dp(4)
        );

        c.setLayoutParams(p);
    }

    // =========================================================
    // شريط المدرس
    // =========================================================

    private void teacherBottomNavigation(
            int selected
    ) {

        bottomBar =
                new LinearLayout(this);

        bottomBar.setOrientation(
                LinearLayout.HORIZONTAL
        );

        GradientDrawable bg =
                new GradientDrawable();

        bg.setColor(
                darkMode
                        ? Color.rgb(8, 48, 34)
                        : WHITE
        );

        bg.setCornerRadius(
                dp(20)
        );

        bg.setStroke(
                dp(1),
                GOLD
        );

        bottomBar.setBackground(bg);

        addNavItem(
                bottomBar,
                "الرئيسية",
                selected == 0,
                v -> teacherHome()
        );

        addNavItem(
                bottomBar,
                "الامتحانات",
                selected == 1,
                v -> examManager()
        );

        addNavItem(
                bottomBar,
                "النتائج",
                selected == 2,
                v -> teacherResults()
        );

        addNavItem(
                bottomBar,
                "المذكرات",
                selected == 3,
                v -> teacherNotes()
        );

        addNavItem(
                bottomBar,
                "المزيد",
                selected == 4,
                v -> teacherMore()
        );

        root.addView(
                bottomBar,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(68)
                )
        );
    }

    // =========================================================
    // المزيد للمدرس
    // =========================================================

    private void teacherMore() {

        createScreen();

        header(
                "أقسام المدرس",
                "كل أدوات التحكم"
        );

        addSecondaryButton(
                "❓ إدارة الأسئلة",
                v -> questionManager()
        );

        addSecondaryButton(
                "🔑 أكواد الامتحانات",
                v -> codes()
        );

        addSecondaryButton(
                "🎬 إدارة الفيديوهات",
                v -> teacherVideos()
        );

        addSecondaryButton(
                "👥 مجموعات الطلاب",
                v -> groups()
        );

        addSecondaryButton(
                "📢 التحديثات",
                v -> updatesManager()
        );

        addSecondaryButton(
                "⚙️ الإعدادات",
                v -> teacherSettings()
        );

        addSecondaryButton(
                "ℹ️ عن التطبيق",
                v -> about()
        );

        teacherBottomNavigation(
                4
        );
    }

    // =========================================================
    // إدارة الامتحانات
    // =========================================================

    private void examManager() {

        createScreen();

        header(
                "إدارة الامتحانات",
                "إنشاء الامتحانات وتعديلها"
        );

        addMainButton(
                "➕ إنشاء امتحان جديد",
                v -> createExamScreen()
        );

        JSONArray exams =
                getArray(
                        KEY_EXAMS
                );

        if (
                exams.length() == 0
        ) {

            content.addView(
                    text(
                            "📝 لا توجد امتحانات حتى الآن.\n"
                                    + "اضغط «إنشاء امتحان جديد» لبدء أول امتحان.",
                            17,
                            secondaryForeground(),
                            true
                    )
            );
        }

        for (
                int i = 0;
                i < exams.length();
                i++
        ) {

            try {

                JSONObject exam =
                        exams.getJSONObject(i);

                addExamManagerCard(
                        exam
                );

            } catch (Exception ignored) {
            }
        }

        teacherBottomNavigation(
                1
        );
    }

    private void addExamManagerCard(
            JSONObject exam
    ) {

        LinearLayout c =
                card();

        String id =
                exam.optString(
                        "id"
                );

        String title =
                exam.optString(
                        "title"
                );

        int duration =
                exam.optInt(
                        "duration",
                        30
                );

        JSONArray questions =
                exam.optJSONArray(
                        "questions"
                );

        int count =
                questions == null
                        ? 0
                        : questions.length();

        c.addView(
                text(
                        "📝 " + title,
                        20,
                        GOLD,
                        true
                )
        );

        c.addView(
                text(
                        "⏱️ المدة: "
                                + duration
                                + " دقيقة\n"
                                + "❓ عدد الأسئلة: "
                                + count,
                        15,
                        foreground(),
                        false
                )
        );

        addCardButton(
                c,
                "✏️ فتح وتعديل الامتحان",
                v -> editExamScreen(
                        id
                )
        );

        addCardButton(
                c,
                "🔑 إنشاء كود للامتحان",
                v -> addCodeForExam(
                        id
                )
        );

        addCardButton(
                c,
                "🗑️ حذف الامتحان",
                v -> deleteExamConfirm(
                        id
                )
        );

        content.addView(
                c,
                margins(
                        -1,
                        -2,
                        10,
                        7,
                        10,
                        7
                )
        );
    }

    private void addCardButton(
            LinearLayout parent,
            String title,
            View.OnClickListener listener
    ) {

        TextView b =
                secondaryButton(
                        title
                );

        b.setOnClickListener(
                listener
        );

        parent.addView(
                b,
                margins(
                        -1,
                        -2,
                        0,
                        5,
                        0,
                        5
                )
        );
    }

    // =========================================================
    // إنشاء امتحان كامل في شاشة واحدة
    // =========================================================

    private void createExamScreen() {

        createScreen();

        header(
                "إنشاء امتحان جديد",
                "أضف بيانات الامتحان وكل الأسئلة في نفس الشاشة"
        );

        EditText title =
                input(
                        "اسم الامتحان"
                );

        EditText duration =
                input(
                        "مدة الامتحان بالدقائق"
                );

        content.addView(
                title,
                margins(
                        -1,
                        dp(70),
                        10,
                        5,
                        10,
                        5
                )
        );

        content.addView(
                duration,
                margins(
                        -1,
                        dp(70),
                        10,
                        5,
                        10,
                        12
                )
        );

        LinearLayout questionsContainer =
                new LinearLayout(this);

        questionsContainer.setOrientation(
                LinearLayout.VERTICAL
        );

        content.addView(
                text(
                        "❓ أسئلة الامتحان",
                        23,
                        GOLD,
                        true
                )
        );

        content.addView(
                questionsContainer
        );

        addQuestionEditor(
                questionsContainer,
                1
        );

        addMainButton(
                "➕ إضافة سؤال آخر",
                v -> addQuestionEditor(
                        questionsContainer,
                        questionsContainer
                                .getChildCount() + 1
                )
        );

        addMainButton(
                "💾 حفظ الامتحان كاملًا",
                v -> saveNewExam(
                        title,
                        duration,
                        questionsContainer
                )
        );

        addSecondaryButton(
                "↩ إلغاء",
                v -> examManager()
        );
    }

    // =========================================================
    // محرر السؤال
    // =========================================================

    private void addQuestionEditor(
            LinearLayout container,
            int number
    ) {

        LinearLayout questionCard =
                card();

        questionCard.setTag(
                "QUESTION"
        );

        TextView title =
                text(
                        "السؤال رقم "
                                + number,
                        19,
                        GOLD,
                        true
                );

        title.setGravity(
                Gravity.RIGHT
        );

        questionCard.addView(
                title
        );

        EditText question =
                input(
                        "اكتب السؤال هنا"
                );

        EditText a =
                input(
                        "الاختيار الأول"
                );

        EditText b =
                input(
                        "الاختيار الثاني"
                );

        EditText c =
                input(
                        "الاختيار الثالث"
                );

        EditText d =
                input(
                        "الاختيار الرابع"
                );

        EditText answer =
                input(
                        "الإجابة الصحيحة: 1 أو 2 أو 3 أو 4"
                );

        EditText explanation =
                input(
                        "شرح الإجابة / تصحيح الخطأ"
                );

        addEditorField(
                questionCard,
                question,
                100
        );

        addEditorField(
                questionCard,
                a,
                70
        );

        addEditorField(
                questionCard,
                b,
                70
        );

        addEditorField(
                questionCard,
                c,
                70
        );

        addEditorField(
                questionCard,
                d,
                70
        );

        addEditorField(
                questionCard,
                answer,
                70
        );

        addEditorField(
                questionCard,
                explanation,
                100
        );

        container.addView(
                questionCard,
                margins(
                        -1,
                        -2,
                        0,
                        7,
                        0,
                        7
                )
        );
    }

    private void addEditorField(
            LinearLayout parent,
            EditText edit,
            int height
    ) {

        parent.addView(
                edit,
                margins(
                        -1,
                        dp(height),
                        0,
                        4,
                        0,
                        4
                )
        );
    }

    // =========================================================
    // حفظ امتحان جديد
    // =========================================================

    private void saveNewExam(
            EditText title,
            EditText duration,
            LinearLayout questionsContainer
    ) {

        String examTitle =
                title.getText()
                        .toString()
                        .trim();

        if (
                examTitle.isEmpty()
        ) {

            title.setError(
                    "اكتب اسم الامتحان"
            );

            return;
        }

        int minutes =
                parseInt(
                        duration.getText()
                                .toString(),
                        30
                );

        if (
                minutes < 1
        ) {
            minutes = 1;
        }

        JSONArray questions =
                collectQuestions(
                        questionsContainer
                );

        if (
                questions.length() == 0
        ) {

            toast(
                    "أضف سؤالًا واحدًا على الأقل"
            );

            return;
        }

        JSONArray exams =
                getArray(
                        KEY_EXAMS
                );

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
                    examTitle
            );

            exam.put(
                    "duration",
                    minutes
            );

            exam.put(
                    "questions",
                    questions
            );

            exams.put(
                    exam
            );

            saveArray(
                    KEY_EXAMS,
                    exams
            );

            toast(
                    "تم إنشاء الامتحان بنجاح"
            );

            examManager();

        } catch (Exception e) {

            toast(
                    "حدث خطأ أثناء حفظ الامتحان"
            );
        }
    }

    // =========================================================
    // جمع الأسئلة
    // =========================================================

    private JSONArray collectQuestions(
            LinearLayout container
    ) {

        JSONArray result =
                new JSONArray();

        for (
                int i = 0;
                i < container.getChildCount();
                i++
        ) {

            View view =
                    container.getChildAt(i);

            if (
                    !(view instanceof LinearLayout)
            ) {
                continue;
            }

            LinearLayout card =
                    (LinearLayout) view;

            ArrayList<EditText> fields =
                    new ArrayList<>();

            collectEditTexts(
                    card,
                    fields
            );

            if (
                    fields.size() < 7
            ) {
                continue;
            }

            String question =
                    fields.get(0)
                            .getText()
                            .toString()
                            .trim();

            String a =
                    fields.get(1)
                            .getText()
                            .toString()
                            .trim();

            String b =
                    fields.get(2)
                            .getText()
                            .toString()
                            .trim();

            String c =
                    fields.get(3)
                            .getText()
                            .toString()
                            .trim();

            String d =
                    fields.get(4)
                            .getText()
                            .toString()
                            .trim();

            String answer =
                    fields.get(5)
                            .getText()
                            .toString()
                            .trim();

            String explanation =
                    fields.get(6)
                            .getText()
                            .toString()
                            .trim();

            if (
                    question.isEmpty() &&
                            a.isEmpty() &&
                            b.isEmpty() &&
                            c.isEmpty() &&
                            d.isEmpty()
            ) {
                continue;
            }

            if (
                    question.isEmpty()
            ) {

                toast(
                        "يوجد سؤال بدون نص"
                );

                return new JSONArray();
            }

            if (
                    a.isEmpty() ||
                            b.isEmpty() ||
                            c.isEmpty() ||
                            d.isEmpty()
            ) {

                toast(
                        "يجب إكمال اختيارات السؤال"
                );

                return new JSONArray();
            }

            if (
                    !answer.equals("1") &&
                            !answer.equals("2") &&
                            !answer.equals("3") &&
                            !answer.equals("4")
            ) {

                toast(
                        "الإجابة الصحيحة يجب أن تكون 1 أو 2 أو 3 أو 4"
                );

                return new JSONArray();
            }

            try {

                JSONObject q =
                        new JSONObject();

                q.put(
                        "question",
                        question
                );

                q.put(
                        "a",
                        a
                );

                q.put(
                        "b",
                        b
                );

                q.put(
                        "c",
                        c
                );

                q.put(
                        "d",
                        d
                );

                q.put(
                        "answer",
                        Integer.parseInt(
                                answer
                        )
                );

                q.put(
                        "explanation",
                        explanation
                );

                result.put(
                        q
                );

            } catch (Exception ignored) {
            }
        }

        return result;
    }

    private void collectEditTexts(
            View view,
            ArrayList<EditText> list
    ) {

        if (
                view instanceof EditText
        ) {

            list.add(
                    (EditText) view
            );

            return;
        }

        if (
                view instanceof LinearLayout
        ) {

            LinearLayout layout =
                    (LinearLayout) view;

            for (
                    int i = 0;
                    i < layout.getChildCount();
                    i++
            ) {

                collectEditTexts(
                        layout.getChildAt(i),
                        list
                );
            }
        }
    }

    // =========================================================
    // تعديل الامتحان
    // =========================================================

    private void editExamScreen(
            String id
    ) {

        JSONObject exam =
                findExam(id);

        if (
                exam == null
        ) {
            toast(
                    "الامتحان غير موجود"
            );
            return;
        }

        createScreen();

        header(
                "تعديل الامتحان",
                exam.optString(
                        "title"
                )
        );

        EditText title =
                input(
                        "اسم الامتحان"
                );

        EditText duration =
                input(
                        "مدة الامتحان بالدقائق"
                );

        title.setText(
                exam.optString(
                        "title"
                )
        );

        duration.setText(
                String.valueOf(
                        exam.optInt(
                                "duration",
                                30
                        )
                )
        );

        content.addView(
                title,
                margins(
                        -1,
                        dp(70),
                        10,
                        5,
                        10,
                        5
                )
        );

        content.addView(
                duration,
                margins(
                        -1,
                        dp(70),
                        10,
                        5,
                        10,
                        10
                )
        );

        LinearLayout questionsContainer =
                new LinearLayout(this);

        questionsContainer.setOrientation(
                LinearLayout.VERTICAL
        );

        content.addView(
                text(
                        "❓ جميع الأسئلة",
                        23,
                        GOLD,
                        true
                )
        );

        content.addView(
                questionsContainer
        );

        JSONArray questions =
                exam.optJSONArray(
                        "questions"
                );

        if (
                questions != null
        ) {

            for (
                    int i = 0;
                    i < questions.length();
                    i++
            ) {

                try {

                    addExistingQuestionEditor(
                            questionsContainer,
                            i + 1,
                            questions.getJSONObject(i)
                    );

                } catch (Exception ignored) {
                }
            }
        }

        if (
                questionsContainer
                        .getChildCount() == 0
        ) {

            addQuestionEditor(
                    questionsContainer,
                    1
            );
        }

        addMainButton(
                "➕ إضافة سؤال آخر",
                v -> addQuestionEditor(
                        questionsContainer,
                        questionsContainer
                                .getChildCount() + 1
                )
        );

        addMainButton(
                "💾 حفظ كل التعديلات",
                v -> saveEditedExam(
                        id,
                        title,
                        duration,
                        questionsContainer
                )
        );

        addSecondaryButton(
                "↩ رجوع",
                v -> examManager()
        );
    }

    private void addExistingQuestionEditor(
            LinearLayout container,
            int number,
            JSONObject q
    ) {

        LinearLayout questionCard =
                card();

        questionCard.setTag(
                "QUESTION"
        );

        questionCard.addView(
                text(
                        "السؤال رقم "
                                + number,
                        19,
                        GOLD,
                        true
                )
        );

        EditText question =
                input(
                        "السؤال"
                );

        EditText a =
                input(
                        "الاختيار الأول"
                );

        EditText b =
                input(
                        "الاختيار الثاني"
                );

        EditText c =
                input(
                        "الاختيار الثالث"
                );

        EditText d =
                input(
                        "الاختيار الرابع"
                );

        EditText answer =
                input(
                        "الإجابة الصحيحة: 1 أو 2 أو 3 أو 4"
                );

        EditText explanation =
                input(
                        "شرح الإجابة"
                );

        question.setText(
                q.optString(
                        "question"
                )
        );

        a.setText(
                q.optString(
                        "a"
                )
        );

        b.setText(
                q.optString(
                        "b"
                )
        );

        c.setText(
                q.optString(
                        "c"
                )
        );

        d.setText(
                q.optString(
                        "d"
                )
        );

        answer.setText(
                String.valueOf(
                        q.optInt(
                                "answer",
                                1
                        )
                )
        );

        explanation.setText(
                q.optString(
                        "explanation",
                        ""
                )
        );

        addEditorField(
                questionCard,
                question,
                100
        );

        addEditorField(
                questionCard,
                a,
                70
        );

        addEditorField(
                questionCard,
                b,
                70
        );

        addEditorField(
                questionCard,
                c,
                70
        );

        addEditorField(
                questionCard,
                d,
                70
        );

        addEditorField(
                questionCard,
                answer,
                70
        );

        addEditorField(
                questionCard,
                explanation,
                100
        );

        container.addView(
                questionCard,
                margins(
                        -1,
                        -2,
                        0,
                        7,
                        0,
                        7
                )
        );
    }

    private void saveEditedExam(
            String id,
            EditText title,
            EditText duration,
            LinearLayout questionsContainer
    ) {

        JSONObject exam =
                findExam(id);

        if (
                exam == null
        ) {
            return;
        }

        String name =
                title.getText()
                        .toString()
                        .trim();

        if (
                name.isEmpty()
        ) {

            title.setError(
                    "اكتب اسم الامتحان"
            );

            return;
        }

        JSONArray questions =
                collectQuestions(
                        questionsContainer
                );

        if (
                questions.length() == 0
        ) {

            toast(
                    "لازم يكون عندك سؤال واحد على الأقل"
            );

            return;
        }

        try {

            exam.put(
                    "title",
                    name
            );

            exam.put(
                    "duration",
                    Math.max(
                            1,
                            parseInt(
                                    duration.getText()
                                            .toString(),
                                    30
                            )
                    )
            );

            exam.put(
                    "questions",
                    questions
            );

            updateExam(
                    exam
            );

            toast(
                    "تم حفظ الامتحان والتعديلات"
            );

            examManager();

        } catch (Exception e) {

            toast(
                    "حدث خطأ أثناء الحفظ"
            );
        }
    }

    // =========================================================
    // مدير الأسئلة
    // =========================================================

    private void questionManager() {

        createScreen();

        header(
                "الأسئلة",
                "اختار الامتحان لتعديل أسئلته"
        );

        JSONArray exams =
                getArray(
                        KEY_EXAMS
                );

        if (
                exams.length() == 0
        ) {

            content.addView(
                    text(
                            "لا توجد امتحانات. أنشئ امتحانًا أولًا.",
                            17,
                            secondaryForeground(),
                            true
                    )
            );
        }

        for (
                int i = 0;
                i < exams.length();
                i++
        ) {

            try {

                JSONObject exam =
                        exams.getJSONObject(i);

                String id =
                        exam.optString(
                                "id"
                        );

                addSecondaryButton(
                        "❓ "
                                + exam.optString(
                                "title"
                        ),
                        v -> editExamScreen(
                                id
                        )
                );

            } catch (Exception ignored) {
            }
        }

        teacherBottomNavigation(
                4
        );
    }

    // =========================================================
    // الأكواد
    // =========================================================

    private void codes() {

        createScreen();

        header(
                "أكواد الامتحانات",
                "أنشئ كودًا لأي امتحان"
        );

        addMainButton(
                "➕ إنشاء كود",
                v -> chooseExamForCode()
        );

        JSONArray codes =
                getArray(
                        KEY_CODES
                );

        if (
                codes.length() == 0
        ) {

            content.addView(
                    text(
                            "لا توجد أكواد حتى الآن.",
                            17,
                            secondaryForeground(),
                            true
                    )
            );
        }

        for (
                int i = 0;
                i < codes.length();
                i++
        ) {

            try {

                JSONObject code =
                        codes.getJSONObject(i);

                LinearLayout c =
                        card();

                c.addView(
                        text(
                                "🔑 "
                                        + code.optString(
                                        "code"
                                ),
                                21,
                                GOLD,
                                true
                        )
                );

                c.addView(
                        text(
                                "الامتحان: "
                                        + code.optString(
                                        "exam"
                                ),
                                15,
                                foreground(),
                                false
                        )
                );

                content.addView(
                        c,
                        margins(
                                -1,
                                -2,
                                10,
                                6,
                                10,
                                6
                        )
                );

            } catch (Exception ignored) {
            }
        }

        teacherBottomNavigation(
                4
        );
    }

    private void chooseExamForCode() {

        JSONArray exams =
                getArray(
                        KEY_EXAMS
                );

        if (
                exams.length() == 0
        ) {

            toast(
                    "أنشئ امتحانًا أولًا"
            );

            return;
        }

        ArrayList<String> names =
                new ArrayList<>();

        ArrayList<String> ids =
                new ArrayList<>();

        for (
                int i = 0;
                i < exams.length();
                i++
        ) {

            try {

                JSONObject e =
                        exams.getJSONObject(i);

                names.add(
                        e.optString(
                                "title"
                        )
                );

                ids.add(
                        e.optString(
                                "id"
                        )
                );

            } catch (Exception ignored) {
            }
        }

        String[] items =
                names.toArray(
                        new String[0]
                );

        new AlertDialog.Builder(this)
                .setTitle(
                        "اختار الامتحان"
                )
                .setItems(
                        items,
                        (dialog, which) ->
                                addCodeForExam(
                                        ids.get(which)
                                )
                )
                .show();
    }

    private void addCodeForExam(
            String examId
    ) {

        JSONObject exam =
                findExam(examId);

        if (
                exam == null
        ) {
            return;
        }

        EditText code =
                input(
                        "اكتب كود الامتحان"
                );

        new AlertDialog.Builder(this)
                .setTitle(
                        "إنشاء كود"
                )
                .setView(
                        code
                )
                .setPositiveButton(
                        "حفظ",
                        (dialog, which) -> {

                            String value =
                                    code.getText()
                                            .toString()
                                            .trim();

                            if (
                                    value.isEmpty()
                            ) {

                                toast(
                                        "اكتب الكود"
                                );

                                return;
                            }

                            JSONArray codes =
                                    getArray(
                                            KEY_CODES
                                    );

                            JSONObject obj =
                                    new JSONObject();

                            try {

                                obj.put(
                                        "code",
                                        value
                                );

                                obj.put(
                                        "exam",
                                        exam.optString(
                                                "title"
                                        )
                                );

                                obj.put(
                                        "examId",
                                        examId
                                );

                                codes.put(
                                        obj
                                );

                                saveArray(
                                        KEY_CODES,
                                        codes
                                );

                                toast(
                                        "تم إنشاء الكود"
                                );

                                codes();

                            } catch (Exception ignored) {
                            }
                        }
                )
                .setNegativeButton(
                        "إلغاء",
                        null
                )
                .show();
    }

    // =========================================================
    // نتائج المدرس
    // =========================================================

    private void teacherResults() {

        createScreen();

        header(
                "الطلاب والنتائج",
                "كل نتائج الامتحانات"
        );

        JSONArray results =
                getArray(
                        KEY_RESULTS
                );

        if (
                results.length() == 0
        ) {

            content.addView(
                    text(
                            "لا توجد نتائج حتى الآن.",
                            17,
                            secondaryForeground(),
                            true
                    )
            );
        }

        for (
                int i = 0;
                i < results.length();
                i++
        ) {

            try {

                JSONObject r =
                        results.getJSONObject(i);

                LinearLayout c =
                        card();

                c.addView(
                        text(
                                "👤 "
                                        + r.optString(
                                        "student"
                                ),
                                19,
                                GOLD,
                                true
                        )
                );

                c.addView(
                        text(
                                "الامتحان: "
                                        + r.optString(
                                        "exam"
                                )
                                        + "\n"
                                        + "الدرجة: "
                                        + r.optInt(
                                        "correct"
                                )
                                        + " / "
                                        + r.optInt(
                                        "total"
                                )
                                        + "\n"
                                        + "تمت الإجابة: "
                                        + r.optInt(
                                        "answered"
                                )
                                        + "\n"
                                        + "الأخطاء: "
                                        + r.optInt(
                                        "wrong"
                                ),
                                15,
                                foreground(),
                                false
                        )
                );

                content.addView(
                        c,
                        margins(
                                -1,
                                -2,
                                10,
                                6,
                                10,
                                6
                        )
                );

            } catch (Exception ignored) {
            }
        }

        teacherBottomNavigation(
                2
        );
    }

    // =========================================================
    // المذكرات للمدرس
    // =========================================================

    private void teacherNotes() {

        createScreen();

        header(
                "إدارة المذكرات",
                "إضافة المذكرات والمرفقات من الهاتف"
        );

        addMainButton(
                "➕ إضافة مذكرة",
                v -> createNote()
        );

        JSONArray notes =
                getArray(
                        KEY_NOTES
                );

        if (
                notes.length() == 0
        ) {

            content.addView(
                    text(
                            "لا توجد مذكرات حتى الآن.",
                            17,
                            secondaryForeground(),
                            true
                    )
            );
        }

        for (
                int i = 0;
                i < notes.length();
                i++
        ) {

            try {

                JSONObject note =
                        notes.getJSONObject(i);

                LinearLayout c =
                        card();

                c.addView(
                        text(
                                "📖 "
                                        + note.optString(
                                        "title"
                                ),
                                20,
                                GOLD,
                                true
                        )
                );

                c.addView(
                        text(
                                note.optString(
                                        "description",
                                        ""
                                ),
                                15,
                                foreground(),
                                false
                        )
                );

                String uri =
                        note.optString(
                                "uri",
                                ""
                        );

                if (
                        !uri.isEmpty()
                ) {

                    addCardButton(
                            c,
                            "📂 فتح المرفق",
                            v -> openUri(
                                    uri
                            )
                    );
                }

                String noteId =
                        note.optString(
                                "id"
                        );

                addCardButton(
                        c,
                        "🗑️ حذف المذكرة",
                        v -> deleteNote(
                                noteId
                        )
                );

                content.addView(
                        c,
                        margins(
                                -1,
                                -2,
                                10,
                                6,
                                10,
                                6
                        )
                );

            } catch (Exception ignored) {
            }
        }

        teacherBottomNavigation(
                3
        );
    }

    // =========================================================
    // إنشاء مذكرة
    // =========================================================

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

        EditText description =
                input(
                        "وصف المذكرة"
                );

        box.addView(
                title,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(70)
                )
        );

        box.addView(
                description,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(100)
                )
        );

        new AlertDialog.Builder(this)
                .setTitle(
                        "إضافة مذكرة"
                )
                .setView(
                        box
                )
                .setPositiveButton(
                        "اختيار المرفق",
                        (dialog, which) -> {

                            pendingNoteTitle =
                                    title.getText()
                                            .toString()
                                            .trim();

                            pendingNoteDescription =
                                    description
                                            .getText()
                                            .toString()
                                            .trim();

                            if (
                                    pendingNoteTitle
                                            .isEmpty()
                            ) {

                                toast(
                                        "اكتب عنوان المذكرة أولًا"
                                );

                                return;
                            }

                            Intent intent =
                                    new Intent(
                                            Intent.ACTION_OPEN_DOCUMENT
                                    );

                            intent.addCategory(
                                    Intent.CATEGORY_OPENABLE
                            );

                            intent.setType(
                                    "*/*"
                            );

                            startActivityForResult(
                                    intent,
                                    PICK_NOTE_FILE
                            );
                        }
                )
                .setNegativeButton(
                        "إلغاء",
                        null
                )
                .show();
    }

    // =========================================================
    // الفيديوهات للمدرس
    // =========================================================

    private void teacherVideos() {

        createScreen();

        header(
                "إدارة الفيديوهات",
                "فيديوهات بأي مدة تريدها"
        );

        addMainButton(
                "➕ إضافة فيديو",
                v -> createVideo()
        );

        JSONArray videos =
                getArray(
                        KEY_VIDEOS
                );

        if (
                videos.length() == 0
        ) {

            content.addView(
                    text(
                            "لا توجد فيديوهات حتى الآن.",
                            17,
                            secondaryForeground(),
                            true
                    )
            );
        }

        for (
                int i = 0;
                i < videos.length();
                i++
        ) {

            try {

                JSONObject video =
                        videos.getJSONObject(i);

                LinearLayout c =
                        card();

                c.addView(
                        text(
                                "🎬 "
                                        + video.optString(
                                        "title"
                                ),
                                20,
                                GOLD,
                                true
                        )
                );

                c.addView(
                        text(
                                video.optString(
                                        "description",
                                        ""
                                ),
                                15,
                                foreground(),
                                false
                        )
                );

                String uri =
                        video.optString(
                                "uri",
                                ""
                        );

                addCardButton(
                        c,
                        "▶ تشغيل",
                        v -> playVideo(
                                uri,
                                video.optString(
                                        "title"
                                )
                        )
                );

                String id =
                        video.optString(
                                "id"
                        );

                addCardButton(
                        c,
                        "🗑️ حذف الفيديو",
                        v -> deleteVideo(
                                id
                        )
                );

                content.addView(
                        c,
                        margins(
                                -1,
                                -2,
                                10,
                                6,
                                10,
                                6
                        )
                );

            } catch (Exception ignored) {
            }
        }

        teacherBottomNavigation(
                4
        );
    }

    // =========================================================
    // إنشاء فيديو
    // =========================================================

    private void createVideo() {

        LinearLayout box =
                new LinearLayout(this);

        box.setOrientation(
                LinearLayout.VERTICAL
        );

        EditText title =
                input(
                        "عنوان الفيديو"
                );

        EditText description =
                input(
                        "وصف الفيديو"
                );

        box.addView(
                title,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(70)
                )
        );

        box.addView(
                description,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(100)
                )
        );

        new AlertDialog.Builder(this)
                .setTitle(
                        "إضافة فيديو"
                )
                .setView(
                        box
                )
                .setPositiveButton(
                        "اختيار الفيديو",
                        (dialog, which) -> {

                            pendingVideoTitle =
                                    title.getText()
                                            .toString()
                                            .trim();

                            pendingVideoDescription =
                                    description
                                            .getText()
                                            .toString()
                                            .trim();

                            if (
                                    pendingVideoTitle
                                            .isEmpty()
                            ) {

                                toast(
                                        "اكتب عنوان الفيديو أولًا"
                                );

                                return;
                            }

                            Intent intent =
                                    new Intent(
                                            Intent.ACTION_OPEN_DOCUMENT
                                    );

                            intent.addCategory(
                                    Intent.CATEGORY_OPENABLE
                            );

                            intent.setType(
                                    "video/*"
                            );

                            startActivityForResult(
                                    intent,
                                    PICK_VIDEO_FILE
                            );
                        }
                )
                .setNegativeButton(
                        "إلغاء",
                        null
                )
                .show();
    }

    // =========================================================
    // استقبال الملفات
    // =========================================================

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data
    ) {

        super.onActivityResult(
                requestCode,
                resultCode,
                data
        );

        if (
                resultCode != RESULT_OK ||
                        data == null ||
                        data.getData() == null
        ) {
            return;
        }

        Uri uri =
                data.getData();

        try {

            getContentResolver()
                    .takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                    );

        } catch (Exception ignored) {
        }

        if (
                requestCode ==
                        PICK_NOTE_FILE
        ) {

            JSONArray notes =
                    getArray(
                            KEY_NOTES
                    );

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
                        pendingNoteTitle
                );

                note.put(
                        "description",
                        pendingNoteDescription
                );

                note.put(
                        "uri",
                        uri.toString()
                );

                notes.put(
                        note
                );

                saveArray(
                        KEY_NOTES,
                        notes
                );

                toast(
                        "تمت إضافة المذكرة"
                );

                teacherNotes();

            } catch (Exception ignored) {
            }

            return;
        }

        if (
                requestCode ==
                        PICK_VIDEO_FILE
        ) {

            JSONArray videos =
                    getArray(
                            KEY_VIDEOS
                    );

            JSONObject video =
                    new JSONObject();

            try {

                video.put(
                        "id",
                        UUID.randomUUID()
                                .toString()
                );

                video.put(
                        "title",
                        pendingVideoTitle
                );

                video.put(
                        "description",
                        pendingVideoDescription
                );

                video.put(
                        "uri",
                        uri.toString()
                );

                videos.put(
                        video
                );

                saveArray(
                        KEY_VIDEOS,
                        videos
                );

                toast(
                        "تمت إضافة الفيديو"
                );

                teacherVideos();

            } catch (Exception ignored) {
            }
        }
    }

    // =========================================================
    // تشغيل ملف
    // =========================================================

    private void openUri(
            String uriString
    ) {

        try {

            Intent intent =
                    new Intent(
                            Intent.ACTION_VIEW
                    );

            intent.setData(
                    Uri.parse(
                            uriString
                    )
            );

            intent.addFlags(
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
            );

            startActivity(
                    intent
            );

        } catch (Exception e) {

            toast(
                    "لا يوجد تطبيق مناسب لفتح الملف"
            );
        }
    }

    // =========================================================
    // تشغيل فيديو داخل التطبيق
    // =========================================================

    private void playVideo(
            String uriString,
            String title
    ) {

        if (
                uriString == null ||
                        uriString.isEmpty()
        ) {

            toast(
                    "الفيديو غير موجود"
            );

            return;
        }

        createScreen();

        header(
                title,
                "مشاهدة الفيديو"
        );

        VideoView videoView =
                new VideoView(this);

        videoView.setVideoURI(
                Uri.parse(
                        uriString
                )
        );

        videoView.setMediaController(
                new android.widget.MediaController(
                        this
                )
        );

        videoView.setOnPreparedListener(
                mp -> {

                    mp.setLooping(
                            false
                    );

                    videoView.start();
                }
        );

        content.addView(
                videoView,
                margins(
                        -1,
                        dp(260),
                        5,
                        15,
                        5,
                        15
                )
        );

        addSecondaryButton(
                "↩ رجوع",
                v -> studentVideos()
        );
    }

    // =========================================================
    // حذف مذكرة
    // =========================================================

    private void deleteNote(
            String id
    ) {

        new AlertDialog.Builder(this)
                .setTitle(
                        "حذف المذكرة"
                )
                .setMessage(
                        "هل تريد حذف هذه المذكرة؟"
                )
                .setPositiveButton(
                        "حذف",
                        (dialog, which) -> {

                            JSONArray notes =
                                    getArray(
                                            KEY_NOTES
                                    );

                            JSONArray result =
                                    new JSONArray();

                            for (
                                    int i = 0;
                                    i < notes.length();
                                    i++
                            ) {

                                try {

                                    JSONObject note =
                                            notes.getJSONObject(
                                                    i
                                            );

                                    if (
                                            !id.equals(
                                                    note.optString(
                                                            "id"
                                                    )
                                            )
                                    ) {

                                        result.put(
                                                note
                                        );
                                    }

                                } catch (Exception ignored) {
                                }
                            }

                            saveArray(
                                    KEY_NOTES,
                                    result
                            );

                            teacherNotes();
                        }
                )
                .setNegativeButton(
                        "إلغاء",
                        null
                )
                .show();
    }

    // =========================================================
    // حذف فيديو
    // =========================================================

    private void deleteVideo(
            String id
    ) {

        new AlertDialog.Builder(this)
                .setTitle(
                        "حذف الفيديو"
                )
                .setMessage(
                        "هل تريد حذف هذا الفيديو؟"
                )
                .setPositiveButton(
                        "حذف",
                        (dialog, which) -> {

                            JSONArray videos =
                                    getArray(
                                            KEY_VIDEOS
                                    );

                            JSONArray result =
                                    new JSONArray();

                            for (
                                    int i = 0;
                                    i < videos.length();
                                    i++
                            ) {

                                try {

                                    JSONObject video =
                                            videos.getJSONObject(
                                                    i
                                            );

                                    if (
                                            !id.equals(
                                                    video.optString(
                                                            "id"
                                                    )
                                            )
                                    ) {

                                        result.put(
                                                video
                                        );
                                    }

                                } catch (Exception ignored) {
                                }
                            }

                            saveArray(
                                    KEY_VIDEOS,
                                    result
                            );

                            teacherVideos();
                        }
                )
                .setNegativeButton(
                        "إلغاء",
                        null
                )
                .show();
    }

    // =========================================================
    // المجموعات
    // =========================================================

    private void groups() {

        createScreen();

        header(
                "مجموعات الطلاب",
                "تنظيم الطلاب"
        );

        String[] groups = {
                "جروب أولى بكالوريا بنات",
                "جروب أولى بكالوريا ولاد",
                "جروب تانية بكالوريا بنات",
                "جروب تانية بكالوريا ولاد",
                "طلاب المايسترو تالتة إعدادي",
                "جروب ذكرى ومنفعة"
        };

        for (
                String group : groups
        ) {

            addSecondaryButton(
                    "👥 " + group,
                    v -> groupDetails(
                            group
                    )
            );
        }

        teacherBottomNavigation(
                4
        );
    }

    private void groupDetails(
            String group
    ) {

        createScreen();

        header(
                group,
                "طلاب المجموعة"
        );

        content.addView(
                text(
                        "المجموعة جاهزة لإضافة وتنظيم الطلاب والنتائج.\n"
                                + "يمكن ربطها لاحقًا بالمزامنة السحابية.",
                        17,
                        foreground(),
                        false
                )
        );

        addSecondaryButton(
                "↩ رجوع",
                v -> groups()
        );
    }

    // =========================================================
    // تحديثات المدرس
    // =========================================================

    private void updatesManager() {

        createScreen();

        header(
                "الإعلانات والتحديثات",
                "رسالة تظهر للطلاب"
        );

        EditText update =
                input(
                        "اكتب التحديث"
                );

        update.setText(
                prefs.getString(
                        KEY_UPDATE,
                        ""
                )
        );

        content.addView(
                update,
                margins(
                        -1,
                        dp(130),
                        10,
                        10,
                        10,
                        10
                )
        );

        addMainButton(
                "📢 حفظ التحديث",
                v -> {

                    prefs.edit()
                            .putString(
                                    KEY_UPDATE,
                                    update.getText()
                                            .toString()
                            )
                            .apply();

                    toast(
                            "تم حفظ التحديث"
                    );

                    teacherHome();
                }
        );

        teacherBottomNavigation(
                4
        );
    }

    // =========================================================
    // إعدادات المدرس
    // =========================================================

    private void teacherSettings() {

        createScreen();

        header(
                "إعدادات المدرس",
                "إعدادات لوحة التحكم"
        );

        LinearLayout profile =
                card();

        profile.addView(
                text(
                        "👨‍🏫 المدرس الحالي",
                        20,
                        GOLD,
                        true
                )
        );

        profile.addView(
                text(
                        currentTeacher,
                        17,
                        foreground(),
                        false
                )
        );

        content.addView(
                profile,
                margins(
                        -1,
                        -2,
                        10,
                        10,
                        10,
                        10
                )
        );

        addMainButton(
                darkMode
                        ? "☀️ الوضع الفاتح"
                        : "🌙 الوضع الداكن",
                v -> {

                    darkMode =
                            !darkMode;

                    prefs.edit()
                            .putBoolean(
                                    KEY_DARK,
                                    darkMode
                            )
                            .apply();

                    applyWindowColors();

                    teacherSettings();
                }
        );

        addSecondaryButton(
                "📢 تعديل التحديث",
                v -> updatesManager()
        );

        addSecondaryButton(
                "ℹ️ عن التطبيق",
                v -> about()
        );

        addSecondaryButton(
                "🚪 تسجيل خروج",
                v -> showRoleScreen()
        );

        teacherBottomNavigation(
                4
        );
    }

    // =========================================================
    // عن التطبيق
    // =========================================================

    private void about() {

        createScreen();

        header(
                "عن المايسترو",
                "منصة تعليمية"
        );

        LinearLayout c =
                card();

        c.addView(
                text(
                        "المايسترو شريف هيبه",
                        23,
                        GOLD,
                        true
                )
        );

        c.addView(
                text(
                        "هتتعلم التاريخ ببساطة\n\n"
                                + "منصة للامتحانات والمراجعة "
                                + "والمذكرات والفيديوهات والأذكار.\n\n"
                                + "مع المبرمج أو المطور محمود كليب\n"
                                + "للتواصل: 01112244710",
                        17,
                        foreground(),
                        false
                )
        );

        content.addView(
                c,
                margins(
                        -1,
                        -2,
                        10,
                        15,
                        10,
                        15
                )
        );

        addSecondaryButton(
                "↩ رجوع",
                v -> showRoleScreen()
        );
    }

    // =========================================================
    // بدء الامتحان
    // =========================================================

    private void startExam(
            JSONObject exam
    ) {

        currentExam =
                exam;

        currentExamId =
                exam.optString(
                        "id"
                );

        currentQuestions =
                exam.optJSONArray(
                        "questions"
                );

        if (
                currentQuestions == null ||
                        currentQuestions.length() == 0
        ) {

            toast(
                    "لا توجد أسئلة"
            );

            return;
        }

        studentAnswers.clear();

        for (
                int i = 0;
                i < currentQuestions.length();
                i++
        ) {

            studentAnswers.add(
                    -1
            );
        }

        currentQuestion = 0;

        examRunning = true;
        examSubmitted = false;

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
        );

        showExamQuestion();

        int minutes =
                Math.max(
                        1,
                        exam.optInt(
                                "duration",
                                30
                        )
                );

        examTimer =
                new CountDownTimer(
                        minutes * 60L * 1000L,
                        1000L
                ) {

                    @Override
                    public void onTick(
                            long millisUntilFinished
                    ) {
                    }

                    @Override
                    public void onFinish() {

                        if (
                                examRunning &&
                                        !examSubmitted
                        ) {

                            submitExam(
                                    true
                            );
                        }
                    }
                };

        examTimer.start();
    }

    // =========================================================
    // سؤال الامتحان
    // =========================================================

    private void showExamQuestion() {

        createScreen();

        if (
                currentQuestions == null ||
                        currentQuestion >=
                                currentQuestions.length()
        ) {

            submitExam(
                    false
            );

            return;
        }

        try {

            JSONObject q =
                    currentQuestions
                            .getJSONObject(
                                    currentQuestion
                            );

            String examTitle =
                    currentExam.optString(
                            "title"
                    );

            header(
                    examTitle,
                    "السؤال "
                            + (
                            currentQuestion + 1
                    )
                            + " من "
                            + currentQuestions.length()
            );

            LinearLayout qCard =
                    card();

            qCard.addView(
                    text(
                            q.optString(
                                    "question"
                            ),
                            21,
                            foreground(),
                            true
                    )
            );

            RadioGroup group =
                    new RadioGroup(this);

            group.setOrientation(
                    RadioGroup.VERTICAL
            );

            addRadio(
                    group,
                    1,
                    "أ) "
                            + q.optString(
                            "a"
                    )
            );

            addRadio(
                    group,
                    2,
                    "ب) "
                            + q.optString(
                            "b"
                    )
            );

            addRadio(
                    group,
                    3,
                    "ج) "
                            + q.optString(
                            "c"
                    )
            );

            addRadio(
                    group,
                    4,
                    "د) "
                            + q.optString(
                            "d"
                    )
            );

            int saved =
                    studentAnswers.get(
                            currentQuestion
                    );

            if (
                    saved >= 1
            ) {

                RadioButton rb =
                        group.findViewById(
                                saved
                        );

                if (
                        rb != null
                ) {
                    rb.setChecked(
                            true
                    );
                }
            }

            qCard.addView(
                    group,
                    margins(
                            -1,
                            -2,
                            0,
                            12,
                            0,
                            8
                    )
            );

            content.addView(
                    qCard,
                    margins(
                            -1,
                            -2,
                            8,
                            10,
                            8,
                            10
                    )
            );

            LinearLayout navigation =
                    new LinearLayout(this);

            navigation.setOrientation(
                    LinearLayout.HORIZONTAL
            );

            if (
                    currentQuestion > 0
            ) {

                TextView previous =
                        secondaryButton(
                                "⬅ السابق"
                        );

                previous.setOnClickListener(
                        v -> {

                            saveCurrentAnswer(
                                    group
                            );

                            currentQuestion--;

                            showExamQuestion();
                        }
                );

                navigation.addView(
                        previous,
                        new LinearLayout.LayoutParams(
                                0,
                                dp(58),
                                1f
                        )
                );
            }

            TextView next =
                    mainButton(
                            currentQuestion ==
                                    currentQuestions.length()
                                            - 1
                                    ? "تسليم الامتحان"
                                    : "التالي ➡"
                    );

            next.setOnClickListener(
                    v -> {

                        saveCurrentAnswer(
                                group
                        );

                        if (
                                currentQuestion ==
                                        currentQuestions.length()
                                                - 1
                        ) {

                            confirmSubmit();

                        } else {

                            currentQuestion++;

                            showExamQuestion();
                        }
                    }
            );

            navigation.addView(
                    next,
                    new LinearLayout.LayoutParams(
                            0,
                            dp(58),
                            1f
                    )
            );

            content.addView(
                    navigation,
                    margins(
                            -1,
                            dp(65),
                            8,
                            10,
                            8,
                            15
                    )
            );

        } catch (Exception e) {

            toast(
                    "حدث خطأ في عرض السؤال"
            );
        }
    }

    private void addRadio(
            RadioGroup group,
            int id,
            String title
    ) {

        RadioButton rb =
                new RadioButton(this);

        rb.setId(
                id
        );

        rb.setText(
                title
        );

        rb.setTextSize(
                17
        );

        rb.setTextColor(
                foreground()
        );

        rb.setGravity(
                Gravity.RIGHT |
                        Gravity.CENTER_VERTICAL
        );

        rb.setPadding(
                dp(8),
                dp(12),
                dp(8),
                dp(12)
        );

        group.addView(
                rb,
                new RadioGroup.LayoutParams(
                        -1,
                        -2
                )
        );
    }

    private void saveCurrentAnswer(
            RadioGroup group
    ) {

        int checked =
                group.getCheckedRadioButtonId();

        studentAnswers.set(
                currentQuestion,
                checked
        );
    }

    // =========================================================
    // تأكيد التسليم
    // =========================================================

    private void confirmSubmit() {

        new AlertDialog.Builder(this)
                .setTitle(
                        "تسليم الامتحان"
                )
                .setMessage(
                        "هل أنت متأكد من تسليم الامتحان؟"
                )
                .setPositiveButton(
                        "تسليم",
                        (dialog, which) ->
                                submitExam(
                                        false
                                )
                )
                .setNegativeButton(
                        "متابعة",
                        null
                )
                .show();
    }

    // =========================================================
    // تسليم الامتحان
    // =========================================================

    private void submitExam(
            boolean automatic
    ) {

        if (
                examSubmitted
        ) {
            return;
        }

        examSubmitted = true;
        examRunning = false;

        stopExamTimer();

        getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_SECURE
        );

        if (
                currentQuestions == null
        ) {
            return;
        }

        int total =
                currentQuestions.length();

        int answered = 0;
        int correct = 0;
        int wrong = 0;

        StringBuilder mistakes =
                new StringBuilder();

        for (
                int i = 0;
                i < total;
                i++
        ) {

            try {

                JSONObject q =
                        currentQuestions
                                .getJSONObject(
                                        i
                                );

                int selected =
                        studentAnswers.get(
                                i
                        );

                int answer =
                        q.optInt(
                                "answer",
                                -1
                        );

                if (
                        selected == -1
                ) {
                    continue;
                }

                answered++;

                if (
                        selected == answer
                ) {

                    correct++;

                } else {

                    wrong++;

                    mistakes.append(
                            "\nالسؤال "
                                    + (
                                    i + 1
                            )
                                    + ": "
                                    + q.optString(
                                    "question"
                            )
                                    + "\nالتصحيح: الاختيار "
                                    + answer
                                    + "\n"
                                    + q.optString(
                                    "explanation",
                                    ""
                            )
                                    + "\n"
                    );
                }

            } catch (Exception ignored) {
            }
        }

        JSONObject result =
                new JSONObject();

        try {

            result.put(
                    "student",
                    currentStudent
            );

            result.put(
                    "exam",
                    currentExam.optString(
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
                    total
            );

            result.put(
                    "mistakes",
                    mistakes.toString()
            );

            result.put(
                    "automatic",
                    automatic
            );

            JSONArray results =
                    getArray(
                            KEY_RESULTS
                    );

            results.put(
                    result
            );

            saveArray(
                    KEY_RESULTS,
                    results
            );

        } catch (Exception ignored) {
        }

        showResult(
                correct,
                wrong,
                answered,
                total,
                mistakes.toString(),
                automatic
        );
    }

    // =========================================================
    // نتيجة الامتحان
    // =========================================================

    private void showResult(
            int correct,
            int wrong,
            int answered,
            int total,
            String mistakes,
            boolean automatic
    ) {

        createScreen();

        header(
                "نتيجة الامتحان",
                currentExam.optString(
                        "title"
                )
        );

        LinearLayout resultCard =
                card();

        resultCard.setGravity(
                Gravity.CENTER
        );

        resultCard.addView(
                text(
                        "🏆",
                        48,
                        GOLD,
                        false
                )
        );

        resultCard.addView(
                text(
                        correct
                                + " / "
                                + total,
                        34,
                        GOLD,
                        true
                )
        );

        resultCard.addView(
                text(
                        "الإجابة: "
                                + answered
                                + "\n"
                                + "الصحيح: "
                                + correct
                                + "\n"
                                + "الخطأ: "
                                + wrong
                                + "\n"
                                + "بدون إجابة: "
                                + (
                                total - answered
                        ),
                        17,
                        foreground(),
                        false
                )
        );

        if (
                automatic
        ) {

            resultCard.addView(
                    text(
                            "⏱️ تم تسليم الامتحان تلقائيًا لانتهاء الوقت.",
                            15,
                            GOLD_LIGHT,
                            true
                    )
            );
        }

        content.addView(
                resultCard,
                margins(
                        -1,
                        -2,
                        10,
                        10,
                        10,
                        10
                )
        );

        if (
                !mistakes.isEmpty()
        ) {

            LinearLayout mistakesCard =
                    card();

            mistakesCard.addView(
                    text(
                            "📌 مراجعة الأخطاء",
                            21,
                            GOLD,
                            true
                    )
            );

            mistakesCard.addView(
                    text(
                            mistakes,
                            15,
                            foreground(),
                            false
                    )
            );

            content.addView(
                    mistakesCard,
                    margins(
                            -1,
                            -2,
                            10,
                            6,
                            10,
                            10
                    )
            );
        }

        addMainButton(
                "📊 امتحاناتي",
                v -> studentResults()
        );

        addSecondaryButton(
                "🏠 الرئيسية",
                v -> studentHome()
        );
    }

    // =========================================================
    // المؤقت
    // =========================================================

    private void stopExamTimer() {

        if (
                examTimer != null
        ) {

            examTimer.cancel();

            examTimer = null;
        }
    }

    // =========================================================
    // حذف امتحان
    // =========================================================

    private void deleteExamConfirm(
            String id
    ) {

        new AlertDialog.Builder(this)
                .setTitle(
                        "حذف الامتحان"
                )
                .setMessage(
                        "سيتم حذف الامتحان وأسئلته. هل أنت متأكد؟"
                )
                .setPositiveButton(
                        "حذف",
                        (dialog, which) -> {

                            JSONArray exams =
                                    getArray(
                                            KEY_EXAMS
                                    );

                            JSONArray result =
                                    new JSONArray();

                            for (
                                    int i = 0;
                                    i < exams.length();
                                    i++
                            ) {

                                try {

                                    JSONObject exam =
                                            exams.getJSONObject(
                                                    i
                                            );

                                    if (
                                            !id.equals(
                                                    exam.optString(
                                                            "id"
                                                    )
                                            )
                                    ) {

                                        result.put(
                                                exam
                                        );
                                    }

                                } catch (Exception ignored) {
                                }
                            }

                            saveArray(
                                    KEY_EXAMS,
                                    result
                            );

                            examManager();
                        }
                )
                .setNegativeButton(
                        "إلغاء",
                        null
                )
                .show();
    }

    // =========================================================
    // بيانات الامتحانات
    // =========================================================

    private JSONObject findExam(
            String id
    ) {

        JSONArray exams =
                getArray(
                        KEY_EXAMS
                );

        for (
                int i = 0;
                i < exams.length();
                i++
        ) {

            try {

                JSONObject exam =
                        exams.getJSONObject(
                                i
                        );

                if (
                        id.equals(
                                exam.optString(
                                        "id"
                                )
                        )
                ) {

                    return exam;
                }

            } catch (Exception ignored) {
            }
        }

        return null;
    }

    private JSONObject findExamByCode(
            String code
    ) {

        JSONArray codes =
                getArray(
                        KEY_CODES
                );

        for (
                int i = 0;
                i < codes.length();
                i++
        ) {

            try {

                JSONObject c =
                        codes.getJSONObject(
                                i
                        );

                if (
                        code.equalsIgnoreCase(
                                c.optString(
                                        "code"
                                )
                        )
                ) {

                    return findExam(
                            c.optString(
                                    "examId"
                            )
                    );
                }

            } catch (Exception ignored) {
            }
        }

        return null;
    }

    private void updateExam(
            JSONObject updated
    ) {

        JSONArray exams =
                getArray(
                        KEY_EXAMS
                );

        for (
                int i = 0;
                i < exams.length();
                i++
        ) {

            try {

                JSONObject old =
                        exams.getJSONObject(
                                i
                        );

                if (
                        old.optString(
                                "id"
                        )
                                .equals(
                                        updated.optString(
                                                "id"
                                        )
                                )
                ) {

                    exams.put(
                            i,
                            updated
                    );

                    break;
                }

            } catch (Exception ignored) {
            }
        }

        saveArray(
                KEY_EXAMS,
                exams
        );
    }

    // =========================================================
    // الأكواد المستخدمة
    // =========================================================

    private boolean isCodeUsed(
            String key
    ) {

        Set<String> used =
                prefs.getStringSet(
                        KEY_USED_CODES,
                        new HashSet<>()
                );

        return used.contains(
                key
        );
    }

    private void markCodeUsed(
            String key
    ) {

        Set<String> old =
                prefs.getStringSet(
                        KEY_USED_CODES,
                        new HashSet<>()
                );

        Set<String> used =
                new HashSet<>(
                        old
                );

        used.add(
                key
        );

        prefs.edit()
                .putStringSet(
                        KEY_USED_CODES,
                        used
                )
                .apply();
    }

    // =========================================================
    // JSON STORAGE
    // =========================================================

    private JSONArray getArray(
            String key
    ) {

        String raw =
                prefs.getString(
                        key,
                        "[]"
                );

        try {

            return new JSONArray(
                    raw
            );

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

    // =========================================================
    // أرقام
    // =========================================================

    private int parseInt(
            String value,
            int fallback
    ) {

        try {

            return Integer.parseInt(
                    value.trim()
            );

        } catch (Exception e) {

            return fallback;
        }
    }

    // =========================================================
    // رجوع
    // =========================================================

    private void teacherBack() {

        addSecondaryButton(
                "↩ لوحة المدرس",
                v -> teacherHome()
        );
    }

    // =========================================================
    // زر الرجوع في النظام
    // =========================================================

    @Override
    public void onBackPressed() {

        if (
                examRunning
        ) {

            new AlertDialog.Builder(this)
                    .setTitle(
                            "الامتحان قيد التشغيل"
                    )
                    .setMessage(
                            "لا يمكنك الخروج من الامتحان قبل التسليم."
                    )
                    .setPositiveButton(
                            "حسنًا",
                            null
                    )
                    .show();

            return;
        }

        super.onBackPressed();
    }

    // =========================================================
    // تنظيف المؤقت
    // =========================================================

    @Override
    protected void onDestroy() {

        stopExamTimer();

        super.onDestroy();
    }
}
