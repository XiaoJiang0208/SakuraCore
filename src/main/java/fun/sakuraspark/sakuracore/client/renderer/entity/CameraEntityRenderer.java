package fun.sakuraspark.sakuracore.client.renderer.entity;

import org.antlr.v4.parse.ANTLRParser.finallyClause_return;

import com.mojang.blaze3d.vertex.PoseStack;

import fun.sakuraspark.sakuracore.world.entity.CameraEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CameraEntityRenderer extends EntityRenderer<CameraEntity> {
    private static final ResourceLocation MISSING_TEXTURE = new ResourceLocation("missingno"); // 这会使用缺失纹理
    public final CameraEntityModel model;

    public CameraEntityRenderer(Context context) {
        super(context);
        //TODO Auto-generated constructor stub
        this.model = new CameraEntityModel(context.bakeLayer(CameraEntityModel.LAYER_LOCATION));
        this.shadowRadius = 0.3f; // 设置阴影大小
    }

    @Override
    public ResourceLocation getTextureLocation(CameraEntity entity) {
        // TODO Auto-generated method stub
        return ResourceLocation.tryBuild("sakuracore", "textures/entity/camera/camera.png");
    }

    @Override
    public void render(CameraEntity entity, float entityYaw, float partialTicks, PoseStack poseStack,
            MultiBufferSource buffer, int packedLight) {
        // TODO Auto-generated method stub
        this.model.setupAnim(entity, 0f, 0f, partialTicks, entityYaw, entity.getXRot());
        this.model.renderToBuffer(poseStack, buffer.getBuffer(this.model.renderType(getTextureLocation(entity))),
                packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

}
