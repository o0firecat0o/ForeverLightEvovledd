#version 330 core
#define PI 3.14159


layout (location = 0) out vec4 color;

in DATA{
	vec2 tc;
} fs_in;

uniform sampler2D tex;
uniform vec2 veclist[2];
const int veccount = 2;
uniform vec2 resolution;


void main()
{
	 float effectRadius = .2;
	 float effectAngle = 2. * PI;

	 vec2 uv = fs_in.tc;

	 for(int i = 0; i < veccount; i++){
		 uv -= veclist[i];
		 float len = length(uv * vec2(resolution.x/ resolution.y,1.));
	     float angle = atan(uv.y, uv.x) + effectAngle * smoothstep(effectRadius, 0., len);
		 float radius = length(uv);
		 uv = vec2(radius * cos(angle), radius * sin(angle))+ veclist[i];
	 }

	color = texture(tex, uv).rgba;
}
