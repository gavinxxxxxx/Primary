package me.gavin.base.function;

/**
 * (T, U) -> Void
 *
 * @author gavin.xiong 2017/8/10
 */
@FunctionalInterface
public interface BiConsumer<T, U> {

    void accept(T t, U u);
}
