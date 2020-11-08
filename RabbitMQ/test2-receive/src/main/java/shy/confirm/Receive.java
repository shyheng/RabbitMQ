package shy.confirm;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
public class Receive {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("root");
        factory.setPassword("123456");
        factory.setHost("192.168.237.128");
        //建立到代理服务器到连接
        Connection conn = factory.newConnection();
        //获得信道
        final Channel channel = conn.createChannel();
        //声明交换器
        String exchangeName = "myExchange";
        String queueName = "myQueueDirect";
        channel.queueDeclare(queueName, true, false, false, null);
        channel.exchangeDeclare(exchangeName, "direct", true);
        //声明队列
        String routingKey = "myRoutingKeyDirect";
        //绑定队列，通过键 hola 将队列和交换器绑定起来
        channel.queueBind(queueName, exchangeName, routingKey);
        //消费消息
        boolean autoAck = false;
        String consumerTag = "";
        //接收消息
        //参数1 队列名称
        //参数2 是否自动确认消息 true表示自动确认 false表示手动确认
        //参数3 为消息标签 用来区分不同的消费者这列暂时为""
        // 参数4 消费者回调方法用于编写处理消息的具体代码（例如打印或将消息写入数据库）
        System.out.println(queueName);
//开启事务
        channel.txSelect();
        channel.basicConsume(queueName, autoAck, consumerTag, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {

                //获取消息数据
                String bodyStr = new String(body, "UTF-8");
                System.out.println(bodyStr);
                //获取当前消息的序列号
                long deliveryTag = envelope.getDeliveryTag();
                //确认消息
                //参数 1 用于确定确认那条消息
                //参数 2 false 表示确认这条消息， true表示确认小于这个值的所有消息
                channel.basicAck(deliveryTag, false);
            }
        });
//开始提交事务
        channel.txCommit();
//回滚事务
//      channel.txRollback();
//      channel.close();
//      conn.close();
    }
}
