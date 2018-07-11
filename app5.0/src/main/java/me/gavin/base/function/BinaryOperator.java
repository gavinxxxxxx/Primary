package me.gavin.base.function;

/**
 * (T, T) -> T
 *
 * @author gavin.xiong 2017/8/10
 */
@FunctionalInterface
public interface BinaryOperator<T> extends BiFunction<T, T, T> {

}
