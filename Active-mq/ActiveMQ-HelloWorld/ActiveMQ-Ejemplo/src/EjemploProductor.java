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
import java.util.Scanner;

public class EjemploProductor implements ExceptionListener {

    void processProducer() {
        try {
            //Especificamos la URL de conecciòn
            ActiveMQConnectionFactory connFact = new ActiveMQConnectionFactory(
                    "tcp://localhost:61616");
            //Creamos la conexiòn y la ejecutamos
            Connection conn = connFact.createConnection();
            conn.start();
            Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //Especificamos cola de destino (Lugar donde llegaran los mensajes -consumidor-)
            Destination destination = session.createQueue("MyQueue");
            MessageProducer prod = session.createProducer(destination);
            int i = 0;
            //Cantidad de mensajes a enviar
            Scanner source = new Scanner(System.in);
            System.out.println("Ingresar cantidad de mensajes a enviar:");
            int total = 0;
            if(source.hasNextInt()){
                total = source.nextInt();
            }
            while (i < total) {
                String text = "Mensaje - " + i;
                TextMessage message = session.createTextMessage(text);
                prod.send(message);
                i++;
            }
            //Cerramos la conexiòn
            session.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Excepccion: ");
            e.printStackTrace();
        }
    }

    public synchronized void onException(JMSException ex) {
        System.out.println("Exepciòn -El cliente se apagarà-.");
    }

    public static void main(String[] args) throws Exception {
        EjemploProductor p = new EjemploProductor();
        System.out.println("::Iniciando productor...");
        p.processProducer();
    }
}
