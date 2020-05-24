#version 330 core

layout (location = 0) out vec4 color;

in DATA{
	vec2 tc;
} fs_in;

uniform sampler2D tex;
uniform sampler2D tex2;

uniform float dissolveAmount = 0;
uniform float outlineThickness = 0.08f;
uniform vec3 dissolveColor = vec3(1,0.4,0);

void main()
{
	float alpha = texture(tex2,fs_in.tc).r;
	alpha = step(dissolveAmount, alpha);

	//smoothstep function for the outline
	float outlineAlpha = texture(tex2,fs_in.tc).r;
	outlineAlpha = outlineAlpha-dissolveAmount;
	outlineAlpha = smoothstep(0,outlineThickness,outlineAlpha);
	if(outlineAlpha>=1){
		outlineAlpha = 0;
	}

	vec4 outlinecolor = vec4(dissolveColor, outlineAlpha*texture(tex,fs_in.tc).a);
	vec4 originalImage = vec4(texture(tex,fs_in.tc).rgb, alpha*texture(tex,fs_in.tc).a);

	if(outlineAlpha>0){
		color = outlinecolor *1.5f + originalImage *0.4f;
	}else{
		color = originalImage;
	}
}
