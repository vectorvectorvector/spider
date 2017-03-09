package com.spider.service;


import com.spider.model.Comment;
import com.spider.model.News;

import java.util.List;

public interface CommentService {

    List<Comment> getAllComment();

    Comment selectCommentById(int commentId);

    int insertComment(Comment comment);//向评论表中添加记录

    Comment selectCommentByComment(Comment comment);//查询记录是否已经存在
}
