package com.semantyca.jmx;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;

public class SystemConfigClient {
    static  String  host = "localhost", port = "9990";

    public static void main(String[] args) throws IOException, MalformedObjectNameException {
        try {
            host = args[0];
            port = args[1];
        } catch (ArrayIndexOutOfBoundsException e){
            System.out.println("It will be a default connection");
        }

        String urlString = "service:jmx:rmi:///jndi/rmi://" + host + ":" + port + "/jmxrmi";
        JMXServiceURL url = new JMXServiceURL(urlString);
        JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
        MBeanServerConnection connection = jmxc.getMBeanServerConnection();
        System.out.println("connected to :" + urlString);
        int count = connection.getMBeanCount();
        System.out.println("beans count=" + count);
        jmxc.close();

    }



}
