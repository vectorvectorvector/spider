package com.spider.dao;

import com.spider.model.Comment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentDao {

    Comment selectCommentById(@Param("commentId") int commentId);

    Comment selectCommentByChannelName(@Param("channelName") String channelName);

    int insertComment(@Param("comment") Comment comment);

    List<Comment> selectAllComment();

    Comment selectCommentByComment(@Param("comment") Comment comment);
}
