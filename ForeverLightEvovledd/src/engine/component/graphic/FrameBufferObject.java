package engine.component.graphic;

import static org.lwjgl.opengl.EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_FRAMEBUFFER_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_RENDERBUFFER_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glBindFramebufferEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glBindRenderbufferEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glFramebufferRenderbufferEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glFramebufferTexture2DEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glGenFramebuffersEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glGenRenderbuffersEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glRenderbufferStorageEXT;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

public class FrameBufferObject {

	public int FrameBufferID;
	public int colorTextureID;
	public int depthTextureID;
	public int depthRenderBufferID;
	public int Width;
	public int Height;

	public FrameBufferObject(int Width, int Height) {
		this.Width = Width;
		this.Height = Height;

		// check if FBO is enable
		boolean FBOEnabled = GL.createCapabilities().GL_EXT_framebuffer_object;
		System.out.println("FBO capabilities is " + FBOEnabled);

		FrameBufferID = glGenFramebuffersEXT(); // create a new framebuffer
		colorTextureID = glGenTextures(); // And finally a new depthbuffer
		depthTextureID = glGenTextures();
		depthRenderBufferID = glGenRenderbuffersEXT(); // And finally a new
														// depthbuffer

		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, FrameBufferID); // switch to
																	// the new
																	// framebuffer

		// initialize color texture
		glBindTexture(GL_TEXTURE_2D, colorTextureID); // Bind the colorbuffer
														// texture
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR); // make
																			// it
																			// linear
																			// filterd
		glTexImage2D(GL_TEXTURE_2D, 0, GL11.GL_RGBA, Width, Height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
				(java.nio.ByteBuffer) null); // Create the texture data

		glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D, colorTextureID, 0); // attach

		// initialize depth texture
		glBindTexture(GL_TEXTURE_2D, depthTextureID);

		glTexImage2D(GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT24, Width, Height, 0, GL11.GL_DEPTH_COMPONENT,
				GL11.GL_UNSIGNED_BYTE, (java.nio.ByteBuffer) null);

		glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_TEXTURE_2D, depthTextureID, 0);

		// initialize depth renderbuffer
		glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, depthRenderBufferID); // bind
																			// the
																			// depth
																			// renderbuffer
		glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL14.GL_DEPTH_COMPONENT24, Width, Height); // get
		// the
		// data
		// space
		// for
		// it
		glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT,
				depthRenderBufferID); // bind it to the renderbuffer

		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0); // Swithch back to normal
														// framebuffer rendering
	}

	public void bind_clear() {
		glViewport(0, 0, Width, Height);
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, FrameBufferID);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public void bind() {
		glViewport(0, 0, Width, Height);
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, FrameBufferID);
		glClear(GL_DEPTH_BUFFER_BIT);
	}
}
