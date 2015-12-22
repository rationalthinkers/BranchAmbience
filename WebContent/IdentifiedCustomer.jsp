<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.branch.data.Customer" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>Branch - Customer Information</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link rel="stylesheet" href="style.css" />
</head>
<body>

<%
	Customer cust = (Customer) session.getAttribute("CustomerInContext");

%>

	<table>
		<tr>
			<td colspan="2">
				<h1>Hi <%=cust.getFirstName() %></h1>, your details
			</td>
		</tr>
		<tr>
			<td>
				Last Name
			</td>
			<td>
				<%=cust.getLastName() %>
			</td>
		</tr>
		<tr>
			<td>
				City
			</td>
			<td>
				<%=cust.getCity() %>
			</td>
		</tr>
	</table>

</body>
</html>