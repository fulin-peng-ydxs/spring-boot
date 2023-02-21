package shiro.entity;



import lombok.Data;

import java.io.Serializable;

@Data
public class Perms implements Serializable {
    private String id;
    private String name;
    private String url;
}
