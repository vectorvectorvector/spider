package com.spider.service.impl;

import com.spider.dao.CommentDao;
import com.spider.dao.NewsDao;
import com.spider.model.Comment;
import com.spider.model.News;
import com.spider.service.CommentService;
import com.spider.service.NewsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("commentService")
@Transactional(rollbackFor = Exception.class)
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentDao commentDao;

    public Comment selectCommentById(int commentId) {
        return commentDao.selectCommentById(commentId);
    }

    public List<Comment> getAllComment() {
        return commentDao.selectAllComment();
    }

    public int insertComment(Comment comment) {
        return commentDao.insertComment(comment);
    }

    @Override
    public Comment selectCommentByComment(Comment comment) {
        return commentDao.selectCommentByComment(comment);
    }
}
