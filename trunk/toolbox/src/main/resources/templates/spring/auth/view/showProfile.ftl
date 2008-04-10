<div class="content">
    <p>Want to logout? <a href="\${Application.contextPath}/webapp/auth/logout">Click here.</a></p>
    
    <h1>Your profile information</h1>  
    <table>
        <tr><td>Username</td><td>\${user.username}</td></tr>
        <tr><td>Email</td><td>\${user.email}</td></tr>
        <#--
        <tr><td>Phone</td><td>\${user.phone}</td></tr>
        <tr><td>First Name</td><td>\${user.firstName}</td></tr>
        <tr><td>Middle Name</td><td>\${user.middleName}</td></tr>
        <tr><td>Last Name</td><td>\${user.lastName}</td></tr>
        <tr><td>Address Line1</td><td>\${user.addressLine1}</td></tr>
        <tr><td>Address Line2</td><td>\${user.addressLine2}</td></tr>
        <tr><td>City</td><td>\${user.city}</td></tr>
        <tr><td>State</td><td>\${user.state}</td></tr>
        <tr><td>Zipcode</td><td>\${user.zipcode}</td></tr>
        -->
    </table>
    <p>Want to update? <a href="\${Application.contextPath}/webapp/auth/editProfile">Modify it here.</a></p>
</div>