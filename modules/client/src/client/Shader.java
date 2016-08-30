package client;

import java.io.*;
import java.util.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;

import utilities.log.*;

public class Shader {

	/** Logging */
	public static Log log = LogFactory.getLog(Shader.class);
	
	private GL gl ;
	
	private GLU glu;
	
	public Shader(GL gl, GLU glu) {
		this.gl = gl;
	}
	
	public void getShader(String vertexShaderPath, String fragmentShaderPath) throws GLException {
		int vertexShader = gl.glCreateShaderObjectARB(GL.GL_VERTEX_SHADER_ARB);
		int fragmentShader = gl.glCreateShaderObjectARB(GL.GL_FRAGMENT_SHADER_ARB);

		gl.glShaderSourceARB(vertexShader, 1, readShader(vertexShaderPath), (int[])null, 0);
		gl.glCompileShaderARB(vertexShader);
		System.out.println(getShaderLog(vertexShader));
		
		gl.glShaderSourceARB(fragmentShader, 1, readShader(fragmentShaderPath), (int[])null, 0);
		gl.glCompileShaderARB(fragmentShader);
		System.out.println(getShaderLog(fragmentShader));
		
		int shaderProgram = gl.glCreateProgramObjectARB();
		gl.glAttachObjectARB(shaderProgram, vertexShader);
		gl.glAttachObjectARB(shaderProgram, fragmentShader);
		gl.glLinkProgramARB(shaderProgram);
		gl.glUseProgramObjectARB(shaderProgram); 
		
		getGlErrorLog();		
	}
	
	private String[] readShader(String file) {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
			List<String> shaderBuffer = new ArrayList<String>();
			String textLine;
			while ((textLine = bufferedReader.readLine()) != null) {
				shaderBuffer.add(textLine + "\n");
			}
			String[] vsrcAr = new String[shaderBuffer.size()];
			Iterator<String> it = shaderBuffer.iterator();
			for(int i = 0; it.hasNext(); i++) {
				vsrcAr[i] = new String(it.next());
			}
			return vsrcAr;
		} catch(IOException e) {
			log.error("Fehler bei laden des Shaders: " + file, e);
			return null;
		}
	}
	
	private void getGlErrorLog() {
		int glError;
		
	    while ((glError = gl.glGetError()) != GL.GL_NO_ERROR) {
	        log.error("getShader() " + glu.gluErrorString(glError));
	    }
	}
	
	private String getShaderLog(int shader) {
		StringBuilder str = new StringBuilder();
		int infologLength[] = new int[255];
	    byte charsWritten[] = new byte[255];

		gl.glGetObjectParameterivARB(shader, GL.GL_OBJECT_INFO_LOG_LENGTH_ARB,	infologLength, 0);
		gl.glGetInfoLogARB(shader, infologLength.length, infologLength, 0, charsWritten, 0);
	   		
		for(byte b : charsWritten) {
			str.append((char)b);
		}
		return str.toString();
	}
}
