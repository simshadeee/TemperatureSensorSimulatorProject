import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import java.util.Random;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class TemperatureSensorSimulatorProject {
    public static long markedTime = System.currentTimeMillis();
    public static int runIt = 1;

    public static void main(String[] args) {
//      This is so that the main function continues to run
        while (runIt != 0) {
//          getting the time to run the function every 5 seconds
            long currTime = System.currentTimeMillis();
            float timePassed = (currTime - markedTime) / 1000F;
//           when 5 seconds have passed, publish out the weather and feel
            if(timePassed > 5.0){
//              Reset the initial time so the function continues to run every 5 seconds
                markedTime = System.currentTimeMillis();
//              set all info needed for publishing. QOS stands for how hard the client/broker should work for this message
                String topic        = "MQTT Examples";
                String content      = "";
                int qos             = 2;
                String broker       = "tcp://localhost:1883";
                String clientId     = "JavaSample";
                MemoryPersistence persistence = new MemoryPersistence();

                try {
//                  Make the connection
                    MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
                    MqttConnectOptions connOpts = new MqttConnectOptions();
                    connOpts.setCleanSession(true);
//                    System.out.println("Connecting to broker: "+broker);
                    sampleClient.connect(connOpts);
//                    System.out.println("Connected");

                    Random rand = new Random();
                    // Generate random integers in range 0 to 100
                    int randInt = rand.nextInt(101);

                    //getting current date and time using Date class
                    DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                    Date dateobj = new Date();

                    if (randInt <= 45) {
                        String feel = "cold";
                        topic = "pittsburgh/temperature/coldTemps";
                        System.out.println("Sending a " + feel + "temp message to " + topic);
                        content = "It is " + feel + " out " + randInt + " degrees at time: " + df.format(dateobj);
                    } else if (randInt >= 46 && randInt <= 80) {
                        String feel = "nice";
                        topic = "pittsburgh/temperature/niceTemps";
                        System.out.println("Sending a " + feel + "temp message to " + topic);
                        content = "It is " + feel + " out " + randInt + " degrees at time: " + df.format(dateobj);
                    } else {
                        String feel = "hot";
                        topic = "pittsburgh/temperature/hotTemps";
                        System.out.println("Sending a " + feel + "temp message to " + topic);
                        content = "It is " + feel + " out " + randInt + " degrees at time: " + df.format(dateobj);
                    }
                    System.out.println("Publishing message: "+content);
                    MqttMessage message = new MqttMessage(content.getBytes());
                    message.setQos(qos);
                    sampleClient.publish(topic, message);
                    System.out.println("Message published");
//                    sampleClient.disconnect();
//                    System.out.println("Disconnected");
//                System.exit(0);
                } catch(MqttException me) {
                    System.out.println("reason "+me.getReasonCode());
                    System.out.println("msg "+me.getMessage());
                    System.out.println("loc "+me.getLocalizedMessage());
                    System.out.println("cause "+me.getCause());
                    System.out.println("excep "+me);
                    me.printStackTrace();
                }
            }



        }
    }
}