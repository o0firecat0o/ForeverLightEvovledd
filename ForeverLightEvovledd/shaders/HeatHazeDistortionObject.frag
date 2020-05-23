#version 330 core

layout (location = 0) out vec4 color;

in DATA{
	vec2 tc;
} fs_in;

uniform sampler2D tex;
uniform float alpha;

void main()
{
	vec3 basecolor = texture(tex, fs_in.tc).rgb;
	basecolor.rgb = mix(vec3(0.5,0.5,1.0), basecolor.rgb, alpha);
	color = vec4(basecolor.rgb, 1.0);

}
