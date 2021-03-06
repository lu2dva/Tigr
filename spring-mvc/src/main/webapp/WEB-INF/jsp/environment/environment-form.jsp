<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../init.jspf" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<tigr:crud-template nav="environments">
<jsp:attribute name="content">
    <form:form modelAttribute="data" method="post" action="${continueLink}" cssClass="environment-form">

        <div class="form-group">
            <label for="name"> <b><spring:message
                    code="tigr-message-species-name"/></b></label>
            <form:input path="name" class="form-control"/>
        </div>
        <div class="form-group">
            <label for="description"><spring:message code="tigr-message-species-description"/></label>
            <form:input path="description" class="form-control"/>
        </div>


        <div class="btn-group" role="group">
            <a class="btn btn-default" href="/pa165/environments">
                <spring:message code="tigr-message-back"/>
            </a>
            <button type="submit" class="btn btn-success">
                <spring:message code="${buttonLabelCode}"/>
            </button>
        </div>
        <form:hidden path="id"/>
    </form:form>

</jsp:attribute>
</tigr:crud-template>

<spring:message var="errorNameRequired" code="tigr-message-species-error-name-required"/>
<spring:message var="errorNameLength" code="tigr-message-species-error-name-minlength"/>
<spring:message var="errorDescRequired" code="tigr-message-species-error-desc"/>
<script>
    $().ready(function () {
        $(".environment-form").validate({
            rules: {
                name: {
                    required: true
                },
                description: {
                    required: true
                }
            },
            messages: {
                name: {
                    required: "${errorNameRequired}",
                    minlength: "${errorNameLength}"
                },
                description: {
                    required: "${errorDescRequired}"
                }
            }
        });
    });
</script>