package io.dsub.discogs.api.test;

import org.junit.jupiter.api.parallel.Execution;

import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;

@Execution(CONCURRENT)
public abstract class ConcurrentTest {
}
