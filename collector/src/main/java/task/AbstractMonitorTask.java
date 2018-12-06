package task;/*
*@ClassName:task.AbstractMonitorTask
 @Description:TODO
 @Author:
 @Date:2018/12/3 14:17 
 @Version:v1.0
*/

import org.apache.commons.configuration.Configuration;

public abstract class AbstractMonitorTask implements MonitorTask {

    private String name;
    private String type;
    private long period;

    public void init(Configuration conf) {
        setName(conf.getString("name"));
        setType(conf.getString("type"));
        setPeriod(conf.getLong("period"));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }


    public void clean() {

    }
}
