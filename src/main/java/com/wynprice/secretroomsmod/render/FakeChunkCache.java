package com.wynprice.secretroomsmod.render;

import java.util.ArrayList;

import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;
import com.wynprice.secretroomsmod.handler.EnergizedPasteHandler;
import com.wynprice.secretroomsmod.items.TrueSightHelmet;
import com.wynprice.secretroomsmod.network.SecretNetwork;
import com.wynprice.secretroomsmod.network.packets.MessagePacketEnergizedPaste;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk.EnumCreateEntityType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Fake chunk cache. Used to help seemless rendering of SRM blocks and to make Energized Paste work
 * @author Wyn Price
 *
 */
public class FakeChunkCache extends ChunkCache
{
	
	private final ChunkCache oldCache;
	
	private final BlockPos fromPos;
	private final BlockPos toPos;
	private final int sub;
	
	public FakeChunkCache(World world, BlockPos from, BlockPos to, int sub, ChunkCache oldCache)
	{
		super(world, from, to, sub);
		this.oldCache = oldCache;
		this.fromPos = from;
		this.toPos = to;
		this.sub = sub;
	}
	
	@Override
	public boolean isEmpty() 
	{
		return oldCache.isEmpty();
	}
	
	@Override
	public Biome getBiome(BlockPos pos) 
	{
		return oldCache.getBiome(pos);
	}
			
	public int prevInt = -1;
	
	@Override
	public IBlockState getBlockState(BlockPos pos) 
	{
		if(!(super.getBlockState(pos).getBlock() instanceof ISecretBlock) && EnergizedPasteHandler.hasReplacedState(world, pos) && !(Minecraft.getMinecraft().player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof TrueSightHelmet))
		{
			if(EnergizedPasteHandler.getSetBlockState(world, pos).getBlock() != super.getBlockState(pos).getBlock()
					|| EnergizedPasteHandler.getSetBlockState(world, pos).getBlock().getMetaFromState(EnergizedPasteHandler.getSetBlockState(world, pos)) != super.getBlockState(pos).getBlock().getMetaFromState(super.getBlockState(pos)))
			{
				//EnergizedPasteHandler.removeReplacedState(world.provider.getDimension(), pos);
				SecretNetwork.sendToServer(new MessagePacketEnergizedPaste(pos, false));
			}
			else
				return EnergizedPasteHandler.getReplacedState(world, pos);
		}
		if(oldCache.getTileEntity(pos) instanceof ISecretTileEntity) {
			return ((ISecretTileEntity)oldCache.getTileEntity(pos)).getMirrorStateSafely();
		}
		return oldCache.getBlockState(pos);
	}	
	
	@Override
	public int getCombinedLight(BlockPos pos, int lightValue) 
	{
		int i = this.getLightForExt(EnumSkyBlock.SKY, pos);
        int j = this.getLightForExt(EnumSkyBlock.BLOCK, pos);

        if (j < lightValue)
        {
            j = lightValue;
        }

        return i << 20 | j << 4;
	}
	
	@SideOnly(Side.CLIENT)
    private int getLightForExt(EnumSkyBlock type, BlockPos pos)
    {
        if (type == EnumSkyBlock.SKY && !this.world.provider.hasSkyLight())
        {
            return 0;
        }
        else if (pos.getY() >= 0 && pos.getY() < 256)
        {
            if (this.getBlockState(pos).useNeighborBrightness() || EnergizedPasteHandler.hasReplacedState(world, pos))
            {
                int l = 0;

                for (EnumFacing enumfacing : EnumFacing.values())
                {
                    int k = this.getLightFor(type, pos.offset(enumfacing));

                    if (k > l)
                    {
                        l = k;
                    }

                    if (l >= 15)
                    {
                        return l;
                    }
                }

                return l;
            }
            else
            {
                int i = (pos.getX() >> 4) - this.chunkX;
                int j = (pos.getZ() >> 4) - this.chunkZ;
                return i >= 0 && i < chunkArray.length && j >= 0 && j < chunkArray[i].length && chunkArray[i][j] != null ? this.chunkArray[i][j].getLightFor(type, pos) : type.defaultLightValue;
            }
        }
        else
        {
            return type.defaultLightValue;
        }
    }
	
	@Override
	public int getLightFor(EnumSkyBlock type, BlockPos pos) 
	{
		return oldCache.getLightFor(type, pos);
	}
	
	@Override
	public int getStrongPower(BlockPos pos, EnumFacing direction) 
	{
		return oldCache.getStrongPower(pos, direction);
	}
	
	@Override
	public TileEntity getTileEntity(BlockPos pos) 
	{
		return oldCache.getTileEntity(pos);
	}
	
	@Override
	public TileEntity getTileEntity(BlockPos pos, EnumCreateEntityType p_190300_2_)
	{
		return oldCache.getTileEntity(pos, p_190300_2_);
	}
	
	@Override
	public WorldType getWorldType() 
	{
		return oldCache.getWorldType();
	}
	
	@Override
	public boolean isAirBlock(BlockPos pos) 
	{
		return oldCache.isAirBlock(pos);
	}
	
	@Override
	public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) {
		return oldCache.isSideSolid(pos, side, _default);
	}
}