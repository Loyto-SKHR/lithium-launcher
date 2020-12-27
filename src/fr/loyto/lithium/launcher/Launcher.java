package fr.loyto.lithium.launcher;

import java.io.File;

import fr.theshark34.openlauncherlib.launcher.GameInfos;
import fr.theshark34.openlauncherlib.launcher.GameTweak;
import fr.theshark34.openlauncherlib.launcher.GameType;
import fr.theshark34.openlauncherlib.launcher.GameVersion;

public class Launcher {
	
	public static final GameVersion LT_VERSION = new GameVersion("1.7.10", GameType.V1_7_10);
	public static final GameInfos LT_INFOS = new GameInfos("Lithium", LT_VERSION, true, new GameTweak[] {GameTweak.FORGE});
	public static final File LT_DIR = LT_INFOS.getGameDir();

}
