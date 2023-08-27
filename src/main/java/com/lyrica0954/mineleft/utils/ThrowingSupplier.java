package com.lyrica0954.mineleft.utils;

import java.util.function.Supplier;

@FunctionalInterface
public interface ThrowingSupplier<T> {

	T get() throws Exception;
}
