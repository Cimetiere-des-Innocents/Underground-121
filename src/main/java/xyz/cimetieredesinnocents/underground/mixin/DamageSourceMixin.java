package xyz.cimetieredesinnocents.underground.mixin;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.cimetieredesinnocents.underground.loaders.datagen.DamageTypeLoader;

@Mixin(DamageSource.class)
public abstract class DamageSourceMixin {
    @Shadow
    @Final
    private Holder<DamageType> type;

    @Shadow
    @Final
    private Entity causingEntity;

    @Inject(method = "getLocalizedDeathMessage", at = @At("HEAD"), cancellable = true)
    protected void onMessage(LivingEntity livingEntity, CallbackInfoReturnable<Component> cir) {
        if (type == DamageTypeLoader.INSTANCE.getSUNBURNT().invoke(livingEntity.level())) {
            if (causingEntity == null || causingEntity == livingEntity) {
                cir.setReturnValue(
                        Component.translatable(
                                "death.attack.underground.sunburnt",
                                livingEntity.getDisplayName()
                        )
                );
            } else {
                cir.setReturnValue(
                        Component.translatable(
                                "death.attack.underground.sunburnt.player",
                                causingEntity.getDisplayName(),
                                livingEntity.getDisplayName()
                        )
                );
            }
        }
    }
}
