<head>
    <style type="text/css">
        .content {
            height: auto;
        }
    </style>
</head>
<div class="content">
    <h1 class="testA">Application Status Info</h1>
    <div class="wrappedContent">
        <table>
            <tr><td>Application Version</td><td>${dollar}{Application.appInfo.fullBuildName}</td></tr>
            <tr><td>Application Uptime</td><td>${dollar}{Request.appUptime}</td></tr>
            <tr><td>Server Time</td><td>${dollar}{Request.systime?datetime}</td></tr>
            <tr><td>Server Info</td><td>${dollar}{Application.serverInfo}</td></tr>
            <tr><td>System Resources</td><td>${dollar}{Request.sysres}</td></tr>
        </table>
    </div>
    
    <h1>Application Properties</h1>
    <div class="wrappedContent">
        <table>
            <${pound}list Request.applicationProps?keys as key>
                <tr><td>${dollar}{key}</td><td>${dollar}{Request.applicationProps[key]}</td></tr>
            </#list>
        </table>
    </div>
    <h1>System Environment</h1>
    <div class="wrappedContent">
        <table>
            <${pound}list Request.sysenv?keys as key>
                <tr><td>${dollar}{key}</td><td>${dollar}{Request.sysenv[key]}</td></tr>
            </#list>
        </table>
    </div>
    
    <h1>System Properties</h1>
    <div class="wrappedContent">
        <table>
            <${pound}list Request.sysprops?keys as key>
                <tr><td>${dollar}{key}</td><td>${dollar}{Request.sysprops[key]}</td></tr>
            </#list>
        </table>
    </div>
</div>
