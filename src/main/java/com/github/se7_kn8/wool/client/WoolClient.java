package com.github.se7_kn8.wool.client;

import com.github.se7_kn8.wool.Wool;
import com.github.se7_kn8.wool.client.gui.BlockEntityInventoryScreen;
import com.github.se7_kn8.wool.container.BlockEntityInventoryContainer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.ContainerScreenFactory;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.util.Identifier;

@SuppressWarnings("unused") // Loaded by fabric-loader
public class WoolClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ScreenProviderRegistry.INSTANCE.registerFactory(new Identifier(Wool.MODID, "shearer"), createBasicInventoryGui("textures/gui/container/basic_machine.png"));
		ScreenProviderRegistry.INSTANCE.registerFactory(new Identifier(Wool.MODID, "tree_cutter"), createBasicInventoryGui("textures/gui/container/basic_machine.png"));
		ScreenProviderRegistry.INSTANCE.registerFactory(new Identifier(Wool.MODID, "wool_collector"), createBasicInventoryGui(new Identifier("textures/gui/container/dispenser.png"))); // Use minecraft texture
		ScreenProviderRegistry.INSTANCE.registerFactory(new Identifier(Wool.MODID, "wood_collector"), createBasicInventoryGui(new Identifier("textures/gui/container/dispenser.png"))); // Use minecraft texture

		ClientSidePacketRegistry.INSTANCE.register(Wool.ADD_VELOCITY_TO_PLAYER_PACKET, (packetContext, packetByteBuf) -> {
			double velX = packetByteBuf.readDouble();
			double velY = packetByteBuf.readDouble();
			double velZ = packetByteBuf.readDouble();

			if (packetContext.getPlayer() != null && !packetContext.getPlayer().abilities.flying) {
				packetContext.getPlayer().addVelocity(velX, velY, velZ);
				packetContext.getPlayer().fallDistance = 0;
			}
		});
	}

	private <T extends BlockEntityInventoryContainer> ContainerScreenFactory<T> createBasicInventoryGui(Identifier identifier) {
		return container -> new BlockEntityInventoryScreen<T>(container) {
			@Override
			protected Identifier getBackground() {
				return identifier;
			}
		};
	}

	private <T extends BlockEntityInventoryContainer> ContainerScreenFactory<T> createBasicInventoryGui(String backgroundPath) {
		return createBasicInventoryGui(new Identifier(Wool.MODID, backgroundPath));
	}

}
