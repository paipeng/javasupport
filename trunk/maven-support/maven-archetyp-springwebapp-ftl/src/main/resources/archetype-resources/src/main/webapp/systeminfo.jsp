<jsp:directive.include file="taglibs.jsp"/>
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
            <tr><td>Application Version</td><td>${dollar}{appInfo.fullBuildName}</td></tr>
            <tr><td>Application Uptime</td><td>${dollar}{appUptime}</td></tr>
            <tr><td>Server Time</td><td>${dollar}{systime}</td></tr>
            <tr><td>Server Info</td><td>${dollar}{serverInfo}</td></tr>
            <tr><td>System Resources</td><td>${dollar}{sysres}</td></tr>
        </table>
    </div>
    
    <h1 class="testA">Application Properties</h1>
    <div class="wrappedContent">
        <table>
            <c:forEach var="entry" items="${dollar}{applicationProps}">
                <tr><td>${dollar}{entry.key}</td><td>${dollar}{entry.value}</td></tr>
            </c:forEach>
        </table>
    </div>
    
    <h1>System Environment</h1>
    <div class="wrappedContent">
        <table>
            <c:forEach var="entry" items="${dollar}{sysenv}">
                <tr><td>${dollar}{entry.key}</td><td>${dollar}{entry.value}</td></tr>
            </c:forEach>
        </table>
    </div>
    
    <h1>System Properties</h1>
    <div class="wrappedContent">
        <table>
            <c:forEach var="entry" items="${dollar}{sysprops}">
                <tr><td>${dollar}{entry.key}</td><td>${dollar}{entry.value}</td></tr>
            </c:forEach>
        </table>
    </div>
</div>
