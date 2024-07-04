# Multithreading

## By implementing Runnable Interface

[!code-java[](code/RunnableTest.java)]

## By extending Thread Class

[!code-java[](code/ThreadTest.java)]

> [!IMPORTANT]
> After starting a thread, it can never be started again.

Get the currently executing thread by using: **public static Thread currentThread()**

## Daemon Thread

- It provides services to user threads for background supporting tasks. It has no role in life than to serve user threads.
- Its life depends on user threads. JVM automatically terminates the daemon thread if there is no user thread.
- It is a low priority thread.

[!code-java[](code/DaemonThreadDemo.java)]

> [!CAUTION]
> If you want to make a user thread as Daemon, it must not be started otherwise it will throw IllegalThreadStateException.

## Thread Pool

[!code-java[](code/ThreadPoolDemo.java)]

> [!CAUTION]
> If someone tries to send another task to the executor after shutdown, it will throw a RejectedExecutionException.

**Risks involved in Thread Pools:**
- Deadlock: Occur when all the threads that are executing are waiting for the results from the threads that are blocked and waiting in the queue because of the non-availability of threads for the execution.
- Thread Leakage: Leakage of threads occurs when a thread is being removed from the pool to execute a task but is not returning to it after the completion of the task.
- Resource Thrashing: A lot of time is wasted in context switching among threads when the size of the thread pool is very large. Whenever there are more threads than the optimal number may cause the starvation problem, and it leads to resource thrashing.

> [!NOTE]
> **Callable** is similar to **Runnable**, except that it can return a value. **Future** is used to get the result returned in the future.

[!code-java[](code/CallableDemo.java)]
