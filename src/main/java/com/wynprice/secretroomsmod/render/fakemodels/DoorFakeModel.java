package com.wynprice.secretroomsmod.render.fakemodels;

import com.wynprice.secretroomsmod.base.BaseBlockDoor;
import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;

/**
 * Fake model for the door
 * @author Wyn Price
 *
 */
public class DoorFakeModel extends BaseTextureFakeModel
{

	public DoorFakeModel(FakeBlockModel model) {
		super(model);
	}

	@Override
	public IBlockState getNormalStateWith(IBlockState s, IBlockState mirrorState) {
		return Blocks.DARK_OAK_DOOR.getDefaultState().withProperty(BlockDoor.FACING, s.getValue(BlockDoor.FACING)).withProperty(BlockDoor.HALF, s.getValue(BlockDoor.HALF))
				.withProperty(BlockDoor.HINGE, s.getValue(BlockDoor.HINGE)).withProperty(BlockDoor.OPEN, s.getValue(BlockDoor.OPEN)).withProperty(BlockDoor.POWERED, s.getValue(BlockDoor.POWERED));
	}

	@Override
	protected Class<? extends ISecretBlock> getBaseBlockClass() {
		return BaseBlockDoor.class;
	}
}
