#version 330 core

layout (location = 0) out vec4 color;

in DATA{
	vec2 tc;
	vec4 col;
} fs_in;

uniform sampler2D tex;

void main()
{
	color.rgb = texture(tex, fs_in.tc).rgb*fs_in.col.rgb;
	color.a = texture(tex, fs_in.tc).a;

	if(color.a<0.1){
		discard;
	}
}
