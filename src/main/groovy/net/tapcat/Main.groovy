package net.tapcat
import net.tapcat.config.DispatcherServletInitializer
import net.tapcat.config.WebSecurityInitializer
import org.eclipse.jetty.annotations.AnnotationConfiguration
import org.eclipse.jetty.annotations.ClassInheritanceHandler
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.session.HashSessionManager
import org.eclipse.jetty.server.session.SessionHandler
import org.eclipse.jetty.util.MultiMap
import org.eclipse.jetty.webapp.Configuration
import org.eclipse.jetty.webapp.WebAppContext
import org.springframework.web.WebApplicationInitializer

class Main {

    public static void main(String[] args) throws Exception
    {
        new Main(args as List)
    }

    Main(List args) {
        int port = args[0]?.toInteger()?:8080
        def server = new Server(port);

        def webAppContext = new WebAppContext()

        webAppContext.configurations = [annotationConfig] as Configuration[]
        webAppContext.contextPath = '/'
        webAppContext.sessionHandler = new SessionHandler()

        def sessionManager = new HashSessionManager()
        sessionManager.savePeriod = 60000 // ms. Persist to disk interval
        sessionManager.lazyLoad = true
        sessionManager.storeDirectory = new File('sessions')
        sessionManager.httpOnly = true
        sessionManager.scavengePeriod = 60000 //ms
        webAppContext.sessionHandler.sessionManager = sessionManager

        server.setHandler(webAppContext)
        server.start()
        server.join()
    }

    private def annotationConfig = new AnnotationConfiguration() {
        /**
         * Jetty Annotation configuration will inspect JAR context, looking for Initializers.
         * But, if we,re going to execute app in exploded mode (without creating a JAR), Jetty won't
         * find any initializers. Let's put our initializer manually.
         * @param context
         * @throws Exception
         */
        @Override
        public void preConfigure(WebAppContext context) throws Exception {
            MultiMap<String> map = new MultiMap<String>()
            map.add(WebApplicationInitializer.getName(), DispatcherServletInitializer.getName())
            map.add(WebApplicationInitializer.getName(), WebSecurityInitializer.getName())
            context.setAttribute(CLASS_INHERITANCE_MAP, map)
            _classInheritanceHandler = new ClassInheritanceHandler(map)
        }
    }
}
