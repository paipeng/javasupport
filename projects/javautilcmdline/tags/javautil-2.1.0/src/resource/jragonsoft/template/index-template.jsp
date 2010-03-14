<%--
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

Page redirect Sample
<jsp:forward page="home.jsp"/>
--%>

<html>
<head>
<title>${__RES_TEMPLATE_PROJECT_NAME}</title>
</head>
<body>

<h1>Welcome to ${__RES_TEMPLATE_PROJECT_NAME}</h1>
<p>How may I serve you today</p>

<form name="simpleForm" action=""/>
<table border="0" cellspacing="0" cellspanning="0">
	<tr bgcolor="#FFCC33"><td colspan="2" align="center">Simple Input Form</td></tr>
	<tr><td>Username</td><td><input type="text" name="user" value="thebugslayer" size="15"/></td></tr>
	<tr><td>Password</td><td><input type="password" name="date" value="thebugslayer" size="15"/></td></tr>
	<tr><td colspan="2" align="center"><input type="submit" name="action" value="Login"/></td></tr>
</table>
</form>
<br/>

<form name="fullForm" action=""/>
<table border="0" cellspacing="0" cellspanning="0">
	<tr bgcolor="#FFCC33"><td colspan="2" align="center">Full Input Form</td></tr>
	<tr><td>Username</td><td><input type="text" name="user" value="thebugslayer" size="15"/></td></tr>
	<tr><td>Password</td><td><input type="password" name="date" value="thebugslayer" size="15"/></td></tr>
	<tr><td>Check me</td>
		<td>
			<label accesskey="c"><input type="radio" name="payment_method" value="credit card" checked> credit card</label>
			<label accesskey="d"><input type="radio" name="payment_method" value="debit card"> debit card</label>
			<label accesskey="m"><input type="radio" name="payment_method" value="money order"> money order</label>
			<label accesskey="s"><input type="checkbox" name="send_receipt" value="yes" checked> send receipt by e-mail</label>
		</td>
	</tr>
	<tr><td>Upload file</td><td><input type="file" name="xmlfile" accept="text/xml"></td></tr>
	<tr><td>Selection</td>
		<td>
			<select name="foobar">
				<option value="none" selected="true">Choose one ...</option>
				<option value="fooX">fooX</option>
				<option value="barX">barX</option>
				<option value="fooY">fooY</option>
				<option value="barY">barY</option>
			</select>
		</td>
	</tr>
	<tr><td>Selection Type 2</td>
		<td>
			<select name=browser>
				<option value="none" selected="true">Choose one ...</option>
			   <optgroup label="netscape navigator">
			     <option label="4.x or higher">netscape navigator 4.x or higher</option>
			     <option label="3.x">netscape navigator 3.x</option>
			     <option label="2.x">netscape navigator 2.x</option>
			     <option label="1.x">netscape navigator 1.x</option>
			   </optgroup>
			   <optgroup label="microsoft internet explorer">
			     <option label="4.x or higher">microsoft internet explorer 4.x or higher</option>
			     <option label="3.x">microsoft internet explorer 3.x</option>
			     <option label="2.x">microsoft internet explorer 2.x</option>
			     <option label="1.x">microsoft internet explorer 1.x</option>
			   </optgroup>
			   <optgroup label="opera">
			     <option label="3.x or higher">opera 3.x or higher</option>
			     <option label="2.x">opera 2.x</option>
			   </optgroup>
			   <option>other</option>
		  	</select>
		</td>
	</tr>
	<tr><td>Comment</td><td><textarea name="comment" cols="40" rows="10">This is a comment input box.</textarea></td></tr>
	<tr>
		<td colspan="2" align="center">
			<input type="submit" name="action" value="Submit"/>
			<input type="reset" name="action" value="Reset"/>
		</td>
	</tr>
	<tr>
		<td colspan="2" align="center">
			<input type="button" name="action" value="Custom Action"/>
			<input type="button" name="action" value="Go go go"/>
		</td>
	</tr>
</table>
</form>
<br/>

<table border="0" cellspacing="2" cellspanning="0">
	<tr bgcolor="#CCCC99"><td colspan="2" align="center">Data Listing</td><tr>
	<tr bgcolor="#999999"><td>1.</td><td>Odd</td><tr>
	<tr bgcolor="#CCCCCC"><td>2.</td><td>Even</td><tr>
	<tr bgcolor="#999999"><td>3.</td><td>Odd</td><tr>
	<tr bgcolor="#CCCCCC"><td>4.</td><td>Even</td><tr>
</table>
<br/>

</body>
</html>
