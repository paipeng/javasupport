<jsp:directive.include file="../taglibs.jsp"/>
<div class="content">
    <h1>Register New User</h1>    
    <form:form commandName="user">
        <table>
            <tr><td>Username</td><td><form:input path="username" size="48"/><br/><form:errors cssClass="inputError" path="username"/></td></tr>
            <tr><td>Password</td><td><form:password path="password" size="48"/><br/><form:errors cssClass="inputError" path="password"/></td></tr>
            <tr><td>Email</td><td><form:input path="email" size="48"/><br/><form:errors cssClass="inputError" path="email"/></td></tr>
            <tr><td>Phone</td><td><form:input path="phone" size="48"/><br/><form:errors cssClass="inputError" path="phone"/></td></tr>
            <tr><td>First Name</td><td><form:input path="firstName" size="48"/><br/><form:errors cssClass="inputError" path="firstName"/></td></tr>
            <tr><td>Middle Name</td><td><form:input path="middleName" size="48"/><br/><form:errors cssClass="inputError" path="middleName"/></td></tr>
            <tr><td>Last Name</td><td><form:input path="lastName" size="48"/><br/><form:errors cssClass="inputError" path="lastName"/></td></tr>
            <tr><td>Address Line1</td><td><form:input path="addressLine1" size="48"/><br/><form:errors cssClass="inputError" path="addressLine1"/></td></tr>
            <tr><td>Address Line2</td><td><form:input path="addressLine2" size="48"/><br/><form:errors cssClass="inputError" path="addressLine2"/></td></tr>
            <tr><td>City</td><td><form:input path="city" size="48"/><br/><form:errors cssClass="inputError" path="city"/></td></tr>
            <tr><td>State</td><td><form:input path="state" size="48"/><br/><form:errors cssClass="inputError" path="state"/></td></tr>
            <tr><td>Zipcode</td><td><form:input path="zipcode" size="48"/><br/><form:errors cssClass="inputError" path="zipcode"/></td></tr>
            <tr><td colspan="2"><input type="submit" value="Save"/></td></tr>
        </table>
    </form:form>
    
    <p>
	<a href="${dollar}{pageContext.request.contextPath}/webapp/user/showProfile">Back to User Profile.</a>
	</p>
</div>

    