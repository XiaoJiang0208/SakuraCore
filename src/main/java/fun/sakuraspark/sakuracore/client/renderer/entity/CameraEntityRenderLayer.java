package fun.sakuraspark.sakuracore.client.renderer.entity;

import fun.sakuraspark.sakuracore.world.entity.CameraEntity;

import com.mojang.blaze3d.vertex.PoseStack;

import fun.sakuraspark.sakuracore.client.renderer.entity.CameraEntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

public class CameraEntityRenderLayer extends RenderLayer<CameraEntity, CameraEntityModel> {

    private final CameraEntityModel model;

    public CameraEntityRenderLayer(RenderLayerParent<CameraEntity, CameraEntityModel> renderer, EntityModelSet entityModelSet) {
        super(renderer);
        this.model = new CameraEntityModel(entityModelSet.bakeLayer(CameraEntityModel.LAYER_LOCATION));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, CameraEntity livingEntity,
            float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw,
            float headPitch) {
        // TODO Auto-generated method stub


    }

}
