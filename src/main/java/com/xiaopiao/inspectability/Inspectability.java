package com.xiaopiao.inspectability;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;

@Mod(Inspectability.MODID)
public class Inspectability
{
    public static final String MODID = "inspectability";

    public Inspectability(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        modEventBus.addListener(this::registerKeyMappings);
    }
    public static KeyMapping inspectorKey;


    public void registerKeyMappings(RegisterKeyMappingsEvent event) {
        inspectorKey = new KeyMapping(
                "key." + Inspectability.MODID + ".openiteminspector",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_GRAVE_ACCENT,
                "category." + Inspectability.MODID
        );

        event.register(inspectorKey);
    }

}
