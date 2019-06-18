package io.gpm.mazerunner.impl;

import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.World;

/***
 * @author George
 * @since 19-Jun-19
 */
public class CustomArmorstand extends EntityArmorStand {
    public CustomArmorstand(World world) {
        super(world);
        this.setGravity(false);
        this.g(0, 0);
    }
    @Override
    public void g(float f, float f1) {
        if(!(this.hasGravity()))
            super.g(f, f1);
        else {
            move(motX, motY, motZ);
        }
    }
}
