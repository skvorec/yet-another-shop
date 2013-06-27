<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Attr Manager</title>
    </head>
    <body>
        <!-- header -->
        <div style="width: 500px; background-color: lightblue;">
            <span style="font-size: 16px">Try to do manager</span>
        </div>
        <table border="1" cellpadding="0" cellspacing="0" width="100%">
            <tr>
                <td>
                    Типы сущностей<br/>                    
                    <form:form commandName="NameAware" action="${pageContext.request.contextPath}/admin/attrManager/newObjectType" >
                        <table>
                            <tr>
                                <td>Имя:<form:input path="name"/></td>
                                <td><input type="submit"/></td>
                            </tr>
                        </table>
                    </form:form>
                    <ul>
                        <c:forEach items="${ObjTypes}" var="objType">
                            <li><c:out value="${objType.name}" /></li>
                        </c:forEach>
                    </ul>
                </td>
                <td>
                    Атрибуты сущностей
                    <table border="1" cellpadding="0" cellspacing="0" width="100%">
                        <c:forEach items="${attrs}" var="attr">
                            <tr>
                                <td>
                                    <c:out value="${attr.name}" />
                                </td>
                                <td>
                                    <c:out value="${attr.type}" />
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                </td>
            </tr>
        </table>

    </body>
</html>
