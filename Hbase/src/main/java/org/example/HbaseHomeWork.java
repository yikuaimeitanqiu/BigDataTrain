package org.example;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * description： <br>
 * 创建Hbase表
 *
 * @author GuWeiSheng
 * @Date 2022/3/18 9:53
 * @history <br>
 * author       desc     date
 * GuWeiSheng   created  2022/3/18
 */
public class HbaseHomeWork
{
    public static void main(String[] args) throws IOException
    {
        //设置hbase模块
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", "10.8.6.175");
        configuration.set("hbase.zookeeper.property.clientPort", "2181");
        configuration.set("hbase.master", "10.8.6.175:16000");

        //建立hbase连接
        Connection connection = ConnectionFactory.createConnection(configuration);

        // 创建指定命名空间
        HBaseAdmin adminNameSpace = (HBaseAdmin) connection.getAdmin();
        // 查看所有命名空间
        NameSpace.listNameSpaces(adminNameSpace);
        // 赋值要创建的命名空间名称
        String appointNameSpace = "guweisheng";
        // 判断命名空间不存在,则创建
        if (!NameSpace.appointNameSpace(adminNameSpace, appointNameSpace))
        {
            NameSpace.createNameSpace(adminNameSpace, appointNameSpace);
        }
        // 再次打印所有命令空间列表
        NameSpace.listNameSpaces(adminNameSpace);


        // 创建表
        Admin admin = connection.getAdmin();
        // 指定表名
        String appointTable = "student";
        // 加上前面创建好的命名空间,组合成,创建到指定命名空间下的表
        TableName tableName = TableName.valueOf(appointNameSpace + ":" + appointTable);
        // 定义列族名
        String[] columnFamilys = {"info", "score"};

        // 建表:如果已经创建好表,则表示结果,否则创建表
        if (admin.tableExists(tableName))
        {
            System.out.println(appointNameSpace + ":" + appointTable + " table already exists or create success");
        }
        else
        {
            //表建造者
            TableDescriptorBuilder tableDescriptorBuilder = TableDescriptorBuilder.newBuilder(tableName);
            //列族建造者
            ColumnFamilyDescriptorBuilder columnFamilyDescriptorBuilder;
            //表描述器
            TableDescriptor tableDescriptor;
            ColumnFamilyDescriptor columnFamilyDescriptor;

            for (String columnFamily : columnFamilys)
            {
                columnFamilyDescriptorBuilder = ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(columnFamily));
                columnFamilyDescriptorBuilder.setMaxVersions(3);
                columnFamilyDescriptor = columnFamilyDescriptorBuilder.build();
                tableDescriptorBuilder.setColumnFamily(columnFamilyDescriptor);
            }
            tableDescriptor = tableDescriptorBuilder.build();
            admin.createTable(tableDescriptor);

            System.out.println(appointNameSpace + ":" + appointTable + " table create successful");
        }


        //
        // //插入数据
        // Put put = new Put(Bytes.toBytes(rowKey));
        // put.addColumn(Bytes.toBytes(colFamily), Bytes.toBytes("uid"), Bytes.toBytes("001"));
        // put.addColumn(Bytes.toBytes(colFamily), Bytes.toBytes("name"), Bytes.toBytes("guweisheng"));
        // connection.getTable(tableName).put(put);
        // System.out.println("Data insert success");
        //
        // // 查看数据
        // Get get = new Get(Bytes.toBytes(rowKey));
        // if (!get.isCheckExistenceOnly())
        // {
        //     Result result = connection.getTable(tableName).get(get);
        //     for (Cell cell : result.rawCells())
        //     {
        //         String colName = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
        //         String value = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
        //         System.out.println("Data get success,colName:" + colName + " value:" + value);
        //     }
        // }
        //


    }
}
