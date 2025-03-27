package  mqtt.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import  mqtt.domain.TbLamp;
import  mqtt.service.TbLampService;
import  mqtt.mapper.TbLampMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
* @author hly
* @description 针对表【tb_lamp】的数据库操作Service实现
* @createDate 2024-10-27 21:00:41
*/
@Service
public class TbLampServiceImpl extends ServiceImpl<TbLampMapper, TbLamp> implements TbLampService{

    @Override
    public void updateLampOnlineStatus(String payload) {
        Map<String , Object> map = JSON.parseObject(payload, Map.class);
        String deviceId = map.get("deviceId").toString();
        Integer status = Integer.parseInt(map.get("online").toString());

        // 根据设备的id查询设备信息
        LambdaQueryWrapper<TbLamp> lambdaQueryWrapper = new LambdaQueryWrapper<>() ;
        lambdaQueryWrapper.eq(TbLamp::getDeviceid , deviceId) ;
        TbLamp tbLamp = this.getOne(lambdaQueryWrapper);

        if(tbLamp == null) {
            tbLamp = new TbLamp() ;
            tbLamp.setDeviceid(deviceId);
            tbLamp.setStatus(status);
            tbLamp.setCreateTime(new Date());
            tbLamp.setUpdateTime(new Date());
            this.save(tbLamp) ;
        }else {
            tbLamp.setStatus(status);
            tbLamp.setUpdateTime(new Date());
            this.updateById(tbLamp) ;
        }

    }

}




