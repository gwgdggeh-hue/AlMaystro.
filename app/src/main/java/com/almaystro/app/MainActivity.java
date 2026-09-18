package com.almaystro.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity {

    private LinearLayout content;
    private LinearLayout bottomBar;
    private SharedPreferences prefs;
    private CountDownTimer examTimer;

    private boolean darkMode;
    private boolean examRunning = false;

    private String studentName = "";
    private String teacherName = "";
    private String activeExam = "";

    private int currentQuestion = 0;
    private int remainingSeconds = 0;

    private final ArrayList<JSONObject> questions = new ArrayList<>();
    private final ArrayList<Integer> answers = new ArrayList<>();

    private final int GREEN = Color.rgb(8, 67, 46);
    private final int GOLD = Color.rgb(220, 184, 70);
    private final int DARK = Color.rgb(25, 29, 27);
    private final int LIGHT = Color.rgb(247, 247, 244);
    private final int WHITE = Color.WHITE;
    private final int BLACK = Color.BLACK;
    private final int GRAY = Color.rgb(110, 110, 110);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
        );

        prefs = getSharedPreferences("almaystro_data", MODE_PRIVATE);

        darkMode = prefs.getBoolean("dark_mode", false);
        studentName = prefs.getString("student_name", "");
        teacherName = prefs.getString("teacher_name", "");

        createDefaultData();
        welcome();
    }

    private void createDefaultData() {
        if (!prefs.contains("exams")) {
            try {
                JSONArray exams = new JSONArray();

                JSONObject exam = new JSONObject();
                exam.put("name", "امتحان التاريخ التجريبي");
                exam.put("duration", 10);
                exam.put("code", "123456");

                JSONArray qs = new JSONArray();

                qs.put(makeQuestion(
                        "من هو مؤسس الدولة الأيوبية؟",
                        new String[]{
                                "صلاح الدين الأيوبي",
                                "عمرو بن العاص",
                                "محمد علي",
                                "أحمد عرابي"
                        },
                        0,
                        "صلاح الدين الأيوبي هو مؤسس الدولة الأيوبية."
                ));

                qs.put(makeQuestion(
                        "ما عاصمة مصر؟",
                        new String[]{
                                "الإسكندرية",
                                "القاهرة",
                                "الجيزة",
                                "أسوان"
                        },
                        1,
                        "القاهرة هي عاصمة جمهورية مصر العربية."
                ));

                qs.put(makeQuestion(
                        "من أسس مدينة القاهرة؟",
                        new String[]{
                                "الفاطميون",
                                "المماليك",
                                "الرومان",
                                "العثمانيون"
                        },
                        0,
                        "أسس الفاطميون مدينة القاهرة."
                ));

                exam.put("questions", qs);
                exams.put(exam);

                prefs.edit()
                        .putString("exams", exams.toString())
                        .apply();

            } catch (Exception ignored) {
            }
        }

        if (!prefs.contains("notes")) {
            try {
                JSONArray notes = new JSONArray();

                notes.put(makeNote(
                        "مراجعة التاريخ",
                        "راجع أهم الأحداث والشخصيات والتواريخ قبل دخول الامتحان."
                ));

                notes.put(makeNote(
                        "نصيحة المايسترو",
                        "اقرأ السؤال بهدوء وحدد المطلوب قبل اختيار الإجابة."
                ));

                prefs.edit()
                        .putString("notes", notes.toString())
                        .apply();

            } catch (Exception ignored) {
            }
        }

        if (!prefs.contains("groups")) {
            JSONArray groups = new JSONArray();

            groups.put("جروب أولى بكالوريا بنات");
            groups.put("جروب أولى بكالوريا ولاد");
            groups.put("جروب تانية بكالوريا بنات");
            groups.put("جروب تانية بكالوريا ولاد");
            groups.put("طلاب المايسترو تالتة إعدادي");
            groups.put("جروب ذكرى ومنفعة");

            prefs.edit()
                    .putString("groups", groups.toString())
                    .apply();
        }
    }

    private JSONObject makeQuestion(
            String title,
            String[] options,
            int correct,
            String explanation
    ) {
        JSONObject q = new JSONObject();

        try {
            q.put("text", title);
            q.put("correct", correct);
            q.put("explanation", explanation);

            JSONArray a = new JSONArray();

            for (String option : options) {
                a.put(option);
            }

            q.put("options", a);

        } catch (Exception ignored) {
        }

        return q;
    }

    private JSONObject makeNote(String title, String body) {
        JSONObject n = new JSONObject();

        try {
            n.put("title", title);
            n.put("body", body);
        } catch (Exception ignored) {
        }

        return n;
    }

    private void setup(String title) {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(darkMode ? DARK : LIGHT);

        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.VERTICAL);
        header.setGravity(Gravity.CENTER);
        header.setPadding(15, 25, 15, 25);
        header.setBackgroundColor(GREEN);

        TextView titleText = text(title, 23, GOLD);
        titleText.setGravity(Gravity.CENTER);
        titleText.setTypeface(Typeface.DEFAULT_BOLD);

        header.addView(titleText);

        root.addView(
                header,
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                )
        );

        ScrollView scroll = new ScrollView(this);

        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(18, 18, 18, 18);

        scroll.addView(content);

        root.addView(
                scroll,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        bottomBar = new LinearLayout(this);
        bottomBar.setOrientation(LinearLayout.HORIZONTAL);
        bottomBar.setGravity(Gravity.CENTER);
        bottomBar.setBackgroundColor(GREEN);

        root.addView(
                bottomBar,
                new LinearLayout.LayoutParams(
                        -1,
                        60
                )
        );

        setContentView(root);
    }

    private TextView text(String value, float size, int color) {
        TextView t = new TextView(this);

        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setPadding(5, 10, 5, 10);

        return t;
    }

    private EditText input(String hint) {
        EditText e = new EditText(this);

        e.setHint(hint);
        e.setTextSize(16);
        e.setSingleLine(true);
        e.setPadding(15, 10, 15, 10);

        e.setTextColor(darkMode ? WHITE : BLACK);
        e.setHintTextColor(
                darkMode ? Color.LTGRAY : GRAY
        );

        return e;
    }

    private Button button(
            String title,
            View.OnClickListener listener
    ) {
        Button b = new Button(this);

        b.setText(title);
        b.setTextSize(16);
        b.setTextColor(WHITE);
        b.setBackgroundColor(GREEN);
        b.setOnClickListener(listener);

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        p.setMargins(0, 7, 0, 7);
        b.setLayoutParams(p);

        return b;
    }

    private void bottomButton(
            String title,
            View.OnClickListener listener
    ) {
        Button b = new Button(this);

        b.setText(title);
        b.setTextSize(11);
        b.setTextColor(WHITE);
        b.setBackgroundColor(Color.TRANSPARENT);
        b.setOnClickListener(listener);

        bottomBar.addView(
                b,
                new LinearLayout.LayoutParams(
                        0,
                        -1,
                        1
                )
        );
    }

    private void welcome() {
        setup("المايسترو");

        TextView welcomeText = text(
                "🎓\n\n" +
                "المايسترو شريف هيبه\n\n" +
                "هتتعلم التاريخ ببساطة",
                24,
                darkMode ? WHITE : GREEN
        );

        welcomeText.setGravity(Gravity.CENTER);
        welcomeText.setTypeface(Typeface.DEFAULT_BOLD);

        content.addView(welcomeText);

        content.addView(
                button(
                        "👨‍🎓 أنا طالب",
                        v -> studentLogin()
                )
        );

        content.addView(
                button(
                        "👨‍🏫 أنا مدرس",
                        v -> teacherLogin()
                )
        );

        content.addView(
                button(
                        "⚙ الإعدادات",
                        v -> settings()
                )
        );
    }

    private void studentLogin() {
        setup("دخول الطالب");

        content.addView(
                text(
                        "اكتب اسم الطالب وكود الامتحان",
                        18,
                        darkMode ? WHITE : BLACK
                )
        );

        EditText name = input("اسم الطالب");
        EditText code = input("كود الامتحان");

        content.addView(name);
        content.addView(code);

        content.addView(
                button(
                        "دخول",
                        v -> {

                            String n =
                                    name.getText()
                                            .toString()
                                            .trim();

                            String c =
                                    code.getText()
                                            .toString()
                                            .trim();

                            if (n.isEmpty() || c.isEmpty()) {
                                toast("اكتب الاسم والكود");
                                return;
                            }

                            JSONArray exams = getExams();

                            for (int i = 0;
                                 i < exams.length();
                                 i++) {

                                try {
                                    JSONObject exam =
                                            exams.getJSONObject(i);

                                    if (c.equals(
                                            exam.optString("code")
                                    )) {

                                        studentName = n;

                                        prefs.edit()
                                                .putString(
                                                        "student_name",
                                                        n
                                                )
                                                .apply();

                                        startExam(exam);
                                        return;
                                    }

                                } catch (Exception ignored) {
                                }
                            }

                            toast("كود الامتحان غير صحيح");
                        }
                )
        );

        content.addView(
                button(
                        "رجوع",
                        v -> welcome()
                )
        );
    }

    private void studentHome() {
        setup("المايسترو - الطالب");

        content.addView(
                text(
                        "أهلًا يا " + studentName + " 👋",
                        22,
                        GOLD
                )
        );

        content.addView(
                button(
                        "📝 الامتحانات",
                        v -> studentExams()
                )
        );

        content.addView(
                button(
                        "📊 امتحاناتي ونتائجي",
                        v -> studentResults()
                )
        );

        content.addView(
                button(
                        "📚 المذكرات",
                        v -> notes()
                )
        );

        content.addView(
                button(
                        "📿 الأذكار",
                        v -> azkar()
                )
        );

        content.addView(
                button(
                        "⚙ الإعدادات",
                        v -> settings()
                )
        );

        bottomButton(
                "الرئيسية",
                v -> studentHome()
        );

        bottomButton(
                "امتحاناتي",
                v -> studentResults()
        );

        bottomButton(
                "أذكار",
                v -> azkar()
        );

        bottomButton(
                "مذكرات",
                v -> notes()
        );

        bottomButton(
                "إعدادات",
                v -> settings()
        );
    }

    private void studentExams() {
        setup("الامتحانات");

        JSONArray exams = getExams();

        if (exams.length() == 0) {
            content.addView(
                    text(
                            "لا توجد امتحانات.",
                            18,
                            GRAY
                    )
            );
        }

        for (int i = 0;
             i < exams.length();
             i++) {

            try {
                JSONObject exam =
                        exams.getJSONObject(i);

                final JSONObject selectedExam = exam;

                int count =
                        exam.optJSONArray(
                                "questions"
                        ).length();

                content.addView(
                        text(
                                exam.optString("name") +
                                "\nالمدة: " +
                                exam.optInt("duration") +
                                " دقيقة" +
                                "\nعدد الأسئلة: " +
                                count,
                                17,
                                darkMode ? WHITE : BLACK
                        )
                );

                content.addView(
                        button(
                                "دخول الامتحان",
                                v -> startExam(selectedExam)
                        )
                );

            } catch (Exception ignored) {
            }
        }

        content.addView(
                button(
                        "رجوع",
                        v -> studentHome()
                )
        );
    }

    private void startExam(JSONObject exam) {
        if (examRunning) {
            toast("يوجد امتحان مفتوح بالفعل");
            return;
        }

        try {
            JSONArray arr =
                    exam.optJSONArray("questions");

            if (arr == null || arr.length() == 0) {
                toast("الامتحان لا يحتوي على أسئلة");
                return;
            }

            questions.clear();
            answers.clear();

            for (int i = 0;
                 i < arr.length();
                 i++) {

                questions.add(
                        arr.getJSONObject(i)
                );

                answers.add(-1);
            }

            activeExam =
                    exam.optString("name");

            remainingSeconds =
                    exam.optInt("duration", 10) * 60;

            currentQuestion = 0;
            examRunning = true;

            showExamQuestion();
            startExamTimer();

        } catch (Exception e) {
            toast("حدث خطأ في فتح الامتحان");
            examRunning = false;
        }
    }

    private void startExamTimer() {
        if (examTimer != null) {
            examTimer.cancel();
        }

        examTimer =
                new CountDownTimer(
                        remainingSeconds * 1000L,
                        1000
                ) {

                    @Override
                    public void onTick(
                            long millis
                    ) {
                        remainingSeconds =
                                (int) (
                                        millis / 1000
                                );
                    }

                    @Override
                    public void onFinish() {
                        remainingSeconds = 0;

                        if (examRunning) {
                            toast(
                                    "انتهى الوقت، سيتم تسليم الامتحان"
                            );

                            submitExam();
                        }
                    }
                };

        examTimer.start();
    }

    private void showExamQuestion() {
        setup("الامتحان");

        TextView timer = text(
                "الوقت المتبقي: " +
                formatTime(remainingSeconds),
                20,
                GOLD
        );

        timer.setGravity(Gravity.CENTER);
        timer.setTypeface(Typeface.DEFAULT_BOLD);

        content.addView(timer);

        TextView number = text(
                "السؤال " +
                (currentQuestion + 1) +
                " من " +
                questions.size(),
                17,
                darkMode ? WHITE : BLACK
        );

        content.addView(number);

        JSONObject q =
                questions.get(currentQuestion);

        TextView question = text(
                q.optString("text"),
                20,
                darkMode ? WHITE : BLACK
        );

        question.setTypeface(
                Typeface.DEFAULT_BOLD
        );

        content.addView(question);

        RadioGroup group =
                new RadioGroup(this);

        group.setOrientation(
                RadioGroup.VERTICAL
        );

        JSONArray options =
                q.optJSONArray("options");

        for (int i = 0;
             i < options.length();
             i++) {

            RadioButton rb =
                    new RadioButton(this);

            rb.setText(
                    options.optString(i)
            );

            rb.setTextSize(17);
            rb.setTextColor(
                    darkMode ? WHITE : BLACK
            );

            final int index = i;

            rb.setOnClickListener(
                    v -> answers.set(
                            currentQuestion,
                            index
                    )
                                       
