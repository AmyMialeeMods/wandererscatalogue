package xyz.amymialee.wandererscatalogue.cca;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.NbtCompound;

public class CustomerComponent implements AutoSyncedComponent {
	private int currentOrder = -1;

	public int getCurrentOrder() {
		return this.currentOrder;
	}

	public void setCurrentOrder(int currentOrder) {
		this.currentOrder = currentOrder;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		this.currentOrder = tag.getInt("currentOrder");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putInt("currentOrder", this.currentOrder);
	}
}