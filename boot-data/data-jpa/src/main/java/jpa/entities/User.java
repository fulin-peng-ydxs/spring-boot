package jpa.entities;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity  //标记持久化映射
public class User {
    @Id
    private Integer id;
    private String userName;
    private Integer userAge;
}
