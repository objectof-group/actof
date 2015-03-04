package net.objectof.actof.minion.util;


import java.util.concurrent.Executor;
import java.util.function.Consumer;

import javafx.application.Platform;


/**
 * Provides a mechanism for first performing work off-thread and then performing
 * follow-up work on-thread for JavaFX. The off-thread task is set by calling
 * {@link Worker#first(Producer)}. The on-thread task is set by calling
 * {@link Worker#then(Consumer)}. Exception handling for problems in the
 * off-thread task can be supplied through {@link Worker#except(Except)}
 * 
 * @author NAS
 *
 * @param <T>
 */
public class Worker<T> implements Runnable {

    public interface Producer<T> {

        T produce() throws Exception;
    }

    private Producer<T> supplier;
    private Consumer<T> consumer;
    private Consumer<Throwable> handler;

    private Worker(Producer<T> supplier) {
        this.supplier = supplier;
    }

    public static <T> Worker<T> first(Runnable supplier) {
        return new Worker<T>(() -> {
            supplier.run();
            return null;
        });
    }

    public static <T> Worker<T> first(Producer<T> supplier) {
        return new Worker<T>(supplier);
    }

    public Worker<T> then(Consumer<T> consumer) {
        this.consumer = consumer;
        return this;
    }

    public Worker<T> except(Consumer<Throwable> handler) {
        this.handler = handler;
        return this;
    }

    private Runnable runnable() {
        return () -> {

            try {
                T result = supplier.produce();
                Platform.runLater(() -> {
                    consumer.accept(result);
                });
            }
            catch (Exception e) {
                e.printStackTrace();
                if (handler != null) {
                    Platform.runLater(() -> handler.accept(e));
                }
            };

        };
    }

    public void run(Executor pool) {
        pool.execute(runnable());
    }

    public void run() {

        Thread thread = new Thread(runnable());
        thread.setDaemon(true);
        thread.start();

    }
}
