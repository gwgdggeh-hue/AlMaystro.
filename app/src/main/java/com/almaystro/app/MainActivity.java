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

    private final int GOLD = Color.rgb(224, 190, 70);
    private final int DARK_GREEN = Color.rgb(5, 38, 27);
    private final int GREEN = Color.rgb(14, 70, 48);
    private final int WHITE = Color.WHITE;
    private final int LIGHT = Color.rgb(220, 220, 220);

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showWelcome();
    }

    private GradientDrawable background() {
        return new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[]{
                        DARK_GREEN,
                        GREEN,
                        DARK_GREEN
                }
        );
    }

    private TextView text(String value, float size, int color) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setGravity(Gravity.CENTER);
        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setIncludeFontPadding(true);
        t.setPadding(dp(8), dp(6), dp(8), dp(6));
        return t;
    }

    private TextView button(String value) {
        TextView b = new TextView(this);
        b.setText(value);
        b.setTextSize(20);
        b.setTextColor(DARK_GREEN);
        b.setGravity(Gravity.CENTER);
        b.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        b.setIncludeFontPadding(true);
        b.setPadding(dp(8), dp(4), dp(8), dp(4));
        b.setClickable(true);
        b.setFocusable(true);

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(GOLD);
        bg.setCornerRadius(dp(35));
        b.setBackground(bg);

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dp(64)
                );

        p.setMargins(dp(25), dp(7), dp(25), dp(7));
        b.setLayoutParams(p);

        return b;
    }

    private EditText input(String hint) {
        EditText e = new EditText(this);

        e.setHint(hint);
        e.setTextSize(18);
        e.setTextColor(WHITE);
        e.setHintTextColor(LIGHT);
        e.setGravity(Gravity.CENTER);
        e.setSingleLine(true);
        e.setPadding(dp(15), 0, dp(15), 0);

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(Color.rgb(20, 85, 59));
        bg.setCornerRadius(dp(18));
        bg.setStroke(dp(1), GOLD);
        e.setBackground(bg);

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dp(58)
                );

        p.setMargins(dp(20), dp(7), dp(20), dp(7));
        e.setLayoutParams(p);

        return e;
    }

    private void createScreen() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setPadding(dp(20), dp(20), dp(20), dp(20));
        root.setBackground(background());

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackground(background());

        content = root;

        scroll.addView(root);
        setContentView(scroll);
    }

    private void addLogo() {
        ImageView logo = new ImageView(this);
        logo.setImageResource(R.drawable.maestro);
        logo.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dp(190)
                );

        p.setMargins(0, dp(5), 0, dp(5));
        content.addView(logo, p);
    }

    private void addHeader(String title, String subtitle) {
        content.addView(text("✦  ❖  ✦", 24, GOLD));
        content.addView(text(title, 32, GOLD));

        if (subtitle != null && !subtitle.isEmpty()) {
            content.addView(text(subtitle, 18, WHITE));
        }

        content.addView(text("❖", 25, GOLD));
    }

    private void showWelcome() {
        createScreen();

        addHeader("المايسترو", "المايسترو شريف هيبه");

        content.addView(
                text("هتتعلم التاريخ ببساطة", 19, LIGHT)
        );

        addLogo();

        TextView start = button("ابدأ الآن");

        start.setOnClickListener(v -> showRoles());

        content.addView(start);

        content.addView(text("✦  ❖  ✦", 22, GOLD));
    }

    private void showRoles() {
        createScreen();

        addHeader(
                "اختيار نوع الدخول",
                "من فضلك اختر طريقة الدخول"
        );

        TextView student = button("أنا طالب");

        student.setOnClickListener(v -> showStudentLogin());

        content.addView(student);

        TextView teacher = button("أنا مدرس");

        teacher.setOnClickListener(v -> showTeacherLogin());

        content.addView(teacher);

        TextView back = button("رجوع");

        back.setOnClickListener(v -> showWelcome());

        content.addView(back);

        content.addView(text("✦  ❖  ✦", 22, GOLD));
    }

    private void showStudentLogin() {
        createScreen();

        addHeader(
                "دخول الطالب",
                "اكتب بياناتك للدخول إلى الامتحان"
        );

        EditText name = input("اكتب اسم الطالب");

        EditText code = input("اكتب كود الامتحان");
        code.setInputType(InputType.TYPE_CLASS_TEXT);

        content.addView(name);
        content.addView(code);

        TextView login = button("دخول الطالب");

        login.setOnClickListener(v -> {

            String studentName =
                    name.getText().toString().trim();

            String examCode =
                    code.getText().toString().trim();

            if (studentName.isEmpty()) {
                name.setError("اكتب اسم الطالب");
                name.requestFocus();
                return;
            }

            if (examCode.isEmpty()) {
                code.setError("اكتب كود الامتحان");
                code.requestFocus();
                return;
            }

            showStudentHome(studentName, examCode);
        });

        content.addView(login);

        TextView back = button("رجوع");

        back.setOnClickListener(v -> showRoles());

        content.addView(back);
    }

    private void showStudentHome(
            String studentName,
            String examCode) {

        createScreen();

        addHeader(
                "أهلًا بك يا " + studentName,
                "تم تسجيل بيانات الدخول"
        );

        content.addView(
                text("كود الامتحان: " + examCode, 17, LIGHT)
        );

        content.addView(
                text("امتحاناتي", 25, GOLD)
        );

        TextView exams = button("الامتحانات");

        exams.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "سيتم تجهيز الامتحانات في المرحلة القادمة",
                        Toast.LENGTH_SHORT
                ).show()
        );

        content.addView(exams);

        TextView myExams = button("امتحاناتي السابقة");

        myExams.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "سيتم تجهيز سجل الامتحانات لاحقًا",
                        Toast.LENGTH_SHORT
                ).show()
        );

        content.addView(myExams);

        TextView back = button("تسجيل خروج");

        back.setOnClickListener(v -> showRoles());

        content.addView(back);
    }

    private void showTeacherLogin() {
        createScreen();

        addHeader(
                "دخول المدرس",
                "الدخول مخصص للمدرسين المصرح لهم"
        );

        EditText teacherName =
                input("اكتب اسم المدرس");

        EditText teacherCode =
                input("اكتب كود المدرس");

        teacherCode.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_VARIATION_PASSWORD
        );

        content.addView(teacherName);
        content.addView(teacherCode);

        TextView login = button("دخول المدرس");

        login.setOnClickListener(v -> {

            String name =
                    teacherName.getText().toString().trim();

            String code =
                    teacherCode.getText().toString().trim();

            if (name.isEmpty()) {
                teacherName.setError("اكتب اسم المدرس");
                teacherName.requestFocus();
                return;
            }

            if (code.isEmpty()) {
                teacherCode.setError("اكتب كود المدرس");
                teacherCode.requestFocus();
                return;
            }

            if (!code.equals("1234")) {
                teacherCode.setError("كود المدرس غير صحيح");
                teacherCode.requestFocus();
                return;
            }

            showTeacherHome(name);
        });

        content.addView(login);

        TextView back = button("رجوع");

        back.setOnClickListener(v -> showRoles());

        content.addView(back);
    }

    private void showTeacherHome(String teacherName) {
        createScreen();

        addHeader(
                "لوحة المدرس",
                "أهلًا بك يا " + teacherName
        );

        content.addView(
                text(
                        "من هنا سيتم إدارة الامتحانات والطلاب والنتائج",
                        18,
                        WHITE
                )
        );

        TextView exams = button("إدارة الامتحانات");

        exams.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "إدارة الامتحانات ستتم إضافتها في المرحلة القادمة",
                        Toast.LENGTH_SHORT
                ).show()
        );

        content.addView(exams);

        TextView students = button("الطلاب والنتائج");

        students.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "الطلاب والنتائج ستتم إضافتها في المرحلة القادمة",
                        Toast.LENGTH_SHORT
                ).show()
        );

        content.addView(students);

        TextView notes = button("مذكرات الشرح");

        notes.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "مذكرات الشرح ستتم إضافتها لاحقًا",
                        Toast.LENGTH_SHORT
                ).show()
        );

        content.addView(notes);

        TextView settings = button("الإعدادات");

        settings.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "الإعدادات ستتم إضافتها لاحقًا",
                        Toast.LENGTH_SHORT
                ).show()
        );

        content.addView(settings);

        TextView logout = button("تسجيل خروج");

        logout.setOnClickListener(v -> showRoles());

        content.addView(logout);
    }
    }
