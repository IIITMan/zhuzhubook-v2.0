package com.example.l.zhuzhubook;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.l.zhuzhubook.SwipeMenuListView.BaseSwipListAdapter;
import com.example.l.zhuzhubook.SwipeMenuListView.SwipeMenu;
import com.example.l.zhuzhubook.SwipeMenuListView.SwipeMenuCreator;
import com.example.l.zhuzhubook.SwipeMenuListView.SwipeMenuItem;
import com.example.l.zhuzhubook.SwipeMenuListView.SwipeMenuListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Main extends AppCompatActivity {
    private FloatingActionButton btn_search;
//    private ListView lview_books;
    private SwipeRefreshLayout srlayout;
    private TextView txt_loading;
    //
    private List<AllClass.Books> list = new ArrayList<>();
//    private AllAdapter.BooksAdapter booksAdapter = null;
    //
    private SQLDBHelper helper;
    private SQLiteDatabase dbr;
    private SQLiteDatabase dbw;
    //
    private HttpClass httpClass = new HttpClass();
    //
    private boolean isNew = false;
    private int index = 0;
    //
    private AllAdapter.AppAdapter mAdapter;
    private SwipeMenuListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //region
//        lview_books = (ListView) findViewById(R.id.lview_books);
        btn_search = (FloatingActionButton) findViewById(R.id.btn_search);
        srlayout = (SwipeRefreshLayout) findViewById(R.id.srlayout);
        txt_loading = findViewById(R.id.txt_loading);
//        lview_books.setEmptyView(txt_loading);
        //endregion
        //region
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Main.this, Search.class);
                startActivity(intent);
            }
        });

//        lview_books.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String lookchapterid = list.get(i).lookchapterid;
//                try {
//                    if (lookchapterid.equals("0")) {
//                        Intent intent = new Intent();
//                        intent.setClass(Main.this, ListChapter.class);
//                        Bundle bundle1 = new Bundle();
//                        bundle1.putString("id", list.get(i).bookid);
//                        bundle1.putString("bookname", list.get(i).bookname);
//                        bundle1.putString("lastchapterid", list.get(i).lastchapterid);
//                        bundle1.putString("lastchapter", list.get(i).lastchapter);
//                        bundle1.putString("updatetime", list.get(i).updatetime);
//                        bundle1.putString("imgurl", list.get(i).imgurl);
//                        intent.putExtras(bundle1);
//                        startActivity(intent);
//                    } else {
//                        Intent intent = new Intent();
//                        intent.setClass(Main.this, Chapter.class);
//                        Bundle bundle1 = new Bundle();
//                        bundle1.putString("id", list.get(i).bookid);
//                        bundle1.putString("chapterid", list.get(i).lookchapterid);
//                        intent.putExtras(bundle1);
//                        startActivity(intent);
//                    }
//                } catch (Exception ex) {
//                    Log.v("error", "");
//                }
//
//            }
//        });
        srlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(runnable).start();
//                Toast.makeText(Main.this, "刷新等待(*^_^*)", Toast.LENGTH_SHORT).show();
            }
        });
        //endregion
        //region 可以左滑的的的
        mListView = (SwipeMenuListView) findViewById(R.id.mListView);
//        mAdapter = new AllAdapter.AppAdapter(this,list);
//        mListView.setAdapter(mAdapter);
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // 创建“置顶”项
                SwipeMenuItem topItem = new SwipeMenuItem(getApplicationContext());
                topItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,0xCE)));
                topItem.setWidth(dp2px(90));
                topItem.setTitle("置顶");
                topItem.setTitleSize(20);
                topItem.setTitleColor(Color.WHITE);
                // 将创建的菜单项添加进菜单中
                menu.addMenuItem(topItem);

                // 创建“删除”项
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,0x3F, 0x25)));
                deleteItem.setWidth(dp2px(90));
