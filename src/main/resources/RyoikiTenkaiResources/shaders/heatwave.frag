#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform float u_time;

uniform float u_amplitude;
uniform float u_frequency;
uniform float u_speed;

void main() {
    // 1. 计算一个偏移值。通过使用坐标之和 (v_texCoords.x + v_texCoords.y)，
    //    我们创建了沿对角线方向传播的波浪。
    float offset = sin((v_texCoords.x + v_texCoords.y) * u_frequency + u_time * u_speed) * u_amplitude;

    // 2. 将这个偏移值同时应用到 x 和 y 坐标上。
    //    这会使像素沿对角线方向移动，从而产生斜向震动效果。
    vec2 distortedCoords = v_texCoords + offset;

    // 使用扭曲后的新坐标对纹理进行采样
    vec4 texColor = texture2D(u_texture, distortedCoords);

    // 与顶点颜色（主要用于控制透明度）相乘
    gl_FragColor = texColor * v_color;
}