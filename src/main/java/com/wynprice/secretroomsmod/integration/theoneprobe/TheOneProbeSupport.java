package com.wynprice.secretroomsmod.integration.theoneprobe;

import mcjty.theoneprobe.api.ITheOneProbe;

public class TheOneProbeSupport implements com.google.common.base.Function<ITheOneProbe, Void> 
{
	@Override
	public Void apply(ITheOneProbe input) 
	{
		input.registerBlockDisplayOverride(new FakeInfoProvider());
		return null;
	}

}
