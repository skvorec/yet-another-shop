<%@page import="org.yetanothershop.persistence.entities.SAttributeType"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Attr Manager</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/yash.css" />
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>
        <script type="text/javascript">
            var rootUrl = '${pageContext.request.contextPath}';
        </script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/yash.js"></script>
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
                    <div>
                        <c:forEach items="${objTypes}" var="objType">
                            <div>
                                <a class="need-confirm" href="${pageContext.request.contextPath}/admin/attrManager/deleteObjType?objtype=${objType.id}">
                                    <img height="15px" width="15px" src="${pageContext.request.contextPath}/img/delete.png"/>
                                </a>
                                <a href="${pageContext.request.contextPath}/admin/attrManager?objtype=${objType.id}"><c:out value="${objType.name}" /></a>
                            </div>
                        </c:forEach>
                    </div>
                </td>
                <td valign="top">
                    <c:if test="${not empty currentObjType}">
                        <div align="center"><span style="font-weight: bold;">Атрибуты сущности ${currentObjType.name}</span></div>
                        <br/>                    
                        <table border="1" cellpadding="0" cellspacing="0" width="100%">
                            <tr>
                                <th>Имя аттрибута</th>
                                <th>Тип аттрибута</th>
                                <th>На какой тип ссылка</th>
                            </tr>
                            <c:forEach items="${attrs}" var="attr">                                
                                <tr>
                                    <td>
                                        <a class="need-confirm" href="${pageContext.request.contextPath}/admin/attrManager/unbindAttr?objtype=${currentObjType.id}&attr=${attr.id}">
                                            <img height="15px" width="15px" src="${pageContext.request.contextPath}/img/unbind.png"/>
                                        </a>
                                        <c:out value="${attr.name}" />
                                    </td>
                                    <td>
                                        <c:out value="${attr.type}" />
                                    </td>
                                    <td>
                                        <c:out value="${attr.refObjectType.name}" />
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                        <div align="left"><span>Добавить существующий атрибут</span></div>
                        <form method="POST"
                              action="${pageContext.request.contextPath}/admin/attrManager/addAttribute" 
                              accept-charset="UTF-8">
                            <table border="0">
                                <tr valign="top">
                                    <td>Имя:</td>
                                    <td>
                                        <input name="attrName" type="text" size="10" class="attr-name-selector" autocomplete="off"/>
                                        <input name="attr" type="hidden" value="" class="attr-id-selector"/>
                                        <div align="left" class="autocomplete-container hidden"/>
                                    </td>                                    
                                    <td><input name="objtype" type="hidden" size="10" value="${currentObjType.id}"/></td>                                    
                                    <td><input type="submit" value="Добавить"/></td>
                                </tr>
                            </table>
                        </form>

                        <div align="left"><span>Создать новый атрибут</span></div>
                        <form method="POST"
                              action="${pageContext.request.contextPath}/admin/attrManager/newAttribute" 
                              accept-charset="UTF-8">
                            <table border="0">
                                <tr valign="top">
                                    <td>Имя:</td>
                                    <td><input name="attrName" type="text" size="10"/></td>
                                        <%
                                            pageContext.setAttribute("allAttrTypes", SAttributeType.values());
                                        %>
                                    <td>
                                        <select name="attrType">
                                            <option value="NONE">--- Select ---</option>
                                            <c:forEach items="${allAttrTypes}" var="attrType">
                                                <option value="${attrType}">${attrType}</option>
                                            </c:forEach>
                                        </select>                                            
                                    </td>
                                    <td><input name="objectTypeId" type="hidden" size="10" value="${currentObjType.id}"/></td>
                                    <td>
                                        <input name="refObjTypeIdName" type="text" size="10" class="obj-type-name-selector" autocomplete="off"/>
                                        <input name="refObjTypeId" type="hidden" value="" class="obj-type-id-selector"/>
                                        <div align="left" class="autocomplete-container hidden"/>
                                    </td>
                                    <td><input type="submit" value="Создать"/></td>
                                </tr>
                            </table>
                        </form>
                    </c:if>


                </td>
            </tr>
        </table>

    </body>
</html>
