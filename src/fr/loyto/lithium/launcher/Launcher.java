package fr.loyto.lithium.launcher;

import java.io.File;
import java.io.IOException;

import fr.holo.mineweb.auth.exception.DataEmptyException;
import fr.holo.mineweb.auth.exception.DataWrongException;
import fr.holo.mineweb.auth.exception.ServerNotFoundException;
import fr.holo.mineweb.auth.mineweb.AuthMineweb;
import fr.holo.mineweb.auth.mineweb.utils.TypeConnection;
import fr.theshark34.openlauncherlib.launcher.AuthInfos;
import fr.theshark34.openlauncherlib.launcher.GameFolder;
import fr.theshark34.openlauncherlib.launcher.GameInfos;
import fr.theshark34.openlauncherlib.launcher.GameLauncher;
import fr.theshark34.openlauncherlib.launcher.GameTweak;
import fr.theshark34.openlauncherlib.launcher.GameType;
import fr.theshark34.openlauncherlib.launcher.GameVersion;
import fr.theshark34.openlauncherlib.util.ErrorUtil;
import fr.theshark34.supdate.BarAPI;
import fr.theshark34.supdate.SUpdate;
import fr.theshark34.supdate.application.integrated.FileDeleter;
import fr.theshark34.swinger.Swinger;

public class Launcher {
	
	public static final GameVersion LT_VERSION = new GameVersion("1.7.10", GameType.V1_7_10);
	public static final GameInfos LT_INFOS = new GameInfos("Lithium", LT_VERSION, true, new GameTweak[] {GameTweak.FORGE});
	public static final File LT_DIR = LT_INFOS.getGameDir();
	public static final File LT_CRASHES_DIR = new File(LT_DIR, "crashes");
	
	private static AuthInfos authInfos;
	private static Thread updateThread;
	
	private static ErrorUtil errorUtil = new ErrorUtil(LT_CRASHES_DIR);
	
	public static void auth(String username, String password) {

		AuthMineweb.setTypeConnection(TypeConnection.launcher);
		AuthMineweb.setUrlRoot("https://lithium-mc.tk/");
		AuthMineweb.setUsername(username);
		AuthMineweb.setPassword(password);
		try {
			AuthMineweb.auth();
		} catch (DataWrongException | DataEmptyException | ServerNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		authInfos = new AuthInfos(username, "dry", "none");
	}
	
	public static void update() throws Exception {
		SUpdate su = new SUpdate("https://loyto-skhr.tk/SUpdate/", LT_DIR);
		su.addApplication(new FileDeleter());
		
		updateThread = new Thread() {
			private int val;
			private int max;
			
			@Override
			public void run() {
				while(!this.isInterrupted()) {
					if(BarAPI.getNumberOfFileToDownload() == 0) {
						LauncherFrame.getInstance().getLaunchPanel().setInfoText("Vérification des fichiers");
						continue;
					}
					
					val = (int) (BarAPI.getNumberOfTotalDownloadedBytes() / 1000);
					max = (int) (BarAPI.getNumberOfTotalBytesToDownload() / 1000);
					
					LauncherFrame.getInstance().getLaunchPanel().getProgressBar().setMaximum(max);
					LauncherFrame.getInstance().getLaunchPanel().getProgressBar().setValue(val);
					
					LauncherFrame.getInstance().getLaunchPanel().setInfoText("Téléchrgement des fichiers " +
							BarAPI.getNumberOfDownloadedFiles() + "/" + BarAPI.getNumberOfFileToDownload() +
								" " + Swinger.percentage(val, max) + "%");
				}
			}
		};
		updateThread.start();
		
		su.start();
		updateThread.interrupt();
	}
	
	public static void launch() throws IOException {
		GameLauncher gameLauncher = new GameLauncher(LT_INFOS, GameFolder.BASIC, authInfos);
		Process p = gameLauncher.launch();
		
		try {
			Thread.sleep(5000L);
		} catch(InterruptedException e) {
		}
		LauncherFrame.getInstance().setVisible(false);
		
		try {
			p.waitFor();
		} catch(InterruptedException e) {
		}
		System.exit(0);
	}
	
	public static void interruptThread() {
		updateThread.interrupt();
	}
	
	public static ErrorUtil getErrorUtil() {
		return errorUtil;
	}

}
