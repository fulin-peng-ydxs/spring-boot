import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import plus.Application;
import plus.entity.Product;
import plus.entity.User;
import plus.service.ProductService;
import plus.service.UserService;

import java.util.List;

@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class DynamicTest {

    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;



    @Test
    public void test(){
        List<Product> products = productService.list(null);
        System.out.println(products);
        List<User> users = userService.list(null);
        System.out.println(users);
    }
}
