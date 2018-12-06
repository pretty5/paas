package util;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.IOException;
import java.util.Properties;

/*
*@ClassName:util.KafkaUtil
 @Description:TODO
 @Author:
 @Date:2018/12/3 15:19 
 @Version:v1.0
*/
public class KafkaUtil {
    private static Producer<String, String> producer = null;

    static {
        Properties properties = new Properties();
        try {
            properties.load(KafkaUtil.class.getClassLoader().getResourceAsStream("kafka.properties"));
            producer = new KafkaProducer<String, String>(properties);
        } catch (IOException e) {
            e.printStackTrace();
        }
       /* properties.put("bootstrap.servers", "192.168.85.7:9092");
        properties.put("acks", "all");
        properties.put("retries", 0);
        properties.put("batch.size", 16384);
        properties.put("linger.ms", 1);
        properties.put("buffer.memory", 33554432);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
*/

    }

    public static void send(String content, String topic) {
        producer.send(new ProducerRecord<String, String>(topic, "", content));
    }


}
