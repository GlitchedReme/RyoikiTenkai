#ifdef GL_ES
    precision mediump float;
#endif

//SpriteBatch will use texture unit 0
uniform sampler2D u_texture;
uniform sampler2D u_noiseTexture;

uniform vec2 u_resolution;
uniform vec2 u_centerPosition;
uniform float u_time;

//"in" varyings from our vertex shader
varying vec4 v_color;
varying vec2 v_texCoord;
varying vec4 v_position;
varying vec4 v_transPosition;

void main() {
    vec4 originalColor = texture2D(u_texture, v_texCoord) * v_color;
    vec2 ndc = v_transPosition.xy / v_transPosition.w;
    vec2 screenCoord = (ndc + 1.0) * 0.5 * u_resolution;
    vec2 noiseCoord = screenCoord / u_resolution * 2.0 + u_time * 0.01;
    float noise = texture2D(u_noiseTexture, noiseCoord).r;
    noise = smoothstep(0.3, 0.7, noise);

    vec3 purpleMist = vec3(0.4, 0.1, 0.7) * (0.5 + noise * 0.8);
    vec3 mistedColor = mix(originalColor.rgb, purpleMist, 0.25);

    gl_FragColor = vec4(mistedColor, originalColor.a);
}
