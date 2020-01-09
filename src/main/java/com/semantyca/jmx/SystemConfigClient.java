package com.semantyca.jmx;

import javax.management.InstanceAlreadyExistsException;
import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Map;
import java.util.Set;

public class SystemConfigClient {

    public static void main(String[] args) throws IOException, MalformedObjectNameException {
        String host = "10.0.75.1";
        int port = 7155;
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
