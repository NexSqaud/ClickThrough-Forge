package ru.nexsqaud.clickthrough.mixins;

import ru.nexsqaud.clickthrough.ClickThrough;
import ru.nexsqaud.clickthrough.Configuration;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.WallBannerBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.DyeItem;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class ItemUseMixin {

    @Shadow
    public HitResult hitResult;

    @Shadow
    public LocalPlayer player;

    @Shadow
    public ClientLevel level;

    private boolean deferSneak = false;

    @Inject(method = "startUseItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;"))
    public void switchCrosshairTarget(CallbackInfo info) {
        ClickThrough.isDyeOnSign = false;
        if (hitResult == null) {
            return;
        }

        if (hitResult.getType() == HitResult.Type.ENTITY
                && ((EntityHitResult) hitResult).getEntity() instanceof ItemFrame) {
            ItemFrame itemFrame = (ItemFrame) ((EntityHitResult) hitResult).getEntity();
            Vec3 frameBack = itemFrame.getForward().reverse();
            BlockPos attachedPos = itemFrame.getPos()
                    .offset(new Vec3i((int) frameBack.x, (int) frameBack.y, (int) frameBack.z));
            if (!player.isCrouching() && isClickableBlockAt(attachedPos)) {
                hitResult = new BlockHitResult(hitResult.getLocation(),
                        itemFrame.getDirection(),
                        attachedPos, false);
            }
            return;
        }

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockPos = ((BlockHitResult) hitResult).getBlockPos();
            BlockState state = level.getBlockState(blockPos);
            Block block = state.getBlock();
            if (block instanceof WallSignBlock) {
                Direction facing = getFacing(state);
                if(facing == null) {
                    return;
                }
                BlockPos attachedPos = blockPos.offset(facing.getOpposite().getNormal());
                if (!isClickableBlockAt(attachedPos)) {
                    return;
                }
                BlockEntity entity = level.getBlockEntity(blockPos);
                if (!(entity instanceof SignBlockEntity)) {
                    return;
                }

                if (player.getMainHandItem().getItem() instanceof DyeItem) {
                    if (!Configuration.needsSneakToDye) {
                        return;
                    }
                    ClickThrough.isDyeOnSign = true;
                    if (player.isCrouching()) {
                        deferSneak = true;
                        player.connection.send(
                                new ServerboundPlayerCommandPacket(player,
                                        ServerboundPlayerCommandPacket.Action.RELEASE_SHIFT_KEY));
                    } else {
                        hitResult = new BlockHitResult(hitResult.getLocation(),
                                ((BlockHitResult) hitResult).getDirection(), attachedPos, false);
                    }
                } else {
                    if (!player.isCrouching()) {
                        hitResult = new BlockHitResult(hitResult.getLocation(),
                                ((BlockHitResult) hitResult).getDirection(), attachedPos, false);
                    }
                }
            } else if (block instanceof WallBannerBlock) {
                Direction facing = getFacing(state);
                if(facing == null) {
                    return;
                }
                BlockPos attachedPos = blockPos.offset(facing.getOpposite().getNormal());
                if (isClickableBlockAt(attachedPos)) {
                    hitResult = new BlockHitResult(hitResult.getLocation(),
                            ((BlockHitResult) hitResult).getDirection(), attachedPos, false);
                }
            }
        }

    }

    @Inject(method = "startUseItem", at = @At("RETURN"))
    public void reSneakIfNeccesary(CallbackInfo ci) {
        if (deferSneak) {
            deferSneak = false;
            player.connection.send(new ServerboundPlayerCommandPacket(player,
                    ServerboundPlayerCommandPacket.Action.PRESS_SHIFT_KEY));
        }
    }

    private boolean isClickableBlockAt(BlockPos pos) {
        if (!Configuration.onlyToContainers) {
            return true;
        }
        BlockEntity entity = level.getBlockEntity(pos);
        return (entity != null && entity instanceof BaseContainerBlockEntity);
    }
    
    private static Direction getFacing(BlockState state) {
        for (Property<?> prop : state.getProperties()) {
            if ("facing".equals(prop.getName()) && prop.getValueClass() == Direction.class) {
                return (Direction) state.getValue(prop);
            }
        }
        return null;
    }
}
