package com.semantyca.jmx;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.rmi.RMIConnectorServer;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIServerSocketFactory;
import java.util.HashMap;
import java.util.Map;

public class SimpleJmxServer implements Remote {
    private static Registry rmiRegistry;
    private MBeanServer mbeanServer;
    private JMXConnectorServer connector;
    private InetAddress inetAddress;
    static int registryPort = 9990;
    private String registryHost = "localhost";
    private int serverPort = 9990;
    private String serverHost = "localhost";

    SimpleJmxServer() {

    }

    public void start(){
        try {
            String serviceUrl = "service:jmx:rmi://" + serverHost + ":" + serverPort + "/jndi/rmi://" + registryHost + ":"
                    + registryPort + "/jmxrmi";
            JMXServiceURL url = new JMXServiceURL(serviceUrl);

            inetAddress = InetAddress.getByName("127.0.0.1");
            Map<String, Object> envMap = new HashMap<String, Object>();
            mbeanServer = ManagementFactory.getPlatformMBeanServer();
            RMIServerSocketFactory serverSocketFactory = new RMIServerSocketFactory() {
                @Override
                public ServerSocket createServerSocket(int port) throws IOException {
                    return new ServerSocket(port, 0, inetAddress);
                }
            };
            envMap.put(RMIConnectorServer.RMI_SERVER_SOCKET_FACTORY_ATTRIBUTE, serverSocketFactory);

            mbeanServer = ManagementFactory.getPlatformMBeanServer();
            connector = JMXConnectorServerFactory.newJMXConnectorServer(url, envMap, mbeanServer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            connector.start();
        } catch (IOException e) {
            connector = null;
            e.printStackTrace();
        }
    }

    public static void register (int port,SimpleJmxServer jmxServer) throws RemoteException {
        rmiRegistry = LocateRegistry.createRegistry(port);
        rmiRegistry.rebind ("serviceImpl", jmxServer);
    }

    public static void main(String[] args) throws RemoteException {
        SimpleJmxServer jmxServer = new SimpleJmxServer();
        register(registryPort, jmxServer);
        jmxServer.start();

    }

}
