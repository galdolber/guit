Because big projects can be elegant when the right way is the fastest one

### About documentation

I'm migrating guit from google code, where there are a lot of old and ugly docs. Guit changed a lot from that. No new docs for now (sorry)

### Components

 * Command Service: easy server-client communication with autobaching, caching, server preprocessors, post/get/xs/websockets implementations and more
 * Place Manager: history management with automatic serialization, runasync and more
 * Guit Binder: mvp pattern implemented in a uibinder fashion with a testable dom abstraction, a plugin system, multi device support and more
 * Displays framework: an easy way to manage your screen containers
 * Google analytics built-in: one line integration with google analytics

### Goal

Aside from code elegancy and testing support, the main goal behind guit is to remove the need to write boilerplate code without limiting your coding abilities, and enforce good patterns that wont make you slow, but faster.

In short: **make you more productive**

### Code generation, the less you write the better!

To accomplish its goal it uses three types of code generation:

 * Gwt generators: this is code you will never see (i.e: static events binding)
 * Annotation processing: this ones generate a lot of code that you'll have to write otherwise, you can see the generated code and reference it while coding, but you don't have to maintain it
 * Static code generation: this last ones are still incomplete, the goal is to generate a start point for common situations (i.e cruds)

### Maven 

[https://clojars.org/guit]
<pre>
	&lt;dependency&gt;
	  &lt;groupId&gt;guit&lt;/groupId&gt;
	  &lt;artifactId&gt;guit&lt;/artifactId&gt;
	  &lt;version&gt;1.8.2&lt;/version&gt;
	&lt;/dependency&gt;
	
	&lt;dependency&gt;
	  &lt;groupId&gt;guit&lt;/groupId&gt;
	  &lt;artifactId&gt;guit-gen&lt;/artifactId&gt;
	  &lt;version&gt;1.8.2&lt;/version&gt;
	&lt;/dependency&gt;
	
	&lt;repository&gt;
		&lt;id&gt;clojars.org&lt;/id&gt;
		&lt;url&gt;http://clojars.org/repo&lt;/url&gt;
	&lt;/repository&gt;
</pre>