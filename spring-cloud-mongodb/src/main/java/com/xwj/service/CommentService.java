package com.xwj.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.xwj.dao.CommentRepository;
import com.xwj.po.Comment;

@Service
public class CommentService {

	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private MongoTemplate mongoTemplate;

	/**
	 * 保存一个评论
	 */
	public void saveComment(Comment comment) {
		// 如果需要自定义主键，可以在这里指定主键；
		// 如果不指定主键，MongoDB会自动生成主键。如：ObjectId("6080e46b1307b72f8c59c445")
		commentRepository.save(comment);
	}

	/**
	 * 批量保存
	 */
	public void saveCommentList(List<Comment> commentList) {
		commentRepository.insert(commentList);
	}

	/**
	 * 更新评论
	 * 
	 * insert和save区别：
	 * 1、使用save函数里，如果原来的对象不存在，那他们都可以向collection里插入数据，如果已经存在，save会调用update更新里面的记录，而insert则会报错(DuplicateKeyException)
	 * 2、insert可以一次性插入一个列表，而不用遍历，效率高， save则需要遍历列表，一个个插入。
	 */
	public void updateComment(Comment comment) {
		// commentRepository.insert(comment);

		// 这种方式更新，会覆盖原文档(字段可能增加或减少)
		commentRepository.save(comment);
	}

	/**
	 * 根据id删除评论
	 */
	public void deleteCommentById(String id) {
		commentRepository.deleteById(id);
	}

	/**
	 * 删除所有
	 */
	public void deleteAllComment() {
		commentRepository.deleteAll();
	}

	/**
	 * 查询所有评论
	 */
	public List<Comment> findCommentList() {
		return commentRepository.findAll();
	}

	/**
	 * 根据id查询评论
	 */
	public Comment findCommentById(String id) {
		Optional<Comment> optional = commentRepository.findById(id);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	public Page<Comment> findCommentListByUserId(String userId, int page, int size) {
		return commentRepository.findByUserId(userId, PageRequest.of(page - 1, size));
	}

	public void incCommentLikenum(String id) {
		// 1、查询条件
		Query query = Query.query(Criteria.where("_id").is(id));

		// 2、更新条件
		Update update = new Update();
		update.inc("likenum", 1);

		// 3、执行更新操作
		mongoTemplate.updateFirst(query, update, Comment.class);
	}

}
