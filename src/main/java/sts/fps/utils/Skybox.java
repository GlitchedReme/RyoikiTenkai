package sts.fps.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class Skybox implements Disposable {
    public Matrix4 transformation;
    ShaderProgram program;
    int u_projTrans;
    int u_worldTrans;
    int u_texture;

    Texture texture;

    Mesh quad;

    static final String VERTEX_SHADER = "#version 330 core\n"
            + "in vec3 a_position;\n"
            + "in vec2 a_texCoord;\n"
            + "out vec2 v_texCoord;\n"
            + "uniform mat4 u_projTrans;\n"
            + "uniform mat4 u_worldTrans;\n"
            + "void main() {\n"
            + "    v_texCoord = a_texCoord;\n"
            + "    gl_Position = u_projTrans * u_worldTrans * vec4(a_position, 1.0);\n"
            + "}\n";

    static final String FRAGMENT_SHADER = "#version 330 core\n"
            + "in vec2 v_texCoord;\n"
            + "out vec4 fragColor;\n"
            + "uniform sampler2D u_texture;\n"
            + "void main() {\n"
            + "    vec3 color = texture(u_texture, v_texCoord).rgb;\n"
            + "    fragColor = vec4(color, 1.0);\n"
            + "}\n";

    public Skybox() {
        init();
    }

    public static Mesh createCylinder(float height, float radius, float arcRadians, int divisions) {
        int numVertices = (divisions + 1) * 2 * 5; // 3 for position + 2 for texCoord
        int numIndices = divisions * 6;

        float[] vertices = new float[numVertices];
        short[] indices = new short[numIndices];

        int idx = 0;
        int idxIdx = 0;

        for (int i = 0; i <= divisions; i++) {
            float angle = (float) i / divisions * arcRadians - arcRadians / 2;
            float x = MathUtils.cos(angle) * radius;
            float z = MathUtils.sin(angle) * radius;
            float u = (float) i / divisions;

            // Bottom circle
            vertices[idx++] = x;
            vertices[idx++] = 0f;
            vertices[idx++] = z;
            vertices[idx++] = u;
            vertices[idx++] = 0f;

            // Top circle
            vertices[idx++] = x;
            vertices[idx++] = height;
            vertices[idx++] = z;
            vertices[idx++] = u;
            vertices[idx++] = 1f;
        }

        for (int i = 0; i < divisions; i++) {
            int p0 = i * 2;
            int p1 = p0 + 1;
            int p2 = p0 + 2;
            int p3 = p0 + 3;

            // First triangle
            indices[idxIdx++] = (short) p0;
            indices[idxIdx++] = (short) p2;
            indices[idxIdx++] = (short) p1;

            // Second triangle
            indices[idxIdx++] = (short) p2;
            indices[idxIdx++] = (short) p3;
            indices[idxIdx++] = (short) p1;
        }

        Mesh mesh = new Mesh(true, numVertices / 5, numIndices,
                new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"),
                new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, "a_texCoord"));

        mesh.setVertices(vertices);
        mesh.setIndices(indices);

        return mesh;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void init() {
        program = new ShaderProgram(VERTEX_SHADER, FRAGMENT_SHADER);
        if (!program.isCompiled())
            throw new GdxRuntimeException(program.getLog());
        u_projTrans = program.getUniformLocation("u_projTrans");
        u_worldTrans = program.getUniformLocation("u_worldTrans");
        u_texture = program.getUniformLocation("u_texture");

        transformation = new Matrix4();

        quad = createCylinder(3400f, 2400f, MathUtils.PI * 0.75f, 48);
    }

    public void render(Camera camera) {
        if (this.texture == null)
            return;

        Gdx.gl20.glCullFace(GL20.GL_BACK);

        program.begin();
        program.setUniformMatrix(u_projTrans, camera.combined);

        transformation.idt();
        transformation.translate(camera.position);
        transformation.rotate(Vector3.Y, 250);
        transformation.translate(0, -800, 0);
        program.setUniformMatrix(u_worldTrans, transformation);
        texture.bind(0);
        program.setUniformi(u_texture, 0);
        quad.render(program, GL20.GL_TRIANGLES);

        program.end();
    }

    @Override
    public void dispose() {
        program.dispose();
        quad.dispose();
    }
}
