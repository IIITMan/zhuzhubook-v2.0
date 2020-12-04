package com.example.l.zhuzhubook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

public class Chapter extends AppCompatActivity {

    private ViewPager viewPager;
    private List<View> viewList = new ArrayList<View>();
    private AllAdapter.PageAdapter pageAdapter;
    private int lastIndex;
    //
    private String id;
    private String chapterid;
    private String chapter;
    private String content;
    private String opid;//上一章
    //    private String nid;//下一章
    private List<String> ids = new ArrayList<>();//章节id
    //
    private HttpClass httpClass = new HttpClass();
    //
    View view1;
    View view2;
    View view3;
    //
    private AllClass.Books books;
    //
    private SQLDBHelper helper;
    private SQLiteDatabase dbw;
    private SQLiteDatabase dbr;
    //
    private LinearLayout layout_btn;
    private int ay;
    private Button btn_list;
    private boolean isShow = false;
    //背景及字体
    private Button btn_sizereduce;
    private Button btn_sizeadd;
    private ImageButton btn_bgc0;
    private ImageButton btn_bgc1;
    private ImageButton btn_bgc2;
    private ImageButton btn_bgc3;
    private Button btn_lineadd;
    private Button btn_lineduce;
    //
    private SharedPreferences sp;
    private float size = 20;
    private int color;
    private int txt_color;
    private int line = 1;
    private TextView NowTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chapter);
        SetSizeOrColor();
        //region
        final Bundle bundle = this.getIntent().getExtras();
        id = bundle.getString("id");
        chapterid = bundle.getString("chapterid");
        //endregion
        //region
        pageAdapter = new AllAdapter.PageAdapter(this, viewList);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        //
        layout_btn = (LinearLayout) findViewById(R.id.layout_btn);
        btn_list = (Button) findViewById(R.id.btn_list);
        //endregion
        //region
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
            }

            @Override
            public void onPageScrollStateChanged(int i) {
//                Log.v("",""+i);
                if (i == 1)
                    lastIndex = viewPager.getCurrentItem();
                if (i == 2) {
                    int index = viewPager.getCurrentItem();
                    if (lastIndex > index) {
//                        Log.v("huadong", "向左");
                        new Thread(runnableUp).start();
                    } else if (lastIndex < index) {
//                        Log.v("huadong", "向右");
                        new Thread(runnableDown).start();
                    }
                }
            }
        });
        new Thread(runnable).start();
        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Chapter.this, ListChapter.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("id", books.bookid);
                bundle1.putString("bookname", books.bookname);
                bundle1.putString("lastchapterid", books.lastchapterid);
                bundle1.putString("lastchapter", books.lastchapter);
                bundle1.putString("updatetime", books.updatetime);
                bundle1.putString("imgurl", books.imgurl);
                intent.putExtras(bundle1);
                startActivity(intent);
                Chapter.this.finish();
            }
        });
        //endregion
    }

    public void SetSizeOrColor() {
        btn_sizereduce = (Button) findViewById(R.id.btn_sizereduce);
        btn_sizeadd = (Button) findViewById(R.id.btn_sizeadd);
        btn_bgc0 = (ImageButton) findViewById(R.id.btn_bgc0);
        btn_bgc1 = (ImageButton) findViewById(R.id.btn_bgc1);
        btn_bgc2 = (ImageButton) findViewById(R.id.btn_bgc2);
        btn_bgc3 = (ImageButton) findViewById(R.id.btn_bgc3);
        btn_lineadd = (Button) findViewById(R.id.btn_lineadd);
        btn_lineduce = (Button) findViewById(R.id.btn_lineduce);
        sp = getSharedPreferences("SizeOrColor", MODE_PRIVATE);
        if (sp != null && sp.getString("size", "").length() > 0 && sp.getString("color", "").length() > 0 && sp.getString("line", "").length() > 0&& sp.getString("txt_color", "").length() > 0) {
            size = Float.valueOf(sp.getString("size", ""));
            color = Integer.parseInt(sp.getString("color", ""));
            txt_color = Integer.parseInt(sp.getString("txt_color", ""));
            line = Integer.parseInt(sp.getString("line", ""));
        }
        btn_sizereduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size--;
                NowTV.setTextSize(size);
                updateSP();
            }
        });
        btn_sizeadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size++;
                NowTV.setTextSize(size);
                updateSP();
            }
        });
        btn_bgc0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = Color.rgb(255, 238, 192);
                NowTV.setBackgroundColor(color);
                txt_color=Color.rgb(81,81,81);
                NowTV.setTextColor(txt_color);
                updateSP();
            }
        });
        btn_bgc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = Color.rgb(246, 235, 188);
                NowTV.setBackgroundColor(color);
                txt_color=Color.rgb(81,81,81);
                NowTV.setTextColor(txt_color);
                updateSP();
            }
        });
        btn_bgc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = Color.rgb(18, 150, 219);
                NowTV.setBackgroundColor(color);
                txt_color=Color.rgb(210,210,210);
                NowTV.setTextColor(txt_color);
                updateSP();
            }
        });
        btn_bgc3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = Color.rgb(81, 81, 81);
                NowTV.setBackgroundColor(color);
                txt_color=Color.rgb(210,210,210);
                NowTV.setTextColor(txt_color);
                updateSP();
            }
        });
        btn_lineadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                line++;
                NowTV.setLineSpacing(line, 1);
                updateSP();
            }
        });
        btn_lineduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                line--;
                NowTV.setLineSpacing(line, 1);
                updateSP();
            }
        });
    }

    public void updateSP() {
        sp.edit().putString("line", String.valueOf(line)).putString("color", String.valueOf(color)).putString("size", String.valueOf(size)).putString("txt_color", String.valueOf(txt_color)).commit();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                Document docHtml = Jsoup.connect("https://www.duoben.net/" + chapterid).get();
                Element eleBody = docHtml.body();
                chapter = eleBody.select("div.content h1").text();
                content = eleBody.select("div#content").text();
                content=content.substring(0,content.lastIndexOf("https"));
                Elements elePage = eleBody.select("div.page_chapter a");
                String pid = elePage.get(0).select("a").attr("href");
                pid = pid.indexOf("html") > -1 ? pid : "-1";
                opid = pid;
                String nid = elePage.get(2).select("a").attr("href");
                nid = nid.indexOf("html") > -1 ? nid : "-1";

                if (!pid.equals("-1")) {
                    view1 = addView("加载中...");
                    viewList.add(view1);
                    ids.add(pid);
                }
                view2 = addView("加载中...");
                viewList.add(view2);
                ids.add(chapterid);
                if (!nid.equals("-1")) {
                    view3 = addView("加载中...");
                    viewList.add(view3);
                    ids.add(nid);
                }
                pageAdapter.notifyDataSetChanged();

                handler.sendMessage(handler.obtainMessage(1, ""));
                writeBooks();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };
    Runnable runnableUp = new Runnable() {
        @Override
        public void run() {
            try {
                int i = viewPager.getCurrentItem();
                Document docHtml = Jsoup.connect("https://www.duoben.net/" + ids.get(i)).get();
                Element eleBody = docHtml.body();
                chapter = eleBody.select("div.content h1").text();
                content = eleBody.select("div#content").text();
                content=content.substring(0,content.lastIndexOf("https"));
                chapterid=ids.get(i);
                handler.sendMessage(handler.obtainMessage(1, ""));
                writeBooks();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message me) {
            super.handleMessage(me);
            try {
                AllAdapter.PageAdapter aadapter = (AllAdapter.PageAdapter) viewPager.getAdapter();
                if (aadapter == null) {
                    viewPager.setAdapter(pageAdapter);
                    if (!opid.equals("-1")) {
                        viewPager.setCurrentItem(1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
                View view = viewList.get(viewPager.getCurrentItem());
                TextView textView = (TextView) view.findViewById(R.id.txt_con);
                NowTV = textView;
                textView.setTextSize(size);
                textView.setBackgroundColor(color);
                textView.setTextColor(txt_color);
                textView.setLineSpacing(1, 1);
                textView.setLineSpacing(line, 1);
                textView.setText(chapter + "\n" + content);
                if (ids.size() > viewList.size()) {
                    viewList.add(addView("加载中..."));
                    pageAdapter.notifyDataSetChanged();
                }
                textView.setOnClickListener(new DoubleClickListener() {
                    @Override
                    public void onDoubleClick(View v) {
                        if (isShow) {
                            layout_btn.setVisibility(View.GONE);
                            isShow = false;
                        } else {
                            layout_btn.setVisibility(View.VISIBLE);
                            isShow = true;
                        }
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };
    @SuppressLint("HandlerLeak")
    private Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message me) {
            super.handleMessage(me);
            try {
                View view = viewList.get(viewPager.getCurrentItem());
                TextView textView = (TextView) view.findViewById(R.id.txt_con);
                textView.setText("没有更多章节了!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };
    Runnable runnableDown = new Runnable() {
        @Override
        public void run() {
            try {
                int i = viewPager.getCurrentItem();
                Document docHtml = Jsoup.connect("https://www.duoben.net/" + ids.get(i)).get();
                Element eleBody = docHtml.body();
                chapter = eleBody.select("div.content h1").text();
                content = eleBody.select("div#content").text();
                content=content.substring(0,content.lastIndexOf("https"));
                Elements elePage = eleBody.select("div.page_chapter a");
                String nid = elePage.get(2).select("a").attr("href");
                nid = nid.indexOf("html") > -1 ? nid : "-1";
                if (!ids.contains(nid) && !nid.equals("-1")) {
                    ids.add(nid);
                }
                chapterid=ids.get(i);
                handler.sendMessage(handler.obtainMessage(1, ""));
                writeBooks();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };

    public View addView(String content) {
        View view = LayoutInflater.from(this).inflate(R.layout.page_layout, null);
        TextView textView = (TextView) view.findViewById(R.id.txt_con);
        textView.setText(content);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        return view;
    }

    private void readBooks() {
        try {
            books = new AllClass.Books(0, "", "", "", "", "", "", "", "","");
            books = helper.SelectBooksData("bookid='" + id+"'", dbr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void writeBooks() {
        try {
            books.lookchapterid = chapterid;
            books.lookchapter = chapter;
            books.lastchapter = books.lastchapter.replace("(有更新)", "");
            helper.UpdateBooksData(dbw, books);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        helper = SQLDBHelper.getInstance(this, 0);
        dbw = helper.getWritableDatabase();
        dbr = helper.getReadableDatabase();
        //
//        new Thread(runnable).start();
        readBooks();
    }

    @Override
    protected void onPause() {
        super.onPause();
        helper.close();
        dbw.close();
    }

    public abstract static class DoubleClickListener implements View.OnClickListener {
        private static final long DOUBLE_TIME = 500;
        private static long lastClickTime = 0;

        @Override
        public void onClick(View v) {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - lastClickTime < DOUBLE_TIME) {
                onDoubleClick(v);
            }
            lastClickTime = currentTimeMillis;
        }

        public abstract void onDoubleClick(View v);
    }
}