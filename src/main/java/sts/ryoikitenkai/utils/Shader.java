package sts.ryoikitenkai.utils;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Shader {
    private ShaderProgram shaderProgram;

    public Shader(String vertexFile, String fragmentFile) {
        shaderProgram = new ShaderProgram(vertexFile, fragmentFile);

        if (!shaderProgram.isCompiled()) {
            throw new IllegalArgumentException("Shader program failed to compile: " + shaderProgram.getLog());
        }
    }

    public ShaderProgram getShaderProgram() {
        return shaderProgram;
    }

    public int getUniformLocation(String name) {
        return shaderProgram.getUniformLocation(name);
    }

}
