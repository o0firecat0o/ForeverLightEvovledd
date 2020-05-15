#version 330 core
#define PI 3.14159

layout (location = 0) out vec4 color;

in DATA{
	vec2 tc;
} fs_in;

uniform sampler2D tex;
uniform vec2[] veclist;


void main()
{
	 float effectRadius = .1;
	 float effectAngle = 2. * PI;

	 vec2 center = vec2(0.5,0.5) + veclist[1];

	 vec2 uv = fs_in.tc / vec2(1.0,1.0) - center;

	 float len = length(uv * vec2(1., 1.));
	 float angle = atan(uv.y, uv.x) + effectAngle * smoothstep(effectRadius, 0., len);
	 float radius = length(uv);

	color = texture(tex, vec2(radius * cos(angle), radius * sin(angle)) + center).rgba;
}
