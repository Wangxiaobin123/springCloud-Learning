package micro.spring.cloud.caffeine.entity;

/**
 * @Author: shengbin
 * @since: 2019/11/21 下午2:42
 */

public class DataObject {
    private final String data;

    private static int objectCounter = 0;

    public DataObject(String data) {
        this.data = data;
    }
    // standard constructors/getters

    public static DataObject get(String data) {
        objectCounter++;
        return new DataObject(data);
    }

    public String getData() {
        return data;
    }
}