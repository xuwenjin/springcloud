package com.xwj.po;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Document("comment") // mongodb的注解，映射文档名称。默认类名小写
@CompoundIndex(def = "{'userId': 1, 'nickname': -1}") // 复合索引
public class Comment {

	// 主键标识，该属性的值会自动对应mongodb的主键字段"_id"，如果该属性名就叫“id”,则该注解可以省略，否则必须写
	// @Id
	private String id;// 主键

	// 该属性对应mongodb的字段的名字，如果一致，则无需该注解
	@Field("content")
	private String content;// 吐槽内容

	// 添加了一个单字段的索引
	@Indexed
	private String userId;// 发布人ID

	private String nickname;// 昵称

	private LocalDateTime createdatetime;// 评论的日期时间

	private Integer likenum;// 点赞数

}
