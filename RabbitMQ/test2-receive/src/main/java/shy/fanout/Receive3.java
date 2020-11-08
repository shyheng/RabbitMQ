package shy.fanout;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Receive3 {
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

            /**
             *  fanout广播模式
             *   因此我们需要创建队列随机的名称
             *   没有参数的q
             */
//            String queueName = channel.queueDeclare().getQueue();
            channel.queueDeclare("zph",true,false,false,null);
            channel.exchangeDeclare("shy","fanout",true);
            channel.queueBind("zph","shy","");

            channel.basicConsume("zph",true,"",new DefaultConsumer(channel){
                /**
                 * 监听某个队列并取队列的数据
                 *
                 * @param consumerTag
                 * @param envelope
                 * @param properties
                 * @param body
                 * @throws IOException
                 */
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body);
                    System.out.println("消费者1----"+message);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
