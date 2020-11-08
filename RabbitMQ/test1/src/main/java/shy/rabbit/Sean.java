package shy.rabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Sean {
    public static void main(String[] args) {
//        创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        /**
         * 配置rabbitMq信息
         */
        factory.setHost("192.168.237.128");//指定ip
        factory.setPort(5672);//指定端口
        factory.setUsername("root");//指定账号
        factory.setPassword("123456");//指定密码
        Connection connection = null;//定义连接
        Channel channel = null;//定义通道

        try {
            connection = factory.newConnection();//获取连接
            channel=connection.createChannel();//获取通道
            /*
            * 声明一个队列
            * 参数1 为 队列名取值任意
            * 参数2 为 是否持久化的队列
            * 参数 3 是否排外，如果排外则这个列队只允许一个消费者监听
            * 参数 4 是否为自动删除 如果为true则表示队列中没有消息，也没有消费连接时
            * 参数 5 队列的一些属性设置通常为null即可
            *
            *
            *注意
            *     1声明队列 ，这个队列名如果已经存在则放弃声明，如果不存在则会和创建新的
            *     2 队列名可以取任意值，但是要和消息接收是完全一致
            *     3 代码可以没有，但是必须有值
            * */
            channel.queueDeclare("myQueue",true,false,false,null);

            String message = "我的rabbit1";
            /**
             * 发送消息到Mq
             * 参数 1 为 交换机名称，这里空字符串表示，不使用交换机
             * 参数2 为队列名或RoutingKey，当指定了交换机名称后这个就是RoutingKey
             * 参数3 为消息属性信息 通常空即可
             * 参数4 为具体的消息数据的字节数组
             *
             */
            channel.basicPublish("","myQueue",null,message.getBytes("utf-8"));

            System.out.println("消息发送成功");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }finally {
            if (channel != null){
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null){
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
