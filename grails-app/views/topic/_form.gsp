<div class="control-group ${hasErrors(bean: topicInstance, field: 'title', 'error')} required">
    <label class="control-label" for="topic.title">
        <strong><g:message code="topic.title.label"/></strong>
        <span class="required-indicator">*</span>
    </label>
    <div class="controls">
        <g:textField name="topic.title" value="${topicInstance?.title}" placeholder="${message(code:'topic.title.label')}" />
    </div>
</div>

<div class="control-group ${hasErrors(bean: topicInstance, field: 'enabled', 'error')}">
    <label class="control-label">
        <strong><g:message code="topic.state.label"/></strong>
    </label>
    <div class="controls">
        <label class="checkbox" for="topic.enabled" id="thesis-enabled">
            <g:checkBox name="topic.enabled" value="${topicInstance?.enabled}" />
            <g:message code="topic.enabled.label"/>
        </label>
    </div>
</div>

<div class="control-group ${hasErrors(bean: topicInstance, field: 'secondaryTitle', 'error')}">
    <label class="control-label" for="topic.secondaryTitle">
        <strong><g:message code="topic.secondaryTitle.label"/></strong>
    </label>
    <div class="controls">
        <g:textField name="topic.secondaryTitle" value="${topicInstance?.secondaryTitle}" placeholder="${message(code:'topic.secondaryTitle.label')}" />
    </div>
</div>

<div class="control-group ${hasErrors(bean: topicInstance, field: 'owner', 'error')} required">
    <label class="control-label" for="topic.owner.fullName">
        <strong><g:message code="role.owner.label"/></strong>
        <span class="required-indicator">*</span>
    </label>
    <div class="controls">
        <g:hiddenField name="topic.owner.id" value="${topicInstance?.owner?.id}"/>
        <g:hiddenField name="a4g-role" value="${com.redhat.theses.auth.Role.OWNER}"/>
        <a4g:textField name="topic.owner.fullName" value="${topicInstance?.owner?.fullName}"
                       data-autocomplete-url="${createLink(controller: 'json', action: 'listUsersByNameAndRole')}"
                       data-autocomplete-target="topic.owner.id"
                       data-autocomplete-opts="a4g-role@role"
                       placeholder="${message(code:'role.owner.label')}" />
    </div>
</div>

<div class="control-group ${hasErrors(bean: topicInstance, field: 'universities', 'error')}">
    <label class="control-label">
        <strong><g:message code="topic.universities.label"/></strong>
    </label>
    <div class="controls">
        <richg:multiselect name="topic.universities" from="${universities}"
                           optionKey="id" value="${topicInstance?.universities*.id}" />
    </div>
</div>



<div class="control-group ${hasErrors(bean: topicInstance, field: 'types', 'error')}">
    <label class="control-label">
        <strong><g:message code="topic.types.label"/></strong>
    </label>
    <div class="controls">
        <richg:multiselect name="topic.types" from="${types}" value="${topicInstance?.types}"
                           optionValue="${{g.message(code:"topic.type.${it?.toString()?.toLowerCase()}.label")}}"/>
    </div>
</div>

<div class="control-group">
    <label class="control-label" for="supervison-list">
        <strong><g:message code="topic.supervision.label"/></strong>
    </label>
    <div class="controls">
        <g:render template="supervision"/>
    </div>
</div>

<div class="control-group ${hasErrors(bean: topicInstance, field: 'lead', 'error')} required">
    <label class="control-label" for="topic.lead">
        <strong><g:message code="topic.lead.label"/></strong>
        <span class="required-indicator">*</span>
    </label>
    <div class="controls">
        <g:textArea name="topic.lead" rows="5" value="${topicInstance?.lead}"/>
    </div>
</div>

<div class="control-group ${hasErrors(bean: topicInstance, field: 'description', 'error')} required">
    <div class="small-msg pull-right">
        <i class="icon-info-sign"></i>
        <g:message code="field.markdown.label" />
    </div>
	<label class="control-label" for="topic.description">
		<strong><g:message code="topic.description.label"/></strong>
        <span class="required-indicator">*</span>
	</label>
    <div class="controls">
	   <g:textArea name="topic.description" rows="15" value="${topicInstance?.description}"/>
    </div>
</div>

<div class="control-group ${hasErrors(bean: topicInstance, field: 'secondaryDescription', 'error')}">
    <div class="small-msg pull-right">
        <i class="icon-info-sign"></i>
        <g:message code="field.markdown.label" />
    </div>
    <label class="control-label" for="topic.secondaryDescription">
        <strong><g:message code="topic.secondaryDescription.label"/></strong>
    </label>
    <div class="controls">
        <g:textArea name="topic.secondaryDescription" rows="15" value="${topicInstance?.secondaryDescription}"/>
    </div>
</div>

<div class="control-group ${hasErrors(bean: topicInstance, field: 'categories', 'error')}">
    <label class="control-label" for="topic.categories">
        <strong><g:message code="topic.categories.label"/></strong>
    </label>
    <div class="controls">
        <richg:multiselect name="topic.categories" from="${com.redhat.theses.Category.list()}"
                  optionKey="id" value="${topicInstance?.categories*.id}"/>
    </div>
</div>

<div class="control-group">
    <label class="control-label" for="topic.tags.title">
        <strong><g:message code="topic.tags.label"/></strong>
    </label>
    <div class="controls">
        <g:select id="topic-tags" name="topic.tags.title" multiple="multiple"
                  from="${topicInstance?.tags}" value="${topicInstance?.tags}"/>
        <script type="text/javascript">$('#topic-tags').taggy();</script>
    </div>
</div>
