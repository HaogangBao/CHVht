package smartlab.communication;

import org.w3c.dom.Text;

import javax.jms.*;

public class TopicListener implements MessageListener {
    private CommunicationManager manager;
    private ISLSubscriber subscriber;
    private Topic topic;

    TopicListener(ISLSubscriber subscriber, Topic topic) {
        this.subscriber = subscriber;
        this.topic = topic;
    }

    public ISLSubscriber getSubscriber() {
        return subscriber;
    }

    public Topic getTopic() {
        return topic;
    }

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage && subscriber instanceof ISLTextSubscriber) {
                ((ISLTextSubscriber)subscriber).onReceive(topic.getTopicName(), ((TextMessage)message).getText());
            }
            else if (message instanceof BytesMessage && subscriber instanceof ISLBytesSubscriber) {
                BytesMessage bm = (BytesMessage)message;
                byte[] temp = new byte[(int)bm.getBodyLength()];
                bm.readBytes(temp);
                ((ISLBytesSubscriber)subscriber).onReceive(topic.getTopicName(), temp);
            }
            else if (subscriber instanceof ISLMessageSubscriber) {
                ((ISLMessageSubscriber)subscriber).onReceive(topic.getTopicName(), message);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
