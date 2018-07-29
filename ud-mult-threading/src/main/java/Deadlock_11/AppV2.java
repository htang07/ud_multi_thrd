/**
 * 
 */
package Deadlock_11;

/**
 * @author haoch
 *
 */
public class AppV2 {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		final RunnerV2 runner = new RunnerV2();
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                try {
                    runner.firstThread();
                } catch (InterruptedException ignored) {}
            }
        });
        t1.setName("firstThread");

        Thread t2 = new Thread(new Runnable() {
            public void run() {
                try {
                    runner.secondThread();
                } catch (InterruptedException ignored) {}
            }
        });
        t2.setName("secondThread");

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        runner.finished();
	}

}
