package com.atguigu.job;

import com.atguigu.constant.RedisConstant;
import com.atguigu.utli.QiniuUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Arrays;
import java.util.Set;

public class ClearImgJob {
    private Logger logger = Logger.getLogger(ClearImgJob.class);

    @Autowired
    JedisPool jedisPool;

    public void clearImg() {
        Jedis jedis = jedisPool.getResource();
        Set<String> pics = jedis.sdiff(RedisConstant.SETMEAL_PIC_RESOURCES,
                                       RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        for (String pic : pics) {
            QiniuUtils.deleteFileFromQiniu(pic);
            jedis.srem(RedisConstant.SETMEAL_PIC_RESOURCES, pic);
        }
        if (pics.toArray().length > 0) {
            logger.info("清理了七牛云垃圾图片: " + Arrays.toString(pics.toArray()));
        }
    }
}
