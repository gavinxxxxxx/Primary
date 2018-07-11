package me.gavin.base.function;

/**
 * T -> R
 *
 * @author gavin.xiong 2017/8/10
 */
@FunctionalInterface
public interface Function<T, R> {

    R apply(T var1);
}
