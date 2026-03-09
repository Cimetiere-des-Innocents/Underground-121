#version 150

uniform float ModTime;
uniform vec2 ScreenSize;
uniform float size;
uniform float speed;

in vec2 vertexUV;
out vec4 fragColor;

// --- 工具函数：生成伪随机哈希值 ---
float hash(vec2 p) {
    return fract(sin(dot(p, vec2(12.9898, 78.233))) * 43758.5453123);
}

// --- 工具函数：2D 平滑噪声 ---
float noise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    vec2 u = f * f * (3.0 - 2.0 * f);

    float a = hash(i + vec2(0.0, 0.0));
    float b = hash(i + vec2(1.0, 0.0));
    float c = hash(i + vec2(0.0, 1.0));
    float d = hash(i + vec2(1.0, 1.0));

    return mix(mix(a, b, u.x), mix(c, d, u.x), u.y);
}

// --- 工具函数：分形布朗运动 (FBM) ---
float fbm(vec2 p) {
    float value = 0.0;
    float amplitude = 0.5;
    for (int i = 0; i < 4; i++) {
        value += amplitude * noise(p);
        p *= 2.0;
        amplitude *= 0.5;
    }
    return value;
}

void main() {
    // ==========================================
    // 像素化 UV 处理 (保留 1/16 像素化效果)
    // ==========================================
    float pixelScale = 16.0;
    vec2 macroPixels = floor(ScreenSize / pixelScale);
    macroPixels = max(macroPixels, vec2(1.0));
    vec2 pixelatedUV = (floor(vertexUV * macroPixels) + 0.5) / macroPixels;

    // ------------------------------------------
    // 1. 计算距离边缘的距离
    vec2 pixelPos = pixelatedUV * ScreenSize;
    float distX = min(pixelPos.x, ScreenSize.x - pixelPos.x);
    float distY = min(pixelPos.y, ScreenSize.y - pixelPos.y);
    float minEdgeDist = min(distX, distY);

    // 2. 根据 size 换算火焰的最大厚度
    float maxThickness = min(ScreenSize.x, ScreenSize.y) * 0.5 * size;

    if (minEdgeDist > maxThickness || maxThickness <= 0.0) {
        fragColor = vec4(0.0);
        return;
    }

    // 3. 基础边缘渐变 (越靠外侧越接近 1.0，越靠内侧越接近 0.0)
    float gradient = 1.0 - (minEdgeDist / maxThickness);

    // ==========================================
    // 核心修改：大幅度增强火焰的起伏
    // ==========================================

    // [修改1] 降低 UV 缩放倍数 (从 15.0 降到 6.0)
    // 这样可以拉扯噪声，让火焰的“火舌”变得更宽大，而不是细碎的波纹。
    vec2 noiseUV = pixelatedUV * vec2(6.0 * (ScreenSize.x / ScreenSize.y), 6.0);

    float steppedTime = floor(ModTime * 20.0) / 20.0; // 模拟每秒20帧
    float timeOffset = steppedTime * speed;
    vec2 scroll = vec2(timeOffset * 0.5, -timeOffset * 1.5);

    float n = fbm(noiseUV + scroll);
    float n2 = fbm(noiseUV * 2.0 - scroll * 1.2);

    // [修改2] 增强噪声的对比度
    // 使用 smoothstep 掐头去尾，让深谷更深，高峰更高
    float combinedNoise = (n + n2) * 0.5;
    combinedNoise = smoothstep(0.1, 0.85, combinedNoise);

    // [修改3] 增大噪声对渐变的“侵蚀”力度 (Amplitude)
    // 增加 amplitude 乘数（如 2.5），让噪声在内部狠狠地“挖”去大块的区域
    // pow(1.0 - gradient, 0.6) 是为了保证最边缘处 (gradient=1) 不被侵蚀，保持边框闭合
    float amplitude = 2.5;
    float flameIntensity = gradient - (combinedNoise * amplitude * pow(1.0 - gradient, 0.6));

    // 6. 颜色映射 (微调了颜色过渡的位置，配合更大的起伏)
    vec4 color = vec4(0.0);
    if (flameIntensity > 0.0) {
        vec3 colDarkRed = vec3(0.5, 0.0, 0.0);
        vec3 colRed     = vec3(1.0, 0.1, 0.0);
        vec3 colOrange  = vec3(1.0, 0.6, 0.0);
        vec3 colYellow  = vec3(1.0, 0.9, 0.2);

        vec3 finalColor = mix(colDarkRed, colRed, smoothstep(0.0, 0.25, flameIntensity));
        finalColor = mix(finalColor, colOrange, smoothstep(0.25, 0.6, flameIntensity));
        finalColor = mix(finalColor, colYellow, smoothstep(0.6, 1.0, flameIntensity));

        // 边缘透明度渐变
        float alpha = smoothstep(0.0, 0.2, flameIntensity);
        color = vec4(finalColor, alpha);
    }

    fragColor = color;
}