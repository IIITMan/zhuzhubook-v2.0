package com.example.l.zhuzhubook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Search extends AppCompatActivity {
    private Button btn_search;
    private EditText edit_search;
    private ListView lview_search;
    private TextView txt_loading;
    //
    private HttpClass httpClass = new HttpClass();
    private List<AllClass.SearchList> searchLists = new ArrayList();
    private AllAdapter.SearchAdapter searchAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        //
        edit_search = (EditText) findViewById(R.id.edit_search);
        btn_search = (Button) findViewById(R.id.btn_search);
        lview_search = (ListView) findViewById(R.id.lview_search);
        //
        searchAdapter = new AllAdapter.SearchAdapter(this, searchLists);
        lview_search.setAdapter(searchAdapter);
        txt_loading=findViewById(R.id.txt_loading);
        lview_search.setEmptyView(txt_loading);
        //
        lview_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    Intent intent = new Intent();
                    intent.setClass(Search.this, ListChapter.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("id", searchLists.get(i).id);
                    bundle1.putString("bookname", searchLists.get(i).name);
                    bundle1.putString("lastchapterid", searchLists.get(i).chapterId);
                    bundle1.putString("lastchapter", searchLists.get(i).lastChapter);
                    bundle1.putString("updatetime", searchLists.get(i).lastTime);
                    bundle1.putString("imgurl", searchLists.get(i).imgurl);
                    intent.putExtras(bundle1);
                    startActivity(intent);
                    Search.this.finish();
                } catch (Exception ex) {
                    Log.v("error", ex.getMessage());
                }
            }
        });
        //
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(runnable).start();
            }
        });
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                searchLists.clear();
                String bookName = edit_search.getText().toString();
                Document docHtml = Jsoup.connect("https://www.duoben.net/s.php?ie=gbk&q=" + bookName).get();
                Element eleBody = docHtml.body();
                Elements eleDiv = eleBody.getElementsByClass("bookbox");
                if (eleDiv.size() > 0) {
                    for (int i=0;i<eleDiv.size();i++) {
                        Elements book = eleDiv.get(i).getElementsByClass("bookname");
                        String id = book.get(0).select("a").attr("href");
                        String name = book.get(0).select("a").text();
                        String author = eleDiv.get(i).getElementsByClass("author").text().replace("作者：","");
                        String lastChapter = eleDiv.get(i).getElementsByClass("update").text();
                        String LastChapterId = "";//jsonObject2.getString("LastChapterId");
                        String updateTime = "";//jsonObject2.getString("UpdateTime");
                        String imgurl ="https://www.duoben.net/"+eleDiv.select("img").attr("src");
                        searchLists.add(new AllClass.SearchList(id, name, author, lastChapter, updateTime, LastChapterId,imgurl));
                    }
                } else {
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
                        searchAdapter.notifyDataSetChanged();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;
                case 2:
                    Toast.makeText(Search.this, "啥都没搜到诶！", Toast.LENGTH_SHORT).show();
                        break;
                default:
                    break;
            }

        }
    };
}
