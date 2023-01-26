package com.gooood.GooooCoin.conf;


import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Set;

@WebListener
@Component
public class ContextFinalizer implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
    }
//    public void contextDestroyed(ServletContextEvent sce) {
//        Enumeration<Driver> drivers = DriverManager.getDrivers();
//        Driver d = null;
//        while (drivers.hasMoreElements()) {
//            try {
//                d = drivers.nextElement();
//                DriverManager.deregisterDriver(d);
//                System.out.println(String.format("ContextFinalizer:Driver %s deregistered", d));
//            } catch (SQLException ex) {
//                System.out.println(String.format("ContextFinalizer:Error deregistering driver %s", d) + ":" + ex);
//            }
//        }
//        AbandonedConnectionCleanupThread.checkedShutdown();
//        AbandonedConnectionCleanupThread.uncheckedShutdown();
//    }

    public void contextDestroyed(ServletContextEvent sce) {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        Driver d = null;
        while (drivers.hasMoreElements()) {
            try {
                d = drivers.nextElement();
                DriverManager.deregisterDriver(d);
                System.out.println(String.format("\n\n\n\n\nContextFinalizer:Driver %s deregistered\n\n\n\n\n", d));
            } catch (SQLException ex) {
                System.out.println(String.format("\n\n\n\n\nContextFinalizer:Error deregistering driver %s\n\n\n\n\n", d) + ":" + ex);
            }
        }
        AbandonedConnectionCleanupThread.checkedShutdown();
        AbandonedConnectionCleanupThread.uncheckedShutdown();
        destroyThreads();
    }

    private void destroyThreads() {
        final Set<Thread> threads = Thread.getAllStackTraces().keySet();
        for (Thread thread : threads) {
//            System.out.println("\n\n\n\n\nthread is " + thread.getName() + "\n\n\n\n\n");
            synchronized (this) {
                try {
                    System.out.println();
                    if(thread.getName().contains("globalEventExecutor")||thread.getName().contains("threadDeathWatcher")){
                        thread.stop();
                        System.out.println("!!!!!!!!!!!!stop " + thread.getName() + "\n\n\n\n\n");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}