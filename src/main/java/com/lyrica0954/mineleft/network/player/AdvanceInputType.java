package com.lyrica0954.mineleft.network.player;

// クライアントの vecX, vecZを受け入れるのとキー入力から取るモードを変更できるようにする
public class AdvanceInputType {

	public static class Vector {

		protected float vecX;

		protected float vecZ;

		public Vector(float vecX, float vecZ) {
			this.vecX = vecX;
			this.vecZ = vecZ;
		}

		public float getVecX() {
			return vecX;
		}

		public float getVecZ() {
			return vecZ;
		}
	}

	public static class Key {

		protected boolean sneaking;

		public Key(boolean sneaking) {
			this.sneaking = sneaking;
		}

		public boolean isSneaking() {
			return sneaking;
		}
	}
}
