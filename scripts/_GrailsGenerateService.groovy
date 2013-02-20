/*
 * Copyright 2004-2005 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.codehaus.groovy.grails.commons.GrailsDomainClass;
import org.codehaus.groovy.grails.scaffolding.*

import grails.util.GrailsNameUtils

/**
 * Gant script that generates a service for a given domain class
 *
 * @author Martín Caballero
 *
 */

includeTargets << grailsScript("_GrailsBootstrap")
includeTargets << new File("$userInterfacePluginDir/scripts/_Utils.groovy")

generateForName = null

target(generateServiceForOne: "Generates service for only one domain class.") {
    depends(loadApp)

    def name = generateForName
    name = name.indexOf('.') > 0 ? name : GrailsNameUtils.getClassNameRepresentation(name)
    def domainClass = grailsApp.getDomainClass(name)

    if (!domainClass) {
        grailsConsole.updateStatus "Domain class not found in grails-app/domain, trying hibernate mapped classes..."
        bootstrap()
        domainClass = grailsApp.getDomainClass(name)
    }

    if (domainClass) {
        generateServiceForDomainClass(domainClass)
        event("StatusFinal", ["Finished generation for domain class ${domainClass.fullName}"])
    }
    else {
        event("StatusFinal", ["No domain class found for name ${name}. Please try again and enter a valid domain class name"])
        exit(1)
    }
}

def generateServiceForDomainClass(domainClass) {
    def templateGenerator = new DefaultGrailsTemplateGenerator(classLoader)
    templateGenerator.grailsApplication = grailsApp
    templateGenerator.pluginManager = pluginManager

    event("StatusUpdate", ["Generating service for domain class ${domainClass.fullName}"])
    templateGenerator.generateService(domainClass, basedir)
    event("GenerateServiceEnd", [domainClass.fullName])
}
