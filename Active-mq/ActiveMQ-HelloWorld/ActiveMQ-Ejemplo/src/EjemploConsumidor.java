
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

public class EjemploConsumidor {

    void processConsumer() {
        //Creamos el ID del cliente que lo està utilizando
        String clientID = "Thormod";
        try {
            //Especificamos la URL de conecciòn
            ActiveMQConnectionFactory connFact = new ActiveMQConnectionFactory(
                    "tcp://localhost:61616");
            //Creamos la conexiòn y la ejecutamos
            Connection conn = connFact.createConnection();
            Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);

            //Especificamos cola de destino (Lugar donde llegaran los mensajes -consumidor-)
            Destination destination = session.createQueue("MyQueue");
            MessageConsumer consumer = session.createConsumer(destination);
            //Añadimos un listener para escuchar los mensajes
            consumer.setMessageListener(listener);
            conn.start();
        } catch (Exception e) {
            System.out.println("Excepcion: " + e.getMessage());
            e.printStackTrace();
        }
    }
    MessageListener listener = new MessageListener() {
        
        public void onMessage(Message msg) {
            if (msg instanceof TextMessage) {
                TextMessage txtmsg = (TextMessage) msg;
                String text = null;
                try {
                    text = txtmsg.getText();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Mensaje recibido: " + text);
            } else {
                System.out.println("Recibido: " + msg);
            }
        }
    };

    public synchronized void onException(JMSException ex) {
        System.out.println("Exepciòn -El cliente se apagarà-.");
    }

    public static void main(String[] args) throws Exception {
        EjemploConsumidor c = new EjemploConsumidor();
        System.out.println("::Iniciando Consumidor...");
        c.processConsumer();

    }
}
