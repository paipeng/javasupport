Freemarker version : ${.version}

<#list props?keys as key>
	${key} = ${props[key]}
</#list>  

<#--
<#assign h = {"name":"mouse", "price":50}>
<#assign keys = h?keys>
<#list keys as key>${key} = ${h[key]}; </#list>  
-->
