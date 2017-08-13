package com.platformer.proto.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.platformer.proto.PlatformerProto;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Platformer Prototype";
		config.width = 800;
		config.height = 600;
		new LwjglApplication(new PlatformerProto(), config);
	}
}
