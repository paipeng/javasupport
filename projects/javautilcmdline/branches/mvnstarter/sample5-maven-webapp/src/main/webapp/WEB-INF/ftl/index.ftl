<#assign h = {"name":"mouse", "price":50}>
<#assign keys = h?keys>

<html>
<body>
<h2>Hello World!</h2>

<#list keys as key>${key} = ${h[key]};<br/></#list>

</body>
</html>
