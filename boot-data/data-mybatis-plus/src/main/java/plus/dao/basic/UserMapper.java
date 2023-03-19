package plus.dao.basic;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import plus.entity.User;

@Repository //持久化层声明，用于注入装配检索
public interface UserMapper extends BaseMapper<User> { }