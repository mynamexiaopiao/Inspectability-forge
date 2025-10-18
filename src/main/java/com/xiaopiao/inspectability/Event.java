package com.xiaopiao.inspectability;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import static com.xiaopiao.inspectability.Inspectability.inspectorKey;

@Mod.EventBusSubscriber(modid = Inspectability.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Event {



    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft client = Minecraft.getInstance();
            while (inspectorKey != null && inspectorKey.consumeClick()) {
                LocalPlayer player = client.player;
                if (player != null && !player.getMainHandItem().isEmpty()) {
                    client.setScreen(new InspectorScreen());
                }
            }
        }
    }


}
