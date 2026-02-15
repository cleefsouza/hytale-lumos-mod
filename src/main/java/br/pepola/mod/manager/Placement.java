package br.pepola.mod.manager;

public final class Placement {
    final int x, y, z;
    final int beforeId;
    final int beforeRot;
    final int beforeFiller;

    Placement(int x, int y, int z, int beforeId, int beforeRot, int beforeFiller) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.beforeId = beforeId;
        this.beforeRot = beforeRot;
        this.beforeFiller = beforeFiller;
    }
}
