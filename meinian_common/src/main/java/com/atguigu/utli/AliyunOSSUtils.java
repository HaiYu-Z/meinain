package com.atguigu.utli;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * 
 * @author HaiYu
 */
public class AliyunOSSUtils {

    private static final String END_POINT = "https://oss-cn-guangzhou.aliyuncs.com";
    private static final String ACCESS_KEY_ID = "LTAI5tK25gMiMgwhfTSRNQ17";
    private static final String ACCESS_KEY_SECRET = "yo4iEZCml7MYNhiAeth05t3GLKhVUO";
    private static final String BUCKET_NAME = "meinian-0306";

    public static void upload(byte[] bytes, String fileName) {

        // 填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称。
        String objectName = "exampledir/exampleobject.txt";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(END_POINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);

        try {
            // 填写Byte数组。
            byte[] content = "Hello OSS".getBytes();
            // 创建PutObject请求。
            ossClient.putObject(BUCKET_NAME, fileName, new ByteArrayInputStream(bytes));
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    public static void delete(String fileName) {

        String objectName = "exampleobject.txt";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(END_POINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);

        // 删除文件或目录。如果要删除目录，目录必须为空。
        ossClient.deleteObject(BUCKET_NAME, fileName);

        // 关闭OSSClient。
        ossClient.shutdown();
    }

    @Test
    public void test() throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream("classpath:1.jpg");
        System.out.println(fileInputStream);
    }
}
