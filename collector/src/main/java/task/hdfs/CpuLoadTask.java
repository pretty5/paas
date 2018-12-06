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
*@ClassName:task.hdfs.CpuLoadTask
 @Description:TODO
 @Author:
 @Date:2018/12/3 14:43 
 @Version:v1.0
*/
public class CpuLoadTask extends AbstractMonitorTask {
    private static final Logger logger=LoggerFactory.getLogger(CpuLoadTask.class);
    //java urlConnection
    //httpclients
    public void run() {
        String url="http://192.168.203.37:50070/jmx?qry=java.lang:type=OperatingSystem";
        HttpGet httpGet = new HttpGet(url);
        try {
            CloseableHttpResponse response = new SystemDefaultHttpClient().execute(httpGet);
            InputStream content = response.getEntity().getContent();
            String jsonstr = IOUtils.toString(content);
            logger.info("cpuloadtask:{}",jsonstr);
            //提取cpu负载
            JsonObject jsonObject = new JsonParser().parse(jsonstr).getAsJsonObject();
            double systemCpuLoad = jsonObject.getAsJsonArray("beans")
                    .get(0).getAsJsonObject().get("SystemCpuLoad")
                    .getAsDouble();
            logger.info("systemCpuLoad:{}",systemCpuLoad);

            Message message = new Message();
            message.setCollectTime(new Date().toLocaleString());
            message.setHostIp("192.168.203.37");
            message.setMetricValue(systemCpuLoad+"");
            message.setCluster("hdfs");
            message.setCompType("hdfs");
            message.setConfigType("namenode");
           message.setMetricCode("cpuload");
            logger.info("message:{}",new Gson().toJson(message));

            //发送出去
            KafkaUtil.send(new Gson().toJson(message),"ETE_HDFS_0371");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
