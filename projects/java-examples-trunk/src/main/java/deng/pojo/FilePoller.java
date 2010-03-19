package deng.pojo;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;


public class FilePoller {
	private static final String DEFAULT_ARCHIVE_DIR_NAME = ".archive";

	private static Logger logger = Logger.getLogger(FilePoller.class.getName());
	
	private File directory;
	
	private File archiveDirectory;	

	private long pollingDelay = 1000L;
	
	private long pollingPeriod = 3000L;
	
	private Timer timer;
	
	private FilePollerAction filePollerAction;
	
	private TimerTask pollerTimerTask = new TimerTask() {
		private FilenameFilter archiveDirFilter = new FilenameFilter(){
			@Override
			public boolean accept(File dir, String name) {
				return !archiveDirectory.getName().equals(archiveDirectory.getName());
			}			
		};
		
		@Override
		public void run() {
			logger.info("Polling dir " + directory);
			File files [] = directory.listFiles(archiveDirFilter);
			if (files != null) {
				for (File file : files) {
					logger.info("Found file " + file);
					filePollerAction.onFile(file);
					file.renameTo(new File(archiveDirectory, file.getName()));
				}
			}
		}		
	};
	
	public void init() {
		if (archiveDirectory == null) {
			archiveDirectory = new File(directory, DEFAULT_ARCHIVE_DIR_NAME);
		}
		
		if (!archiveDirectory.exists()) {
			logger.info("Creating archive directory " + archiveDirectory);
			archiveDirectory.mkdirs();
		}
		
		logger.info("Initializing timer with delay " + pollingDelay + ", period " + pollingPeriod);
		timer = new Timer();
		timer.schedule(pollerTimerTask , pollingDelay, pollingPeriod);
	}
	
	public void destroy() {
		logger.info("Destroying timer object " + timer);
		timer.cancel();
	}
		
	public void setFilePollerAction(FilePollerAction filePollerAction) {
		this.filePollerAction = filePollerAction;
	}

	public File getArchiveDirectory() {
		return archiveDirectory;
	}

	public File getDirectory() {
		return directory;
	}

	public long getPollingDelay() {
		return pollingDelay;
	}

	public long getPollingPeriod() {
		return pollingPeriod;
	}

	public FilePollerAction getFilePollerAction() {
		return filePollerAction;
	}
	
	public TimerTask getPollerTimerTask() {
		return pollerTimerTask;
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public void setDirectory(File directory) {
		this.directory = directory;
	}

	public void setArchiveDirectory(File archiveDirectory) {
		this.archiveDirectory = archiveDirectory;
	}

	public void setPollingDelay(long pollingDelay) {
		this.pollingDelay = pollingDelay;
	}

	public void setPollingPeriod(long pollingPeriod) {
		this.pollingPeriod = pollingPeriod;
	}
}
