package net.tapcat.config
import org.eclipse.jetty.annotations.AnnotationConfiguration
import org.eclipse.jetty.annotations.ClassInheritanceHandler
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.nio.NetworkTrafficSelectChannelConnector
import org.eclipse.jetty.util.MultiMap
import org.eclipse.jetty.webapp.Configuration
import org.eclipse.jetty.webapp.WebAppContext
import org.springframework.web.WebApplicationInitializer

class WebServer {



    private Server server;
    private int port;
    private String bindInterface;

    public WebServer(int aPort)
    {
        this(aPort, null);
    }

    public WebServer(int aPort, String aBindInterface)
    {
        port = aPort;
        bindInterface = aBindInterface;
    }

    public void start() throws Exception
    {
        server = new Server(port);
        WebAppContext webAppContext = new WebAppContext()
        webAppContext.setConfigurations([
                new AnnotationConfiguration() {
                    @Override
                    public void preConfigure(WebAppContext context) throws Exception {
                        MultiMap<String> map = new MultiMap<String>();
                        map.add(WebApplicationInitializer.class.getName(), DispatcherServletInitializer.class.getName());
                        context.setAttribute(CLASS_INHERITANCE_MAP, map);
                        _classInheritanceHandler = new ClassInheritanceHandler(map);
                    }
                }] as Configuration[])
        webAppContext.setContextPath('/')
        server.setHandler(webAppContext)
        server.start();
    }

    public void join() throws InterruptedException
    {
        server.join();
    }

    public void stop() throws Exception
    {
        server.stop();
    }

    private NetworkTrafficSelectChannelConnector createConnector()
    {
        NetworkTrafficSelectChannelConnector _connector =
            new NetworkTrafficSelectChannelConnector(server);
        _connector.setPort(port);
        _connector.setHost(bindInterface);
        return _connector;
    }




}
