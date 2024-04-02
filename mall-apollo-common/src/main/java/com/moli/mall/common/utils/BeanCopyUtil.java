package com.moli.mall.common.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author moli
 * @time 2024-03-30 10:46:57
 * @description 对象拷贝工具类
 */
public class BeanCopyUtil {

    /**
     * 拷贝对象
     *
     * @param source 源对象
     * @param target 目标对象
     * @return target -> bean
     */
    public static <T> T copyBean(Object source, Class<T> target) {
        if (Objects.isNull(target)) return null;
        try {
            T targetIns = target.newInstance();
            if (Objects.nonNull(source)) {
                BeanUtils.copyProperties(source, targetIns);
            }
            return targetIns;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("拷贝对象出错~~");
        }
    }

    /**
     * 从源集合中拷贝到新的集合中
     *
     * @param srcList 源集合
     * @param target  目标集合中的对象类型
     * @return List
     */
    public static <T> List<T> copyBeanList(List<?> srcList, Class<T> target) {
        List<T> ret = new ArrayList<>();
        if (CollectionUtils.isEmpty(srcList)) return ret;
        for (Object srcObj : srcList) {
            T targetIns = BeanCopyUtil.copyBean(srcObj, target);
            ret.add(targetIns);
        }
        return ret;
    }
}
