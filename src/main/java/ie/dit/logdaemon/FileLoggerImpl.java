package ie.dit.logdaemon;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;

public class FileLoggerImpl implements FileLogger 
{
	private boolean isExecuting = true;
	private int DELAY_SECONDS = 5;
	private String id;
	
	private Logger logger = LogManager.getLogger(FileLoggerImpl.class);
	
	public FileLoggerImpl(String id) {
		this.id = id;
	}
	
	public void run() 
	{
		while(isExecuting) 
		{
			logger.info("Thread " + id + ": Now writing logfile");
			logger.info("Thread " + id + ": Now waiting " + DELAY_SECONDS + " seconds...");
			pause(DELAY_SECONDS);	
		}
	}
	
	private void pause(int seconds)
	{
		try {
			Thread.sleep(seconds * 1000);
		}
		catch(Exception ex) { }
	}
	
	public void isExecuting(boolean value) 
	{
		this.isExecuting = false;	
	}
	
	public String getId() {
		return id;
	}
}
