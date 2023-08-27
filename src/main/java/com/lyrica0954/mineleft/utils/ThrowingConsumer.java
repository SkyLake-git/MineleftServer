package com.lyrica0954.mineleft.utils;

import java.util.Objects;

@FunctionalInterface
public interface ThrowingConsumer<T> {

	/**
	 * Performs this operation on the given argument.
	 *
	 * @param t the input argument
	 */
	void accept(T t) throws Exception;

	default ThrowingConsumer<T> andThen(ThrowingConsumer<? super T> after) throws Exception {
		Objects.requireNonNull(after);
		return (T t) -> { accept(t); after.accept(t); };
	}
}
