#version 330 core

layout (location = 0) out vec4 color;

in DATA{
	vec2 tc;
} fs_in;

uniform sampler2D tex;
uniform sampler2D tex2;



void main()
{
	vec2 offset = (texture(tex2, fs_in.tc).rg - 0.5) * 2.0 * 0.1f;
	color = texture(tex, fs_in.tc + offset);
}
