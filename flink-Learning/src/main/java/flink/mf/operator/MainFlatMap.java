package flink.mf.operator;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @author: wangshengbin
 * @date: 2020/12/12 下午10:31
 */
public class MainFlatMap {
    public static void main(String[] args) throws Exception {
        //1. 构建执行环境
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //2. 定义数据源（source），这里使用监听9000端口的socket消息，通常也可用来测试flink任务
        DataStream<String> socketTextStream = env.socketTextStream("localhost", 9001, "\n");
        DataStream<String> flatMapSocketTextStream = socketTextStream.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public void flatMap(String value, Collector<String> out) throws Exception {
                for (String v : value.split(" ")) {
                    out.collect(v);
                }
            }
        });
        flatMapSocketTextStream.print();
        env.execute();
    }
}
