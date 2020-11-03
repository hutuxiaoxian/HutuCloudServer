package com.zhishouwei.common.model.service.impl;

import com.zhishouwei.common.model.service.RedissonConfigService;
import com.zhishouwei.common.redisson.RedissonConfig;
import lombok.extern.slf4j.Slf4j;
import org.redisson.config.Config;
import org.springframework.util.StringUtils;

/**
 * 哨兵集群部署Redis连接配置
 *
 * @className: SentineConfigImpl
 * @package: com.bjblackhole.common.service.impl
 * @author: 曾维录
 * @date: 2020/7/7 13:01
 */
@Slf4j
public class SentineConfigImpl implements RedissonConfigService {



    @Override
    public Config createRedissonConfig(RedissonConfig redissonConfig) {
        Config config = new Config();
        try {
            String address = redissonConfig.getAddress();
            String password = redissonConfig.getPassword();
            int database = redissonConfig.getDatabase();
            String[] addrTokens = address.split(",");
            String sentinelAliasName = addrTokens[0];
            //设置redis配置文件sentinel.conf配置的sentinel别名
            config.useSentinelServers().setMasterName(sentinelAliasName);
            config.useSentinelServers().setDatabase(database);
            if (!StringUtils.isEmpty(password)) {
                config.useSentinelServers().setPassword(password);
            }
            //设置sentinel节点的服务IP和端口
            for (int i = 1; i < addrTokens.length; i++) {
                config.useSentinelServers().addSentinelAddress(REDIS_CONNECTION_PREFIX + addrTokens[i]);
            }
            log.info("初始化[哨兵部署]方式Config,redisAddress:" + address);
        } catch (Exception e) {
            log.error("哨兵部署 Redisson init error", e);

        }
        return config;
    }
}
