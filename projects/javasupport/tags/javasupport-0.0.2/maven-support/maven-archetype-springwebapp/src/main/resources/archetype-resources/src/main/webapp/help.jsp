<jsp:directive.include file="taglibs.jsp"/>
<head>
    <style type="text/css">
        .content h1 {
            font-size:25px;
        }
    </style>
</head>
<div class="content">
    <h1>About</h1>
    <p>You are using ${dollar}{appInfo.fullBuildName}.</p>
    
    <h1>Navigations and Menus</h1>
    <p>The layout of this application has a top menu that will not change. This is
    done so the user can easily can go back to the top few main areas of the site.</p>
    
    <h1>System Information</h1>
    <p>This application comes with a SystemInfo page the shows the application 
        health status at anytime. The information presented contains many server parameters
    information that's best to secured if possible.</p>
    
    <h1>Feedback and Comments</h1>
    <p>If you have problems, suggestions, or any feedback please do contact our
    site admin and support team at ${artifactId} at ${groupId} dot com</p>    
</div>
