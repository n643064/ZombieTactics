package n643064.zombie_tactics;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class MarkerRenderer extends EntityRenderer<Entity>
{
    private final ResourceLocation none = ResourceLocation.fromNamespaceAndPath("minecraft", "empty");

    protected MarkerRenderer(EntityRendererProvider.Context context)
    {
        super(context);
    }

    @Override @NotNull
    public ResourceLocation getTextureLocation(@NotNull Entity entity)
    {
        return none;
    }
}
