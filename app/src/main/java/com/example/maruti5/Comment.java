package com.example.maruti5;

public class Comment {
    String CommentBody;
    long CommentedAt;
    String CommentedBy;


    public Comment() {
    }

    public String getCommentBody() {
        return CommentBody;
    }

    public void setCommentBody(String commentBody) {
        CommentBody = commentBody;
    }

    public long getCommentedAt() {
        return CommentedAt;
    }

    public void setCommentedAt(long commentedAt) {
        CommentedAt = commentedAt;
    }

    public String getCommentedBy() {
        return CommentedBy;
    }

    public void setCommentedBy(String commentedBy) {
        CommentedBy = commentedBy;
    }
}
