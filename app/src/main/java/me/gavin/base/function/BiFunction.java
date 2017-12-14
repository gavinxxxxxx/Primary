package me.gavin.base.function;

/**
 * (T, U) -> R
 *
 * @author gavin.xiong 2017/8/10
 */
@FunctionalInterface
public interface BiFunction<T, U, R> {

    R apply(T t, U u);
}
