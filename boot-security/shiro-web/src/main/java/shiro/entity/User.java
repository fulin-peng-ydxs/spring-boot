package shiro.entity;


import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
public class User implements Serializable {
    private String  id;
    private String username;
    private String password;
    private String salt;

    //定义角色集合
    private List<Role> roles;


}
