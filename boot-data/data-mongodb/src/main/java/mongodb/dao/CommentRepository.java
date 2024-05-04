package mongodb.dao;

import mongodb.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;


/**
 * 持久层接口
 * author: pengshuaifeng
 * 2023/10/22
 */
public interface CommentRepository extends MongoRepository<Comment,String> {

    //根据父id，查询子评论的分页列表
    Page<Comment> findByParentId(String parentId, Pageable pageable);

}