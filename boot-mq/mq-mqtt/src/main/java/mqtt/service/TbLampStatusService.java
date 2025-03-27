package  mqtt.service;

import  mqtt.domain.TbLampStatus;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author hly
* @description 针对表【tb_lamp_status】的数据库操作Service
* @createDate 2024-10-27 21:00:41
*/
public interface TbLampStatusService extends IService<TbLampStatus> {

    public abstract void saveDeviceStatus(String payload);
}
