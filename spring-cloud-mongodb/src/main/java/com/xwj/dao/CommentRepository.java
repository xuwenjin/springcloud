package com.xwj.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.xwj.po.Comment;

public interface CommentRepository extends MongoRepository<Comment, String> {

	/**
	 * findByXXX后面的XXX，必须是Comment类中的字段
	 */
	Page<Comment> findByUserId(String userId, Pageable pageable);

}
