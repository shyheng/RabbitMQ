package shy;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Receive {

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost("192.168.237.128");//指定ip
        factory.setPort(5672);//指定端口
        factory.setUsername("root");//指定账号
        factory.setPassword("123456");//指定密码
        Connection connection = null;//定义连接
        Channel channel = null;//定义通道

        try {
            connection = factory.newConnection();//获取连接
            channel=connection.createChannel();//获取通道

            channel.queueDeclare("myQueue",true,false,false,null);

            /**
             * 接收消息
             *  参数 1 消息者需要监听的队列名，队列名不一致，接收不到消息
             *  参数 2 消息是否自动确认 true表示确认接收用户会自动将消息移除
             *  参数 3 消息接收者的标签，用于多个消费者同时监听一个队列，通常为null
             *  参数 4 为消费接收的回调方法这个方法中具体完成对消息的处理代码
             */
            channel.basicConsume("myQueue",true,"",new DefaultConsumer(channel){
                //信息具体接收和处理方法
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body,"utf-8");
                    System.out.println("消费者"+message);
                }
            });
            //不能关闭，关了无法接受和监听
            channel.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }
}
