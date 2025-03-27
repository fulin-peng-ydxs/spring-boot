package  mqtt.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import mqtt.domain.TbLampStatus;
import mqtt.mapper.TbLampStatusMapper;
import mqtt.service.TbLampStatusService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
* @author hly
* @description 针对表【tb_lamp_status】的数据库操作Service实现
* @createDate 2024-10-27 21:00:41
*/
@Service
public class TbLampStatusServiceImpl extends ServiceImpl<TbLampStatusMapper, TbLampStatus>
    implements TbLampStatusService{

    @Override
    public void saveDeviceStatus(String payload) {
        Map<String , Object> map = JSON.parseObject(payload, Map.class);
        String deviceId = map.get("deviceId").toString();
        Integer status = Integer.parseInt(map.get("status").toString());
        TbLampStatus tbLampStatus = new TbLampStatus() ;
        tbLampStatus.setDeviceid(deviceId);
        tbLampStatus.setStatus(status);
        tbLampStatus.setCreateTime(new Date());
        this.save(tbLampStatus) ;
    }
}




