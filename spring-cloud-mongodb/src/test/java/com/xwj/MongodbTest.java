package com.xwj;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import com.xwj.po.Comment;
import com.xwj.po.SubComment;
import com.xwj.service.CommentService;

/**
 * 测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MongodbTest {

	@Autowired
	private CommentService commentService;

	/**
	 * 测试新增
	 */
	@Test
	public void testSaveComment() {
		Comment comment = new Comment();
		comment.setUserId("100101");
		comment.setContent("测试添加的数据2");
		comment.setCreatedatetime(LocalDateTime.now());
		comment.setNickname("凯撒大帝2");
		comment.setLikenum(0);
		
		comment.setSubComment(new SubComment("zhangsan", 13));
		
		
		List<SubComment> list = new ArrayList<>();
		list.add(new SubComment("zhangsan1", 10));
		list.add(new SubComment("zhangsan2", 12));
		comment.setCommList(list);

		commentService.saveComment(comment);
	}

	/**
	 * 测试批量新增
	 */
	@Test
	public void testSaveCommentList() {
		List<Comment> commentList = new ArrayList<>();

		Comment comment = new Comment();
		comment.setId("1");
		comment.setUserId("1001");
		comment.setContent("测试添加的数据");
		comment.setCreatedatetime(LocalDateTime.now());
		comment.setNickname("凯撒大帝");
		comment.setLikenum(0);
		commentList.add(comment);

		Comment comment2 = new Comment();
		comment2.setId("2");
		comment2.setUserId("1002");
		comment2.setContent("我要喝开水");
		comment2.setCreatedatetime(LocalDateTime.now());
		comment2.setNickname("火云邪神");
		comment2.setLikenum(1);
		commentList.add(comment2);

		Comment comment3 = new Comment();
		comment3.setId("3");
		comment3.setUserId("1002");
		comment3.setContent("我很爱测试数据");
		comment3.setCreatedatetime(LocalDateTime.now());
		comment3.setNickname("蓝猫");
		comment3.setLikenum(2);
		commentList.add(comment3);

		commentService.saveCommentList(commentList);
	}

	/**
	 * 测试修改
	 */
	@Test
	public void testUpdateComment() {
		// 这样修改后，会覆盖原文档，即comment文档里就只有id、userId、content字段了
		Comment comment = new Comment();
		comment.setId("6080eb381307b72e5850b307");
		comment.setUserId("1001");
		comment.setContent("修改后的数据");
		commentService.updateComment(comment);
	}

	/**
	 * 测试修改某一个字段值
	 */
	@Test
	public void testUpdateCommentLikenum() {
		// 这样修改后，不会覆盖原文档，即只修改了likenum字段值
		commentService.incCommentLikenum("6080eb381307b72e5850b307");
	}

	/**
	 * 测试查询列表
	 */
	@Test
	public void testFindCommentList() {
		List<Comment> commentList = commentService.findCommentList();
		System.out.println(commentList);
	}

	/**
	 * 测试通过id查询
	 */
	@Test
	public void testFindCommentById() {
		Comment commentById = commentService.findCommentById("2");
		System.out.println(commentById);
	}

	/**
	 * 分页查询
	 */
	@Test
	public void testFindCommentListByUserId() {
		Page<Comment> page = commentService.findCommentListByUserId("100101", 1, 2);
		System.out.println(page.getTotalElements());
		System.out.println(page.getContent());
	}

	/**
	 * 测试通过id删除
	 */
	@Test
	public void testDeleteCommentById() {
		commentService.deleteCommentById("1");
	}

	/**
	 * 测试删除
	 */
	@Test
	public void testDeleteComment() {
		commentService.deleteAllComment();
	}

}
