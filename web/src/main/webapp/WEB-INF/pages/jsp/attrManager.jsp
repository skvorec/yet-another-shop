<%@page import="org.yetanothershop.persistence.entities.SAttributeType"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
        <div align="center">
            <div style="height: 50px; width: 100%; background-color: lightblue;">
                <span style="font-size: 16px; font-weight: bold;">Управление типами сущностей</span>
            </div>
        </div>
        <table align="center" border="1" cellpadding="0" cellspacing="0" width="100%">
            <tr>
                <td valign="top" width="400px">
                    <div align="center"><span style="font-weight: bold;">Типы сущностей</span></div>
                    <br/>
                    <div align="left"><span>Создать новый тип</span></div>
                    <form:form commandName="NameAware" 
                               action="${pageContext.request.contextPath}/admin/attrManager/newObjectType" 
                               accept-charset="UTF-8">
                        <table>
                            <tr>
                                <td>Имя:</td>
                                <td><form:input path="name"/></td>
                                <td><input type="submit" value="Создать"/></td>
                            </tr>
                        </table>
                    </form:form>
                    <ul>
                        <c:forEach items="${objTypes}" var="objType">
                            <li><a href="${pageContext.request.contextPath}/admin/attrManager?objtype=${objType.id}"><c:out value="${objType.name}" /></a></li>
                        </c:forEach>
                    </ul>
                </td>
                <td valign="top">
                    <div align="center"><span style="font-weight: bold;">Атрибуты сущностей</span></div>
                    <br/>                    
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
                        <c:if test="${not empty param.objtype}">
                            <div align="left"><span>Создать новый атрибут</span></div>
                            <form:form commandName="AttrCreation" 
                                       action="${pageContext.request.contextPath}/admin/attrManager/newAttribute" 
                                       accept-charset="UTF-8">
                                <table border="0">
                                    <tr>
                                        <td>Имя:</td>
                                        <td><form:input path="name"/></td>
                                        <%
                                            pageContext.setAttribute("allAttrTypes", SAttributeType.values());
                                        %>
                                        <td><form:select path="attrType">
                                                <form:option value="NONE" label="--- Select ---"/>
                                                <form:options items="${allAttrTypes}" />
                                            </form:select>
                                        </td>
                                        <form:hidden path="objectTypeId"/>
                                        <td><input type="submit" value="Создать"/></td>
                                    </tr>
                                </table>
                            </form:form>
                        </c:if>

                    </table>
                </td>
            </tr>
        </table>

    </body>
</html>
