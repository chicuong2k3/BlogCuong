# Synchronization

## Synchronized Method

[!code-java[](code/SynchronizedMethod.java)]

## Synchronized Block

[!code-java[](code/SynchronizedBlock.java)]

## Static Synchronization

Suppose there are two objects of a shared class named printer1 and printer2.

```java
PrintThread t1 = new PrintThread(printer1, "Hello");
PrintThread t2 = new PrintThread(printer1, "Static");
PrintThread t3 = new PrintThread(printer2, "Synchronized");
PrintThread t4 = new PrintThread(printer2, "World");
```


In case of synchronized method and synchronized block there cannot be interference between t1 and t2 (or t3 and t4) because t1 and t2 (or t3 and t4) both refers to a common object that have a single lock. 

But there can be interference between t1 and t3 because t1 acquires another lock and t3 acquires another lock. We don't want interference between t1 and t3 or t2 and t4. Static synchronization solves this problem.

[!code-java[](code/StaticSynchronization.java)]

## Reentrant Lock

Provide additional mechanisms to handle the following cases:
- Lock an object immediately or within a timeout period.
- Be able to unlock in another method.
- Fair lock waiting mechanism (the thread that has waited the longest will acquire the lock).
- May have lock conditions.

[!code-java[](code/ReentrantLockDemo.java)]

## Inter-thread Communication

The **wait()** method causes current thread to release the lock and wait until either another thread invokes the **notify()** method or the **notifyAll()** method for this object, or a specified amount of time has elapsed.

The **notify()** method wakes up a single thread that is waiting on this object's monitor. If any threads are waiting on this object, one of them is chosen to be awakened. The choice is arbitrary and occurs at the discretion of the implementation.

The **notifyAll()** method wakes up all threads that are waiting on this object's monitor.


