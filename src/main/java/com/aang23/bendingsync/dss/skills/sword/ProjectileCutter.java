package com.aang23.bendingsync.dss.skills.sword;

import dynamicswordskills.skills.SkillActive;
import dynamicswordskills.skills.SkillBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ProjectileCutter extends SkillActive {

	protected ProjectileCutter(String name) {
		super(name);
	}

	@Override
	public boolean isActive() {
		return false;
	}

	@Override
	protected float getExhaustion() {
		return 0;
	}

	@Override
	protected boolean onActivated(World world, EntityPlayer entityPlayer) {
		return false;
	}

	@Override
	protected void onDeactivated(World world, EntityPlayer entityPlayer) {

	}

	@Override
	public SkillBase newInstance() {
		return null;
	}
}
