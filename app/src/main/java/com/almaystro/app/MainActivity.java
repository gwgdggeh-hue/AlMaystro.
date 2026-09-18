package com.almaystro.app;

import android.app.*;
import android.os.*;
import android.graphics.*;
import android.graphics.drawable.GradientDrawable;
import android.content.*;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.text.InputType;
import org.json.*;
import java.util.*;

public class MainActivity extends Activity {
    private LinearLayout content;
    private android.content.SharedPreferences sp;
    private CountDownTimer timer;
    private boolean darkMode=true, examActive=false, submitted=false;
    private String student="", teacher="", activeCode="";
    private static final int GOLD=0xFFE0BE46, DARK=0xFF05261B, GREEN=0xFF0E4630;

    @Override public void onCreate(Bundle b){
        super.onCreate(b);
        sp=getSharedPreferences("almaystro",MODE_PRIVATE);
        darkMode=sp.getBoolean("dark",true);
        applyBrightness();
        showWelcome();
    }

    private int dp(int n){return (int)(n*getResources().getDisplayMetrics().density+.5f);}
    private int fg(){return darkMode?Color.WHITE:0xFF202020;}
    private int sub(){return darkMode?0xFFD0D0D0:0xFF666666;}
    private int card(){return darkMode?GREEN:Color.WHITE;}

