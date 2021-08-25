package Notorious;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import Notorious.ClickGui.settings.SettingsManager;
import Notorious.altManager.AltManager;
import Notorious.command.CommandManager;
import Notorious.config.ConfigManager;
import Notorious.config.FileFactory;
import Notorious.config.impl.AccountsFile;
import Notorious.config.impl.FriendsFile;
import Notorious.config.impl.ModulesFile;
import Notorious.config.impl.StatsFile;
import Notorious.event.Event;
import Notorious.event.events.EventKey;
import Notorious.friend.FriendManager;
import Notorious.module.Module;
import Notorious.module.ModuleManager;
import Notorious.module.modules.render.HUD;
import Notorious.ui.UIDScreen;
import Notorious.utils.Console;
import Notorious.utils.XRayUtils;
import Notorious.utils.fontRenderer.GlyphPageFontRenderer;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;

public class Client {

	public static final Client instance = new Client();
	public String name = "Notorious", author = "DemoDev", prefix = "[" + name + "] ";
	public float version_int = 1.0f;
	public String version = "B" + version_int;
	public String version_type = "Alpha";

	public boolean isDev = false;

	public boolean isVerified = false;

	public static boolean ready = false;

	public static boolean isShuttingDown = false;

	public long shaderInitTime = System.currentTimeMillis();

	public long savedStartTime = 0;

	public long startTime = System.currentTimeMillis();

	public int totalClientMills = 0;

	public ModuleManager moduleManager;
	public SettingsManager settingsManager;
	public FriendManager friendManager;
	public FileFactory fileFactory;
	public ConfigManager configManager;
	public HUD hud;
	public AltManager altManager;

	public GlyphPageFontRenderer font20Consolas;
	public GlyphPageFontRenderer font20;
	public GlyphPageFontRenderer font22;
	public GlyphPageFontRenderer font24;
	public GlyphPageFontRenderer font26;
	public GlyphPageFontRenderer font28;
	public GlyphPageFontRenderer font30;
	public GlyphPageFontRenderer font32;
	public GlyphPageFontRenderer font34;
	public GlyphPageFontRenderer font36;
	public GlyphPageFontRenderer font38;
	public GlyphPageFontRenderer font40;
	public GlyphPageFontRenderer font42;
	public GlyphPageFontRenderer font44;
	public GlyphPageFontRenderer font46;
	public GlyphPageFontRenderer font48;
	public GlyphPageFontRenderer font50;
	public GlyphPageFontRenderer font52;
	public GlyphPageFontRenderer font54;
	public GlyphPageFontRenderer font56;

	public void start() {
		initDiscord();

		int score = 0;
		System.out.println("Running callbacks...");

		Console.printWithPrefix("Loading " + name);
		Display.setTitle(isDev ? name + " " + version + " [DEV]" : name + " " + version);

		font20Consolas = GlyphPageFontRenderer.create("Consolas", 20, false, false, false);
		font20 = GlyphPageFontRenderer.create("segoe-ui", 20, false, false, false);
		font22 = GlyphPageFontRenderer.create("segoe-ui", 22, false, false, false);
		font24 = GlyphPageFontRenderer.create("segoe-ui", 24, false, false, false);
		font26 = GlyphPageFontRenderer.create("segoe-ui", 26, false, false, false);
		font28 = GlyphPageFontRenderer.create("segoe-ui", 28, false, false, false);
		font30 = GlyphPageFontRenderer.create("segoe-ui", 30, false, false, false);
		font32 = GlyphPageFontRenderer.create("segoe-ui", 32, false, false, false);
		font34 = GlyphPageFontRenderer.create("segoe-ui", 34, false, false, false);
		font36 = GlyphPageFontRenderer.create("segoe-ui", 36, false, false, false);
		font38 = GlyphPageFontRenderer.create("segoe-ui", 38, false, false, false);
		font40 = GlyphPageFontRenderer.create("segoe-ui", 40, false, false, false);
		font42 = GlyphPageFontRenderer.create("segoe-ui", 42, false, false, false);
		font44 = GlyphPageFontRenderer.create("segoe-ui", 44, false, false, false);
		font46 = GlyphPageFontRenderer.create("segoe-ui", 46, false, false, false);
		font48 = GlyphPageFontRenderer.create("segoe-ui", 48, false, false, false);
		font50 = GlyphPageFontRenderer.create("segoe-ui", 50, false, false, false);
		font52 = GlyphPageFontRenderer.create("segoe-ui", 52, false, false, false);
		font54 = GlyphPageFontRenderer.create("segoe-ui", 54, false, false, false);
		font56 = GlyphPageFontRenderer.create("segoe-ui", 56, false, false, false);

		settingsManager = new SettingsManager();
		moduleManager = new ModuleManager();
		friendManager = new FriendManager();
		configManager = new ConfigManager();
		altManager = new AltManager();
		hud = new HUD();

		this.fileFactory = new FileFactory();
		this.fileFactory.setupRoot();
		this.fileFactory.add(new ModulesFile(), new FriendsFile(), new AccountsFile(), new StatsFile());
		fileFactory.load();
		XRayUtils.initXRayBlocks();
		CommandManager.loadCommands();
		ConfigManager.downloadConfigs();
	}

	public void onGameLoopTick() {
		if (!(Minecraft.getMinecraft().currentScreen instanceof UIDScreen) && !isVerified && !isDev) {
			Minecraft.getMinecraft().displayGuiScreen(new UIDScreen());
		}
		if (!isShuttingDown) {
			DiscordRPC.discordRunCallbacks();
		}
	}

	public void createNewPresence(String bigtext, String details) {
		DiscordRichPresence rich = new DiscordRichPresence.Builder(bigtext).setDetails(details).build();
		DiscordRPC.discordUpdatePresence(rich);
	}

	private static void initDiscord() {
		DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler((user) -> {
			ready = true;
			System.out
					.println("Initialized Discord RPC with discord user:  " + user.username + "#" + user.discriminator);
			DiscordRichPresence.Builder presence = new DiscordRichPresence.Builder("");
			presence.setDetails("Starting...");
			DiscordRPC.discordUpdatePresence(presence.build());
		}).build();
		DiscordRPC.discordInitialize("862832265163571200", handlers, false);
		DiscordRPC.discordRegister("862832265163571200", "");
	}

	public void shutdown() {
		isShuttingDown = true;
		DiscordRPC.discordShutdown();

		if (!isVerified)
			return;
		Console.printWithPrefix("Shutting down " + name);
		// fileFactory.save(); //already calling in mc class to fix error with stats
	}

	public void onEvent(Event e) {
		for (Module m : Client.instance.moduleManager.modules) {
			if (!m.isEnabled())
				continue;

			m.onEvent(e);
		}
	}

	public void onKeyPress(int key) {
		// Key Press Event
		Client.instance.onEvent(new EventKey(key));

		for (Module m : Client.instance.moduleManager.getModules()) {
			if (m.getKey() == key) {
				m.toggle();
			}
		}

		if (key == Keyboard.KEY_PERIOD) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiChat(CommandManager.prefix));
		}
	}

	public int getTotalUptime() {
		return (int) -((startTime - savedStartTime) - System.currentTimeMillis());
	}

}
