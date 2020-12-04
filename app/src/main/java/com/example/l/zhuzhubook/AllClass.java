package com.example.l.zhuzhubook;

public class AllClass {
    //搜索列表
    public static class SearchList{
        //id
        public String id;
        //标题-书名
        public String name;
        //作者
        public String author;
        //最新一章
        public String lastChapter;
        //最后更新时间
        public String lastTime;
        //章节id
        public String chapterId;
        //图片
        public String imgurl;
        public SearchList(String id,String name,String author,String lastChapter,String lastTime,String chapterId,String imgurl){
            this.id=id;
            this.name=name;
            this.author=author;
            this.lastChapter=lastChapter;
            this.lastTime=lastTime;
            this.chapterId=chapterId;
            this.imgurl=imgurl;
        }
    }
    //搜索列表
    public static class Books{
        //id
        public int id;
        public String bookid;
        public String bookname;
        public String lookchapterid;
        public String lookchapter;
        public String lastchapterid;
        public String lastchapter;
        public String updatetime;
        public String imgurl;
        public String sort;
        public Books(int id,String bookid,String bookname,String lookchapterid,String lookchapter,String lastchapterid,String lastchapter,String updatetime,String imgurl,String sort){
            this.id=id;
            this.bookid=bookid;
            this.bookname=bookname;
            this.lookchapterid=lookchapterid;
            this.lookchapter=lookchapter;
            this.lastchapterid=lastchapterid;
            this.lastchapter=lastchapter;
            this.updatetime=updatetime;
            this.imgurl=imgurl;
            this.sort=sort;
        }
    }
}
