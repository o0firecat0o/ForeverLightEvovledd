#version 330 core

layout (location = 0) out vec4 color;

in DATA{
	vec2 tc;
} fs_in;

uniform sampler2D tex;
uniform sampler2D tex2;

uniform float time = 0;
uniform float flameScale = 1;


void main()
{
	color.r = texture(tex2, fract(fs_in.tc*flameScale +vec2(0.0,time*0.2))).r;
	color.g = texture(tex2, fract(fs_in.tc*flameScale*3.4 +vec2(0.0,time*0.3))).g*1.3;
	color.a = texture(tex, fs_in.tc).a;
}
