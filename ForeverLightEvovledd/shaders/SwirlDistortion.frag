#version 330 core
#define PI 3.14159


layout (location = 0) out vec4 color;

in DATA{
	vec2 tc;
} fs_in;

uniform sampler2D tex;
uniform vec4 veclist[50];
uniform float veccount;
uniform float scroll;
uniform vec2 resolution;


void main()
{


	 vec2 uv = fs_in.tc;

	 for(int i = 0; i < veccount; i++){
		 float effectRadius = veclist[i].z * scroll;
		 float effectAngle = veclist[i].w * PI;

		 uv -= veclist[i].xy;
		 float len = length(uv * vec2(resolution.x/ resolution.y,1.));
	     float angle = atan(uv.y, uv.x) + effectAngle * smoothstep(effectRadius, 0., len);
		 float radius = length(uv);
		 uv = vec2(radius * cos(angle), radius * sin(angle))+ veclist[i].xy;
	 }

	color = texture(tex, uv).rgba;
}
