package dev.mCraft.Coinz.Blocks;

import org.getspout.spoutapi.block.design.GenericCuboidBlockDesign;

import dev.mCraft.Coinz.Coinz;

public class Design extends GenericCuboidBlockDesign {

	public Design(int[] textureId) {
		super(Coinz.instance, Blocks.texture, textureId, 0, 0, 0, 1, 1, 1);
	}

}
