package com.wynprice.secretroomsmod.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.init.Blocks;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;

/**
 * Custom {@link IRenderChunkFactory} used to create my own {@link ChunkCache}
 * @author Wyn Price
 *
 */
public class FakeChunkRenderFactory implements IRenderChunkFactory 
{
	private final IRenderChunkFactory base;
	
	public FakeChunkRenderFactory(IRenderChunkFactory base) 
	{
		this.base = base;
	}
	
	@Override
	public RenderChunk create(World worldIn, RenderGlobal renderGlobalIn, int index) {
		return OpenGlHelper.useVbo() ? new FakeRenderChunk(worldIn, renderGlobalIn, index, base.create(worldIn, renderGlobalIn, index)) : new FakeRenderChunkListed(worldIn, renderGlobalIn, index, base.create(worldIn, renderGlobalIn, index));
	}

}
