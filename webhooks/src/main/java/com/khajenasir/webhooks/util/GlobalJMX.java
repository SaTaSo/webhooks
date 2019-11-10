package AfshinTest;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 10/1/2016.
 */
public class GlobalJMX {

    private static MBeanServer mBeanServer;
    private static AtomicInteger counter = new AtomicInteger();
    private static NotificationBroadcasterSupport notificationBS = new NotificationBroadcasterSupport();

    public static MBeanServer getMBeanServer() {
        if (mBeanServer == null)
            mBeanServer = ManagementFactory.getPlatformMBeanServer();
        return mBeanServer;
    }

    public static <T> ObjectName getObjectName(String id, String nameSpace, T object) throws MalformedObjectNameException {
        String Type = object.getClass().getName();
        return new ObjectName(nameSpace + ":type=" + Type + ",name=" + id);
    }

    public static <T> ObjectInstance registerMBean(String id, String nameSpace, T object) throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException {
        return getMBeanServer().registerMBean(object, getObjectName(id, nameSpace, object));
    }

    public <T> void notify(T object) {
        Integer i = counter.incrementAndGet();
        if (i >= Integer.MAX_VALUE) counter.set(Integer.MIN_VALUE);

        long currentTimeMillis = System.currentTimeMillis();
        Notification notification = new Notification(object.getClass().getName(), mBeanServer, i.longValue(), currentTimeMillis);
        notificationBS.sendNotification(notification);
    }

}
