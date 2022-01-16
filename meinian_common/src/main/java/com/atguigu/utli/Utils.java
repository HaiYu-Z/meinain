package com.atguigu.utli;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    public static List<Map<String, Integer>> ergodicForIdsArray(Integer id, Integer[] ids) {
        if (id != null && ids != null && ids.length > 0) {
            List<Map<String, Integer>> list = new ArrayList<>();
            for (Integer i : ids) {
                Map<String, Integer> map = new HashMap<>();
                map.put("id", id);
                map.put("ids", i);
                list.add(map);
            }
            return list;
        }
        return null;
    }
}
