package me.gavin.base.function;

/**
 * (T, U) -> boolean
 *
 * @author gavin.xiong 2017/8/10
 */
@FunctionalInterface
public interface BiPredicate<T, U> {

    boolean test(T t, U u);
}
