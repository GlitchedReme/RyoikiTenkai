package sts.ryoikitenkai.modcore;

import com.badlogic.gdx.graphics.Texture;

import sts.ryoikitenkai.utils.Shader;

public class ShaderManager {
    public static Shader heatWave;
    public static int heatWaveTime;
    public static int heatWaveAmplitude;
    public static int heatWaveFrequency;
    public static int heatWaveSpeed;

    public static Shader mist;
    public static int mistNoise;
    public static int mistTime;
    public static int mistResolution;
    public static int mistCenterPosition;

    public static Texture noiseTexture;

    public static void init() {
        heatWave = new Shader("RyoikiTenkaiResources/shaders/heatwave.vert", "RyoikiTenkaiResources/shaders/heatwave.frag");
        heatWaveTime = heatWave.getUniformLocation("u_time");
        heatWaveAmplitude = heatWave.getUniformLocation("u_amplitude");
        heatWaveFrequency = heatWave.getUniformLocation("u_frequency");
        heatWaveSpeed = heatWave.getUniformLocation("u_speed");

        mist = new Shader("RyoikiTenkaiResources/shaders/mist.vert", "RyoikiTenkaiResources/shaders/mist.frag");
        mistNoise = mist.getUniformLocation("u_noiseTexture");
        mistTime = mist.getUniformLocation("u_time");
        mistResolution = mist.getUniformLocation("u_resolution");
        mistCenterPosition = mist.getUniformLocation("u_centerPosition");

        noiseTexture = new Texture("RyoikiTenkaiResources/images/noiseTexture.png");
        noiseTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }
}
