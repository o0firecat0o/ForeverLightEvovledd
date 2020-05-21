#version 330 core

layout (location = 0) in vec4 position;
layout (location = 1) in vec2 tc;

layout (location = 2) in mat4 modelViewMatrix;
layout (location = 6) in vec4 col;
layout (location = 7) in vec2 offset;
layout (location = 8) in vec2 size;
layout (location = 9) in float bold;

uniform mat4 pr_matrix;
uniform mat4 vw_matrix = mat4(1.0);

out DATA{
	vec2 tc;
	vec4 col;
	vec2 offset;
	vec2 size;
	float bold;
} vs_out;

void main(){
	gl_Position = pr_matrix * vw_matrix * modelViewMatrix * position;
	vs_out.tc = tc;
	vs_out.col = col;
	vs_out.offset = offset;
	vs_out.size = size;
	vs_out.bold = bold;
}