    private GradientDrawable gradient(){
        GradientDrawable g=new GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            darkMode?new int[]{DARK,GREEN,DARK}:new int[]{0xFFF6F2E8,Color.WHITE,0xFFE9E5D8});
        g.setCornerRadius(dp(18));
        return g;
    }
    private TextView text(String s,float size,int color){
        TextView t=new TextView(this);
        t.setText(s);t.setTextSize(size);t.setTextColor(color);
        t.setGravity(Gravity.CENTER);t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        t.setPadding(dp(8),dp(8),dp(8),dp(8));
        return t;
    }
    private TextView button(String s){
        TextView b=text(s,16,darkMode?DARK:0xFF173D2C);
        GradientDrawable g=new GradientDrawable();
        g.setColor(GOLD);g.setCornerRadius(dp(26));b.setBackground(g);
        LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,dp(56));
        p.setMargins(dp(10),dp(5),dp(10),dp(5));b.setLayoutParams(p);
        return b;
    }
    private EditText input(String hint){
        EditText e=new EditText(this);
        e.setHint(hint);e.setTextSize(16);e.setTextColor(fg());e.setHintTextColor(sub());
        e.setGravity(Gravity.CENTER);e.setSingleLine(true);
        GradientDrawable g=new GradientDrawable();g.setColor(card());g.setCornerRadius(dp(16));g.setStroke(dp(1),GOLD);
        e.setBackground(g);
        LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,dp(54));
        p.setMargins(dp(10),dp(4),dp(10),dp(4));e.setLayoutParams(p);
        return e;
    }
    private void base(){
        LinearLayout root=new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);root.setPadding(dp(12),dp(10),dp(12),dp(10));
        root.setBackground(gradient());
        ScrollView scroll=new ScrollView(this);scroll.setFillViewport(true);scroll.addView(root);
        content=root;setContentView(scroll);
    }
    private void header(String title,String subTitle){
        content.addView(text("✦  ❖  ✦",20,GOLD));
        content.addView(text(title,28,GOLD));
        if(subTitle!=null&&!subTitle.isEmpty())content.addView(text(subTitle,15,sub()));
        content.addView(text("❖",18,GOLD));
    }
    private void addBtn(String s,View.OnClickListener l){TextView b=button(s);b.setOnClickListener(l);content.addView(b);}
    private void say(String s){Toast.makeText(this,s,Toast.LENGTH_SHORT).show();}
    private void backTo(View.OnClickListener l){addBtn("↩ رجوع",l);}

    private void showWelcome(){
        if(timer!=null){timer.cancel();timer=null;}examActive=false;submitted=false;
        base();
        ImageView im=new ImageView(this);im.setImageResource(R.drawable.maestro);im.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        content.addView(im,new LinearLayout.LayoutParams(-1,dp(180)));
        content.addView(text("المايسترو",38,GOLD));
        content.addView(text("المايسترو شريف هيبه",21,fg()));
        content.addView(text("هتتعلم التاريخ ببساطة",18,sub()));
        content.addView(text("✦  منصة الامتحانات والمراجعة ✦",15,GOLD));
        addBtn("🚀 ابدأ الآن",v->roles());
        content.addView(text("طالب • مدرس • امتحانات • نتائج • مذكرات • أذكار",14,sub()));
    }

    private void roles(){
        base();header("مرحبًا بك في المايسترو","اختر نوع الدخول");
        addBtn("👨‍🎓 أنا طالب",v->studentLogin());
        addBtn("👨‍🏫 أنا مدرس",v->teacherLogin());
        addBtn("ℹ️ عن التطبيق",v->about());
    }

    private void studentLogin(){
        base();header("دخول الطالب","الاسم + كود الامتحان");
        EditText n=input("اسم الطالب");EditText c=input("كود الامتحان");
        content.addView(n);content.addView(c);
        addBtn("دخول وبدء الامتحان",v->{
            String nn=n.getText().toString().trim(),cc=c.getText().toString().trim();
            if(nn.isEmpty()){n.setError("اكتب اسم الطالب");return;}
            if(cc.isEmpty()){c.setError("اكتب الكود");return;}
            JSONObject ex=findByCode(cc);
            if(ex==null){c.setError("الكود غير موجود");return;}
            String key=nn.toLowerCase(Locale.ROOT)+"|"+cc.toLowerCase(Locale.ROOT);
            if(isUsed(key)){c.setError("الكود مستخدم بالفعل لهذا الطالب");return;}
            student=nn;activeCode=cc;markUsed(key);startExam(ex);
        });
        backTo(v->roles());
    }

    private void studentHome(){
        base();header("أهلًا يا "+(student.isEmpty()?"طالب":student),"لوحة الطالب");
        addBtn("📝 الامتحانات المتاحة",v->studentLogin());
        addBtn("📊 امتحاناتي ونتائجي",v->studentResults());
        addBtn("📚 مذكرات الشرح",v->studentNotes());
        addBtn("🤲 الأذكار والأوراد",v->azkar());
        addBtn("🏆 إنجازاتي وتقدمي",v->progress());
        addBtn("🔔 التحديثات",v->updates());
        addBtn("⚙️ الإعدادات",v->settings());
        addBtn("ℹ️ عن التطبيق",v->about());
        addBtn("🚪 تسجيل خروج",v->roles());
        studentNav();
    }

    private void studentResults(){
        base();header("امتحاناتي ونتائجي",student);
        JSONArray a=array("results");boolean found=false;
        for(int i=0;i<a.length();i++)try{
            JSONObject r=a.getJSONObject(i);
            if(student.equals(r.optString("student"))){
                found=true;
                String mistakes=r.optString("mistakes","");
                String m=mistakes.isEmpty()?"لا توجد إجابات خاطئة مسجلة.":"أخطاء للمراجعة:\n"+mistakes;
                content.addView(text("📝 "+r.optString("exam")+"\nالدرجة: "+r.optInt("correct")+"/"+r.optInt("total")+
                    "\nتمت الإجابة: "+r.optInt("answered")+" | بدون إجابة: "+(r.optInt("total")-r.optInt("answered"))+
                    "\n"+m,15,fg()));
            }
        }catch(Exception ignored){}
        if(!found)content.addView(text("لا توجد نتائج لهذا الطالب حتى الآن.",17,fg()));
        backTo(v->studentHome());studentNav();
    }

    private void studentNotes(){
        base();header("مذكرات الشرح","المذكرات المتاحة");
        JSONArray a=array("notes");
        if(a.length()==0)content.addView(text("لا توجد مذكرات منشورة حاليًا.",17,fg()));
        for(int i=0;i<a.length();i++)try{
            JSONObject n=a.getJSONObject(i);
            content.addView(text("📖 "+n.optString("title")+"\n\n"+n.optString("body"),16,fg()));
        }catch(Exception ignored){}
        backTo(v->studentHome());studentNav();
    }

    private void azkar(){
        base();header("الأذكار والأوراد","ذكر الله وطمأنينة القلب");
        content.addView(text("☀️ أذكار الصباح",22,GOLD));
        content.addView(text(
            "آية الكرسي\nالإخلاص والفلق والناس\nأصبحنا وأصبح الملك لله\nاللهم إني أسألك خير هذا اليوم\nسبحان الله وبحمده\nأستغفر الله وأتوب إليه\nلا إله إلا الله وحده لا شريك له",
            17,fg()));
        content.addView(text("🌙 أذكار المساء",22,GOLD));
        content.addView(text(
            "آية الكرسي\nالإخلاص والفلق والناس\nأمسينا وأمسى الملك لله\nاللهم بك أمسينا وبك أصبحنا\nسبحان الله وبحمده\nأستغفر الله وأتوب إليه",
            17,fg()));
        content.addView(text("📿 ورد التسبيح والاستغفار",22,GOLD));
        content.addView(text("سبحان الله • الحمد لله • الله أكبر\nلا إله إلا الله • الصلاة على النبي ﷺ\nأستغفر الله وأتوب إليه",17,fg()));
        addBtn("🔢 عدّاد الذكر",v->dhikrCounter());
        backTo(v->studentHome());studentNav();
    }

    private void dhikrCounter(){
        final int[] count={0};
        TextView number=text("0",48,GOLD);
        TextView plus=button("📿 اضغط للذكر");
        plus.setOnClickListener(v->{count[0]++;number.setText(String.valueOf(count[0]));});
        new AlertDialog.Builder(this).setTitle("عداد الذكر").setView(number).setMessage("اضغط الزر لزيادة العدد.")
            .setPositiveButton("إغلاق",null).show();
        // Keep the visible counter usable through a second dialog action.
        number.setOnClickListener(v->{count[0]++;number.setText(String.valueOf(count[0]));});
    }

    private void progress(){
        base();header("إنجازاتي وتقدمي","ملخص نتائج "+student);
        JSONArray a=array("results");int exams=0,correct=0,total=0;
        for(int i=0;i<a.length();i++)try{JSONObject r=a.getJSONObject(i);
            if(student.equals(r.optString("student"))){exams++;correct+=r.optInt("correct");total+=r.optInt("total");}
        }catch(Exception ignored){}
        content.addView(text("🏆 امتحانات منجزة: "+exams+"\n✅ إجابات صحيحة: "+correct+"\n📚 إجمالي الأسئلة: "+total,20,fg()));
        content.addView(text(exams>0?"🌟 استمر في المراجعة والتدريب.":"ابدأ أول امتحان وسجل أول إنجاز لك.",17,GOLD));
        backTo(v->studentHome());studentNav();
    }

    private void updates(){
        base();header("التحديثات","آخر أخبار المايسترو");
        String u=sp.getString("update","لا توجد تحديثات جديدة.");
        content.addView(text("🔔 "+u,17,fg()));
        backTo(v->studentHome());studentNav();
    }

    private void settings(){
        base();header("الإعدادات","تخصيص التطبيق");
        addBtn(darkMode?"☀️ التحويل للوضع الفاتح":"🌙 التحويل للوضع الداكن",v->{
            darkMode=!darkMode;sp.edit().putBoolean("dark",darkMode).apply();settings();
        });
        addBtn("🔆 ضبط سطوع التطبيق",v->brightnessDialog());
        addBtn("🔒 الخصوصية والأمان",v->privacy());
        addBtn("ℹ️ عن التطبيق",v->about());
        backTo(v->studentHome());
    }

    private void brightnessDialog(){
        final SeekBar bar=new SeekBar(this);bar.setMax(100);bar.setProgress(sp.getInt("brightness",100));
        new AlertDialog.Builder(this).setTitle("سطوع التطبيق").setMessage("اختر مستوى السطوع")
            .setView(bar).setPositiveButton("حفظ",(d,w)->{
                sp.edit().putInt("brightness",bar.getProgress()).apply();applyBrightness();
            }).setNegativeButton("إلغاء",null).show();
    }
    private void applyBrightness(){
        int v=sp==null?100:sp.getInt("brightness",100);
        WindowManager.LayoutParams p=getWindow().getAttributes();
        p.screenBrightness=Math.max(.05f,Math.min(1f,v/100f));
        getWindow().setAttributes(p);
    }
    private void privacy(){
        base();header("الخصوصية والأمان","معلومات مهمة");
        content.addView(text("🔐 الامتحان يستخدم حماية الشاشة أثناء الاختبار.\n📵 Screenshot وScreen Recording محظوران أثناء الامتحان.\n⏱️ المؤقت ينتهي تلقائيًا.\n🛑 لا يمكن الرجوع أثناء الامتحان.\n🔑 كود الامتحان مرتبط بمحاولة الطالب.\n💾 النسخة الحالية تحفظ البيانات محليًا.",16,fg()));
        backTo(v->settings());
    }
    private void about(){
        base();header("عن المايسترو","منصة تعليمية");
        content.addView(text("المايسترو شريف هيبه\nهتتعلم التاريخ ببساطة\n\nمع المبرمج أو المطور محمود كليب\nللتواصل: 01112244710\n\nنسخة مستقلة تعمل ببيانات محلية، ومهيأة للربط بالمزامنة.",17,fg()));
        backTo(v->roles());
    }
    private void studentNav(){
        LinearLayout bar=new LinearLayout(this);bar.setOrientation(LinearLayout.HORIZONTAL);
        GradientDrawable g=new GradientDrawable();g.setColor(card());g.setCornerRadius(dp(18));g.setStroke(dp(1),GOLD);bar.setBackground(g);
        nav(bar,"الرئيسية",v->studentHome());nav(bar,"نتائجي",v->studentResults());
        nav(bar,"الأذكار",v->azkar());nav(bar,"المذكرات",v->studentNotes());nav(bar,"الإعدادات",v->settings());
        content.addView(bar,new LinearLayout.LayoutParams(-1,dp(62)));
    }
    private void nav(LinearLayout bar,String s,View.OnClickListener l){
        TextView t=text(s,10,GOLD);t.setOnClickListener(l);bar.addView(t,new LinearLayout.LayoutParams(0,-1,1));
    }

    private void teacherLogin(){
        base();header("دخول المدرس","اكتب اسم المدرس وكود الدخول");
        EditText n=input("اسم المدرس"),c=input("كود الدخول");
        c.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        content.addView(n);content.addView(c);
        addBtn("👨‍🏫 دخول لوحة المدرس",v->{
            String nn=n.getText().toString().trim(),cc=c.getText().toString().trim();
            if(nn.isEmpty()){n.setError("اكتب اسم المدرس");return;}
            if(!"1234".equals(cc)){c.setError("الكود غير صحيح");return;}
            teacher=nn;teacherHome();
        });
        backTo(v->roles());
    }

    private void teacherHome(){
        base();header("لوحة المدرس","أهلًا يا "+teacher);
        JSONArray e=array("exams");JSONArray r=array("results");
        content.addView(text("📊 الامتحانات: "+e.length()+"   |   النتائج: "+r.length(),17,GOLD));
        addBtn("📝 إدارة الامتحانات",v->examManager());
        addBtn("❓ الأسئلة والأكواد",v->questionManager());
        addBtn("📊 الطلاب والنتائج",v->teacherResults());
        addBtn("📚 مذكرات الشرح",v->teacherNotes());
        addBtn("👥 مجموعات الطلاب",v->groups());
        addBtn("📢 الإعلانات والتحديثات",v->updatesManager());
        addBtn("⚙️ إعدادات المدرس",v->teacherSettings());
        addBtn("🚪 تسجيل خروج",v->roles());
    }

    private void examManager(){
        base();header("إدارة الامتحانات","إنشاء وتعديل وحذف");
        addBtn("➕ إضافة امتحان",v->addExamDialog());
        JSONArray a=array("exams");
        if(a.length()==0)content.addView(text("لا توجد امتحانات.",17,fg()));
        for(int i=0;i<a.length();i++)try{
            JSONObject e=a.getJSONObject(i);String id=e.optString("id");
            content.addView(text("📝 "+e.optString("title")+"\n⏱️ "+e.optInt("duration",30)+" دقيقة\n❓ "+e.optJSONArray("questions").length()+" سؤال",16,fg()));
            addBtn("❓ إدارة أسئلة الامتحان",v->questionsForExam(id));
            addBtn("✏️ تعديل الامتحان",v->editExamDialog(id));
            addBtn("🗑️ حذف الامتحان",v->confirmDeleteExam(id));
        }catch(Exception ignored){}
        teacherBack();
    }

    private void addExamDialog(){
        LinearLayout box=new LinearLayout(this);box.setOrientation(LinearLayout.VERTICAL);
        EditText title=input("اسم الامتحان"),dur=input("المدة بالدقائق");
        box.addView(title);box.addView(dur);
        new AlertDialog.Builder(this).setTitle("إضافة امتحان").setView(box)
            .setPositiveButton("حفظ",(d,w)->{
                String t=title.getText().toString().trim();if(t.isEmpty()){say("اكتب اسم الامتحان");return;}
                JSONArray a=array("exams");JSONObject e=new JSONObject();
                try{e.put("id",String.valueOf(System.currentTimeMillis()));e.put("title",t);
                    e.put("duration",Math.max(1,num(dur.getText().toString(),30)));e.put("questions",new JSONArray());
                    a.put(e);save("exams",a);examManager();
                }catch(Exception ignored){}
            }).setNegativeButton("إلغاء",null).show();
    }

    private void editExamDialog(String id){
        JSONObject e=findExam(id);if(e==null)return;
        LinearLayout box=new LinearLayout(this);box.setOrientation(LinearLayout.VERTICAL);
        EditText title=input("اسم الامتحان"),dur=input("المدة بالدقائق");
        title.setText(e.optString("title"));dur.setText(String.valueOf(e.optInt("duration",30)));
        box.addView(title);box.addView(dur);
        new AlertDialog.Builder(this).setTitle("تعديل الامتحان").setView(box)
            .setPositiveButton("حفظ",(d,w)->{try{
                e.put("title",title.getText().toString().trim());
                e.put("duration",Math.max(1,num(dur.getText().toString(),30)));
                updateExam(e);examManager();
            }catch(Exception ignored){}}).setNegativeButton("إلغاء",null).show();
    }

    private void confirmDeleteExam(String id){
        new AlertDialog.Builder(this).setTitle("حذف الامتحان")
            .setMessage("سيتم حذف الامتحان وأسئلته. هل أنت متأكد؟")
            .setPositiveButton("حذف",(d,w)->{deleteExam(id);examManager();})
            .setNegativeButton("إلغاء",null).show();
    }

    private void questionsForExam(String id){
        JSONObject e=findExam(id);if(e==null)return;
        base();header("أسئلة "+e.optString("title"),"اختيار من متعدد");
        addBtn("➕ إضافة سؤال",v->addQuestion(id));
        JSONArray q=e.optJSONArray("questions");if(q==null)q=new JSONArray();
        for(int i=0;i<q.length();i++)try{
            JSONObject x=q.getJSONObject(i);int k=i;
            content.addView(text((i+1)+") "+x.optString("q")+
                "\nأ) "+x.optString("a")+"\nب) "+x.optString("b")+
                "\nج) "+x.optString("c")+"\nد) "+x.optString("d")+
                "\nالإجابة الصحيحة: "+x.optString("ans"),15,fg()));
            addBtn("🗑️ حذف السؤال",v->{
                try{JSONObject z=findExam(id);z.getJSONArray("questions").remove(k);updateExam(z);questionsForExam(id);}
                catch(Exception ignored){}
            });
        }catch(Exception ignored){}
        backTo(v->examManager());
    }

    private void addQuestion(String id){
        LinearLayout box=new LinearLayout(this);box.setOrientation(LinearLayout.VERTICAL);
        EditText q=input("السؤال"),a=input("اختيار أ"),b=input("اختيار ب"),c=input("اختيار ج"),d=input("اختيار د"),ans=input("الإجابة الصحيحة: أ أو ب أو ج أو د");
        box.addView(q);box.addView(a);box.addView(b);box.addView(c);box.addView(d);box.addView(ans);
        new AlertDialog.Builder(this).setTitle("إضافة سؤال").setView(box)
            .setPositiveButton("حفظ",(x,w)->{
                String answer=ans.getText().toString().trim();
                if(!answer.equals("أ")&&!answer.equals("ب")&&!answer.equals("ج")&&!answer.equals("د")){say("الإجابة يجب أن تكون أ أو ب أو ج أو د");return;}
                try{
                    JSONObject e=findExam(id);JSONArray ar=e.getJSONArray("questions");JSONObject z=new JSONObject();
                    z.put("q",q.getText().toString().trim());z.put("a",a.getText().toString().trim());
                    z.put("b",b.getText().toString().trim());z.put("c",c.getText().toString().trim());
                    z.put("d",d.getText().toString().trim());z.put("ans",answer);
                    ar.put(z);updateExam(e);questionsForExam(id);
                }catch(Exception ignored){}
            }).setNegativeButton("إلغاء",null).show();
    }

    private void questionManager(){
        base();header("الأسئلة والأكواد","اختر الامتحان");
        JSONArray e=array("exams");
        for(int i=0;i<e.length();i++)try{
            JSONObject x=e.getJSONObject(i);String id=x.optString("id");
            addBtn("❓ "+x.optString("title"),v->questionsForExam(id));
        }catch(Exception ignored){}
        addBtn("🔑 إدارة أكواد الامتحانات",v->codes());
        teacherBack();
    }

    private void codes(){
        base();header("أكواد الامتحانات","كود لكل محاولة");
        addBtn("➕ إنشاء كود",v->addCode());
        JSONArray a=array("codes");
        if(a.length()==0)content.addView(text("لا توجد أكواد.",17,fg()));
        for(int i=0;i<a.length();i++)try{
            JSONObject x=a.getJSONObject(i);
                        content.addView(
                    text(
                            "الكود: " + x.optString("code") +
                            "\nالامتحان: " + x.optString("exam"),
                            16,
                            fg()
                    )
            );
        }catch(Exception ignored){}

        teacherBack();
    }

    // =========================================================
    // ABOUT
    // =========================================================

    private void showAbout() {

        new AlertDialog.Builder(this)
                .setTitle("ℹ️ عن المايسترو")
                .setMessage(
                        "المايسترو\n\n" +
                        "المايسترو شريف هيبه\n" +
                        "هتتعلم التاريخ ببساطة 📚\n\n" +
                        "تطبيق تعليمي للطلاب والمدرسين، " +
                        "ويحتوي على الامتحانات والنتائج والمذكرات والأذكار.\n\n" +
                        "مع المبرمج أو المطور محمود كليب\n" +
                        "للتواصل: 01112244710"
                )
                .setPositiveButton(
                        "حسنًا",
                        null
                )
                .show();
    }
