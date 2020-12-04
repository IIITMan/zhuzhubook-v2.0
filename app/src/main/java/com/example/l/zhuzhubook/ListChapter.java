package com.example.l.zhuzhubook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;

public class ListChapter extends AppCompatActivity {
    private String id;
    private String bookname;
    private String lastchapterid;
    private String lastchapter;
    private String updatetime;
    private String imgurl;
    //
    private ListView lview_list;
    private ListView lview_list_search;
    private EditText edit_search;
    private Button btn_reverse;
    private Button btn_sc;
    private TextView txt_loading;
    //
    private boolean isReverse = false;
    private boolean isSC = false;
    private HttpClass httpClass = new HttpClass();
    private java.util.List<AllClass.SearchList> searchLists = new ArrayList();
    private java.util.List<AllClass.SearchList> searchLists_01 = new ArrayList();
    private AllAdapter.ListAdapter listAdapter = null;
    private AllAdapter.ListAdapter listAdapter_01 = null;
    //
    private SQLDBHelper helper;
    private SQLiteDatabase dbw;
    private SQLiteDatabase dbr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        //region
        final Bundle bundle = this.getIntent().getExtras();
        id = bundle.getString("id");
        bookname = bundle.getString("bookname");
        lastchapterid = bundle.getString("lastchapterid");
        lastchapter = bundle.getString("lastchapter");
        updatetime = bundle.getString("updatetime");
        imgurl = bundle.getString("imgurl");
        //endregion
        edit_search = (EditText) findViewById(R.id.edit_search);
        btn_reverse = (Button) findViewById(R.id.btn_reverse);
        btn_sc = (Button) findViewById(R.id.btn_sc);
        //
        lview_list = (ListView) findViewById(R.id.lview_list);
        lview_list_search = (ListView) findViewById(R.id.lview_list_search);
        listAdapter = new AllAdapter.ListAdapter(this, searchLists);
        lview_list.setAdapter(listAdapter);
        listAdapter_01 = new AllAdapter.ListAdapter(this, searchLists_01);
        lview_list_search.setAdapter(listAdapter_01);
        txt_loading=findViewById(R.id.txt_loading);
        lview_list.setEmptyView(txt_loading);
        //
        new Thread(runnable).start();
        //
        btn_reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Collections.reverse(searchLists);
                    listAdapter.notifyDataSetChanged();
                    Collections.reverse(searchLists_01);
                    listAdapter_01.notifyDataSetChanged();
                    if (isReverse) {
                        btn_reverse.setText("倒序");
                        isReverse = false;
                    } else {
                        btn_reverse.setText("正序");
                        isReverse = true;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        //
        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                handler1.sendMessage(handler1.obtainMessage(1, ""));
            }
        });
        //
        lview_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    Intent intent = new Intent();
                    intent.setClass(ListChapter.this, Chapter.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("id", ListChapter.this.id);
                    bundle1.putString("chapterid", searchLists.get(i).chapterId);
                    intent.putExtras(bundle1);
                    startActivity(intent);
                } catch (Exception ex) {
                    Log.v("error", ex.getMessage());
                }
            }
        });
        lview_list_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    Intent intent = new Intent();
                    intent.setClass(ListChapter.this, Chapter.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("id", ListChapter.this.id);
                    bundle1.putString("chapterid", searchLists_01.get(i).chapterId);
                    intent.putExtras(bundle1);
                    startActivity(intent);
                } catch (Exception ex) {
                    Log.v("error", ex.getMessage());
                }
            }
        });
        btn_sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSC) {
                    helper.execSql("delete from books where bookid='" + id+"'", dbw);
                    Toast.makeText(ListChapter.this, "取消收藏成功。", Toast.LENGTH_SHORT).show();
                    btn_sc.setText("收藏");
                    isSC = false;
                } else {
                    AllClass.Books books = new AllClass.Books(0, id, bookname, "0", "无", lastchapterid, lastchapter, updatetime, null,null);

                    new Thread(runnableA).start();

                    helper.InsertBooksData(dbw, books);
                    Toast.makeText(ListChapter.this, "收藏成功。", Toast.LENGTH_SHORT).show();
                    btn_sc.setText("取消收藏");
                    isSC = true;
                }
            }
        });

    }
    Runnable runnableA = new Runnable() {
        @Override
        public void run() {
            try {
                String saveUrl= httpClass.SaveImage(imgurl);
                helper.execSql("update books set imgurl='"+saveUrl+"' where bookid='"+id+"'",dbw);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };
    private void readBooks() {
        try {
            AllClass.Books books = new AllClass.Books(0, "", "", "", "", "", "", "", "",null);
            books = helper.SelectBooksData("bookid='" + id+"'", dbr);
            if (books.id>0) {
                btn_sc.setText("取消收藏");
                isSC = true;
            } else {
                btn_sc.setText("收藏");
                isSC = false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                Document docHtml = Jsoup.connect("https://www.duoben.net/" + id).get();
                Elements eleList=docHtml.body().select("div.listmain a");
                if(eleList.size()>0){
                    for(int i=0;i<eleList.size();i++){
                        String id = eleList.get(i).attr("href");
                        String name = eleList.get(i).text();
                        searchLists.add(new AllClass.SearchList(ListChapter.this.id, "", "", name, "", id, ""));
                    }
                }else {
                    handler.sendMessage(handler.obtainMessage(2, ""));
                    return;
                }
                handler.sendMessage(handler.obtainMessage(1, ""));
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
            switch (me.what) {
                case 1:
                    try {
                        listAdapter.notifyDataSetChanged();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;
                case 2:
                    Toast.makeText(ListChapter.this, "作者懒的是一章也没更新啊！", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    @SuppressLint("HandlerLeak")
    private Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message me) {
            super.handleMessage(me);
            try {
                String search = edit_search.getText().toString();
                if (search.length() > 0) {
                    lview_list.setVisibility(View.GONE);
                    lview_list_search.setVisibility(View.VISIBLE);
                    searchLists_01.clear();
                    for (int i = 0; i < searchLists.size(); i++) {
                        if (searchLists.get(i).lastChapter.indexOf(search) > -1) {
                            searchLists_01.add(searchLists.get(i));
                        }
                    }
                    listAdapter_01.notifyDataSetChanged();
                } else {
                    lview_list_search.setVisibility(View.GONE);
                    lview_list.setVisibility(View.VISIBLE);
                    searchLists_01.clear();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        helper = SQLDBHelper.getInstance(this, 0);
        dbw = helper.getWritableDatabase();
        dbr = helper.getReadableDatabase();
        readBooks();
    }

    @Override
    protected void onPause() {
        super.onPause();
        helper.close();
        dbw.close();
        dbr.close();
    }
}
