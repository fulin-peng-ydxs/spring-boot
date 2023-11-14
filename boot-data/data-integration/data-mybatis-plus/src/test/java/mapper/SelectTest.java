package mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import plus.dao.UserMapper;
import plus.entity.User;
import java.util.List;

/**
 * 查询使用
 * author: pengshuaifeng
 * 2023/11/6
 */
public class SelectTest extends BasicTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void list(){
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }

    @Test
    public void page(){
        //分页+总数
        Page<User> page = new Page<>(1, 10);
        userMapper.selectPage(page,new QueryWrapper<>());
        //分页+不计算总数
        Page<User> userPageNoCount = new Page<>(2, 10, false);
        userMapper.selectPage(userPageNoCount,new QueryWrapper<>());
    }

}
