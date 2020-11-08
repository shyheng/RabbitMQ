package shy.transaction;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Receive {
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
            //开启事务
            //
            channel.txSelect();
            channel.basicConsume("shyQueue",true,"",new DefaultConsumer(channel){

                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body);
                    System.out.println("消费者----"+message);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
