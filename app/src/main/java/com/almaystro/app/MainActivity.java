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
            content.addView(te
