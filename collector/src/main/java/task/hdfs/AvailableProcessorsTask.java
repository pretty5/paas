package task.hdfs;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.SystemDefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task.AbstractMonitorTask;
import task.Message;
import util.KafkaUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/*
*@ClassName:task.hdfs.AvailableProcessorsTask
 @Description:TODO
 @Author:
 @Date:2018/12/3 15:29 
 @Version:v1.0
*/
public class AvailableProcessorsTask extends AbstractMonitorTask {
    private static final Logger logger = LoggerFactory.getLogger(AvailableProcessorsTask.class);

    public void run() {
        String url = "?qry=java.lang:type=OperatingSystem";
        HttpGet httpGet = new HttpGet(url);
        try {
            CloseableHttpResponse response = new SystemDefaultHttpClient().execute(httpGet);
            InputStream content = response.getEntity().getContent();
            String jsonstr = IOUtils.toString(content);
            logger.info("AvailableProcessors:{}", jsonstr);
            //提取chttp://192.168.203.37:50070/jmxpu负载
            JsonObject jsonObject = new JsonParser().parse(jsonstr).getAsJsonObject();
            String availableProcessors = jsonObject.getAsJsonArray("beans")
                    .get(0).getAsJsonObject().get("AvailableProcessors")
                    .getAsString();
            logger.info("AvailableProcessors:{}", availableProcessors);

            Message message = new Message();
            message.setCollectTime(new Date().toLocaleString());
            message.setHostIp("192.168.203.37");
            message.setMetricValue(availableProcessors + "");
            message.setCluster("hdfs");
            message.setCompType("hdfs");
            message.setConfigType("namenode");
            message.setMetricCode("availableProcessors");

            logger.info("message:{}", new Gson().toJson(message));

            //发送出去
            KafkaUtil.send(new Gson().toJson(message), "ETE_HDFS_0371");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

