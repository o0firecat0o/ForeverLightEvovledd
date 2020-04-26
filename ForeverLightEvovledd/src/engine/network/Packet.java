package engine.network;

import java.nio.ByteBuffer;

import com.esotericsoftware.kryo.Kryo;

public class Packet {
	public static class Packet0LoginRequest {
	}

	public static class Packet1LoginAnswer {
		boolean accepted = false;
	}

	public static class Packet2Message {
		public String message;
	}

	public static class Packet3ByteBuffer {
		public ByteBuffer byteBuffer;
	}

	public static class Packet4Function {
		public int functionID;
		public Object[] objects;
	}

	public static void RegisterAll(Kryo kryo) {
		kryo.register(Packet0LoginRequest.class);
		kryo.register(Packet2Message.class);
		kryo.register(Packet4Function.class);
		kryo.register(ByteBuffer.class);
		kryo.register(byte[].class);
		kryo.register(String.class);
		kryo.register(Object[].class);
		kryo.register(org.joml.Vector2f.class);
	}
}
