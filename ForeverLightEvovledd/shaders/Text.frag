#version 330 core

layout (location = 0) out vec4 color;

in DATA{
	vec2 tc;
	vec4 col;
	vec2 offset;
	vec2 size;
	float bold;
} fs_in;

uniform sampler2D tex;


const float width = 0.5;
const float edge = 0.1;

const float borderWidth = 0.5;
const float borderEdge = 0.4;
const vec3 outlineColour = vec3 (1.0,1.0,0.0);

const vec2 offset = vec2(0.002,0.002);

void main()
{
	vec2 loc = vec2(fs_in.offset + fs_in.tc*fs_in.size);
	float distance = 1.0 - texture(tex, loc).a;

	//for center charater
	float alpha = 1.0 - smoothstep(width, width + edge, distance);

	//for border
	float distance2 = 1.0 - texture(tex, loc + offset).a;
	float alphaedge = 0;
	if(fs_in.bold==1f){
		alphaedge = 1.0 - smoothstep(borderWidth, borderWidth + borderEdge, distance2);
	}
	


	float overallAlpha = alpha + (1.0 - alpha) * alphaedge;
	vec3 overallColour = mix(outlineColour, fs_in.col.rgb, overallAlpha);

	color.rgba = vec4(overallColour, overallAlpha);
}
