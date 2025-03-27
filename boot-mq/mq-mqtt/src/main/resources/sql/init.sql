-- 智能灯泡设备表
CREATE TABLE `tb_lamp` (
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `deviceId` varchar(50) DEFAULT NULL,
                           `status` int DEFAULT NULL COMMENT '1：上线  0：下线',
                           `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
                           `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 智能灯泡设备状态表
CREATE TABLE `tb_lamp_status` (
                                  `id` int NOT NULL AUTO_INCREMENT,
                                  `deviceId` varchar(50) DEFAULT NULL,
                                  `status` int DEFAULT NULL COMMENT '0: 关灯   1：开灯',
                                  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;