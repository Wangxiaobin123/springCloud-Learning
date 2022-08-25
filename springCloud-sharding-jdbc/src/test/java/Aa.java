import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: wangshengbin
 * @date: 2020/7/29 下午3:16
 */
public class Aa {

    @Test
    public void testA(){
        List<Map<String,Object>> list = new ArrayList();

        Map<String,Object> map = new HashMap();
        map.put("entryName","modify/12");
        map.put("emotionValue",0.65);
        list.add(map);

        map = new HashMap();
        map.put("entryName","default");
        map.put("emotionValue",0.65);

        for(Map<String,Object> map1:list){
            System.out.println(map1.containsValue("modify/13"));
        }
    }
}
