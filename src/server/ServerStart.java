package server;

import com.eclipsesource.v8.V8;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public class ServerStart {

    public static void main(String[] args) {

        V8 runtime = V8.createV8Runtime();
        String result = runtime.executeStringScript(""
          + "var hello = 'hello, ';\n"
          + "var world = 'world!';\n"
          + "hello.concat(world);\n");
        System.out.println(result);
        runtime.release();

        DatabaseConnection.open("MessageBoard.db");

        ResourceConfig config = new ResourceConfig();
        config.packages("server");
        ServletHolder servlet = new ServletHolder(new ServletContainer(config));

        Server server = new Server(8081);
        ServletContextHandler context = new ServletContextHandler(server, "/*");
        context.addServlet(servlet, "/*");

        try {
            server.start();
            Console.log("Server successfully started.");
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.destroy();
            DatabaseConnection.open("MessageBoard.db");
        }
    }
}

