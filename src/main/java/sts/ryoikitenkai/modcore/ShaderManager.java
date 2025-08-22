package sts.ryoikitenkai.modcore;

import com.badlogic.gdx.graphics.Texture;

import sts.ryoikitenkai.utils.Shader;

public class ShaderManager {
    public static final String heatWaveVert = "attribute vec4 a_position;" +
            "attribute vec4 a_color;" +
            "attribute vec2 a_texCoord0;" +
            "" +
            "uniform mat4 u_projTrans;" +
            "" +
            "varying vec4 v_color;" +
            "varying vec2 v_texCoords;" +
            "" +
            "void main() {" +
            "    v_color = a_color;" +
            "    v_texCoords = a_texCoord0;" +
            "    gl_Position = u_projTrans * a_position;" +
            "}";

    public static final String heatWaveFrag = "#ifdef GL_ES\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "\n" +
            "varying vec4 v_color;\n" +
            "varying vec2 v_texCoords;\n" +
            "\n" +
            "uniform sampler2D u_texture;\n" +
            "uniform float u_time;\n" +
            "\n" +
            "uniform float u_amplitude;\n" +
            "uniform float u_frequency;\n" +
            "uniform float u_speed;\n" +
            "\n" +
            "void main() {\n" +
            "    float offset = sin((v_texCoords.x + v_texCoords.y) * u_frequency + u_time * u_speed) * u_amplitude;\n" +
            "    vec2 distortedCoords = v_texCoords + offset;\n" +
            "    vec4 texColor = texture2D(u_texture, distortedCoords);\n" +
            "    gl_FragColor = texColor * v_color;\n" +
            "}";

    public static Shader heatWave;
    public static int heatWaveTime;
    public static int heatWaveAmplitude;
    public static int heatWaveFrequency;
    public static int heatWaveSpeed;

    public static final String mistVert = "uniform mat4 u_projTrans;" +
            "" +
            "attribute vec4 a_position;" +
            "attribute vec2 a_texCoord0;" +
            "attribute vec4 a_color;" +
            "" +
            "varying vec4 v_color;" +
            "varying vec2 v_texCoord;" +
            "varying vec4 v_position;" +
            "varying vec4 v_transPosition;" +
            "" +
            "uniform vec2 u_viewportInverse;" +
            "" +
            "void main() {" +
            "    v_position = a_position;" +
            "    gl_Position = v_transPosition = u_projTrans * a_position;" +
            "    v_texCoord = a_texCoord0;" +
            "    v_color = a_color;" +
            "}";

    public static final String mistFrag = "#ifdef GL_ES\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "\n" +
            "//SpriteBatch will use texture unit 0\n" +
            "uniform sampler2D u_texture;\n" +
            "uniform sampler2D u_noiseTexture;\n" +
            "\n" +
            "uniform vec2 u_resolution;\n" +
            "uniform vec2 u_centerPosition;\n" +
            "uniform float u_time;\n" +
            "\n" +
            "//\"in\" varyings from our vertex shader\n" +
            "varying vec4 v_color;\n" +
            "varying vec2 v_texCoord;\n" +
            "varying vec4 v_position;\n" +
            "varying vec4 v_transPosition;\n" +
            "\n" +
            "void main() {\n" +
            "    vec4 originalColor = texture2D(u_texture, v_texCoord) * v_color;\n" +
            "    vec2 ndc = v_transPosition.xy / v_transPosition.w;\n" +
            "    vec2 screenCoord = (ndc + 1.0) * 0.5 * u_resolution;\n" +
            "    vec2 noiseCoord = screenCoord / u_resolution * 2.0 + u_time * 0.01;\n" +
            "    float noise = texture2D(u_noiseTexture, noiseCoord).r;\n" +
            "    noise = smoothstep(0.3, 0.7, noise);\n" +
            "\n" +
            "    vec3 purpleMist = vec3(0.4, 0.1, 0.7) * (0.5 + noise * 0.8);\n" +
            "    vec3 mistedColor = mix(originalColor.rgb, purpleMist, 0.25);\n" +
            "\n" +
            "    gl_FragColor = vec4(mistedColor, originalColor.a);\n" +
            "}";

    public static Shader mist;
    public static int mistNoise;
    public static int mistTime;
    public static int mistResolution;
    public static int mistCenterPosition;

    public static Texture noiseTexture;

    public static void init() {
        heatWave = new Shader(heatWaveVert, heatWaveFrag);
        heatWaveTime = heatWave.getUniformLocation("u_time");
        heatWaveAmplitude = heatWave.getUniformLocation("u_amplitude");
        heatWaveFrequency = heatWave.getUniformLocation("u_frequency");
        heatWaveSpeed = heatWave.getUniformLocation("u_speed");

        mist = new Shader(mistVert, mistFrag);
        mistNoise = mist.getUniformLocation("u_noiseTexture");
        mistTime = mist.getUniformLocation("u_time");
        mistResolution = mist.getUniformLocation("u_resolution");
        mistCenterPosition = mist.getUniformLocation("u_centerPosition");

        noiseTexture = new Texture("RyoikiTenkaiResources/images/noiseTexture.png");
        noiseTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }
}
