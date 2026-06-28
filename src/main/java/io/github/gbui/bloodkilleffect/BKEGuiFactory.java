package io.github.gbui.bloodkilleffect;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import java.util.Set;

public class BKEGuiFactory implements IModGuiFactory {
    @Override
    public void initialize(net.minecraft.client.Minecraft minecraftInstance) {}

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return null;
    }

    @Override
    public Set<IModGuiFactory.RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    @Override
    public IModGuiFactory.RuntimeOptionGuiHandler getHandlerFor(
            IModGuiFactory.RuntimeOptionCategoryElement element) {
        return null;
    }
}
