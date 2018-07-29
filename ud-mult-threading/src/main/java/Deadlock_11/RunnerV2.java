/**
 * 
 */
package Deadlock_11;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author haoch
 *
 */
public class RunnerV2 {

	private Account acc1 = new Account();
	private Account acc2 = new Account();
	private Lock lock1 = new ReentrantLock();
	private Lock lock2 = new ReentrantLock();
	

	// don't hold several locks at once. If you do, always acquire the locks in the
	// same order
	// try to get the both locks
	private void acquireLocks(Lock firstLock, Lock secondLock) throws InterruptedException {
		
		boolean gotFirstLock = false;
		boolean gotSecondLock = false;
		while (gotFirstLock==false && gotSecondLock==false) {
			// Acquire locks
			
			try {
				/**
				 * tryLock() which will only acquire a lock if it’s available and not already
				 * acquired by another thread and tryLock(long time,TimeUnit unit), which will
				 * try to acquire a lock and, if it's unavailable wait for the specified timer
				 * to expire before giving up
				 */
				gotFirstLock = firstLock.tryLock();
				gotSecondLock = secondLock.tryLock();
			} finally {
				if (gotFirstLock && gotSecondLock)
					return;
				else if (gotFirstLock) {
					firstLock.unlock();
					gotFirstLock=false;
				}
				else if (gotSecondLock) {
					secondLock.unlock();
					gotSecondLock=false;
				}
			}
			// Locks not acquired
			//Thread.sleep(1);
			Thread.yield();
			

		}
	}

	// firstThread runs
	public void firstThread() throws InterruptedException {
		Random random = new Random();
		int count=0;
		for (int i = 0; i < 10000; i++) {
			acquireLocks(lock1, lock2);
			try {
				Account.transfer(acc1, acc2, random.nextInt(100));
			} finally {
				lock1.unlock();
				lock2.unlock();
			
			}
			count =i;
		}
		System.out.println("Iteration for " + Thread.currentThread().getName() + count);
	}

	// SecondThread runs
	public void secondThread() throws InterruptedException {
		int count =0;
		Random random = new Random();
		for (int i = 0; i < 10000; i++) {
			acquireLocks(lock2, lock1);
			try {
				Account.transfer(acc2, acc1, random.nextInt(100));
			} finally {
				lock1.unlock();
				lock2.unlock();
			
			}
			count =i;
		}
		System.out.println("Iteration for " + Thread.currentThread().getName() + count);
	}

	// When both threads finish execution, finished runs
	public void finished() {
		System.out.println("Account 1 balance: " + acc1.getBalance());
		System.out.println("Account 2 balance: " + acc2.getBalance());
		System.out.println("Total balance: " + (acc1.getBalance() + acc2.getBalance()));
	}

}
