package com.dlpower.p2p.utils;

import java.util.HashMap;
import java.util.Map;


public class Result{

    /**
     * 成功的json
     * @return
     */
    public static Map<Object, Object> success() {
        Map<Object,Object> maps = new HashMap<Object, Object>();
        maps.put("code",1);
        maps.put("message","");
        maps.put("success",true);
        return maps;
    }

    /**
     * 返回失败json
     * @param msg
     * @return
     */
    public static Map<Object, Object> error(String msg) {
        Map<Object,Object> maps = new HashMap<>();
        maps.put("code",-1);
        maps.put("message",msg);
        maps.put("success",false);
        return maps;
    }

}
