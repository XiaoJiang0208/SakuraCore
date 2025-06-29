package fun.sakuraspark.sakuracore.world.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;

public class CameraEntity extends Entity {
    
    private double baseY = -1; // 新增字段，记录初始Y

    public CameraEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        // TODO Auto-generated constructor stub
        this.setInvisible(false);
        this.setCustomNameVisible(true);
        this.setCustomName(Component.translatable("Camera"));
        this.setInvulnerable(false);

    }

    @Override
    public void tick() {
        super.tick();
    if (!this.level().isClientSide) { // 只在服务端执行
        if (baseY < 0)
            baseY = this.getY();
        float time = (float) this.level().getGameTime() + this.tickCount;
        float amplitude = 1.0F; 
        float frequency = 0.1F; 
        float offsetY = amplitude * (float) Math.sin(frequency * time);
        double targetY = baseY + offsetY;
        double dy = targetY - this.getY();
        this.setDeltaMovement(0, dy*0.1, 0); // 设置Y方向速度
        this.hasImpulse = true; // 通知MC需要移动
        this.move(MoverType.SELF, this.getDeltaMovement());
    }

    }

    @Override
    protected void defineSynchedData() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method
        // 'defineSynchedData'");
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method
        // 'readAdditionalSaveData'");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method
        // 'addAdditionalSaveData'");
    }

    @Override
    public float getEyeHeight(Pose pose, EntityDimensions dimensions) {
        return 0.5F; // 这是普通玩家的眼睛高度，可以根据需要调整
    }

    @Override
    public boolean isPickable() {
        return true; // 使实体可被选中
    }
}
