package enkan.util;

import enkan.exception.FalteringEnvironmentException;
import enkan.exception.MisconfigurationException;
import enkan.exception.UnrecoverableException;

import java.lang.reflect.InvocationTargetException;

/**
 * @author kawasima
 */
public class ReflectionUtils {
    public static <T> T tryReflection(ReflectionRunnable<T> runnable) {
        try {
            return runnable.run();
        } catch (InstantiationException e) {
            throw MisconfigurationException.create("INSTANTIATION.problem");
        } catch (InvocationTargetException e) {
            Throwable t = e.getTargetException();
            if (t instanceof Error) {
                throw (Error) t;
            } else if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else if (t instanceof Exception) {
                throw FalteringEnvironmentException.create(e);
            } else {
                throw new InternalError(t);
            }
        } catch (NoSuchMethodException e) {
            throw MisconfigurationException.create("NO_SUCH_METHOD");
        } catch (IllegalAccessException e) {
            throw MisconfigurationException.create("ILLEGAL_ACCESS");
        } catch (ClassNotFoundException e) {
            throw MisconfigurationException.create("CLASS_NOT_FOUND");
        }
    }
}