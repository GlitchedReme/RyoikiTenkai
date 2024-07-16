package utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.GroupStrategy;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import java.util.Comparator;

public class CustomGroupStrategy implements GroupStrategy, Disposable {
    private PerspectiveCamera camera;
    ShaderProgram shader;

    private final Comparator<Decal> comparator = (o1, o2) -> {
        float dist1 = camera.position.dst(o1.getPosition());
        float dist2 = camera.position.dst(o2.getPosition());
        return (int)Math.signum(dist2 - dist1);
    };

    public CustomGroupStrategy(PerspectiveCamera camera) {
        this.camera = camera;
        this.createDefaultShader();
    }

    @Override
    public ShaderProgram getGroupShader(int i) {
        return this.shader;
    }

    @Override
    public int decideGroup(Decal decal) {
        return decal.getTextureRegion().getTexture().hashCode();
    }

    @Override
    public void beforeGroup(int i, Array<Decal> array) {
        Gdx.gl.glEnable(GL20.GL_TEXTURE_2D);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        array.sort(comparator);
    }

    @Override
    public void afterGroup(int i) {
        Gdx.gl.glDisable(GL20.GL_BLEND);
        Gdx.gl.glDisable(GL20.GL_TEXTURE_2D);
    }

    @Override
    public void beforeGroups() {
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        this.shader.begin();
        this.shader.setUniformMatrix("u_projectionViewMatrix", this.camera.combined);
        this.shader.setUniformi("u_texture", 0);
    }

    @Override
    public void afterGroups() {
        this.shader.end();
        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
    }

    private void createDefaultShader() {
        String vertexShader = "attribute vec4 a_position;\nattribute vec4 a_color;\nattribute vec2 a_texCoord0;\nuniform mat4 u_projectionViewMatrix;\nvarying vec4 v_color;\nvarying vec2 v_texCoords;\n\nvoid main()\n{\n   v_color = a_color;\n   v_texCoords = a_texCoord0;\n   gl_Position =  u_projectionViewMatrix * a_position;\n}\n";
        String fragmentShader = "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec4 v_color;\nvarying vec2 v_texCoords;\nuniform sampler2D u_texture;\nvoid main()\n{\n  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n}";
        this.shader = new ShaderProgram(vertexShader, fragmentShader);
        if (!this.shader.isCompiled()) {
            throw new IllegalArgumentException("couldn't compile shader: " + this.shader.getLog());
        }
    }

    @Override
    public void dispose() {
        if (this.shader != null) {
            this.shader.dispose();
        }
    }
}
