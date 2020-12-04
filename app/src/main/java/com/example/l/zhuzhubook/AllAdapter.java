package com.example.l.zhuzhubook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.l.zhuzhubook.SwipeMenuListView.BaseSwipListAdapter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class AllAdapter {

    //region搜索列表适配器
    public static class SearchAdapter extends BaseAdapter {
        private Context mcontext;
        private List<AllClass.SearchList> mlist;
        public SearchAdapter(Context context,List<AllClass.SearchList> list){
            mcontext=context;
            mlist=list;
        }
        public  int getCount(){
            return mlist.size();
        }
        public Object getItem(int arg0){
            return mlist.get(arg0);
        }
        public long getItemId(int arg0){
            return arg0;
        }
        public View getView(final int position, View convertView, ViewGroup parent){
            ViewHolder holder;
            if(convertView==null){
                holder=new ViewHolder();
                convertView=LayoutInflater.from(mcontext).inflate(R.layout.item_search,null);
                holder.txt1=convertView.findViewById(R.id.txt_bookname);
                holder.txt2=convertView.findViewById(R.id.txt_author);
                convertView.setTag(holder);
            }else {
                holder=(ViewHolder)convertView.getTag();
            }
            AllClass.SearchList str=mlist.get(position);
            holder.txt1.setText(str.name);
            holder.txt2.setText(str.author);
            return convertView;
        }
        public  final  class ViewHolder{
            public TextView txt1;
            public TextView txt2;
        }
    }
    //endregion
    //region章节列表适配器
    public static class ListAdapter extends BaseAdapter {
        private Context mcontext;
        private List<AllClass.SearchList> mlist;
        public ListAdapter(Context context,List<AllClass.SearchList> list){
            mcontext=context;
            mlist=list;
        }
        public  int getCount(){
            return mlist.size();
        }
        public Object getItem(int arg0){
            return mlist.get(arg0);
        }
        public long getItemId(int arg0){
            return arg0;
        }
        public View getView(final int position, View convertView, ViewGroup parent){
            ViewHolder holder;
            if(convertView==null){
                holder=new ViewHolder();
                convertView=LayoutInflater.from(mcontext).inflate(R.layout.item_list,null);
                holder.txt1=convertView.findViewById(R.id.txt_bookname);
                convertView.setTag(holder);
            }else {
                holder=(ViewHolder)convertView.getTag();
            }
            AllClass.SearchList str=mlist.get(position);
            holder.txt1.setText(str.lastChapter);
            return convertView;
        }
        public  final  class ViewHolder{
            public TextView txt1;
        }
    }
    //endregion
    //region书架适配器
    public static class BooksAdapter extends BaseAdapter {
        private Context mcontext;
        private List<AllClass.Books> mlist;
        public BooksAdapter(Context context,List<AllClass.Books> list){
            mcontext=context;
            mlist=list;
        }
        public  int getCount(){
            return mlist.size();
        }
        public Object getItem(int arg0){
            return mlist.get(arg0);
        }
        public long getItemId(int arg0){
            return arg0;
        }
        public View getView(final int position, View convertView, ViewGroup parent){
            ViewHolder holder;
            if(convertView==null){
                holder=new ViewHolder();
                convertView=LayoutInflater.from(mcontext).inflate(R.layout.item_books,null);
                holder.txt1=convertView.findViewById(R.id.txt_bookname);
                holder.txt2=convertView.findViewById(R.id.txt_lookchapter);
                holder.txt3=convertView.findViewById(R.id.txt_lastchapter);
                holder.txt4=convertView.findViewById(R.id.txt_updatetime);
                holder.txt5=convertView.findViewById(R.id.img_tt);
                convertView.setTag(holder);
            }else {
                holder=(ViewHolder)convertView.getTag();
            }
            AllClass.Books str=mlist.get(position);
            holder.txt1.setText(str.bookname);
            holder.txt2.setText("最后观看章节："+str.lookchapter);
            holder.txt3.setText("最新章节："+str.lastchapter);
            holder.txt4.setText("更新日期："+str.updatetime);
            if(str.imgurl!=null&&str.imgurl!=""){
                holder.txt5.setImageURI(Uri.fromFile(new File(str.imgurl)));
            }
            return convertView;
        }
        public  final  class ViewHolder{
            public TextView txt1;
            public TextView txt2;
            public TextView txt3;
            public TextView txt4;
            public ImageView txt5;
        }
    }
    //endregion
    //region分页
    public static class PageAdapter extends PagerAdapter{
        private Context mcontext;
        private List<View> listView;
        int viewCount = 0;
        public PageAdapter(Context context,List<View> list){
            viewCount=list.size();
            mcontext=context;
            listView=list;
        }
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return listView.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position,Object object) {
            // TODO Auto-generated method stub
            container.removeView(listView.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            container.addView(listView.get(position));
            return listView.get(position);
        }

    };
    //endregion
    //region可以左滑的书架适配器
    public static class AppAdapter extends BaseSwipListAdapter {
        private Context mcontext;
        private List<AllClass.Books> mlist;
        public AppAdapter(Context context, List<AllClass.Books> list){
            mcontext=context;
            mlist=list;
        }
        @Override
        public int getCount() {
            return mlist.size();
        }

        @Override
        public AllClass.Books getItem(int position) {
            return mlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            AppAdapter.ViewHolder holder;
            if(convertView==null){
                holder=new ViewHolder();
                convertView=LayoutInflater.from(mcontext).inflate(R.layout.item_books,null);
                holder.txt1=convertView.findViewById(R.id.txt_bookname);
                holder.txt2=convertView.findViewById(R.id.txt_lookchapter);
                holder.txt3=convertView.findViewById(R.id.txt_lastchapter);
                holder.txt4=convertView.findViewById(R.id.txt_updatetime);
                holder.txt5=convertView.findViewById(R.id.img_tt);
                convertView.setTag(holder);
            }else {
                holder=(AppAdapter.ViewHolder)convertView.getTag();
            }
            AllClass.Books str=mlist.get(position);
            holder.txt1.setText(str.bookname);
            holder.txt2.setText("最后观看章节："+str.lookchapter);
            holder.txt3.setText("最新章节："+str.lastchapter);
            holder.txt4.setText("更新日期："+str.updatetime);
            if(str.imgurl!=null&&str.imgurl!=""){
                holder.txt5.setImageURI(Uri.fromFile(new File(str.imgurl)));
            }
            return convertView;
        }
        public  final  class ViewHolder{
            public TextView txt1;
            public TextView txt2;
            public TextView txt3;
            public TextView txt4;
            public ImageView txt5;
        }

        //这里我们可以根据列表项的位置来设置某项是否允许侧滑
        //(此处我们设置的是当下标为偶数项的时候不允许侧滑)
        @Override
        public boolean getSwipEnableByPosition(int position) {
//            if(position % 2 == 0){
//                return false;
//            }
            return true;
        }
    }
    //endregion
}