// =========================================================
    // ABOUT
    // =========================================================

    private void showAbout() {

        new AlertDialog.Builder(this)
                .setTitle("ℹ️ عن المايسترو")
                .setMessage(
                        "المايسترو\n\n" +
                        "المايسترو شريف هيبه\n" +
                        "هتتعلم التاريخ ببساطة 📚\n\n" +
                        "تطبيق تعليمي للطلاب والمدرسين، " +
                        "ويحتوي على الامتحانات والنتائج والمذكرات والأذكار.\n\n" +
                        "مع المبرمج أو المطور محمود كليب\n" +
                        "للتواصل: 01112244710"
                )
                .setPositiveButton(
                        "حسنًا",
                        null
                )
                .show();
    }

    // =========================================================
    // UI HELPERS
    // =========================================================

    private LinearLayout vertical() {

        LinearLayout layout =
                new LinearLayout(this);

        layout.setOrientation(
                LinearLayout.VERTICAL
        );

        return layout;
    }

    private LinearLayout cardLayout() {

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setPadding(
                18,
                18,
                18,
                18
        );

        android.graphics.drawable.GradientDrawable bg =
                new android.graphics.drawable.GradientDrawable();

        bg.setCornerRadius(22);

        if (darkMode) {

            bg.setColor(
                    Color.rgb(35, 43, 40)
            );

        } else {

            bg.setColor(
                    Color.WHITE
            );
        }

        bg.setStroke(
                1,
                darkMode
                        ? Color.rgb(70, 85, 78)
                        : Color.rgb(225, 215, 190)
        );

        card.setBackground(bg);

        return card;
    }

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
                Gravity.RIGHT |
                Gravity.CENTER_VERTICAL
        );

        tv.setTypeface(
                Typeface.DEFAULT,
                bold
                        ? Typeface.BOLD
                        : Typeface.NORMAL
        );

        tv.setPadding(
                4,
                4,
                4,
                4
        );

        return tv;
    }

    private EditText input(
            String hint
    ) {

        EditText edit =
                new EditText(this);

        edit.setHint(hint);

        edit.setTextSize(16);

        edit.setSingleLine(false);

        edit.setPadding(
                18,
                5,
                18,
                5
        );

        edit.setGravity(
                Gravity.RIGHT |
                Gravity.CENTER_VERTICAL
        );

        edit.setTextColor(
                darkMode
                        ? white
                        : black
        );

        edit.setHintTextColor(
                darkMode
                        ? Color.LTGRAY
                        : Color.GRAY
        );

        android.graphics.drawable.GradientDrawable bg =
                new android.graphics.drawable.GradientDrawable();

        bg.setCornerRadius(18);

        bg.setColor(
                darkMode
                        ? Color.rgb(30, 35, 33)
                        : Color.rgb(248, 248, 248)
        );

        bg.setStroke(
                1,
                darkMode
                        ? Color.rgb(80, 90, 85)
                        : Color.rgb(215, 215, 215)
        );

        edit.setBackground(bg);

        return edit;
    }

    private TextView button(
            String value
    ) {

        TextView btn =
                text(
                        value,
                        16,
                        white,
                        true
                );

        btn.setGravity(
                Gravity.CENTER
        );

        btn.setPadding(
                12,
                12,
                12,
                12
        );

        android.graphics.drawable.GradientDrawable bg =
                new android.graphics.drawable.GradientDrawable();

        bg.setCornerRadius(20);

        bg.setColor(green);

        bg.setStroke(
                1,
                gold
        );

        btn.setBackground(bg);

        return btn;
    }

    private TextView secondaryButton(
            String value
    ) {

        TextView btn =
                text(
                        value,
                        15,
                        darkMode
                                ? white
                                : green,
                        true
                );

        btn.setGravity(
                Gravity.CENTER
        );

        btn.setPadding(
                12,
                10,
                12,
                10
        );

        android.graphics.drawable.GradientDrawable bg =
                new android.graphics.drawable.GradientDrawable();

        bg.setCornerRadius(18);

        bg.setColor(
                darkMode
                        ? Color.rgb(35, 43, 40)
                        : Color.WHITE
        );

        bg.setStroke(
                2,
                gold
        );

        btn.setBackground(bg);

        return btn;
    }

    private LinearLayout.LayoutParams marginParams(
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
                left,
                top,
                right,
                bottom
        );

        return params;
    }

    // =========================================================
    // TOAST
    // =========================================================

    private void toast(
            String message
    ) {

        Toast.makeText(
                this,
                message,
                Toast.LENGTH_SHORT
        ).show();
    }

    // =========================================================
    // BACK BUTTON PROTECTION
    // =========================================================

    @Override
    public void onBackPressed() {

        if (examRunning) {

            new AlertDialog.Builder(this)
                    .setTitle("الامتحان قيد التشغيل")
                    .setMessage(
                            "لا يمكنك الخروج من الامتحان قبل تسليمه."
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
    // LIFECYCLE SAFETY
    // =========================================================

    @Override
    protected void onDestroy() {

        if (examTimer != null) {

            examTimer.cancel();

            examTimer = null;
        }

        super.onDestroy();
    }

    // =========================================================
    // END OF MAIN ACTIVITY
    // =========================================================

                }
// =========================================================
    // STUDENT SETTINGS
    // =========================================================

    private void showStudentSettings() {

        content.removeAllViews();

        TextView title = text(
                "⚙️ الإعدادات",
                27,
                darkMode ? white : green,
                true
        );

        title.setPadding(20, 25, 20, 20);
        content.addView(title);

        // اسم الطالب
        LinearLayout profile = cardLayout();

        TextView profileTitle = text(
                "👤 بيانات الطالب",
                20,
                darkMode ? white : green,
                true
        );

        profile.addView(profileTitle);

        String name = prefs.getString(
                STUDENT_NAME,
                "غير مسجل"
        );

        TextView profileName = text(
                "الاسم: " + name,
                16,
                darkMode ? white : black,
                false
        );

        profile.addView(
                profileName,
                marginParams(
                        -1, -2, 0, 0, 0, 0
                )
        );

        content.addView(
                profile,
                marginParams(
                        -1, -2, 15, 8, 15, 0
                )
        );

        // الوضع الليلي
        LinearLayout appearance = cardLayout();

        TextView appearanceTitle = text(
                "🎨 المظهر",
                20,
                darkMode ? white : green,
                true
        );

        appearance.addView(appearanceTitle);

        TextView mode = button(
                darkMode
                        ? "☀️ تفعيل الوضع النهاري"
                        : "🌙 تفعيل الوضع الليلي"
        );

        mode.setOnClickListener(v -> {

            darkMode = !darkMode;

            prefs.edit()
                    .putBoolean(
                            DARK_MODE,
                            darkMode
                    )
                    .apply();

            openStudentHome();
        });

        appearance.addView(mode);

        content.addView(
                appearance,
                marginParams(
                        -1, -2, 15, 8, 15, 0
                )
        );

        // السطوع
        LinearLayout brightness =
                cardLayout();

        TextView brightnessTitle = text(
                "☀️ التحكم في السطوع",
                20,
                darkMode ? white : green,
                true
        );

        brightness.addView(brightnessTitle);

        SeekBar seekBar =
                new SeekBar(this);

        seekBar.setMax(100);

        Window window = getWindow();

        WindowManager.LayoutParams params =
                window.getAttributes();

        float currentBrightness =
                params.screenBrightness;

        if (currentBrightness < 0) {
            currentBrightness = 0.5f;
        }

        seekBar.setProgress(
                (int) (currentBrightness * 100)
        );

        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(
                            SeekBar seekBar,
                            int progress,
                            boolean fromUser
                    ) {

                        float value =
                                progress / 100f;

                        if (value < 0.05f) {
                            value = 0.05f;
                        }

                        WindowManager.LayoutParams p =
                                getWindow().getAttributes();

                        p.screenBrightness = value;

                        getWindow().setAttributes(p);
                    }

                    @Override
                    public void onStartTrackingTouch(
                            SeekBar seekBar
                    ) {
                    }

                    @Override
                    public void onStopTrackingTouch(
                            SeekBar seekBar
                    ) {
                    }
                }
        );

        brightness.addView(
                seekBar,
                marginParams(
                        -1, -2, 0, 5, 0, 0
                )
        );

        content.addView(
                brightness,
                marginParams(
                        -1, -2, 15, 8, 15, 0
                )
        );

        // الخصوصية
        LinearLayout privacy =
                cardLayout();

        TextView privacyTitle = text(
                "🔐 الخصوصية والأمان",
                20,
                darkMode ? white : green,
                true
        );

        privacy.addView(privacyTitle);

        TextView privacyText = text(
                "بيانات الامتحانات والنتائج محفوظة داخل التطبيق حاليًا. "
                        + "سيتم ربط النظام بخدمة Firebase للمزامنة والحماية "
                        + "عند تفعيل النظام السحابي.",
                15,
                gray,
                false
        );

        privacy.addView(
                privacyText,
                marginParams(
                        -1, -2, 0, 0, 0, 0
                )
        );

        content.addView(
                privacy,
                marginParams(
                        -1, -2, 15, 8, 15, 0
                )
        );

        // حول التطبيق
        TextView about =
                secondaryButton("ℹ️ عن تطبيق المايسترو");

        about.setOnClickListener(
                v -> showAbout()
        );

        content.addView(
                about,
                marginParams(
                        -1, 55, 15, 8, 15, 0
                )
        );

        // تسجيل الخروج
        TextView logout =
                secondaryButton("🚪 تسجيل الخروج");

        logout.setOnClickListener(
                v -> confirmLogout()
        );

        content.addView(
                logout,
                marginParams(
                        -1, 55, 15, 20, 15, 0
                )
        );
    }

    // =========================================================
    // LOGOUT
    // =========================================================

    private void confirmLogout() {

        new AlertDialog.Builder(this)
                .setTitle("تسجيل الخروج")
                .setMessage(
                        "هل تريد تسجيل الخروج من حسابك؟"
                )
                .setNegativeButton(
                        "إلغاء",
                        null
                )
                .setPositiveButton(
                        "خروج",
                        (dialog, which) -> {

                            prefs.edit()
                                    .remove(ROLE)
                                    .remove(STUDENT_NAME)
                                    .remove("teacher_name")
                                    .apply();

                            showWelcome();
                        }
                )
                .show();
    }

    // =========================================================
    // STUDENT BOTTOM NAVIGATION
    // =========================================================

    private void createStudentBottomBar() {

        bottomBar =
                new LinearLayout(this);

        bottomBar.setOrientation(
                LinearLayout.HORIZONTAL
        );

        bottomBar.setGravity(
                Gravity.CENTER
        );

        bottomBar.setPadding(
                4,
                5,
                4,
                5
        );

        bottomBar.setBackgroundColor(
                darkMode
                        ? Color.rgb(30, 45, 39)
                        : white
        );

        addNavButton(
                "الرئيسية",
                () -> buildStudentHome()
        );

        addNavButton(
                "امتحاناتي",
                () -> showStudentExams()
        );

        addNavButton(
                "الأذكار",
                () -> showAzkar()
        );

        addNavButton(
                "المذكرات",
                () -> showStudentNotes()
        );

        addNavButton(
                "الإعدادات",
                () -> showStudentSettings()
        );

        root.addView(
                bottomBar,
                new LinearLayout.LayoutParams(
                        -1,
                        65
                )
        );
    }

    private void addNavButton(
            String title,
            Runnable action
    ) {

        TextView nav =
                new TextView(this);

        nav.setText(title);

        nav.setTextSize(11);

        nav.setGravity(
                Gravity.CENTER
        );

        nav.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        nav.setTextColor(
                darkMode
                        ? white
                        : green
        );

        nav.setPadding(
                2,
                2,
                2,
                2
        );

        nav.setOnClickListener(
                v -> action.run()
        );

        bottomBar.addView(
                nav,
                new LinearLayout.LayoutParams(
                        0,
                        -1,
                        1
                )
        );
    }

    // =========================================================
    // TEACHER HOME
    // =========================================================

    private void openTeacherHome() {

        createRoot();

        content = vertical();

        root.addView(
                content,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        buildTeacherHome();

        createTeacherBottomBar();
    }

    private void buildTeacherHome() {

        content.removeAllViews();

        String teacher =
                prefs.getString(
                        "teacher_name",
                        "المدرس"
                );

        TextView title =
                text(
                        "أهلًا بك يا " + teacher,
                        25,
                        darkMode ? white : green,
                        true
                );

        title.setPadding(
                20,
                25,
                20,
                5
        );

        content.addView(title);

        TextView subtitle =
                text(
                        "لوحة تحكم المدرس",
                        17,
                        gray,
                        false
                );

        subtitle.setPadding(
                20,
                0,
                20,
                20
        );

        content.addView(subtitle);

        addTeacherAction(
                "📝 إدارة الامتحانات",
                "إضافة وتعديل وحذف الامتحانات",
                () -> teacherExams()
        );

        addTeacherAction(
                "❓ بنك الأسئلة",
                "إدارة الأسئلة والاختيارات والإجابات الصحيحة",
                () -> teacherQuestions()
        );

        addTeacherAction(
                "🔑 أكواد الامتحانات",
                "إنشاء وم
            // =========================================================
    // TEACHER BOTTOM NAVIGATION
    // =========================================================

    private void createTeacherBottomBar() {

        bottomBar = new LinearLayout(this);

        bottomBar.setOrientation(
                LinearLayout.HORIZONTAL
        );

        bottomBar.setGravity(
                Gravity.CENTER
        );

        bottomBar.setPadding(
                4,
                5,
                4,
                5
        );

        bottomBar.setBackgroundColor(
                darkMode
                        ? Color.rgb(30, 45, 39)
                        : white
        );

        addNavButton(
                "الرئيسية",
                () -> buildTeacherHome()
        );

        addNavButton(
                "الامتحانات",
                () -> teacherExams()
        );

        addNavButton(
                "الأسئلة",
                () -> teacherQuestions()
        );

        addNavButton(
                "النتائج",
                () -> teacherResults()
        );

        addNavButton(
                "الإعدادات",
                () -> teacherSettings()
        );

        root.addView(
                bottomBar,
                new LinearLayout.LayoutParams(
                        -1,
                        65
                )
        );
    }

    // =========================================================
    // TEACHER EXAMS
    // =========================================================

    private void teacherExams() {

        content.removeAllViews();

        TextView title = text(
                "📝 إدارة الامتحانات",
                27,
                darkMode ? white : green,
                true
        );

        title.setPadding(
                20,
                25,
                20,
                20
        );

        content.addView(title);

        LinearLayout current =
                cardLayout();

        TextView currentTitle =
                text(
                        "امتحان التاريخ - تجريبي",
                        20,
                        darkMode ? white : green,
                        true
                );

        current.addView(currentTitle);

        TextView details =
                text(
                        "10 أسئلة\nالمدة: 30 دقيقة\nالحالة: متاح",
                        15,
                        gray,
                        false
                );

        current.addView(
                details,
                marginParams(
                        -1,
                        -2,
                        0,
                        12,
                        0,
                        0
                )
        );

        TextView edit =
                secondaryButton(
                        "✏️ تعديل بيانات الامتحان"
                );

        edit.setOnClickListener(
                v -> editExamDialog()
        );

        current.addView(edit);

        content.addView(
                current,
                marginParams(
                        -1,
                        -2,
                        15,
                        10,
                        15,
                        0
                )
        );

        TextView add =
                button(
                        "➕ إضافة امتحان جديد"
                );

        add.setOnClickListener(
                v -> addExamDialog()
        );

        content.addView(
                add,
                marginParams(
                        -1,
                        60,
                        15,
                        10,
                        15,
                        0
                )
        );

        TextView delete =
                secondaryButton(
                        "🗑️ حذف الامتحان التجريبي"
                );

        delete.setOnClickListener(
                v -> {

                    new AlertDialog.Builder(this)
                            .setTitle("حذف الامتحان")
                            .setMessage(
                                    "هل تريد حذف الامتحان التجريبي؟"
                            )
                            .setNegativeButton(
                                    "إلغاء",
                                    null
                            )
                            .setPositiveButton(
                                    "حذف",
                                    (dialog, which) ->
                                            toast(
                                                    "تم حذف الامتحان من القائمة التجريبية"
                                            )
                            )
                            .show();
                }
        );

        content.addView(
                delete,
                marginParams(
                        -1,
                        55,
                        15,
                        10,
                        15,
                        0
                )
        );
    }

    // =========================================================
    // ADD EXAM
    // =========================================================

    private void addExamDialog() {

        LinearLayout box =
                vertical();

        box.setPadding(
                10,
                5,
                10,
                5
        );

        EditText name =
                input("اسم الامتحان");

        box.addView(name);

        EditText duration =
                input("مدة الامتحان بالدقائق");

        duration.setInputType(
                android.text.InputType.TYPE_CLASS_NUMBER
        );

        box.addView(
                duration,
                marginParams(
                        -1,
                        60,
                        0,
                        10,
                        0,
                        0
                )
        );

        new AlertDialog.Builder(this)
                .setTitle("إضافة امتحان")
                .setView(box)
                .setNegativeButton(
                        "إلغاء",
                        null
                )
                .setPositiveButton(
                        "إضافة",
                        (dialog, which) -> {

                            String examName =
                                    name.getText()
                                            .toString()
                                            .trim();

                            String minutes =
                                    duration.getText()
                                            .toString()
                                            .trim();

                            if (examName.isEmpty()) {
                                toast(
                                        "اكتب اسم الامتحان"
                                );
                                return;
                            }

                            if (minutes.isEmpty()) {
                                toast(
                                        "اكتب مدة الامتحان"
                                );
                                return;
                            }

                            prefs.edit()
                                    .putString(
                                            "custom_exam_name",
                                            examName
                                    )
                                    .putInt(
                                            "custom_exam_duration",
                                            Integer.parseInt(
                                                    minutes
                                            )
                                    )
                                    .apply();

                            toast(
                                    "تم حفظ بيانات الامتحان"
                            );

                            teacherExams();
                        }
                )
                .show();
    }

    // =========================================================
    // EDIT EXAM
    // =========================================================

    private void editExamDialog() {

        LinearLayout box =
                vertical();

        box.setPadding(
                10,
                5,
                10,
                5
        );

        String oldName =
                prefs.getString(
                        "custom_exam_name",
                        "امتحان التاريخ - تجريبي"
                );

        int oldDuration =
                prefs.getInt(
                        "custom_exam_duration",
                        30
                );

        EditText name =
                input("اسم الامتحان");

        name.setText(oldName);

        box.addView(name);

        EditText duration =
                input("المدة بالدقائق");

        duration.setInputType(
                android.text.InputType.TYPE_CLASS_NUMBER
        );

        duration.setText(
                String.valueOf(oldDuration)
        );

        box.addView(
                duration,
                marginParams(
                        -1,
                        60,
                        0,
                        10,
                        0,
                        0
                )
        );

        new AlertDialog.Builder(this)
                .setTitle("تعديل الامتحان")
                .setView(box)
                .setNegativeButton(
                        "إلغاء",
                        null
                )
                .setPositiveButton(
                        "حفظ",
                        (dialog, which) -> {

                            try {

                                String newName =
                                        name.getText()
                                                .toString()
                                                .trim();

                                int newDuration =
                                        Integer.parseInt(
                                                duration
                                                        .getText()
                                                        .toString()
                                                        .trim()
                                        );

                                if (newName.isEmpty()) {
                                    toast(
                                            "اكتب اسم الامتحان"
                                    );
                                    return;
                                }

                                prefs.edit()
                                        .putString(
                                                "custom_exam_name",
                                                newName
                                        )
                                        .putInt(
                                                "custom_exam_duration",
                                                newDuration
                                        )
                                        .apply();

                                toast(
                                        "تم تعديل الامتحان"
                                );

                                teacherExams();

                            } catch (Exception e) {

                                toast(
                                        "تأكد من إدخال البيانات بشكل صحيح"
                                );
                            }
                        }
                )
                .show();
    }

    // =========================================================
    // TEACHER QUESTIONS
    // =========================================================

    private void teacherQuestions() {

        content.removeAllViews();

        TextView title =
                text(
                        "❓ بنك الأسئلة",
                        27,
                        darkMode ? white : green,
                        true
                );

        title.setPadding(
                20,
                25,
                20,
                20
        );

        content.addView(title);

        TextView info =
                text(
                        "يمكنك إضافة أسئلة اختيار من متعدد وتحديد الإجابة الصحيحة وشرحها.",
                        15,
                        gray,
                        false
                );

        info.setPadding(
                20,
                0,
                20,
                15
        );

        content.addView(info);

        LinearLayout questionsCard =
                cardLayout();

        TextView count =
                text(
                        "📚 الأسئلة الحالية: " +
                                currentQuestions.size(),
                        20,
                        darkMode ? white : green,
                        true
                );

        questionsCard.addView(count);

        TextView open =
                button(
                        "عرض الأسئلة"
                );

        open.setOnClickListener(
                v -> showQuestionList()
        );

        questionsCard.addView(open);

        content.addView(
                questionsCard,
                marginParams(
                        -1,
                        -2,
                        15,
                        10,
                        15,
                        0
                )
        );

        TextView add =
                button(
                        "➕ إضافة سؤال جديد"
                );

        add.setOnClickListener(
                v -> addQuestionDialog()
        );

        content.addView(
                add,
                marginParams(
                        -1,
                        60,
                        15,
                        10,
                        15,
                        0
                )
        );
    }

    // =========================================================
    // SHOW QUESTION LIST
    // =========================================================

    private void showQuestionList() {

        content.removeAllViews();

        TextView title =
                text(
                        "📚 قائمة الأسئلة",
                        27,
                        darkMode ? white : green,
                        true
                );

        title.setPadding(
                20,
                25,
                20,
                20
        );

        content.addView(title);

        for (int i = 0;
             i < currentQuestions.size();
             i++) {

            Question q =
                    currentQuestions.get(i);

            LinearLayout card =
                    cardLayout();

            TextView number =
                    text(
                            "السؤال " + (i + 1),
                            18,
                            gold,
                            true
                    );

            card.addView(number);

            TextView question =
                    text(
                            q.question,
                            17,
                            darkMode ? white : black,
                            true
                    );

            card.addView(
                    question,
                    marginParams(
                            -1,
                            -2,
                            0,
                            8,
                            0,
                            0
                    )
            );

            TextView answer =
                    text(
                            "الإجابة الصحيحة: " +
                                    getCorrectText(q),
                            15,
                            green,
                            true
                    );

            card.addView(answer);

            TextView explanation =
                    text(
                            "الشرح: " +
                                    q.explanation,
                            14,
                            gray,
                            false
                    );

            card.addView(
                    explanation,
                    marginParams(
                            -1,
                            -2,
                            0,
                            0,
                            0,
                            0
                    )
            );

            content.addView(
                    card,
                    marginParams(
                            -1,
                            -2,
                            15,
                            8,
                            15,
                            0
                    )
            );
        }

        TextView add =
                button(
                        "➕ إضافة سؤال"
                );

        add.setOnClickListener(
                v -> addQuestionDialog()
        );

        content.addView(
                add,
                marginParams(
                        -1,
                        60,
                        15,
                        20,
                        15,
                        10
                )
        );
    }

    // =========================================================
    // ADD QUESTION
    // =========================================================

    private void addQuestionDialog() {

        LinearLayout box =
                vertical();

        box.setPadding(
                5,
                5,
                5,
                5
        );

        EditText question =
                input("نص السؤال");

        box.addView(question);

        EditText a =
                input("الاختيار الأول");

        box.addView(
                a,
                marginParams(
                        -1,
                        55,
                        0,
                        6,
                        0,
                        0
                )
        );

        EditText b =
                input("الاختيار الثاني");

        box.addView(
                b,
                marginParams(
                        -1,
                        55,
                        0,
                        6,
                        0,
                        0
                )
        );

        EditText c =
                input("الاختيار الثالث");

        box.addView(
                c,
                marginParams(
                        -1,
                        55,
                        0,
                        6,
                        0,
                        0
                )
        );

        EditText d =
                input("الاختيار الرابع");

        box.addView(
                d,
                marginParams(
                        -1,
                        55,
                        0,
                        6,
                        0,
                        0
                )
        );

        EditText correct =
                input(
                        "رقم الإجابة الصحيحة 1 أو 2 أو 3 أو 4"
                );

        correct.setInputType(
                android.text.InputType.TYPE_CLASS_NUMBER
        );

        box.addView(
                correct,
                marginParams(
                        -1,
                        55,
                        0,
                        6,
                        0,
                        0
                )
        );

        EditText explanation =
                input("شرح الإجابة");

        explanation.setMinLines(3);

        explanation.setGravity(
                Gravity.TOP | Gravity.RIGHT
        );

        box.addView(
                explanation,
                marginParams(
                        -1,
                    // =========================================================
    // TEACHER CODES
    // =========================================================

    private void teacherCodes() {

        content.removeAllViews();

        TextView title = text(
                "🔑 أكواد الامتحانات",
                27,
                darkMode ? white : green,
                true
        );

        title.setPadding(20, 25, 20, 20);
        content.addView(title);

        LinearLayout infoCard = cardLayout();

        TextView info = text(
                "الأكواد تستخدم للسماح للطلاب بالدخول إلى الامتحانات.",
                16,
                gray,
                false
        );

        infoCard.addView(info);

        content.addView(
                infoCard,
                marginParams(-1, -2, 15, 10, 15, 0)
        );

        String savedCode = prefs.getString(
                "exam_code",
                "MAESTRO-2026"
        );

        LinearLayout codeCard = cardLayout();

        TextView codeTitle = text(
                "🔐 الكود الحالي",
                20,
                darkMode ? white : green,
                true
        );

        codeCard.addView(codeTitle);

        TextView codeText = text(
                savedCode,
                24,
                gold,
                true
        );

        codeText.setGravity(Gravity.CENTER);

        codeCard.addView(
                codeText,
                marginParams(-1, -2, 0, 15, 0, 0)
        );

        TextView change = button(
                "🔄 إنشاء كود جديد"
        );

        change.setOnClickListener(
                v -> generateExamCode()
        );

        codeCard.addView(change);

        content.addView(
                codeCard,
                marginParams(-1, -2, 15, 10, 15, 0)
        );

        LinearLayout usage = cardLayout();

        TextView usageTitle = text(
                "📊 حالة الكود",
                20,
                darkMode ? white : green,
                true
        );

        usage.addView(usageTitle);

        TextView usageText = text(
                "عدد مرات الاستخدام: " +
                        prefs.getInt("code_uses", 0) +
                        "\nالحالة: فعال",
                16,
                gray,
                false
        );

        usage.addView(
                usageText,
                marginParams(-1, -2, 0, 0, 0, 0)
        );

        content.addView(
                usage,
                marginParams(-1, -2, 15, 10, 15, 0)
        );
    }

    // =========================================================
    // GENERATE CODE
    // =========================================================

    private void generateExamCode() {

        String code =
                "M-" +
                        (1000 +
                                (int)
                                        (Math.random() * 9000));

        prefs.edit()
                .putString(
                        "exam_code",
                        code
                )
                .putInt(
                        "code_uses",
                        0
                )
                .apply();

        new AlertDialog.Builder(this)
                .setTitle("تم إنشاء الكود")
                .setMessage(
                        "كود الامتحان الجديد:\n\n" +
                                code +
                                "\n\nيمكنك إعطاؤه للطلاب."
                )
                .setPositiveButton(
                        "حسنًا",
                        null
                )
                .show();

        teacherCodes();
    }

    // =========================================================
    // TEACHER RESULTS
    // =========================================================

    private void teacherResults() {

        content.removeAllViews();

        TextView title = text(
                "👨‍🎓 الطلاب والنتائج",
                27,
                darkMode ? white : green,
                true
        );

        title.setPadding(20, 25, 20, 20);
        content.addView(title);

        LinearLayout summary =
                cardLayout();

        TextView summaryTitle =
                text(
                        "📊 ملخص النتائج",
                        20,
                        darkMode ? white : green,
                        true
                );

        summary.addView(summaryTitle);

        int resultCount = 0;

        try {

            JSONArray results =
                    new JSONArray(
                            prefs.getString(
                                    "results",
                                    "[]"
                            )
                    );

            resultCount = results.length();

        } catch (Exception ignored) {
        }

        TextView summaryText =
                text(
                        "عدد النتائج المسجلة: " +
                                resultCount +
                                "\n\nيمكن للمدرس متابعة نتائج الطلاب "
                                +
                                "من خلال البيانات المسجلة على الجهاز.",
                        15,
                        gray,
                        false
                );

        summary.addView(
                summaryText,
                marginParams(-1, -2, 0, 0, 0, 0)
        );

        content.addView(
                summary,
                marginParams(-1, -2, 15, 10, 15, 0)
        );

        try {

            JSONArray results =
                    new JSONArray(
                            prefs.getString(
                                    "results",
                                    "[]"
                            )
                    );

            if (results.length() == 0) {

                LinearLayout empty =
                        cardLayout();

                TextView emptyText =
                        text(
                                "لا توجد نتائج طلاب حتى الآن.",
                                17,
                                gray,
                                false
                        );

                emptyText.setGravity(
                        Gravity.CENTER
                );

                empty.addView(emptyText);

                content.addView(
                        empty,
                        marginParams(
                                -1,
                                -2,
                                15,
                                10,
                                15,
                                0
                        )
                );

            } else {

                for (int i = results.length() - 1;
                     i >= 0;
                     i--) {

                    JSONObject result =
                            results.getJSONObject(i);

                    LinearLayout card =
                            cardLayout();

                    TextView exam =
                            text(
                                    "📝 " +
                                            result.optString(
                                                    "exam"
                                            ),
                                    19,
                                    darkMode
                                            ? white
                                            : green,
                                    true
                            );

                    card.addView(exam);

                    TextView student =
                            text(
                                    "👤 الطالب: " +
                                            prefs.getString(
                                                    STUDENT_NAME,
                                                    "غير معروف"
                                            ),
                                    15,
                                    darkMode
                                            ? white
                                            : black,
                                    false
                            );

                    card.addView(
                            student,
                            marginParams(
                                    -1,
                                    -2,
                                    0,
                                    7,
                                    0,
                                    0
                            )
                    );

                    TextView resultText =
                            text(
                                    "الدرجة: " +
                                            result.optInt(
                                                    "correct"
                                            ) +
                                            " / " +
                                            result.optInt(
                                                    "total"
                                            ) +
                                            "\nصحيح: " +
                                            result.optInt(
                                                    "correct"
                                            ) +
                                            " | خطأ: " +
                                            result.optInt(
                                                    "wrong"
                                            ) +
                                            " | بدون إجابة: " +
                                            result.optInt(
                                                    "unanswered"
                                            ),
                                    15,
                                    gray,
                                    false
                            );

                    card.addView(resultText);

                    content.addView(
                            card,
                            marginParams(
                                    -1,
                                    -2,
                                    15,
                                    8,
                                    15,
                                    0
                            )
                    );
                }
            }

        } catch (Exception e) {

            toast(
                    "حدث خطأ في قراءة النتائج"
            );
        }
    }

    // =========================================================
    // TEACHER NOTES
    // =========================================================

    private void teacherNotes() {

        content.removeAllViews();

        TextView title =
                text(
                        "📚 مذكرات الشرح",
                        27,
                        darkMode ? white : green,
                        true
                );

        title.setPadding(
                20,
                25,
                20,
                20
        );

        content.addView(title);

        LinearLayout noteCard =
                cardLayout();

        TextView noteTitle =
                text(
                        "📖 إضافة مذكرة",
                        20,
                        darkMode ? white : green,
                        true
                );

        noteCard.addView(noteTitle);

        EditText noteInput =
                input(
                        "اكتب عنوان أو محتوى المذكرة"
                );

        noteInput.setMinLines(5);

        noteInput.setGravity(
                Gravity.TOP | Gravity.RIGHT
        );

        noteCard.addView(
                noteInput,
                marginParams(
                        -1,
                        130,
                        0,
                        12,
                        0,
                        0
                )
        );

        TextView save =
                button(
                        "💾 حفظ المذكرة"
                );

        save.setOnClickListener(
                v -> {

                    String note =
                            noteInput
                                    .getText()
                                    .toString()
                                    .trim();

                    if (note.isEmpty()) {

                        toast(
                                "اكتب محتوى المذكرة أولًا"
                        );

                        return;
                    }

                    prefs.edit()
                            .putString(
                                    "teacher_note",
                                    note
                            )
                            .apply();

                    toast(
                            "تم حفظ المذكرة"
                    );

                    teacherNotes();
                }
        );

        noteCard.addView(save);

        content.addView(
                noteCard,
                marginParams(
                        -1,
                        -2,
                        15,
                        10,
                        15,
                        0
                )
        );

        String savedNote =
                prefs.getString(
                        "teacher_note",
                        ""
                );

        if (!savedNote.isEmpty()) {

            LinearLayout saved =
                    cardLayout();

            TextView savedTitle =
                    text(
                            "📌 المذكرة المحفوظة",
                            20,
                            darkMode
                                    ? white
                                    : green,
                            true
                    );

            saved.addView(savedTitle);

            TextView savedBody =
                    text(
                            savedNote,
                            16,
                            darkMode
                                    ? white
                                    : black,
                            false
                    );

            saved.addView(
                    savedBody,
                    marginParams(
                            -1,
                            -2,
                            0,
                            12,
                            0,
                            0
                    )
            );

            TextView delete =
                    secondaryButton(
                            "🗑️ حذف المذكرة"
                    );

            delete.setOnClickListener(
                    v -> {

                        prefs.edit()
                                .remove(
                                        "teacher_note"
                                )
                                .apply();

                        teacherNotes();
                    }
            );

            saved.addView(delete);

            content.addView(
                    saved,
                    marginParams(
                            -1,
                            -2,
                            15,
                            10,
                            15,
                            0
                    )
            );
        }
    }

    // =========================================================
    // TEACHER GROUPS
    // =========================================================

    private void teacherGroups() {

        content.removeAllViews();

        TextView title =
                text(
                        "👥 مجموعات الطلاب",
                        27,
                        darkMode ? white : green,
                        true
                );

        title.setPadding(
                20,
                25,
                20,
                20
        );

        content.addView(title);

        addGroupCard(
                "جروب أولى بكالوريا بنات"
        );

        addGroupCard(
                "جروب أولى بكالوريا ولاد"
        );

        addGroupCard(
                "جروب تانية بكالوريا بنات"
        );

        addGroupCard(
                "جروب تانية بكالوريا ولاد"
        );

        addGroupCard(
                "طلاب المايسترو تالتة إعدادي"
        );

        addGroupCard(
                "جروب ذكرى ومنفعة"
        );
    }

    private void addGroupCard(
            String groupName
    ) {

        LinearLayout card =
                cardLayout();

        TextView title =
                text(
                        "👥 " + groupName,
                        19,
                        darkMode ? white : green,
                        true
                );

        card.addView(title);

        TextView count =
                text(
                        "عدد الطلاب المسجلين: " +
                                "0",
                        15,
                        gray,
                        false
                );

        card.addView(
                count,
                marginParams(
                        -1,
                        -2,
                        0,
                        10,
                        0,
                        0
                )
        );

        TextView manage =
                secondaryButton(
                        "إدارة المجموعة"
                );

        manage.setOnClickListener(
                v -> showGroupDialog(groupName)
        );

        card.addView(manage);

        content.addView(
                card,
                marginParams(
                        -1,
                        -2,
                        15,
                        8,
                        15,
                        0
                )
        );
    }

    private void showGroupDialog(
            String groupName
    ) {

        new AlertDialog.Builder(this)
                .setTitle(groupName)
                .setMessage(
                        "هذه مساحة المجموعة.\n\n"
                                +
                                "يمكن لاحقًا ربطها بقاعدة بيانات Firebase "
                                +
                                "لعرض الطلاب والامتحانات والنتائج الخاصة بها."
                )
                .setPositiveButton(
                        "حسنًا",
                        null
                )
                .show();
    }

    // =========================================================
    // TEACHER SETTINGS
    // =========================================================

    private void teacherSettings() {

        content.removeAllViews();

        TextView title =
                text(
                        "⚙️ إعدادات المدرس",
                        27,
                        darkMode ? white : green,
                        true
                );

        title.setPadding(
                20,
                25,
                20,
                20
        );

        content.addView(title);

        LinearLayout profile =
                cardLayout();

        TextView profileTitle =
                text(
                        "👨‍🏫 حساب المدرس",
                        20,
                        darkMode ? white : green,
                        true
                );

        profile.addView(profileTitle);

        String teacher =
                prefs.getString(
                        "teacher_name",
                        "غير معروف"
                );

        TextView teacherName =
                text(
                        "الاسم: " + teacher,
                        16,
                        darkMode ? white : black,
                        false
                );

        profile.addView(teacherName);

        content.addView(
                profile,
                marginParams(
                        -1,
                        -2,
                        15,
                        10,
                    // =========================================================
    // ABOUT
    // =========================================================

    private void showAbout() {

        new AlertDialog.Builder(this)
                .setTitle("ℹ️ عن المايسترو")
                .setMessage(
                        "المايسترو\n\n" +
                        "المايسترو شريف هيبه\n" +
                        "هتتعلم التاريخ ببساطة 📚\n\n" +
                        "تطبيق تعليمي للطلاب والمدرسين، " +
                        "ويحتوي على الامتحانات والنتائج والمذكرات والأذكار.\n\n" +
                        "مع المبرمج أو المطور محمود كليب\n" +
                        "للتواصل: 01112244710"
                )
                .setPositiveButton(
                        "حسنًا",
                        null
                )
                .show();
    }

    // =========================================================
    // UI HELPERS
    // =========================================================

    private LinearLayout vertical() {

        LinearLayout layout =
                new LinearLayout(this);

        layout.setOrientation(
                LinearLayout.VERTICAL
        );

        return layout;
    }

    private LinearLayout cardLayout() {

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setPadding(
                18,
                18,
                18,
                18
        );

        android.graphics.drawable.GradientDrawable bg =
                new android.graphics.drawable.GradientDrawable();

        bg.setCornerRadius(22);

        if (darkMode) {

            bg.setColor(
                    Color.rgb(35, 43, 40)
            );

        } else {

            bg.setColor(
                    Color.WHITE
            );
        }

        bg.setStroke(
                1,
                darkMode
                        ? Color.rgb(70, 85, 78)
                        : Color.rgb(225, 215, 190)
        );

        card.setBackground(bg);

        return card;
    }

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
                Gravity.RIGHT |
                Gravity.CENTER_VERTICAL
        );

        tv.setTypeface(
                Typeface.DEFAULT,
                bold
                        ? Typeface.BOLD
                        : Typeface.NORMAL
        );

        tv.setPadding(
                4,
                4,
                4,
                4
        );

        return tv;
    }

    private EditText input(
            String hint
    ) {

        EditText edit =
                new EditText(this);

        edit.setHint(hint);

        edit.setTextSize(16);

        edit.setSingleLine(false);

        edit.setPadding(
                18,
                5,
                18,
                5
        );

        edit.setGravity(
                Gravity.RIGHT |
                Gravity.CENTER_VERTICAL
        );

        edit.setTextColor(
                darkMode
                        ? white
                        : black
        );

        edit.setHintTextColor(
                darkMode
                        ? Color.LTGRAY
                        : Color.GRAY
        );

        android.graphics.drawable.GradientDrawable bg =
                new android.graphics.drawable.GradientDrawable();

        bg.setCornerRadius(18);

        bg.setColor(
                darkMode
                        ? Color.rgb(30, 35, 33)
                        : Color.rgb(248, 248, 248)
        );

        bg.setStroke(
                1,
                darkMode
                        ? Color.rgb(80, 90, 85)
                        : Color.rgb(215, 215, 215)
        );

        edit.setBackground(bg);

        return edit;
    }

    private TextView button(
            String value
    ) {

        TextView btn =
                text(
                        value,
                        16,
                        white,
                        true
                );

        btn.setGravity(
                Gravity.CENTER
        );

        btn.setPadding(
                12,
                12,
                12,
                12
        );

        android.graphics.drawable.GradientDrawable bg =
                new android.graphics.drawable.GradientDrawable();

        bg.setCornerRadius(20);

        bg.setColor(green);

        bg.setStroke(
                1,
                gold
        );

        btn.setBackground(bg);

        return btn;
    }

    private TextView secondaryButton(
            String value
    ) {

        TextView btn =
                text(
                        value,
                        15,
                        darkMode
                                ? white
                                : green,
                        true
                );

        btn.setGravity(
                Gravity.CENTER
        );

        btn.setPadding(
                12,
                10,
                12,
                10
        );

        android.graphics.drawable.GradientDrawable bg =
                new android.graphics.drawable.GradientDrawable();

        bg.setCornerRadius(18);

        bg.setColor(
                darkMode
                        ? Color.rgb(35, 43, 40)
                        : Color.WHITE
        );

        bg.setStroke(
                2,
                gold
        );

        btn.setBackground(bg);

        return btn;
    }

    private LinearLayout.LayoutParams marginParams(
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
                left,
                top,
                right,
                bottom
        );

        return params;
    }

    // =========================================================
    // TOAST
    // =========================================================

    private void toast(
            String message
    ) {

        Toast.makeText(
                this,
                message,
                Toast.LENGTH_SHORT
        ).show();
    }

    // =========================================================
    // BACK BUTTON PROTECTION
    // =========================================================

    @Override
    public void onBackPressed() {

        if (examRunning) {

            new AlertDialog.Builder(this)
                    .setTitle("الامتحان قيد التشغيل")
                    .setMessage(
                            "لا يمكنك الخروج من الامتحان قبل تسليمه."
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
    // LIFECYCLE SAFETY
    // =========================================================

    @Override
    protected void onDestroy() {

        if (examTimer != null) {

            examTimer.cancel();

            examTimer = null;
        }

        super.onDestroy();
    }

    // =========================================================
    // END OF MAIN ACTIVITY
    // =========================================================

                    }
