package org.example;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;

/**
 * description： <br>
 * 创建Hbase表,创建数据
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
        configuration.set("hbase.zookeeper.quorum", "127.0.0.1");
        configuration.set("hbase.zookeeper.property.clientPort", "2181");
        configuration.set("hbase.master", "127.0.0.1:16000");

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


        // 插入数据
        Data.putDataRow(tableName, "Tom", "info", "student_id", "20210000000001");
        Data.putDataRow(tableName, "Tom", "info", "class", "1");
        Data.putDataRow(tableName, "Tom", "score", "understanding", "75");
        Data.putDataRow(tableName, "Tom", "score", "programming", "82");

        Data.putDataRow(tableName, "Jerry", "info", "student_id", "20210000000002");
        Data.putDataRow(tableName, "Jerry", "info", "class", "1");
        Data.putDataRow(tableName, "Jerry", "score", "understanding", "85");
        Data.putDataRow(tableName, "Jerry", "score", "programming", "67");

        Data.putDataRow(tableName, "Jack", "info", "student_id", "20210000000003");
        Data.putDataRow(tableName, "Jack", "info", "class", "2");
        Data.putDataRow(tableName, "Jack", "score", "understanding", "80");
        Data.putDataRow(tableName, "Jack", "score", "programming", "80");

        Data.putDataRow(tableName, "Rose", "info", "student_id", "20210000000004");
        Data.putDataRow(tableName, "Rose", "info", "class", "2");
        Data.putDataRow(tableName, "Rose", "score", "understanding", "60");
        Data.putDataRow(tableName, "Rose", "score", "programming", "61");

        Data.putDataRow(tableName, "guweisheng", "info", "student_id", "G20220735020033");


        // 全表扫描,获取表名
        Table table = connection.getTable(tableName);
        // 实例scan
        Scan scan = new Scan();
        // 获取全表扫描结果
        ResultScanner scanner = table.getScanner(scan);
        // 定义空值
        Result result = null;

        // 迭代数据,直至结果不为空
        while ((result = scanner.next()) != null)
        {
            // 打印获取到的所有单元格
            List<Cell> cells = result.listCells();
            for (Cell cell : cells)
            {
                System.out.println(Bytes.toString(CellUtil.cloneRow(cell))
                        + " => " + Bytes.toString(CellUtil.cloneFamily(cell))
                        + " {" + Bytes.toString(CellUtil.cloneQualifier(cell))
                        + ":" + Bytes.toString(CellUtil.cloneValue(cell)) + "}");
            }
        }

        // 查看指定行数据
        // Get get = new Get(Bytes.toBytes("Rose"));
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


        // 关闭连接
        connection.close();
    }
}
