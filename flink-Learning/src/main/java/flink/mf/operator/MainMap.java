package flink.mf.operator;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.shaded.curator.org.apache.curator.shaded.com.google.common.base.Joiner;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author: wangshengbin
 * @date: 2020/12/12 下午6:22
 */
public class MainMap {
    private static final Joiner ADD_JOINER = Joiner.on(":");

    public static void main(String[] args) throws Exception {
        //1. 构建执行环境
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //2. 定义数据源（source），这里使用监听9000端口的socket消息，通常也可用来测试flink任务
        DataStream<String> socketTextStream = env.socketTextStream("localhost", 9001, "\n");
        //3. map
        DataStream<String> mapSocketTextStream = socketTextStream.map(new MapFunction<String, String>() {
            @Override
            public String map(String value) throws Exception {
                return ADD_JOINER.join("message", value);
            }
        });
        //lambda表达
//        DataStream<String> mapSocketTextStream = socketTextStream
//                .map((MapFunction<String, String>) value -> ADD_JOINER.join("socket message", value));
        //4. 打印dataStream内容
        mapSocketTextStream.print();
        //5. 执行
        env.execute();

    }
}
