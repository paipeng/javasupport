<jsp:directive.include file="../taglibs.jsp"/>
<div class="content">
    <h1>User Login</h1>
    
    <form:form commandName="user">
        <table>
        <tr><td>Username</td><td><form:input path="username"/><br/><form:errors cssClass="inputError" path="username"/></td></tr>
            <tr><td>Password</td><td><form:password path="password"/><br/><form:errors cssClass="inputError" path="password"/></td></tr>
            <tr><td colspan="3"><input type="submit" value="Login"/></td></tr>
        </table>
    </form:form>
    
    <p>Not a member yet? <a href="${dollar}{pageContext.request.contextPath}/webapp/user/create">Register here.</a></p>
</div>
