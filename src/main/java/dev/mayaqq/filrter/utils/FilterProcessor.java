package dev.mayaqq.filrter.utils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;

public class FilterProcessor {
    public static boolean test(Component text, ItemStack stack) {
        if (!text.getString().startsWith("\uD83D\uDD0D")) return false;

        String string = text.getString().replaceAll("\uD83D\uDD0D ", "");
        String[] filters = string.split(",");
        for (String filter : filters) {
            if (filter.startsWith("#") && stack.is(TagKey.create(BuiltInRegistries.ITEM.key(), new ResourceLocation(filter.replace("#", ""))))) {
                return true;
            } else if (BuiltInRegistries.ITEM.getKey(stack.getItem()).toString().matches(filter)) {
                return true;
            }
        }
        return false;
    }
}
