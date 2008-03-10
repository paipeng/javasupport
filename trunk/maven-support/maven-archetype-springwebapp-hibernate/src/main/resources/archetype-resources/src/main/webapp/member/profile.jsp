<jsp:directive.include file="../taglibs.jsp"/>
<div class="content">
    <p>Want to logout? <a href="${dollar}{pageContext.request.contextPath}/webapp/member/logout">Click here.</a></p>
    
    <h1>Your profile information</h1>  
    <table>
        <tr><td>Username</td><td>${dollar}{user.username}</td></tr>
        <tr><td>Email</td><td>${dollar}{user.email}</td></tr>
        <tr><td>Phone</td><td>${dollar}{user.phone}</td></tr>
        <tr><td>First Name</td><td>${dollar}{user.firstName}</td></tr>
        <tr><td>Middle Name</td><td>${dollar}{user.middleName}</td></tr>
        <tr><td>Last Name</td><td>${dollar}{user.lastName}</td></tr>
        <tr><td>Address Line1</td><td>${dollar}{user.addressLine1}</td></tr>
        <tr><td>Address Line2</td><td>${dollar}{user.addressLine2}</td></tr>
        <tr><td>City</td><td>${dollar}{user.city}</td></tr>
        <tr><td>State</td><td>${dollar}{user.state}</td></tr>
        <tr><td>Zipcode</td><td>${dollar}{user.zipcode}</td></tr>
    </table>
    <p>Want to update? <a href="${dollar}{pageContext.request.contextPath}/webapp/member/edit">Modify it here.</a></p>
</div>