package com.almaystro.app;

import android.app.*;
import android.os.*;
import android.graphics.*;
import android.graphics.drawable.GradientDrawable;
import android.text.InputType;
import android.view.*;
import android.widget.*;
import android.content.*;
import org.json.*;
import java.util.*;

public class MainActivity extends Activity {
    private LinearLayout content;
    private boolean darkMode=true;
    private String student="الطالب", teacher="المدرس";
    private CountDownTimer timer;
    private boolean examActive=false;
    private String activeCode="";
    private android.content.SharedPreferences sp;
    private final int GOLD=Color.rgb(224,190,70), DG=Color.rgb(5,38,27), GREEN=Color.rgb(14,70,48);

    @Override public void onCreate(Bundle b){
        super.onCreate(b);
        sp=getSharedPreferences("almaystro",MODE_PRIVATE);
        darkMode=sp.getBoolean("dark",true);
        showWelcome();
    }
    private int dp(int x){return (int)(x*getResources().getDisplayMetrics().density+.5f);}
    private int fg(){return darkMode?Color.WHITE:Color.rgb(35,35,35);}
    private int sub(){return darkMode?Color.LTGRAY:Color.rgb(85,85,85);}
    private int card(){return darkMode?GREEN:Color.WHITE;}
    private GradientDrawable bg(){
        return new GradientDrawable(GradientDrawable.Orientation.TL_BR,
            darkMode?new int[]{DG,GREEN,DG}:new int[]{Color.rgb(245,242,232),Color.WHITE,Color.rgb(235,232,220)});
    }
    private TextView txt(String s,float z,int c){
        TextView t=new TextView(this); t.setText(s);t.setTextSize(z);t.setTextColor(c);
        t.setGravity(Gravity.CENTER);t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        t.setPadding(dp(8),dp(7),dp(8),dp(7));return t;
    }
    private TextView btn(String s){
        TextView b=txt(s,17,darkMode?DG:Color.rgb(20,60,45));
        GradientDrawable g=new GradientDrawable();g.setColor(GOLD);g.setCornerRadius(dp(28));b.setBackground(g);
        LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,dp(58));
        p.setMargins(dp(15),dp(6),dp(15),dp(6));b.setLayoutParams(p);return b;
    }
    private EditText inp(String hint){
        EditText e=new EditText(this);e.setHint(hint);e.setTextSize(16);e.setTextColor(fg());e.setHintTextColor(sub());
        e.setGravity(Gravity.CENTER);e.setSingleLine(true);
        GradientDrawable g=new GradientDrawable();g.setColor(card());g.setCornerRadius(dp(17));g.setStroke(dp(1),GOLD);e.setBackground(g);
        LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,dp(55));p.setMargins(dp(15),dp(4),dp(15),dp(4));e.setLayoutParams(p);return e;
    }
    private void screen(){
        LinearLayout root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(dp(15),dp(12),dp(15),dp(12));root.setGravity(Gravity.CENTER_HORIZONTAL);root.setBackground(bg());
        ScrollView sv=new ScrollView(this);sv.setFillViewport(true);sv.setBackground(bg());content=root;sv.addView(root);setContentView(sv);
    }
    private void header(String a,String b){
        content.addView(txt("✦  ❖  ✦",22,GOLD));content.addView(txt(a,29,GOLD));
        if(b!=null&&!b.isEmpty())content.addView(txt(b,16,sub()));content.addView(txt("❖",20,GOLD));
    }
    private void toast(String s){Toast.makeText(this,s,Toast.LENGTH_SHORT).show();}

    private void showWelcome(){
        if(timer!=null){timer.cancel();timer=null;} screen();
        ImageView im=new ImageView(this);im.setImageResource(R.drawable.maestro);im.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        content.addView(im,new LinearLayout.LayoutParams(-1,dp(185)));
        content.addView(txt("المايسترو",38,GOLD));content.addView(txt("المايسترو شريف هيبه",21,fg()));
        content.addView(txt("هتتعلم التاريخ ببساطة",18,sub()));content.addView(txt("✦  ❖  ✦",22,GOLD));
        TextView b=btn("ابدأ الآن");b.setOnClickListener(v->roles());content.addView(b);
        content.addView(txt("منصة تعليمية للامتحانات والمراجعة والتطوير",14,sub()));
    }
    private void roles(){
        screen();header("اختيار نوع الدخول","اختر طريقة الدخول إلى المايسترو");
        TextView s=btn("👨‍🎓  أنا طالب");s.setOnClickListener(v->studentLogin());content.addView(s);
        TextView t=btn("👨‍🏫  أنا مدرس");t.setOnClickListener(v->teacherLogin());content.addView(t);
        TextView b=btn("رجوع");b.setOnClickListener(v->showWelcome());content.addView(b);
    }

    private void studentLogin(){
        screen();header("دخول الطالب","اكتب الاسم وكود الامتحان");
        EditText n=inp("اسم الطالب"),c=inp("كود الامتحان");content.addView(n);content.addView(c);
        TextView b=btn("دخول الطالب");b.setOnClickListener(v->{
            String nn=n.getText().toString().trim(),cc=c.getText().toString().trim();
            if(nn.isEmpty()){n.setError("اكتب الاسم");return;}
            if(cc.isEmpty()){c.setError("اكتب الكود");return;}
            JSONObject ex=findByCode(cc);
            if(ex==null){c.setError("الكود غير موجود");return;}
            String key=nn.toLowerCase(Locale.ROOT)+"|"+cc.toLowerCase(Locale.ROOT);
            if(isCodeUsed(key)){
                c.setError("هذا الكود تم استخدامه من قبل لهذا الطالب");
                toast("الكود مخصص لمحاولة واحدة لهذا الطالب.");
                return;
            }
            student=nn;
            activeCode=cc;
            markCodeUsed(key);
            startExam(ex);
        });content.addView(b);
        TextView back=btn("رجوع");back.setOnClickListener(v->roles());content.addView(back);
    }
    private void studentHome(){
        screen();header("أهلًا يا "+student,"رحلتك التعليمية في مكان واحد");
        addStudentButton("📝  دخول امتحان",v->studentLogin());
        addStudentButton("📊  امتحاناتي ونتائجي",v->myExams());
        addStudentButton("📚  مذكرات الشرح",v->notes());
        addStudentButton("🤲  الأذكار والأوراد",v->azkar());
        addStudentButton("🏆  إنجازاتي وتقدمي",v->progress());
        addStudentButton("🔔  التحديثات",v->updates());
        addStudentButton("⚙️  الإعدادات",v->settings());
        addStudentButton("تسجيل خروج",v->roles());bottom();
    }
    private void addStudentButton(String s,View.OnClickListener l){TextView b=btn(s);b.setOnClickListener(l);content.addView(b);}

    private void myExams(){
        screen();header("امتحاناتي ونتائجي","سجل نتائج "+student);
        JSONArray a=arr("results");boolean found=false;
        for(int i=0;i<a.length();i++)try{JSONObject r=a.getJSONObject(i);
            if(student.equals(r.optString("student"))){found=true;
                content.addView(txt("📝 "+r.optString("exam")+"\nالدرجة: "+r.optInt("correct")+"/"+r.optInt("total")+
                    "\nتمت الإجابة: "+r.optInt("answered")+" | بدون إجابة: "+(r.optInt("total")-r.optInt("answered"))+
                    "\n"+r.optString("mistakes","لا توجد أخطاء"),16,fg()));}
        }catch(Exception ignored){}
        if(!found)content.addView(txt("لا توجد نتائج مسجلة حتى الآن.",17,fg()));
        backStudent();bottom();
    }
    private void notes(){
        screen();header("مذكرات الشرح","المذكرات المنشورة للطلاب");
        JSONArray a=arr("notes");if(a.length()==0)content.addView(txt("لا توجد مذكرات حاليًا.",17,fg()));
        for(int i=0;i<a.length();i++)try{JSONObject n=a.getJSONObject(i);
            content.addView(txt("📖 "+n.optString("title")+"\n"+n.optString("body"),17,fg()));
        }catch(Exception ignored){}
        backStudent();bottom();
    }
    private void azkar(){
        screen();header("الأذكار والأوراد","اذكر الله واطمئن قلبك 🤍");
        content.addView(txt("☀️ أذكار الصباح",23,GOLD));
        content.addView(txt("آية الكرسي\nالإخلاص والفلق والناس\nسبحان الله وبحمده\nأستغفر الله وأتوب إليه\nلا إله إلا الله وحده لا شريك له",18,fg()));
        content.addView(txt("🌙 أذكار المساء",23,GOLD));
        content.addView(txt("آية الكرسي\nالإخلاص والفلق والناس\nأمسينا وأمسى الملك لله\nاللهم بك أمسينا وبك أصبحنا",18,fg()));
        content.addView(txt("📿 ورد يومي",23,GOLD));
        content.addView(txt("استغفار • تسبيح • صلاة على النبي ﷺ\nيمكنك متابعة وردك يوميًا من هذا القسم.",17,fg()));
        backStudent();bottom();
    }
    private void progress(){
        screen();header("إنجازاتي وتقدمي","تابع رحلتك التعليمية");
        JSONArray r=arr("results");int count=0,correct=0,total=0;
        for(int i=0;i<r.length();i++)try{JSONObject x=r.getJSONObject(i);if(student.equals(x.optString("student"))){count++;correct+=x.optInt("correct");total+=x.optInt("total");}}catch(Exception ignored){}
        content.addView(txt("🏆 الامتحانات المنجزة: "+count+"\n✅ مجموع الإجابات الصحيحة: "+correct+"\n📚 مجموع الأسئلة: "+total,20,fg()));
        if(count>0)content.addView(txt("🌟 أحسنت! استمر في المراجعة والتدريب.",17,GOLD));
        else content.addView(txt("ابدأ أول امتحان لتحصل على أول إنجاز.",17,sub()));
        backStudent();bottom();
    }
    private void updates(){
        screen();header("التحديثات","آخر أخبار المايسترو");
        content.addView(txt("📢 مرحبًا بك في المايسترو\nتابع هذا القسم لمعرفة الامتحانات والمذكرات والتحديثات الجديدة.",18,fg()));
        content.addView(txt("🔔 سيتم عرض التنبيهات الجديدة هنا عند تفعيل المزامنة.",16,sub()));
        backStudent();bottom();
    }
    private void settings(){
        screen();header("الإعدادات","تحكم في تجربة التطبيق");
        TextView m=btn(darkMode?"☀️  الوضع الفاتح":"🌙  الوضع الداكن");m.setOnClickListener(v->{darkMode=!darkMode;sp.edit().putBoolean("dark",darkMode).apply();settings();});content.addView(m);
        TextView br=btn("🔆  مستوى السطوع");br.setOnClickListener(v->brightness());content.addView(br);
        TextView no=btn("🔔  الإشعارات");no.setOnClickListener(v->toast("إعداد الإشعارات جاهز للربط بالمزامنة."));content.addView(no);
        content.addView(txt("🔒 الخصوصية والأمان",21,GOLD));
        content.addView(txt("في النسخة الحالية تُحفظ البيانات محليًا على الجهاز. سيتم ربط الحسابات والبيانات عبر Firebase في مرحلة المزامنة.",15,sub()));
        content.addView(txt("ℹ️ عن التطبيق",21,GOLD));
        content.addView(txt("المايسترو شريف هيبه\nهتتعلم التاريخ ببساطة\nمع المبرمج أو المطور محمود كليب\nللتواصل: 01112244710",15,fg()));
        backStudent();
    }
    private void brightness(){
        SeekBar s=new SeekBar(this);s.setMax(100);s.setProgress(sp.getInt("brightness",100));
        new AlertDialog.Builder(this).setTitle("السطوع").setMessage("مستوى واجهة التطبيق").setView(s)
            .setPositiveButton("حفظ",(d,w)->{sp.edit().putInt("brightness",s.getProgress()).apply();toast("تم الحفظ");})
            .setNegativeButton("إلغاء",null).show();
    }
    private void backStudent(){TextView b=btn("رجوع للرئيسية");b.setOnClickListener(v->studentHome());content.addView(b);}

    private void bottom(){
        LinearLayout bar=new LinearLayout(this);bar.setOrientation(LinearLayout.HORIZONTAL);
        GradientDrawable g=new GradientDrawable();g.setColor(card());g.setCornerRadius(dp(20));g.setStroke(dp(1),GOLD);bar.setBackground(g);
        nav(bar,"الرئيسية",v->studentHome());nav(bar,"امتحاناتي",v->myExams());nav(bar,"الأذكار",v->azkar());nav(bar,"المذكرات",v->notes());nav(bar,"الإعدادات",v->settings());
        LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,dp(68));p.setMargins(2,dp(15),2,dp(4));content.addView(bar,p);
    }
    private void nav(LinearLayout b,String s,View.OnClickListener l){TextView t=txt(s,11,GOLD);t.setOnClickListener(l);b.addView(t,new LinearLayout.LayoutParams(0,-1,1));}

    private void teacherLogin(){
        screen();header("دخول المدرس","اكتب اسم المدرس وكود الدخول");
        EditText n=inp("اسم المدرس"),c=inp("كود الدخول");c.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        content.addView(n);content.addView(c);
        TextView b=btn("دخول لوحة المدرس");b.setOnClickListener(v->{String nn=n.getText().toString().trim(),cc=c.getText().toString().trim();
            if(nn.isEmpty()){n.setError("اكتب الاسم");return;}if(!"1234".equals(cc)){c.setError("الكود غير صحيح");return;}teacher=nn;teacherHome();});content.addView(b);
        TextView back=btn("رجوع");back.setOnClickListener(v->roles());content.addView(back);
    }
    private void teacherHome(){
        screen();header("لوحة المدرس","أهلًا يا "+teacher);
        addTeacherButton("📝  إدارة الامتحانات",v->examManager());
        addTeacherButton("❓  الأسئلة والأكواد",v->questionManager());
        addTeacherButton("📊  الطلاب والنتائج",v->teacherResults());
        addTeacherButton("📚  إضافة مذكرات شرح",v->teacherNotes());
        addTeacherButton("👥  مجموعات الطلاب",v->groups());
        addTeacherButton("📢  الإعلانات والتحديثات",v->updatesManager());
        addTeacherButton("⚙️  إعدادات المدرس",v->teacherSettings());
        addTeacherButton("تسجيل خروج",v->roles());
    }
    private void addTeacherButton(String s,View.OnClickListener l){TextView b=btn(s);b.setOnClickListener(l);content.addView(b);}

    private void examManager(){
        screen();header("إدارة الامتحانات","إنشاء وتنظيم الامتحانات");
        TextView a=btn("➕  إضافة امتحان");a.setOnClickListener(v->addExamDialog());content.addView(a);
        JSONArray es=arr("exams");if(es.length()==0)content.addView(txt("لا توجد امتحانات. أضف أول امتحان.",17,fg()));
        for(int i=0;i<es.length();i++)try{JSONObject e=es.getJSONObject(i);String id=e.optString("id");
            content.addView(txt("📝 "+e.optString("title")+"\n⏱️ "+e.optInt("duration",30)+" دقيقة\n❓ "+e.optJSONArray("questions").length()+" سؤال",17,fg()));
            TextView q=btn("إدارة أسئلة الامتحان");q.setOnClickListener(v->questionsForExam(id));content.addView(q);
            TextView d=btn("🗑️ حذف");d.setOnClickListener(v->{deleteExam(id);examManager();});content.addView(d);
        }catch(Exception ignored){}
        teacherBack();
    }
    private void addExamDialog(){
        LinearLayout box=new LinearLayout(this);box.setOrientation(LinearLayout.VERTICAL);EditText t=inp("اسم الامتحان"),d=inp("المدة بالدقائق");box.addView(t);box.addView(d);
        new AlertDialog.Builder(this).setTitle("إضافة امتحان").setView(box).setPositiveButton("حفظ",(x,w)->{
            if(t.getText().toString().trim().isEmpty()){toast("اكتب اسم الامتحان");return;}
            try{JSONArray a=arr("exams");JSONObject e=new JSONObject();e.put("id",System.currentTimeMillis()+"");e.put("title",t.getText().toString().trim());e.put("duration",Math.max(1,integer(d.getText().toString(),30)));e.put("questions",new JSONArray());a.put(e);save("exams",a);examManager();}catch(Exception ignored){}
        }).setNegativeButton("إلغاء",null).show();
    }
    private void questionsForExam(String id){
        JSONObject e=findExam(id);if(e==null)return;screen();header("أسئلة "+e.optString("title"),"بنك أسئلة اختيار من متعدد");
        TextView add=btn("➕ إضافة سؤال");add.setOnClickListener(v->addQuestion(id));content.addView(add);
        JSONArray q=e.optJSONArray("questions");if(q==null)q=new JSONArray();
        for(int i=0;i<q.length();i++)try{JSONObject x=q.getJSONObject(i);int k=i;
            content.addView(txt((i+1)+") "+x.optString("q")+"\nأ) "+x.optString("a")+"\nب) "+x.optString("b")+"\nج) "+x.optString("c")+"\nد) "+x.optString("d")+"\nالصحيح: "+x.optString("ans"),15,fg()));
            TextView del=btn("حذف السؤال");del.setOnClickListener(v->{JSONObject z=findExam(id);try{z.getJSONArray("questions").remove(k);updateExam(z);questionsForExam(id);}catch(Exception ignored){}});content.addView(del);
        }catch(Exception ignored){}
        TextView back=btn("رجوع للامتحان");back.setOnClickListener(v->examManager());content.addView(back);
    }
    private void addQuestion(String id){
        LinearLayout box=new LinearLayout(this);box.setOrientation(LinearLayout.VERTICAL);
        EditText q=inp("السؤال"),a=inp("اختيار أ"),b=inp("اختيار ب"),c=inp("اختيار ج"),d=inp("اختيار د"),ans=inp("الإجابة الصحيحة: أ/ب/ج/د");
        box.addView(q);box.addView(a);box.addView(b);box.addView(c);box.addView(d);box.addView(ans);
        new AlertDialog.Builder(this).setTitle("إضافة سؤال").setView(box).setPositiveButton("حفظ",(x,w)->{
            String aa=ans.getText().toString().trim();if(!Arrays.asList("أ","ب","ج","د").contains(aa)){toast("اكتب أ أو ب أو ج أو د");return;}
            JSONObject e=findExam(id);try{JSONArray ar=e.getJSONArray("questions");JSONObject z=new JSONObject();z.put("q",q.getText().toString().trim());z.put("a",a.getText().toString().trim());z.put("b",b.getText().toString().trim());z.put("c",c.getText().toString().trim());z.put("d",d.getText().toString().trim());z.put("ans",aa);ar.put(z);updateExam(e);questionsForExam(id);}catch(Exception ignored){}
        }).setNegativeButton("إلغاء",null).show();
    }
    private void questionManager(){
        screen();header("الأسئلة والأكواد","اختر الامتحان لإدارة أسئلته");
        JSONArray e=arr("exams");for(int i=0;i<e.length();i++)try{JSONObject x=e.getJSONObject(i);String id=x.optString("id");TextView b=btn("❓ "+x.optString("title"));b.setOnClickListener(v->questionsForExam(id));content.addView(b);}catch(Exception ignored){}
        TextView c=btn("🔑 إدارة الأكواد");c.setOnClickListener(v->codes());content.addView(c);teacherBack();
    }
    private void codes(){
        screen();header("أكواد الامتحانات","أنشئ كودًا للدخول إلى الامتحان");
        TextView add=btn("➕ إنشاء كود");add.setOnClickListener(v->addCode());content.addView(add);
        JSONArray a=arr("codes");if(a.length()==0)content.addView(txt("لا توجد أكواد.",17,fg()));
        for(int i=0;i<a.length();i++)try{JSONObject x=a.getJSONObject(i);content.addView(txt("🔐 "+x.optString("code")+"  →  "+x.optString("exam"),17,fg()));}catch(Exception ignored){}
        TextView b=btn("رجوع");b.setOnClickListener(v->questionManager());content.addView(b);
    }
    private void addCode(){
        LinearLayout box=new LinearLayout(this);box.setOrientation(LinearLayout.VERTICAL);EditText c=inp("الكود"),e=inp("اسم الامتحان");box.addView(c);box.addView(e);
        new AlertDialog.Builder(this).setTitle("إنشاء كود").setView(box).setPositiveButton("حفظ",(d,w)->{try{JSONArray a=arr("codes");JSONObject x=new JSONObject();x.put("code",c.getText().toString().trim());x.put("exam",e.getText().toString().trim());a.put(x);save("codes",a);codes();}catch(Exception ignored){}}).setNegativeButton("إلغاء",null).show();
    }
    private void teacherResults(){
        screen();header("الطلاب والنتائج","متابعة نتائج الطلاب");
        JSONArray a=arr("results");if(a.length()==0)content.addView(txt("لا توجد نتائج حتى الآن.",17,fg()));
        for(int i=0;i<a.length();i++)try{JSONObject r=a.getJSONObject(i);content.addView(txt("👤 "+r.optString("student")+"\n📝 "+r.optString("exam")+"\nالدرجة: "+r.optInt("correct")+"/"+r.optInt("total")+"\nتمت الإجابة: "+r.optInt("answered"),17,fg()));}catch(Exception ignored){}
        TextView c=btn("🗑️ مسح النتائج");c.setOnClickListener(v->{sp.edit().remove("results").apply();teacherResults();});content.addView(c);teacherBack();
    }
    private void teacherNotes(){
        screen();header("مذكرات الشرح","أضف مذكرات تظهر للطلاب");
        TextView a=btn("➕ إضافة مذكرة");a.setOnClickListener(v->addNote());content.addView(a);
        JSONArray ar=arr("notes");for(int i=0;i<ar.length();i++)try{JSONObject n=ar.getJSONObject(i);int k=i;content.addView(txt("📖 "+n.optString("title")+"\n"+n.optString("body"),16,fg()));TextView d=btn("حذف");d.setOnClickListener(v->{JSONArray z=arr("notes");z.remove(k);save("notes",z);teacherNotes();});content.addView(d);}catch(Exception ignored){}
        teacherBack();
    }
    private void addNote(){
        LinearLayout box=new LinearLayout(this);box.setOrientation(LinearLayout.VERTICAL);EditText t=inp("عنوان المذكرة"),b=inp("محتوى المذكرة");b.setSingleLine(false);b.setMinLines(4);box.addView(t);box.addView(b);
        new AlertDialog.Builder(this).setTitle("مذكرة جديدة").setView(box).setPositiveButton("حفظ",(d,w)->{try{JSONArray a=arr("notes");JSONObject n=new JSONObject();n.put("title",t.getText().toString().trim());n.put("body",b.getText().toString().trim());a.put(n);save("notes",a);teacherNotes();}catch(Exception ignored){}}).setNegativeButton("إلغاء",null).show();
    }
    private void groups(){
        screen();header("مجموعات الطلاب","تنظيم الطلاب");
        String[] gs={"جروب أولى بكالوريا بنات","جروب أولى بكالوريا ولاد","جروب تانية بكالوريا بنات","جروب تانية بكالوريا ولاد","طلاب المايسترو تالتة إعدادي","جروب ذكرى ومنفعة"};
        for(String g:gs){TextView b=btn("👥 "+g);b.setOnClickListener(v->groupMembers(g));content.addView(b);}
        TextView a=btn("➕ إضافة طالب لمجموعة");a.setOnClickListener(v->addMember());content.addView(a);teacherBack();
    }
    private void groupMembers(String g){
        screen();header(g,"الطلاب داخل المجموعة");JSONArray a=arr("groups");boolean f=false;
        for(int i=0;i<a.length();i++)try{JSONObject x=a.getJSONObject(i);if(g.equals(x.optString("group"))){f=true;content.addView(txt("👤 "+x.optString("name"),17,fg()));}}catch(Exception ignored){}
        if(!f)content.addView(txt("لا يوجد طلاب مضافون.",17,fg()));TextView b=btn("رجوع");b.setOnClickListener(v->groups());content.addView(b);
    }
    private void addMember(){
        LinearLayout box=new LinearLayout(this);box.setOrientation(LinearLayout.VERTICAL);EditText n=inp("اسم الطالب"),g=inp("اسم المجموعة");box.addView(n);box.addView(g);
        new AlertDialog.Builder(this).setTitle("إضافة طالب").setView(box).setPositiveButton("حفظ",(d,w)->{try{JSONArray a=arr("groups");JSONObject x=new JSONObject();x.put("name",n.getText().toString().trim());x.put("group",g.getText().toString().trim());a.put(x);save("groups",a);groups();}catch(Exception ignored){}}).setNegativeButton("إلغاء",null).show();
    }
    private void updatesManager(){
        screen();header("الإعلانات والتحديثات","رسالة تظهر للطلاب");
        EditText e=inp("اكتب الإعلان");content.addView(e);TextView b=btn("📢 نشر");b.setOnClickListener(v->{sp.edit().putString("update",e.getText().toString().trim()).apply();toast("تم نشر التحديث");});content.addView(b);
        String u=sp.getString("update","");if(!u.isEmpty())content.addView(txt("آخر تحديث:\n"+u,17,fg()));teacherBack();
    }
    private void teacherSettings(){
        screen();header("إعدادات المدرس","إدارة النسخة الحالية");
        content.addView(txt("كود الدخول التجريبي: 1234\nسيتم استبداله بنظام Firebase الآمن.",17,fg()));
        TextView r=btn("♻️ إعادة بيانات التطبيق");r.setOnClickListener(v->new AlertDialog.Builder(this).setTitle("تأكيد").setMessage("حذف كل البيانات المحلية؟").setPositiveButton("حذف",(d,w)->{sp.edit().clear().apply();darkMode=true;showWelcome();}).setNegativeButton("إلغاء",null).show());content.addView(r);teacherBack();
    }
    private void teacherBack(){TextView b=btn("رجوع للوحة المدرس");b.setOnClickListener(v->teacherHome());content.addView(b);}

    private void startExam(JSONObject e){
        JSONArray q=e.optJSONArray("questions");
        if(q==null||q.length()==0){toast("الامتحان لا يحتوي على أسئلة");return;}

        examActive=true;
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
        );

        screen();header(e.optString("title"),"أجب ثم اضغط تسليم");
        TextView tm=txt("الوقت",20,GOLD);content.addView(tm);
        RadioButton[][] rb=new RadioButton[q.length()][4];
        String[] L={"أ","ب","ج","د"};

        for(int i=0;i<q.length();i++)try{
            JSONObject x=q.getJSONObject(i);
            content.addView(txt((i+1)+") "+x.optString("q"),18,fg()));
            RadioGroup rg=new RadioGroup(this);
            String[] v={x.optString("a"),x.optString("b"),x.optString("c"),x.optString("d")};
            for(int j=0;j<4;j++){
                RadioButton r=new RadioButton(this);
                r.setText(L[j]+"  "+v[j]);
                r.setTextSize(16);
                r.setTextColor(fg());
                rg.addView(r);
                rb[i][j]=r;
            }
            content.addView(rg);
        }catch(Exception ignored){}

        TextView subm=btn("✅ تسليم الامتحان");
        subm.setOnClickListener(v->{
            new AlertDialog.Builder(this)
                    .setTitle("تأكيد التسليم")
                    .setMessage("هل أنت متأكد من تسليم الامتحان؟ لن تتمكن من العودة إليه بعد التسليم.")
                    .setPositiveButton("تسليم",(d,w)->submit(e,rb,L))
                    .setNegativeButton("إلغاء",null)
                    .show();
        });
        content.addView(subm);

        int mins=Math.max(1,e.optInt("duration",30));
        timer=new CountDownTimer(mins*60000L,1000){
            public void onTick(long m){
                tm.setText(String.format(Locale.getDefault(),
                        "الوقت المتبقي: %02d:%02d",(m/1000)/60,(m/1000)%60));
            }
            public void onFinish(){
                toast("انتهى الوقت وسيتم تسليم الامتحان تلقائيًا.");
                submit(e,rb,L);
            }
        }.start();
    }
    private void submit(JSONObject e,RadioButton[][] rb,String[] L){
        if(!examActive)return;
        examActive=false;
        if(timer!=null){timer.cancel();timer=null;}

        int correct=0,answered=0,total=rb.length;
        StringBuilder bad=new StringBuilder();

        for(int i=0;i<rb.length;i++){
            String sel="";
            for(int j=0;j<4;j++){
                if(rb[i][j].isChecked()){
                    sel=L[j];
                    answered++;
                    break;
                }
            }
            try{
                String right=e.getJSONArray("questions").getJSONObject(i).optString("ans");
                if(right.equals(sel))correct++;
                else if(!sel.isEmpty()){
                    bad.append("السؤال ").append(i+1)
                       .append(": إجابتك ").append(sel)
                       .append(" | الصحيحة ").append(right).append("\n");
                }
            }catch(Exception ignored){}
        }

        try{
            JSONArray a=arr("results");
            JSONObject r=new JSONObject();
            r.put("student",student);
            r.put("exam",e.optString("title"));
            r.put("code",activeCode);
            r.put("correct",correct);
            r.put("answered",answered);
            r.put("total",total);
            r.put("mistakes",bad.toString());
            a.put(r);
            save("results",a);
        }catch(Exception ignored){}

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        result(e,correct,answered,total,bad.toString());
    }

    private void result(JSONObject e,int c,int an,int total,String bad){
        screen();header("نتيجة الامتحان","أحسنت يا "+student);
        content.addView(txt("الدرجة: "+c+" / "+total+"\nتمت الإجابة: "+an+"\nبدون إجابة: "+(total-an),25,GOLD));
        content.addView(txt(bad.isEmpty()?"🎉 ممتاز! لا توجد إجابات خاطئة.":"❌ أسئلة تحتاج مراجعة:\n"+bad,17,fg()));
        TextView h=btn("العودة للرئيسية");h.setOnClickListener(v->studentHome());content.addView(h);
        TextView r=btn("امتحاناتي");r.setOnClickListener(v->myExams());content.addView(r);
    }

    @Override public void onBackPressed(){
        if(examActive){
            toast("أثناء الامتحان لا يمكن الرجوع. استخدم زر التسليم عند الانتهاء.");
            return;
        }
        super.onBackPressed();
    }

    private int integer(String s,int d){try{return Integer.parseInt(s.trim());}catch(Exception e){return d;}}
    private JSONArray arr(String k){try{return new JSONArray(sp.getString(k,"[]"));}catch(Exception e){return new JSONArray();}}
    private void save(String k,JSONArray a){sp.edit().putString(k,a.toString()).apply();}
    private boolean isCodeUsed(String key){
        JSONArray a=arr("used_codes");
        for(int i=0;i<a.length();i++){
            if(key.equals(a.optString(i)))return true;
        }
        return false;
    }

    private void markCodeUsed(String key){
        JSONArray a=arr("used_codes");
        if(!isCodeUsed(key)){
            a.put(key);
            save("used_codes",a);
        }
    }

    private JSONObject findExam(String id){JSONArray a=arr("exams");for(int i=0;i<a.length();i++)try{JSONObject x=a.getJSONObject(i);if(id.equals(x.optString("id")))return x;}catch(Exception ignored){}return null;}
    private JSONObject findByCode(String code){JSONArray c=arr("codes"),e=arr("exams");for(int i=0;i<c.length();i++)try{JSONObject x=c.getJSONObject(i);if(code.equalsIgnoreCase(x.optString("code"))){String n=x.optString("exam");for(int j=0;j<e.length();j++){JSONObject z=e.getJSONObject(j);if(n.equals(z.optString("title")))return z;}}}catch(Exception ignored){}return null;}
    private void updateExam(JSONObject e){JSONArray a=arr("exams");for(int i=0;i<a.length();i++)try{if(e.optString("id").equals(a.getJSONObject(i).optString("id"))){a.put(i,e);save("exams",a);return;}}catch(Exception ignored){}}
    private void deleteExam(String id){JSONArray a=arr("exams");for(int i=0;i<a.length();i++)try{if(id.equals(a.getJSONObject(i).optString("id"))){a.remove(i);break;}}catch(Exception ignored){}save("exams",a);}
}
