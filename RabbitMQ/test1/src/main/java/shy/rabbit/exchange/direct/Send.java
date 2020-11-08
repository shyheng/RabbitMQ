package shy.rabbit.exchange.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Send {
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
            channel.queueDeclare("myDirectQueue",true,false,false,null);
            /**
             * 声明一个交换机
             * 参数1 交换机的名称
             * 参数 2 集合的类型，direct,fanout,topic,headers
             * 参数 3 是否为持久化交换机
             *
             * 注意
             *    声明交换机时如果这个交换机应该存在，发放弃声明
             *    这个代码有无，必须要确保交换机被声明
             *
             */
            channel.exchangeDeclare("directExchange","direct",true);

            /**
             * 绑定交换机
             * 参数1 队列名称
             * 参数2 交换机名称
             * 参数3 消息的RoutingKey（就是BindingKey）
             * 注意
             *    在进行队列和交换机绑定是必须确保交换机和队列已经成功声明
             *
             */
            channel.queueBind("myDirectQueue","directExchange","directRoutingKey");
            String message="direct的测试消息";
            /**
             * 发送消息到指定的队列
             * 参数1 交换机 名称
             * 参数2 消息的RoutingKey和某一个队列与计划绑定的RoutingKey一致
             *      这个消息就会发送到指定队列中
             *
             *      注意
             *        发送消息是必须确保交换机已经创建，并且已经正确的绑定到某个队列中
             */
            channel.basicPublish("directExchange","directRoutingKey",null,message.getBytes("utf-8"));
            System.out.println("消息发送成功");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }finally {
            if (channel!=null){
                try {
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
