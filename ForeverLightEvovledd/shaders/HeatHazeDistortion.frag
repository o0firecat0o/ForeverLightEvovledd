#version 330 core

layout (location = 0) out vec4 color;

in DATA{
	vec2 tc;
} fs_in;

uniform sampler2D tex;
uniform sampler2D tex2;



void main()
{
	vec2 offset;
	vec4 offsetcal = texture(tex2, fs_in.tc);
	//offsetcal.a is the strength of the heathaze
	offset.x = offsetcal.r * offsetcal.a * 0.01;
	offset.y = offsetcal.g * offsetcal.a * 0.01;
	color = texture(tex, fs_in.tc + offset);
}
