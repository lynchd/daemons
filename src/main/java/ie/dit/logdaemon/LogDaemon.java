package ie.dit.logdaemon;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class LogDaemon implements Daemon
{
	private Logger thisLogger = LogManager.getLogger(FileLoggerImpl.class);
	
	private static int NUMBER_OF_THREADS = 5;
	
	private List<FileLogger> loggers;
	private List<Thread> threads;
	
	public void init(DaemonContext arg0) throws DaemonInitException, Exception 
	{
		thisLogger.info("Initializing....");
		loggers = new ArrayList<FileLogger>();
		threads = new ArrayList<Thread>();
		thisLogger.info("Done Initializing");
	}

	public void start() throws Exception 
	{
		if(threads.size()>0 || loggers.size()>0)
			stop();
		
		thisLogger.info("Creating and starting threads....");
		for(int i = 0; i < NUMBER_OF_THREADS; i++)
		{
			FileLogger logger = new FileLoggerImpl(Integer.toString(i));
			Thread thread = new Thread(logger);
			loggers.add(logger);
			threads.add(thread);
			thread.start();
		}
		thisLogger.info("Done creating and starting threads.");
	}

	public void stop() throws Exception
	{
		thisLogger.info("Cleaning up threads...");
		for(int i = 0; i < NUMBER_OF_THREADS; i++)
		{
			FileLogger logger = loggers.get(i);
			Thread thread = threads.get(i);
			logger.isExecuting(false);
			thread.join();
		}
		thisLogger.info("Done cleaning up threads.");
	}
	public void destroy() 
	{
		thisLogger.info("Destroying resources...");
		loggers = null;
		threads = null;
		thisLogger.info("Done destroying resources.");
	}
	
	/**
	 * This is just for testing that our daemon works correctly 
	 * without the need for a scheduler
	 */
	public static void main(String argsv[]) 
		throws Exception
	{
		LogDaemon daemon = new LogDaemon();
		daemon.init(null);
		daemon.start();
		Thread.sleep(15000);
		daemon.stop();
	}
}
