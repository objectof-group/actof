package net.objectof.actof.common.controller.change;


import java.util.function.Consumer;


public abstract class Change {

    public <T extends Change> boolean is(Class<T> cls) {
        return cls.isInstance(this);
    }

    public <T extends Change> void when(Class<T> cls, Runnable runnable) {

        if (cls.isInstance(this)) {
            runnable.run();
        }

    }

    public <T extends Change> void when(Class<T> cls, Consumer<T> consumer) {

        if (cls.isInstance(this)) {
            @SuppressWarnings("unchecked")
            T t = (T) this;
            consumer.accept(t);
        }

    }


}
