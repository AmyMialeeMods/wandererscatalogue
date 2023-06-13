package xyz.amymialee.wandererscatalogue.cca;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.NbtCompound;

public class SellerComponent implements AutoSyncedComponent {
	private boolean hasBeenTradedWith = false;

	public boolean isHasBeenTradedWith() {
		return this.hasBeenTradedWith;
	}

	public void setHasBeenTradedWith(boolean hasBeenTradedWith) {
		this.hasBeenTradedWith = hasBeenTradedWith;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		this.hasBeenTradedWith = tag.getBoolean("hasBeenTradedWith");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putBoolean("hasBeenTradedWith", this.hasBeenTradedWith);
	}
}