#version 330 core

layout (location = 0) out vec4 color;

in DATA{
	vec2 tc;
} fs_in;

uniform sampler2D tex;

void main()
{
	color = texture(tex, vec2(fs_in.tc.x,1-fs_in.tc.y));
	color.a = color.a-0.002f;
}
