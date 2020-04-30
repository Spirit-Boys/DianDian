package cn.edu.scujcc.diandian;

import java.util.Date;

public class Comment {
    /**
     * 评论作者
     */
    private String author;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 评论日期时间
     */
    private Date dt;
    /**
     * 评论点赞数量
     */
    private int star = 0;
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Date getDt() {
        return dt;
    }
    public void setDt(Date dt) {
        this.dt = dt;
    }
    public int getStar() {
        return star;
    }
    public void setStar(int star) {
        this.star = star;
    }

    @Override
    public String toString() {
        return "Comment [author=" + author + ", content=" + content + ", dt=" + dt + ", star=" + star + "]";
    }

}
