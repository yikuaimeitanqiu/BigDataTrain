package org.example;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;

/**
 * description： <br>
 *
 * @author GuWeiSheng
 * @Date 2022/3/19 10:47
 * @history <br>
 * author       desc     date
 * GuWeiSheng   created  2022/3/19
 */
public class Data
{

    public static List<Put> putDataRow(TableName tableName, String row, String columns, String cell, String value) throws IOException
    {
        //设置hbase模块
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", "emr-worker-2,emr-worker-1,emr-header-1");
        configuration.set("hbase.zookeeper.property.clientPort", "2181");
        configuration.set("hbase.master", "127.0.0.1:16000");

        //建立hbase连接
        Connection connection = ConnectionFactory.createConnection(configuration);

        Put put = new Put(Bytes.toBytes(row));
        put.addColumn(Bytes.toBytes(columns), Bytes.toBytes(cell), Bytes.toBytes(value));

        connection.getTable(tableName).put(put);
        return null;
    }
}