//                deleteItem.setIcon(R.drawable.ic_delete);
                deleteItem.setTitle("删除");
                deleteItem.setTitleColor(Color.WHITE);
                deleteItem.setTitleSize(20);;
                // 将创建的菜单项添加进菜单中
                menu.addMenuItem(deleteItem);
            }
        };
        mListView.setMenuCreator(creator);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                String lookchapterid = list.get(i).lookchapterid;
                try {
                    if (lookchapterid.equals("0")) {
                        Intent intent = new Intent();
                        intent.setClass(Main.this, ListChapter.class);
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("id", list.get(i).bookid);
                        bundle1.putString("bookname", list.get(i).bookname);
                        bundle1.putString("lastchapterid", list.get(i).lastchapterid);
                        bundle1.putString("lastchapter", list.get(i).lastchapter);
                        bundle1.putString("updatetime", list.get(i).updatetime);
                        bundle1.putString("imgurl", list.get(i).imgurl);
                        intent.putExtras(bundle1);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent();
                        intent.setClass(Main.this, Chapter.class);
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("id", list.get(i).bookid);
                        bundle1.putString("chapterid", list.get(i).lookchapterid);
                        intent.putExtras(bundle1);
                        startActivity(intent);
                    }
                } catch (Exception ex) {
                    Log.v("error", "");
                }
            }
        });
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                final AllClass.Books item = list.get(position);
                final int po=position;
                switch (index) {
                    case 0:
                        // open
                        handler.sendMessage(handler.obtainMessage(2, position));
                        break;
                    case 1:
                        // delete
                        AlertDialog.Builder builder  = new AlertDialog.Builder(Main.this);
                        builder.setTitle("书籍管理" ) ;
                        builder.setMessage("是否删除？" ) ;
                        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                helper.execSql("delete from books where bookid='" + item.bookid+"'", dbw);
                                Toast.makeText(Main.this, "删除成功！", Toast.LENGTH_SHORT).show();
                                list.remove(po);
                                //通知监听者数据集发生改变，更新ListView界面
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                        builder.setNegativeButton("否", null);
                        builder.show();
                        break;
                }
                // true：其他已打开的列表项的菜单状态将保持原样，不会受到其他列表项的影响而自动收回
                // false:已打开的列表项的菜单将自动收回
                return false;
            }
        });
        //endregion
    }
    //region
    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            isNew = false;
            if (list.size() <= 0) {
                handler.sendMessage(handler.obtainMessage(1, ""));
                return;
            }
            for (int i = 0; i < list.size(); i++) {
                index = i;
                new Thread(runnableGetNew).start();
            }
        }
    };
    Runnable runnableGetNew = new Runnable() {
        @Override
        public void run() {
            try {
                int rIndex = index;
                Document docHtml = Jsoup.connect("https://www.duoben.net/" + list.get(rIndex).bookid).timeout(3000).get();
                if (docHtml == null) {
                    handler.sendMessage(handler.obtainMessage(1, ""));
                    return;
                }
                Elements eleMain = docHtml.select("div.blockcontent").get(0).select("p");
                String lastChapter = eleMain.get(5).select("a").text();
                String LastChapterId = eleMain.get(5).select("a").attr("href");
                String updateTime = eleMain.get(4).text().replace("最后更新：", "");
                Log.v("aaaa",list.get(rIndex).lastchapterid);
                Log.v("bbbb",LastChapterId);
                if (!list.get(rIndex).lastchapterid.equals(LastChapterId)) {
                    list.get(rIndex).lastchapterid = LastChapterId;
                    list.get(rIndex).lastchapter = lastChapter + "(有更新)";
                    list.get(rIndex).updatetime = updateTime;
                    helper.UpdateBooksData(dbw, list.get(rIndex));
                    isNew = true;
                }
                handler.sendMessage(handler.obtainMessage(1, ""));
            } catch (IOException e) {
                e.printStackTrace();
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
                        srlayout.setRefreshing(false);
                        if (isNew) {
                            Toast.makeText(Main.this, "您追的小说有更新呦(*^_^*)", Toast.LENGTH_SHORT).show();
                        } else {
//                            Toast.makeText(Main.this, "您追的小说还没有更新呢╮(╯▽╰)╭", Toast.LENGTH_SHORT).show();
                        }
//                        booksAdapter.notifyDataSetChanged();
                        mAdapter.notifyDataSetChanged();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;
                case 2:
                    int position= (int) me.obj;
                    int id=2;
                    for(int i=0;i<list.size();i++){
                        if(list.get(i).bookid==list.get(position).bookid){
                            list.get(i).sort="1";
                            helper.execSql("update books set sort='1' where bookid='" + list.get(i).bookid+"'", dbw);
                        }else {
                            list.get(i).sort=id+"";
                            helper.execSql("update books set sort='"+id+"' where bookid='" + list.get(i).bookid+"'", dbw);
                            id++;
                        }
                    }
                    BooksSorts(list);
                    mAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        helper = SQLDBHelper.getInstance(this, 0);
        dbr = helper.getReadableDatabase();
        dbw = helper.getWritableDatabase();
        list.clear();
        list = helper.SelectBooksListData("", dbr);
//        booksAdapter = new AllAdapter.BooksAdapter(this, list);
//        lview_books.setAdapter(booksAdapter);
        if (!hasPermission(this)) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        mAdapter=new AllAdapter.AppAdapter(this,list);
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(txt_loading);
//        new Thread(runnable).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        helper.close();
        dbr.close();
    }

    public static boolean hasPermission(Context context) {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    //endregion

    private int dp2px(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,getResources().getDisplayMetrics());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        return super.onOptionsItemSelected(item);
    }
    public static void BooksSorts(List<AllClass.Books> books){
        Collections.sort(books,new Comparator<AllClass.Books>() {
            @Override
            public int compare(AllClass.Books o1, AllClass.Books o2) {
                // TODO Auto-generated method stub
                if(o1.sort.compareTo(o2.sort)>0){
                    return 1;
                }
                return -1;
            }
        });
    }
}
