package  mqtt.service;

import  mqtt.domain.TbLamp;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author hly
* @description 针对表【tb_lamp】的数据库操作Service
* @createDate 2024-10-27 21:00:41
*/
public interface TbLampService extends IService<TbLamp> {

    public abstract void updateLampOnlineStatus(String payload);
}
