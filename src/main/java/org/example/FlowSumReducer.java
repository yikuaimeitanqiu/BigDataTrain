package org.example;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * description： <br>
 *
 * @author GuWeiSheng
 * @Date 2022/3/10 20:41
 * @history <br>
 * author       desc     date
 * GuWeiSheng   created  2022/3/10
 */
public class FlowSumReducer extends Reducer<Text, FlowBean, Text, FlowBean>
{
    FlowBean v = new FlowBean();

    @Override
    protected void reduce(Text key, Iterable<FlowBean> values, Context context) throws IOException, InterruptedException
    {
        long upFlowCount = 0;
        long downFlowCount = 0;

        for (FlowBean bean : values)
        {
            upFlowCount += bean.getUpFlow();
            downFlowCount += bean.getDownFlow();
        }
        v.set(upFlowCount, downFlowCount);
        context.write(key, v);
    }
}
