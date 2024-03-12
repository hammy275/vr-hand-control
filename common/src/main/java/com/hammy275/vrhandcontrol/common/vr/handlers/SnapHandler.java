package com.hammy275.vrhandcontrol.common.vr.handlers;

import com.hammy275.vrhandcontrol.VRHandControl;
import com.hammy275.vrhandcontrol.client.vr_data.vd.Finger;
import com.hammy275.vrhandcontrol.client.vr_data.vd.Hand;
import com.hammy275.vrhandcontrol.client.vr_data.vd.Joint;
import com.hammy275.vrhandcontrol.client.vr_data.vd.VRData;
import com.hammy275.vrhandcontrol.client.vr_data.vivecraft.VRPlugin;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.LinkedList;
import java.util.List;

public class SnapHandler implements GestureHandler {

    private boolean middlePinched = false;

    @Override
    public boolean handleDetection(VRData data, LinkedList<VRData> oldData) {
        // TODO: Potentitally detect if the middle finger tip is extremely close to the palm after the snap motion
        if (middlePinched && !isMiddlePinched(data)) {
            Pair<Finger, Float> maxPinch = data.rightAimState.getMaxFingerPinch();
            // If we're now at least barely pinching our index finger, and we aren't pinching our
            // middle finger much at all anymore, then we just did a snap.
            if (maxPinch.getFirst() == Finger.INDEX && maxPinch.getSecond() > 0.01f
                && closerToIndexThanMiddle(data)) {
                middlePinched = false;
                return true;
            }
        }
        middlePinched = isMiddlePinched(data);
        return false;
    }

    @Override
    public boolean performAction(Player player) {
        BlockPos blockTarget = null;
        LivingEntity entityTarget = null;
        HitResult hit = player.pick(50d, 1f, false);
        if (hit instanceof BlockHitResult bhr && bhr.getType() == HitResult.Type.BLOCK) {
            blockTarget = bhr.getBlockPos();
        }
        // Not-so-great raycast for entity detection
        Vec3 pos = VRPlugin.API.getVRPlayer(player).getHMD().position();
        Vec3 look = VRPlugin.API.getVRPlayer(player).getHMD().getLookAngle();
        for (int i = 0; i < 50; i++) {
            List<Entity> nearby = player.level().getEntities(player, AABB.ofSize(pos.add(look.scale(i)), 4, 4, 4),
                    (e) -> e instanceof LivingEntity);
            if (!nearby.isEmpty()) {
                entityTarget = (LivingEntity) nearby.get(0);
                break;
            }
        }
        if (blockTarget == null && entityTarget == null) {
            // TODO: Use translation string here.
            player.sendSystemMessage(Component.literal("No valid targets!"));
            return false;
        }
        boolean hitEntity = blockTarget == null || (entityTarget != null && player.distanceToSqr(entityTarget) < player.distanceToSqr(blockTarget.getX(), blockTarget.getY(), blockTarget.getZ()));

        LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, player.level());
        bolt.setVisualOnly(true);
        if (hitEntity) {
            bolt.setPos(entityTarget.position());
            DamageSource source = entityTarget.isInvulnerableTo(entityTarget.damageSources().lightningBolt()) ?
                    entityTarget.damageSources().genericKill() : entityTarget.damageSources().lightningBolt();
            entityTarget.hurt(source, entityTarget.getHealth() * 4f);
        } else {
            bolt.setPos(Vec3.atBottomCenterOf(blockTarget).add(0, 1, 0));
        }
        player.level().addFreshEntity(bolt);

        return true;
    }

    @Override
    public ResourceLocation getID() {
        return new ResourceLocation(VRHandControl.MOD_ID, "snap");
    }

    private boolean isMiddlePinched(VRData data) {
        Pair<Finger, Float> maxPinch = data.rightAimState.getMaxFingerPinch();
        return data.rightAimState.isPinching(Finger.MIDDLE) || (maxPinch.getFirst() == Finger.MIDDLE && maxPinch.getSecond() > 0.5);
    }

    private boolean closerToIndexThanMiddle(VRData data) {
        Vector3f thumb = data.getFingerJoint(Joint.THUMB_TIP, Hand.RIGHT).pose().position();
        Vector3f indexA = data.getFingerJoint(Joint.INDEX_TIP, Hand.RIGHT).pose().position();
        Vector3f indexB = data.getFingerJoint(Joint.INDEX_DISTAL, Hand.RIGHT).pose().position();
        Vector3f middle = data.getFingerJoint(Joint.MIDDLE_TIP, Hand.RIGHT).pose().position();
        return thumb.distanceSquared(indexA) < thumb.distanceSquared(middle) &&
                thumb.distanceSquared(indexB) < thumb.distanceSquared(middle);
    }
}
