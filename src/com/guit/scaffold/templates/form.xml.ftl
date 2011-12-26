<!DOCTYPE ui:UiBinder SYSTEM "http://dl.google.com/gwt/DTD/xhtml.ent">
<ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder" 
  xmlns:g="urn:import:com.google.gwt.user.client.ui"
  xmlns:d="urn:import:com.google.gwt.user.datepicker.client"
  xmlns:v="urn:import:${package}.client.view">

<ui:style>
</ui:style>

<g:ScrollPanel>
	<g:HTMLPanel>
		<h1>${pojoName}</h1>
        <#list fields as field>
        
		<label>${field.label}</label>
		<${field.tag} ui:field="${field.name}"></${field.tag}>
		<br/>	
        </#list>
        
		<g:Button ui:field="save">Save</g:Button>
		<g:Button ui:field="cancel">Cancel</g:Button>			
	</g:HTMLPanel>
</g:ScrollPanel>
</ui:UiBinder> 