package org.example;

import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;

import java.io.IOException;

/**
 * description： <br>
 *
 * @author GuWeiSheng
 * @Date 2022/3/18 23:43
 * @history <br>
 * author       desc     date
 * GuWeiSheng   created  2022/3/18
 */
public class NameSpace
{

    public static void createNameSpace(HBaseAdmin admin, String name) throws IOException
    {

        // namespace描述器
        NamespaceDescriptor namespaceDescriptor = NamespaceDescriptor.create(name).build();
        // 创建namespace
        admin.createNamespace(namespaceDescriptor);
    }


    // 查看namespace列表
    public static void listNameSpaces(HBaseAdmin admin) throws IOException
    {
        NamespaceDescriptor[] namespaceDescriptors = admin.listNamespaceDescriptors();
        for (NamespaceDescriptor namespace : namespaceDescriptors)
        {
            System.out.println(namespace.getName());
        }
    }

    // 查看指定的namespace命名空间是否存在,存在返回真
    public static boolean appointNameSpace(HBaseAdmin admin, String name) throws IOException
    {
        NamespaceDescriptor[] namespaceDescriptors = admin.listNamespaceDescriptors();
        boolean exists = false;
        for (NamespaceDescriptor namespace : namespaceDescriptors)
        {
            if (namespace.getName().equals(name))
            {
                System.out.println(name + " already exists");
                return true;
            }

        }
        return exists;
    }


    // 删除namespace
    public static void dropNameSpace(HBaseAdmin admin, String name) throws IOException
    {
        admin.deleteNamespace(name);
    }


}
