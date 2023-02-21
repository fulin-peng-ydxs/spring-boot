package shiro.entity;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Role implements Serializable {
    private String id;
    private String name;
    //定义权限的集合
    private List<Perms> perms;
}
