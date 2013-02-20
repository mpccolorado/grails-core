package org.codehaus.groovy.grails.scaffolding

import grails.persistence.Event
import org.codehaus.groovy.grails.scaffolding.DomainClassPropertyComparator

class ScaffoldDomain {
	public static def getDomainProperties(domainClass, comparator){
		def excludedProps = Event.allEvents.toList() << 'id' << 'version'
		def allowedNames = domainClass.persistentProperties*.name << 'dateCreated' << 'lastUpdated'
		def props = domainClass.properties.findAll { allowedNames.contains(it.name) && !excludedProps.contains(it.name) && it.type != null && !Collection.isAssignableFrom(it.type) }
		Collections.sort(props, comparator.constructors[0].newInstance([domainClass] as Object[]))
		return props
	}
}
