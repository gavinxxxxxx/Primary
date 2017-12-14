package me.gavin.base.function;

/**
 * T -> Void
 *
 * @author gavin.xiong 2017/8/10
 */
@FunctionalInterface
public interface Consumer<T> {

    void accept(T t);
}
