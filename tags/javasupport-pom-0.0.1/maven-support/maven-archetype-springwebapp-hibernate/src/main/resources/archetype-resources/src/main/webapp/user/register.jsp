<jsp:directive.include file="../taglibs.jsp"/>
<div class="content">
    <h1>Register New User</h1>    
    <form:form commandName="user">
        <table>
            <tr><td>Username</td><td><form:input path="username" size="48"/><br/><form:errors cssClass="inputError" path="username"/></td></tr>
            <tr><td>Password</td><td><form:password path="password" size="48"/><br/><form:errors cssClass="inputError" path="password"/></td></tr>
            <tr><td>Email</td><td><form:input path="email" size="48"/><br/><form:errors cssClass="inputError" path="email"/></td></tr>
            <tr><td colspan="2"><input type="submit" value="Register"/></td></tr>
        </table>
    </form:form>
</div>

    