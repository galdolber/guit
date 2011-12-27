<!DOCTYPE ui:UiBinder SYSTEM "http://dl.google.com/gwt/DTD/xhtml.ent">
<ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	xmlns:g="urn:import:com.google.gwt.user.client.ui" xmlns:a="urn:import:${package}.client.view"
	xmlns:c="urn:import:com.google.gwt.user.cellview.client">

<ui:style>
</ui:style>
<g:DockLayoutPanel>
	<g:north size="60">
		<g:HTMLPanel>
			<h1>${pojoName} list</h1>
		</g:HTMLPanel>
	</g:north>

	<g:north size="30">
		<g:HTMLPanel>
			<g:Button ui:field="create">Create</g:Button>
			<g:Button ui:field="edit">Edit</g:Button>
			<g:Button ui:field="delete">Delete</g:Button>
		</g:HTMLPanel>
	</g:north>

	<g:center>
		<g:ScrollPanel>
			<a:${pojoName}CellTable ui:field="table" width="100%"></a:${pojoName}CellTable>
		</g:ScrollPanel>
	</g:center>
            	
	<g:south size="30">
		<c:SimplePager location="LEFT" display="{table}"></c:SimplePager>
	</g:south>
</g:DockLayoutPanel> 
</ui:UiBinder>