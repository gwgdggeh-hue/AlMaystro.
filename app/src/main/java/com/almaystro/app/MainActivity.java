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
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends Activity {

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

    private SharedPreferences prefs;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    private boolean darkMode = false;

    private int GOLD = Color.rgb(212, 175, 55);
    private int DARK_GREEN = Color.rgb(18, 61, 43);
    private int GREEN = Color.rgb(35, 94, 65);
    private int WHITE = Color.WHITE;
    private int BLACK = Color.rgb(20, 20, 20);
    private int GRAY = Color.rgb(110, 110, 110);
    private int LIGHT_GRAY = Color.rgb(245, 245, 245);

    private LinearLayout root;
    private TextView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences(PREFS, MODE_PRIVATE);

        darkMode = prefs.getBoolean(KEY_DARK, false);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            firebaseAuth.signInAnonymously()
                    .addOnFailureListener(e ->
                            Toast.makeText(
                                    MainActivity.this,
                                    "تعذر الاتصال بالسيرفر",
                                    Toast.LENGTH_LONG
                            ).show()
                    );
        }

        showWelcome();
    }

    private void ensureFirebaseAuth(final Runnable action) {

        if (firebaseAuth.getCurrentUser() != null) {
            action.run();
            return;
        }

        firebaseAuth.signInAnonymously()
                .addOnSuccessListener(authResult -> action.run())
                .addOnFailureListener(e ->
                        Toast.makeText(
                                MainActivity.this,
                                "تعذر الاتصال بالسيرفر",
                                Toast.LENGTH_LONG
                        ).show()
                );
    }

    private int bgColor() {
        return darkMode ? Color.rgb(25, 25, 25) : WHITE;
    }

    private int cardColor() {
        return darkMode ? Color.rgb(38, 38, 38) : Color.rgb(250, 250, 250);
    }

    private int textColor() {
        return darkMode ? WHITE : BLACK;
    }

    private int subTextColor() {
        return darkMode ? Color.rgb(205, 205, 205) : GRAY;
    }

    private void applyWindow() {
        Window window = getWindow();

        window.setStatusBarColor(DARK_GREEN);
        window.setNavigationBarColor(BLACK);

        if (darkMode) {
            window.getDecorView().setSystemUiVisibility(0);
        } else {
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            );
        }
    }

    private void baseScreen() {

        applyWindow();

        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(bgColor());

        setContentView(root);
    }

    private TextView text(
            String value,
            float size,
            int color,
            int gravity
    ) {
        TextView t = new TextView(this);

        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setGravity(gravity);
        t.setTypeface(Typeface.DEFAULT);

        t.setPadding(12, 12, 12, 12);

        return t;
    }

    private TextView boldText(
            String value,
            float size,
            int color,
            int gravity
    ) {
        TextView t = text(value, size, color, gravity);

        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        return t;
    }

    private LinearLayout.LayoutParams lp(
            int width,
            int height
    ) {
        return new LinearLayout.LayoutParams(width, height);
    }

    private LinearLayout.LayoutParams lpMargin(
            int width,
            int height,
            int left,
            int top,
            int right,
            int bottom
    ) {
        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(width, height);

        p.setMargins(left, top, right, bottom);

        return p;
    }

    private GradientDrawable drawable(
            int color,
            float radius
    ) {
        GradientDrawable d = new GradientDrawable();

        d.setColor(color);
        d.setCornerRadius(radius);

        return d;
    }

    private GradientDrawable strokeDrawable(
            int color,
            int strokeColor,
            int strokeWidth,
            float radius
    ) {
        GradientDrawable d = new GradientDrawable();

        d.setColor(color);
        d.setStroke(strokeWidth, strokeColor);
        d.setCornerRadius(radius);

        return d;
    }

    private Button button(String value) {

        Button b = new Button(this);

        b.setText(value);
        b.setTextSize(16);
        b.setTextColor(WHITE);
        b.setAllCaps(false);
        b.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        b.setBackground(
                drawable(
                        DARK_GREEN,
                        30
                )
        );

        b.setPadding(20, 10, 20, 10);

        return b;
    }

    private EditText input(
            String hint
    ) {
        EditText e = new EditText(this);

        e.setHint(hint);
        e.setTextSize(16);
        e.setTextColor(textColor());
        e.setHintTextColor(subTextColor());
        e.setGravity(Gravity.RIGHT);

        e.setSingleLine(true);

        e.setPadding(
                20,
                10,
                20,
                10
        );

        e.setBackground(
                strokeDrawable(
                        cardColor(),
                        GOLD,
                        2,
                        25
                )
        );

        return e;
    }

    private ScrollView scroll() {

        ScrollView s = new ScrollView(this);

        s.setFillViewport(true);
        s.setBackgroundColor(bgColor());

        return s;
    }

    private LinearLayout vertical() {

        LinearLayout l = new LinearLayout(this);

        l.setOrientation(LinearLayout.VERTICAL);
        l.setGravity(Gravity.CENTER_HORIZONTAL);

        return l;
    }

    private LinearLayout horizontal() {

        LinearLayout l = new LinearLayout(this);

        l.setOrientation(LinearLayout.HORIZONTAL);
        l.setGravity(Gravity.CENTER_VERTICAL);

        return l;
    }

    private void header(
            String title,
            String subtitle
    ) {

        LinearLayout h = vertical();

        h.setPadding(20, 25, 20, 15);

        TextView t = boldText(
                title,
                27,
                GOLD,
                Gravity.CENTER
        );

        TextView s = text(
                subtitle,
                15,
                subTextColor(),
                Gravity.CENTER
        );

        h.addView(
                t,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        h.addView(
                s,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        root.addView(
                h,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );
    }

    private void showWelcome() {

        baseScreen();

        LinearLayout content = vertical();

        content.setPadding(25, 35, 25, 25);

        SpaceHolder(content, 30);

        ImageView image = new ImageView(this);

        try {
            int id = getResources().getIdentifier(
                    "maestro",
                    "drawable",
                    getPackageName()
            );

            if (id != 0) {
                image.setImageResource(id);
            }
        } catch (Exception ignored) {
        }

        image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        content.addView(
                image,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        220
                )
        );

        TextView title = boldText(
                "المايسترو شريف هيبه",
                29,
                GOLD,
                Gravity.CENTER
        );

        content.addView(
                title,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        0,
                        15,
                        0,
                        0
                )
        );

        TextView subtitle = text(
                "هتتعلم التاريخ ببساطة",
                18,
                subTextColor(),
                Gravity.CENTER
        );

        content.addView(
                subtitle,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        0,
                        5,
                        0,
                        25
                )
        );

        Button start = button("ابدأ الآن");

        start.setOnClickListener(v -> showRoleScreen());

        content.addView(
                start,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        15,
                        0,
                        15
                )
        );

        TextView developer = text(
                "مع المبرمج أو المطور محمود كليب للتواصل",
                13,
                subTextColor(),
                Gravity.CENTER
        );

        content.addView(
                developer,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        root.addView(
                content,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                )
        );
    }

    private void SpaceHolder(
            LinearLayout parent,
            int height
    ) {
        SpaceHolderView(parent, height);
    }

    private void SpaceHolderView(
            LinearLayout parent,
            int height
    ) {
        View v = new View(this);

        parent.addView(
                v,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        height
                )
        );
    }

    private void showRoleScreen() {

        baseScreen();

        header(
                "المايسترو",
                "اختار طريقة الدخول"
        );

        LinearLayout content = vertical();

        content.setPadding(
                25,
                20,
                25,
                25
        );

        LinearLayout studentCard = card();

        TextView st = boldText(
                "👨‍🎓 طالب",
                23,
                GOLD,
                Gravity.CENTER
        );

        TextView stSub = text(
                "الدخول إلى الامتحانات والنتائج",
                14,
                subTextColor(),
                Gravity.CENTER
        );

        studentCard.addView(
                st,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        studentCard.addView(
                stSub,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        studentCard.setOnClickListener(
                v -> studentLogin()
        );

        content.addView(
                studentCard,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        130,
                        0,
                        15,
                        0,
                        15
                )
        );

        LinearLayout teacherCard = card();

        TextView tt = boldText(
                "👨‍🏫 مدرس",
                23,
                GOLD,
                Gravity.CENTER
        );

        TextView ttSub = text(
                "إدارة الامتحانات والطلاب والنتائج",
                14,
                subTextColor(),
                Gravity.CENTER
        );

        teacherCard.addView(
                tt,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        teacherCard.addView(
                ttSub,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        teacherCard.setOnClickListener(
                v -> teacherLogin()
        );

        content.addView(
                teacherCard,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        130,
                        0,
                        15,
                        0,
                        15
                )
        );

        Button settings = button("⚙ الإعدادات");

        settings.setOnClickListener(
                v -> settingsScreen()
        );

        content.addView(
                settings,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        20,
                        0,
                        0
                )
        );

        root.addView(
                content,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                )
        );
    }

    private LinearLayout card() {

        LinearLayout c = vertical();

        c.setGravity(Gravity.CENTER);

        c.setPadding(
                20,
                15,
                20,
                15
        );

        c.setBackground(
                strokeDrawable(
                        cardColor(),
                        GOLD,
                        2,
                        28
                )
        );

        return c;
    }

    private void studentLogin() {

        baseScreen();

        header(
                "دخول الطالب",
                "اكتب اسمك للدخول إلى واجهة الطالب"
        );

        LinearLayout content = vertical();

        content.setPadding(
                25,
                20,
                25,
                25
        );

        EditText name = input(
                "اكتب اسمك"
        );

        name.setText(
                prefs.getString(
                        KEY_STUDENT,
                        ""
                )
        );

        content.addView(
                name,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        15,
                        0,
                        15
                )
        );

        Button enter = button(
                "👨‍🎓 دخول الطالب"
        );

        enter.setOnClickListener(
                v -> {

                    String studentName =
                            name.getText()
                                    .toString()
                                    .trim();

                    if (studentName.isEmpty()) {
                        name.setError(
                                "اكتب اسمك أولًا"
                        );
                        return;
                    }

                    prefs.edit()
                            .putString(
                                    KEY_STUDENT,
                                    studentName
                            )
                            .apply();

                    studentHome();
                }
        );

        content.addView(
                enter,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        10,
                        0,
                        15
                )
        );

        Button back = button(
                "رجوع"
        );

        back.setOnClickListener(
                v -> showRoleScreen()
        );

        content.addView(
                back,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60
                )
        );

        root.addView(
                content,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                )
        );
    }

    private void studentHome() {

        baseScreen();

        header(
                "الرئيسية",
                "أهلًا بك يا " +
                        prefs.getString(
                                KEY_STUDENT,
                                "طالب"
                        )
        );

        LinearLayout content = vertical();

        content.setPadding(
                20,
                10,
                20,
                20
        );

        LinearLayout exam = card();

        TextView e1 = boldText(
                "📝 امتحان جديد",
                21,
                GOLD,
                Gravity.CENTER
        );

        TextView e2 = text(
                "شاهد الامتحانات المتاحة واختر الامتحان",
                14,
                subTextColor(),
                Gravity.CENTER
        );

        exam.addView(
                e1,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        exam.addView(
                e2,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        exam.setOnClickListener(
                v -> availableExams()
        );

        content.addView(
                exam,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        125,
                        0,
                        10,
                        0,
                        10
                )
        );

        LinearLayout results = card();

        TextView r1 = boldText(
                "📊 نتائجي",
                21,
                GOLD,
                Gravity.CENTER
        );

        TextView r2 = text(
                "عرض نتائج الامتحانات السابقة",
                14,
                subTextColor(),
                Gravity.CENTER
        );

        results.addView(
                r1,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        results.addView(
                r2,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        results.setOnClickListener(
                v -> studentResults()
        );

        content.addView(
                results,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        115,
                        0,
                        10,
                        0,
                        10
                )
        );

        LinearLayout notes = card();

        TextView n1 = boldText(
                "📚 مذكرات الشرح",
                21,
                GOLD,
                Gravity.CENTER
        );

        TextView n2 = text(
                "المذكرات المتاحة للطلاب",
                14,
                subTextColor(),
                Gravity.CENTER
        );

        notes.addView(
                n1,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        notes.addView(
                n2,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        notes.setOnClickListener(
                v -> studentNotes()
        );

        content.addView(
                notes,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        115,
                        0,
                        10,
                        0,
                        10
                )
        );

        LinearLayout videos = card();

        TextView v1 = boldText(
                "🎥 الفيديوهات",
                21,
                GOLD,
                Gravity.CENTER
        );

        TextView v2 = text(
                "الفيديوهات التعليمية",
                14,
                subTextColor(),
                Gravity.CENTER
        );

        videos.addView(
                v1,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        videos.addView(
                v2,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        videos.setOnClickListener(
                v -> studentVideos()
        );

        content.addView(
                videos,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        115,
                        0,
                        10,
                        0,
                        10
                )
        );

        LinearLayout azkar = card();

        TextView a1 = boldText(
                "📿 الأذكار",
                21,
                GOLD,
                Gravity.CENTER
        );

        TextView a2 = text(
                "أذكار المسلم",
                14,
                subTextColor(),
                Gravity.CENTER
        );

        azkar.addView(
                a1,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        azkar.addView(
                a2,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        azkar.setOnClickListener(
                v -> azkarScreen()
        );

        content.addView(
                azkar,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        115,
                        0,
                        10,
                        0,
                        10
                )
        );

        Button logout = button(
                "تسجيل الخروج"
        );

        logout.setOnClickListener(
                v -> showRoleScreen()
        );

        content.addView(
                logout,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        20,
                        0,
                        20
                )
        );

        root.addView(
                content,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                )
        );
    }

    private void availableExams() {

        baseScreen();

        header(
                "الامتحانات المتاحة",
                "اختر الامتحان الذي تريد دخوله"
        );

        LinearLayout content = vertical();

        content.setPadding(
                20,
                10,
                20,
                20
        );

        ProgressBar progress = new ProgressBar(this);

        content.addView(
                progress,
                lpMargin(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        0,
                        25,
                        0,
                        25
                )
        );

        root.addView(
                content,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                )
        );

        ensureFirebaseAuth(() -> {

            db.collection("exams")
                    .get()
                    .addOnSuccessListener(snapshot -> {

                        progress.setVisibility(
                                View.GONE
                        );

                        if (snapshot.isEmpty()) {

                            TextView empty = text(
                                    "لا توجد امتحانات متاحة حاليًا",
                                    17,
                                    subTextColor(),
                                    Gravity.CENTER
                            );

                            content.addView(
                                    empty,
                                    lpMargin(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            0,
                                            20,
                                            0,
                                            20
                                    )
                            );

                            return;
                        }

                        for (
                                QueryDocumentSnapshot document
                                : snapshot
                        ) {

                            LinearLayout examCard =
                                    card();

                            String examId =
                                    document.getId();

                            String examName =
                                    document.getString(
                                            "name"
                                    );

                            if (
                                    examName == null ||
                                    examName.trim().isEmpty()
                            ) {
                                examName =
                                        document.getString(
                                                "title"
                                        );
                            }

                            if (
                                    examName == null ||
                                    examName.trim().isEmpty()
                            ) {
                                examName =
                                        "امتحان";
                            }

                            TextView title =
                                    boldText(
                                            "📝 " +
                                                    examName,
                                            20,
                                            GOLD,
                                            Gravity.CENTER
                                    );

                            examCard.addView(
                                    title,
                                    lp(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                    )
                            );

                            String description =
                                    document.getString(
                                            "description"
                                    );

                            if (
                                    description != null &&
                                    !description.trim().isEmpty()
                            ) {

                                TextView desc =
                                        text(
                                                description,
                                                14,
                                                subTextColor(),
                                                Gravity.CENTER
                                        );

                                examCard.addView(
                                        desc,
                                        lp(
                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT
                                        )
                                );
                            }

                            examCard.setOnClickListener(
                                    v ->
                                            askExamCode(
                                                    examId,
                                                    examName
                                            )
                            );

                            content.addView(
                                    examCard,
                                    lpMargin(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            0,
                                            8,
                                            0,
                                            8
                                    )
                            );
                        }
                    })
                    .addOnFailureListener(
                            e -> {

                                progress.setVisibility(
                                        View.GONE
                                );

                                Toast.makeText(
                                        MainActivity.this,
                                        "تعذر تحميل الامتحانات",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                    );
        });
    }

    private void askExamCode(
            String examId,
            String examName
    ) {

        final EditText code =
                input(
                        "اكتب كود الامتحان"
                );

        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle(
                                "دخول امتحان: " +
                                        examName
                        )
                        .setMessage(
                                "اكتب الكود الخاص بهذا الامتحان"
                        )
                        .setView(code)
                        .setPositiveButton(
                                "دخول",
                                null
                        )
                        .setNegativeButton(
                                "إلغاء",
                                null
                        )
                        .create();

        dialog.setOnShowListener(
                d -> {

                    Button positive =
                            dialog.getButton(
                                    AlertDialog.BUTTON_POSITIVE
                            );

                    positive.setOnClickListener(
                            v -> {

                                String value =
                                        code.getText()
                                                .toString()
                                                .trim();

                                if (
                                        value.isEmpty()
                                ) {
                                    code.setError(
                                            "اكتب الكود"
                                    );
                                    return;
                                }

                                verifyExamCode(
                                        examId,
                                        value,
                                        dialog
                                );
                            }
                    );
                }
        );

        dialog.show();
    }

    private void verifyExamCode(
            String examId,
            String code,
            AlertDialog dialog
    ) {

        ensureFirebaseAuth(() -> {

            db.collection("codes")
                    .document(code)
                    .get()
                    .addOnSuccessListener(
                            document -> {

                                if (
                                        !document.exists()
                                ) {

                                    Toast.makeText(
                                            MainActivity.this,
                                            "الكود غير صحيح",
                                            Toast.LENGTH_LONG
                                    ).show();

                                    return;
                                }

                                String savedExamId =
                                        document.getString(
                                                "examId"
                                        );

                                if (
                                        savedExamId == null ||
                                        !savedExamId.equals(
                                                examId
                                        )
                                ) {

                                    Toast.makeText(
                                            MainActivity.this,
                                            "الكود لا يخص هذا الامتحان",
                                            Toast.LENGTH_LONG
                                    ).show();

                                    return;
                                }

                                String student =
                                        prefs.getString(
                                                KEY_STUDENT,
                                                ""
                                        );

                                if (
                                        student.trim().isEmpty()
                                ) {

                                    Toast.makeText(
                                            MainActivity.this,
                                            "اكتب اسم الطالب أولًا",
                                            Toast.LENGTH_LONG
                                    ).show();

                                    return;
                                }

                                String usageId =
                                        makeUsageId(
                                                code,
                                                student
                                        );

                                db.collection("usages")
                                        .document(usageId)
                                        .get()
                                        .addOnSuccessListener(
                                                usage -> {

                                                    if (
                                                            usage.exists()
                                                    ) {

                                                        Toast.makeText(
                                                                MainActivity.this,
                                                                "تم استخدام هذا الكود من قبل لهذا الطالب",
                                                                Toast.LENGTH_LONG
                                                        ).show();

                                                        return;
                                                    }

                                                    dialog.dismiss();

                                                    findExamFromFirebase(
                                                            examId,
                                                            exam -> {

                                                                if (
                                                                        exam == null
                                                                ) {

                                                                    Toast.makeText(
                                                                            MainActivity.this,
                                                                            "تعذر تحميل الامتحان",
                                                                            Toast.LENGTH_LONG
                                                                    ).show();

                                                                    return;
                                                                }

                                                                startExam(
                                                                        exam,
                                                                        code
                                                                );
                                                            }
                                                    );
                                                }
                                        )
                                        .addOnFailureListener(
                                                e ->
                                                        Toast.makeText(
                                                                MainActivity.this,
                                                                "تعذر التحقق من استخدام الكود",
                                                                Toast.LENGTH_LONG
                                                        ).show()
                                        );
                            }
                    )
                    .addOnFailureListener(
                            e ->
                                    Toast.makeText(
                                            MainActivity.this,
                                            "تعذر الاتصال بالسيرفر",
                                            Toast.LENGTH_LONG
                                    ).show()
                    );
        });
    }

    private String makeUsageId(
            String code,
            String student
    ) {

        String raw =
                code +
                        "_" +
                        student
                                .trim()
                                .toLowerCase(
                                        Locale.ROOT
                                );

        return UUID.nameUUIDFromBytes(
                raw.getBytes()
        ).toString();
    }

    private void findExamFromFirebase(
            String examId,
            ExamCallback callback
    ) {

        ensureFirebaseAuth(() -> {

            db.collection("exams")
                    .document(examId)
                    .get()
                    .addOnSuccessListener(
                            document -> {

                                if (
                                        !document.exists()
                                ) {

                                    callback.onExam(null);
                                    return;
                                }

                                try {

                                    JSONObject object =
                                            new JSONObject();

                                    for (
                                            Map.Entry<String, Object> entry
                                            : document.getData()
                                                    .entrySet()
                                    ) {

                                        Object value =
                                                entry.getValue();

                                        if (
                                                value instanceof
                                                        ArrayList
                                        ) {

                                            object.put(
                                                    entry.getKey(),
                                                    new JSONArray(
                                                            (ArrayList<?>)
                                                                    value
                                                    )
                                            );

                                        } else {

                                            object.put(
                                                    entry.getKey(),
                                                    value
                                            );
                                        }
                                    }

                                    object.put(
                                            "id",
                                            examId
                                    );

                                    callback.onExam(
                                            object
                                    );

                                } catch (
                                        Exception e
                                ) {

                                    callback.onExam(
                                            null
                                    );
                                }
                            }
                    )
                    .addOnFailureListener(
                            e ->
                                    callback.onExam(
                                            null
                                    )
                    );
        });
    }

    private interface ExamCallback {
        void onExam(JSONObject exam);
    }

    private void startExam(
            JSONObject exam,
            String code
    ) {

        try {

            JSONArray questions =
                    exam.optJSONArray(
                            "questions"
                    );

            if (
                    questions == null ||
                    questions.length() == 0
            ) {

                Toast.makeText(
                        this,
                        "الامتحان لا يحتوي على أسئلة",
                        Toast.LENGTH_LONG
                ).show();

                return;
            }

            showExamScreen(
                    exam,
                    questions,
                    code
            );

        } catch (
                Exception e
        ) {

            Toast.makeText(
                    this,
                    "تعذر فتح الامتحان",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private void showExamScreen(
            JSONObject exam,
            JSONArray questions,
            String code
    ) {

        baseScreen();

        String examName =
                exam.optString(
                        "name",
                        exam.optString(
                                "title",
                                "الامتحان"
                        )
                );

        header(
                examName,
                "أجب عن الأسئلة ثم اضغط تسليم"
        );

        ScrollView scroll =
                scroll();

        LinearLayout content =
                vertical();

        content.setPadding(
                15,
                5,
                15,
                20
        );

        TextView counter =
                boldText(
                        "عدد الأسئلة: " +
                                questions.length(),
                        16,
                        GOLD,
                        Gravity.CENTER
                );

        content.addView(
                counter,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        0,
                        5,
                        0,
                        10
                )
        );

        ArrayList<EditText> answers =
                new ArrayList<>();

        for (
                int i = 0;
                i < questions.length();
                i++
        ) {

            try {

                JSONObject q =
                        questions.getJSONObject(i);

                String question =
                        q.optString(
                                "question",
                                q.optString(
                                        "text",
                                        ""
                                )
                        );

                String correct =
                        q.optString(
                                "answer",
                                q.optString(
                                        "correctAnswer",
                                        ""
                                )
                        );

                LinearLayout qCard =
                        card();

                qCard.setGravity(
                        Gravity.RIGHT
                );

                TextView qText =
                        boldText(
                                "سؤال " +
                                        (i + 1) +
                                        "\n" +
                                        question,
                                17,
                                textColor(),
                                Gravity.RIGHT
                        );

                qCard.addView(
                        qText,
                        lp(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                );

                EditText answer =
                        input(
                                "اكتب إجابتك"
                        );

                qCard.addView(
                        answer,
                        lpMargin(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                60,
                                0,
                                10,
                                0,
                                5
                        )
                );

                answer.setTag(
                        correct
                );

                answers.add(
                        answer
                );

                content.addView(
                        qCard,
                        lpMargin(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                0,
                                7,
                                0,
                                7
                        )
                );

            } catch (
                    Exception ignored
            ) {
            }
        }

        Button submit =
                button(
                        "✅ تسليم الامتحان"
                );

        submit.setOnClickListener(
                v ->
                        submitExam(
                                exam,
                                questions,
                                answers,
                                code
                        )
        );

        content.addView(
                submit,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        65,
                        0,
                        20,
                        0,
                        20
                )
        );

        scroll.addView(
                content
        );

        root.addView(
                scroll,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                )
        );
    }

    private void submitExam(
            JSONObject exam,
            JSONArray questions,
            ArrayList<EditText> answers,
            String code
    ) {

        String student =
                prefs.getString(
                        KEY_STUDENT,
                        ""
                );

        int total =
                questions.length();

        int answered =
                0;

        int correct =
                0;

        JSONArray mistakes =
                new JSONArray();

        for (
                int i = 0;
                i < answers.size();
                i++
        ) {

            EditText field =
                    answers.get(i);

            String answer =
                    field.getText()
                            .toString()
                            .trim();

            if (
                    !answer.isEmpty()
            ) {

                answered++;

                String right =
                        String.valueOf(
                                field.getTag()
                        );

                if (
                        normalize(answer)
                                .equals(
                                        normalize(right)
                                )
                ) {

                    correct++;

                } else {

                    try {

                        JSONObject mistake =
                                new JSONObject();

                        mistake.put(
                                "question",
                                questions
                                        .getJSONObject(i)
                                        .optString(
                                                "question",
                                                ""
                                        )
                        );

                        mistake.put(
                                "studentAnswer",
                                answer
                        );

                        mistake.put(
                                "correctAnswer",
                                right
                        );

                        mistake.put(
                                "explanation",
                                questions
                                        .getJSONObject(i)
                                        .optString(
                                                "explanation",
                                                ""
                                        )
                        );

                        mistakes.put(
                                mistake
                        );

                    } catch (
                            Exception ignored
                    ) {
                    }
                }
            }
        }

        int wrong =
                answered - correct;

        String examName =
                exam.optString(
                        "name",
                        exam.optString(
                                "title",
                                "الامتحان"
                        )
                );

        String resultId =
                UUID.randomUUID()
                        .toString();

        JSONObject result =
                new JSONObject();

        try {

            result.put(
                    "id",
                    resultId
            );

            result.put(
                    "studentName",
                    student
            );

            result.put(
                    "examId",
                    exam.optString(
                            "id",
                            ""
                    )
            );

            result.put(
                    "examName",
                    examName
            );

            result.put(
                    "code",
                    code
            );

            result.put(
                    "total",
                    total
            );

            result.put(
                    "answered",
                    answered
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
                    "mistakes",
                    mistakes
            );

            result.put(
                    "date",
                    new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm",
                            Locale.getDefault()
                    ).format(
                            new Date()
                    )
            );

        } catch (
                Exception ignored
        ) {
        }

        saveResultLocally(
                result
        );

        markCodeUsed(
                code,
                student
        );

        uploadResult(
                result,
                () ->
                        showResult(
                                result
                        )
        );
    }

    private String normalize(
            String value
    ) {

        if (
                value == null
        ) {
            return "";
        }

        return value
                .trim()
                .toLowerCase(
                        Locale.ROOT
                )
                .replace(
                        "أ",
                        "ا"
                )
                .replace(
                        "إ",
                        "ا"
                )
                .replace(
                        "آ",
                        "ا"
                )
                .replace(
                        "ة",
                        "ه"
                );
    }

    private void uploadResult(
            JSONObject result,
            Runnable done
    ) {

        ensureFirebaseAuth(() -> {

            Map<String, Object> data =
                    jsonToMap(result);

            db.collection("results")
                    .document(
                            result.optString(
                                    "id",
                                    UUID.randomUUID()
                                            .toString()
                            )
                    )
                    .set(data)
                    .addOnSuccessListener(
                            v -> {

                                done.run();

                            }
                    )
                    .addOnFailureListener(
                            e -> {

                                Toast.makeText(
                                        MainActivity.this,
                                        "تم حفظ النتيجة على الهاتف، لكن تعذر رفعها للسيرفر",
                                        Toast.LENGTH_LONG
                                ).show();

                                done.run();
                            }
                    );
        });
    }

    private Map<String, Object> jsonToMap(
            JSONObject object
    ) {

        Map<String, Object> map =
                new HashMap<>();

        JSONArray names =
                object.names();

        if (
                names == null
        ) {
            return map;
        }

        for (
                int i = 0;
                i < names.length();
                i++
        ) {

            try {

                String key =
                        names.getString(i);

                Object value =
                        object.get(key);

                if (
                        value instanceof JSONObject
                ) {

                    map.put(
                            key,
                            jsonObjectToMap(
                                    (JSONObject) value
                            )
                    );

                } else if (
                        value instanceof JSONArray
                ) {

                    map.put(
                            key,
                            jsonArrayToList(
                                    (JSONArray) value
                            )
                    );

                } else {

                    map.put(
                            key,
                            value
                    );
                }

            } catch (
                    Exception ignored
            ) {
            }
        }

        return map;
    }

    private Map<String, Object> jsonObjectToMap(
            JSONObject object
    ) {

        return jsonToMap(
                object
        );
    }

    private ArrayList<Object> jsonArrayToList(
            JSONArray array
    ) {

        ArrayList<Object> list =
                new ArrayList<>();

        for (
                int i = 0;
                i < array.length();
                i++
        ) {

            try {

                Object value =
                        array.get(i);

                if (
                        value instanceof JSONObject
                ) {

                    list.add(
                            jsonObjectToMap(
                                    (JSONObject) value
                            )
                    );

                } else if (
                        value instanceof JSONArray
                ) {

                    list.add(
                            jsonArrayToList(
                                    (JSONArray) value
                            )
                    );

                } else {

                    list.add(
                            value
                    );
                }

            } catch (
                    Exception ignored
            ) {
            }
        }

        return list;
    }

    private void markCodeUsed(
            String code,
            String student
    ) {

        String usageId =
                makeUsageId(
                        code,
                        student
                );

        Map<String, Object> data =
                new HashMap<>();

        data.put(
                "code",
                code
        );

        data.put(
                "studentName",
                student
        );

        data.put(
                "date",
                new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm",
                        Locale.getDefault()
                ).format(
                        new Date()
                )
        );

        ensureFirebaseAuth(() ->
                db.collection("usages")
                        .document(usageId)
                        .set(data)
        );

        JSONArray used =
                getArray(
                        KEY_USED_CODES
                );

        try {

            JSONObject item =
                    new JSONObject();

            item.put(
                    "code",
                    code
            );

            item.put(
                    "student",
                    student
            );

            used.put(
                    item
            );

            saveArray(
                    KEY_USED_CODES,
                    used
            );

        } catch (
                Exception ignored
        ) {
        }
    }

    private void showResult(
            JSONObject result
    ) {

        baseScreen();

        header(
                "نتيجة الامتحان",
                result.optString(
                        "examName",
                        "الامتحان"
                )
        );

        ScrollView scroll =
                scroll();

        LinearLayout content =
                vertical();

        content.setPadding(
                20,
                10,
                20,
                25
        );

        int total =
                result.optInt(
                        "total",
                        0
                );

        int answered =
                result.optInt(
                        "answered",
                        0
                );

        int correct =
                result.optInt(
                        "correct",
                        0
                );

        int wrong =
                result.optInt(
                        "wrong",
                        0
                );

        TextView score =
                boldText(
                        correct +
                                " / " +
                                total,
                        38,
                        GOLD,
                        Gravity.CENTER
                );

        content.addView(
                score,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        75,
                        0,
                        10,
                        0,
                        10
                )
        );

        addResultRow(
                content,
                "عدد الأسئلة",
                String.valueOf(
                        total
                )
        );

        addResultRow(
                content,
                "تمت الإجابة عن",
                String.valueOf(
                        answered
                )
        );

        addResultRow(
                content,
                "الإجابات الصحيحة",
                String.valueOf(
                        correct
                )
        );

        addResultRow(
                content,
                "الإجابات الخاطئة",
                String.valueOf(
                        wrong
                )
        );

        JSONArray mistakes =
                result.optJSONArray(
                        "mistakes"
                );

        if (
                mistakes != null &&
                mistakes.length() > 0
        ) {

            TextView mt =
                    boldText(
                            "مراجعة الأخطاء",
                            23,
                            GOLD,
                            Gravity.RIGHT
                    );

            content.addView(
                    mt,
                    lpMargin(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            0,
                            20,
                            0,
                            10
                    )
            );

            for (
                    int i = 0;
                    i < mistakes.length();
                    i++
            ) {

                try {

                    JSONObject m =
                            mistakes.getJSONObject(
                                    i
                            );

                    LinearLayout mc =
                            card();

                    mc.setGravity(
                            Gravity.RIGHT
                    );

                    String q =
                            m.optString(
                                    "question",
                                    ""
                            );

                    String studentAnswer =
                            m.optString(
                                    "studentAnswer",
                                    ""
                            );

                    String correctAnswer =
                            m.optString(
                                    "correctAnswer",
                                    ""
                            );

                    String explanation =
                            m.optString(
                                    "explanation",
                                    ""
                            );

                    mc.addView(
                            boldText(
                                    "السؤال:\n" +
                                            q,
                                    16,
                                    textColor(),
                                    Gravity.RIGHT
                            ),
                            lp(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                    );

                    mc.addView(
                            text(
                                    "إجابتك: " +
                                            studentAnswer,
                                    15,
                                    Color.rgb(
                                            190,
                                            60,
                                            60
                                    ),
                                    Gravity.RIGHT
                            ),
                            lp(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                    );

                    mc.addView(
                            text(
                                    "الإجابة الصحيحة: " +
                                            correctAnswer,
                                    15,
                                    Color.rgb(
                                            50,
                                            150,
                                            80
                                    ),
                                    Gravity.RIGHT
                            ),
                            lp(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                    );

                    if (
                            !explanation
                                    .trim()
                                    .isEmpty()
                    ) {

                        mc.addView(
                                text(
                                        "الشرح: " +
                                                explanation,
                                        14,
                                        subTextColor(),
                                        Gravity.RIGHT
                                ),
                                lp(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                )
                        );
                    }

                    content.addView(
                            mc,
                            lpMargin(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    0,
                                    7,
                                    0,
                                    7
                            )
                    );

                } catch (
                        Exception ignored
                ) {
                }
            }
        }

        Button home =
                button(
                        "🏠 العودة للرئيسية"
                );

        home.setOnClickListener(
                v -> studentHome()
        );

        content.addView(
                home,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        20,
                        0,
                        10
                )
        );

        scroll.addView(
                content
        );

        root.addView(
                scroll,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                )
        );
    }

    private void addResultRow(
            LinearLayout parent,
            String label,
            String value
    ) {

        LinearLayout row =
                horizontal();

        row.setGravity(
                Gravity.CENTER_VERTICAL
        );

        row.setPadding(
                15,
                10,
                15,
                10
        );

        row.setBackground(
                drawable(
                        cardColor(),
                        20
                )
        );

        TextView l =
                text(
                        label,
                        16,
                        textColor(),
                        Gravity.RIGHT
                );

        TextView v =
                boldText(
                        value,
                        17,
                        GOLD,
                        Gravity.LEFT
                );

        row.addView(
                l,
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                )
        );

        row.addView(
                v,
                new LinearLayout.LayoutParams(
                        100,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        parent.addView(
                row,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        0,
                        5,
                        0,
                        5
                )
        );
    }

    private void studentResults() {

        baseScreen();

        header(
                "نتائجي",
                "نتائج الامتحانات الخاصة بك"
        );

        LinearLayout content =
                vertical();

        content.setPadding(
                20,
                10,
                20,
                20
        );

        ProgressBar progress =
                new ProgressBar(this);

        content.addView(
                progress,
                lpMargin(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        0,
                        25,
                        0,
                        25
                )
        );

        root.addView(
                content,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                )
        );

        String student =
                prefs.getString(
                        KEY_STUDENT,
                        ""
                );

        ensureFirebaseAuth(() -> {

            db.collection("results")
                    .whereEqualTo(
                            "studentName",
                            student
                    )
                    .get()
                    .addOnSuccessListener(
                            snapshot -> {

                                progress.setVisibility(
                                        View.GONE
                                );

                                if (
                                        snapshot.isEmpty()
                                ) {

                                    content.addView(
                                            text(
                                                    "لا توجد نتائج حتى الآن",
                                                    17,
                                                    subTextColor(),
                                                    Gravity.CENTER
                                            ),
                                            lpMargin(
                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                                    0,
                                                    25,
                                                    0,
                                                    20
                                            )
                                    );

                                    return;
                                }

                                for (
                                        QueryDocumentSnapshot doc
                                        : snapshot
                                ) {

                                    LinearLayout c =
                                            card();

                                    String examName =
                                            doc.getString(
                                                    "examName"
                                            );

                                    if (
                                            examName == null
                                    ) {
                                        examName =
                                                "امتحان";
                                    }

                                    int correct =
                                            getInt(
                                                    doc,
                                                    "correct"
                                            );

                                    int total =
                                            getInt(
                                                    doc,
                                                    "total"
                                            );

                                    int wrong =
                                            getInt(
                                                    doc,
                                                    "wrong"
                                            );

                                    TextView title =
                                            boldText(
                                                    examName,
                                                    19,
                                                    GOLD,
                                                    Gravity.CENTER
                                            );

                                    c.addView(
                                            title,
                                            lp(
                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                            )
                                    );

                                    c.addView(
                                            text(
                                                    "النتيجة: " +
                                                            correct +
                                                            " / " +
                                                            total,
                                                    16,
                                                    textColor(),
                                                    Gravity.CENTER
                                            ),
                                            lp(
                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                            )
                                    );

                                    c.addView(
                                            text(
                                                    "الأخطاء: " +
                                                            wrong,
                                                    14,
                                                    subTextColor(),
                                                    Gravity.CENTER
                                            ),
                                            lp(
                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                            )
                                    );

                                    content.addView(
                                            c,
                                            lpMargin(
                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                                    0,
                                                    7,
                                                    0,
                                                    7
                                            )
                                    );
                                }
                            }
                    )
                    .addOnFailureListener(
                            e -> {

                                progress.setVisibility(
                                        View.GONE
                                );

                                Toast.makeText(
                                        MainActivity.this,
                                        "تعذر تحميل النتائج",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                    );
        });
    }

    private int getInt(
            QueryDocumentSnapshot doc,
            String key
    ) {

        Long value =
                doc.getLong(
                        key
                );

        if (
                value == null
        ) {
            return 0;
        }

        return value.intValue();
    }

    private void saveResultLocally(
            JSONObject result
    ) {

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
    }

    private void studentNotes() {

        baseScreen();

        header(
                "مذكرات الشرح",
                "المذكرات المتاحة"
        );

        ScrollView s =
                scroll();

        LinearLayout content =
                vertical();

        content.setPadding(
                20,
                10,
                20,
                20
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
                            "لا توجد مذكرات حاليًا",
                            17,
                            subTextColor(),
                            Gravity.CENTER
                    ),
                    lpMargin(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            0,
                            30,
                            0,
                            20
                    )
            );

        } else {

            for (
                    int i = 0;
                    i < notes.length();
                    i++
            ) {

                try {

                    JSONObject n =
                            notes.getJSONObject(
                                    i
                            );

                    LinearLayout c =
                            card();

                    c.addView(
                            boldText(
                                    n.optString(
                                            "title",
                                            "مذكرة"
                                    ),
                                    18,
                                    GOLD,
                                    Gravity.CENTER
                            ),
                            lp(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                    );

                    c.addView(
                            text(
                                    n.optString(
                                            "content",
                                            ""
                                    ),
                                    14,
                                    textColor(),
                                    Gravity.RIGHT
                            ),
                            lp(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                    );

                    content.addView(
                            c,
                            lpMargin(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    0,
                                    7,
                                    0,
                                    7
                            )
                    );

                } catch (
                        Exception ignored
                ) {
                }
            }
        }

        s.addView(
                content
        );

        root.addView(
                s,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                )
        );
    }

    private void studentVideos() {

        baseScreen();

        header(
                "الفيديوهات",
                "الفيديوهات التعليمية"
        );

        ScrollView s =
                scroll();

        LinearLayout content =
                vertical();

        content.setPadding(
                20,
                10,
                20,
                20
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
                            "لا توجد فيديوهات حاليًا",
                            17,
                            subTextColor(),
                            Gravity.CENTER
                    ),
                    lpMargin(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            0,
                            30,
                            0,
                            20
                    )
            );

        } else {

            for (
                    int i = 0;
                    i < videos.length();
                    i++
            ) {

                try {

                    JSONObject v =
                            videos.getJSONObject(
                                    i
                            );

                    LinearLayout c =
                            card();

                    String title =
                            v.optString(
                                    "title",
                                    "فيديو"
                            );

                    String url =
                            v.optString(
                                    "url",
                                    ""
                            );

                    c.addView(
                            boldText(
                                    title,
                                    18,
                                    GOLD,
                                    Gravity.CENTER
                            ),
                            lp(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                    );

                    Button open =
                            button(
                                    "▶ فتح الفيديو"
                            );

                    open.setOnClickListener(
                            view -> {

                                if (
                                        url.trim().isEmpty()
                                ) {

                                    Toast.makeText(
                                            MainActivity.this,
                                            "رابط الفيديو غير موجود",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                    return;
                                }

                                try {

                                    startActivity(
                                            new Intent(
                                                    Intent.ACTION_VIEW,
                                                    Uri.parse(url)
                                            )
                                    );

                                } catch (
                                        Exception e
                                ) {

                                    Toast.makeText(
                                            MainActivity.this,
                                            "تعذر فتح الفيديو",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            }
                    );

                    c.addView(
                            open,
                            lpMargin(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    55,
                                    0,
                                    10,
                                    0,
                                    0
                            )
                    );

                    content.addView(
                            c,
                            lpMargin(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    0,
                                    7,
                                    0,
                                    7
                            )
                    );

                } catch (
                        Exception ignored
                ) {
                }
            }
        }

        s.addView(
                content
        );

        root.addView(
                s,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                )
        );
    }

    private void azkarScreen() {

        baseScreen();

        header(
                "الأذكار",
                "أذكار المسلم"
        );

        ScrollView s =
                scroll();

        LinearLayout content =
                vertical();

        content.setPadding(
                20,
                10,
                20,
                20
        );

        String[] azkar = {
                "سبحان الله",
                "الحمد لله",
                "الله أكبر",
                "لا إله إلا الله",
                "أستغفر الله",
                "اللهم صل وسلم على نبينا محمد"
        };

        for (
                String z
                : azkar
        ) {

            LinearLayout c =
                    card();

            c.addView(
                    text(
                            z,
                            20,
                            textColor(),
                            Gravity.CENTER
                    ),
                    lp(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    )
            );

            content.addView(
                    c,
                    lpMargin(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            75,
                            0,
                            7,
                            0,
                            7
                    )
            );
        }

        s.addView(
                content
        );

        root.addView(
                s,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                )
        );
    }

    private void teacherLogin() {

        baseScreen();

        header(
                "دخول المدرس",
                "اكتب اسم المدرس وكود الدخول"
        );

        LinearLayout content =
                vertical();

        content.setPadding(
                25,
                15,
                25,
                25
        );

        EditText teacherName =
                input(
                        "اكتب اسم المدرس"
                );

        content.addView(
                teacherName,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        10,
                        0,
                        10
                )
        );

        EditText code =
                input(
                        "كود الدخول"
                );

        code.setInputType(
                android.text.InputType.TYPE_CLASS_NUMBER |
                        android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD
        );

        content.addView(
                code,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        10,
                        0,
                        20
                )
        );

        Button enter =
                button(
                        "👨‍🏫 دخول المدرس"
                );

        enter.setOnClickListener(
                v -> {

                    String name =
                            teacherName.getText()
                                    .toString()
                                    .trim();

                    String pass =
                            code.getText()
                                    .toString()
                                    .trim();

                    if (
                            name.isEmpty()
                    ) {

                        teacherName.setError(
                                "اكتب اسم المدرس"
                        );

                        return;
                    }

                    if (
                            !"1234".equals(
                                    pass
                            )
                    ) {

                        code.setError(
                                "كود الدخول غير صحيح"
                        );

                        return;
                    }

                    prefs.edit()
                            .putString(
                                    KEY_TEACHER,
                                    name
                            )
                            .apply();

                    syncLocalDataToFirebase(
                            () -> teacherHome()
                    );
                }
        );

        content.addView(
                enter,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60
                )
        );

        Button back =
                button(
                        "رجوع"
                );

        back.setOnClickListener(
                v -> showRoleScreen()
        );

        content.addView(
                back,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        15,
                        0,
                        0
                )
        );

        root.addView(
                content,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                )
        );
    }

    private void syncLocalDataToFirebase(
            Runnable done
    ) {

        ensureFirebaseAuth(() -> {

            JSONArray exams =
                    getArray(
                            KEY_EXAMS
                    );

            JSONArray codes =
                    getArray(
                            KEY_CODES
                    );

            if (
                    exams.length() == 0 &&
                    codes.length() == 0
            ) {

                done.run();
                return;
            }

            final int total =
                    exams.length() +
                            codes.length();

            final int[] completed =
                    {0};

            Runnable check =
                    () -> {

                        completed[0]++;

                        if (
                                completed[0] >= total
                        ) {
                            done.run();
                        }
                    };

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
                                    "id",
                                    ""
                            );

                    if (
                            id.isEmpty()
                    ) {

                        id =
                                UUID.randomUUID()
                                        .toString();

                        exam.put(
                                "id",
                                id
                        );
                    }

                    Map<String, Object> data =
                            jsonToMap(exam);

                    data.put(
                            "createdBy",
                            firebaseAuth
                                    .getCurrentUser()
                                    .getUid()
                    );

                    db.collection("exams")
                            .document(id)
                            .set(data)
                            .addOnCompleteListener(
                                    task ->
                                            check.run()
                            );

                } catch (
                        Exception e
                ) {

                    check.run();
                }
            }

            for (
                    int i = 0;
                    i < codes.length();
                    i++
            ) {

                try {

                    JSONObject item =
                            codes.getJSONObject(i);

                    String code =
                            item.optString(
                                    "code",
                                    ""
                            );

                    if (
                            code.isEmpty()
                    ) {

                        check.run();
                        continue;
                    }

                    Map<String, Object> data =
                            jsonToMap(item);

                    data.put(
                            "createdBy",
                            firebaseAuth
                                    .getCurrentUser()
                                    .getUid()
                    );

                    db.collection("codes")
                            .document(code)
                            .set(data)
                            .addOnCompleteListener(
                                    task ->
                                            check.run()
                            );

                } catch (
                        Exception e
                ) {

                    check.run();
                }
            }
        });
    }

    private void teacherHome() {

        baseScreen();

        header(
                "لوحة المدرس",
                prefs.getString(
                        KEY_TEACHER,
                        "المدرس"
                )
        );

        LinearLayout content =
                vertical();

        content.setPadding(
                20,
                10,
                20,
                20
        );

        addTeacherCard(
                content,
                "📝 إدارة الامتحانات",
                "إنشاء وتعديل وحذف الامتحانات",
                v -> examManager()
        );

        addTeacherCard(
                content,
                "🔑 أكواد الامتحانات",
                "إنشاء وإدارة أكواد الدخول",
                v -> codesManager()
        );

        addTeacherCard(
                content,
                "📊 النتائج والطلاب",
                "عرض نتائج الطلاب",
                v -> teacherResults()
        );

        addTeacherCard(
                content,
                "📚 مذكرات الشرح",
                "إدارة المذكرات",
                v -> notesManager()
        );

        addTeacherCard(
                content,
                "🎥 الفيديوهات",
                "إدارة الفيديوهات",
                v -> videosManager()
        );

        addTeacherCard(
                content,
                "⚙ الإعدادات",
                "إعدادات التطبيق",
                v -> settingsScreen()
        );

        Button logout =
                button(
                        "تسجيل الخروج"
                );

        logout.setOnClickListener(
                v -> showRoleScreen()
        );

        content.addView(
                logout,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        20,
                        0,
                        15
                )
        );

        root.addView(
                content,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                )
        );
    }

    private void addTeacherCard(
            LinearLayout parent,
            String title,
            String subtitle,
            View.OnClickListener listener
    ) {

        LinearLayout c =
                card();

        c.addView(
                boldText(
                        title,
                        20,
                        GOLD,
                        Gravity.CENTER
                ),
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        c.addView(
                text(
                        subtitle,
                        14,
                        subTextColor(),
                        Gravity.CENTER
                ),
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        c.setOnClickListener(
                listener
        );

        parent.addView(
                c,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        105,
                        0,
                        7,
                        0,
                        7
                )
        );
    }

    private void examManager() {

        baseScreen();

        header(
                "إدارة الامتحانات",
                "إنشاء وإدارة الامتحانات"
        );

        LinearLayout content =
                vertical();

        content.setPadding(
                20,
                10,
                20,
                20
        );

        Button add =
                button(
                        "➕ إنشاء امتحان جديد"
                );

        add.setOnClickListener(
                v -> createExamScreen()
        );

        content.addView(
                add,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        5,
                        0,
                        15
                )
        );

        ProgressBar progress =
                new ProgressBar(this);

        content.addView(
                progress
        );

        root.addView(
                content,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                )
        );

        ensureFirebaseAuth(() -> {

            db.collection("exams")
                    .get()
                    .addOnSuccessListener(
                            snapshot -> {

                                progress.setVisibility(
                                        View.GONE
                                );

                                for (
                                        QueryDocumentSnapshot doc
                                        : snapshot
                                ) {

                                    String name =
                                            doc.getString(
                                                    "name"
                                            );

                                    if (
                                            name == null
                                    ) {
                                        name =
                                                doc.getString(
                                                        "title"
                                                );
                                    }

                                    if (
                                            name == null
                                    ) {
                                        name =
                                                "امتحان";
                                    }

                                    String finalName =
                                            name;

                                    LinearLayout c =
                                            card();

                                    c.addView(
                                            boldText(
                                                    finalName,
                                                    19,
                                                    GOLD,
                                                    Gravity.CENTER
                                            ),
                                            lp(
                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                            )
                                    );

                                    c.addView(
                                            text(
                                                    "عدد الأسئلة: " +
                                                            getQuestionsCount(
                                                                    doc
                                                            ),
                                                    14,
                                                    subTextColor(),
                                                    Gravity.CENTER
                                            ),
                                            lp(
                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                            )
                                    );

                                    Button delete =
                                            button(
                                                    "حذف الامتحان"
                                            );

                                    String id =
                                            doc.getId();

                                    delete.setOnClickListener(
                                            v ->
                                                    confirmDeleteExam(
                                                            id
                                                    )
                                    );

                                    c.addView(
                                            delete,
                                            lpMargin(
                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                    55,
                                                    0,
                                                    8,
                                                    0,
                                                    0
                                            )
                                    );

                                    content.addView(
                                            c,
                                            lpMargin(
                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                                    0,
                                                    7,
                                                    0,
                                                    7
                                            )
                                    );
                                }
                            }
                    )
                    .addOnFailureListener(
                            e -> {

                                progress.setVisibility(
                                        View.GONE
                                );

                                Toast.makeText(
                                        MainActivity.this,
                                        "تعذر تحميل الامتحانات",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                    );
        });
    }

    private int getQuestionsCount(
            QueryDocumentSnapshot doc
    ) {

        Object q =
                doc.get(
                        "questions"
                );

        if (
                q instanceof ArrayList
        ) {

            return (
                    (ArrayList<?>)
                            q
            ).size();
        }

        return 0;
    }

    private void confirmDeleteExam(
            String id
    ) {

        new AlertDialog.Builder(this)
                .setTitle(
                        "حذف الامتحان"
                )
                .setMessage(
                        "هل أنت متأكد من حذف هذا الامتحان؟"
                )
                .setPositiveButton(
                        "حذف",
                        (d, w) ->
                                ensureFirebaseAuth(
                                        () ->
                                                db.collection(
                                                        "exams"
                                                )
                                                        .document(id)
                                                        .delete()
                                                        .addOnSuccessListener(
                                                                v -> {

                                                                    Toast.makeText(
                                                                            MainActivity.this,
                                                                            "تم حذف الامتحان",
                                                                            Toast.LENGTH_SHORT
                                                                    ).show();

                                                                    examManager();
                                                                }
                                                        )
                                )
                )
                .setNegativeButton(
                        "إلغاء",
                        null
                )
                .show();
    }

    private void createExamScreen() {

        baseScreen();

        header(
                "إنشاء امتحان",
                "أدخل بيانات الامتحان"
        );

        ScrollView s =
                scroll();

        LinearLayout content =
                vertical();

        content.setPadding(
                20,
                10,
                20,
                25
        );

        EditText name =
                input(
                        "اسم الامتحان"
                );

        content.addView(
                name,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        8,
                        0,
                        8
                )
        );

        EditText description =
                input(
                        "وصف الامتحان"
                );

        content.addView(
                description,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        8,
                        0,
                        15
                )
        );

        TextView info =
                text(
                        "أضف الأسئلة واحدًا تلو الآخر",
                        16,
                        GOLD,
                        Gravity.CENTER
                );

        content.addView(
                info,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        LinearLayout questionsBox =
                vertical();

        ArrayList<EditText> questionFields =
                new ArrayList<>();

        ArrayList<EditText> answerFields =
                new ArrayList<>();

        ArrayList<EditText> explanationFields =
                new ArrayList<>();

        Button addQuestion =
                button(
                        "➕ إضافة سؤال"
                );

        addQuestion.setOnClickListener(
                v -> {

                    LinearLayout qCard =
                            card();

                    EditText q =
                            input(
                                    "السؤال"
                            );

                    EditText a =
                            input(
                                    "الإجابة الصحيحة"
                            );

                    EditText ex =
                            input(
                                    "شرح الإجابة"
                            );

                    questionFields.add(
                            q
                    );

                    answerFields.add(
                            a
                    );

                    explanationFields.add(
                            ex
                    );

                    qCard.addView(
                            boldText(
                                    "سؤال " +
                                            questionFields.size(),
                                    18,
                                    GOLD,
                                    Gravity.CENTER
                            ),
                            lp(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                    );

                    qCard.addView(
                            q,
                            lpMargin(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    60,
                                    0,
                                    8,
                                    0,
                                    8
                            )
                    );

                    qCard.addView(
                            a,
                            lpMargin(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    60,
                                    0,
                                    8,
                                    0,
                                    8
                            )
                    );

                    qCard.addView(
                            ex,
                            lpMargin(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    60,
                                    0,
                                    8,
                                    0,
                                    8
                            )
                    );

                    questionsBox.addView(
                            qCard,
                            lpMargin(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    0,
                                    7,
                                    0,
                                    7
                            )
                    );
                }
        );

        content.addView(
                addQuestion,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        10,
                        0,
                        10
                )
        );

        content.addView(
                questionsBox,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        Button save =
                button(
                        "💾 حفظ الامتحان"
                );

        save.setOnClickListener(
                v -> {

                    String examName =
                            name.getText()
                                    .toString()
                                    .trim();

                    if (
                            examName.isEmpty()
                    ) {

                        name.setError(
                                "اكتب اسم الامتحان"
                        );

                        return;
                    }

                    if (
                            questionFields.isEmpty()
                    ) {

                        Toast.makeText(
                                MainActivity.this,
                                "أضف سؤالًا واحدًا على الأقل",
                                Toast.LENGTH_LONG
                        ).show();

                        return;
                    }

                    JSONArray questions =
                            new JSONArray();

                    for (
                            int i = 0;
                            i < questionFields.size();
                            i++
                    ) {

                        String q =
                                questionFields
                                        .get(i)
                                        .getText()
                                        .toString()
                                        .trim();

                        String a =
                                answerFields
                                        .get(i)
                                        .getText()
                                        .toString()
                                        .trim();

                        String ex =
                                explanationFields
                                        .get(i)
                                        .getText()
                                        .toString()
                                        .trim();

                        if (
                                q.isEmpty() ||
                                a.isEmpty()
                        ) {

                            Toast.makeText(
                                    MainActivity.this,
                                    "أكمل السؤال والإجابة الصحيحة",
                                    Toast.LENGTH_LONG
                            ).show();

                            return;
                        }

                        try {

                            JSONObject item =
                                    new JSONObject();

                            item.put(
                                    "question",
                                    q
                            );

                            item.put(
                                    "answer",
                                    a
                            );

                            item.put(
                                    "explanation",
                                    ex
                            );

                            questions.put(
                                    item
                            );

                        } catch (
                                Exception ignored
                        ) {
                        }
                    }

                    String id =
                            UUID.randomUUID()
                                    .toString();

                    JSONObject exam =
                            new JSONObject();

                    try {

                        exam.put(
                                "id",
                                id
                        );

                        exam.put(
                                "name",
                                examName
                        );

                        exam.put(
                                "description",
                                description
                                        .getText()
                                        .toString()
                                        .trim()
                        );

                        exam.put(
                                "questions",
                                questions
                        );

                        exam.put(
                                "createdAt",
                                new Date()
                                        .getTime()
                        );

                    } catch (
                            Exception ignored
                    ) {
                    }

                    JSONArray local =
                            getArray(
                                    KEY_EXAMS
                            );

                    local.put(
                            exam
                    );

                    saveArray(
                            KEY_EXAMS,
                            local
                    );

                    uploadExam(
                            exam
                    );
                }
        );

        content.addView(
                save,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        65,
                        0,
                        20,
                        0,
                        10
                )
        );

        Button back =
                button(
                        "رجوع"
                );

        back.setOnClickListener(
                v -> examManager()
        );

        content.addView(
                back,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60
                )
        );

        s.addView(
                content
        );

        root.addView(
                s,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                )
        );

        addQuestion.performClick();
    }

    private void uploadExam(
            JSONObject exam
    ) {

        ensureFirebaseAuth(() -> {

            String id =
                    exam.optString(
                            "id",
                            UUID.randomUUID()
                                    .toString()
                    );

            Map<String, Object> data =
                    jsonToMap(
                            exam
                    );

            data.put(
                    "createdBy",
                    firebaseAuth
                            .getCurrentUser()
                            .getUid()
            );

            data.put(
                    "teacherName",
                    prefs.getString(
                            KEY_TEACHER,
                            ""
                    )
            );

            db.collection("exams")
                    .document(id)
                    .set(data)
                    .addOnSuccessListener(
                            v -> {

                                Toast.makeText(
                                        MainActivity.this,
                                        "تم حفظ الامتحان على السيرفر",
                                        Toast.LENGTH_SHORT
                                ).show();

                                examManager();
                            }
                    )
                    .addOnFailureListener(
                            e ->
                                    Toast.makeText(
                                            MainActivity.this,
                                            "تم حفظ الامتحان على الهاتف لكن فشل رفعه للسيرفر",
                                            Toast.LENGTH_LONG
                                    ).show()
                    );
        });
    }

    private void codesManager() {

        baseScreen();

        header(
                "أكواد الامتحانات",
                "إنشاء وإدارة أكواد الدخول"
        );

        LinearLayout content =
                vertical();

        content.setPadding(
                20,
                10,
                20,
                20
        );

        Button add =
                button(
                        "➕ إنشاء كود جديد"
                );

        add.setOnClickListener(
                v -> createCodeScreen()
        );

        content.addView(
                add,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        5,
                        0,
                        15
                )
        );

        ProgressBar progress =
                new ProgressBar(this);

        content.addView(
                progress
        );

        root.addView(
                content,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                )
        );

        ensureFirebaseAuth(() -> {

            db.collection("codes")
                    .get()
                    .addOnSuccessListener(
                            snapshot -> {

                                progress.setVisibility(
                                        View.GONE
                                );

                                for (
                                        QueryDocumentSnapshot doc
                                        : snapshot
                                ) {

                                    String code =
                                            doc.getId();

                                    String examId =
                                            doc.getString(
                                                    "examId"
                                            );

                                    LinearLayout c =
                                            card();

                                    c.addView(
                                            boldText(
                                                    "🔑 " +
                                                            code,
                                                    21,
                                                    GOLD,
                                                    Gravity.CENTER
                                            ),
                                            lp(
                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                            )
                                    );

                                    if (
                                            examId != null
                                    ) {

                                        c.addView(
                                                text(
                                                        "الامتحان: " +
                                                                examId,
                                                        13,
                                                        subTextColor(),
                                                        Gravity.CENTER
                                                ),
                                                lp(
                                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                                )
                                        );
                                    }

                                    content.addView(
                                            c,
                                            lpMargin(
                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                                    0,
                                                    7,
                                                    0,
                                                    7
                                            )
                                    );
                                }
                            }
                    )
                    .addOnFailureListener(
                            e ->
                                    progress.setVisibility(
                                            View.GONE
                                    )
                    );
        });
    }

    private void createCodeScreen() {

        baseScreen();

        header(
                "إنشاء كود",
                "اختر الامتحان واكتب الكود"
        );

        LinearLayout content =
                vertical();

        content.setPadding(
                20,
                10,
                20,
                20
        );

        EditText examId =
                input(
                        "معرف الامتحان"
                );

        content.addView(
                examId,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        10,
                        0,
                        10
                )
        );

        EditText code =
                input(
                        "كود الامتحان"
                );

        content.addView(
                code,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        10,
                        0,
                        20
                )
        );

        Button save =
                button(
                        "💾 حفظ الكود"
                );

        save.setOnClickListener(
                v -> {

                    String id =
                            examId.getText()
                                    .toString()
                                    .trim();

                    String value =
                            code.getText()
                                    .toString()
                                    .trim();

                    if (
                            id.isEmpty()
                    ) {

                        examId.setError(
                                "اكتب معرف الامتحان"
                        );

                        return;
                    }

                    if (
                            value.isEmpty()
                    ) {

                        code.setError(
                                "اكتب الكود"
                        );

                        return;
                    }

                    JSONObject item =
                            new JSONObject();

                    try {

                        item.put(
                                "code",
                                value
                        );

                        item.put(
                                "examId",
                                id
                        );

                    } catch (
                            Exception ignored
                    ) {
                    }

                    JSONArray codes =
                            getArray(
                                    KEY_CODES
                            );

                    codes.put(
                            item
                    );

                    saveArray(
                            KEY_CODES,
                            codes
                    );

                    ensureFirebaseAuth(() -> {

                        Map<String, Object> data =
                                jsonToMap(
                                        item
                                );

                        data.put(
                                "createdBy",
                                firebaseAuth
                                        .getCurrentUser()
                                        .getUid()
                        );

                        db.collection("codes")
                                .document(value)
                                .set(data)
                                .addOnSuccessListener(
                                        x -> {

                                            Toast.makeText(
                                                    MainActivity.this,
                                                    "تم حفظ الكود",
                                                    Toast.LENGTH_SHORT
                                            ).show();

                                            codesManager();
                                        }
                                )
                                .addOnFailureListener(
                                        e ->
                                                Toast.makeText(
                                                        MainActivity.this,
                                                        "تم حفظ الكود على الهاتف لكن فشل رفعه للسيرفر",
                                                        Toast.LENGTH_LONG
                                                ).show()
                                );
                    });
                }
        );

        content.addView(
                save,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60
                )
        );

        root.addView(
                content,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                )
        );
    }

    private void teacherResults() {

        baseScreen();

        header(
                "نتائج الطلاب",
                "جميع النتائج المرفوعة"
        );

        ScrollView s =
                scroll();

        LinearLayout content =
                vertical();

        content.setPadding(
                20,
                10,
                20,
                20
        );

        ProgressBar progress =
                new ProgressBar(this);

        content.addView(
                progress,
                lpMargin(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        0,
                        20,
                        0,
                        20
                )
        );

        s.addView(
                content
        );

        root.addView(
                s,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                )
        );

        ensureFirebaseAuth(() -> {

            db.collection("results")
                    .get()
                    .addOnSuccessListener(
                            snapshot -> {

                                progress.setVisibility(
                                        View.GONE
                                );

                                if (
                                        snapshot.isEmpty()
                                ) {

                                    content.addView(
                                            text(
                                                    "لا توجد نتائج",
                                                    17,
                                                    subTextColor(),
                                                    Gravity.CENTER
                                            )
                                    );

                                    return;
                                }

                                for (
                                        QueryDocumentSnapshot doc
                                        : snapshot
                                ) {

                                    LinearLayout c =
                                            card();

                                    String student =
                                            doc.getString(
                                                    "studentName"
                                            );

                                    String exam =
                                            doc.getString(
                                                    "examName"
                                            );

                                    int correct =
                                            getInt(
                                                    doc,
                                                    "correct"
                                            );

                                    int total =
                                            getInt(
                                                    doc,
                                                    "total"
                                            );

                                    int wrong =
                                            getInt(
                                                    doc,
                                                    "wrong"
                                            );

                                    c.addView(
                                            boldText(
                                                    student == null
                                                            ? "طالب"
                                                            : student,
                                                    19,
                                                    GOLD,
                                                    Gravity.CENTER
                                            ),
                                            lp(
                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                            )
                                    );

                                    c.addView(
                                            text(
                                                    exam == null
                                                            ? "امتحان"
                                                            : exam,
                                                    15,
                                                    textColor(),
                                                    Gravity.CENTER
                                            ),
                                            lp(
                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                            )
                                    );

                                    c.addView(
                                            text(
                                                    "النتيجة: " +
                                                            correct +
                                                            " / " +
                                                            total +
                                                            "\nالأخطاء: " +
                                                            wrong,
                                                    15,
                                                    subTextColor(),
                                                    Gravity.CENTER
                                            ),
                                            lp(
                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                            )
                                    );

                                    content.addView(
                                            c,
                                            lpMargin(
                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                                    0,
                                                    7,
                                                    0,
                                                    7
                                            )
                                    );
                                }
                            }
                    )
                    .addOnFailureListener(
                            e -> {

                                progress.setVisibility(
                                        View.GONE
                                );

                                Toast.makeText(
                                        MainActivity.this,
                                        "تعذر تحميل النتائج",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                    );
        });
    }

    private void notesManager() {

        baseScreen();

        header(
                "مذكرات الشرح",
                "إدارة المذكرات"
        );

        LinearLayout content =
                vertical();

        content.setPadding(
                20,
                10,
                20,
                20
        );

        Button add =
                button(
                        "➕ إضافة مذكرة"
                );

        add.setOnClickListener(
                v -> addNoteDialog()
        );

        content.addView(
                add,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        5,
                        0,
                        15
                )
        );

        JSONArray notes =
                getArray(
                        KEY_NOTES
                );

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

                LinearLayout c =
                        card();

                c.addView(
                        boldText(
                                note.optString(
                                        "title",
                                        "مذكرة"
                                ),
                                18,
                                GOLD,
                                Gravity.CENTER
                        ),
                        lp(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                );

                c.addView(
                        text(
                                note.optString(
                                        "content",
                                        ""
                                ),
                                14,
                                textColor(),
                                Gravity.RIGHT
                        ),
                        lp(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                );

                content.addView(
                        c,
                        lpMargin(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                0,
                                7,
                                0,
                                7
                        )
                );

            } catch (
                    Exception ignored
            ) {
            }
        }

        root.addView(
                content,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                )
        );
    }

    private void addNoteDialog() {

        LinearLayout box =
                vertical();

        box.setPadding(
                20,
                5,
                20,
                5
        );

        EditText title =
                input(
                        "عنوان المذكرة"
                );

        EditText content =
                input(
                        "محتوى المذكرة"
                );

        box.addView(
                title,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        5,
                        0,
                        5
                )
        );

        box.addView(
                content,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        100,
                        0,
                        5,
                        0,
                        5
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
                        "حفظ",
                        (d, w) -> {

                            JSONObject note =
                                    new JSONObject();

                            try {

                                note.put(
                                        "title",
                                        title.getText()
                                                .toString()
                                                .trim()
                                );

                                note.put(
                                        "content",
                                        content.getText()
                                                .toString()
                                                .trim()
                                );

                            } catch (
                                    Exception ignored
                            ) {
                            }

                            JSONArray notes =
                                    getArray(
                                            KEY_NOTES
                                    );

                            notes.put(
                                    note
                            );

                            saveArray(
                                    KEY_NOTES,
                                    notes
                            );

                            notesManager();
                        }
                )
                .setNegativeButton(
                        "إلغاء",
                        null
                )
                .show();
    }

    private void videosManager() {

        baseScreen();

        header(
                "الفيديوهات",
                "إدارة الفيديوهات"
        );

        LinearLayout content =
                vertical();

        content.setPadding(
                20,
                10,
                20,
                20
        );

        Button add =
                button(
                        "➕ إضافة فيديو"
                );

        add.setOnClickListener(
                v -> addVideoDialog()
        );

        content.addView(
                add,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        5,
                        0,
                        15
                )
        );

        JSONArray videos =
                getArray(
                        KEY_VIDEOS
                );

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

                LinearLayout c =
                        card();

                c.addView(
                        boldText(
                                video.optString(
                                        "title",
                                        "فيديو"
                                ),
                                18,
                                GOLD,
                                Gravity.CENTER
                        ),
                        lp(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                );

                c.addView(
                        text(
                                video.optString(
                                        "url",
                                        ""
                                ),
                                13,
                                subTextColor(),
                                Gravity.CENTER
                        ),
                        lp(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                );

                content.addView(
                        c,
                        lpMargin(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                0,
                                7,
                                0,
                                7
                        )
                );

            } catch (
                    Exception ignored
            ) {
            }
        }

        root.addView(
                content,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                )
        );
    }

    private void addVideoDialog() {

        LinearLayout box =
                vertical();

        box.setPadding(
                20,
                5,
                20,
                5
        );

        EditText title =
                input(
                        "عنوان الفيديو"
                );

        EditText url =
                input(
                        "رابط الفيديو"
                );

        box.addView(
                title,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        5,
                        0,
                        5
                )
        );

        box.addView(
                url,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        5,
                        0,
                        5
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
                        "حفظ",
                        (d, w) -> {

                            JSONObject video =
                                    new JSONObject();

                            try {

                                video.put(
                                        "title",
                                        title.getText()
                                                .toString()
                                                .trim()
                                );

                                video.put(
                                        "url",
                                        url.getText()
                                                .toString()
                                                .trim()
                                );

                            } catch (
                                    Exception ignored
                            ) {
                            }

                            JSONArray videos =
                                    getArray(
                                            KEY_VIDEOS
                                    );

                            videos.put(
                                    video
                            );

                            saveArray(
                                    KEY_VIDEOS,
                                    videos
                            );

                            videosManager();
                        }
                )
                .setNegativeButton(
                        "إلغاء",
                        null
                )
                .show();
    }

    private void settingsScreen() {

        baseScreen();

        header(
                "الإعدادات",
                "إعدادات التطبيق"
        );

        LinearLayout content =
                vertical();

        content.setPadding(
                20,
                10,
                20,
                20
        );

        LinearLayout dark =
                card();

        TextView darkTitle =
                boldText(
                        "🌙 الوضع الداكن",
                        19,
                        GOLD,
                        Gravity.CENTER
                );

        TextView darkState =
                text(
                        darkMode
                                ? "مفعّل"
                                : "غير مفعّل",
                        15,
                        subTextColor(),
                        Gravity.CENTER
                );

        dark.addView(
                darkTitle,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        dark.addView(
                darkState,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        dark.setOnClickListener(
                v -> {

                    darkMode =
                            !darkMode;

                    prefs.edit()
                            .putBoolean(
                                    KEY_DARK,
                                    darkMode
                            )
                            .apply();

                    settingsScreen();
                }
        );

        content.addView(
                dark,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        110,
                        0,
                        10,
                        0,
                        10
                )
        );

        LinearLayout brightness =
                card();

        brightness.addView(
                boldText(
                        "☀ سطوع الشاشة",
                        19,
                        GOLD,
                        Gravity.CENTER
                ),
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        brightness.addView(
                text(
                        "فتح إعدادات السطوع",
                        14,
                        subTextColor(),
                        Gravity.CENTER
                ),
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        brightness.setOnClickListener(
                v -> {

                    try {

                        startActivity(
                                new Intent(
                                        Settings.ACTION_DISPLAY_SETTINGS
                                )
                        );

                    } catch (
                            Exception ignored
                    ) {
                    }
                }
        );

        content.addView(
                brightness,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        110,
                        0,
                        10,
                        0,
                        10
                )
        );

        Button back =
                button(
                        "رجوع"
                );

        back.setOnClickListener(
                v -> showRoleScreen()
        );

        content.addView(
                back,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        20,
                        0,
                        10
                )
        );

        root.addView(
                content,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                )
        );
    }

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

        } catch (
                Exception e
        ) {

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

    @Override
    public void onBackPressed() {

        showRoleScreen();
    }
}
                                    if (
                                            name == null ||
                                            name.trim().isEmpty()
                                    ) {
                                        name =
                                                doc.getString(
                                                        "title"
                                                );
                                    }

                                    if (
                                            name == null ||
                                            name.trim().isEmpty()
                                    ) {
                                        name = "امتحان";
                                    }

                                    String examId =
                                            doc.getId();

                                    int questionCount = 0;

                                    Object questions =
                                            doc.get("questions");

                                    if (
                                            questions instanceof ArrayList
                                    ) {
                                        questionCount =
                                                ((ArrayList<?>) questions)
                                                        .size();
                                    }

                                    LinearLayout c =
                                            card();

                                    c.addView(
                                            boldText(
                                                    "📝 " + name,
                                                    19,
                                                    GOLD,
                                                    Gravity.CENTER
                                            ),
                                            lp(
                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                            )
                                    );

                                    c.addView(
                                            text(
                                                    "عدد الأسئلة: " +
                                                            questionCount,
                                                    14,
                                                    subTextColor(),
                                                    Gravity.CENTER
                                            ),
                                            lp(
                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                            )
                                    );

                                    Button edit =
                                            button(
                                                    "✏ تعديل الامتحان"
                                            );

                                    String finalExamId =
                                            examId;

                                    edit.setOnClickListener(
                                            v ->
                                                    editExamScreen(
                                                            finalExamId
                                                    )
                                    );

                                    c.addView(
                                            edit,
                                            lpMargin(
                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                    55,
                                                    0,
                                                    8,
                                                    0,
                                                    5
                                            )
                                    );

                                    Button createCode =
                                            button(
                                                    "🔑 إنشاء كود"
                                            );

                                    createCode.setOnClickListener(
                                            v ->
                                                    createCodeForExam(
                                                            finalExamId,
                                                            name
                                                    )
                                    );

                                    c.addView(
                                            createCode,
                                            lpMargin(
                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                    55,
                                                    0,
                                                    5,
                                                    0,
                                                    5
                                            )
                                    );

                                    Button delete =
                                            button(
                                                    "🗑 حذف الامتحان"
                                            );

                                    delete.setOnClickListener(
                                            v ->
                                                    confirmDeleteExam(
                                                            finalExamId,
                                                            name
                                                    )
                                    );

                                    c.addView(
                                            delete,
                                            lpMargin(
                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                    55,
                                                    0,
                                                    5,
                                                    0,
                                                    5
                                            )
                                    );

                                    content.addView(
                                            c,
                                            lpMargin(
                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                                    0,
                                                    8,
                                                    0,
                                                    8
                                            )
                                    );
                                }
                            }
                    )
                    .addOnFailureListener(
                            e -> {

                                progress.setVisibility(
                                        View.GONE
                                );

                                Toast.makeText(
                                        MainActivity.this,
                                        "تعذر تحميل الامتحانات",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                    );
        });
    }

    private void createExamScreen() {

        baseScreen();

        header(
                "إنشاء امتحان",
                "اكتب بيانات الامتحان والأسئلة"
        );

        ScrollView s =
                scroll();

        LinearLayout content =
                vertical();

        content.setPadding(
                20,
                10,
                20,
                25
        );

        EditText title =
                input(
                        "اسم الامتحان"
                );

        content.addView(
                title,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        5,
                        0,
                        10
                )
        );

        EditText duration =
                input(
                        "مدة الامتحان بالدقائق"
                );

        duration.setInputType(
                android.text.InputType.TYPE_CLASS_NUMBER
        );

        content.addView(
                duration,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        5,
                        0,
                        15
                )
        );

        TextView questionsTitle =
                boldText(
                        "الأسئلة",
                        22,
                        GOLD,
                        Gravity.RIGHT
                );

        content.addView(
                questionsTitle,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        0,
                        10,
                        0,
                        10
                )
        );

        LinearLayout questionsContainer =
                vertical();

        content.addView(
                questionsContainer,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        addQuestionEditor(
                questionsContainer,
                1
        );

        Button addQuestion =
                button(
                        "➕ إضافة سؤال"
                );

        addQuestion.setOnClickListener(
                v ->
                        addQuestionEditor(
                                questionsContainer,
                                questionsContainer.getChildCount() + 1
                        )
        );

        content.addView(
                addQuestion,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        15,
                        0,
                        15
                )
        );

        Button save =
                button(
                        "💾 حفظ الامتحان"
                );

        save.setOnClickListener(
                v ->
                        saveNewExam(
                                title,
                                duration,
                                questionsContainer
                        )
        );

        content.addView(
                save,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        65,
                        0,
                        5,
                        0,
                        10
                )
        );

        Button back =
                button(
                        "رجوع"
                );

        back.setOnClickListener(
                v ->
                        examManager()
        );

        content.addView(
                back,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60
                )
        );

        s.addView(
                content
        );

        root.addView(
                s,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                )
        );
    }

    private void addQuestionEditor(
            LinearLayout parent,
            int number
    ) {

        LinearLayout box =
                card();

        box.setGravity(
                Gravity.RIGHT
        );

        TextView numberText =
                boldText(
                        "السؤال " + number,
                        19,
                        GOLD,
                        Gravity.RIGHT
                );

        box.addView(
                numberText,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        EditText question =
                input(
                        "نص السؤال"
                );

        box.addView(
                question,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        8,
                        0,
                        6
                )
        );

        EditText a =
                input(
                        "الإجابة أ"
                );

        box.addView(
                a,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        55,
                        0,
                        5,
                        0,
                        5
                )
        );

        EditText b =
                input(
                        "الإجابة ب"
                );

        box.addView(
                b,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        55,
                        0,
                        5,
                        0,
                        5
                )
        );

        EditText c =
                input(
                        "الإجابة ج"
                );

        box.addView(
                c,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        55,
                        0,
                        5,
                        0,
                        5
                )
        );

        EditText d =
                input(
                        "الإجابة د"
                );

        box.addView(
                d,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        55,
                        0,
                        5,
                        0,
                        5
                )
        );

        EditText answer =
                input(
                        "رقم الإجابة الصحيحة: 1 أو 2 أو 3 أو 4"
                );

        answer.setInputType(
                android.text.InputType.TYPE_CLASS_NUMBER
        );

        box.addView(
                answer,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        8,
                        0,
                        5
                )
        );

        EditText explanation =
                input(
                        "شرح الإجابة"
                );

        explanation.setSingleLine(
                false
        );

        explanation.setMinLines(
                2
        );

        box.addView(
                explanation,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        90,
                        0,
                        5,
                        0,
                        5
                )
        );

        box.setTag(
                new EditText[]{
                        question,
                        a,
                        b,
                        c,
                        d,
                        answer,
                        explanation
                }
        );

        parent.addView(
                box,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        0,
                        7,
                        0,
                        7
                )
        );
    }

    private void saveNewExam(
            EditText title,
            EditText duration,
            LinearLayout questionsContainer
    ) {

        String examTitle =
                title.getText()
                        .toString()
                        .trim();

        String durationText =
                duration.getText()
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

        if (
                durationText.isEmpty()
        ) {

            duration.setError(
                    "اكتب مدة الامتحان"
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

            Toast.makeText(
                    this,
                    "أضف سؤالًا واحدًا على الأقل",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        JSONObject exam =
                new JSONObject();

        String id =
                UUID.randomUUID()
                        .toString();

        try {

            exam.put(
                    "id",
                    id
            );

            exam.put(
                    "name",
                    examTitle
            );

            exam.put(
                    "title",
                    examTitle
            );

            exam.put(
                    "duration",
                    Integer.parseInt(
                            durationText
                    )
            );

            exam.put(
                    "questions",
                    questions
            );

        } catch (
                Exception e
        ) {

            Toast.makeText(
                    this,
                    "حدث خطأ أثناء إنشاء الامتحان",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        JSONArray local =
                getArray(
                        KEY_EXAMS
                );

        local.put(
                exam
        );

        saveArray(
                KEY_EXAMS,
                local
        );

        ensureFirebaseAuth(
                () -> {

                    Map<String, Object> data =
                            jsonToMap(
                                    exam
                            );

                    db.collection("exams")
                            .document(id)
                            .set(data)
                            .addOnSuccessListener(
                                    v -> {

                                        Toast.makeText(
                                                MainActivity.this,
                                                "تم إنشاء الامتحان بنجاح",
                                                Toast.LENGTH_SHORT
                                        ).show();

                                        examManager();
                                    }
                            )
                            .addOnFailureListener(
                                    e -> {

                                        Toast.makeText(
                                                MainActivity.this,
                                                "تم حفظ الامتحان على الهاتف، لكن تعذر رفعه للسيرفر",
                                                Toast.LENGTH_LONG
                                        ).show();

                                        examManager();
                                    }
                            );
                }
        );
    }

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

            Object tag =
                    view.getTag();

            if (
                    !(tag instanceof EditText[])
            ) {
                continue;
            }

            EditText[] fields =
                    (EditText[]) tag;

            String question =
                    fields[0]
                            .getText()
                            .toString()
                            .trim();

            String a =
                    fields[1]
                            .getText()
                            .toString()
                            .trim();

            String b =
                    fields[2]
                            .getText()
                            .toString()
                            .trim();

            String c =
                    fields[3]
                            .getText()
                            .toString()
                            .trim();

            String d =
                    fields[4]
                            .getText()
                            .toString()
                            .trim();

            String answer =
                    fields[5]
                            .getText()
                            .toString()
                            .trim();

            String explanation =
                    fields[6]
                            .getText()
                            .toString()
                            .trim();

            if (
                    question.isEmpty()
            ) {
                continue;
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

            } catch (
                    Exception ignored
            ) {
            }
        }

        return result;
    }

    private void editExamScreen(
            String examId
    ) {

        ensureFirebaseAuth(
                () -> {

                    findExamFromFirebase(
                            examId,
                            exam -> {

                                if (
                                        exam == null
                                ) {

                                    Toast.makeText(
                                            MainActivity.this,
                                            "الامتحان غير موجود",
                                            Toast.LENGTH_LONG
                                    ).show();

                                    return;
                                }

                                showEditExam(
                                        exam
                                );
                            }
                    );
                }
        );
    }

    private void showEditExam(
            JSONObject exam
    ) {

        baseScreen();

        header(
                "تعديل الامتحان",
                exam.optString(
                        "name",
                        "الامتحان"
                )
        );

        ScrollView s =
                scroll();

        LinearLayout content =
                vertical();

        content.setPadding(
                20,
                10,
                20,
                25
        );

        EditText title =
                input(
                        "اسم الامتحان"
                );

        title.setText(
                exam.optString(
                        "name",
                        exam.optString(
                                "title",
                                ""
                        )
                )
        );

        content.addView(
                title,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        5,
                        0,
                        10
                )
        );

        EditText duration =
                input(
                        "مدة الامتحان"
                );

        duration.setInputType(
                android.text.InputType.TYPE_CLASS_NUMBER
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
                duration,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        5,
                        0,
                        15
                )
        );

        LinearLayout questionsContainer =
                vertical();

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

                    JSONObject q =
                            questions.getJSONObject(
                                    i
                            );

                    addExistingQuestionEditor(
                            questionsContainer,
                            i + 1,
                            q
                    );

                } catch (
                        Exception ignored
                ) {
                }
            }
        }

        content.addView(
                questionsContainer,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        Button addQuestion =
                button(
                        "➕ إضافة سؤال"
                );

        addQuestion.setOnClickListener(
                v ->
                        addQuestionEditor(
                                questionsContainer,
                                questionsContainer.getChildCount() + 1
                        )
        );

        content.addView(
                addQuestion,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        15,
                        0,
                        15
                )
        );

        Button save =
                button(
                        "💾 حفظ التعديلات"
                );

        save.setOnClickListener(
                v ->
                        saveEditedExam(
                                exam,
                                title,
                                duration,
                                questionsContainer
                        )
        );

        content.addView(
                save,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        65,
                        0,
                        5,
                        0,
                        10
                )
        );

        Button back =
                button(
                        "رجوع"
                );

        back.setOnClickListener(
                v ->
                        examManager()
        );

        content.addView(
                back,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60
                )
        );

        s.addView(
                content
        );

        root.addView(
                s,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                )
        );
    }

    private void addExistingQuestionEditor(
            LinearLayout parent,
            int number,
            JSONObject q
    ) {

        LinearLayout box =
                card();

        box.setGravity(
                Gravity.RIGHT
        );

        box.addView(
                boldText(
                        "السؤال " + number,
                        19,
                        GOLD,
                        Gravity.RIGHT
                ),
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        EditText question =
                input(
                        "نص السؤال"
                );

        question.setText(
                q.optString(
                        "question",
                        ""
                )
        );

        box.addView(
                question,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        8,
                        0,
                        6
                )
        );

        EditText a =
                input(
                        "الإجابة أ"
                );

        a.setText(
                q.optString(
                        "a",
                        ""
                )
        );

        box.addView(
                a,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        55,
                        0,
                        5,
                        0,
                        5
                )
        );

        EditText b =
                input(
                        "الإجابة ب"
                );

        b.setText(
                q.optString(
                        "b",
                        ""
                )
        );

        box.addView(
                b,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        55,
                        0,
                        5,
                        0,
                        5
                )
        );

        EditText c =
                input(
                        "الإجابة ج"
                );

        c.setText(
                q.optString(
                        "c",
                        ""
                )
        );

        box.addView(
                c,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        55,
                        0,
                        5,
                        0,
                        5
                )
        );

        EditText d =
                input(
                        "الإجابة د"
                );

        d.setText(
                q.optString(
                        "d",
                        ""
                )
        );

        box.addView(
                d,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        55,
                        0,
                        5,
                        0,
                        5
                )
        );

        EditText answer =
                input(
                        "رقم الإجابة الصحيحة"
                );

        answer.setInputType(
                android.text.InputType.TYPE_CLASS_NUMBER
        );

        answer.setText(
                String.valueOf(
                        q.optInt(
                                "answer",
                                1
                        )
                )
        );

        box.addView(
                answer,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        8,
                        0,
                        5
                )
        );

        EditText explanation =
                input(
                        "شرح الإجابة"
                );

        explanation.setSingleLine(
                false
        );

        explanation.setMinLines(
                2
        );

        explanation.setText(
                q.optString(
                        "explanation",
                        ""
                )
        );

        box.addView(
                explanation,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        90,
                        0,
                        5,
                        0,
                        5
                )
        );

        box.setTag(
                new EditText[]{
                        question,
                        a,
                        b,
                        c,
                        d,
                        answer,
                        explanation
                }
        );

        parent.addView(
                box,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        0,
                        7,
                        0,
                        7
                )
        );
    }

    private void saveEditedExam(
            JSONObject oldExam,
            EditText title,
            EditText duration,
            LinearLayout questionsContainer
    ) {

        String name =
                title.getText()
                        .toString()
                        .trim();

        String durationText =
                duration.getText()
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

            Toast.makeText(
                    this,
                    "الامتحان يحتاج إلى سؤال واحد على الأقل",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        String id =
                oldExam.optString(
                        "id",
                        ""
                );

        JSONObject updated =
                new JSONObject();

        try {

            updated.put(
                    "id",
                    id
            );

            updated.put(
                    "name",
                    name
            );

            updated.put(
                    "title",
                    name
            );

            updated.put(
                    "duration",
                    Integer.parseInt(
                            durationText
                    )
            );

            updated.put(
                    "questions",
                    questions
            );

        } catch (
                Exception e
        ) {

            Toast.makeText(
                    this,
                    "تعذر حفظ التعديلات",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        JSONArray local =
                getArray(
                        KEY_EXAMS
                );

        for (
                int i = 0;
                i < local.length();
                i++
        ) {

            try {

                JSONObject item =
                        local.getJSONObject(i);

                if (
                        item.optString(
                                "id",
                                ""
                        ).equals(
                                id
                        )
                ) {

                    local.put(
                            i,
                            updated
                    );

                    break;
                }

            } catch (
                    Exception ignored
            ) {
            }
        }

        saveArray(
                KEY_EXAMS,
                local
        );

        ensureFirebaseAuth(
                () -> {

                    db.collection("exams")
                            .document(id)
                            .set(
                                    jsonToMap(
                                            updated
                                    )
                            )
                            .addOnSuccessListener(
                                    v -> {

                                        Toast.makeText(
                                                MainActivity.this,
                                                "تم حفظ التعديلات",
                                                Toast.LENGTH_SHORT
                                        ).show();

                                        examManager();
                                    }
                            )
                            .addOnFailureListener(
                                    e -> {

                                        Toast.makeText(
                                                MainActivity.this,
                                                "تم الحفظ على الهاتف لكن تعذر التحديث على السيرفر",
                                                Toast.LENGTH_LONG
                                        ).show();

                                        examManager();
                                    }
                            );
                }
        );
    }

    private void confirmDeleteExam(
            String examId,
            String examName
    ) {

        new AlertDialog.Builder(this)
                .setTitle(
                        "حذف الامتحان"
                )
                .setMessage(
                        "هل تريد حذف \"" +
                                examName +
                                "\"؟"
                )
                .setNegativeButton(
                        "إلغاء",
                        null
                )
                .setPositiveButton(
                        "حذف",
                        (dialog, which) ->
                                deleteExam(
                                        examId
                                )
                )
                .show();
    }

    private void deleteExam(
            String examId
    ) {

        ensureFirebaseAuth(
                () -> {

                    db.collection("exams")
                            .document(examId)
                            .delete()
                            .addOnSuccessListener(
                                    v -> {

                                        JSONArray local =
                                                getArray(
                                                        KEY_EXAMS
                                                );

                                        JSONArray updated =
                                                new JSONArray();

                                        for (
                                                int i = 0;
                                                i < local.length();
                                                i++
                                        ) {

                                            try {

                                                JSONObject exam =
                                                        local.getJSONObject(i);

                                                if (
                                                        !exam.optString(
                                                                "id",
                                                                ""
                                                        ).equals(
                                                                examId
                                                        )
                                                ) {

                                                    updated.put(
                                                            exam
                                                    );
                                                }

                                            } catch (
                                                    Exception ignored
                                            ) {
                                            }
                                        }

                                        saveArray(
                                                KEY_EXAMS,
                                                updated
                                        );

                                        Toast.makeText(
                                                MainActivity.this,
                                                "تم حذف الامتحان",
                                                Toast.LENGTH_SHORT
                                        ).show();

                                        examManager();
                                    }
                            )
                            .addOnFailureListener(
                                    e ->
                                            Toast.makeText(
                                                    MainActivity.this,
                                                    "تعذر حذف الامتحان",
                                                    Toast.LENGTH_LONG
                                            ).show()
                            );
                }
        );
    }

    private void createCodeForExam(
            String examId,
            String examName
    ) {

        final EditText code =
                input(
                        "اكتب كود الامتحان"
                );

        code.setInputType(
                android.text.InputType.TYPE_CLASS_NUMBER
        );

        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle(
                                "إنشاء كود"
                        )
                        .setMessage(
                                "الامتحان: " +
                                        examName
                        )
                        .setView(
                                code
                        )
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

                    Button save =
                            dialog.getButton(
                                    AlertDialog.BUTTON_POSITIVE
                            );

                    save.setOnClickListener(
                            v -> {

                                String value =
                                        code.getText()
                                                .toString()
                                                .trim();

                                if (
                                        value.isEmpty()
                                ) {

                                    code.setError(
                                            "اكتب الكود"
                                    );

                                    return;
                                }

                                saveExamCode(
                                        examId,
                                        examName,
                                        value,
                                        dialog
                                );
                            }
                    );
                }
        );

        dialog.show();
    }

    private void saveExamCode(
            String examId,
            String examName,
            String code,
            AlertDialog dialog
    ) {

        ensureFirebaseAuth(
                () -> {

                    Map<String, Object> data =
                            new HashMap<>();

                    data.put(
                            "code",
                            code
                    );

                    data.put(
                            "examId",
                            examId
                    );

                    data.put(
                            "examName",
                            examName
                    );

                    data.put(
                            "createdBy",
                            firebaseAuth
                                    .getCurrentUser()
                                    .getUid()
                    );

                    db.collection("codes")
                            .document(code)
                            .set(data)
                            .addOnSuccessListener(
                                    v -> {

                                        JSONArray local =
                                                getArray(
                                                        KEY_CODES
                                                );

                                        JSONObject item =
                                                new JSONObject();

                                        try {

                                            item.put(
                                                    "code",
                                                    code
                                            );

                                            item.put(
                                                    "examId",
                                                    examId
                                            );

                                            item.put(
                                                    "examName",
                                                    examName
                                            );

                                            local.put(
                                                    item
                                            );

                                            saveArray(
                                                    KEY_CODES,
                                                    local
                                            );

                                        } catch (
                                                Exception ignored
                                        ) {
                                        }

                                        dialog.dismiss();

                                        Toast.makeText(
                                                MainActivity.this,
                                                "تم إنشاء الكود بنجاح",
                                                Toast.LENGTH_SHORT
                                        ).show();

                                        codesManager();
                                    }
                            )
                            .addOnFailureListener(
                                    e ->
                                            Toast.makeText(
                                                    MainActivity.this,
                                                    "تعذر حفظ الكود على السيرفر",
                                                    Toast.LENGTH_LONG
                                            ).show()
                            );
                }
        );
    }

    private void codesManager() {

        baseScreen();

        header(
                "أكواد الامتحانات",
                "الأكواد الموجودة على السيرفر"
        );

        LinearLayout content =
                vertical();

        content.setPadding(
                20,
                10,
                20,
                20
        );

        ProgressBar progress =
                new ProgressBar(this);

        content.addView(
                progress,
                lpMargin(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        0,
                        20,
                        0,
                        20
                )
        );

        root.addView(
                content,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                )
        );

        ensureFirebaseAuth(
                () -> {

                    db.collection("codes")
                            .get()
                            .addOnSuccessListener(
                                    snapshot -> {

                                        progress.setVisibility(
                                                View.GONE
                                        );

                                        if (
                                                snapshot.isEmpty()
                                        ) {

                                            content.addView(
                                                    text(
                                                            "لا توجد أكواد",
                                                            17,
                                                            subTextColor(),
                                                            Gravity.CENTER
                                                    ),
                                                    lpMargin(
                                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                                            0,
                                                            20,
                                                            0,
                                                            20
                                                    )
                                            );

                                            return;
                                        }

                                        for (
                                                QueryDocumentSnapshot doc
                                                : snapshot
                                        ) {

                                            String code =
                                                    doc.getString(
                                                            "code"
                                                    );

                                            String examName =
                                                    doc.getString(
                                                            "examName"
                                                    );

                                            if (
                                                    code == null
                                            ) {
                                                code = doc.getId();
                                            }

                                            if (
                                                    examName == null
                                            ) {
                                                examName =
                                                        "الامتحان";
                                            }

                                            LinearLayout c =
                                                    card();

                                            c.addView(
                                                    boldText(
                                                            "🔑 " + code,
                                                            22,
                                                            GOLD,
                                                            Gravity.CENTER
                                                    ),
                                                    lp(
                                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                                    )
                                            );

                                            c.addView(
                                                    text(
                                                            examName,
                                                            15,
                                                            textColor(),
                                                            Gravity.CENTER
                                                    ),
                                                    lp(
                                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                                    )
                                            );

                                            Button delete =
                                                    button(
                                                            "🗑 حذف الكود"
                                                    );

                                            String finalCode =
                                                    code;

                                            delete.setOnClickListener(
                                                    v ->
                                                            deleteCode(
                                                                    finalCode
                                                            )
                                            );

                                            c.addView(
                                                    delete,
                                                    lpMargin(
                                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                                            55,
                                                            0,
                                                            10,
                                                            0,
                                                            0
                                                    )
                                            );

                                            content.addView(
                                                    c,
                                                    lpMargin(
                                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                                            0,
                                                            7,
                                                            0,
                                                            7
                                                    )
                                            );
                                        }
                                    }
                            )
                            .addOnFailureListener(
                                    e -> {

                                        progress.setVisibility(
                                                View.GONE
                                        );

                                        Toast.makeText(
                                                MainActivity.this,
                                                "تعذر تحميل الأكواد",
                                                Toast.LENGTH_LONG
                                        ).show();
                                    }
                            );
                }
        );
    }

    private void deleteCode(
            String code
    ) {

        ensureFirebaseAuth(
                () ->
                        db.collection("codes")
                                .document(code)
                                .delete()
                                .addOnSuccessListener(
                                        v -> {

                                            Toast.makeText(
                                                    MainActivity.this,
                                                    "تم حذف الكود",
                                                    Toast.LENGTH_SHORT
                                            ).show();

                                            codesManager();
                                        }
                                )
                                .addOnFailureListener(
                                        e ->
                                                Toast.makeText(
                                                        MainActivity.this,
                                                        "تعذر حذف الكود",
                                                        Toast.LENGTH_LONG
                                                ).show()
                                )
        );
    }

    private void teacherResults() {

        baseScreen();

        header(
                "نتائج الطلاب",
                "النتائج المسجلة على السيرفر"
        );

        ScrollView s =
                scroll();

        LinearLayout content =
                vertical();

        content.setPadding(
                20,
                10,
                20,
                25
        );

        ProgressBar progress =
                new ProgressBar(this);

        content.addView(
                progress,
                lpMargin(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        0,
                        20,
                        0,
                        20
                )
        );

        s.addView(
                content
        );

        root.addView(
                s,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                )
        );

        ensureFirebaseAuth(
                () -> {

                    db.collection("results")
                            .get()
                            .addOnSuccessListener(
                                    snapshot -> {

                                        progress.setVisibility(
                                                View.GONE
                                        );

                                        if (
                                                snapshot.isEmpty()
                                        ) {

                                            content.addView(
                                                    text(
                                                            "لا توجد نتائج حتى الآن",
                                                            17,
                                                            subTextColor(),
                                                            Gravity.CENTER
                                                    ),
                                                    lpMargin(
                                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                                            0,
                                                            25,
                                                            0,
                                                            20
                                                    )
                                            );

                                            return;
                                        }

                                        for (
                                                QueryDocumentSnapshot doc
                                                : snapshot
                                        ) {

                                            String student =
                                                    doc.getString(
                                                            "studentName"
                                                    );

                                            String exam =
                                                    doc.getString(
                                                            "examName"
                                                    );

                                            int correct =
                                                    getInt(
                                                            doc,
                                                            "correct"
                                                    );

                                            int total =
                                                    getInt(
                                                            doc,
                                                            "total"
                                                    );

                                            int answered =
                                                    getInt(
                                                            doc,
                                                            "answered"
                                                    );

                                            int wrong =
                                                    getInt(
                                                            doc,
                                                            "wrong"
                                                    );

                                            LinearLayout c =
                                                    card();

                                            c.setGravity(
                                                    Gravity.RIGHT
                                            );

                                            c.addView(
                                                    boldText(
                                                            "👨‍🎓 " +
                                                                    (student == null
                                                                            ? "طالب"
                                                                            : student),
                                                            19,
                                                            GOLD,
                                                            Gravity.RIGHT
                                                    ),
                                                    lp(
                                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                                    )
                                            );

                                            c.addView(
                                                    text(
                                                            "الامتحان: " +
                                                                    (exam == null
                                                                            ? "امتحان"
                                                                            : exam),
                                                            15,
                                                            textColor(),
                                                            Gravity.RIGHT
                                                    ),
                                                    lp(
                                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                                    )
                                            );

                                            c.addView(
                                                    text(
                                                            "النتيجة: " +
                                                                    correct +
                                                                    " / " +
                                                                    total,
                                                            17,
                                                            textColor(),
                                                            Gravity.RIGHT
                                                    ),
                                                    lp(
                                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                                    )
                                            );

                                            c.addView(
                                                    text(
                                                            "تمت الإجابة: " +
                                                                    answered +
                                                                    " | الخطأ: " +
                                                                    wrong,
                                                            14,
                                                            subTextColor(),
                                                            Gravity.RIGHT
                                                    ),
                                                    lp(
                                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                                    )
                                            );

                                            content.addView(
                                                    c,
                                                    lpMargin(
                                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                                            0,
                                                            7,
                                                            0,
                                                            7
                                                    )
                                            );
                                        }
                                    }
                            )
                            .addOnFailureListener(
                                    e -> {

                                        progress.setVisibility(
                                                View.GONE
                                        );

                                        Toast.makeText(
                                                MainActivity.this,
                                                "تعذر تحميل النتائج",
                                                Toast.LENGTH_LONG
                                        ).show();
                                    }
                            );
                }
        );
    }

    private void notesManager() {

        baseScreen();

        header(
                "مذكرات الشرح",
                "إضافة المذكرات للطلاب"
        );

        ScrollView s =
                scroll();

        LinearLayout content =
                vertical();

        content.setPadding(
                20,
                10,
                20,
                20
        );

        EditText title =
                input(
                        "عنوان المذكرة"
                );

        content.addView(
                title,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        5,
                        0,
                        10
                )
        );

        EditText body =
                input(
                        "محتوى المذكرة"
                );

        body.setSingleLine(
                false
        );

        body.setMinLines(
                5
        );

        content.addView(
                body,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        180,
                        0,
                        5,
                        0,
                        10
                )
        );

        Button save =
                button(
                        "💾 إضافة المذكرة"
                );

        save.setOnClickListener(
                v -> {

                    String t =
                            title.getText()
                                    .toString()
                                    .trim();

                    String b =
                            body.getText()
                                    .toString()
                                    .trim();

                    if (
                            t.isEmpty() ||
                            b.isEmpty()
                    ) {

                        Toast.makeText(
                                this,
                                "اكتب عنوان ومحتوى المذكرة",
                                Toast.LENGTH_LONG
                        ).show();

                        return;
                    }

                    JSONArray notes =
                            getArray(
                                    KEY_NOTES
                            );

                    JSONObject n =
                            new JSONObject();

                    try {

                        n.put(
                                "title",
                                t
                        );

                        n.put(
                                "content",
                                b
                        );

                        notes.put(
                                n
                        );

                        saveArray(
                                KEY_NOTES,
                                notes
                        );

                        Toast.makeText(
                                this,
                                "تمت إضافة المذكرة",
                                Toast.LENGTH_SHORT
                        ).show();

                        title.setText(
                                ""
                        );

                        body.setText(
                                ""
                        );

                    } catch (
                            Exception ignored
                    ) {
                    }
                }
        );

        content.addView(
                save,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        5,
                        0,
                        20
                )
        );

        TextView listTitle =
                boldText(
                        "المذكرات الموجودة",
                        20,
                        GOLD,
                        Gravity.RIGHT
                );

        content.addView(
                listTitle,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        0,
                        10,
                        0,
                        10
                )
        );

        JSONArray notes =
                getArray(
                        KEY_NOTES
                );

        for (
                int i = 0;
                i < notes.length();
                i++
        ) {

            try {

                JSONObject n =
                        notes.getJSONObject(
                                i
                        );

                content.addView(
                        text(
                                "• " +
                                        n.optString(
                                                "title",
                                                "مذكرة"
                                        ),
                                16,
                                textColor(),
                                Gravity.RIGHT
                        ),
                        lpMargin(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                0,
                                5,
                                0,
                                5
                        )
                );

            } catch (
                    Exception ignored
            ) {
            }
        }

        s.addView(
                content
        );

        root.addView(
                s,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                )
        );
    }

    private void videosManager() {

        baseScreen();

        header(
                "الفيديوهات",
                "إضافة فيديوهات تعليمية"
        );

        ScrollView s =
                scroll();

        LinearLayout content =
                vertical();

        content.setPadding(
                20,
                10,
                20,
                20
        );

        EditText title =
                input(
                        "عنوان الفيديو"
                );

        content.addView(
                title,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        5,
                        0,
                        10
                )
        );

        EditText url =
                input(
                        "رابط الفيديو"
                );

        content.addView(
                url,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        5,
                        0,
                        10
                )
        );

        Button save =
                button(
                        "💾 إضافة الفيديو"
                );

        save.setOnClickListener(
                v -> {

                    String t =
                            title.getText()
                                    .toString()
                                    .trim();

                    String u =
                            url.getText()
                                    .toString()
                                    .trim();

                    if (
                            t.isEmpty() ||
                            u.isEmpty()
                    ) {

                        Toast.makeText(
                                this,
                                "اكتب عنوان ورابط الفيديو",
                                Toast.LENGTH_LONG
                        ).show();

                        return;
                    }

                    JSONArray videos =
                            getArray(
                                    KEY_VIDEOS
                            );

                    JSONObject item =
                            new JSONObject();

                    try {

                        item.put(
                                "title",
                                t
                        );

                        item.put(
                                "url",
                                u
                        );

                        videos.put(
                                item
                        );

                        saveArray(
                                KEY_VIDEOS,
                                videos
                        );

                        Toast.makeText(
                                this,
                                "تمت إضافة الفيديو",
                                Toast.LENGTH_SHORT
                        ).show();

                        title.setText(
                                ""
                        );

                        url.setText(
                                ""
                        );

                    } catch (
                            Exception ignored
                    ) {
                    }
                }
        );

        content.addView(
                save,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        5,
                        0,
                        20
                )
        );

        JSONArray videos =
                getArray(
                        KEY_VIDEOS
                );

        for (
                int i = 0;
                i < videos.length();
                i++
        ) {

            try {

                JSONObject item =
                        videos.getJSONObject(
                                i
                        );

                content.addView(
                        text(
                                "• " +
                                        item.optString(
                                                "title",
                                                "فيديو"
                                        ),
                                16,
                                textColor(),
                                Gravity.RIGHT
                        ),
                        lpMargin(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                0,
                                5,
                                0,
                                5
                        )
                );

            } catch (
                    Exception ignored
            ) {
            }
        }

        s.addView(
                content
        );

        root.addView(
                s,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                )
        );
    }

    private void settingsScreen() {

        baseScreen();

        header(
                "الإعدادات",
                "إعدادات تطبيق المايسترو"
        );

        LinearLayout content =
                vertical();

        content.setPadding(
                20,
                15,
                20,
                20
        );

        TextView mode =
                boldText(
                        darkMode
                                ? "الوضع الحالي: داكن 🌙"
                                : "الوضع الحالي: فاتح ☀️",
                        19,
                        GOLD,
                        Gravity.CENTER
                );

        content.addView(
                mode,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        0,
                        10,
                        0,
                        15
                )
        );

        Button toggle =
                button(
                        darkMode
                                ? "☀ تفعيل الوضع الفاتح"
                                : "🌙 تفعيل الوضع الداكن"
                );

        toggle.setOnClickListener(
                v -> {

                    darkMode =
                            !darkMode;

                    prefs.edit()
                            .putBoolean(
                                    KEY_DARK,
                                    darkMode
                            )
                            .apply();

                    settingsScreen();
                }
        );

        content.addView(
                toggle,
                lpMargin(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60,
                        0,
                        5,
                        0,
                        15
                )
        );

        Button back =
                button(
                        "رجوع"
                );

        back.setOnClickListener(
                v ->
                        showRoleScreen()
        );

        content.addView(
                back,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        60
                )
        );

        root.addView(
                content,
                lp(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                )
        );
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

    private JSONArray getArray(
            String key
    ) {

        String value =
                prefs.getString(
                        key,
                        "[]"
                );

        try {

            return new JSONArray(
                    value
            );

        } catch (
                Exception e
        ) {

            return new JSONArray();
        }
    }

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
                        exam.optString(
                                "id",
                                ""
                        ).equals(
                                id
                        )
                ) {

                    return exam;
                }

            } catch (
                    Exception ignored
            ) {
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

                JSONObject item =
                        codes.getJSONObject(
                                i
                        );

                if (
                        item.optString(
                                "code",
                                ""
                        ).equals(
                                code
                        )
                ) {

                    return findExam(
                            item.optString(
                                    "examId",
                                    ""
                            )
                    );
                }

            } catch (
                    Exception ignored
            ) {
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

        String id =
                updated.optString(
                        "id",
                        ""
                );

        for (
                int i = 0;
                i < exams.length();
                i++
        ) {

            try {

                JSONObject item =
                        exams.getJSONObject(
                                i
                        );

                if (
                        item.optString(
                                "id",
                                ""
                        ).equals(
                                id
                        )
                ) {

                    exams.put(
                            i,
                            updated
                    );

                    break;
                }

            } catch (
                    Exception ignored
            ) {
            }
        }

        saveArray(
                KEY_EXAMS,
                exams
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

    @Override
    public void onBackPressed() {

        showRoleScreen();
    }
}
