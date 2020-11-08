package shy.rabbit.transaction;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Sean {
    public static void main(String[] args) {
        ConnectionFactory factory= new ConnectionFactory();
        factory.setHost("192.168.237.128");//指定ip
        factory.setPort(5672);//指定端口
        factory.setUsername("root");//指定账号
        factory.setPassword("123456");//指定密码
        Connection connection = null;//定义连接
        Channel channel = null;//定义通道
        try {
            connection = factory.newConnection();//获取连接
            channel=connection.createChannel();//获取通道
            channel.queueDeclare("shyQueue",true,false,false,null);

            channel.exchangeDeclare("zphExchange","direct",true);

            channel.queueBind("shyQueue","zphExchange","transactionRoutingKey");
            String message="事务成功的测试消息";
            //启动 事务
//            必须有添加才知晓
            channel.txSelect();
            channel.basicPublish("zhpExchange","transactionRoutingKey",null,message.getBytes("utf-8"));
//            提交事务
//            不然不会上传
            channel.txCommit();
            System.out.println("消息发送成功");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }finally {
            if (channel!=null){
                try {
                    //回滚
//                    释放资源
                    channel.txRollback();

                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
            if (connection!=null){
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
