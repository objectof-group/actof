package net.objectof.actof.common.controller.change;


import java.util.function.Consumer;


public interface ChangeController {

    void listen(Consumer<Change> l);

    default <T extends Change> void listen(Class<T> cls, Runnable l) {
        listen(change -> change.when(cls, l));
    }

    default <T extends Change> void listen(Class<T> cls, Consumer<T> l) {
        listen(change -> change.when(cls, l));
    }

    void unlisten(final Consumer<Change> l);

    void clear();

    void broadcast(final Change message);

}