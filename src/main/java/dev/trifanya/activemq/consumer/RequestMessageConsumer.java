package dev.trifanya.activemq.consumer;

import dev.trifanya.SwingCRUDApp;

import javax.jms.*;

import static javax.jms.Session.AUTO_ACKNOWLEDGE;

public class RequestMessageConsumer {
    private Session session;
    private Destination destination;
    private MessageConsumer consumer;

    public RequestMessageConsumer() throws JMSException {
        session = SwingCRUDApp.connection.createSession(false, AUTO_ACKNOWLEDGE);

        destination = session.createQueue(SwingCRUDApp.requestFromClientQueue);
        consumer = session.createConsumer(destination);
        consumer.setMessageListener(new RequestMessageListener());
    }
}