package net.rokyinfo.receive.util;

import org.apache.activemq.pool.PooledConnectionFactory;

import javax.jms.*;
import java.util.Map;


public class PoolMQSender {

    private PooledConnectionFactory poolConnectionFactory;

    public void sendCommonMsg(Map<String, String> messages, String queueName) throws JMSException {

        Connection connection = poolConnectionFactory.createConnection();

        try {
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(queueName);
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            TextMessage txtMessage = session.createTextMessage();

            messages.forEach((k, v) -> {
                try {
                    txtMessage.setStringProperty(k, v);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            });

            producer.send(txtMessage);
        } finally {
            connection.close();
        }
    }

    public void sendCommonMsgToTopic(Map<String, String> messages, String queueName) throws JMSException {

        Connection connection = poolConnectionFactory.createConnection();

        try {
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic(queueName);
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            TextMessage txtMessage = session.createTextMessage();

            messages.forEach((k, v) -> {
                try {
                    txtMessage.setStringProperty(k, v);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            });

            producer.send(txtMessage);
        } finally {
            connection.close();
        }

    }

    public Connection getMQConnection() throws JMSException {

        return poolConnectionFactory.createConnection();
    }

    public PooledConnectionFactory getPoolConnectionFactory() {
        return poolConnectionFactory;
    }

    public void setPoolConnectionFactory(
            PooledConnectionFactory poolConnectionFactory) {
        this.poolConnectionFactory = poolConnectionFactory;
    }
}
