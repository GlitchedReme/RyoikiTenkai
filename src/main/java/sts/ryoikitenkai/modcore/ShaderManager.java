package sts.ryoikitenkai.modcore;

import sts.ryoikitenkai.utils.Shader;

public class ShaderManager {
    public static Shader heatWave;
    public static int heatWaveTime;
    public static int heatWaveAmplitude;
    public static int heatWaveFrequency;
    public static int heatWaveSpeed;

    public static void init() {
        heatWave = new Shader("RyoikiTenkaiResources/shaders/heatwave.vert", "RyoikiTenkaiResources/shaders/heatwave.frag");
        heatWaveTime = heatWave.getUniformLocation("u_time");
        heatWaveAmplitude = heatWave.getUniformLocation("u_amplitude");
        heatWaveFrequency = heatWave.getUniformLocation("u_frequency");
        heatWaveSpeed = heatWave.getUniformLocation("u_speed");
    }
}
